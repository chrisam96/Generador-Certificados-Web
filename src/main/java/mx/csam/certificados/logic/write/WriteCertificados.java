package mx.csam.certificados.logic.write;

import java.io.FileWriter;
import java.io.IOException;
import java.math.BigInteger;
import java.security.InvalidAlgorithmParameterException;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.PrivateKey;
import java.security.Security;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.security.spec.ECGenParameterSpec;
import java.security.spec.MGF1ParameterSpec;
import java.security.spec.PSSParameterSpec;
import java.time.Instant;
import java.time.OffsetDateTime;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.IllegalFormatException;
import java.util.List;
import java.util.Map;

import org.bouncycastle.asn1.ASN1Encodable;
import org.bouncycastle.asn1.ASN1Enumerated;
import org.bouncycastle.asn1.ASN1GeneralizedTime;
import org.bouncycastle.asn1.ASN1Integer;
import org.bouncycastle.asn1.ASN1ObjectIdentifier;
import org.bouncycastle.asn1.DERSequence;
import org.bouncycastle.asn1.DERUTF8String;
import org.bouncycastle.asn1.pkcs.PKCSObjectIdentifiers;
import org.bouncycastle.asn1.x500.X500Name;
import org.bouncycastle.asn1.x509.Extension;
import org.bouncycastle.asn1.x509.ExtensionsGenerator;
import org.bouncycastle.asn1.x509.GeneralName;
import org.bouncycastle.asn1.x509.GeneralNames;
import org.bouncycastle.cert.X509CertificateHolder;
import org.bouncycastle.cert.jcajce.JcaX509CertificateConverter;
import org.bouncycastle.cert.jcajce.JcaX509v3CertificateBuilder;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.bouncycastle.jce.spec.ECNamedCurveGenParameterSpec;
import org.bouncycastle.openssl.PKCS8Generator;
import org.bouncycastle.openssl.jcajce.JcaPEMWriter;
import org.bouncycastle.openssl.jcajce.JcaPKCS8Generator;
import org.bouncycastle.openssl.jcajce.JceOpenSSLPKCS8EncryptorBuilder;
import org.bouncycastle.operator.ContentSigner;
import org.bouncycastle.operator.OperatorCreationException;
import org.bouncycastle.operator.OutputEncryptor;
import org.bouncycastle.operator.jcajce.JcaContentSignerBuilder;
import org.bouncycastle.pkcs.PKCS10CertificationRequest;
import org.bouncycastle.pkcs.PKCS10CertificationRequestBuilder;
import org.bouncycastle.pkcs.jcajce.JcaPKCS10CertificationRequestBuilder;
import org.bouncycastle.util.io.pem.PemGenerationException;
import org.bouncycastle.util.io.pem.PemObject;

import mx.csam.certificados.utilerias.constantes.CertificadosUtils;

public class WriteCertificados {
	
	public WriteCertificados() {
		
	}
	
	/**
	 * Metódo que generará un Certificado AutoFirmado dado 
	 * un algoritmo asimetrico dado.
	 * 
	 * @param Algoritmo asimetrico escogido para la geneación de llaves pares
	 * @throws Exception
	 * @return {@link X509Certificate}
	 * */
	public Object[] generarCertificadoOrCsr
		(
		String algoritmo, //Algoritmo Asimetrico para generar el Certificado 
		String[] titularDN, //Datos del titular del Certificado 
		String[] emisorDN, //Datos del Emisor (quién firma el certificado) ---> .Cer + .key	
		
		String[] fecha_vig_y_serial,//Fechas de inicio de vigencia		
		String[] datosKPG, //Datos para la generación de las LLaves Publica y Privada
		boolean CerOrCsr, //Bandera para diferenciar entre Cer/CSR
		
		Object[] datosPKCifrado, //Datos para cifrar o no la PrivateKey		
		Map<String, String[]> _SAN //Datos del Subjective Alternative Names ---> .csr
		//String[] ruta,//Ruta del archivo doonde se guardará el .cer/.key o .csr
		) 
		throws Exception {

		//Por defecto, se agrega el Provider de BouncyCastle a la configuracion del Certificado
		Security.addProvider(new BouncyCastleProvider());
		
		Object[] arr = generarCuerpoDeCertificadoOrCsr(algoritmo, titularDN, datosKPG);
		
		X500Name x500 = (X500Name) arr[0];
		KeyPair keyPair = (KeyPair) arr[1];
		ContentSigner firmador = (ContentSigner) arr[2];
		
		Object arch = null;//Recibira el .cer/.csr a escribir
		String extension = "";
		
		//Objs. relac. a PrivateKey cifrada.
		JcaPKCS8Generator pkCifrada = null;
			// 0: Bandera si se cifra (true) o no(false)
		boolean cifrado = Boolean.valueOf(datosPKCifrado[0].toString());
				
		//true = .cer (Certificado), false = .csr (CSR)
		if(CerOrCsr) {
			arch = generarCertificado(x500, keyPair, firmador, emisorDN, fecha_vig_y_serial);
			extension = ".cer";
			
			/* Datos para cifrar
			 * 0: Booleano para determinar si se cifra (true) o no(false)
			 * 1: String con el ALGORITMO SIMETRICO para cifrar
			 * 2: String para la contraseña usada para cifrar la PrivateKey
			 * */			
			if(cifrado){
				String algCifradoPK = String.valueOf(datosPKCifrado[1]); 
				String passCifrado = String.valueOf(datosPKCifrado[2]); 
				pkCifrada = cifrarPrivateKey(keyPair.getPrivate(), algCifradoPK, passCifrado);
			}
			
		} else {
			arch = generarCSR(x500, keyPair, firmador, _SAN);
			extension = ".csr";
		}
				
		long subfijo = System.currentTimeMillis();
		
		//*
		escritorDeArchivos(arch, "certificado_"+subfijo, extension);
		if(cifrado) {
			escritorDeArchivos(pkCifrada, "PrivadaCifrada_"+subfijo, ".key");
			//Crear archivo de password
			escritorDeArchivos(
				passwordArchivoCifrado(
					(String) datosPKCifrado[1],
					(String) datosPKCifrado[2]
				), 
				"passPrivadaCifrado_"+subfijo, 
				".txt"
			);			
		}else {
			escritorDeArchivos(keyPair.getPrivate(), "Privada_"+subfijo, ".key");
		}
		escritorDeArchivos(keyPair.getPublic(), "publica_"+subfijo, ".der");
		//*/
		Object[] resultado = new Object[4];
		resultado[0] = arch;//Object: el certificado en si
		resultado[1] = extension;//String: la extension del archivo del certificado
		resultado[2] = subfijo;//String: un nombre
		resultado[3] = keyPair;//KeyPair: Llaves Privada y Publica
		
		return resultado;
	} 
	


	/**
	 * Metódo que generará los datos internos necesarios para 
	 * crear ya sea un Certificado AutoFirmado o un CSR 
	 * dado un algoritmo asimetrico dado, un tamaño de clave si fuera necesario
	 * y un algoritmo SHA para el firmado.
	 * 
	 * @param Algoritmo asimetrico escogido para la geneación de llaves pares
	 * @throws Exception
	 * @return {@link X509Certificate}
	 * */
	public Object[] generarCuerpoDeCertificadoOrCsr
	(String algoritmo, String[] datosDN, String[] datosKPG) 
			throws Exception {
		
		//Por defecto, se agrega el Provider de BouncyCastle a la configuracion del Certificado
		Security.addProvider(new BouncyCastleProvider());
		
		//DN = Distinguised Name
		String distName = concatenarContenidoCertificado(datosDN);
		
		//Se asignan los datos
		// CN = Nombre del Titular			
		// O = Organizacion+
		// OU = Unidad Organizacional o subdivision de la organizacion
		// L = Ciudad o Localidad
		// ST = Estado o Provincia
		// C = Codigo de 2 letras del Pais
		
		//Todos ellos en conjunto se llaman Distingished Name (DN) 
		X500Name x500 = new X500Name(distName);
		
		//Genera  un KPG a partir de algún algoritmo
		KeyPair keyPair = generaKeyPair(algoritmo, datosKPG);		
		
		//Cuando Firmas un certificado con X509v3CertificateBuilder, usas un ContentSigner. 
		JcaContentSignerBuilder jcaContentSigner = generaJcaContentSigner(algoritmo, datosKPG);
		ContentSigner firmador = jcaContentSigner.build(keyPair.getPrivate());
		
		/*
		X509v3CertificateBuilder certBuilder = new JcaX509v3CertificateBuilder(
	        dn,        			// issuer: quién firma el certificado
	        serial,    			// serialNumber: número de serie
	        notBefore, 			// startDate: inicio de validez
	        notAfter,  			// endDate: fin de validez
	        dn,        			// subject: a quién pertenece (igual que issuer si es auto-firmado)
	        keyPair.getPublic() // publicKey: clave pública del sujeto
		);
		 */
		
		Object[] arr =  new Object[3];
		arr[0] = x500;
		arr[1] = keyPair;
		arr[2] = firmador;
		
		return arr;
	} 
	
	

	/**
	 * Metódo que generará las llaves publicas(public) y privadas(private) dado 
	 * un algoritmo asimetrico dado.<p>
	 * 
	 * <h4>Combinaciones de Hashes y Curvas segun Algoritmo Asimetrico</h4>
	 * <p>
	 * <h5>RSA</h5> 
	 * <br>
	 * Básicamente todos los tamaños de Clave son compatibles con 
	 * todas las variaciones de Hash.<p>
	 * <br>
	 * 
	 * <table border="1">
	 *   <!-- <caption>Hashes permitidos históricamente para RSA</caption> -->
	 *   <thead>
	 *     <tr>
	 *       <th>Tamaño de clave RSA</th>
	 *       <th>Hash permitido históricamente</th>
	 *     </tr>
	 *   </thead>
	 *   <tbody>
	 *     <tr>
	 *       <td>1024, 2048, 3072, 4096, 6144, 8192</td>
	 *       <td>
	 *         MD2, MD5, SHA-1 (160 bits), SHA-224, SHA-256,
	 *         SHA-384, SHA-512, SHA-512/224, SHA-512/256,
	 *         SHA3-256, SHA3-384, SHA3-512
	 *       </td>
	 *     </tr>
	 *   </tbody>
	 * </table>
	 *
	 * <br>
	 * nota: Basicamente, todos con todos
	 *  
	 * <h5>RSA-PSS</h5>
	 * 
	 * <p>A pesar de que se puede combinar todos los tamaños de clave
	 * (tamñao de bits) con los algoritmos SHA hay restricciones en
	 * sobre el tamaño de la sal que se puede llegar a usar:
	 * <br>
	 * <br>
	 * <table border="1">
	 *   <!-- <caption>Hashes permitidos históricamente para RSA</caption> -->
	 *   <thead>
	 *     <tr>
	 *       <th>Tamaño de clave RSA</th>
	 *       <th>Hash permitido históricamente</th>
	 *     </tr>
	 *   </thead>
	 *   <tbody>
	 *     <tr>
	 *       <td>1024, 2048, 3072, 4096, 6144, 8192</td>
	 *       <td>
	 *         MD2, MD5, SHA-1 (160 bits), SHA-224, SHA-256,
	 *         SHA-384, SHA-512, SHA-512/224, SHA-512/256,
	 *         SHA3-256, SHA3-384, SHA3-512
	 *       </td>
	 *     </tr>
	 *   </tbody>
	 * </table>
	 *
	 * <p>La combinación del tamaño del hash (SHA), tamaño de clave y de la Sal
	 * obedece a cierta condición para hacer válida criptográficamente en RSA-PSS.
	 * Debe cumplirse esta condición de PKCS #1:
	 * <blockquote><pre>
	 * emLen >= hLen + sLen + 2
	 * </blockquote></pre>
	 * donde:
	 * <blockquote><pre>
	 * emLen = longitud codificada disponible del módulo RSA (en bytes)
	 * hLen = tamaño del hash del algoritmo SHA (en bytes)
	 * sLen = El tamaño de la Sal (en bytes)
	 * </blockquote></pre>
	 * <blockquote><pre>
	 *  Para obtener "emLen":
	 *  
	 *  	emLen = redondeoHaciaArriba( (tamClaveBits - 1 ) / 8 )
	 *  
	 *  donde:
	 *  
	 *  	tamClaveBits = Tamaño de la Clave elegida, en BITS. Normalmente son
	 *  					valores de 1024, 2048, 3072, 4096, 6144, 8192
	 *  
	 *  Para obtener "hLen":
	 *  
	 *  	hLen = Longitud SHA-XXX / 8; 
	 *  
	 *  donde:
	 *  
	 *  	Longitud SHA-XXX = Es el tamaño del algoritmo SHA (en BITS). Suelen ser 
	 *  					   valores fijos, tales como SHA-256=256, SHA-512=512, etc. 
	 *  					   Al estar en bits es necesario pasarlos a bytes para hacer
	 *  					   el calculo correctamente (1 byte = 8 BITS). 
	 *  
	 * De las operaciones anteriores, podemos deducir la fórmula que marcará cuál
	 * sea el límite máximo de la Sal (en bytes):
	 * 
	 * 		maxSaltLen = emLen - hLen - 2
	 *
	 * Así que el máximo de la Sal depende de dos factores: EL tamaño de la Clave y
	 * el tamaño de hash SHA elegido.
	 * 
	 * DSA
	 * 
	 * Hay restricciones en tamaños aprobados históricamente por pares L/N 
	 * concretos:
	 * 
	 * 
	 *	| ------------------- | ----------------------------- |
	 * 	| Tamaño de clave DSA | Hash permitido históricamente |
	 *	| ------------------- | ----------------------------- |
	 *	| 1024                | SHA-1 (160 bits)              |
	 *	| ------------------- | ----------------------------- |
	 *	| 2048                | SHA-224, SHA-256              |
	 *	| ------------------- | ----------------------------- |
	 *	| 3072                | SHA-256                       |
	 *	| ------------------- | ----------------------------- |
	 *
	 * ojo: DSA no va con 4096 ni 8192 como opción estándar habitual en certificados; 
	 * lo normal es quedarse en los tamaños estándar anteriores. En caso de exceder
	 * esos tamaños de clave, el algoritmo internamente solo usará los primeros bits
	 * hasta completar 2048 o 3072 según sea el SHA elegido. 
	 *
	 *
	 * EC/ECDSA
	 *  
	 * Cada curva elíptica es fija y no parámetrica, es decir, que no se pueden 
	 * cambiar los datos sobre ella, ya que estas necesitan definir:
	 * 		ecuación matemática
	 * 		campo finito
	 * 		punto generador (G)
	 * 		orden del grupo (n)
	 * 		cofactor (h)
	 * 
	 * Cambiar algo como “256” o “r1” implicaría cambiar TODO. No se puede cambiar 
	 * un parámetro simple y ya.
	 * 
	 * NOTA:
	 * - Se instancia su KeyPairGenerator con un "EC". 
	 * 
	 * - Se firma con "SHAxxxwithECDSA" en el constructor de JcaContentSignerBuilder, 
	 * donde "xxx" puede ser 256, 384, etc., en base a la curva elegida.
	 * 
	 * - Requiere agregar el Provider de BouncyCastle mediante setProvider() del 
	 * JcaContentSignerBuilder.
	 * 
	 * ojo: No requiere un tamaño de bits para inicializar nada.
	 * 
	 * 
	 * GOST
	 * 
	 * Son 2 generaciones dentro de esta sub familia estructurada de curvas, donde a
	 * pesar de compartir un mismo tipo no se pueden combinar, todo tiene que ser tanto
	 * de la misma generación como del mismo tamaño SHA
	 * 
	 * Se instancia su KeyPairGenerator eligiendo uno de los Algorimtmo de Clave:
	 * 		ECGOST3410 		(viejo)
	 * 		ECGOST3410-2012 (nuevo)
	 * 
	 * Se inicializa mediante un objeto de tipo ECNamedCurveGenParameterSpec, 
	 * al cual se le necesita elegir una de las Curvas de parámetros:
	 * 
	 * 		GostR3410-2001-CryptoPro-A 		(viejo)
	 * 		Tc26-Gost-3410-12-256-paramSetA (nuevo)
	 * 
	 * De forma desglosada:
	 * 		GOST 2001			(viejo)
	 * 
	 * 		GostR3410-2001-CryptoPro-A
	 * 		GostR3410-2001-CryptoPro-B
	 * 		GostR3410-2001-CryptoPro-C
	 * 
	 * 		GOST 2012 (256)		(nuevo)
	 * 		
	 * 		Tc26-Gost-3410-12-256-paramSetA
	 * 		Tc26-Gost-3410-12-256-paramSetB
	 * 		Tc26-Gost-3410-12-256-paramSetC
	 * 		Tc26-Gost-3410-12-256-paramSetD
	 * 
	 * 		GOST 2012 (512)		(nuevo)
	 * 
	 * 		Tc26-Gost-3410-12-512-paramSetA
	 * 		Tc26-Gost-3410-12-512-paramSetB
	 * 		Tc26-Gost-3410-12-512-paramSetC
	 * 
	 * Para firmar el certificado se tiene que elegir uno de los Algoritmos de 
	 * Firma:
	 * 
	 * 		GOST 2001	(viejo)
	 * 			GOST3411withECGOST3410
	 * 
	 * 		GOST 2012	(nuevo)
	 * 			GOST3411-2012-256withECGOST3410-2012-256
	 * 			GOST3411-2012-256WITHECGOST3410-2012-512
	 * 
	 * ojo:  
	 * - No se pueden combinar tamaños diferencres de hash SHA y de Curva
	 * 
	 * - Requiere agregar el Provider de BouncyCastle mediante setProvider() 
	 * del JcaContentSignerBuilder.
	 * 
	 * Ed25519 y Ed448
	 * 
	 * Se instancia su KeyPairGenerator con un "Ed25519" o "Ed448".
	 * 
	 * Se firma con "Ed25519" o "Ed448" en el constructor de JcaContentSignerBuilder,
	 * 
	 * Requiere agregar el Provider de BouncyCastle mediante setProvider() 
	 *  	del  JcaContentSignerBuilder.
	 * 
	 * ojo: No piden curva, tamaño de clave (en bits), ni hash explícito: 
	 * el algoritmo lleva la definición interna.
	 * </blockquote></pre>
	 * 
	 * @param alg Algoritmo asimetrico escogido para la geneación de llaves pares
	 * @param datosKPG Los datos
	 * @return KeyPair
	 * @throws InvalidAlgorithmParameterException 
	 * @throws NoSuchProviderException 
	 * @throws IllegalArgumentException
	 * @throws NoSuchProviderException
	 * */
	//public KeyPair generaKeyPair(String alg, String tamClaveBits_o_curvaInit, String tamSHA_o_genCurva, String salt) 
	public KeyPair generaKeyPair(String alg, String[] datosKPG) 
			throws NoSuchAlgorithmException, IllegalArgumentException, InvalidAlgorithmParameterException, NoSuchProviderException {

		//Por defecto, se agrega el Provider de BouncyCastle a la configuracion del Certificado
		Security.addProvider(new BouncyCastleProvider());
		
		/* 
		 * Los parametros del arreglo datosKPG son nombrados de la sig. forma:
		 * 
		 * [0] tamClaveBits_o_curvaInit
		 * [1] tamSHA_o_genCurva
		 * [2] salt
		 * 
		 * Dichos nombres de los parametros fueron elaborados en base a la tabla
		 * de abajo. En la primera columna van los algoritmos y las demás columnas
		 * son para saber si dicho algoritmo requiere 1, 2 o 3 parámetros. 
		 * 
		 * En cada columna de Param "X" viene el nombre que se le dió al dato 
		 * (parametro) que requiere cada algoritmo.  
		 * --------------------------------------------------------
		 * ALGORITMO	|Param 1		|Param 2		|Param 3
		 * --------------------------------------------------------
		 * RSA-PSS 		|Tam Clave BITS	|Tam Hash SHA	|Salt
		 * EC			|Curva Init		|				|
		 * GOST			|Curva Init 	|Gen Curva		|	 
		 * RSA			|Tam Clave BITS	|				|
		 * DSA			|Tam Clave BITS	|				|
		 * ---------------------------------------------------------
		*/ 
		
		String tamClaveBits_o_curvaInit = datosKPG[0]; 
		String tamSHA_o_genCurva = datosKPG[1];
		String salt = datosKPG[2];
		
		//Genera  un KPG a partir de algún algoritmo
		KeyPairGenerator kpg =  null;				
		
		/*
		 * Combinaciones de Hashes y Curvas segun Algoritmo Asimetrico
		 * 
		 * RSA
		 * 
		 * Básicamente todos los tamaños de Clave son compatibles con 
		 * todas las variaciones de Hash
		 * 
		 *	| ------------------- | ----------------------------- |
		 * 	| Tamaño de clave RSA | Hash permitido históricamente |
		 *	| ------------------- | ----------------------------- |
		 *	| 1024, 2048,         | MD2, MD5, SHA-1 (160 bits)    |
		 *	| 3072, 4096,         | SHA-224, SHA-256, SHA-384,    |
		 *	| 6144, 8192          | SHA-512, SHA-512/224,         |
		 *	|                     | SHA3-512/256, SHA3-256,       |
		 *	|                     | SHA3-384, SHA3-512            |
		 *	-------------------------------------------------------			
		 * 
		 * nota: Basicamente, todos con todos
		 *  
		 * RSA-PSS
		 * 
		 * Ha pesar de que se puede combinar todos los tamaños de clave
		 * (tamñao de bits) con los algoritmos SHA hay restricciones en
		 * sobre el tamaño de la sal que se puede llegar a usar
		 * 
		 *	| ------------------- | ----------------------------- |
		 * 	| Tamaño de clave RSA | Hash permitido históricamente |
		 *	| ------------------- | ----------------------------- |
		 *	| 1024, 2048,         | MD2, MD5, SHA-1 (160 bits)    |
		 *	| 3072, 4096,         | SHA-224, SHA-256, SHA-384,    |
		 *	| 6144, 8192          | SHA-512, SHA-512/224,         |
		 *	|                     | SHA3-512/256, SHA3-256,       |
		 *	|                     | SHA3-384, SHA3-512            |
		 *	-------------------------------------------------------	
		 *
		 * La combinación del tamaño del hash (SHA), tamaño de clave y de la Sal
		 * obedece a cierta condición para hacer válida criptográficamente en RSA-PSS.
		 * Debe cumplirse esta condición de PKCS #1:
		 *
		 *		emLen >= hLen + sLen + 2
		 *	
		 *	donde:
		 *	
		 *		emLen = longitud codificada disponible del módulo RSA (en bytes)
		 *		hLen = tamaño del hash del algoritmo SHA (en bytes)
		 *		sLen = El tamaño de la Sal (en bytes)
		 * 
		 *  Para obtener "emLen":
		 *  
		 *  	emLen = redondeoHaciaArriba( (tamClaveBits - 1 ) / 8 )
		 *  
		 *  donde:
		 *  
		 *  	tamClaveBits = Tamaño de la Clave elegida, en BITS. Normalmente son
		 *  					valores de 1024, 2048, 3072, 4096, 6144, 8192
		 *  
		 *  Para obtener "hLen":
		 *  
		 *  	hLen = Longitud SHA-XXX / 8; 
		 *  
		 *  donde:
		 *  
		 *  	Longitud SHA-XXX = Es el tamaño del algoritmo SHA (en BITS). Suelen ser 
		 *  					   valores fijos, tales como SHA-256=256, SHA-512=512, etc. 
		 *  					   Al estar en bits es necesario pasarlos a bytes para hacer
		 *  					   el calculo correctamente (1 byte = 8 BITS). 
		 *  
		 * De las operaciones anteriores, podemos deducir la fórmula que marcará cuál
		 * sea el límite máximo de la Sal (en bytes):
		 * 
		 * 		maxSaltLen = emLen - hLen - 2
		 *
		 * Así que el máximo de la Sal depende de dos factores: EL tamaño de la Clave y
		 * el tamaño de hash SHA elegido.
		 * 
		 * DSA
		 * 
		 * Hay restricciones en tamaños aprobados históricamente por pares L/N 
		 * concretos:
		 * 
		 * 
		 *	| ------------------- | ----------------------------- |
		 * 	| Tamaño de clave DSA | Hash permitido históricamente |
		 *	| ------------------- | ----------------------------- |
		 *	| 1024                | SHA-1 (160 bits)              |
		 *	| ------------------- | ----------------------------- |
		 *	| 2048                | SHA-224, SHA-256              |
		 *	| ------------------- | ----------------------------- |
		 *	| 3072                | SHA-256                       |
		 *	| ------------------- | ----------------------------- |
		 *
		 * ojo: DSA no va con 4096 ni 8192 como opción estándar habitual en certificados; 
		 * lo normal es quedarse en los tamaños estándar anteriores. En caso de exceder
		 * esos tamaños de clave, el algoritmo internamente solo usará los primeros bits
		 * hasta completar 2048 o 3072 según sea el SHA elegido. 
		 *
		 *
		 * EC/ECDSA
		 *  
		 * Cada curva elíptica es fija y no parámetrica, es decir, que no se pueden 
		 * cambiar los datos sobre ella, ya que estas necesitan definir:
		 * 		ecuación matemática
		 * 		campo finito
		 * 		punto generador (G)
		 * 		orden del grupo (n)
		 * 		cofactor (h)
		 * 
		 * Cambiar algo como “256” o “r1” implicaría cambiar TODO. No se puede cambiar 
		 * un parámetro simple y ya.
		 * 
		 * NOTA:
		 * - Se instancia su KeyPairGenerator con un "EC". 
		 * 
		 * - Se firma con "SHAxxxwithECDSA" en el constructor de JcaContentSignerBuilder, 
		 * donde "xxx" puede ser 256, 384, etc., en base a la curva elegida.
		 * 
		 * - Requiere agregar el Provider de BouncyCastle mediante setProvider() del 
		 * JcaContentSignerBuilder.
		 * 
		 * ojo: No requiere un tamaño de bits para inicializar nada.
		 * 
		 * 
		 * GOST
		 * 
		 * Son 2 generaciones dentro de esta sub familia estructurada de curvas, donde a
		 * pesar de compartir un mismo tipo no se pueden combinar, todo tiene que ser tanto
		 * de la misma generación como del mismo tamaño SHA
		 * 
		 * Se instancia su KeyPairGenerator eligiendo uno de los Algorimtmo de Clave:
		 * 		ECGOST3410 		(viejo)
		 * 		ECGOST3410-2012 (nuevo)
		 * 
		 * Se inicializa mediante un objeto de tipo ECNamedCurveGenParameterSpec, 
		 * al cual se le necesita elegir una de las Curvas de parámetros:
		 * 
		 * 		GostR3410-2001-CryptoPro-A 		(viejo)
		 * 		Tc26-Gost-3410-12-256-paramSetA (nuevo)
		 * 
		 * De forma desglosada:
		 * 		GOST 2001			(viejo)
		 * 
		 * 		GostR3410-2001-CryptoPro-A
		 * 		GostR3410-2001-CryptoPro-B
		 * 		GostR3410-2001-CryptoPro-C
		 * 
		 * 		GOST 2012 (256)		(nuevo)
		 * 		
		 * 		Tc26-Gost-3410-12-256-paramSetA
		 * 		Tc26-Gost-3410-12-256-paramSetB
		 * 		Tc26-Gost-3410-12-256-paramSetC
		 * 		Tc26-Gost-3410-12-256-paramSetD
		 * 
		 * 		GOST 2012 (512)		(nuevo)
		 * 
		 * 		Tc26-Gost-3410-12-512-paramSetA
		 * 		Tc26-Gost-3410-12-512-paramSetB
		 * 		Tc26-Gost-3410-12-512-paramSetC
		 * 
		 * Para firmar el certificado se tiene que elegir uno de los Algoritmos de 
		 * Firma:
		 * 
		 * 		GOST 2001	(viejo)
		 * 			GOST3411withECGOST3410
		 * 
		 * 		GOST 2012	(nuevo)
		 * 			GOST3411-2012-256withECGOST3410-2012-256
		 * 			GOST3411-2012-256WITHECGOST3410-2012-512
		 * 
		 * ojo:  
		 * - No se pueden combinar tamaños diferencres de hash SHA y de Curva
		 * 
		 * - Requiere agregar el Provider de BouncyCastle mediante setProvider() 
		 * del JcaContentSignerBuilder.
		 * 
		 * Ed25519 y Ed448
		 * 
		 * Se instancia su KeyPairGenerator con un "Ed25519" o "Ed448".
		 * 
		 * Se firma con "Ed25519" o "Ed448" en el constructor de JcaContentSignerBuilder,
		 * 
		 * Requiere agregar el Provider de BouncyCastle mediante setProvider() 
		 *  	del  JcaContentSignerBuilder.
		 * 
		 * ojo: No piden curva, tamaño de clave (en bits), ni hash explícito: 
		 * el algoritmo lleva la definición interna.
		 * */
		
		//Inicializa  el KPG con un cierto tamaño de Key (Clave)
		kpg = switch(alg) {
			case String a when a.equals("RSA") -> {
				//
				kpg = KeyPairGenerator.getInstance(alg);
				kpg.initialize(Integer.parseInt(tamClaveBits_o_curvaInit));
				yield kpg;
			}
			case String a when a.equals("RSA-PSS") -> {
				//
				if (!CertificadosUtils.validarSalt_TamSHA_ClaveBits(tamSHA_o_genCurva, tamClaveBits_o_curvaInit, salt) ) {
					throw new IllegalArgumentException("""							
					Los valores del tamaño de la Clave, del Hash SHA y de la sal no son válidos en conjunto.
					La combinación debe cumplir la siguiente formular:
							(tamaño Clave / 8) >= (tamaño SHA / 8) + tamaño Sal + 2;
					""");
				}
				kpg = KeyPairGenerator.getInstance("RSA");
				kpg.initialize(Integer.parseInt(tamClaveBits_o_curvaInit));
				yield kpg; 
			}
			case String a when a.equals("DSA") -> {
				kpg = KeyPairGenerator.getInstance(alg);
				int tamClave = Integer.parseInt(tamClaveBits_o_curvaInit); 

				if(!CertificadosUtils.validarEnDSAMinMaxTamClave(tamClaveBits_o_curvaInit)) {
					throw new IllegalArgumentException("""
						El tamaño o no es un numero o supera el minimo/maximo.
						El tamaño de inicializacion no puede superar los 3072,
						ni ser inferior a 1024.
						""");
				}
				
				if(!CertificadosUtils.validarEnDSA_HashSHA_TamClave(tamSHA_o_genCurva, tamClaveBits_o_curvaInit)) {
					throw new IllegalArgumentException("""
						El tamaño no corresponde a los tamaños aprobados históricamente por 
						pares L/N concretos.
						""");
				}
				
				kpg.initialize(tamClave);
				yield kpg;				
			}
			case String a when a.equals("EC") -> {
				//
				kpg =KeyPairGenerator.getInstance(alg, "BC");
				kpg.initialize(new ECGenParameterSpec(tamClaveBits_o_curvaInit));
				yield kpg;
			}
			case String a when a.equals("GOST") -> {
				//
				kpg = KeyPairGenerator.getInstance(tamSHA_o_genCurva, "BC");
				kpg.initialize(new ECNamedCurveGenParameterSpec(tamClaveBits_o_curvaInit));
				yield kpg;
			}
			case String a when a.equals("Ed25519") -> {
				//
				kpg = KeyPairGenerator.getInstance(alg, "BC");
				yield kpg;
			}
			case String a when a.equals("Ed448") -> {
				//
				kpg = KeyPairGenerator.getInstance(alg, "BC");
				yield kpg;
			}
			default -> {
				throw new IllegalArgumentException("""
					El valor minimo para el algoritmo de DSA no puede ser menor a 2048
					""");
			}
		};
		
		//Genera el par de claves Pública y Privada
		return kpg.generateKeyPair();
	}

	/**
	 * Metódo que generará las llaves publicas(public) y privadas(private) dado 
	 * un algoritmo asimetrico dado.<p>
	 * 
	 * @param alg Algoritmo asimetrico escogido para la geneación de llaves pares
	 * @param datosKPG Los datos
	 * @return KeyPair
	 * @throws InvalidAlgorithmParameterException 
	 * @throws NoSuchProviderException 
	 * @throws IllegalArgumentException
	 * @throws NoSuchProviderException
	 * */	
	public JcaContentSignerBuilder generaJcaContentSigner(String alg, String[] datosKPG) 
			throws NoSuchAlgorithmException, IllegalArgumentException, InvalidAlgorithmParameterException, NoSuchProviderException {
		
		//Por defecto, se agrega el Provider de BouncyCastle a la configuracion del Certificado
		Security.addProvider(new BouncyCastleProvider());
		
		String tamClaveBits_o_curvaInit = datosKPG[0]; 
		String tamSHA_o_genCurva = datosKPG[1];
		String salt = datosKPG[2];

		/* 
		 * Los parametros del arreglo datosKPG son nombrados de la sig. forma:
		 * 
		 * [0] tamClaveBits_o_curvaInit
		 * [1] tamSHA_o_genCurva
		 * [2] salt
		 * 
		 * Cuando creas un certificado con X509v3CertificateBuilder, usas un ContentSigner. 
		 * El algoritmo que le pasas debe corresponder al tipo de clave:

			RSA → "SHA256withRSA", ("SHA384withRSA", etc.) "SHA1withRSA", "MD2withRSA", "MD5withRSA"
			ECDSA → "SHA256withECDSA" (o "SHA384withECDSA")
			Ed25519 → "Ed25519" (sin prefijo SHA)
			Ed448 → "Ed448" (sin prefijo SHA)
			DSA → "SHA256withDSA", "SHA1withDSA"
			GOST -> "GOST3411withECGOST3410" (viejo) o "GOST3411-2012-256withECGOST3410-2012[-256/512]" (moderno)
			RSA-PSS -> SHAxxxwithRSAandMGF1 
		 */	
		
		//Genera un JcaContentSignerBuilder personalizado a partir de algún algoritmo
		JcaContentSignerBuilder contentSigner =  null;						
		
		//String con la plantilla de la firma
		String firma = CertificadosUtils.plantilla_algoritmo_para_firmar();//"[XXX]with[YYY][ZZZ]"
		String[]  placeholders =  {"[XXX]", "[YYY]", "[ZZZ]" };
		
		/*
		 * Combinaciones de Hashes y Curvas segun Algoritmo Asimetrico
		 * 
		 * RSA		 * 
		 * 
		 * Básicamente todos los tamaños de Clave son compatibles con 
		 * todas las variaciones de Hash
		 * 
		 * 	| Tamaño de clave RSA | Hash permitido históricamente |
		 *	| ------------------- | ----------------------------- |
		 *	| 1024, 2048,         | MD2, MD5, SHA-1 (160 bits)    |
		 *	| 3072, 4096,         | SHA-224, SHA-256, SHA-384,    |
		 *	| 6144, 8192          | SHA-512, SHA-512/224,         |
		 *	|                     | SHA3-512/256, SHA3-256,       |
		 *	|                     | SHA3-384, SHA3-512            |
		 *	-------------------------------------------------------			
		 * 
		 * nota: Basicamente, todos con todos
		 *  
		 * RSA-PSS
		 * 
		 * Ha pesar de que se puede combinar todos los tamaños de clave
		 * (tamñao de bits) con los algoritmos SHA hay restricciones en
		 * sobre el tamaño de la sal que se puede llegar a usar
		 * 
		 * 	| Tamaño de clave RSA | Hash permitido históricamente |
		 *	| ------------------- | ----------------------------- |
		 *	| 1024, 2048,         | MD2, MD5, SHA-1 (160 bits)    |
		 *	| 3072, 4096,         | SHA-224, SHA-256, SHA-384,    |
		 *	| 6144, 8192          | SHA-512, SHA-512/224,         |
		 *	|                     | SHA3-512/256, SHA3-256,       |
		 *	|                     | SHA3-384, SHA3-512            |
		 *	-------------------------------------------------------	
		 *
		 * La combinación del tamaño del hash (SHA), tamaño de clave y de la Sal
		 * obedece a cierta condición para hacer válida criptográficamente en RSA-PSS.
		 * Debe cumplirse esta condición de PKCS #1:
		 *
		 *		emLen >= hLen + sLen + 2
		 *	
		 *	donde:
		 *	
		 *		emLen = longitud codificada disponible del módulo RSA (en bytes)
		 *		hLen = tamaño del hash del algoritmo SHA (en bytes)
		 *		sLen = El tamaño de la Sal (en bytes)
		 * 
		 *  Para obtener "emLen":
		 *  
		 *  	emLen = redondeoHaciaArriba( (tamClaveBits - 1 ) / 8 )
		 *  
		 *  donde:
		 *  
		 *  	tamClaveBits = Tamaño de la Clave elegida, en BITS. Normalmente son
		 *  					valores de 1024, 2048, 3072, 4096, 6144, 8192
		 *  
		 *  Para obtener "hLen":
		 *  
		 *  	hLen = Longitud SHA-XXX / 8; 
		 *  
		 *  donde:
		 *  
		 *  	Longitud SHA-XXX = Es el tamaño del algoritmo SHA (en BITS). Suelen ser 
		 *  					   valores fijos, tales como SHA-256=256, SHA-512=512, etc. 
		 *  					   Al estar en bits es necesario pasarlos a bytes para hacer
		 *  					   el calculo correctamente (1 byte = 8 BITS). 
		 *  
		 * De las operaciones anteriores, podemos deducir la fórmula que marcará cuál
		 * sea el límite máximo de la Sal (en bytes):
		 * 
		 * 		maxSaltLen = emLen - hLen - 2
		 *
		 * Así que el máximo de la Sal depende de dos factores: EL tamaño de la Clave y
		 * el tamaño de hash SHA elegido.
		 * 
		 * Se firma con "SHAXXXwithRSAandMGF1" y un PSSParameterSpec en el constructor del 
		 * JcaContentSignerBuilder, 
		 * 		Donde "xxx" puede ser 256, 384, etc.
		 * 
		 * El objeto PSSParameterSpec requiere de 5 parametros:
		 * 		
		 * 		PSSParameterSpec spec = new PSSParameterSpec(
		 * 			"SHA-256", 	 				// Hash principal
		 * 			"MGF1", 	 	 			// Función de Generación de Máscara (MGF)
		 * 			MGF1ParameterSpec.SHA256, 	// Hash de MGF1
		 * 			32, 		 	 			// Tamaño del salt (bytes)
		 * 			1 		 	 				// trailer field (Siempre 1 en la práctica)
		 * 		);
		 * 
		 * Ojo: Solo se permite cambiar los parámetros del Hash Principal, Hash del MGF1 y
		 * el tamaño de la sal.
		 * 
		 * DSA
		 * 
		 * Hay restricciones en tamaños aprobados históricamente por pares L/N 
		 * concretos:
		 * 
		 * 
		 *	| ------------------- | ----------------------------- |
		 * 	| Tamaño de clave DSA | Hash permitido históricamente |
		 *	| ------------------- | ----------------------------- |
		 *	| 1024                | SHA-1 (160 bits)              |
		 *	| ------------------- | ----------------------------- |
		 *	| 2048                | SHA-224, SHA-256              |
		 *	| ------------------- | ----------------------------- |
		 *	| 3072                | SHA-256                       |
		 *	| ------------------- | ----------------------------- |
		 *
		 * ojo: DSA no va con 4096 ni 8192 como opción estándar habitual en certificados; 
		 * lo normal es quedarse en los tamaños estándar anteriores. En caso de exceder
		 * esos tamaños de clave, el algoritmo internamente solo usará los primeros bits
		 * hasta completar 2048 o 3072 según sea el SHA elegido. 
		 *
		 *
		 * EC/ECDSA
		 *  
		 * Cada curva elíptica es fija y no parámetrica, es decir, que no se pueden 
		 * cambiar los datos sobre ella, ya que estas necesitan definir:
		 * 		ecuación matemática
		 * 		campo finito
		 * 		punto generador (G)
		 * 		orden del grupo (n)
		 * 		cofactor (h)
		 * 
		 * Cambiar algo como “256” o “r1” implicaría cambiar TODO. No se puede cambiar 
		 * un parámetro simple y ya.
		 * 
		 * NOTA:
		 * - Se instancia su KeyPairGenerator con un "EC". 
		 * 
		 * - Se firma con "SHAxxxwithECDSA" en el constructor de JcaContentSignerBuilder, 
		 * donde "xxx" puede ser 256, 384, etc., en base a la curva elegida.
		 * 
		 * - Requiere agregar el Provider de BouncyCastle mediante setProvider() del 
		 * JcaContentSignerBuilder.
		 * 
		 * ojo: No requiere un tamaño de bits para inicializar nada.
		 * 
		 * 
		 * GOST
		 * 
		 * Son 2 generaciones dentro de esta sub familia estructurada de curvas, donde a
		 * pesar de compartir un mismo tipo no se pueden combinar, todo tiene que ser tanto
		 * de la misma generación como del mismo tamaño SHA
		 * 
		 * Se instancia su KeyPairGenerator eligiendo uno de los Algorimtmo de Clave:
		 * 		ECGOST3410 		(viejo)
		 * 		ECGOST3410-2012 (nuevo)
		 * 
		 * Se inicializa mediante un objeto de tipo ECNamedCurveGenParameterSpec, 
		 * al cual se le necesita elegir una de las Curvas de parámetros:
		 * 
		 * 		GostR3410-2001-CryptoPro-A 		(viejo)
		 * 		Tc26-Gost-3410-12-256-paramSetA (nuevo)
		 * 
		 * De forma desglosada:
		 * 		GOST 2001			(viejo)
		 * 
		 * 		GostR3410-2001-CryptoPro-A
		 * 		GostR3410-2001-CryptoPro-B
		 * 		GostR3410-2001-CryptoPro-C
		 * 
		 * 		GOST 2012 (256)		(nuevo)
		 * 		
		 * 		Tc26-Gost-3410-12-256-paramSetA
		 * 		Tc26-Gost-3410-12-256-paramSetB
		 * 		Tc26-Gost-3410-12-256-paramSetC
		 * 		Tc26-Gost-3410-12-256-paramSetD
		 * 
		 * 		GOST 2012 (512)		(nuevo)
		 * 
		 * 		Tc26-Gost-3410-12-512-paramSetA
		 * 		Tc26-Gost-3410-12-512-paramSetB
		 * 		Tc26-Gost-3410-12-512-paramSetC
		 * 
		 * Para firmar el certificado se tiene que elegir uno de los Algoritmos de 
		 * Firma:
		 * 
		 * 		GOST 2001	(viejo)
		 * 			GOST3411withECGOST3410
		 * 
		 * 		GOST 2012	(nuevo)
		 * 			GOST3411-2012-256withECGOST3410-2012-256
		 * 			GOST3411-2012-256WITHECGOST3410-2012-512
		 * 
		 * ojo:  
		 * - No se pueden combinar tamaños diferencres de hash SHA y de Curva
		 * 
		 * - Requiere agregar el Provider de BouncyCastle mediante setProvider() 
		 * del JcaContentSignerBuilder.
		 * 
		 * Ed25519 y Ed448
		 * 
		 * Se instancia su KeyPairGenerator con un "Ed25519" o "Ed448".
		 * 
		 * Se firma con "Ed25519" o "Ed448" en el constructor de JcaContentSignerBuilder,
		 * 
		 * Requiere agregar el Provider de BouncyCastle mediante setProvider() 
		 *  	del  JcaContentSignerBuilder.
		 * 
		 * ojo: No piden curva, tamaño de clave (en bits), ni hash explícito: 
		 * el algoritmo lleva la definición interna.
		 * */
		
		//Inicializa  el KPG con un cierto tamaño de Key (Clave)
		contentSigner = switch(alg) {
			case String a when a.equals("RSA") -> {
				//
				firma = firma.
						replace(placeholders[0], tamSHA_o_genCurva.replace("-","")); //Agrega SHAxxx
				firma = firma.
						replace(placeholders[1], alg); //Agrega el algoritmo
				firma = firma.
						replace(placeholders[2], ""); //Elimina el [ZZZ]
				
				//RSA → "SHA256withRSA", ("SHA384withRSA", etc.) "SHA1withRSA", "MD2withRSA", "MD5withRSA"
				contentSigner = new JcaContentSignerBuilder(firma);
				yield contentSigner;
			}
			case String a when a.equals("RSA-PSS") -> {
			//
				if (!CertificadosUtils.validarSalt_TamSHA_ClaveBits(tamSHA_o_genCurva, tamClaveBits_o_curvaInit, salt) ) {
					throw new IllegalArgumentException("""							
						Los valores del tamaño de la Clave, del Hash SHA y de la sal no son válidos en conjunto.
						La combinación debe cumplir la siguiente formula:
								(tamaño Clave / 8) >= (tamaño SHA / 8) + tamaño Sal + 2;
						""");
				}
				
				MGF1ParameterSpec mgf1 = CertificadosUtils.listaPSSParameterSpec().get(tamSHA_o_genCurva);
				if (mgf1 == null) {
					throw new IllegalArgumentException("""
						No sé permiten los algoritmos ni MD2 ni MD5 tanto su uso en el PSSParameterSpec
						como en la firma del Content-Signer.
						""");
				}
				
				//MGF = Mask Generation Function
				PSSParameterSpec pssSpec =  new PSSParameterSpec(
						tamSHA_o_genCurva, 		// Hash principal
						"MGF1",					// Algoritmo de Función de generación de máscara (MGF) 
						mgf1,					//Hash del MGF
						Integer.parseInt(salt),	//Longitud de Salt
						1						// Trailer Field
					);
				
				
				firma = firma.
						replace(placeholders[0], tamSHA_o_genCurva.replace("-","")); //Agrega SHAxxx
				firma = firma.
						replace(placeholders[1], "RSA"); //Agrega el algoritmo
				firma = firma.
						replace(placeholders[2], "andMGF1"); //Elimina el [ZZZ]				
				
				//RSA-PSS -> SHAxxxwithRSAandMGF1
				contentSigner =  new JcaContentSignerBuilder("RSA", pssSpec);
				yield contentSigner; 
			}
			case String a when a.equals("DSA") -> {
				
				if(!CertificadosUtils.validarEnDSAMinMaxTamClave(tamClaveBits_o_curvaInit)) {
					throw new IllegalArgumentException("""
						El tamaño o no es un numero o supera el minimo/maximo.
						El tamaño de inicializacion no puede superar los 3072,
						ni ser inferior a 1024
						""");
				}			
				
				if(!CertificadosUtils.validarEnDSA_HashSHA_TamClave(tamSHA_o_genCurva, tamClaveBits_o_curvaInit)) {
					throw new IllegalArgumentException("""
						El tamaño no corresponde a los tamaños aprobados históricamente por 
						pares L/N concretos.
						""");
				}			
				
				firma = firma.
						replace(placeholders[0], tamSHA_o_genCurva.replace("-","")); //Agrega SHAxxx
				firma = firma.
						replace(placeholders[1], alg); //Agrega el algoritmo
				firma = firma.
						replace(placeholders[2], ""); //Elimina el [ZZZ]
				
				//DSA → "SHA256withDSA", "SHA1withDSA"
				contentSigner =  new JcaContentSignerBuilder(firma);
				yield contentSigner;				
			}
			case String a when a.equals("EC") -> {
				//ECDSA → "SHA256withECDSA" (o "SHA384withECDSA")
				
				firma = firma.
						replace(placeholders[0], CertificadosUtils.firmaEnEC(tamClaveBits_o_curvaInit)); //Agrega SHAxxx
				firma = firma.
						replace(placeholders[1], "ECDSA"); //Agrega el algoritmo
				firma = firma.
						replace(placeholders[2], ""); //Elimina el [ZZZ]
				
				contentSigner = new JcaContentSignerBuilder(firma);
				yield contentSigner;
			}
			case String a when a.equals("GOST") -> {
				//GOST -> "GOST3411withECGOST3410" (viejo) o "GOST3411-2012-256withECGOST3410-2012[-256/512]" (moderno)				
				contentSigner = new JcaContentSignerBuilder(
					CertificadosUtils.firmaEnGOST(tamSHA_o_genCurva, tamClaveBits_o_curvaInit)
				);
				yield contentSigner;
			}
			case String a when a.equals("Ed25519") -> {
				//Ed25519 → "Ed25519" (sin prefijo SHA)
				contentSigner = new JcaContentSignerBuilder(alg);
				yield contentSigner;
			}
			case String a when a.equals("Ed448") -> {
				//Ed448 → "Ed448" (sin prefijo SHA)
				contentSigner = new JcaContentSignerBuilder(alg);
				yield contentSigner;
			}
			default -> {
				throw new IllegalArgumentException("""
						El valor minimo para el algoritmo de DSA no puede ser menor a 2048
						""");
			}
		};
		
		System.out.println("generaJcaContentSigner() - "+ firma);
		
		//Se asigna el proveedor mediante un String
		contentSigner = contentSigner.setProvider("BC");
		
		//Devuela el objeto de la firma creado a partir de la clave Privada
		
		return contentSigner;
		
	}
	

	 /**
	  * 
      Separa los SAN en una lista de 2 elementos bajo el siguiente orden:
      
      <ul>
      <li>{@code Map<String, String[]>} predeterminados del RFC 5280
      <li>{@code Map<String, String[]>} creados por el usuario
      </ul>                       
      
      @param _SAN	{@code Map<String, String[]>} que almacena los SAN (subjective Alternavite Names)            
      @return {@code List<Map<String, String[]>> }
	  * */
	private List<Map<String, String[]>> separaAtributoDeExtension(Map<String, String[]> _SAN) {
		
		//Lista de SANs establecidos en RFC 5280
		String[] array_predeterminados = CertificadosUtils.lista_SANs_Predeterminados;
		
		List<Map<String, String[]>> separados = new ArrayList<Map<String, String[]>>(2);
		Map<String, String[]> predeterminado = new HashMap<String, String[]>();
		Map<String, String[]> propios = new HashMap<String, String[]>();
		
		//Bandera para que "propios" no guarde todo sin discriminar
		boolean yaGuardado = false;
		
		for (Map.Entry<String, String[]> entry : _SAN.entrySet()) {
			String key = entry.getKey();
			String[] val = entry.getValue();
			
			//Busca que se encuentre dentro de los predeterminados
			for(String pred: array_predeterminados) {
				
				if(pred.equalsIgnoreCase(key)) {
					//Se guarda en el predet. si coincide con uno
					predeterminado.put(key, val);
					//Se autonotif. que ya se guardo para no volver hacerlo
					yaGuardado = true;
					break;
				}
			}
			
			//Agregar si no se encontró dentro de los predeterminados
			if(!yaGuardado) {
				propios.put(key, val);
			}
			
			//Se reactiva la bandera para que siga funcionando
			yaGuardado = false;
		}
		
		//Orden de los elementos
		separados.add(predeterminado);
		separados.add(propios);
		
		return separados;
	}
	
	 /**
	  * 
       Metodo creado para clasificar los <strong>SAN (Subjective Alternative Names)</strong> 
       y guardarlos en un objeto de tipo <strong>ExtensionsGenerator</strong>. 
       Los datos son metadatos que sirven para guardar informacion extra para una 
       <i>CA (Certificate Authority)</i> o un servidor.
       <p>
       Se pueden agregar datos predefinidos que están en el estándar RFC 5280:
       <ul>
       <li>DNS (dNSName), 
       <li>IP (iPAddress), 
       <li>Email (rfc822Name), 
       <li>URI, 
       <li>DirectoryName, 
       <li>RegisteredID, 
       <li>otherName, 
       <li>Otros nombres basados en OID
       </ul>
       
       <p>También puede agregar otros SANs personalizados no establecido por el estándar
       
       @param estandar_RFC_5280	{@code Map<String, String[]>} que almacena los SAN (subjective Alternavite Names) basados en el estandar RFC 5280
       @param propios			Map que almacena SANs no predeterminados, sino creados por el usuario
       @return ExtensionGenerator
       @throws IllegalArgumentException
       @throws IOException
       @throws Exception
	  * */
	public ExtensionsGenerator agregarAtributosDeExtension
		(Map<String,String[]> estandar_RFC_5280, Map<String, String[]> propios) {
		
		//Objeto que almacenará todas los ATRIBUTOS DE EXTENSION
		ExtensionsGenerator extGen = new ExtensionsGenerator();		
		
		//Una lista para guardar todos los "GeneralName" predeterminados
		ArrayList<GeneralName> arrayGN = new ArrayList<GeneralName>();
		
		String[] valores = null;

		if(estandar_RFC_5280 != null && estandar_RFC_5280.size() > 0){
			
			
			for (String rfc5280 : estandar_RFC_5280.keySet()) {							
				switch(rfc5280) {
					//String api.dominio.xx
					case "DNS" -> {
						//Recupera todos los valores para cada atributo de extension.
						valores = estandar_RFC_5280.getOrDefault(rfc5280, new String[0]);						
						
						for(String valor: valores) {							
							arrayGN.add(new GeneralName(GeneralName.dNSName, valor) );
						}
					}
					//String ###.###.###.### e IPv6
					case "IP" -> {						
						valores = estandar_RFC_5280.getOrDefault(rfc5280, new String[0] );						
						
						for(String valor: valores) {							
							arrayGN.add(new GeneralName(GeneralName.iPAddress, valor) );
						}
					}
					//String correo xxxx@yyyy.zz
					case "Email" -> {
						valores = estandar_RFC_5280.getOrDefault(rfc5280, new String[0]);						
						
						for(String valor: valores) {							
							arrayGN.add(new GeneralName(GeneralName.rfc822Name, valor) );
						}
					}
					//String  "https://midominio.com/recurso"
					case "URI" -> {
						valores = estandar_RFC_5280.getOrDefault(rfc5280, new String[0]);						
						
						for(String valor: valores) {							
							arrayGN.add(new GeneralName(GeneralName.uniformResourceIdentifier, valor) );
						}
					}
					//X509Name:  // "CN=,O=,OU=,L =,ST=,C =,"
					case "DirectoryName" -> {
						valores = estandar_RFC_5280.getOrDefault(rfc5280, new String[0]);						
					
						for(String valor: valores) {							
							arrayGN.add(new GeneralName(GeneralName.directoryName, new X500Name(valor) ) );
						}
						
					} 
					//ASN1ObjectIdentifier("1.2.3.4.5")
					//De preferencia LIMITAR A 1
					case "RegisteredID" -> {
						valores = estandar_RFC_5280.getOrDefault(rfc5280, new String[0]);						
						
						for(String valor: valores) {							
							arrayGN.add(new GeneralName(GeneralName.registeredID, new ASN1ObjectIdentifier(valor) ) );
						}
					} 
					///*new DERSequence( new ANS1Encondable[] ( ASN1ObjectIdentifier("1.2.3.4.5"),DERUTF8String("Valor"),ASN1Integer(7),...)*/
					/////De preferencia LIMITAR A 1
					/// Regla: Identificar cada valor con un prefijo para crear su respectivo objeto  
					case "otherName" -> {
						valores = estandar_RFC_5280.getOrDefault(rfc5280, new String[0]);
						
						//Almacen temp de los valores de otherName
						List<ASN1Encodable> asn1 = new ArrayList<ASN1Encodable>();
						
						for(String valor: valores) {
							//Se identifica el valor por el prefijo para crear un objeto ASN1 correspondiente
							/*String prefijo = identificaPrefijoASN1(valor);
							switch(valor) {
								
							}*/													
							switch(valor) {
								case String s when s.startsWith("OID") ->  {
									asn1.add(new ASN1ObjectIdentifier(valor));
									//yield "";
								}
								case String s when s.startsWith("DERUTF8") -> {
									asn1.add(new DERUTF8String(valor));
									//yield "";
								}
								case String s when s.startsWith("ASN1Integer") -> {
									//new ASN1Integer(0L);//Long
									try {
										asn1.add(new ASN1Integer(new BigInteger(valor)) );
									}catch(NumberFormatException ne){
										System.out.println("No es un número o no tiene el formato correcto:" + ne.getMessage());
										continue;
									}catch(Exception ex){
										System.out.println("Excepción inesperada:" + ex.getMessage());
										continue;
									}
									//yield "";
								}
								case String s when s.startsWith("ASN1Time") -> {
									/* El formato es YYYYMMDDHHMMSS[.f]Z, 
									 * 	sin Z para la hora local,
									 *  o Z+-HHMM al final para la diferencia de horas con UTC 
									 * 
									 * Para la parte fraccionaria de los segundos (o sea, "[.f]")
									 * debe contener al menos 1 numero
									 * con los 0s finales (los de la derecha). 
									 * */
									try {
										asn1.add(new ASN1GeneralizedTime(valor));
									}catch(IllegalFormatException ne){
										System.out.println("No tiene el formato \"YYYYMMDDHHMMSS[.f]Z+-HHMM\" correcto:" + ne.getMessage());
										continue;
									}catch(Exception ex){
										System.out.println("Excepción inesperada:" + ex.getMessage());
										continue;
									}
								}
								case String s when s.startsWith("ASN1Enum") -> {
									//new ASN1Enumerated(0);//Integer
									try {
										asn1.add(new ASN1Enumerated(new BigInteger(valor)));//BigInteger
									}catch(NumberFormatException ne){
										System.out.println("No es un número o no tiene el formato correcto:" + ne.getMessage());
										continue;
									}catch(Exception ex){
										System.out.println("Excepción Inesperada:" + ex.getMessage());
										continue;
									}
								}
								default -> {
									asn1.add(new DERUTF8String(valor));
									//continue;
									//throw new IllegalArgumentException("No es un ningún objeto conocido o válido");
								}
							};
						}
						
						arrayGN.add(
							new GeneralName(GeneralName.otherName, new DERSequence
								(
									(ASN1Encodable[]) asn1.toArray()
								)
							)
						);
					} 
					default -> {
						throw new IllegalArgumentException("No es un SANs predeterminado válido");
					}
				};
			}
			
			GeneralNames gn = new GeneralNames((GeneralName[]) arrayGN.toArray());
			try{
				/*	
					oid: Identificador en forma de objeto ASN1ObjectIdentifier
					critical: Booleano donde si se considera "critico" será "true"
						(es decir quien lea el certificado DEBE entender o rechazar ese SAN), 
						pero sino entoces será "false".
					value: El objeto ASN.1 que sera incluido en la extension.
				 * */
				extGen.addExtension(Extension.subjectAlternativeName, false, gn);
			}catch(IOException io) {
				System.out.println("Error alagregar los SANs al generador de Extensiones (ExtensionsGenerator)");
				io.printStackTrace();
			}
		}
		
		//Aquí se agregan los atrubutos de extension (SANs) personalizados,
		//es decir, que no tienen un OID prestablecido.
		if (propios != null && propios.size() > 0) {
			
			//Se vacia para poder almacenar los SANs propios
			List<ASN1Encodable> asn1 =  new ArrayList<ASN1Encodable>(); 
			asn1.clear();
			
			//Obtiene cada id de los OID
			for(String oids: propios.keySet()) {
				//Por cada SANs personalizado se obtiene un conjunto de valores
				valores = propios.get(oids);
				
				//Para guardar cada valor se genera un objeto ASN1Encodable
				for(String unidad: valores) {
					asn1.add(new DERUTF8String(unidad));
				}
				
				//Al ser personalizados se agregan directamente al ExtensionsGenerator.
				try {
					/*	
					oid: Identificador en forma de objeto ASN1ObjectIdentifier
					critical: Booleano donde si se considera "critico" será "true"
						(es decir quien lea el certificado DEBE entender o rechazar ese SAN), 
						pero sino entoces será "false".
					value: El objeto ASN.1 que sera incluido en la extension.
				 * */
					extGen.addExtension(
						new ASN1ObjectIdentifier(oids), 
						false, 
						new DERSequence( (ASN1Encodable []) asn1.toArray() )
					);
				} catch (IOException e) {
					System.out.println("Error al tratar a un SAN propio: " + oids);
					e.printStackTrace();
				} finally {
					asn1.clear();
				}
			}
		}
		
		return extGen;
	}
	
	/**
	 * Metódo que generará un Distinguished Name en formato String
	 * 
	 * 
	 * @param datos Datos que forman parte del Distinguished Name
	 * @return {@link String}
	 * */
	String concatenarContenidoCertificado(String[] datos) {
		
		
		//Formatear la cadena al formato "XX=Valor"
		for(int i = 0; i < datos.length; i++) {
			if(!datos[i].contains("=") 
				//&& (datos[i].charAt(1) == '=' || datos[i].charAt(2) == '=')
			){
				datos[i] = formatearDato(i, datos[i]);
			}
		}
		
		/*for(int i = 0; i < datos.length; i++) {
			System.out.println(datos[i]);
		}*/
		
		//Concetenar el texto
		String concatenado = "";
		
		for(int i = 0; i < datos.length; i++) {
			if(i <= datos.length - 1) {
				concatenado = concatenado.concat(datos[i]);
				if(i != datos.length - 1) {
					concatenado = concatenado.concat(",");
				}
			}
		}
		//System.out.println("X.509 Name: "+concatenado);
		return concatenado;
		
		
	}
	
	/**
	 * Modifica el dato "txt" dado para asignarle un prefijo para que este con el 
	 * formato de Distinguised Name (DN)
	 * 
	 * @param i Integer Numero de posicion
	 * @param txt String al que se le va a asignar un encabezado de acuerdo a su posicion
	 * @throws IllegalArgumentException
	 * @return String
	 * */
	private String formatearDato(int i, String txt) {
		String texto = "";
		texto = switch(i) {
			case 0: { 
				// CN = Nombre del Titular
				yield "CN=".concat(txt);
			}
			case 1: {
				// O = Organizacion
				yield "O=".concat(txt); 
			}
			case 2: { 
				// OU = Unidad Organizacional o subdivision de la organizacion
				yield "OU=".concat(txt);
			}
			case 3: { 
				// L = Ciudad o Localidad
				yield "L=".concat(txt);
			}
			case 4: { 
				// ST = Estado o Provincia
				yield "ST=".concat(txt);
			}
			case 5: { 
				// C = Codigo de 2 letras del Pais
				yield "C=".concat(txt);
			}
			default : throw new IllegalArgumentException("No es una opcion valida: " + txt);
		};
		return texto;
	}
	
	@Deprecated
	private String entenderSwitch(String sg) {
		String txt = "";
		txt = switch (sg) {
			case "hola" -> "mucho gusto";
			case "adios" -> "mucho gusto";
			case "anda" -> {yield "mucho gusto";}
			
			
			
			default -> throw new IllegalArgumentException("Unexpected value: " + sg);
		};
		return txt;
	}
	
	public Object generarCertificado
		(X500Name titularX500, KeyPair keyPair, 
		ContentSigner firmador, String[] emisorDN,
		String[] vig_y_serial) 
	throws CertificateException 
	{
		X500Name emisorX500 = null;
		
		//Se crea el DN del Emisor del certificado
		if(emisorDN != null && emisorDN.length > 0) {
			//DN = Distinguised Name
			String distName = concatenarContenidoCertificado(emisorDN);
			
			/*Se asignan los datos
			// CN = Nombre del Titular			
			// O = Organizacion
			// OU = Unidad Organizacional o subdivision de la organizacion
			// L = Ciudad o Localidad
			// ST = Estado o Provincia
			// C = Codigo de 2 letras del Pais
			//Todos ellos en conjunto se llaman Distingished Name (DN) */
			emisorX500 = new X500Name(distName);
		}
		//Aqui se indica que es un Certificado auto-firmado
		else {
			emisorX500 = titularX500;
		}
		
		/*String[] vig_y_serial:
		 * Array usado para obtener los últimos 3 datos para crear un certificado:
		 * [0]: No de serial
		 * [1]: Fecha de inicio de validez
		 * [2]: Fecha de fin de vigencia
		 * */
		
		//No. Serial
		//BigInteger serial = BigInteger.valueOf(System.currentTimeMillis());
		BigInteger serial = null;
		if (validarSerial(vig_y_serial[0])) {
			serial = BigInteger.valueOf( Long.valueOf(vig_y_serial[0] ) );
		}else {
			serial = BigInteger.valueOf(System.currentTimeMillis());			
		}
		
		//Fecha de Inicio de Vigencia
		Date ahora = null;
		if (vig_y_serial[1] != null) {
			ahora = crearFechaInicioValidez(vig_y_serial[1] );
		}else {
			ahora = crearFechaInicioValidez(null);			
		}
		
		//Fecha de Fin de Validez 
		// La fecha-actual + (dias * horas * minutos * segundos * milisegundos) 
			//Vigencia de un año
		//Date vigencia = new Date(System.currentTimeMillis() + (365L*24L*60L*60L*1000L));
		Date vigencia = null;
		if (vig_y_serial[2] != null) {
			vigencia = crearFechaFinVigencia(vig_y_serial[2], ahora );
		}else {
			vigencia = crearFechaFinVigencia(null, ahora);			
		}
		
		JcaX509v3CertificateBuilder certBuilder = new JcaX509v3CertificateBuilder(
			emisorX500, 		// X500Name: Emisor o Quién firma el Certificado 
			serial, 			// BigInteger: No. Serial
			ahora, 			// Date: notBefore, Inicio de la vigencia/validez
			vigencia, 		// Date: notAfter, Fecha de fin de validez 
			titularX500, 	// X500Name: Titular del Certificado 
			keyPair.getPublic() //Llave Pública
		);
		
		//Crea el holder del Certificado
		//Separado solo con fines de debug y aprendizaje
		X509CertificateHolder holder = certBuilder.build(firmador);
		
		return new JcaX509CertificateConverter()
				.setProvider("BC")
				.getCertificate(holder);
	}
	
	private boolean validarSerial(String serial) {
		try {
			long serie = Long.parseLong(serial);
			return true;
		}catch(NumberFormatException nfe ) {
			//nfe.printStackTrace();
			return false;
		}			
	}
	
	@Deprecated
	private boolean validarFecha(String _fecha) {
		if (_fecha == null) {
			return false;
		}
		
		/* OffsetDateTime obedece el estándar ISO-8601,
		 * por lo que para separa le fecha y la hora lo hace
		 * mediante una T.
		 * 
		 * Además para agregar la diferencia de horas respecto
		 * el GMT(UTC) se añade el offset con un guion(-) o un
		 * signo de más(+). 
		 * 
		 * Offset: indica cuántas horas se desplaza respecto a UTC
		 * 
		 * Offset obedece al ISO-8601 cuyo formato es:
		 * 2026-05-08T15:42:10.123-05:00
		 * 
		 * Si tu string NO tiene:
			
			Z
			GMT
			-05:00
			+02:00
			
			entonces el parser NO sabe qué offset usar.
		 * */
		try {
			OffsetDateTime o = OffsetDateTime.parse(_fecha);
		}catch(DateTimeParseException  d) {
			return false;
		}
		
		return true;
	}
	
	public Date crearFechaInicioValidez(String _fecha) {
		//TimeZone.setDefault(TimeZone.getTimeZone("America/Mexico_City"));
		if (_fecha == null) {
			return new Date();
		}
		
		/* OffsetDateTime obedece el estándar ISO-8601,
		 * por lo que para separar la fecha y la hora lo hace
		 * mediante una T.
		 * 
		 * Además para agregar la diferencia de horas respecto
		 * el GMT(UTC) se añade el offset con un guion(-) o un
		 * signo de más(+). 
		 * 
		 * Offset: indica cuántas horas se desplaza respecto a UTC
		 * 
		 * Offset obedece al ISO-8601 cuyo formato es:
		 * 2026-05-08T15:42:10.123-05:00
		 * 
		 * Si tu string NO tiene:
			
			Z
			GMT
			-05:00
			+02:00
			
			entonces el parser NO sabe qué offset usar.
		 * */
		try {
			if (!_fecha.contains("T")) {
				_fecha = _fecha.concat("T00:00:00-00:00");
			} else if(!_fecha.contains("+") 
				|| !_fecha.contains("-")) {
				_fecha = _fecha.concat("+00:00");
			}
			
			/*//En DESUSO porque 
			 * el objeto Date solamente almacena un instante en el tiempo, 
			 * es decir, un número de milisegundos desde el 
			 * epoch (1970-01-01T00:00:00Z).
			 * 
			 * New Date() NO guarda:
			 * 		-Zona horaria (America/Mexico_City)
			 * 		-Offset (-06:00, +02:00)
			 * 
			 * El offset se pierde porque:
			 * 		-Instant no guarda offset.
			 * 		-Date tampoco guarda offset.
			 * 
			 * OffsetDateTime sí guarda offset
			
			OffsetDateTime o = OffsetDateTime.parse(_fecha);
			
			Instant i = o.toInstant();
			
			Date fecha = Date.from(i);*/
			
	        /*DateTimeFormatter formatter =
	                DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ssxxx")
	                .withZone(ZoneId.of("America/Mexico_City"));
	        System.out.println("WriteCertificados.convertirFecha():"+ formatter.format(fecha.toInstant()));*/
	        System.out.println("WriteCertificados.crearFechaInicioValidez():"+ _fecha);

			Date fecha = Date.from(Instant.parse(_fecha));
	        
			return fecha;
			
		}catch(DateTimeParseException  d) {
			d.printStackTrace();
			return new Date();
		}
	}
	
	public Date crearFechaFinVigencia(String _fecha, Date inicioVig) {
		if (_fecha == null) {
			long min = 1L * 60L * 1000L;
			long seg = 0L * 1000L;
			long total = min + seg;			
			//return new Date(System.currentTimeMillis() + total);
			return new Date(inicioVig.getTime() + total);
		}
		
		/* _fecha debe tener el formato:
		 * [0]_[1]_[2]_[3]_[4]_[5]_[6]
		 * 
		 * Diccionario del String[] 
		 *		[0]: años
		 *		[1]: dias
		 *		[2]: horas
		 *		[3]: minutos
		 *		[4]: segundos
		 *		[5]: milisegundos
		 *		[6]: TimeZone (Offset) //En desuso
		*/
		String[] fecha_string = _fecha.split("_");
		long[] f = new long[fecha_string.length];
		
		for(int i = 0; i < fecha_string.length; i++) {
			f[i] = Long.parseLong(fecha_string[i]);
		}
		
		//Vigencia de un año
		long milisegundos 	=                                  f[5];
		long segundos 		=                          f[4] * 1000L;
		long minutos 		=                    f[3] * 60L * 1000L;
		long horas 			=              f[2] * 60L * 60L * 1000L;
		long dias 			=        f[1] * 24L * 60L * 60L * 1000L;
		long anios 			= f[0] * 365L * 24L * 60L * 60L * 1000L;
		long tiempo = anios + dias + horas + minutos + segundos + milisegundos;		
		
		//Date vigencia = new Date(System.currentTimeMillis() + tiempo);
		
		try {
			
			Instant tiempoAgregado = inicioVig.toInstant().plusMillis(tiempo);			
			
			/* NOTAS SOBRE DATE
			 * Lo que está mal es asumir que Date conserva el offset.
			 * (Zona horaria o desfase de horas)
			 * 
			 * Instant no guarda offset.
			 * Date tampoco guarda offset.
			 * 
			
			//Objeto con la diferencia de horas (offset)
			ZoneOffset offset = ZoneOffset.of(
				//fecha_string[6].replace("+","").replace("-", "")
				fecha_string[6]
			);
			
			//Tiempo con el offset incluido
			OffsetDateTime o = tiempoAgregado.atOffset(offset);
			
			//Conversion a Date
			Instant i = o.toInstant();
			Date vigencia = Date.from(i);
			
			return vigencia;*/
			
			Date vigencia = Date.from(tiempoAgregado);
			return vigencia;
			
		}catch(DateTimeParseException  d) {
			d.printStackTrace();
			long min = 1L * 60L * 1000L;
			long seg = 0L * 1000L;
			long total = min + seg;
			return new Date(System.currentTimeMillis() + total);
		}
	}
	
	@Deprecated
	public String formatearHora(String _hora) {
		if(_hora == null || _hora.isBlank()) {
			return "+00:00";
		}	
		
		String tmp = _hora.trim()
				.replace("+", "").replace("-", "")
				.replace("_", "").replace(":", "")
				.replace(" ", "");
		try {
			Integer.parseInt(tmp);
		}catch(NumberFormatException e) {
			return "+00:00";
		}
		
		/*Suponer que siempre son 4 
		agregar un digito 0 al inicio 
		si no tiene 4 o 5 digitos > agregar 0s
		dividir en 2 partes siendo la ultima de 2 digito
		
		probar 1:3 y 11:3
		 * */
		
		tmp = "0".concat(tmp);		
		for (int i = 0; 5 >= tmp.length(); i++) {
			tmp = tmp.concat("0");
		}
		
		String[] arrTmp = { tmp.substring(0, 3), tmp.substring(3) };
		
		int hr = Integer.parseInt(arrTmp[0]);
		int mm = Integer.parseInt(arrTmp[1]);
		
		if(hr > 23) {
			//Agregar mensaje y excepcion(opcional)
			return "+00:00";
		}
		if(mm > 59) {
			//Agregar mensaje y excepcion(opcional)
			return "+00:00";
		}
		
		String signo = _hora.substring(0, 1);
		
		if(!signo.contains("+") || !signo.contains("-")) {
			signo = "+";
		}
		
		String result = signo + hr + ":" + mm;
		
		return result;
	}
	
	
	public Object generarCSR(X500Name x500, KeyPair keyPair, 
		ContentSigner firmador, Map<String, String[]> _SAN) {
		
		PKCS10CertificationRequestBuilder p10Builder = new JcaPKCS10CertificationRequestBuilder(x500, keyPair.getPublic());
		
		//Agrega Atributos de Extension (en base a su OID) 
		if(_SAN != null && !_SAN.isEmpty()) {
			//Se separan los SAN en 2: los prederterminado y creados por el usuario
			List<Map<String, String[]>> listSANs = separaAtributoDeExtension(_SAN);
			//Se crean los respectivos extensionGenerator
			ExtensionsGenerator extGen = agregarAtributosDeExtension(listSANs.get(0), listSANs.get(1));
			//Se agregan al constructor (builder) de PKCS10CertificationRequestBuilder (p10Builder)
			p10Builder.addAttribute(
				PKCSObjectIdentifiers.pkcs_9_at_extensionRequest, 
				extGen.generate()
			);
		}
		
		PKCS10CertificationRequest csr = p10Builder.build(firmador);
		
		return csr;
	}
	
	/**
	 * Crea los archivos fisicos del Certificado (.cer), Private Key (.key),
	 * Public Key (.der), Solicitud de Creación de Certificado (.csr) y 
	 * Private Key cifrada (.key)
	 * 
	 * @param archivo El objeto lógico (es decir la info.) a escribir dentro del archivo
	 * @param nomArch La ruta con el nombre del archivo a generar
	 * @param extension El formato del archivo
	 */
	public void escritorDeArchivos(Object archivo, String nomArch, String extension) {
		try(JcaPEMWriter writer = new JcaPEMWriter(new FileWriter(nomArch+extension)) ) {
			/*Si es un objeto PEM usará JcaPEMWrite.writeObject() 
			 * que esta estructurado para objetos PEM
			 * 
			 * Para usar JcaPEMWriter.writeObject() correctamente, 
			 * se debe pasarle objetos criptográficos reconocidos por el 
			 * proveedor BouncyCastle. 
			 * 
			 * Los más comunes son: 
			 * 
			 * - KeyPair o claves individuales como PrivateKey y PublicKey.
			 * - Certificados digitales del formato X509Certificate.
			 * - Solicitudes de firma de certificado (CSR)
			 * 		(PKCS10CertificationRequest o ContentInfo).
			 * - Instancias directas que implementen PemObjectGenerator.
			 * 
			 * 
			 * Para un escribir un String plano se puede invocar 
			 * directamente el método write(String), 
			 * dado que JcaPEMWriter extiende indirectamente de 
			 * java.io.Writer (4ta generación descendiente). 
			 * 
			 * La clase FilterWriter también extiende de java.io.Writer. 
			 * 
			 * */
			if(extension.equalsIgnoreCase(".txt") 
				|| extension.contains("txt")) {
				writer.write((String) archivo);
			}else {
				writer.writeObject(archivo);
			}
			System.out.println("--- Se ha creado el archivo "+ extension +" ---");
			System.out.println("--- Archivo generado: " + nomArch+extension  + " ---");
		} catch (IOException e) {
			System.out.println("Excepcion generada:\n");
			e.printStackTrace();
		}
	}
	
	/**
	 * Método encargado de cifrar una PrivateKey mediante un 
	 * algorimto SIMETRICO especificado y una contraseña que se usara
	 * para derivarla mediante un KDF (Key Derivation Function) 
	 * crear un IV (Vector de Inicialización) para dicho algorimtmo.
	 * 
	 * @param pKey PrivateKey a cifrar
	 * @param algCifradoPK Nombre del algoritmo simetrico (interoperable
	 * con los demás)
	 * @param pass Contraseña para generar el IV del algoritmo
	 * @return {@link JcaPKCS8Generator}
	 * @throws OperatorCreationException
	 * @throws PemGenerationException
	 */
	private JcaPKCS8Generator cifrarPrivateKey(PrivateKey pKey, String algCifradoPK, String pass) 
		throws OperatorCreationException, PemGenerationException {
		
		//Por defecto, se agrega el Provider de BouncyCastle a la configuracion del Certificado
		Security.addProvider(new BouncyCastleProvider());
		
		/*Se indica el algoritmo SIMETRICO que se usara 
		 * para el cifrado de la Private Key*/
		JceOpenSSLPKCS8EncryptorBuilder encryptorBuilder 
		= 
		new JceOpenSSLPKCS8EncryptorBuilder(
			CertificadosUtils.elegirAlgoritmoSimetricoParaPKCS8(algCifradoPK)
		);
		
        /*Se agrega la contraseña que se usará para derivarla
         *mediante un KDF (Key Derivation Function para así
         *crear el IV (Vector de Inicialización) empleado en el
         *algoritmo siemtrico*/
		encryptorBuilder.setPassword(pass.toCharArray());
		encryptorBuilder.setProvider("BC");
					
		OutputEncryptor outputEncryptor = encryptorBuilder.build();
		
		/* Aquí generamos la PrivateKey cifada con 2 parametros:
		 * 0: PrivateKey
		 * 1: Algoritmo (JceOpenSSLPKCS8EncryptorBuilder)
		 * 
		 * Para generar el objeto PEM:
         *
         * -----BEGIN ENCRYPTED PRIVATE KEY-----
		 * */
		JcaPKCS8Generator pkcs8Cifrado =
				new JcaPKCS8Generator(pKey, outputEncryptor);
		
		
		return pkcs8Cifrado;
	}

	private String passwordArchivoCifrado(String algoritmo, String pass) {
		/* Datos para cifrar 
		 * 1: String con el ALGORITMO SIMETRICO para cifrar
		 * 2: String para la contraseña usada para cifrar la PrivateKey
		 * */
		String msg = """				
				La contraseña es:"""+pass+
				""" 				
				\nEl algoritmo usado es:"""+algoritmo+"""
				""";
		return msg;
	}
}
