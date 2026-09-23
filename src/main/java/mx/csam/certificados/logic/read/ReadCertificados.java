package mx.csam.certificados.logic.read;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.Security;
import java.security.Signature;
import java.security.SignatureException;
import java.security.cert.CertificateException;
import java.security.cert.CertificateExpiredException;
import java.security.cert.CertificateFactory;
import java.security.cert.CertificateNotYetValidException;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.Arrays;

import org.bouncycastle.asn1.ASN1Integer;
import org.bouncycastle.asn1.ASN1OctetString;
import org.bouncycastle.asn1.ASN1Primitive;
import org.bouncycastle.asn1.ASN1Sequence;
import org.bouncycastle.asn1.DERBitString;
import org.bouncycastle.asn1.pkcs.EncryptedPrivateKeyInfo;
import org.bouncycastle.asn1.pkcs.PrivateKeyInfo;
import org.bouncycastle.asn1.x509.SubjectPublicKeyInfo;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.bouncycastle.openssl.PEMException;
import org.bouncycastle.openssl.PEMKeyPair;
import org.bouncycastle.openssl.PEMParser;
import org.bouncycastle.openssl.jcajce.JcaPEMKeyConverter;
import org.bouncycastle.openssl.jcajce.JceOpenSSLPKCS8DecryptorProviderBuilder;
import org.bouncycastle.operator.InputDecryptorProvider;
import org.bouncycastle.operator.OperatorCreationException;
import org.bouncycastle.pkcs.PKCS8EncryptedPrivateKeyInfo;
import org.bouncycastle.pkcs.PKCSException;

import jakarta.annotation.Nullable;
import mx.csam.certificados.utilerias.constantes.CertificadosUtils;


public class ReadCertificados {
	
	private Path pathGral = Path.of("");
	
	public ReadCertificados() {
		
	}
	
	public ArrayList<Object> leerArchivos(
		String certiRuta, String privKRuta, String pubKRuta, @Nullable String passPK)
		throws IllegalArgumentException, OperatorCreationException, IOException, 
		PKCSException, CertificateException, NoSuchProviderException
	{	
		
		//Por defecto, se agrega el Provider de BouncyCastle a la configuracion del Certificado
		Security.addProvider(new BouncyCastleProvider());	
		
		// LECTURA DE ARCHIVOS
		
		//Lista de objetos recuperados del Certificados
		ArrayList<Object> listaCert = new ArrayList<Object>();
		
		//Se lee el certificado y se obtienen sus datos
		X509Certificate certi = null;			
		
		/* El try-catch servirá para cuando no se evaluén
		 * todos los archivos, en este caso en particular,
		 * cuando no se evalué eL Certificado.
		 * 
		 * --------------------------
		 * //0: Se agrega el Certifcado - X509Certificate
		 * //1: Algoritmo de Firma (String)
		 * //2: Identificador OID del Algoritmo de Firma (String)
		 * //3: Public Key (java.security.PublicKey)
		 * //4: Algoritmo de Public Key (String)
		 * //5: Sujeto Dueño del Certificado (javax.security.auth.x500.X500Principal)
		 * //6: Sujeto Emisor del Certificado (javax.security.auth.x500.X500Principal)
		 * //7: Fecha de Inicio de Validez (Date)
		 * //8: Fecha de Fin de Vigencia (Date)
		 * //9: Tipo
		 * //10: PrivateKey
		 * //11: PublicKey 
		 * */
		
		listaCert = leerCertificado(certiRuta);
		certi = (X509Certificate)(listaCert.get(0));
		
		
		/**
		 * Se lee la Private Key:
		 * 
		 * A. Se determina la codificación del archivo
		 * 		- PEM: compatible con PEMParser 
		 * 		- DER: ligago a ASN.1
		 * 
		 * B. Se determina que formato de estructura tiene
		 * 		- PKCS#1 RSA
		 * 		- PKCS#8 PrivateKeyInfo
		 * 		- PKCS#8 (CIFRADO) EncryptedPrivateKeyInfo
		 * 
		 * B.a Se debe determinar si esta cifrado o no intermanente
		 * para saber si se puede devolver directamente o descifrarlo
		 * */
		PrivateKey privk = leerPrivateKey(privKRuta, passPK);
		
		//Lee la Public Key
		PublicKey pubk = null;		
		pubk = leerPublicKey(pubKRuta);
		
		// PRUEBAS CRIPTOGRAFICAS
		
		//Verificar que el Certificado este vigente
		if(!validarValidezCertificado(certi)) {
			throw new IllegalArgumentException("Fechas del certificado ya no son validas");
		}
		
		//Verificar q' PublicKey externa == PublicKey del certificado
		if(!validarParPublicKeys(certi, pubk)) {
			throw new IllegalArgumentException("La PublicKey no corresponde al "
					+ "Certificado");
		}
		
		System.out.println("algoritmo de firma" + (String)listaCert.get(1));
		System.out.println("OID algoritmo de firma" + (String)listaCert.get(2));
		
		//Verificar q' PrivateKey corresponde a la PublicKey externa		
		if(!validarParDeKeys(privk, pubk, (String)listaCert.get(1) ) ) {
			throw new IllegalArgumentException("Las keys no corresponden entre sí");
		}
		
			//Saca la PublicKey del Certificado
		PublicKey pubKCerti = certi.getPublicKey();
		
		//Verificar q' PrivateKey corresponde a la PublicKey Certificado
		if(!validarParDeKeys(privk, pubKCerti, (String)listaCert.get(1) ) ) {
			throw new IllegalArgumentException("Las keys (del certificado) y private no corresponden entre sí");
		}
		
		//Validar criptograficamente la firma del Certificado con la PublicKey externa 
		if(!validarFirmaDelCertificado(certi, pubk) ) {
			throw new IllegalArgumentException("La firma del certificado  es diferente a la representada con la PublicKey externa");
		}
		
		listaCert.add(privk);
		listaCert.add(pubk);
		
		return listaCert;
	}

	// --------------------------------------------------------------------
	// LECTURA DEL CERTIFICADO
	
	/**
	 * Devuelve una lista de {@code ArrayList<Objecct>} con varios
	 * objetos de propiedades del Certificado ( {@code X509Certificate})
	 * 
	 * <pre>
	 * 0: Se agrega el Certifcado - X509Certificate
	 * 1: Algoritmo de Firma - (String)
	 * 2: Public Key - (java.security.PublicKey)
	 * 3: Algoritmo de Public Key - (String)
	 * 4: Sujeto Dueño del Certificado - (javax.security.auth.x500.X500Principal)
	 * 5: Sujeto Emisor del Certificado - (javax.security.auth.x500.X500Principal)
	 * 6: Fecha de Inicio de Validez - (Date)
	 * 7: Fecha de Fin de Vigencia - (Date)
	 * 8: Tipo de Certificado - (String)
	 * </pre>
	 * 
	 * @param certiRuta Ruta donde se encuentra el Certificado
	 * @return {@code ArrayList<Object>}
	 * @throws IOException
	 * @throws CertificateException
	 * @throws NoSuchProviderException 
	 */
	public ArrayList<Object> leerCertificado(String certiRuta) 
		throws IOException, CertificateException, NoSuchProviderException{
		
		//Por defecto, se agrega el Provider de BouncyCastle a la configuracion del Certificado
		Security.addProvider(new BouncyCastleProvider());		
		
		Path ruta = Path.of(certiRuta);
		
		ArrayList<Object> resul = null;
		
		//A partir de un Path se leen los bytes del Certificados
		try(InputStream is = Files.newInputStream(ruta)) {
			
			//Se crea un obj Factory para  poder abstraer el certificado digital
			CertificateFactory cf = CertificateFactory.getInstance("X.509", "BC");
						
			//Se extrae/genera el (X.509) Certificado
			X509Certificate cert = (X509Certificate) cf.generateCertificate(is);
			
			resul = new ArrayList<Object>();
			
			//0: Se agrega el Certifcado - X509Certificate
			resul.add(cert);			
			
			//Uso StringBuilder para no ahogar el pool String
			StringBuilder msj = new StringBuilder(
				//Version del Certificado
		    	"Tipo: " + cert.getType() + "\n" +
		    	"Versión: " + cert.getVersion() + "\n" +
		    	"Número de serie: " + cert.getSerialNumber() + "\n" +
		    	//Firma del Certificado
		    	"Algoritmo de firma: " + cert.getSigAlgName() + "\n" +
		    	
		    	//Info del Distinguished Name (DN)
		    	"Sujeto: " + cert.getSubjectX500Principal() + "\n" +
		    	"Emisor: " + cert.getIssuerX500Principal() + "\n" +
		    	//Fecha de Inicio de Validez y Fin de Vigencia
		    	"Válido desde: " + cert.getNotBefore() + "\n" +
		    	"Válido hasta: " + cert.getNotAfter() + "\n" +
		        
		    	//Datos de la Public Key
		    	"Clave pública: " + cert.getPublicKey() +
		    	"Algoritmo de clave pública: "
		    		+ cert.getPublicKey().getAlgorithm() + "\n" 
		    );
			
			System.out.println("""
				\n\n
				========= ReadCertificados.leerCertificado =========
				
				====================================================
				CERTIFICADO
				====================================================
				Imprimiendo contenido\n\n
				""" + "\n" + msj.toString());
			
			//1: Algoritmo de Firma (String)
			resul.add(cert.getSigAlgName());
			//2: Identificador OID del Algoritmo de Firma (String)
			resul.add(cert.getSigAlgName());
			//3: Public Key (java.security.PublicKey)
			resul.add(cert.getPublicKey());
			//4: Algoritmo de Public Key (String)
			resul.add(cert.getPublicKey().getAlgorithm()); 
	    	//5: Sujeto Dueño del Certificado ( javax.security.auth.x500.X500Principal)
			resul.add(cert.getSubjectX500Principal());
			//6: Sujeto Emisor del Certificado (javax.security.auth.x500.X500Principal)
			resul.add(cert.getIssuerX500Principal());
	    	//7: Fecha de Inicio de Validez (Date)
	    	resul.add(cert.getNotBefore()); 
	    	//8: Fecha de Fin de Vigencia (Date)
	    	resul.add(cert.getNotAfter());
	    	//9: Tipo (String)
	    	resul.add(cert.getType());
	    	
	    	return resul;
		} catch (IOException e) {
			e.printStackTrace();
			throw new IOException("Error en la lectura del certificado X.509");
		} catch (CertificateException e) { 
			e.printStackTrace();
			throw new CertificateException("No es posible abstraer el certificado X.509");
		}
		
		//return resul;
	}
	
	// --------------------------------------------------------------------
	// LECTURA DEL PUBLIC_KEY
	
	/**
	 * Lee el archivo correspondiente y devuelve un
	 * objeto {@link PublicKey}
	 * 
	 * @param pubKRuta [{@link String}] La ruta del archivo 
	 * @return {@link PublicKey}
	 */
	public PublicKey leerPublicKey(String pubKRuta) 
		throws IOException, IllegalArgumentException{
		PublicKey pk = null;
			
		Boolean[] analisis = null;
		try {
			analisis = determinaPEMoDER_y_encriptadoPKCS8(pubKRuta);
		}
		catch(IOException e) {
			System.err.println("El archivo no "
					+ "corresponde a una PrivateKey, "
					+ "por lo que no se puede procesar");
			throw new IOException("El archivo no "
					+ "corresponde a una PrivateKey, "
					+ "por lo que no se puede procesar");
		}  		
		catch(IllegalArgumentException e) {
			System.err.println("El archivo no "
				+ "corresponde a una PrivateKey, "
				+ "por lo que no se puede procesar");
			throw new IllegalArgumentException("El archivo no "
				+ "corresponde a una PrivateKey, "
				+ "por lo que no se puede procesar");
		}
		
		 /* De acuerdo al analisis devuelve el siguiente vector
		  * 0: PEM(true) o DER(false)
		  * 1: Publica(true) o Privada(false)
		  * 2: Cifrada(true) o No Cifrada(false)
		  */
		//¿es PublicKey?
		if (analisis[1] == false) {
			throw new IllegalArgumentException("El archivo no es una PublicKey "
					+ "por lo que no se puede procesar");
		}		
		
		//es PEM
		if (analisis[0] == true) {
			pk = descifrarPublicKeyPEM(pubKRuta);
		} 
		//es DER
		else {
			pk = descifrarPublicKeyDER(pubKRuta);
		}
				
		return pk;
	}
	
	/**
	 * Descifra la PrivateKey en formato PEM
	 * @param pubKRuta La ruta del archivo
	 * @return {@link PublicKey}
	 */
	public PublicKey descifrarPublicKeyPEM(String pubKRuta) {
		//Por defecto, se agrega el Provider de BouncyCastle a la configuracion del Certificado
		Security.addProvider(new BouncyCastleProvider());
		
		Path path = Path.of(pubKRuta);
		SubjectPublicKeyInfo pki = null;
		PublicKey pk = null;		
		Object obj = null;
		
		/*
		 * Lee el archivo, lo transforma a un objeto génerico PEM 
		 * mediante PEMParser y obtiene un PrivateKeyInfo*/
		try(PEMParser ps =	new PEMParser(Files.newBufferedReader(path))) {
			obj = ps.readObject();
			pki = (SubjectPublicKeyInfo) obj;
		} catch(IOException io)
		{
			io.printStackTrace();
			return null;
		}
		
		/*
		 * Intenta convertir la PrivateKeyInfo obtenido de la 
		 * lectura del archivo al objeto PEM de PrivateKey*/
		try {
			pk = new JcaPEMKeyConverter().
				setProvider("BC").
				getPublicKey(pki);
		} catch (PEMException e) {			
			e.printStackTrace();
		}
		
		return pk;	
	}

	/**
	 * Descifra la PublicKey en formato DER
	 * @param pubKRuta La ruta del archivo
	 * @return {@link PublicKey}
	 */
	public PublicKey descifrarPublicKeyDER(String pubKRuta) {
		//Por defecto, se agrega el Provider de BouncyCastle a la configuracion del Certificado
		Security.addProvider(new BouncyCastleProvider());
		
		Path path = Path.of(pubKRuta);
		SubjectPublicKeyInfo pki = null;
		PublicKey pk = null;		
		
		/*
		 * Lee el archivo, lo transforma en byte[] y 
		 * convierte en un obj génerico deL estándar ASN.1*/
		try {
			byte[] datos = Files.readAllBytes(path);
			ASN1Primitive asn1 = ASN1Primitive.fromByteArray(datos);
			pki = SubjectPublicKeyInfo.getInstance(asn1);
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
			
		/*
		 * Intenta convertir la SubjectPublicKeyInfo obtenido de la 
		 * lectura del archivo a un objeto PEM de PublicKey*/
		try {
			pk = new JcaPEMKeyConverter().
				setProvider("BC").
				getPublicKey(pki);
		} catch (PEMException e) {			
			e.printStackTrace();
		}
		
		return pk;
	}

	// --------------------------------------------------------------------
	// LECTURA DEL PRIVATE_KEY Y ENCRYPTED_PRIVATE_KEY	
	
	/**
	 * Lee el archivo correspondiente y devuelve un
	 * objeto {@link PrivateKey}
	 * 
	 * @param privKRuta [{@link String}] La ruta del archivo 
	 * @param pass [{@link String}] La contraseña del archivo
	 * @return {@link PrivateKey}
	 * 
	 * @throws IllegalArgumentException
	 * @throws OperatorCreationException
	 * @throws IOException
	 * @throws PKCSException
	 */
	public PrivateKey leerPrivateKey(String privKRuta, String pass) 
		throws IllegalArgumentException, OperatorCreationException, 
		IOException, PKCSException{
		
		
		PrivateKey pk = null;

		Boolean[] analisis = null;
		try {
			analisis = determinaPEMoDER_y_encriptadoPKCS8(privKRuta);
		}
		catch(IOException e) {
			System.err.println("El archivo no "
					+ "corresponde a una PrivateKey, "
					+ "por lo que no se puede procesar");
			throw new IOException("El archivo no "
					+ "corresponde a una PrivateKey, "
					+ "por lo que no se puede procesar");
		}  		
		catch(IllegalArgumentException e) {
			System.err.println("El archivo no "
				+ "corresponde a una PrivateKey, "
				+ "por lo que no se puede procesar");
			throw new IllegalArgumentException("El archivo no "
				+ "corresponde a una PrivateKey, "
				+ "por lo que no se puede procesar");
		}
		
		 /* De acuerdo al analisis devuelve el siguiente vector
		  * 0: PEM(true) o DER(false)
		  * 1: Publica(true) o Privada(false)
		  * 2: Cifrada(true) o No Cifrada(false)
		  */
		//¿es PublicKey?
		if (analisis[1] == true) {
			throw new IllegalArgumentException("El archivo no es una PrivateKey "
					+ "por lo que no se puede procesar");
		}
		
		//es Cifrada
		if(analisis[2] == true) {
			//es PEM
			if (analisis[0] == true) {
				pk = descifrarEncryptedPrivateKeyPEM(privKRuta, pass);
			} 
			//es DER
			else {
				pk = descifrarEncryptedPrivateKeyDER(privKRuta, pass);
			}
		}
		//es No Cifrada
		else {
			//es PEM
			if (analisis[0] == true) {
				pk = descifrarPrivateKeyPEM(privKRuta);
			} 
			//es DER
			else {
				pk = descifrarPrivateKeyDER(privKRuta);
			}
			
		}
		
		return pk;
	}
	
	/**
	 * Descifra la PrivateKey en formato PEM
	 * @param privKRuta La ruta del archivo
	 * @return {@link PrivateKey}
	 */
	public PrivateKey descifrarPrivateKeyPEM(String privKRuta) {
		//Por defecto, se agrega el Provider de BouncyCastle a la configuracion del Certificado
		Security.addProvider(new BouncyCastleProvider());
		
		Path path = Path.of(privKRuta);
		//PrivateKeyInfo pki = null;
		PrivateKey pk = null;		
		Object obj = null;
		
		/*
		 * Lee el archivo, lo transforma a un objeto génerico PEM 
		 * mediante PEMParser y obtiene un PrivateKeyInfo*/
		try(PEMParser ps =	new PEMParser(Files.newBufferedReader(path))) {
			obj = ps.readObject();
			//pki = (PrivateKeyInfo) obj;
		} catch(IOException io)
		{
			io.printStackTrace();
			return null;
		}
		
		/*
		 * Intenta convertir la PrivateKeyInfo obtenido de la 
		 * lectura del archivo al objeto PEM de PrivateKey*/
		try {
			/*pk = new JcaPEMKeyConverter().
				setProvider("BC").
				getPrivateKey(pki);*/
			JcaPEMKeyConverter conversor = new JcaPEMKeyConverter()
					.setProvider("BC");
			
			if (obj instanceof PrivateKeyInfo pki) {
				pk = conversor.getPrivateKey(pki);
			}
			/* Para los algoritmos EC|RSA(PKCS#1)|DSA en PEM "tradicional"
			 * sus cabeceras son diferentes al BEGIN PRIVATE KEY 
			 * 
			 * RSA/RSA-PSS: -----BEGIN RSA PRIVATE KEY-----
			 * DSA: -----BEGIN DSA PRIVATE KEY-----
			 * EC: -----BEGIN EC PRIVATE KEY-----
			*/
			else if(obj instanceof PEMKeyPair kp) {
				pk = conversor.getPrivateKey(kp.getPrivateKeyInfo());
				System.out.println("ReadCertificados.leerPrivateKeyPEM()"
					+ "´\nentro: " + pk.getAlgorithm() + " - " + pk.getFormat());
			}
			else if(obj instanceof PrivateKey p) {
				pk = p;				
			}
			else {
				throw new IllegalArgumentException("Clave privada tipo PEM no soportada");	
			}
		} catch (PEMException e) {			
			e.printStackTrace();
			return null;
		}
		
		return pk;			
	}

	/**
	 * Descifra la PrivateKey en formato DER
	 * @param privKRuta La ruta del archivo
	 * @return {@link PrivateKey}
	 */
	public PrivateKey descifrarPrivateKeyDER(String privKRuta) 
		 {
		//Por defecto, se agrega el Provider de BouncyCastle a la configuracion del Certificado
		Security.addProvider(new BouncyCastleProvider());
		
		Path path = Path.of(privKRuta);
		PrivateKeyInfo pki = null;
		PrivateKey pk = null;		
		
		/*
		 * Lee el archivo, lo transforma en byte[] y 
		 * convierte en un obj génerico deL estándar ASN.1*/
		try {
			byte[] datos = Files.readAllBytes(path);
			ASN1Primitive asn1 = ASN1Primitive.fromByteArray(datos);
			pki = PrivateKeyInfo.getInstance(asn1);
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
			
		/*
		 * Intenta convertir la PrivateKeyInfo obtenido de la 
		 * lectura del archivo a un objeto PEM de PrivateKey*/
		try {
			pk = new JcaPEMKeyConverter().
				setProvider("BC").
				getPrivateKey(pki);
		} catch (PEMException e) {			
			e.printStackTrace();
		}
		
		return pk;
	}

	/**
	 * Descifra la EncryptedPrivateKey DER (una PrivateKey cifrada 
	 * con una contraseña y algoritmo) en formato DER
	 *  
	 * @param privKRuta [{@link String}] La ruta del archivo 
	 * @param pass [{@link String}] La contraseña del archivo
	 * @return {@link PrivateKey}
	 * @throws IOException
	 * @throws OperatorCreationException
	 * @throws PKCSException
	 */
	public PrivateKey descifrarEncryptedPrivateKeyDER(String privKRuta, String pass) 
			throws IOException, OperatorCreationException, PKCSException {
			
			//Por defecto, se agrega el Provider de BouncyCastle a la configuracion del Certificado
			Security.addProvider(new BouncyCastleProvider());
			
			// 1. Se adapta la ruta a un objeto Path
			Path path = Path.of(privKRuta);
			
			// 2. Se lee todo el archivo y se convierte en byte[] 
			byte[] datos = Files.readAllBytes(path);
			
			 /*
	         * 3. Comprueba que realmente tenemos un
	         * PKCS8EncryptedPrivateKeyInfo.
	         * 
	         *  El PEM debe contener:
	         *  -----BEGIN ENCRYPTED PRIVATE KEY-----
	         */
			PKCS8EncryptedPrivateKeyInfo encriptadaPKInfo = new PKCS8EncryptedPrivateKeyInfo(datos);
			
			
			/*
			 * 4. Crea el Proveedor de Descifrado del PKCS#8
			 * 
			 * Se le proporciona la contraseña del cifrado.
			 * */
			InputDecryptorProvider desencriptador = 
				new JceOpenSSLPKCS8DecryptorProviderBuilder()
				//Se surte como proveedor de seguridad a BouncyCastle
				.setProvider("BC")
				//Se añade la contraseña del Cifrado
				.build(pass.toCharArray());
			
			/*
			 * 5. Descifra el PK
			 * 
			 * El resultado es un PrivateKeyInfo ASN.1 del PKCS#8
			 * sin cifrar
			 * */
			PrivateKeyInfo pki;
			try {
				pki = encriptadaPKInfo
						.decryptPrivateKeyInfo(desencriptador);
			} catch (PKCSException e) {
				e.printStackTrace();
				throw new PKCSException("Fallo al desencriptar con"
					+ "el proveedor configurado a partir de la contraseña");
			}
			
			/**
			 * 6. Convertir el PrivateKeyInfo a un java.security.PrivateKey
			 */				
			PrivateKey descifrada = 
				new JcaPEMKeyConverter()
				.setProvider("BC")
				.getPrivateKey(pki);			
			
			return descifrada;
		}

	/**
	 * Descifra la EncryptedPrivateKey PEM (una PrivateKey cifrada 
	 * con una contraseña y algoritmo) en formato PEM
	 *  
	 * @param ruta [{@link String}] La ruta del archivo 
	 * @param pass [{@link String}] La contraseña del archivo
	 * @return {@link PrivateKey}
	 * @throws IOException
	 * @throws IllegalArgumentException
	 * @throws OperatorCreationException
	 * @throws PKCSException
	 */
	public PrivateKey descifrarEncryptedPrivateKeyPEM(String ruta, String pass)
		throws IOException, IllegalArgumentException, 
		OperatorCreationException, PKCSException{
		
		//Por defecto, se agrega el Provider de BouncyCastle a la configuracion del Certificado
		Security.addProvider(new BouncyCastleProvider());
		
		//AÚN EN DESUSO POR FLOJERA PERO DEBE PODER CAMBIAR LA CODIFICACION
		//DEL TEXTO EN LOS ARCHIVOS ENTRE EL iso Y EL utf-8
		CertificadosUtils.obtenFormatoCharset();
		
		/* Otra forma de leer el archivo pero conviertiendo
		 * a bytes y a la vez en un objeto PEM (ASN.1)
		 * 
		try(
		//Lectura del archivo a partir de una ruta URI/String			
		InputStream is = Files.newInputStream(Path.of(""));
		//Convierte esa entrada de stream de bytes (InputStream<byte>)
		//	en un Reader 
		InputStreamReader isr = new InputStreamReader(is);
		//La lectura de bytes va hacia el PEMParser
		PEMParser ps =	new PEMParser(isr)) {
		}
		*/
		
		/*1. Abre el archivo con el PemParser*/
		Object pemObj = null; 
		Path path = Path.of(ruta);
		
		System.out.println("descifrarEncryptedPrivateKeyPEM()\n"
				+ "Archivo elegido: " + ruta + "\n");
		
		try(/*FileReader fr = new FileReader(ruta);
			PEMParser pemParser = new PEMParser(fr)*/
				
			//Sustitución del FileReader por Files.newBufferedReader
			PEMParser pemParser = new PEMParser(Files.newBufferedReader(path))
			) {
			/*2. Lee el objeto PEM y lo convierte en un 
			 * objeto de BouncyCastle.
			 * 
			 * NOTAS SOBRE readObject() y readPemObject():
			 * 
			 * - readObject() parsea el PEM a PKCS8EncryptedPrivateKeyInfo, 
			 * y a otros objetos con terminación "Info" (o sea, etc).
			 * Es decir, decodifica el PEM y devuelve el tipo Java adecuado
			 * (ej. PKCS8EncryptedPrivateKeyInfo para
			 *  -----BEGIN ENCRYPTED PRIVATE KEY-----)
			 * 
			 * - readPemObject() solo devuelve PemObject 
			 * (etiqueta/tipo + bytes) 
			 * sin parsear a ninguna estructura ASN.1
			 * */
			
			pemObj = pemParser.readObject();
			
			/* NOTA SOBRE 
			 * NullPointerException con readPemObject().getType()
			 * 
			 * Nunca es null el pemParser dentro del 
			 * try-with-resources. sino el problema es llamar 
			 * dos veces a readPemObject():
			 * 
			 * La primera lectura consume el único bloque PEM del archivo.
			 * 
			 * La segunda (la del println comentado) devuelve null 
			 * porque ya no hay más bloques.
			 * */
			//System.out.println(pemParser.readPemObject().getType());
			
		} catch (IOException e) {
			e.printStackTrace();
			throw new IOException("""
				Imposible leer el archivo
				No es un objeto (PrivateKey - PKCS#8)
				PEM legible o la ruta esta mal"""
			);
		}
		
		//Validacion para no trabajar con nulos
		if (pemObj == null) {
			throw new IllegalArgumentException(
			"El archivo PEM no contiene ningún bloque legible. "
			+ "Puede que este vacío o mal formado.");
		}
		
		 /*
         * 3. Comprueba que realmente tenemos un
         * PKCS8EncryptedPrivateKeyInfo.
         * 
         *  El PEM debe contener:
         *  -----BEGIN ENCRYPTED PRIVATE KEY-----
         */
		PKCS8EncryptedPrivateKeyInfo encriptadaPKInfo = null;		
		
		if(pemObj instanceof PKCS8EncryptedPrivateKeyInfo _pkcs8) {
			//encriptadaPKInfo = (PKCS8EncryptedPrivateKeyInfo) pemObj;
			encriptadaPKInfo = _pkcs8;
		}
		else if(pemObj instanceof EncryptedPrivateKeyInfo _pkcs8) {
			
			/*Si llega EncryptedPrivateKeyInfo, se envuelve en 
			 * new PKCS8EncryptedPrivateKeyInfo(epki) para 
			 * reutilizar el mismo flujo de descifrado.*/
			encriptadaPKInfo = new PKCS8EncryptedPrivateKeyInfo(_pkcs8);
			
				/*Es lo mismo pero SIN PatternMatch 
				 * aplicado al instanceof
			EncryptedPrivateKeyInfo _pemObj = (EncryptedPrivateKeyInfo) pemObj;
			encriptadaPKInfo = new PKCS8EncryptedPrivateKeyInfo(_pemObj);
				 */			
		}
		else {			
			throw new IllegalArgumentException ("El archivo de "
				+ pemObj.getClass().getCanonicalName() 
				+ " no contiene una Private Key (PKCS#8) cifrada");
		}
		
		/*
		 * 4. Crea el Proveedor de Descifrado del PKCS#8
		 * 
		 * Se le proporciona la contraseña del cifrado.
		 * */
		InputDecryptorProvider desencriptador = 
			new JceOpenSSLPKCS8DecryptorProviderBuilder()
			//Se surte como proveedor de seguridad a BouncyCastle
			.setProvider("BC")
			//Se añade la contraseña del Cifrado
			.build(pass.toCharArray());
		
		/*
		 * 5. Descifra el PK
		 * 
		 * El resultado es un PrivateKeyInfo ASN.1 del PKCS#8
		 * sin cifrar
		 * */
		PrivateKeyInfo pki;
		try {
			pki = encriptadaPKInfo
					.decryptPrivateKeyInfo(desencriptador);
		} catch (PKCSException e) {
			e.printStackTrace();
			throw new PKCSException("Fallo al desencriptar con"
				+ "el proveedor configurado a partir de la contraseña");
		}
		
		/**
		 * 6. Convertir el PrivateKeyInfo a un java.security.PrivateKey
		 */				
		PrivateKey descifrada = 
			new JcaPEMKeyConverter()
			.setProvider("BC")
			.getPrivateKey(pki);
		return descifrada;
	}

	// --------------------------------------------------------------------
	// DETECTOR DE PEM/DER Y TIPO DE ARCHIVO .KEY
	
	/**
	 * Identifica si un archivo pertene a una key
	 * es PublicKey, PrivateKey o EncryptedPrivateKey. Al 
	 * evaluarlo se analiza primero si es formato PEM o DER,
	 * segundo si es PublicKey o PrivateKey y tercero se
	 * evalua si se trata de una PrivateKey Cifrada o No 
	 * Cifrada. En caso de haber sido detectada como 
	 * PublicKey en el paso 2, por defecto en la tercera
	 * evaluación se determina como false.
	 * 
	 *  Un forma de ver el análisis de las key es de la 
	 *  siguiente forma:
	 * 
	 *  <pre>
	 * 0: PEM(true) o DER(false)
	 * 1: Publica(true) o Privada(false)
	 * 2: Cifrada(true) o No Cifrada(false)
	 *  </pre>
	 * @param ruta [{@link String}] Ruta del archivo a analizar
	 * @return {@link Boolean}[]
	 * @throws IOException
	 * @throws IllegalArgumentException 
	 */
	public Boolean[] determinaPEMoDER_y_encriptadoPKCS8(String ruta) 
	throws IOException, IllegalArgumentException {
		
		//Por defecto, se agrega el Provider de BouncyCastle a la configuracion del Certificado
		Security.addProvider(new BouncyCastleProvider());
		
		//Vector que tendrá los resultados de este método
		Boolean[] res = {null, null, null};
		
		/**
		 * 0: PEM(true) o DER(false)
		 * 1: Publica(true) o Privada(false)
		 * 2: Cifrada(true) o No Cifrada(false)
		 * */
		
		Path path = Path.of(ruta);
		String texto = "";
		byte[] datos = null;
		
		//Leer todo el archivo para hacer las pruebas 
		try {
			datos =  Files.readAllBytes(path);
			texto = new String(datos, StandardCharsets.UTF_8);
		} catch (IOException e) {
			System.err.println("No se pudo leer el archivo");
			e.printStackTrace();
			return new Boolean[] {null, null, null};			
		}
		
		
		//0: Detectar si es PEM(true) o DER(false)
		if (texto.contains("-----BEGIN")) {
			//Es PEM
			res[0] = true;
			
			try(
				/*
				//Lectura del archivo a partir de una ruta URI/String			
				InputStream is = Files.newInputStream(path);
				//Convierte esa entrada de stream de bytes (InputStream<byte>)
				//	en un Reader 
				InputStreamReader isr = new InputStreamReader(is);
				
				//La lectura de bytes va hacia el PEMParser
				PEMParser ps =	new PEMParser(isr))
				
				O ABREVIADO:
				PEMParser ps =	new PEMParser(new InputStreamReader(Files.newInputStream(path)))) {			
				*/		
				//BufferedReader r = Files.newBufferedReader(path);
				PEMParser ps =	new PEMParser(Files.newBufferedReader(path))) {
					
				//De lo lógico pasamos a un Object "físico"
				Object obj = ps.readObject();
				
				//Validacion para no trabajar con nulos
				if (obj == null) {
					throw new IllegalArgumentException(
					"El archivo PEM no contiene ningún bloque legible. "
					+ "Puede que este vacío o mal formado.");
				}
				
				//Se crea un PEMConverter 
				/*JcaPEMKeyConverter converter = new 
						JcaPEMKeyConverter()
						.setProvider("BC");*/
				
				//1: Publica(true) o Privada(false)
				if (obj instanceof SubjectPublicKeyInfo pub) {
					//Es PublicKey
					res[1] = true;
					//Por lo tanto, NO CIFRADA
					//2: Cifrada(true) o No Cifrada(false)
					res[2] = false;
				}
				
				//1: Publica(true) o Privada(false)
				else if (obj instanceof PrivateKeyInfo priv) {
					//Es PrivateKey
					res[1] = false;
					//2: Cifrada(true) o No Cifrada(false)
					res[2] = false;
				}
				
				/**
				 * Para los algoritmos EC|RSA(PKCS#1)|DSA en PEM "tradicional"
				 * sus cabeceras son diferentes al BEGIN PRIVATE KEY
				 * 
				 * RSA/RSA-PSS: -----BEGIN RSA PRIVATE KEY-----
				 * DSA: -----BEGIN DSA PRIVATE KEY-----
				 * EC: -----BEGIN EC PRIVATE KEY-----
				 * 
				 * Para esos casos:
				 * //PEM: true, Priv: false, NO Cif: false
				 * */
				else if(obj instanceof PEMKeyPair) {
					//Es PrivateKey
					res[1] = false;
					//2: Cifrada(true) o No Cifrada(false)
					res[2] = false;
				}
				
				//1: Publica(true) o Privada(false)
				else if(obj instanceof EncryptedPrivateKeyInfo enc ||
					obj instanceof PKCS8EncryptedPrivateKeyInfo pkcs) {
					
					//System.out.println("\nObjeto es de clase:"+obj.getClass());					
					
					if(PKCS8EncryptedPrivateKeyInfo.class.isInstance(obj)) {
						System.out.println("Casteando:"+ 
						((PKCS8EncryptedPrivateKeyInfo) obj).getClass());						
					}
					if(EncryptedPrivateKeyInfo.class.isInstance(obj)) {
						System.out.println("Casteando:"+ 
								((EncryptedPrivateKeyInfo) obj).getClass());						
					}
					
					//Es EncryptedPrivateKey, por lo tanto -> 1: false
					res[1] = false;
					//2: Cifrada(true) o No Cifrada(false)
					res[2] = true;
				}
				else {
					throw new IllegalArgumentException("El archivo no corresponde"
					+ "ni a una PrivateKey, ni a una PublicKey "
					+ "por lo que no se puede procesar");
				}
			
				return res;
		
			} catch (IOException e) {
				System.err.println("No se pudo leer el archivo");
				//e.printStackTrace();
				//return new Boolean[] {null, null, null};
				throw new IOException("No se puede leer el archivo");
			}
		} else {
			//Es DER
			res[0] = false;
			
			datos = null;
			
			try {
				datos = Files.readAllBytes(path);
				ASN1Primitive a_prim =  ASN1Primitive.fromByteArray(datos);
				
				//
				ASN1Sequence secuencia = ASN1Sequence.getInstance(a_prim);
				
				//Si no contiene la estructura de un archivo .key
				/*if(!(a_prim instanceof ASN1Sequence)) {
					throw new IOException("El archivo no es compatible.");
				}*/
				
				
				//1: Publica(true) o Privada(false)
				if (esPublicKey(secuencia)) {
					//Es PublicKey
					res[1] = true;
					//Por lo tanto, NO CIFRADA
					//2: Cifrada(true) o No Cifrada(false)
					res[2] = false;
				}
				
				//1: Publica(true) o Privada(false)
				else if (esPrivateKey(secuencia)) {
					//Es PrivateKey
					res[1] = false;
					//2: Cifrada(true) o No Cifrada(false)
					res[2] = false;
				}
			
				//1: Publica(true) o Privada(false)
				else if(esPrivateKeyCifrada(secuencia)) {
					//Es EncryptedPrivateKey -> 1: false
					res[1] = false;
					//2: Cifrada(true) o No Cifrada(false)
					res[2] = true;
				}
				
				if (res[1] == null || res[2] == null) {
					throw new IllegalArgumentException("El archivo no corresponde"
					+ "ni a una PublicKey, ni a una PrivateKey "
					+ "por lo que no se puede procesar");
				}
			
				return res;
			} catch (IOException e) {
				System.err.println("No se pudo leer el archivo");
				//e.printStackTrace();
				throw new IOException("No se pudo leer el archivo");
				//return new Boolean[] {null, null, null};
			}	
		}		
	}
	
	/**
	 * Compara la estructura interna tipo {@link ASN1Sequence}
	 * del parametro contra 
	 * la estructura de interna de una PublicKey
	 * 
	 * <pre>
	 * SEQUENCE {
	 *    AlgorithmIdentifier
	 *    BIT STRING
	 * }
	 * </pre>
	 * @param secuencia
	 * @return
	 */	
	public boolean esPublicKey(ASN1Sequence secuencia) {
		boolean resul = false;
		
		resul = secuencia.size() == 2 &&
				secuencia.getObjectAt(0).
					toASN1Primitive() instanceof ASN1Sequence &&
				secuencia.getObjectAt(1).
					toASN1Primitive() instanceof DERBitString;
		
		return resul;
	}

	/**
	 * Compara la estructura interna tipo {@link ASN1Sequence}
	 * del parametro contra 
	 * la estructura de interna de una PrivateKey
	 * 
	 * <pre>
	 * SEQUENCE {
	 * 	INTEGER
	 * 	AlgorithmIdentifier
	 * 	OCTET STRING
	 * 	...
	 * }
	 * </pre>
	 * @param secuencia  ASN1Sequence
	 * @return boolean
	 */
	public boolean esPrivateKey(ASN1Sequence secuencia) {
		boolean resul = false;
		
		resul = secuencia.size() >= 3 &&
				secuencia.getObjectAt(0).
					toASN1Primitive() instanceof ASN1Integer &&
				secuencia.getObjectAt(1).
					toASN1Primitive() instanceof ASN1Sequence &&
					secuencia.getObjectAt(2).
						toASN1Primitive() instanceof ASN1OctetString;
		
		return resul;
	}
	
	/**
	 * Compara la estructura interna tipo {@link ASN1Sequence}
	 * del parametro contra 
	 * la estructura de interna de una EncryptedPrivateKey
	 * 
	 * <pre>
	 * SEQUENCE {
	 * 	AlgorithmIdentifier
	 * 	OCTET STRING
	 * }
	 * </pre>
	 * @param secuencia
	 * @return boolean
	 */
	public boolean esPrivateKeyCifrada(ASN1Sequence secuencia) {
		boolean resul = false;
		
		resul = secuencia.size() == 2 
				&& secuencia.getObjectAt(0)
				.toASN1Primitive() instanceof ASN1Sequence
				&& secuencia.getObjectAt(1)
				.toASN1Primitive() instanceof ASN1OctetString;
				
		return resul;
	}
	
	// --------------------------------------------------------------------
	// PRUEBAS CRIPTOGRAFICAS	

	/**
	 * Verifica mediante checkValidity() la vigencia del Certificado.
	 * 
	 * @param x509
	 * @return boolean
	 * @throws CertificateExpiredException 
	 * @throws CertificateNotYetValidException
	 */
	public boolean validarValidezCertificado(X509Certificate x509) {
		boolean valido = false;
		
		try {
			x509.checkValidity();
			valido = true;
		}catch(CertificateExpiredException cee) {
			System.err.println("Certificado ya expirado");
			cee.printStackTrace();			
		}catch(CertificateNotYetValidException cnyve) {
			System.err.println("Certificado todavía no válido. Aún no comienza su vigencia.");
			cnyve.printStackTrace();			
		}
		
		return valido;
	}
	
	/**
	 * Verifica mediante verify(PublicKey) la 
	 * validez de la firma del Certificado.
	 * 
	 * @param x509
	 * @return boolean
	 * @throws CertificateExpiredException 
	 * @throws CertificateNotYetValidException
	 */
	public boolean validarFirmaDelCertificado(
			X509Certificate x509,
			PublicKey pb 
		) {
		boolean valido = false;
		
		try {
			x509.verify(pb);
			valido = true;		
		} catch (InvalidKeyException e) {
			e.printStackTrace();
		} catch (CertificateException e) {
			e.printStackTrace();
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		} catch (NoSuchProviderException e) {
			e.printStackTrace();
		} catch (SignatureException e) {
			e.printStackTrace();
		}
		
		return valido;
	}
	
	/**
	 * Verifica que un par de llaves públicas, 
	 * la integrada en el {@link X509Certificate} y {@link PublicKey}
	 * sean exactamente iguales.   
	 * 
	 * @param x509 {@link X509Certificate}
	 * @param pk {@link PublicKey}
	 * @return boolean
	 */
	public boolean validarParPublicKeys(X509Certificate x509, PublicKey pk) {
		boolean resul = false;
		
		resul = Arrays.equals(
			x509.getPublicKey().getEncoded(), 
			pk.getEncoded()
		);
		
		return resul;
	}		
		
	/**
	 * Evalúa un texto fijo para comprobar que un par de llaves, 
	 * PublicKey y PrivateKey, son llaves correspondientes mutuamente.
	 * 
	 * Para ello, primero con la PrivateKey se firma el texto estatico,
	 * luego se comprueba con la PublicKey que tenga la misma huella de 
	 * la firma de la PrivateKey mediante Signature.verify(byte[]).
	 * 
	 * @param priv [{@link PrivateKey}]
	 * @param pub [{@link PublicKey}]
	 * @param algfirma [{@link String}] Nombre dek akgiritmo de la firma
	 * @return
	 */
	public boolean validarParDeKeys(PrivateKey priv, PublicKey pub, String algfirma) {

		//Por defecto, se agrega el Provider de BouncyCastle a la configuracion del Certificado
		Security.addProvider(new BouncyCastleProvider());		
		
		boolean resul = false;
		
		/*El texto que sirve para firmar con la PrivateKey
		 *y comparar la firma con la PublicKey
		 * */
		byte[] texto = "prueba-parDeKeys".getBytes(StandardCharsets.UTF_8);
		
		//Objeto de la Firma
		Signature firmante = null;
		Signature verificador = null;
		try {
			//SE FIRMA CON PRIVATE_KEY
			
			/*Recupera la firma en base al algoritmo de firma
			 * descrito en el Certificado */
			firmante = Signature.getInstance(algfirma, "BC");
			
			//Ni idea, supongo configura al obj firmante de Firma
			firmante.initSign(priv);
			firmante.update(texto);
			
			//Firma el texto usando la PrivateKey
			byte[] txtFirmado = firmante.sign();
			
			//----------------------------------------------
			//SE VERIFICA la firma CON PUBLIC_KEY
			
			/*Recupera la firma en base al algoritmo de firma
			 * descrito en el Certificado */
			verificador = Signature.getInstance(algfirma, "BC");
			
			//Creo, configura al obj verificador de Firma
			verificador.initVerify(pub);
			verificador.update(texto);
			
			resul = verificador.verify(txtFirmado);
			
			/**
			 * NOTAS:
			 * Parece que update() actualiza el texto con el que va a 
			 * trabajar en el objeto de Signature.
			 * 
			 * Al crear un Signature estos empiezan con un texto 
			 * vacio y con update() se indica con cuál (texto)
			 * se va a trabajar/manipular, sea para
			 * firmar (sign()) o comprobar (verify()) 
			 */
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		} catch (NoSuchProviderException e) {
			e.printStackTrace();
		} catch (InvalidKeyException e) {
			e.printStackTrace();
		} catch (SignatureException e) {
			e.printStackTrace();
		}
		
		return resul;
	}
}