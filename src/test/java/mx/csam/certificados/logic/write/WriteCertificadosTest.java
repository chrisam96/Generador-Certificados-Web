package mx.csam.certificados.logic.write;

//import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assumptions.assumingThat;

import java.security.InvalidAlgorithmParameterException;
import java.security.KeyPair;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.SecureRandom;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TimeZone;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.bouncycastle.asn1.x500.X500Name;
import org.bouncycastle.operator.ContentSigner;
import org.bouncycastle.operator.jcajce.JcaContentSignerBuilder;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.MultiValueMap;

import mx.csam.certificados.utilerias.constantes.CertificadosUtils;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class WriteCertificadosTest {

	Logger logger = LoggerFactory.getLogger(getClass());
	
	@Test
	@Order(1)
	@DisplayName("Validar concatenarContenidoCertificado()")
	@Disabled
	void test1() {
		//WriteCertificados w = mock(WriteCertificados.class);
		WriteCertificados w = new WriteCertificados();
		String[] arr = {"Christian", "Demo", "Sistemas", "Metepec", "EdoMex", "MX"};
		String dN = "CN=Christian,O=Demo,OU=Sistemas,L=Metepec,ST=EdoMex,C=MX";
		
		assertEquals(dN, w.concatenarContenidoCertificado(arr));
	}

	//@Test
	@ParameterizedTest
	@MethodSource("datos_generarKeyPair")
	@Order(2)
	@DisplayName("Validar generarKeyPair()")
	@Disabled
	void test2(String alg, String[] datosKPG) {
		//WriteCertificados w = mock(WriteCertificados.class);
		WriteCertificados w = new WriteCertificados();
		
		try {
			System.out.println("Alg:"+alg+" KPG-tamClaveBits_o_curvaInit:"+datosKPG[0] + " KPG-tamSHA_o_genCurva:"+datosKPG[1] + " KPG-salt:"+datosKPG[2]);
			KeyPair kp = w.generaKeyPair(alg, datosKPG);
			System.out.println("algoritmo:" + kp.getPublic().getAlgorithm()+ "\n");
			//assertNotNull(w.generaKeyPair(alg, datosKPG));
			assertNotNull(kp);
		} catch (NoSuchAlgorithmException | IllegalArgumentException | InvalidAlgorithmParameterException
				| NoSuchProviderException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	static Stream<Arguments> datos_generarKeyPair(){
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
		 * RSA			|Tam Clave BITS	|				|
		 * RSA-PSS 		|Tam Clave BITS	|Tam Hash SHA	|Salt
		 * EC			|Curva Init		|				|
		 * GOST			|Curva Init 	|Gen Curva		|	 
		 * DSA			|Tam Clave BITS	|				|
		 */
		return Stream.of(
			Arguments.of(
				"RSA", 
				new String[] {"2048", "SHA-256", ""}
			),			
			Arguments.of(
				"RSA-PSS", 
				new String[] {"4096","SHA-512","64"}
			),
			Arguments.of(
				"DSA", 
				new String[] {"2048", "SHA-256", ""}
			),
			Arguments.of(
				"DSA", 
				new String[] {"3072", "SHA-256", ""}
			),			
			Arguments.of(
				"Ed25519", 
				new String[] {"4096","SHA-512","64"}
			),
			Arguments.of(
				"Ed448", 
				new String[] {"2048", "SHA-256", ""}
			)
			
		);
	}
	
	@Disabled
	@ParameterizedTest
	@MethodSource("datos_generarKeyPair_EC")
	@Order(3)
	@DisplayName("Validar generarKeyPair() (Curvas Elipticas EC y GOST)")	
	void test3(String alg, String[] datosKPG) {
		//WriteCertificados w = mock(WriteCertificados.class);
		WriteCertificados w = new WriteCertificados();
		
		try {
			System.out.println("Alg:"+alg+" KPG-tamClaveBits_o_curvaInit:"+datosKPG[0] + " KPG-tamSHA_o_genCurva:"+datosKPG[1] + " KPG-salt:"+datosKPG[2]);
			KeyPair kp = w.generaKeyPair(alg, datosKPG);
			System.out.println("algoritmo:" + kp.getPublic().getAlgorithm());
			//assertNotNull(w.generaKeyPair(alg, datosKPG));
			assertNotNull(kp);
		} catch (NoSuchAlgorithmException | IllegalArgumentException | InvalidAlgorithmParameterException
				| NoSuchProviderException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	static Stream<Arguments> datos_generarKeyPair_EC(){
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
		 * EC			|Curva Init		|				|
		 * GOST			|Curva Init 	|Gen Curva		|	 
		 */
		
		String[] arrCurvasEC = CertificadosUtils.concatenarArrays(
				CertificadosUtils.ALGORTIMOS_FAM_EC.SECG_o_NIST.NIST_mas_usadas,
				CertificadosUtils.ALGORTIMOS_FAM_EC.Brainpool.Brainpool_mas_usados
		);
		
		List<Arguments> lista = new ArrayList<Arguments>();
		
		//Se agregan los algoritmos de la familia EC(ECDSA)
		for (int i = 0; i < arrCurvasEC.length; i++) {
			String ec = arrCurvasEC[i];
			
			Arguments args = Arguments.of("EC", new String[] {ec, "", ""});
			lista.add(args);
		}
		
		// CURVAS GOST
		String[] arrGOST_tmp = CertificadosUtils.ALGORTIMOS_FAM_EC.GOST.algoritmos_de_clave;
		
		//Generaciones de Curva 
		String[] arrGOST = {
				arrGOST_tmp[0], 
				arrGOST_tmp[1].concat(" (256)"), 
				arrGOST_tmp[1].concat(" (512)")
		};
		
		//Devuelve un mapa de las generaciones de curva y sus parametros de curva respectivos
		MultiValueMap<String, String> listaGOST = CertificadosUtils.ALGORTIMOS_FAM_EC.GOST.parametros_de_curva_GOST();
		
		//Se agregan los algoritmos de la familia GOST
		for (int i = 0; i < arrGOST.length; i++) {
			//Obtiene una generacion de curva
			String genCurva = arrGOST[i];
			
			//Se devuelve una lista de "parametros de curva" de acuerdo a su generacion_de_curva
			List<String> listaGOSTtmp = listaGOST.get(genCurva);
			
			//for-interno para agregar cada par Gen.Curva/Parametro_de_Curva
			for (int j = 0; j < listaGOSTtmp.size(); j++) {
				//Obtiene un elemento de Parametro de Curva
				String curvaInit = listaGOSTtmp.get(j);
				
				//Crea la dupla
				Arguments args = Arguments.of("GOST", new String[] {
					curvaInit, 
					CertificadosUtils.ALGORTIMOS_FAM_EC.GOST.reversoCambioNombreSegunBits(genCurva), 
					""
				});
				lista.add(args);
			}
			
		}
		
		return lista.stream();
	}

	@Disabled
	@ParameterizedTest
	@MethodSource("datos_generarKeyPair")
	@Order(4)
	@DisplayName("Validar generaJcaContentSigner()")	
	void test4(String alg, String[] datosKPG) {
		//WriteCertificados w = mock(WriteCertificados.class);
		WriteCertificados w = new WriteCertificados();
		
		try {
			System.out.println("Alg:"+alg+" tamClaveBits_o_curvaInit:"+datosKPG[0] + " tamSHA_o_genCurva:"+datosKPG[1] + " salt:"+datosKPG[2]);
			JcaContentSignerBuilder jcs = w.generaJcaContentSigner(alg, datosKPG);
			System.out.println("Valor(memoria):" + jcs.toString() + "\n");
			//assertNotNull(w.generaKeyPair(alg, datosKPG));
			assertNotNull(jcs);
		} catch (NoSuchAlgorithmException | IllegalArgumentException | InvalidAlgorithmParameterException
				| NoSuchProviderException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Disabled
	@ParameterizedTest
	@MethodSource("datos_generarKeyPair_EC")
	@Order(5)
	@DisplayName("Validar generaJcaContentSigner() (Curvas Elipticas EC y GOST)")	
	void test5(String alg, String[] datosKPG) {
		//WriteCertificados w = mock(WriteCertificados.class);
		WriteCertificados w = new WriteCertificados();
		
		try {
			System.out.println("Alg:"+alg+" tamClaveBits_o_curvaInit:"+datosKPG[0] + " tamSHA_o_genCurva:"+datosKPG[1] + " salt:"+datosKPG[2]);
			JcaContentSignerBuilder jcs = w.generaJcaContentSigner(alg, datosKPG);
			System.out.println("Valor(memoria):" + jcs.toString() + "\n");
			//assertNotNull(w.generaKeyPair(alg, datosKPG));
			assertNotNull(jcs);
		} catch (NoSuchAlgorithmException | IllegalArgumentException | InvalidAlgorithmParameterException
				| NoSuchProviderException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	/*
		 * 		donde el formato es:
		 * 		[1]: ISO-8601 
		 * 			2026-05-08T15:42:10.123-05:00 
	 * */
	@Test
	//@ParameterizedTest
	//@MethodSource("datos_generarKeyPair_EC")
	@Order(6)
	@DisplayName("Validar convertirFecha() (Para la fecha de Inicio de Validez)")	
	void test6() {
		
		//Para Formatear la fecha estilo "2020-07-01T12:00:00+02:00"
		DateTimeFormatter formatter =
                DateTimeFormatter.
                ofPattern("yyyy-MM-dd'T'HH:mm:ssxxx")
                .withZone(ZoneOffset.UTC);
		
		/* PRUEBAS CON LAS FECHAS Y ZONAS HORARIAS
		 * 
		 * Al parecer TODOS LOS DATE(new Date()) nunca tienen 
		 * Zonas Horarias
		 * y esas son asignadas con objetos como el ZonedDateTime
		 * o el OffsetDateTime.
		 * 
		 * ZoneId.of("+02:00"), ZoneId.of("-05:00"), ZoneId.UTC
		 *  proporcionan tambien una Zona Horaria
		 *  
		 * Para configurar una zona horaria por defecto para toda la 
		 * aplicación Java desde código, sin pasar parámetros al 
		 * arrancar la JVM (-Duser.timezone=...) ni usar Maven/Gradle, 
		 * se puede establecer con TimeZone.setDefault() solo que se 
		 * debe pasar como parametro algun ZoneId 
		 * al metodo de TimeZone.getTimeZone():
		 * 
		 * TimeZone.setDefault(TimeZone.getTimeZone("America/Mexico_City")); 
		 * */
				
		//ZoneId zona = ZoneId.of("America/Mexico_City");
		//TimeZone.setDefault(TimeZone.getTimeZone("America/Mexico_City"));		
		//TimeZone.setDefault(TimeZone.getTimeZone("GMT"));		
		TimeZone.setDefault(TimeZone.getTimeZone(ZoneOffset.UTC));		
		System.out.println("ZoneId.systemDefault: "+ZoneId.systemDefault() + "\n");		
		
		
		WriteCertificados w = new WriteCertificados();
		
		Date nulo = w.crearFechaInicioValidez(null);
		assertNotNull(nulo, "Es nulo");
		//System.out.println("\nnulo: "+nulo.toGMTString());
		
		String sinHoraStr = "2026-05-08";
		Date sinHora = w.crearFechaInicioValidez(sinHoraStr);
		/*assertNotNull(sinHora, "Es nulo");
		assertEquals("2026-05-08T00:00:00+00:00", formatter.format(sinHora.toInstant()));*/
		//System.out.println("sinHora: "+sinHora.toGMTString());
		
		assumingThat(sinHora != null, () -> {
			assertEquals("2026-05-08T00:00:00+00:00", formatter.format(sinHora.toInstant()));
		});
		
		Date sinGMT = w.crearFechaInicioValidez("2026-01-12T00:00:00");
		assumingThat(sinGMT != null, () -> {
			assertEquals("2026-01-12T00:00:00+00:00", formatter.format(sinGMT.toInstant()));
		});
        
	}
	
	
	/*
	 * 		[2]: [0]_[1]_[2]_[3]_[4]_[5]_[6]
	 * 
	 * 		Diccionario del String[2] 
	 *			[0]: años
	 *			[1]: dias
	 *			[2]: horas
	 *			[3]: minutos
	 *			[4]: segundos
	 *			[5]: milisegundos
	 *			[6]: TimeZone (Offset) >> Descartar
	*/
	@Test
	//@ParameterizedTest
	//@MethodSource("datos_generarKeyPair_EC")
	@Order(7)
	@DisplayName("Validar crearFechaVigencia()")	
	@Disabled
	void test7() {
		//Para Formatear la fecha estilo "2020-07-01T12:00:00+02:00"
		DateTimeFormatter formatter =
                DateTimeFormatter.
                ofPattern("yyyy-MM-dd 'T' HH:mm:ssxxx")
                .withZone(ZoneOffset.UTC);
		
		TimeZone.setDefault(TimeZone.getTimeZone(ZoneOffset.UTC));		
		System.out.println("ZoneId.systemDefault: "+ZoneId.systemDefault() + "\n");		
		
		WriteCertificados w = new WriteCertificados();
		
		Date inicioVig = new Date(); 
		System.out.println("subTest-1\nInicioVig: "+formatter.format(inicioVig.toInstant()));
		Date nulo = w.crearFechaFinVigencia(null, inicioVig);
		assertNotNull(nulo, "Es nulo");
		
		String _fecha = "0_0_0_1_0_0_+02:00";
		Date unMinutoMas = w.crearFechaFinVigencia(_fecha, inicioVig);
		assumingThat(unMinutoMas != null, () -> {
			//assertEquals("2026-01-12T00:00:00+02:00", formatter.format(conGMT.toInstant()));
			Date tmp = Date.from( inicioVig.toInstant().plusMillis(60L * 1000L));
			System.out.println("\nsubTest-2\ntmp: " + formatter.format(tmp.toInstant()));
			System.out.println("sinGMT: " +formatter.format(unMinutoMas.toInstant()));			
		});
		
		_fecha = "0_0_0_1_30_0_+02:00";
		Date _90seg = w.crearFechaFinVigencia(_fecha, inicioVig);
		assumingThat(_90seg != null, () -> {
			//assertEquals("2026-01-12T00:00:00+02:00", formatter.format(conGMT.toInstant()));
			Date tmp = Date.from( inicioVig.toInstant().plusMillis(90L * 1000L));
			System.out.println("\nsubTest-3\ntmp: " + formatter.format(tmp.toInstant()));
			System.out.println("90seg: " +formatter.format(_90seg.toInstant()));			
		});
		
		
	}
	
	
	@Test
	//@ParameterizedTest
	//@MethodSource("datos_generarKeyPair_EC")
	@Disabled
	@Order(8)
	@DisplayName("Validar generarCuerpoDeCertificadoOrCsr() - Solo .cer")	
	void test8() {
		WriteCertificados w = new WriteCertificados();
		
		System.out.println("\nPRUEBA #8\n");
		/*
		 * GRUPO DE PARAMETROS A CREAR
		 * 
		 * >#1
		 * 3 param para el generarCuerpoDeCertificadoOrCsr (algoritmo, titularDN, datosKPG)
		 * - String algoritmo
		 * - String[] titularDN, emisorDN, -> Crearlo a partir del titularDN
		 * - String[] datosKPG
		 * 
		 * Eso generara 3 param de un array de objetos:
		 * 
		 * - X500Name = arr[0];
		 * - KeyPair = arr[1];
		 * - ContentSigner = arr[2];
		 * 
		 * necesarios para el 
		 * 		generarCertificado (x509, keypair, firmador, ...)		 * 
		 * 
		 * >#2	
		 * Crear 2 param extra 
		 * 		p/generaCertificado(..., emisorDN, vig_y_serial)
		 * - String[] emisorDN -> Crearlo a partir del titularDN
		 * - String[] vig_y_serial
		 * 
		 * >#3
		 * Boolean en true para el CerOrCsr
		 * 
		 * >#4
		 * Verificar que sean un notNull y su tipo de Clase: 
		 * 
		 * - Excepciones:
		 * java.security.cert.CertificateException
		 * 
		 * - Return:
		 * java.security.cert.X509Certificate
		 * -------------------------------------------------------------------
		 * NOTAS
		 * 
		 * Datos del DN:
		 * 
		 * CN = Nombre del Titular
		 * O = Organizacion
		 * OU = Unidad Organizacional o subdivision de la organizacion
		 * L = Ciudad o Localidad
		 * ST = Estado o Provincia 
		 * C = Codigo de 2 letras del Pais
		 * 
		 * Datos del vig_y_serial:
		 *
		 * [0]: No de serial
		 * [1]: Fecha de inicio de validez
		 * 		donde:
		 * 		Formatear la fecha estilo "2020-07-01T12:00:00+02:00"
		 * 
		 * [2]: Fecha de fin de vigencia
		 * 		donde el formato es:
		 * 		[1]: ISO-8601 
		 * 			2026-05-08T15:42:10.123-05:00
		 * 
		 * 		[2]: [0]_[1]_[2]_[3]_[4]_[5]_[6]
		 * 
		 * 		Diccionario del String[2] 
		 *			[0]: años
		 *			[1]: dias
		 *			[2]: horas
		 *			[3]: minutos
		 *			[4]: segundos
		 *			[5]: milisegundos
		 *			[6]: TimeZone (Offset)

		 * */
		
		//ALGORITMOS
		String [] algoritmo_asimetrico = {"RSA", "DSA", "EC", "Ed25519", "Ed448", "GOST", "RSA-PSS" };
		
		// titularDN | emisorDN
		String[] arr = {"Christian", "Demo", "Sistemas", "Metepec", "EdoMex", "MX"};
		String dn1 = "CN=Christian,O=Demo1,OU=Sistemas1,L=Metepec,ST=EdoMex,C=MX";
		String dn2 = "CN=Sebastian,O=Demo2,OU=Sistemas2,L=Toluca,ST=EdoMex,C=MX";
		String dn3 = "CN=Agueros,O=Demo3,OU=Sistemas3,L=Iztapalapa,ST=CDMX,C=MX";
		
		//datosKPG
		 HashMap<Integer, Object[]> datosKPG = new HashMap<Integer, Object[]>();
		 int cont = 0;
		 
		 //se agregan los algoritmos RSA; DSA; RSA-PSS; Ed448 y Ed25519
		 ArrayList<Arguments> arrLs = (ArrayList<Arguments>)
				 datos_generarKeyPair()
				 .collect(Collectors.toList());
		 
		 for (Arguments arg : arrLs) {						
			//Object tmp = new String[2];
			datosKPG.put(cont++, arg.get());
		}

		 //se agregan los algoritmos ECDSA y GOST
		arrLs = (ArrayList<Arguments>)
				 datos_generarKeyPair_EC()
				 .collect(Collectors.toList());
		 
		 for (Arguments arg : arrLs) {						
			//Object tmp = new String[2];
			datosKPG.put(cont++, arg.get());
		}
		 
		 
		ArrayList<Object[]> casos =  new ArrayList<Object[]>();
		
		for (int i = 0; i < datosKPG.size(); i++) {
			Object[] arrObj = {
				String.valueOf( datosKPG.get(i)[0]), //algoritmo
				arrAleatorio(dn1, dn2, dn3), //datosDN
				(String[]) datosKPG.get(i)[1], //datosKPG
				i
			};
			casos.add(arrObj);			
		}
		 
		
		 assertAll(
				 //Haciendo uso de la API Stream
				casos.stream().map(caso -> () -> {
					System.out.println("Datos:"
						+ " Algoritmo>>>" + String.valueOf(caso[0]) 
						+ " \ndatos DN>>>" + imprimirArreglos(caso[1]) 
						+ " \ndatos KPG>>>" + imprimirArreglos(caso[2], caso[0]) 
						+ "");
					
					assertNotNull(caso);
					assertInstanceOf(String.class, caso[0]);					
					assertInstanceOf(String[].class, caso[1]);					
					assertInstanceOf(String[].class, caso[2]);	
					
					/* Eso generara 3 param de un array de objetos:
					 * 
					 * - X500Name = arr[0];
					 * - KeyPair = arr[1];
					 * - ContentSigner = arr[2];
					 * 
					 * necesarios para el 
					 * 		generarCertificado (x509, keypair, firmador, ...)		 * 
					 */
					Object[] res = w.generarCuerpoDeCertificadoOrCsr(
						String.valueOf(caso[0]), 
						((String[])caso[1]), 
						((String[])caso[2])
					);

					System.out.println("\n");
					
					assertNotNull(res);
					assertInstanceOf(X500Name.class, res[0]);					
					assertInstanceOf(KeyPair.class, res[1]);					
					assertInstanceOf(ContentSigner.class, res[2]);	
					
				} )
		);
		 
		
		/*
		//Aparentemente hacen lo mismo
		//-----
		//Para saber si es de la misma clase o su hija
		assertInstanceOf(String.class, dn1);
		//Para saber si es de la misma clase o su hija
		assertTrue(dn1 instanceof String);
		//Para saber si es EXACTAMENTE la misma clase
		assertSame(String.class, dn1.getClass());
		*/
		
		//vig_y_serial
		String[] vigSerial1 = {"SERIAL", "2020-07-01T12:00:00+02:00", "0_0_0_1_0_0_"};
		String[] vigSerial2 = {"1", "2020-07-01T12:00:00+02:00", "0_0_0_1_0_0_"};
		String[] vigSerial3 = {null, "2020-07-01T12:00:00+02:00", "0_0_0_1_0_0_"};			
		
		boolean CERTIFICADO = true;
	}
	
	private <K,V> void imprimirMaps(Map<K, Object[]> p) {
		System.out.println("\n\n--------------------------");
		for (Entry<K, Object[]> elem : p.entrySet()) {
			System.out.println("Id:"+ elem.getKey() + " - Algoritmo:"
			+ String.valueOf(elem.getValue()[0]) 
			+ " - ValoresKPG:" + imprimirArreglos(elem.getValue()[1]
					, elem.getValue()[0] ));
		}
	}

	private String imprimirArreglos(Object array, Object algoritmo) {
		/*--------------------------------------------------------
		 * ALGORITMO	|Param 1		|Param 2		|Param 3
		 * --------------------------------------------------------
		 * RSA-PSS 		|Tam Clave BITS	|Tam Hash SHA	|Salt
		 * EC			|Curva Init		|				|
		 * GOST			|Curva Init 	|Gen Curva		|	 
		 * RSA			|Tam Clave BITS	|				|
		 * DSA			|Tam Clave BITS	|				|
		 * ---------------------------------------------------------*/
		//System.out.println("isArr?:" + Arrays.isArray(obj));
		String par1 = "", par2 = "", par3 = "";
		String name = String.valueOf(algoritmo);
		
		switch (name) {
		case "RSA-PSS", "RSA": {
			par1 = " > tamClaveBITS: ";
			par2 = " > tamHashSHA: ";
			par3 = " > salt: ";
			break;
		}
		case "EC": {
			par1 = " > curvaInit: ";
			break;
		}
		case "GOST": {
			par1 = " > curvaInit: ";
			par2 = " > genCurva: ";
			break;
		}
		case "DSA": {
			par1 = " > tamClaveBITS: ";
			break;
		}
		default: {			//throw new IllegalArgumentException("Valor desconocido: " + name);
			System.err.println("Valor desconocido: " + name);			
			break;
			}
		}
		String[] tmp = (String[]) array;		
		return par1 + tmp[0] + 
			par2 + tmp[1] + 
			par3 + tmp[2] + "|";
	}
	
	private String imprimirArreglos(Object obj) {
		String tmp = "";
		
		if(obj != null){
			//String[] array = (String[]) obj;
			Object[] array = (Object[]) obj;
		
			for (int i = 0; i < array.length; i++) {
				if(array[i] != null) {
					tmp = tmp.concat(String.valueOf( array[i].toString() ) );
				}else {
					tmp = tmp.concat("NULL");					
				}
				//-----
				if (i != array.length - 1) {
					tmp = tmp.concat(" | ");
				}
			}
			
		}else {
			tmp = "NULL";
		}
		return tmp;
	}

	private String[] deStringA_Array(String str) {
		
		String[] arr = str.split(",");
		for (int i = 0; i < arr.length; i++) {
			switch(i) {
				case 0: { 
					arr[0] = arr[0].replace("CN=", "");
					break;
					}
				case 1: { 
					arr[1] = arr[1].replace("O=", "");
					break;
				}
				case 2: { 					
					arr[2] = arr[2].replace("OU=", "");
					break;
					}
				case 3: { 
					arr[3] = arr[3].replace("L=", "");					
					break;
					}
				case 4: { 
					arr[4] = arr[4].replace("ST=", "");					
					break;
					}
				case 5: { 
					arr[5] = arr[5].replace("C=", "");					
					break;
					}
				default: {
					break;
				}
			}
		}				
		return arr;
	}
	
	private String[] arrAleatorio(String ... strings ){
		ArrayList<String[]> a = new ArrayList<String[]>();
		SecureRandom ran = new SecureRandom();
		int cont = 0;		 
		
		for(String str: strings) {
			if(str != null) {
				String[] tmp = deStringA_Array(str);
				a.add(tmp);
			}else {
				a.add(null);
			}
			cont++;
		}
		
		return a.get(ran.nextInt(cont));
	} 
	
	private String[] arrAleatorio(String[] ... strings ){
		ArrayList<String[]> a = new ArrayList<String[]>();
		SecureRandom ran = new SecureRandom();
		int cont = 0;		 
		
		for(String[] str: strings) {
			if(str != null) {				
				a.add(str);
			}else {
				a.add(null);
			}
			cont++;
		}
		
		return a.get(ran.nextInt(cont));
	} 
		
	@Test
	//@ParameterizedTest
	//@MethodSource("datos_generarKeyPair_EC")
	@Order(9)
	@DisplayName("Validar generarCertificadoOrCsr() - Solo .cer")	
	void test9() throws Exception {
		WriteCertificados w = new WriteCertificados();
		
		System.out.println("\nPRUEBA #9\n");
		/*
		 * GRUPO DE PARAMETROS A CREAR
		 * 
		 * >#1
		 * 3 param para el generarCuerpoDeCertificadoOrCsr (algoritmo, titularDN, datosKPG)
		 * - String algoritmo
		 * - String[] titularDN, emisorDN, -> Crearlo a partir del titularDN
		 * - String[] datosKPG
		 * 
		 * Eso generara 3 param de un array de objetos:
		 * 
		 * - X500Name = arr[0];
		 * - KeyPair = arr[1];
		 * - ContentSigner = arr[2];
		 * 
		 * necesarios para el 
		 * 		generarCertificado (x509, keypair, firmador, ...)		 * 
		 * 
		 * >#2	
		 * Crear 2 param extra 
		 * 		p/generaCertificado(..., emisorDN, vig_y_serial)
		 * - String[] emisorDN -> Crearlo a partir del titularDN
		 * - String[] vig_y_serial
		 * 
		 * >#3
		 * Boolean en true para el CerOrCsr.
		 * Boolean en false para el Cifrado.
		 * 
		 * >#4
		 * Verificar que su retorno sean un notNull y su tipo de Clase: 
		 * 
		 * - Excepciones:
		 * java.security.cert.CertificateException
		 * 
		 * - Return:
		 * java.security.cert.X509Certificate
		 * -------------------------------------------------------------------
		 * NOTAS
		 * 
		 * Datos del DN:
		 * 
		 * CN = Nombre del Titular
		 * O = Organizacion
		 * OU = Unidad Organizacional o subdivision de la organizacion
		 * L = Ciudad o Localidad
		 * ST = Estado o Provincia 
		 * C = Codigo de 2 letras del Pais
		 * 
		 * Datos del vig_y_serial:
		 *
		 * [0]: No de serial
		 * [1]: Fecha de inicio de validez
		 * 		donde:
		 * 		Formatear la fecha estilo "2020-07-01T12:00:00+02:00"
		 * 
		 * [2]: Fecha de fin de vigencia
		 * 		donde el formato es:
		 * 		[1]: ISO-8601 
		 * 			2026-05-08T15:42:10.123-05:00
		 * 
		 * 		[2]: [0]_[1]_[2]_[3]_[4]_[5]_[6]
		 * 
		 * 		Diccionario del String[2] 
		 *			[0]: años
		 *			[1]: dias
		 *			[2]: horas
		 *			[3]: minutos
		 *			[4]: segundos
		 *			[5]: milisegundos
		 *			[6]: TimeZone (Offset)

		 * */
		
		// >>> ALGORITMOS
		String [] algoritmo_asimetrico = {"RSA", "DSA", "EC", "Ed25519", "Ed448", "GOST", "RSA-PSS" };
		
		// >>> titularDN | emisorDN
		String[] arr = {"Christian", "Demo", "Sistemas", "Metepec", "EdoMex", "MX"};
		String dn1 = "CN=Christian,O=Demo1,OU=Sistemas1,L=Metepec,ST=EdoMex,C=MX";
		String dn2 = "CN=Sebastian,O=Demo2,OU=Sistemas2,L=Toluca,ST=EdoMex,C=MX";
		String dn3 = "CN=Agueros,O=Demo3,OU=Sistemas3,L=Iztapalapa,ST=CDMX,C=MX";
		
		//  >>> vig_y_serial
		String[] vigSerial1 = {"SERIAL", "2020-07-01T12:00:00+02:00", "0_0_0_1_0_0_"};
		String[] vigSerial2 = {"1", "2020-07-01T12:00:00+02:00", "0_0_0_1_0_0_"};
		String[] vigSerial3 = {null, "2020-07-01T12:00:00+02:00", "0_0_0_1_0_0_"};							

		// >>> datosKPG
		HashMap<Integer, Object[]> datosKPG = new HashMap<Integer, Object[]>();
		int cont = 0;
		 
		 	//se agregan los algoritmos RSA; DSA; RSA-PSS; Ed448 y Ed25519
		 ArrayList<Arguments> arrLs = (ArrayList<Arguments>)
				 datos_generarKeyPair()
				 .collect(Collectors.toList());
		 
		 for (Arguments arg : arrLs) {						
			//Object tmp = new String[2];
			datosKPG.put(cont++, arg.get());
		 }

		 	//se agregan los algoritmos ECDSA y GOST
		 arrLs = (ArrayList<Arguments>)
				 datos_generarKeyPair_EC()
				 .collect(Collectors.toList());
		 
		 for (Arguments arg : arrLs) {						
			//Object tmp = new String[2];
			datosKPG.put(cont++, arg.get());
		 }
		
		// >>> CerOrCsr
		boolean CERTIFICADO = true;
		
		// >>> datosCifrado
		/* Datos para cifrar
		 * 0: Booleano para determinar si se cifra (true) o no(false)
		 * 1: String con el ALGORITMO SIMETRICO para cifrar
		 * 2: String para la contraseña usada para cifrar la PrivateKey
		 * */
		Object[] datosPKCifrado = {false, "N/A", "N/A"};
		//boolean CIFRADO = Boolean.valueOf(datosCifrado[0].toString());
		//boolean CIFRADO = false;
		
		// >>> _SAN
		// No aplica
		 
		// >>> CASOS DE PRUEBA
		/// Cantidad de pruebas es dada por datosKPG.size()
		ArrayList<Object[]> casos =  new ArrayList<Object[]>();
		
		//Se agregan todos los datos a un caso de prueba
		for (int i = 0; i < datosKPG.size(); i++) {
			Object[] arrObj = {
				String.valueOf( datosKPG.get(i)[0]), //algoritmo: 0
				arrAleatorio(dn1, dn2, dn3), //titularDN : 1
				arrAleatorio(dn1, dn2, dn3, null), //emisorDN : 2
				arrAleatorio(vigSerial1, vigSerial2, vigSerial3),//vig_y_serial : 3
				
				(String[]) datosKPG.get(i)[1], //datosKPG : 4
				"N/A", //ruta: 5
				CERTIFICADO, //CerOrCsr : 6
				datosPKCifrado,//datosPKCifrado : 7
				"N/A",//_SAN : 8
				
				i //# de prueba : 9
			};
			casos.add(arrObj);			
		}
		 
		for(int i=0; i < casos.size(); i++) {
		 //assertAll(
				 //Haciendo uso de la API Stream
			Object[] caso = casos.get(i);
			System.out.println("\n\n"+caso.toString());
				//casos.stream().map(
				//	caso -> () -> {
			
			//Se imprimen un caso de prueba
			System.out.println("Datos:"
				+ " \nAlgoritmo>>> " + String.valueOf(caso[0]) 
				+ " \nTitular DN>>> " + imprimirArreglos(caso[1]) 
				+ " \nEmisor DN>>> " + imprimirArreglos(caso[2]) 
				+ " \nVig_y_serial>>> " + imprimirArreglos(caso[3]) 
				+ " \nDatosKPG>>> " + imprimirArreglos(caso[4], caso[0]) 
				+ " \nRuta>>> " + String.valueOf(caso[5])
				+ " \nCerOrCsr>>> " + String.valueOf(caso[6])  
				+ " \nDatos PK Cifrado>>> " + imprimirArreglos(caso[7])
				+ " \nSANs>>> " + String.valueOf(caso[8])
				+ " \nNo. Prueba>>> " + String.valueOf(caso[9])
				+ "");

			//Varaibles aplicadas en la prueba
			String _algoritmo = String.valueOf(caso[0]);
			String[] _titularDN = (String[]) caso[1]; 
			String[] _emisorDN = (String[]) caso[2]; 
			String[] _fecha_vig_y_serial = (String[]) caso[3];
			
			String[] _datosKPG = (String[])caso[4]; 
			String _ruta = String.valueOf(caso[5]);
			boolean _CERTIFICADO = Boolean.valueOf( String.valueOf(caso[6]) ); 
			
			Object[] _DATOS_PK_CIFRADO = (Object[])caso[7]; 
			Map<String, String[]> __SAN = null; //caso[8]
			int _num = Integer.parseInt(String.valueOf(caso[9]));
			
			System.out.println("\n");
					
					Object[] res = w.generarCertificadoOrCsr(
							_algoritmo, 
							_titularDN, 
							_emisorDN, 
							
							_fecha_vig_y_serial,							
							_datosKPG, 
							_CERTIFICADO, 
							
							_DATOS_PK_CIFRADO,							
							__SAN
					);
					/*
					//Aparentemente hacen lo mismo
					//-----
					//Para saber si es de la misma clase o su hija
					assertInstanceOf(String.class, dn1);
					//Para saber si es de la misma clase o su hija
					assertTrue(dn1 instanceof String);
					//Para saber si es EXACTAMENTE la misma clase
					assertSame(String.class, dn1.getClass());
					 */		
					assumingThat(res != null,
						() -> {
							assertNotNull(res);
							assertNotNull(res[0]);
							assertInstanceOf(Object.class, res[0]);
							assertInstanceOf(String.class, res[1]); 					
							assertInstanceOf(Long.class, res[2]);	
							assertInstanceOf(KeyPair.class, res[3]);
							assertInstanceOf(PublicKey.class, ((KeyPair) (res[3])).getPublic() );
							assertInstanceOf(PrivateKey.class, ((KeyPair) (res[3])).getPrivate() ); 
						}		
					);
				//} );
		//);
		 
		}
		

	}
	

}
