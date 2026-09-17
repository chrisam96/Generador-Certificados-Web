package mx.csam.certificados.utilerias.constantes;

import java.math.BigInteger;
import java.security.spec.MGF1ParameterSpec;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.swing.JOptionPane;

import org.bouncycastle.asn1.ASN1Encodable;
import org.bouncycastle.asn1.ASN1Enumerated;
import org.bouncycastle.asn1.ASN1GeneralizedTime;
import org.bouncycastle.asn1.ASN1Integer;
import org.bouncycastle.asn1.ASN1ObjectIdentifier;
import org.bouncycastle.asn1.DERUTF8String;
import org.bouncycastle.openssl.PKCS8Generator;
import org.bouncycastle.openssl.jcajce.JceOpenSSLPKCS8EncryptorBuilder;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;


public final class CertificadosUtils {
	
	// --------------------------------------------------------------------
	// HERRAMIENTA PARA LA GENERACION DE CERTIFICADOS--------------------------------------------------------------------
	//Tipo de Herramienta de Generación de Certificados 
	public String [] herramienta_de_desarrollo = {"BouncyCastle", "OpenSSL", "Java Keytool"};
	
	// --------------------------------------------------------------------
	// ALGORITMOS ASIMETRICOS PARA KEY_PAIR_GENERATOR
	
	//Tipo de Algoritmo Asímetrico 
	public static String [] algoritmo_asimetrico = {"RSA", "DSA", "EC", "Ed25519", "Ed448", "GOST", "RSA-PSS" };
	
	public static Map<String, String> lista_menu_algoritmo_asimetrico(){
		Map<String, String> lista =  new HashMap<String, String>();
		
		lista.put("RSA", "RSA"); 
		lista.put("DSA", "DSA");
		lista.put("EC", "EC"); 
		lista.put("ECDSA", "EC"); 
		lista.put("Ed25519", "Ed25519"); 
		lista.put("Ed448", "Ed448"); 
		lista.put("GOST", "GOST");
		lista.put("RSA-PSS", "RSA-PSS");
		
		return lista;
	}
		
	// --------------------------------------------------------------------
	// INICIALIZADORES PARA KEY_PAIR_GENERATOR

	//Verificar si se puede MENOS DE 2048
	public static String [] tamanio_de_clave_KPG = {"1024", "2048", "3072", "4096", "6144", "8192"};
	
	public static MultiValueMap<String, String> lista_tamanio_Bits_KPG_algoritmo_asimetrico(){
		MultiValueMap<String, String> lista =  new LinkedMultiValueMap<String, String>();
		
		lista.put("RSA", Arrays.asList(tamanio_de_clave_KPG) ); 
		lista.put("DSA", Arrays.asList(tamanio_de_clave_KPG) );
		lista.put("EC", Arrays.asList(
				concatenarArrays(
					ALGORTIMOS_FAM_EC.SECG_o_NIST.NIST_mas_usadas,
					ALGORTIMOS_FAM_EC.Brainpool.Brainpool_mas_usados
				)
			)); 
		lista.put("Ed25519", Arrays.asList()); 
		lista.put("Ed448", Arrays.asList()); 
		lista.put("GOST", Arrays.asList(ALGORTIMOS_FAM_EC.GOST.algoritmos_de_clave));
		lista.put("RSA-PSS", Arrays.asList(tamanio_de_clave_KPG));
		
		return lista;
	}
	
	public static String[] concatenarArrays(String[] ... listas ) {
		ArrayList<String> arr =  new ArrayList<String>();
		
		for (String[] unaLista : listas) {
			/*Transforma el array en una List<>
			 * Posteriormente agrega todo a otra List<>*/			
			arr.addAll( Arrays.asList(unaLista) );
		}
		
		//Es como si fuera un (String[])arr.toArray();
		/*Object[]: el array original 
		 *int: la cantidad de elementos a copiar (se rellena con nulls si
		 *		es necesario)
		 *Class<? extends T>: El tipo de clase a retornar
		 * */
		return Arrays.copyOf(arr.toArray(), arr.toArray().length, String[].class);
	}
		
	/**
	 * Clase interna con la FAMILIA DE ALGORITMO DE CURVAS ELIPTICAS (EC) <br>
	 * <p>Incluye:</p>
	 * 
	 * 	<ul>
	 * 		<li>SECG o NIST (son lo mismo): 
	 * 		<ul>
	 * 			<li>SECP: Curvas sobre campos primos
	 * 			<li>SECT: Curvas binarias
	 * 			<li>Curvas NIST más usadas tipo SECP
	 * 		</ul>
	 * 		<li>Brainpool (algoritmos empleados en Europa):
	 * 		<ul>
	 * 			<li>Lista de algorimtos
	 * 			<li>Brainpool más usados
	 * 		</ul>
	 * 		<li>Curvas Modernas:
	 * 		<ul>
	 * 			<li>RFC_7748: Para intercambio de claves
	 * 			<li>RFC_8032: Para firmas modernas
	 * 			<li>ANSI_X9_62: Curvas ANSI X9.62 (similares a SECP)
	 * 		</ul>
	 * 		<li>GOST (algoritmos usados en Rusia):
	 * 		<ul> 
	 * 			<li>ECGOSTT3410
	 * 			<li>ECGOSTT3410-2012
	 * 		</ul>
	 * 	</ul>
	 * 
	 * 		
	 */
	public static class ALGORTIMOS_FAM_EC {			
		
 		public final class SECG_o_NIST{			
				public static final String[] SECP_Curvas_sobre_campos_primos = {
					"secp112r1",          
					"secp112r2",          	
					"secp128r1",          	
					"secp128r2",          	
					"secp160k1",          	
					"secp160r1",          	
					"secp160r2",          	
					"secp192k1",          	
					"secp192r1",          	
					"secp224k1",          	
					"secp224r1",          	
					"secp256k1",          	
					"secp256r1", // (P-256)	
					"secp384r1", // (P-384)	
					"secp521r1", // (P-521)							
				};
				public static final String[] SECT_Curvas_binarias = {
					"sect113r1",
					"sect113r2",
					"sect131r1",
					"sect131r2",
					"sect163k1",
					"sect163r1",
					"sect163r2",
					"sect193r1",
					"sect193r2",
					"sect233k1",
					"sect233r1",
					"sect239k1",
					"sect283k1",
					"sect283r1",
					"sect409k1",
					"sect409r1",
					"sect571k1",
					"sect571r1"					
				};
				
				public static final String[] NIST_mas_usadas = {
					"secp256r1", // (P-256)	
					"secp384r1", // (P-384)	
					"secp521r1", // (P-521)							
				};
		};
		
		public final class Brainpool{
			public static final String[] algoritmos= {
				"brainpoolP160r1",
				"brainpoolP160t1",	
				"brainpoolP192r1",	
				"brainpoolP192t1",	
				"brainpoolP224r1",	
				"brainpoolP224t1",	
				"brainpoolP256r1",	
				"brainpoolP256t1",	
				"brainpoolP320r1",	
				"brainpoolP320t1",	
				"brainpoolP384r1",	
				"brainpoolP384t1",	
				"brainpoolP512r1",	
				"brainpoolP512t1"
			};
			
			public static final String[] Brainpool_mas_usados= {
				"brainpoolP256r1",
				"brainpoolP384r1",
				"brainpoolP512r1"				
			};
		};
		
		public final class Curvas_Modernas{
			//Para intercambio de claves
			public static final String[] RFC_7748 = {
					"X25519",
					"X448"
				};
			//Para firmas modernas
			public static final String[] RFC_8032 = {
					 "Ed25519",
					 "Ed448"
				};
			//Curvas ANSI X9.62
			public static final String[] ANSI_X9_62 = {
					"prime192v1",   //prime192v1 (similar a secp192r1)
					"prime256v1"    //prime256v1 (similar a secp256r1)
				};
		};
		
		public final class GOST{
			//Gen_curva: GENERACIÓN DE LA CURVA
			public static final String[] algoritmos_de_clave
			= new String[] {"ECGOST3410", "ECGOST3410-2012"};
			
			//Gen_curva: GENERACIÓN DE LA CURVA
			public static final String[] gen_curva
				= new String[] {"ECGOST3410", "ECGOST3410-2012 (256)", "ECGOST3410-2012 (512)"};
			
			/** 
			 * Devuelve una {@code MultiValueMap<String,String>} (aka {@code LinkedHashMap<String,ArrayList<String>>}) dividido por la generación del
			 * algoritmo y la cantidad de BITS empleada.
			 * 
			 * La lista esta dividida en 
			 * <ul>
			 * 	<li> Algoritmos viejos: ECGOST3410 (2001)
			 * 	<li> Algoritmos nuevos: ECGOST3410-2012 (2012)
			 * </ul>
			 * 
			 * La lista  de algoritmos nuevos esta subdividida en la cantidad de bit 
			 * <ul>
			 * 	<li> 256
			 * 	<li> 512
			 * </ul>
			 * 
			 * @return {@code MultiValueMap<String,String>}
			 * 
			 * */
			public static final MultiValueMap<String, String> parametros_de_curva_GOST(){
				MultiValueMap<String, String> map = new LinkedMultiValueMap<String, String>();

				map.put("ECGOST3410", List.of(
						"GostR3410-2001-CryptoPro-A", 
						"GostR3410-2001-CryptoPro-B", 
						"GostR3410-2001-CryptoPro-C"
					)
				);
				
				map.put("ECGOST3410-2012 (256)", List.of(
					//256
					"Tc26-Gost-3410-12-256-paramSetA", 
					"Tc26-Gost-3410-12-256-paramSetB", 
					"Tc26-Gost-3410-12-256-paramSetC", 
					"Tc26-Gost-3410-12-256-paramSetD"
					)
				);
				
				map.put("ECGOST3410-2012 (512)", List.of( 
					//512
					"Tc26-Gost-3410-12-512-paramSetA", 
					"Tc26-Gost-3410-12-512-paramSetB", 
					"Tc26-Gost-3410-12-512-paramSetC"
					)
				);
				
				return map;
			}
			
			public static final HashMap<String, String> algoritmos_de_firma(){
				
				HashMap<String, String> map = new HashMap<String, String>();
				
				map.put("ECGOST3410", "GOST3411withECGOST3410");
				map.put("ECGOST3410-2012 (256)", "GOST3411-2012-256withECGOST3410-2012-256");
				map.put("ECGOST3410-2012 (512)", "GOST3411-2012-256WITHECGOST3410-2012-512");
				
				return map;
			}
			
			public static String cambioNombreSegunBits(String txt) {
				if (txt.contains("256")) {
					return "ECGOST3410-2012 (256)";
				}else if(txt.contains("512")) {
					return "ECGOST3410-2012 (512)";
				}
				
				return "ECGOST3410";
			}
			
			public static String reversoCambioNombreSegunBits(String txt) {
				if(txt.contains("512") || txt.contains("256"))
				{
					return "ECGOST3410-2012";
				}
			
				return "ECGOST3410"; 
			}
		};
	}
	
	
	// --------------------------------------------------------------------
	// ALGORITMO PARA LA FIRMA (DEL OBJETO JCA_CONTENT_SIGNER_BUILDER)
	
	//Tamaño de bits
	public String [] tamanio_de_hash_firma_SHA1 = {"SHA-1", "MD2", "MD5"};
	public String [] tamanio_de_hash_firma_SHA2 = {"224", "256", "384", "512", "512/224","512/256"};
	public String [] tamanio_de_hash_firma_SHA3 = {"224", "256", "384", "512"};
	
	/**
	* Plantilla para el Algoritmo de Hashing<br><br>
	* <ul>
	* 	<li>[XXX] define si es SHA1, MD2 MD5, SHA-XXX (SHA2) o SHA3-XXX<rbr>
	* 	<li>[YYY] define si el algoritmo asimetrico con el que se combina<br>
	* 	<li>[ZZZ] es para el "andMGF1" del RSA-PSS<br>
	* </ul>
	*  <br>
	* Nota:
	* <ul>
	*	<li>"MD2withRSA" para MD2
	*	<li>"MD5withRSA" para MD5
	*	<li>"SHA1withRSA" para SHA1
	*	<li>"SHA256withRSA" para SHA2
	*	<li>"SHA3-256withRSA" para SHA3
	*</ul>
	* </p>
	* @return String
	*/
	public static String plantilla_algoritmo_para_firmar() { 
		String firma = "[XXX]with[YYY][ZZZ]";
		return firma;
	}
	
	/*
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
	
	/**
	 * Indica los Hashes disponibles de acuerdo a la generación de la familia SHA.
	 * Devuelve una Map de la forma {@code MultiValueMap<String,String>} o lo que
	 * es lo mismo, un {@code LinkedHashMap<String,ArrayList<String>>} 
	 * 
	 * @return {@code LinkedMultiValueMap<String,String>} 
	 * @return {@code LinkedHashMap<String,ArrayList<String>>} 
	 * 
	*/
	public final MultiValueMap<String,String> lista_generaciones_algoritmo_SHA(){
		MultiValueMap<String, String> generaciones =  new LinkedMultiValueMap<String, String>();
		
		ArrayList<String> gen1 =  new ArrayList<String>();
		gen1.add("SHA-1");//SHA1withRSA, SHA1wtihECDSA
		gen1.add("MD2");//MD2withRSA
		gen1.add("MD5");//MD5withRSA
		
		ArrayList<String> gen2 =  new ArrayList<String>();		
		for(String hash : tamanio_de_hash_firma_SHA2) {
			gen2.add("SHA-"+hash);
		}
		
		//SHA3-356withRSA
		ArrayList<String> gen3 =  new ArrayList<String>();
		for(String hash : tamanio_de_hash_firma_SHA3) {
			gen3.add("SHA3-"+hash);
		}
		
		generaciones.put("gen1", gen1);
		generaciones.put("gen2", gen2);
		generaciones.put("gen3", gen3);
		
		return generaciones;
	}
	
	/**
	 * Tamaño de la Sal en BITS que por defecto maneja cada hash del SHA. 
	 * <p> En caso de usar la sal por defecto y no una personalizada se debe
	 * usar el método {@link listaTamanioBITSSegunSHA()} que implementa un 
	 * wrapper Optional<> para devolver el valor o uno por defecto 512.
	 * </p>
	 */
	public static final Map<String, Integer> listaTamanioBITSSegunSHA(){
		final HashMap<String, Integer> map =  new HashMap<String, Integer>();
		
		map.put("MD2", 128);
		map.put("MD5", 128);
		map.put("SHA-1", 160);
		map.put("SHA-224", 224);
		map.put("SHA-256", 256);
		map.put("SHA-384", 384);
		map.put("SHA-512", 512);
		map.put("SHA-512/224", 224);
		map.put("SHA-512/256", 256);
		map.put("SHA3-224", 224);
		map.put("SHA3-256", 256);
		map.put("SHA3-384", 384);
		map.put("SHA3-512", 512);
		
		return map;
	}
	
	/**
	 * Devuelve el tamaño en BITS de la implementacion del SHA pasada
	 * en el parámetro
	 * <p> En caso de usar la sal por defecto y no una personalizada se debe
	 * usar este método para devolver el valor
	 * 
	 * @param SHA Algoritmo SHA
	 * 
	 * @return int
	 * */
	public int tamanioBITSSegunSHA(String SHA) {		
		return listaTamanioBITSSegunSHA().getOrDefault(SHA, 512);
	}
	
	/**
	 * Tamanio de la Sal en BYTES que por defecto maneja cada hash del SHA. 
	 * <p> En caso de usar la sal por defecto y no una personalizada se debe
	 * usar el método {@link tamanioHashSegunSHA()} para devolver el valor.
	 * 
	 * @return {@code final Map<String, Integer>}
	*/
	public static final Map<String, Integer> listaTamanioBytesSegunSHA(){
		final HashMap<String, Integer> map =  new HashMap<String, Integer>();
				
		map.put("MD2", 16);
		map.put("MD5", 16);
		map.put("SHA-1", 20);
		map.put("SHA-224", 28);
		map.put("SHA-256", 32);
		map.put("SHA-384", 48);
		map.put("SHA-512", 64);
		map.put("SHA-512/224", 28);
		map.put("SHA-512/256", 32);
		map.put("SHA3-224", 28);
		map.put("SHA3-256", 32);
		map.put("SHA3-384", 48);
		map.put("SHA3-512", 64);		
		//map.put("SHA3-512/224", 28);
		//map.put("SHA3-512/256", 32);
		
		return map;
	}
	
	/**Devuelve el tamaño en bytes de la implementacion del SHA pasada
	 * en el parámetro
	 * <p> En caso de usar la sal por defecto y no una personalizada se debe
	 * usar este método para devolver el valor
	 * */
	public int tamanioBytesSegunSHA(String SHA) {		
		return listaTamanioBytesSegunSHA().getOrDefault(SHA, 64);
	}
	
	/**Calculo de Max de Sal para el algo. RSA-PSS, basados en
	 * el tamaño de la clave usasda (tamClave) y del algoritmo SHA (hashAlg). */
	public static int tamMaxDeSalt(String tamHashSHA, String tamClaveBits) {		
		
		int maxSal = 0 ;
		Map<String, Integer> tamanios = listaTamanioBytesSegunSHA();
		
		//hLen
		int longHash = tamanios.getOrDefault(tamHashSHA, 64);
		
		//emLen
		int longClave = (int) Math.ceil( (Integer.parseInt(tamClaveBits) - 1 )/8 );  
		
		//MaxSalt = emLen - hLen - 2 
		maxSal = longClave - longHash - 2;
		
		/*Debe cumplirse esta condición de PKCS #1:
			emLen >= hLen + maxSal + 2
		*/
		
		return maxSal;
	}
	
	
	/**
	 * Valida que los parametros del tamaño del SHA (en bytes), 
	 * el tamaño de la Clave (en bits) y el tamaño de la Sal puedan servir 
	 * para el algoritmo de RSA-PSS
	 * 
	 * @param tamHashSHA El tamaño de bits del algoritmo SHA
	 * @param tamClaveBits El tamaño de bits usado para inicializar el KeyPairGenerator 
	 * @param tamSal Tamaño usado para la "sal"
	 * 
	 * @return boolean
	 * */
	public static boolean validarSalt_TamSHA_ClaveBits
	(String tamHashSHA, String tamClaveBits, String tamSal) {
		
		int sal = 0;
		try {
			//saltLen 
			sal = Integer.parseInt(tamSal);
		}catch( NumberFormatException e) {
			System.out.println(e.getMessage());
			JOptionPane.showMessageDialog(
				null, 
				"La sal debe ser un número",
				"Salt requerido",
				JOptionPane.ERROR_MESSAGE
			);
			//throw e;
			return false;
		}
		
		if(sal < 0) {
			JOptionPane.showMessageDialog(
				null, 
				"La sal no puede ser menor a 0.",
				"Tamaño de Salt m&iacute;nimo requerido",
				JOptionPane.ERROR_MESSAGE
			);
			return false;
		}
		
		if (!validarQueEsNumero(tamClaveBits)) {
			JOptionPane.showMessageDialog(
					null, 
					"""
					El tamaño de la Clave NO pueden ser nulo 
					ni negativos, ni numeros fraccionarios,
					ni letras
					""",
					"Informacion inválida.",
					JOptionPane.ERROR_MESSAGE
					);
			return false;
		}
		
		
		boolean validez = true;		
		
		//hLen
		int hashLeng = listaTamanioBytesSegunSHA().getOrDefault(tamHashSHA, 0);
		
		//emLen
		int emLengClaveBits = (int) Math.ceil( (Integer.parseInt(tamClaveBits) - 1 )/8 );  
		
		/*Debe cumplirse esta condición de PKCS #1:
			emLen >= hLen + sal + 2
		 */
		validez = (emLengClaveBits >= hashLeng + sal + 2);
		
		if(!validez) {
			
			//MaxSalt = emLen - hLen - 2 
			int limiteMaxSal = emLengClaveBits - hashLeng - 2;
			JOptionPane.showMessageDialog(
				null, 
				"La sal no puede ser mayor a " + limiteMaxSal + " basado en el "
				+ "\n hash del SHA eleigdo, ni menor a 0.",
				"Tamaño de Salt excedido",
				JOptionPane.ERROR_MESSAGE
			);
			return false;
		}
		return validez;
	}
	
	/**
	 * Comprueba que un String sea un número entero positivo.
	 * No admite signos negativos ni puntos/comas decimales
	 * 
	 * @param txt
	 * @return boolean
	 */
	public static boolean validarQueEsNumero(String txt) {
		boolean veredicto = true;
		
		if(txt == null) {
			return false;
		}
		
		if(txt.isBlank()) {
			return false;
		}
					
		veredicto = txt.chars().allMatch(alfanum -> Character.isDigit(alfanum));
		
		if(!veredicto) {
			return false;
		}
		return veredicto;
	}
	
	// --------------------------------------------------------------------
	// PARAMETROS DEL RSA-PSS

	/**
	 * 
	 * */
	public static Map<String, MGF1ParameterSpec> listaPSSParameterSpec(){
		HashMap<String, MGF1ParameterSpec> lista = new HashMap<String, MGF1ParameterSpec>();
		lista.put("MD2", null);
		lista.put("MD5", null);		
		lista.put("SHA-1", MGF1ParameterSpec.SHA1);
		lista.put("SHA-224", MGF1ParameterSpec.SHA224);
		lista.put("SHA-256", MGF1ParameterSpec.SHA256);
		lista.put("SHA-384", MGF1ParameterSpec.SHA384);
		lista.put("SHA-512", MGF1ParameterSpec.SHA512);
		lista.put("SHA-512/224", MGF1ParameterSpec.SHA512_224);
		lista.put("SHA-512/256", MGF1ParameterSpec.SHA512_256);
		lista.put("SHA3-224", MGF1ParameterSpec.SHA3_224);
		lista.put("SHA3-256", MGF1ParameterSpec.SHA3_256);
		lista.put("SHA3-384", MGF1ParameterSpec.SHA3_384);
		lista.put("SHA3-512", MGF1ParameterSpec.SHA3_512);
		return lista;
	}
	
	// --------------------------------------------------------------------
	// VALIDACIONES DE TAMAÑOS EN CLAVE DE BITS Y HASH SHA PARA DSA	
	
	/**
	 * 
	 * <p>Hay restricciones en tamaños aprobados históricamente por pares L/N 
	 * concretos:
	 * 
	 * <pre>
	 *	| ------------------- | ----------------------------- |
	 * 	| Tamaño de clave DSA | Hash permitido históricamente |
	 *	| ------------------- | ----------------------------- |
	 *	| 1024                | SHA-1 (160 bits)              |
	 *	| ------------------- | ----------------------------- |
	 *	| 2048                | SHA-224, SHA-256              |
	 *	| ------------------- | ----------------------------- |
	 *	| 3072                | SHA-256                       |
	 *	| ------------------- | ----------------------------- |
	 *</pre>
	 *
	 * @param tamClave (String) Tamaño de la clave a validar
	 * @return boolean
	 * */
	public static boolean validarEnDSAMinMaxTamClave(String tamClave) {
		int tamanio = 0;
		try {
			tamanio = Integer.parseInt(tamClave);
		}
		catch(NumberFormatException pe) {
			return false;
		}
				
		if(tamanio > 3072 || tamanio < 1024) {
			return false;
		}			
		return true;
	}
	
	/**
	 * 
	 * <p>Hay restricciones en tamaños mínimos y máximos en el Tamaño de la Clave 
	 * aprobados históricamente por pares L/N concretos:
	 * 
	 * <pre>
	 *	| ------------------- | ----------------------------- |
	 * 	| Tamaño de clave DSA | Hash permitido históricamente |
	 *	| ------------------- | ----------------------------- |
	 *	| 1024                | SHA-1 (160 bits)              |
	 *	| ------------------- | ----------------------------- |
	 *	| 2048                | SHA-224, SHA-256              |
	 *	| ------------------- | ----------------------------- |
	 *	| 3072                | SHA-256                       |
	 *	| ------------------- | ----------------------------- |
	 *</pre>
	 *
	 * @param tamClave (String) Tamaño de la clave a validar
	 * @return boolean
	 * */
	public static boolean validarEnDSA_HashSHA_TamClave(String HashSHA, String tamClave) {
		boolean valido = false;
		switch (tamClave) {
			case "1024":
				if ("SHA-1".equals(HashSHA)) {
					valido = true;
				}
				break;
			case "2048":
				if ("SHA-224".equals(HashSHA) || "SHA-256".equals(HashSHA)) {
					valido = true;
				}
				break;
			case "3072":
				if ("SHA-256".equals(HashSHA)) {
					valido = true;
				}
				break;
		}
		
		return valido;
	}
	
	// --------------------------------------------------------------------
	/* GENERADOR DE FIRMAS DEL KEY_PAIR_GENERATOR 
	 PARA ALGORITMOS DE CURVAS ELIPTICAS */
	
	/**</pre>
	* */
	public static String firmaEnGOST(String genCurva, String curvaInit ) {
		
		//Firma para los Nuevos
		if (genCurva.contains("2012")) {
			//Hash de 256 bits
			if (curvaInit.contains("256")) {
				return "GOST3411-2012-256withECGOST3410-2012-256";
			} 
			//Hash de 512 bits
			else {
				return "GOST3411-2012-512WITHECGOST3410-2012-512";
				//return "GOST3411-2012-256withECGOST3410-2012-512";
			}
		} 
		//Firma para los Viejos
		else {
			return "GOST3411withECGOST3410";
		}
	}
	
	/**
	 * Devuelve el algoritmo de la firma en base a la curva inicializadora
	 * pasada como argumento (String).
	 * <p>
	 * Consta de varios pasos para elegir el tamaño adecuado del hash SHA 
	 * siendo:
	 * <ul>
	 * 		<li> Eliminar el sufijo del nombre de la curva inicializadora
	 * 		<li> Eliminar el prefijo en el mismo nombre
	 * 		<li> Elegir el tamaño del SHA basado en el número obtenido
	 * </ul>  
	 * <pre>
	 * </pre>
	 * @param  curvaInit {String} La curva inicializadora de la curva eliptica
	 * @return String
	 * 
	 * */
	public static String firmaEnEC(String curvaInit ) {
		
		/*
		 * Es más universal pero peca de ser más lento
		 * 
		//Elimina los subindices de la curva EC para buscar un aproximado
		String[] EC_sufijos = { "r1", "t1", "v1", "k1", "r2", "Ed", "X" }; 
		
		for (String pos : EC_sufijos) {
			if(curvaInit.contains(pos)){
				if(pos.equals("t1") && curvaInit.contains("sect")) {
					continue;
				}
				else {
					curvaInit = curvaInit.replace(pos, "");
					break;
				}
			}
		}
		*/
	
		//Remueve el sufijo { "r1", "t1", "v1", "k1", "r2", "Ed", "X" };
		if(curvaInit.contains("secp") || curvaInit.contains("sect") 
			|| curvaInit.contains("brainpool") || curvaInit.contains("prime")) {
			
			curvaInit = curvaInit.substring(0, curvaInit.length()-2 );
		}

		//remueve el prefijo de la curva EC para buscar un aproximado
		String[] EC_prefijos = { "secp", "sect", "brainpoolP", "X", "Ed", "prime" }; 
		
		for (String pre : EC_prefijos) {
			if(curvaInit.contains(pre)) {
				curvaInit = curvaInit.replace(pre, "");
				break;
			}
		}
		
		System.out.println("CertificadosUtils.firmaEnEC().string quedo:'"+ curvaInit +"'");
		
		/*
		secp 		112 128 160 192 224 256 384 521
		sect 		113 131 163 193 233 239 283 409 571
		brainpool 	160 192 224 256 320 284 512
		RFC_7748	25519	448
		RFC_8032	25519 	448
		ANSI_X9_62	192 256
		*/
		
		curvaInit = switch(curvaInit) {
			case "112", "113", "128", "131", "160", "163" -> {
				yield "SHA1"; 
				
			}
			//El más cercano es 224
			case "192", "193", "224", "233", "239"  -> {
				yield "SHA224"; 
				
			}
			case "256", "283" -> {
				yield "SHA256"; 
				
			}
			case "384", "409" -> {
				yield "SHA384"; 
				
			}
			case "512", "521", "571" -> {
				yield "SHA512"; 
				
			}
			default -> {
				System.out.println("CertificadosUtils.firmaEnEC()\nNo se determino una firma");
				yield "SHA256";
			}
		};
				
		
		return curvaInit;
	}
	
	// --------------------------------------------------------------------
	// PARAMETROS DEL ARCHIVO CREADO

	public static String[] lista_codif_archivo = {"PEM", "DER"};
	public static String CODIF_ARCHIVO_PEM = "PEM";
	public static String CODIF_ARCHIVO_DER = "DER";
	
	//Bandera de Encryptado
	@Deprecated
	public static boolean es_encryptado = false;	
	
	@Deprecated
	//Extension de Certificado
	public static String [] formato_extension_certificado = {".cer", ".crt", ".der", ".pem"}; 
	
	@Deprecated
	//Extension de Llave Privada
	public static String [] formato_extension_PrivateKey = {".key", ".der", ".pem"};
	
	@Deprecated
	//Extension de Llave Pública -->> ¿Esto lo agrego Codeium?
	public static String [] formato_extension_publicKey = {".crt", ".der", ".pem"};
	
	//Extension de Almacen de LLaves
	@Deprecated
	public static String [] formato_extension_KeyStore = {".jks", ".p12", ".pfx" };
	
	//Extension de Solicitud de Certificado (CSR)
	@Deprecated
	public static String [] formato_extension_CSR = {".csr"};
	
	// --------------------------------------------------------------------
	// VALORES DEL SANs	
	
	public static String[] lista_datos_DN_del_X509 = {
			"CN","O","OU","L","ST","C" };
	
	public static final Map<String, String> mapa_lista_datos_DN_del_X509 () {
			
		Map<String, String> mapa =  new HashMap<>();	
		mapa.put( "CN", "Nombre del Titular");
		mapa.put( "O", "Organizacion");
		mapa.put( "OU", "Unidad Organizacional o subdivision de la organizacion");
		mapa.put( "L", "Ciudad o Localidad");
		mapa.put( "ST", "Estado o Provincia");
		mapa.put( "C", "Codigo de 2 letras del Pais");
		return mapa;
	}

	public static String [] lista_SANs_Predeterminados = {"DNS", "IP", "Email", "URI", "DirectoryName","RegisteredID", "otherName"};
	
	public static String [] lista_abrev_objetos_ASN1Encodable = { 
			"OID", "DERUTF8", "ASN1Integer", "ASN1Time", "ASN1Enum", "default"};
		
	public static final Map<String, ASN1Encodable> mapa_lista_abrev_objetos_ASN1Encodable(String valor) {	
		
		final Map<String, ASN1Encodable> mapa = new HashMap<String, ASN1Encodable>();
		
		mapa.put("OID", new ASN1ObjectIdentifier(valor));
		mapa.put("DERUTF8", new DERUTF8String(valor));
		mapa.put("ASN1Integer_v2", new ASN1Integer(0L));//Long
		mapa.put("ASN1Integer", new ASN1Integer(new BigInteger(valor)) );
		mapa.put("ASN1Time", new ASN1GeneralizedTime(valor));
		mapa.put("ASN1Enum_v2", new ASN1Enumerated(0));//Integer
		mapa.put("ASN1Enum", new ASN1Enumerated(new BigInteger(valor)));//BigInteger
		mapa.put("default", new DERUTF8String(valor));
		
		return mapa;
	}

	// --------------------------------------------------------------------
	// ALGORITMOS SIMETRICOS PARA ENCRIPTAR PRIVATE_KEY (PKCS#8)	
	
	/**
	 * Devuelve una lista de tipo {@code String[]}
	 * organizado por la generación del esquema
	 * @return {@link String}[]
	 */
	public static String [] algoritmo_simetrico = {
		/* --------------------------------------
		 * Esquema MODERNO PBES2 / PBKFD2
		 *  */
		// AES
		"AES_128_CBC",
		"AES_192_CBC",
		"AES_256_CBC",
		
		// 3DES
		"DES3_CBC",
		
		// SM4 (CBC moderno)
		//"SM4_CBC",
		
		/* --------------------------------------
		 * Esquema HISTORICO PBE
		 *  */
		
		// PBE históricos
		"PBE_SHA1_2DES",
		"PBE_SHA1_3DES",
		"PBE_SHA1_RC2_128",
		"PBE_SHA1_RC2_40",
		"PBE_SHA1_RC4_128",
		"PBE_SHA1_RC4_40"
	};
	
	/**
	 * Devuelve una lista de tipo {@code LinkedHashMap<String,ArrayList<String>>}
	 * organizado por la generación del esquema
	 * @return {@code LinkedHashMap<String, ArrayList<String>>}
	 */
	public static LinkedHashMap<String,ArrayList<String>> lista_generaciones_algoritmo_simetrico(){
		LinkedHashMap<String, ArrayList<String>> lista = new LinkedHashMap<String, ArrayList<String>>();
		ArrayList<String> esquemaPBES2 = new ArrayList<String>();
		ArrayList<String> esquemaPBE = new ArrayList<String>();
		
		// Esquema MODERNO PBES2 / PBKFD2
		esquemaPBES2.add("AES_128_CBC");
		esquemaPBES2.add("AES_192_CBC");
		esquemaPBES2.add("AES_256_CBC");
		esquemaPBES2.add("DES3_CBC");
		//esquemaPBES2.add("SM4_CBC");
		
		// Esquema HISTORICO PBE
		esquemaPBE.add("PBE_SHA1_2DES");
		esquemaPBE.add("PBE_SHA1_3DES");
		esquemaPBE.add("PBE_SHA1_RC2_128");
		esquemaPBE.add("PBE_SHA1_RC2_40");
		esquemaPBE.add("PBE_SHA1_RC4_128");
		esquemaPBE.add("PBE_SHA1_RC4_40");		
		
		lista.put("PBES2", esquemaPBES2);
		lista.put("PBE", esquemaPBE);
		
		return lista;
	}
		
	/**
	 * Devuelve una lista de tipo {@code LinkedHashMap<String,ArrayList<String>>}
	 * separado por la FAMILIA del esquema
	 * @return {@code LinkedHashMap<String,ArrayList<String>>}
	 */
	//public static LinkedHashMap<String,ArrayList<Object[]>> lista_familia_algoritmo_simetrico(){
	public static LinkedHashMap<String,Object[]> lista_familia_algoritmo_simetrico(){
		//LinkedHashMap<String, ArrayList<Object[]>> lista = new LinkedHashMap<String, ArrayList<Object[]>>();
		LinkedHashMap<String, Object[]> lista = new LinkedHashMap<String, Object[]>();
		ArrayList<Object[]> familiaAES = new ArrayList<Object[]>();
		ArrayList<Object[]> familia3DES = new ArrayList<Object[]>();
		//ArrayList<Object[]> familiaSM4 = new ArrayList<Object[]>();
		ArrayList<Object[]> familiaPBE = new ArrayList<Object[]>();
		
		// AES
		Object[] aes = { "AES_128_CBC", "AES_192_CBC", "AES_256_CBC" };
		
		// 3DES/DES3
		Object[] des3 = {"DES3_CBC"};
		
		// SM4 (CBC moderno)
		//Object[] sm4 = {"SM4_CBC"};
		
		// PBE históricos
		Object[] pbe = { "PBE_SHA1_2DES", "PBE_SHA1_3DES", 
				"PBE_SHA1_RC2_128", "PBE_SHA1_RC2_40", 
				"PBE_SHA1_RC4_128", "PBE_SHA1_RC4_40" };
		
		lista.put("AES", aes);
		lista.put("3DES", des3);
		//lista.put("SM4", familiaSM4);
		lista.put("PBE", pbe);
		
		return lista;
	}
	
	/**
	 * Devuelve un ASN1ObjectIdentifier correspondiente al algoritmo
	 * enviado como String
	 * @param nombre Nombre del algoritmo simetrico
	 * @return
	 */
	public static ASN1ObjectIdentifier elegirAlgoritmoSimetricoParaPKCS8(String nombre) {
		ASN1ObjectIdentifier algoritmo = null;
				
		JceOpenSSLPKCS8EncryptorBuilder jceOpenSSLPKCS8EncryptorBuilder 
		= 
		new JceOpenSSLPKCS8EncryptorBuilder(PKCS8Generator.AES_256_CBC);
        ;       
        
        
		algoritmo = switch(nombre) {
			
		//Esquema Moderno PBES2 / PBKDF2
			// AES
			case "AES_128_CBC"-> {
				yield PKCS8Generator.AES_256_CBC;
				}
			case "AES_192_CBC"-> {
				yield PKCS8Generator.AES_256_CBC;
				}
			case "AES_256_CBC"-> {
				yield PKCS8Generator.AES_256_CBC;
				}
			
			// 3DES
			case "DES3_CBC"-> {
				yield PKCS8Generator.AES_256_CBC;
				}
			// CBC moderno
			/* No existe la constante en la clase
			 * PKCS8Generator
			 * 
			 * case "SM4_CBC" -> {
				yield null;
			}*/
		//Esquemas Historicos PBE	
			// PBE históricos
			case "PBE_SHA1_2DES"-> {
				yield PKCS8Generator.AES_256_CBC;
				}
			case "PBE_SHA1_3DES"-> {
				yield PKCS8Generator.AES_256_CBC;
				}
			case "PBE_SHA1_RC2_128"-> {
				yield PKCS8Generator.AES_256_CBC;
				}
			case "PBE_SHA1_RC2_40"-> {
				yield PKCS8Generator.AES_256_CBC;
				}
			case "PBE_SHA1_RC4_128"-> {
				yield PKCS8Generator.AES_256_CBC;
				}
			case "PBE_SHA1_RC4_40"-> {
				yield PKCS8Generator.AES_256_CBC;
				}			
			
			default -> {
				yield PKCS8Generator.AES_256_CBC;
			}
			
		};
		
		return algoritmo;
	}

	// --------------------------------------------------------------------
	// METADATOS DEL ARCHIVO
	public static String[] lista_formatos_Charset= {"UTF-8", "ISO-8859-1"};
	
	//Formato de codificación de caraácteres para los archivos
	private static String charset = "UTF-8";
	
	/**
	 * Establece el formato de codificación de caraácteres 
	 * para los archivos, entre UTF-8 y ISO-8859-1
	 * @param formato String
	 */
	public static void estableceCharset(String formato) {
		charset = formato;
	}
	
	/**
	 * Establece el formato de codificación de caraácteres 
	 * para los archivos, entre UTF-8 y ISO-8859-1
	 * @param formato String
	 */
	public static void setCharset(String formato) {
		charset = formato;
	}
	
	/**
	 * Devuelve el formato de codificación de caraácteres 
	 * para los archivos configurado entre UTF-8 y ISO-8859-1
	 * @return String
	 */
	public static String obtenFormatoCharset() {
		return charset;
	}
	
	/**
	 * Devuevle el formato de codificación de caraácteres 
	 * para los archivos configurado entre UTF_8 y ISO-8859-1.
	 * @return String
	 */
	public static String getCharsetString() {
		return charset;
	}

	
	
	// --------------------------------------------------------------------
	//Ejemplos usando Templates (Genericos)
	public <T, U> ArrayList<T> met2(T obj, U obj2){
		ArrayList<T> a = new ArrayList();
		a.add(obj);
		a.add((T) obj2);

		U u_Clase = (U) obj2;
		String string = u_Clase.toString();
		return a;
	}
	
	public <T> ArrayList<T>  met(T obj){
		//return obj.toString();
			ArrayList<T> a = new ArrayList<>();
			a.add(0, obj);
			return a;
	} 
	
	public <T> T tipo(T obj) {
		CertificadosUtils c = new CertificadosUtils();
		//herramientaEnum jkt = c.herramientaEnum.JavaKeytool;
		CertificadosUtils.herramientaEnum jkt = CertificadosUtils.herramientaEnum.JavaKeytool;
		//jkt es del tipo CertificadosUtils.herramientaEnum
		return obj;
	}
	// --------------------------------------------------------------------
	
	//public static enum herramientaEnum { //ES VALIDO
	public enum herramientaEnum { //ES VALIDO
		BouncyCastle, 
		OpenSSL, 
		JavaKeytool{ 
			int value = 3; 
			float value2 = 3.0f;
			
			public String metodo() { 
				return "Java Keytool"; 
			} 
		}
	}
	
	
	public void test_metodos() {
		String [] arr = ALGORTIMOS_FAM_EC.GOST.algoritmos_de_clave;
		//String [] arr2 = ALGORTIMOS_FAM_EC.GOST.parametros_de_curva;
		//String [] arr3 = ALGORTIMOS_FAM_EC.GOST.algoritmos_de_firma;
		
		//String [] arr4 = ALGORTIMOS_FAM_EC.GOST.algoritmos_de_firma;
		
		
	}
	
}
