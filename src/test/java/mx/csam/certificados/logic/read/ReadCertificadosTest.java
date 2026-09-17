package mx.csam.certificados.logic.read;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.security.NoSuchProviderException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.SecureRandom;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.Date;

import javax.security.auth.x500.X500Principal;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.parallel.Execution;
import org.junit.jupiter.api.parallel.ExecutionMode;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@Execution(ExecutionMode.SAME_THREAD) // Fuerza la ejecución secuencial en este hilo
class ReadCertificadosTest {

	@Order(1)
	@Test
	//@ParameterizedTest
	//@MethodSource("datos_generarKeyPair_EC")
	//@Disabled
	@DisplayName("Validar leerCertificado() - JUnit 5")
	public void test1() {

		System.out.println("""
				\n\n
				====================================================
				Test #1
				====================================================
				""");	
		
		String ruta = "D:\\eclipse-workspace\\Certificados\\Certificados-y-Private-Key\\PEM\\1789488505823_certificado_RSA-PSS+SHA512withRSAandMGF1_.cer";
		ReadCertificados rc = new ReadCertificados();
		ArrayList<Object> lista = null;
		
		try {
			listarArchivosPorExtension(null, null, true);
			lista = rc.leerCertificado(ruta);
			
		} catch (CertificateException e) {
			e.printStackTrace();
		} catch (NoSuchProviderException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		assertNotNull(lista, "No se recupero info");
		assertEquals(lista.size(), 10);
		assertInstanceOf(X509Certificate.class, (X509Certificate) lista.get(0));
		assertInstanceOf(String.class, (String) lista.get(1));
		assertInstanceOf(String.class, (String) lista.get(2));
		assertInstanceOf(PublicKey.class, (PublicKey) lista.get(3));
		
		assertInstanceOf(String.class, (String) lista.get(4));
		assertInstanceOf(X500Principal.class, (X500Principal) lista.get(5));
		assertInstanceOf(X500Principal.class, (X500Principal) lista.get(6));

		assertInstanceOf(Date.class, (Date) lista.get(7));
		assertInstanceOf(Date.class, (Date) lista.get(8));
		assertInstanceOf(String.class, (String) lista.get(9));
		/*
		 * //0: Se agrega el Certifcado (X509Certificate) : cert
		 * //1: Algoritmo de Firma (String) : cert.getSigAlgName()
		 * //2: Identificador OID del Algoritmo de Firma (String) : cert.getSigAlgName()
		 * //3: Public Key (java.security.PublicKey) : cert.getPublicKey()
		 * //4: Algoritmo de Public Key (String) : cert.getPublicKey().getAlgorithm()
		 * //5: Sujeto Dueño del Certificado ( javax.security.auth.x500.X500Principal ) : cert.getSubjectX500Principal()
		 * //6: Sujeto Emisor del Certificado (javax.security.auth.x500.X500Principal) : cert.getIssuerX500Principal()
		 * //7: Fecha de Inicio de Validez (Date) : cert.getNotBefore()
		 * //8: Fecha de Fin de Vigencia (Date) : cert.getNotAfter()
		 * //9: Tipo (String) : cert.getType()
		 * 
		 * */
	}
	
	@Order(2)
	@Test
	//@ParameterizedTest
	//@MethodSource("datos_generarKeyPair_EC")
	//@Disabled
	@DisplayName("Validar leerCertificado() - AssertJ")
	public void test2() {
		
		System.out.println("""
				\n\n
				====================================================
				Test #2
				====================================================
				""");	
		
		String ruta = "D:\\eclipse-workspace\\Certificados\\Certificados-y-Private-Key\\PEM\\1789488505823_certificado_RSA-PSS+SHA512withRSAandMGF1_.cer";
		ReadCertificados rc = new ReadCertificados();
		ArrayList<Object> lista = null;
		
		try {
			lista = rc.leerCertificado(ruta);
			//listarArchivosPorExtension(null, null);
			listarArchivosPorPrefijo(null, "1789488505823", true);
			listarArchivosPorExtPrefijo(null, "1789488505823", ".cer", true);
		} catch (CertificateException e) {
			e.printStackTrace();
		} catch (NoSuchProviderException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		assertThat(lista).hasSize(10);
		
	}
	
	private File[] listarArchivosPorExtension(String ruta, String ext, boolean show) 
		throws IOException {
		
		File[] archivos = null;
		
		//Ruta de la carpeta donde se buscará
		String rutaCarpeta = 
			ruta == null || ruta.isBlank() ? 
			"D:\\eclipse-workspace\\Certificados\\Certificados-y-Private-Key\\PEM"
			: ruta ; 
		
		//Extensión a buscar/filtar
		String extension = 
			ext == null || ext.isBlank() ? ".cer" : ext;		
		
		System.out.println("ruta carpeta: "+rutaCarpeta);
		System.out.println("extension: "+extension+"\n");
		
		//Objeto lógico File de la Ruta de la carpeta
		File carpeta = new File(rutaCarpeta);
		
		if(carpeta.exists() && carpeta.isDirectory()) {
			/*Se crea una función lambda para establecer una regla
			 * con la que se pueda filtrar por extensión (de archivo)
			 * */
			FilenameFilter filtro = (File dir, String name) -> name.toLowerCase().endsWith(extension)/*ret boolean*/;
			
			//Obtener la lista de archivos filtrados
			archivos = carpeta.listFiles(filtro);
			if(show) {
				if (archivos != null && archivos.length > 0 ) {
					System.out.println(archivos.length + " archivo(s) encontrado(s): ");
					for (File arch : archivos) {
						//System.out.println(arch.getName());
						System.out.println(arch.getAbsolutePath());
						//System.out.println(arch.getCanonicalPath());
						
					}
				} else {
					System.out.println("No hay archivos en la carpeta elegida.");
				}
			}
		}else {
			System.out.println("No es posible buscar en la carpeta elegida.");
		}
		
		
		return archivos;
	}

	
	private File[] listarArchivosPorPrefijo(String ruta, String pre, boolean show) 
			throws IOException {
		
		File[] archivos = null;
		
		//Ruta de la carpeta donde se buscará
		String rutaCarpeta = 
				ruta == null || ruta.isBlank() ? 
						"D:\\eclipse-workspace\\Certificados\\Certificados-y-Private-Key\\PEM"
						: ruta ; 
		
		//Prefijo a buscar/filtar
		String prefijo = 
				pre == null || pre.isBlank() ? "" : pre;		
		
		System.out.println("ruta carpeta: "+rutaCarpeta);
		System.out.println("prefijo: "+prefijo+"\n");
		
		//Objeto lógico File de la Ruta de la carpeta
		File carpeta = new File(rutaCarpeta);
		
		if(carpeta.exists() && carpeta.isDirectory()) {
			/*Se crea una función lambda para establecer una regla
			 * con la que se pueda filtrar por extensión (de archivo)
			 * */
			FilenameFilter filtro = (File dir, String name) -> name.toLowerCase().startsWith(prefijo)/*ret boolean*/;
			
			//Obtener la lista de archivos filtrados
			archivos = carpeta.listFiles(filtro);
			
			if(show) {
				if (archivos != null && archivos.length > 0 ) {
					System.out.println(archivos.length + " archivo(s) encontrado(s): ");
					for (File arch : archivos) {
						//System.out.println(arch.getName());
						System.out.println(arch.getAbsolutePath());
						//System.out.println(arch.getCanonicalPath());
						
					}
				} else {				
					System.out.println("No hay archivos en la carpeta elegida.");
				}
			}
		}else {
			System.out.println("No es posible buscar en la carpeta elegida.");
		}
		
		
		return archivos;
	}
	
		
	private File[] listarArchivosPorExtPrefijo(String ruta, String pre, String ext, boolean show) 
			throws IOException {
		
		File[] archivos = null;
		
		//Ruta de la carpeta donde se buscará
		String rutaCarpeta = 
				ruta == null || ruta.isBlank() ? 
						"D:\\eclipse-workspace\\Certificados\\Certificados-y-Private-Key\\PEM"
						: ruta ; 
		
		//Extensión a buscar/filtar
		String extension = 
			ext == null || ext.isBlank() ? ".cer" : ext;
		
		//Prefijo a buscar/filtar
		String prefijo = 
				pre == null || pre.isBlank() ? "" : pre;
		
		//Mostrar el Syso()
		
		
		System.out.println("ruta carpeta: "+rutaCarpeta);
		System.out.println("prefijo: "+prefijo+"\n");
		
		//Objeto lógico File de la Ruta de la carpeta
		File carpeta = new File(rutaCarpeta);
		
		if(carpeta.exists() && carpeta.isDirectory()) {
			/*Se crea una función lambda para establecer una regla
			 * con la que se pueda filtrar por extensión (de archivo)
			 * */
			FilenameFilter filtroExt = (File dir, String name) -> name.toLowerCase().endsWith(prefijo)/*ret boolean*/;
			FilenameFilter filtroPre = (File dir, String name) -> name.toLowerCase().startsWith(prefijo)/*ret boolean*/;
			
			//Obtener la lista de archivos filtrados
			archivos = carpeta.listFiles(
				//(/*File*/ dir, /*String*/ name) -> {
				(File dir, String name) -> {
					return name.toLowerCase().startsWith(prefijo) 
					&& name.toLowerCase().endsWith(extension);
					//return filtroExt && filtroPre; -> no sirve
				}
			);
			if(show) {
				if (archivos != null && archivos.length > 0 ) {
					System.out.println(archivos.length + " archivo(s) encontrado(s): ");
					for (File arch : archivos) {
						//System.out.println(arch.getName());
						System.out.println(arch.getAbsolutePath());
						//System.out.println(arch.getCanonicalPath());
						
					}
				} else {				
					System.out.println("No hay archivos en la carpeta elegida.");
				}
			}
		}else {
			System.out.println("No es posible buscar en la carpeta elegida.");
		}
				
		return archivos;
	}
	
	private File seleccionArchivo(File[] lista) {
		SecureRandom ran = new SecureRandom();
		int cont = lista.length;		 
		
		File arch = lista[ ran.nextInt(lista.length) ];		
		return arch;
	}
	
	@Order(3)
	@Test
	//@ParameterizedTest
	//@MethodSource("datos_generarKeyPair_EC")
	//@Disabled
	@DisplayName("Validar leerPublicKey() - codif: PEM, DER + N/A EncryptedPrivateKey")
	public void test3() {
		
		System.out.println("""
				\n\n
				====================================================
				Test #3
				====================================================
				""");	
		
		//String ruta = "D:\\eclipse-workspace\\Certificados\\Certificados-y-Private-Key\\PEM\\1789488505265_publica_RSA+SHA256withRSA_.pubkey";
		String ruta = "D:\\eclipse-workspace\\Certificados\\Certificados-y-Private-Key\\";
		ReadCertificados rc = new ReadCertificados();
		
		File[] lista = null;		
		PublicKey pk = null;
		
		try {
			/*-----------------------------------------------------
			 * PEM*/
			String codif = "PEM";
						
			lista = listarArchivosPorExtension(ruta+codif, ".pubkey", false);
			File arch = seleccionArchivo(lista);
			
			pk = rc.descifrarPublicKeyPEM(arch.getAbsolutePath());
			
			//Uso StringBuilder para no ahogar el pool String
			StringBuilder msj = new StringBuilder(		        
		    	//Datos de la Public Key
		    	"Ruta: " + arch.getAbsolutePath() + "\n" +
		    	"Nom: " + arch.getName() + "\n" +
		    	"Codif: " + codif + "\n" +
		    	"Clave pública: " + pk.getFormat() + "\n" +
		    	"Algoritmo de clave pública: "
		    		+ pk.getAlgorithm() + "\n" 
		    );
			
			assertNotNull(pk);
			
			System.out.println("""
				========= ReadCertificados.leerPublicKeyPEM =========
				
				====================================================
				PUBLIC KEY - PEM
				====================================================
				Imprimiendo contenido\n\n
				""" + "\n" + msj.toString());
			/*-----------------------------------------------------
			 * DER*/
			codif = "DER";
			
			lista = listarArchivosPorExtension(ruta+codif, ".pubkey", false);
			arch = seleccionArchivo(lista);
			
			pk = rc.descifrarPublicKeyDER(arch.getAbsolutePath());
			
			//Uso StringBuilder para no ahogar el pool String
			msj = new StringBuilder(		        
					//Datos de la Public Key
					"Ruta: " + arch.getAbsolutePath() + "\n" +
					"Nom: " + arch.getName() + "\n" +
					"Codif: " + codif + "\n" +
					"Clave pública: " + pk.getFormat() + "\n" +
					"Algoritmo de clave pública: "
					+ pk.getAlgorithm() + "\n" 
					);
			
			System.out.println("""				
				========= ReadCertificados.leerPublicKeyPEM =========
				
				====================================================
				PUBLIC KEY - DER
				====================================================
				Imprimiendo contenido\n\n
				""" + "\n" + msj.toString());
			
			assertNotNull(pk);
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}			
		
		//assertThat(lista).hasSize(10);
		
	}
	
	@Order(4)
	@Test
	//@ParameterizedTest
	//@MethodSource("datos_generarKeyPair_EC")
	//@Disabled
	@DisplayName("Validar leerPrivateKey() - codif: PEM, DER + Sin EncryptedPrivateKey")
	public void test4() {
		
		System.out.println("""
				\n\n
				====================================================
				Test #4
				====================================================
				""");	
		
		String ruta = "D:\\eclipse-workspace\\Certificados\\Certificados-y-Private-Key\\";
		ReadCertificados rc = new ReadCertificados();
		
		File[] lista = null;		
		PrivateKey pk = null;
		File arch = null;
		
		try {
			/*-----------------------------------------------------
			 * PEM*/
			String codif = "PEM";
						
			lista = listarArchivosPorExtension(ruta+codif, ".key", false);
			arch = seleccionArchivo(lista);
			
			pk = rc.descifrarPrivateKeyPEM(arch.getAbsolutePath());
			
			//Uso StringBuilder para no ahogar el pool String
			StringBuilder msj = new StringBuilder(		        
		    	//Datos de la Public Key
		    	"Ruta: " + arch.getAbsolutePath() + "\n" +
		    	"Nom: " + arch.getName() + "\n" +
		    	"Codif: " + codif + "\n" +
		    	"Clave pública: " + pk.getFormat() + "\n" +
		    	"Algoritmo de clave pública: "
		    		+ pk.getAlgorithm() + "\n" 
		    );
			
			assertNotNull(pk);
			
			System.out.println("""
				========= ReadCertificados.leerPublicKeyPEM =========
				
				====================================================
				PUBLIC KEY - PEM
				====================================================
				Imprimiendo contenido\n\n
				""" + "\n" + msj.toString());
			/*-----------------------------------------------------
			 * DER*/
			codif = "DER";
			
			lista = listarArchivosPorExtension(ruta+codif, ".key", false);
			arch = seleccionArchivo(lista);
			
			pk = rc.descifrarPrivateKeyDER(arch.getAbsolutePath());
			
			//Uso StringBuilder para no ahogar el pool String
			msj = new StringBuilder(		        
					//Datos de la Public Key
					"Ruta: " + arch.getAbsolutePath() + "\n" +
					"Nom: " + arch.getName() + "\n" +
					"Codif: " + codif + "\n" +
					"Clave pública: " + pk.getFormat() + "\n" +
					"Algoritmo de clave pública: "
					+ pk.getAlgorithm() + "\n" 
					);
			
			System.out.println("""				
				========= ReadCertificados.leerPublicKeyPEM =========
				
				====================================================
				PUBLIC KEY - DER
				====================================================
				Imprimiendo contenido\n\n
				""" + "\n" + msj.toString());
			
			assertNotNull(pk);
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}			
		//assertThat(lista).hasSize(10);
		
	}
	
}
