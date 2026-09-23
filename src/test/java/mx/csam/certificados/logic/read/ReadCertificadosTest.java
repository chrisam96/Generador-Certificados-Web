package mx.csam.certificados.logic.read;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

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
import java.util.Objects;
import java.util.function.Predicate;
import java.util.stream.Stream;

import javax.security.auth.x500.X500Principal;

import org.bouncycastle.operator.OperatorCreationException;
import org.bouncycastle.pkcs.PKCSException;
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
	@Disabled
	@DisplayName("Validar leerCertificado() - JUnit 5")
	public void test1() {

		System.out.println("""
				\n\n
				====================================================
				Test #1
				====================================================
				""");	
		
		String ruta = "D:\\eclipse-workspace\\Certificados\\Certificados-y-Private-Key\\";
		ReadCertificados rc = new ReadCertificados();
		ArrayList<Object> lista = new ArrayList<Object>();
		File[] listaFile = null;
		String codif = "PEM";
		
		try {
			listaFile = listarArchivosPorExtension(ruta+codif, true, ".cer");
			File arch = seleccionArchivo(listaFile);
			
			lista = rc.leerCertificado(arch.getAbsolutePath());
			
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
	@Disabled
	@DisplayName("Validar leerCertificado() - AssertJ")
	public void test2() {
		
		System.out.println("""
				\n\n
				====================================================
				Test #2
				====================================================
				""");	
		
		String ruta = "D:\\eclipse-workspace\\Certificados\\Certificados-y-Private-Key\\";
		
		ReadCertificados rc = new ReadCertificados();
		ArrayList<Object> lista = new ArrayList<Object>();
		File[] listaFile = null;
		String codif = "PEM";
		
		try {
			listaFile = listarArchivosPorExtension(ruta+codif, true, ".cer");
			File arch = seleccionArchivo(listaFile);
			
			lista = rc.leerCertificado(arch.getAbsolutePath());
			//listarArchivosPorExtension(null, null);
			//listarArchivosPorPrefijo(null, "1789488505823", true);
			//listarArchivosPorExtPrefijo(null, "1789488505823", ".cer", true);
		} catch (CertificateException e) {
			e.printStackTrace();
		} catch (NoSuchProviderException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		assertThat(lista).hasSize(10);
		
	}

	@Order(3)
	@Test
	@Disabled
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
						
			lista = listarArchivosPorExtension(ruta+codif, false, ".pubkey");
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
				====================================================
				PUBLIC KEY - PEM
				====================================================
				Imprimiendo contenido\n\n
				""" + "\n" + msj.toString());
			/*-----------------------------------------------------
			 * DER*/
			codif = "DER";
			
			lista = listarArchivosPorExtension(ruta+codif, false, ".pubkey");
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
	@Disabled
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
						
			//lista = listarArchivosPorExtension(ruta+codif, ".key", false);
			//lista = listarArchivosPorExtPrefijo(ruta+codif, false, "1789633884677", ".key");
			lista = listarArchivosPorPreConteExt(ruta+codif, true, null, ".key", "EC+");
			//lista = listarArchivosPorPreConteExt(ruta+codif, true, null, ".key", "DSA+");
			//lista = listarArchivosPorPreConteExt(ruta+codif, true, null, ".key", "RSA+");
			//lista = listarArchivosPorPreConteExt(ruta+codif, true, null, ".key", "RSA-PSS");
			//	(String ruta, boolean show, String pre, String ext, String cont  /*, String cont2*/  ) 
			arch = seleccionArchivo(lista);
			
			System.out.println("Archivo elegido: " + arch.getAbsolutePath());
			
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
				====================================================
				PRIVATE KEY - PEM
				====================================================
				Imprimiendo contenido\n\n
				""" + "\n" + msj.toString());
			/*-----------------------------------------------------
			 * DER*/
			codif = "DER";
			
			lista = listarArchivosPorExtension(ruta+codif, false, ".key");
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
				====================================================
				PRIVATE KEY - DER
				====================================================
				Imprimiendo contenido\n\n
				""" + "\n" + msj.toString());
			
			assertNotNull(pk);
			
		} catch (IOException e) {		
			e.printStackTrace();
		}		
	}
	
	
	@Order(5)
	@Test
	@Disabled
	@DisplayName("Validar leerPrivateKey() - codif: PEM, DER + Con EncryptedPrivateKey")
	public void test5() 
	throws IOException, IllegalArgumentException, OperatorCreationException, PKCSException{
		
		System.out.println("""
				\n\n
				====================================================
				Test #5
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
			String codif = "PEM_enc";
			
			lista = listarArchivosPorExtension(ruta+codif, false, ".key");
			arch = seleccionArchivo(lista);
			
			System.out.println("Archivo elegido: " + arch.getAbsolutePath() + "\n");
			
			pk = rc.descifrarEncryptedPrivateKeyPEM(arch.getAbsolutePath(), "pass");
			
			//Uso StringBuilder para no ahogar el pool String
			StringBuilder msj = new StringBuilder(		        
					//Datos de la Public Key
					"Ruta: " + arch.getAbsolutePath() + "\n" +
					"Nom: " + arch.getName() + "\n" +
					"Codif: " + codif + "\n" +
					"Clave privada: " + pk.getFormat() + "\n" +
					"Algoritmo de clave privada: "
					+ pk.getAlgorithm() + "\n" 
					);
			
			assertNotNull(pk);
			
			System.out.println("""								
				====================================================
				PRIVATE KEY - PEM + Encrypted
				====================================================
				Imprimiendo contenido\n\n
				""" + "\n" + msj.toString());
			/*-----------------------------------------------------
			 * DER*/
			codif = "DER_enc";
			
			lista = listarArchivosPorExtension(ruta+codif, false, ".key");
			arch = seleccionArchivo(lista);
			
			pk = rc.descifrarEncryptedPrivateKeyDER(arch.getAbsolutePath(), "pass");			
			
			//Uso StringBuilder para no ahogar el pool String
			msj = new StringBuilder(		        
					//Datos de la Public Key
					"Ruta: " + arch.getAbsolutePath() + "\n" +
					"Nom: " + arch.getName() + "\n" +
					"Codif: " + codif + "\n" +
					"Clave privada: " + pk.getFormat() + "\n" +
					"Algoritmo de clave privada: "
					+ pk.getAlgorithm() + "\n" 
					);
			
			System.out.println("""								
				====================================================
				PRIVATE KEY - DER + Encrypted
				====================================================
				Imprimiendo contenido\n\n
				""" + "\n" + msj.toString());
			
			assertNotNull(pk);
			
		} 
		catch (IOException e) {
			e.printStackTrace();
		}			
		catch (IllegalArgumentException e) {
			e.printStackTrace();
		} 
		catch (OperatorCreationException e) {
			e.printStackTrace();
		} 
		catch (PKCSException e) {			
			e.printStackTrace();
		}
		
	}
	
	
	@Order(6)
	@Test
	//@Disabled
	@DisplayName("Validar leerCertificado() - LISTA")
	public void test6() 
		throws CertificateException, IOException, NoSuchProviderException{
		
		System.out.println("""
			\n\n
			====================================================
			Test #6
			====================================================
			""");	
		
		String ruta = "D:\\eclipse-workspace\\Certificados\\Certificados-y-Private-Key\\";
		ReadCertificados rc = new ReadCertificados();
		ArrayList<Object> lista = new ArrayList<Object>();
		File[] listaFile = null;
		String codif = "DER";//"PEM"
				
		listaFile = listarArchivosPorExtension(ruta+codif, true, ".cer");
		
		for(File arch: listaFile) {				
			lista = rc.leerCertificado(arch.getAbsolutePath());
			
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
		}		
	
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
	
	
	@Order(7)
	@Test
	//@Disabled
	@DisplayName("Validar leerPublicKey() - LISTA: codif: PEM, DER + N/A EncryptedPrivateKey")
	public void test7() throws IOException{
		
		System.out.println("""
			\n\n
			====================================================
			Test #7
			====================================================
			""");	
		
		//String ruta = "D:\\eclipse-workspace\\Certificados\\Certificados-y-Private-Key\\PEM\\1789488505265_publica_RSA+SHA256withRSA_.pubkey";
		String ruta = "D:\\eclipse-workspace\\Certificados\\Certificados-y-Private-Key\\";
		ReadCertificados rc = new ReadCertificados();
		
		File[] lista = null;		
		PublicKey pk = null;
		
		StringBuilder msj = null;
		/*-----------------------------------------------------
		 * PEM*/
		String codif = "PEM";
					
		lista = listarArchivosPorExtension(ruta+codif, false, ".pubkey");
		
		for(File arch : lista) {
			//File arch = seleccionArchivo(lista);
			
			pk = rc.descifrarPublicKeyPEM(arch.getAbsolutePath());
			
			assertNotNull(pk);
			assertInstanceOf(PublicKey.class, pk);
			
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
				====================================================
				PUBLIC KEY - PEM
				====================================================
				Imprimiendo contenido\n\n
				""" + "\n" + msj.toString());
		}
		/*-----------------------------------------------------
		 * DER*/
		codif = "DER";
			
		lista = listarArchivosPorExtension(ruta+codif, true, ".pubkey");
		//arch = seleccionArchivo(lista);
		
		for(File arch : lista) {
			pk = rc.descifrarPublicKeyDER(arch.getAbsolutePath());
			
			assertNotNull(pk);
			assertInstanceOf(PublicKey.class, pk);
			
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
				====================================================
				PUBLIC KEY - DER
				====================================================
				Imprimiendo contenido\n\n
				""" + "\n" + msj.toString());			
		}
	}
	
	
	@Order(8)
	@Test
	//@Disabled
	@DisplayName("Validar leerPrivateKey() - LISTA: codif: PEM, DER + Sin EncryptedPrivateKey")
	public void test8() throws IOException{
		
		System.out.println("""
			\n\n
			====================================================
			Test #8
			====================================================
			""");	
		
		String ruta = "D:\\eclipse-workspace\\Certificados\\Certificados-y-Private-Key\\";
		ReadCertificados rc = new ReadCertificados();
		
		File[] lista = null;		
		PrivateKey pk = null;
		StringBuilder msj = null;
		
		/*-----------------------------------------------------
		 * PEM*/
		String codif = "PEM";
		lista = listarArchivosPorExtension(ruta+codif, true, ".key");
		
		for(File arch : lista) {
			System.out.println("Archivo elegido PEM: " + arch.getAbsolutePath());
			 
			pk = rc.descifrarPrivateKeyPEM(arch.getAbsolutePath());
			
			assertNotNull(pk);
			assertInstanceOf(PrivateKey.class, pk);
			
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
				====================================================
				PRIVATE KEY - PEM
				====================================================
				Imprimiendo contenido\n\n
				""" + "\n" + msj.toString());
		}
		
		/*-----------------------------------------------------
		 * DER*/
		codif = "DER";
		lista = listarArchivosPorExtension(ruta+codif, true, ".key");
		
		for(File arch : lista) {
			System.out.println("Archivo elegido DER: " + arch.getAbsolutePath());
			
			pk = rc.descifrarPrivateKeyDER(arch.getAbsolutePath());
			
			assertNotNull(pk);
			assertInstanceOf(PrivateKey.class, pk);
			
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
				====================================================
				PRIVATE KEY - DER
				====================================================
				Imprimiendo contenido\n\n
				""" + "\n" + msj.toString());
		}
	}

	@Order(9)
	@Test
	@Disabled
	@DisplayName("Validar leerPrivateKey() - LISTA : codif: PEM, DER + Con EncryptedPrivateKey")
	public void test9() 
	throws IOException, IllegalArgumentException, OperatorCreationException, PKCSException{
		
		System.out.println("""
			\n\n
			====================================================
			Test #9
			====================================================
			""");
		
		String ruta = "D:\\eclipse-workspace\\Certificados\\Certificados-y-Private-Key\\";
		ReadCertificados rc = new ReadCertificados();
		
		File[] lista = null;		
		PrivateKey pk = null;		
		
		StringBuilder msj = null;
		/*-----------------------------------------------------
		 * PEM*/
		String codif = "PEM_enc";
		lista = listarArchivosPorExtension(ruta+codif, true, ".key");
		
		for(File arch : lista) {	
			System.out.println("Archivo elegido PEM: " + arch.getAbsolutePath() + "\n");
			
			pk = rc.descifrarEncryptedPrivateKeyPEM(arch.getAbsolutePath(), "pass");
			assertNotNull(pk);
			assertInstanceOf(PrivateKey.class, pk);
			
			//Uso StringBuilder para no ahogar el pool String
			msj = new StringBuilder(		        
				//Datos de la Public Key
				"Ruta: " + arch.getAbsolutePath() + "\n" +
				"Nom: " + arch.getName() + "\n" +
				"Codif: " + codif + "\n" +
				"Clave privada: " + pk.getFormat() + "\n" +
				"Algoritmo de clave privada: "
				+ pk.getAlgorithm() + "\n" 
			);			
			
			System.out.println("""								
				====================================================
				PRIVATE KEY - PEM + Encrypted
				====================================================
				Imprimiendo contenido\n\n
				""" + "\n" + msj.toString());
		}
		
		/*-----------------------------------------------------
		 * DER*/
		codif = "DER_enc";	
		lista = listarArchivosPorExtension(ruta+codif, true, ".key");
					
		for(File arch: lista) {	
			pk = rc.descifrarEncryptedPrivateKeyDER(arch.getAbsolutePath(), "pass");			
			assertNotNull(pk);
			assertInstanceOf(PrivateKey.class, pk);
			
			//Uso StringBuilder para no ahogar el pool String
			msj = new StringBuilder(		        
				//Datos de la Public Key
				"Ruta: " + arch.getAbsolutePath() + "\n" +
				"Nom: " + arch.getName() + "\n" +
				"Codif: " + codif + "\n" +
				"Clave privada: " + pk.getFormat() + "\n" +
				"Algoritmo de clave privada: "
				+ pk.getAlgorithm() + "\n" 
			);
			
			System.out.println("""								
				====================================================
				PRIVATE KEY - DER + Encrypted
				====================================================
				Imprimiendo contenido\n\n
				""" + "\n" + msj.toString());
		}
		
	}
	

	@Order(10)
	@Test
	@Disabled
	@DisplayName("Validar Excepcion determinaPEMoDER_y_encriptadoPKCS8() - .cer")
	public void test10() 
	throws CertificateException, IOException, NoSuchProviderException{
		
		System.out.println("""
			\n\n
			====================================================
			Test #10
			====================================================
			""");	
		
		String ruta = "D:\\eclipse-workspace\\Certificados\\Certificados-y-Private-Key\\";
		ReadCertificados rc = new ReadCertificados();
		Boolean[] lista = null;
		File[] listaFile = null;
		
		String codif = "PEM";
		listaFile = listarArchivosPorExtension(ruta+codif, true, ".cer");
		
		for(File arch: listaFile) {				
			//lista = rc.determinaPEMoDER_y_encriptadoPKCS8(arch.getAbsolutePath());
			
			assertThrows(
				IllegalArgumentException.class, 
				() -> rc.determinaPEMoDER_y_encriptadoPKCS8(arch.getAbsolutePath())
			);
		}
		
		codif = "DER";
		listaFile = listarArchivosPorExtension(ruta+codif, true, ".cer");
		
		for(File arch: listaFile) {				
			//lista = rc.determinaPEMoDER_y_encriptadoPKCS8(arch.getAbsolutePath());
			
			assertThrows(
				IllegalArgumentException.class, 
				() -> rc.determinaPEMoDER_y_encriptadoPKCS8(arch.getAbsolutePath())
			);
		}		
		
		codif = "PEM_enc";
		listaFile = listarArchivosPorExtension(ruta+codif, true, ".cer");
		
		for(File arch: listaFile) {				
			//lista = rc.determinaPEMoDER_y_encriptadoPKCS8(arch.getAbsolutePath());
			
			assertThrows(
				IllegalArgumentException.class, 
				() -> rc.determinaPEMoDER_y_encriptadoPKCS8(arch.getAbsolutePath())
			);
		}
		
		listaFile = listarArchivosPorExtension(ruta+codif, true, ".txt");
		
		for(File arch: listaFile) {				
			//lista = rc.determinaPEMoDER_y_encriptadoPKCS8(arch.getAbsolutePath());
			
			assertThrows(
				IOException.class, 
				() -> rc.determinaPEMoDER_y_encriptadoPKCS8(arch.getAbsolutePath())
			);
		}
		
		codif = "DER_enc";
		listaFile = listarArchivosPorExtension(ruta+codif, true, ".cer");
		
		for(File arch: listaFile) {				
			//lista = rc.determinaPEMoDER_y_encriptadoPKCS8(arch.getAbsolutePath());
			
			assertThrows(
				IllegalArgumentException.class, 
				() -> rc.determinaPEMoDER_y_encriptadoPKCS8(arch.getAbsolutePath())
			);
		}		
	
		listaFile = listarArchivosPorExtension(ruta+codif, true, ".txt");
		
		for(File arch: listaFile) {				
			//lista = rc.determinaPEMoDER_y_encriptadoPKCS8(arch.getAbsolutePath());
			
			assertThrows(
				IOException.class, 
				() -> rc.determinaPEMoDER_y_encriptadoPKCS8(arch.getAbsolutePath())
			);
		}

	}
	
	
	@Order(11)
	@Test
	@Disabled
	@DisplayName("Validar Excepcion leerPrivateKey() - .cer, .txt, .pubkey")
	public void test11() 
			throws CertificateException, IOException, NoSuchProviderException{
		
		System.out.println("""
			\n\n
			====================================================
			Test #11
			====================================================
			""");	
		
		String ruta = "D:\\eclipse-workspace\\Certificados\\Certificados-y-Private-Key\\";
		ReadCertificados rc = new ReadCertificados();
		Boolean[] lista = null;
		File[] listaFile = null;
		
		String codif = "PEM";
		
		listaFile = listarArchivosPorExtension(ruta+codif, true, ".cer");
		for(File arch: listaFile) {				
			//lista = rc.determinaPEMoDER_y_encriptadoPKCS8(arch.getAbsolutePath());
			assertThrows(
				IllegalArgumentException.class, 
				() -> rc.leerPrivateKey(arch.getAbsolutePath(), null)
			);
		}
		
		listaFile = listarArchivosPorExtension(ruta+codif, true, ".pubkey");
		for(File arch: listaFile) {				
			//lista = rc.determinaPEMoDER_y_encriptadoPKCS8(arch.getAbsolutePath());
			assertThrows(
				IllegalArgumentException.class, 
				() -> rc.leerPrivateKey(arch.getAbsolutePath(), null)
			);
		}
		
		codif = "DER";
		
		listaFile = listarArchivosPorExtension(ruta+codif, true, ".cer");
		for(File arch: listaFile) {				
			//lista = rc.determinaPEMoDER_y_encriptadoPKCS8(arch.getAbsolutePath());
			assertThrows(
				IllegalArgumentException.class, 
				() -> rc.leerPrivateKey(arch.getAbsolutePath(), null)
			);
		}
		
		listaFile = listarArchivosPorExtension(ruta+codif, true, ".pubkey");
		for(File arch: listaFile) {				
			//lista = rc.determinaPEMoDER_y_encriptadoPKCS8(arch.getAbsolutePath());
			assertThrows(
				IllegalArgumentException.class, 
				() -> rc.leerPrivateKey(arch.getAbsolutePath(), null)
			);
		}		
		
		codif = "PEM_enc";

		listaFile = listarArchivosPorExtension(ruta+codif, true, ".cer");
		for(File arch: listaFile) {				
			//lista = rc.determinaPEMoDER_y_encriptadoPKCS8(arch.getAbsolutePath());
			assertThrows(
				IllegalArgumentException.class, 
				() -> rc.leerPrivateKey(arch.getAbsolutePath(), null)
			);
		}
		
		listaFile = listarArchivosPorExtension(ruta+codif, true, ".pubkey");
		for(File arch: listaFile) {				
			//lista = rc.determinaPEMoDER_y_encriptadoPKCS8(arch.getAbsolutePath());
			assertThrows(
				IllegalArgumentException.class, 
				() -> rc.leerPrivateKey(arch.getAbsolutePath(), null)
			);
		}
		
		listaFile = listarArchivosPorExtension(ruta+codif, true, ".txt");		
		for(File arch: listaFile) {				
			//lista = rc.determinaPEMoDER_y_encriptadoPKCS8(arch.getAbsolutePath());
			
			assertThrows(
				IOException.class, 
				() -> rc.leerPrivateKey(arch.getAbsolutePath(), null)
			);
		}
		
		codif = "DER_enc";
		
		listaFile = listarArchivosPorExtension(ruta+codif, true, ".cer");
		for(File arch: listaFile) {				
			//lista = rc.determinaPEMoDER_y_encriptadoPKCS8(arch.getAbsolutePath());
			assertThrows(
				IllegalArgumentException.class, 
				() -> rc.leerPrivateKey(arch.getAbsolutePath(), null)
			);
		}
		
		listaFile = listarArchivosPorExtension(ruta+codif, true, ".pubkey");
		for(File arch: listaFile) {				
			//lista = rc.determinaPEMoDER_y_encriptadoPKCS8(arch.getAbsolutePath());
			assertThrows(
				IllegalArgumentException.class, 
				() -> rc.leerPrivateKey(arch.getAbsolutePath(), null)
			);
		}
		
		listaFile = listarArchivosPorExtension(ruta+codif, true, ".txt");
		for(File arch: listaFile) {				
			//lista = rc.determinaPEMoDER_y_encriptadoPKCS8(arch.getAbsolutePath());
			
			assertThrows(
				IOException.class, 
				() -> rc.leerPrivateKey(arch.getAbsolutePath(), null)
			);
		}
		
	}
	
	
	
	@Order(12)
	@Test
	@Disabled
	@DisplayName("Validar Excpecion leerPublicKey() - .cer, .txt, .key")
	public void test12() 
			throws CertificateException, IOException, NoSuchProviderException{
		
		System.out.println("""
		\n\n
		====================================================
		Test #12
		====================================================
		""");
		
		String ruta = "D:\\eclipse-workspace\\Certificados\\Certificados-y-Private-Key\\";
		ReadCertificados rc = new ReadCertificados();
		Boolean[] lista = null;
		File[] listaFile = null;
		
		String codif = "PEM";
		
		listaFile = listarArchivosPorExtension(ruta+codif, true, ".cer");
		for(File arch: listaFile) {				
			//lista = rc.determinaPEMoDER_y_encriptadoPKCS8(arch.getAbsolutePath());
			assertThrows(
				IllegalArgumentException.class, 
				() -> rc.leerPublicKey( arch.getAbsolutePath() )
			);
		}
		
		listaFile = listarArchivosPorExtension(ruta+codif, true, ".key");
		for(File arch: listaFile) {				
			//lista = rc.determinaPEMoDER_y_encriptadoPKCS8(arch.getAbsolutePath());
			assertThrows(
				IllegalArgumentException.class, 
				() -> rc.leerPublicKey( arch.getAbsolutePath() )
			);
		}
		
		codif = "DER";
		
		listaFile = listarArchivosPorExtension(ruta+codif, true, ".cer");
		for(File arch: listaFile) {				
			//lista = rc.determinaPEMoDER_y_encriptadoPKCS8(arch.getAbsolutePath());
			assertThrows(
				IllegalArgumentException.class, 
				() -> rc.leerPublicKey( arch.getAbsolutePath() )
			);
		}
		
		listaFile = listarArchivosPorExtension(ruta+codif, true, ".key");
		for(File arch: listaFile) {				
			//lista = rc.determinaPEMoDER_y_encriptadoPKCS8(arch.getAbsolutePath());
			assertThrows(
				IllegalArgumentException.class, 
				() -> rc.leerPublicKey( arch.getAbsolutePath() )
			);
		}		
		
		codif = "PEM_enc";
		
		listaFile = listarArchivosPorExtension(ruta+codif, true, ".cer");
		for(File arch: listaFile) {				
			//lista = rc.determinaPEMoDER_y_encriptadoPKCS8(arch.getAbsolutePath());
			assertThrows(
				IllegalArgumentException.class, 
				() -> rc.leerPublicKey( arch.getAbsolutePath() )
			);
		}
		
		listaFile = listarArchivosPorExtension(ruta+codif, true, ".key");
		for(File arch: listaFile) {				
			//lista = rc.determinaPEMoDER_y_encriptadoPKCS8(arch.getAbsolutePath());
			assertThrows(
				IllegalArgumentException.class, 
				() -> rc.leerPublicKey( arch.getAbsolutePath() )
			);
		}
		
		listaFile = listarArchivosPorExtension(ruta+codif, true, ".txt");		
		for(File arch: listaFile) {				
			//lista = rc.determinaPEMoDER_y_encriptadoPKCS8(arch.getAbsolutePath());
			
			assertThrows(
				IOException.class, 
				() -> rc.leerPublicKey( arch.getAbsolutePath() )
			);
		}
		
		codif = "DER_enc";
		
		listaFile = listarArchivosPorExtension(ruta+codif, true, ".cer");
		for(File arch: listaFile) {				
			//lista = rc.determinaPEMoDER_y_encriptadoPKCS8(arch.getAbsolutePath());
			assertThrows(
				IllegalArgumentException.class, 
				() -> rc.leerPublicKey( arch.getAbsolutePath() )
			);
		}
		
		listaFile = listarArchivosPorExtension(ruta+codif, true, ".key");
		for(File arch: listaFile) {				
			//lista = rc.determinaPEMoDER_y_encriptadoPKCS8(arch.getAbsolutePath());
			assertThrows(
				IllegalArgumentException.class, 
				() -> rc.leerPublicKey( arch.getAbsolutePath() )
			);
		}
		
		listaFile = listarArchivosPorExtension(ruta+codif, true, ".txt");
		for(File arch: listaFile) {				
			//lista = rc.determinaPEMoDER_y_encriptadoPKCS8(arch.getAbsolutePath());
			assertThrows(
				IOException.class, 
				() -> rc.leerPublicKey( arch.getAbsolutePath() )
			);
		}
		
	}
	
	
	
	@Order(13)
	@Test
	//@Disabled
	@DisplayName("Validar validarValidezCertificado() - LISTA")
	public void test13() 
		throws CertificateException, IOException, NoSuchProviderException{
		
		System.out.println("""
			\n\n
			====================================================
			Test #13
			====================================================
			""");	
		
		String ruta = "D:\\eclipse-workspace\\Certificados\\Certificados-y-Private-Key\\";
		ReadCertificados rc = new ReadCertificados();
		ArrayList<Object> lista = new ArrayList<Object>();
		X509Certificate cert = null;
		File[] listaFile = null;
		
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
		 * */
		
		String codif = "PEM";
		listaFile = listarArchivosPorExtension(ruta+codif, true, ".cer");		
		for(File arch: listaFile) {
			lista = rc.leerCertificado(arch.getAbsolutePath());
			cert = (X509Certificate) lista.get(0); 			
			assertEquals(true, rc.validarValidezCertificado(cert));
		}
		
		codif = "DER";
		listaFile = listarArchivosPorExtension(ruta+codif, true, ".cer");		
		for(File arch: listaFile) {
			lista = rc.leerCertificado(arch.getAbsolutePath());
			cert = (X509Certificate) lista.get(0); 			
			assertEquals(true, rc.validarValidezCertificado(cert));
		}		
		
		codif = "PEM_enc";
		listaFile = listarArchivosPorExtension(ruta+codif, true, ".cer");		
		for(File arch: listaFile) {
			lista = rc.leerCertificado(arch.getAbsolutePath());
			cert = (X509Certificate) lista.get(0); 			
			assertEquals(true, rc.validarValidezCertificado(cert));
		}		
		
		codif = "DER_enc";
		listaFile = listarArchivosPorExtension(ruta+codif, true, ".cer");		
		for(File arch: listaFile) {
			lista = rc.leerCertificado(arch.getAbsolutePath());
			cert = (X509Certificate) lista.get(0); 			
			assertEquals(true, rc.validarValidezCertificado(cert));
		}
	}
	
	@Order(14)
	@Test
	//@Disabled
	@DisplayName("Validar validarFirmaDelCertificado() - LISTA")
	public void test14() 
		throws IOException, CertificateException, NoSuchProviderException, 
		IllegalArgumentException, OperatorCreationException, PKCSException
	{
		
		System.out.println("""
			\n\n
			====================================================
			Test #14
			====================================================
			""");	
		
		String ruta = "D:\\eclipse-workspace\\Certificados\\Certificados-y-Private-Key\\";
		ReadCertificados rc = new ReadCertificados();
		
		//Lista de archivos
		File[] listaCert = null;		
		
		//Objetos de los Archivos		
		X509Certificate cert = null;
		PublicKey pk = null;
		Archivos lista_arch = null;
		
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
		 * */
		
		String codif = "PEM";
		listaCert = listarArchivosPorExtension(ruta+codif, false, ".cer");			
		for(File arch: listaCert) {
			lista_arch = listaArchivosMismoPrefijo(arch, rc, null);	
			
			cert = lista_arch.certificado();			
			pk = lista_arch.publicKey();
			
			assertEquals(true, rc.validarFirmaDelCertificado(cert, pk));
		}		
		
		codif = "DER";
		listaCert = listarArchivosPorExtension(ruta+codif, false, ".cer");			
		for(File arch: listaCert) {
			lista_arch = listaArchivosMismoPrefijo(arch, rc, null);	
			
			cert = lista_arch.certificado();			
			pk = lista_arch.publicKey();
			
			assertEquals(true, rc.validarFirmaDelCertificado(cert, pk));			
		}		
		
		codif = "PEM_enc";
		listaCert = listarArchivosPorExtension(ruta+codif, false, ".cer");			
		for(File arch: listaCert) {
			lista_arch = listaArchivosMismoPrefijo(arch, rc, "pass");
			
			cert = lista_arch.certificado();			
			pk = lista_arch.publicKey();
			
			assertEquals(true, rc.validarFirmaDelCertificado(cert, pk));
		}		
		
		codif = "DER_enc";
		listaCert = listarArchivosPorExtension(ruta+codif, false, ".cer");			
		for(File arch: listaCert) {
			lista_arch = listaArchivosMismoPrefijo(arch, rc, "pass");
			
			cert = lista_arch.certificado();			
			pk = lista_arch.publicKey();
			
			assertEquals(true, rc.validarFirmaDelCertificado(cert, pk));
		}
		
	}
	
	@Order(15)
	@Test
	//@Disabled
	@DisplayName("Validar validarParPublicKeys() - LISTA")
	public void test15() 
			throws IOException, CertificateException, NoSuchProviderException, 
			IllegalArgumentException, OperatorCreationException, PKCSException
	{
		
		System.out.println("""
			\n\n
			====================================================
			Test #15
			====================================================
			""");	
		
		String ruta = "D:\\eclipse-workspace\\Certificados\\Certificados-y-Private-Key\\";
		ReadCertificados rc = new ReadCertificados();
		
		//Lista de archivos
		File[] listaCert = null;
		
		//Objetos de los Archivos
		X509Certificate cert = null;
		PublicKey pk = null;
		Archivos lista_arch = null;
		
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
		 * */
		
		String codif = "PEM";
		listaCert = listarArchivosPorExtension(ruta+codif, false, ".cer");			
		for(File arch: listaCert) {
			lista_arch = listaArchivosMismoPrefijo(arch, rc, null);	
			
			cert = lista_arch.certificado();			
			pk = lista_arch.publicKey();
			
			assertEquals(true, rc.validarParPublicKeys(cert, pk));			
		}		
		
		codif = "DER";
		listaCert = listarArchivosPorExtension(ruta+codif, false, ".cer");			
		for(File arch: listaCert) {
			lista_arch = listaArchivosMismoPrefijo(arch, rc, null);	
			
			cert = lista_arch.certificado();			
			pk = lista_arch.publicKey();
			
			assertEquals(true, rc.validarParPublicKeys(cert, pk));
		}		
		
		codif = "PEM_enc";
		listaCert = listarArchivosPorExtension(ruta+codif, false, ".cer");			
		for(File arch: listaCert) {
			lista_arch = listaArchivosMismoPrefijo(arch, rc, "pass");
			
			cert = lista_arch.certificado();			
			pk = lista_arch.publicKey();
			
			assertEquals(true, rc.validarParPublicKeys(cert, pk));
		}		
		
		codif = "DER_enc";
		listaCert = listarArchivosPorExtension(ruta+codif, false, ".cer");			
		for(File arch: listaCert) {
			lista_arch = listaArchivosMismoPrefijo(arch, rc, "pass");
			
			cert = lista_arch.certificado();			
			pk = lista_arch.publicKey();
			
			assertEquals(true, rc.validarParPublicKeys(cert, pk));
		}
		
	}
	

	
	@Order(16)
	@Test
	//@Disabled
	@DisplayName("Validar validarParDeKeys() - LISTA")
	public void test16() 
			throws IOException, CertificateException, NoSuchProviderException, 
			IllegalArgumentException, OperatorCreationException, PKCSException
	{
		
		System.out.println("""
			\n\n
			====================================================
			Test #16 - validarParDeKeys
			====================================================
			""");	
		
		String ruta = "D:\\eclipse-workspace\\Certificados\\Certificados-y-Private-Key\\";
		ReadCertificados rc = new ReadCertificados();
		
		//Lista de archivos
		File[] listaCert = null;
		
		//Objetos de los Archivos
		ArrayList<Object> lista = null;
		Archivos lista_arch = null;
		X509Certificate cert = null;
		PublicKey pubk = null;
		PrivateKey prik = null;
		String algfirma = null;
		
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
		 * */
		
		String codif = "PEM";
		listaCert = listarArchivosPorExtension(ruta+codif, false, ".cer");			
		for(File arch: listaCert) {
			lista_arch = listaArchivosMismoPrefijo(arch, rc, null);
			//lista = rc.leerCertificado(arch.getAbsolutePath());
			
			cert = lista_arch.certificado();
			algfirma = cert.getSigAlgName();
			pubk = lista_arch.publicKey();
			prik = lista_arch.privateKey();
			
			assertEquals(true, rc.validarParDeKeys(prik, pubk, algfirma));			
		}		
		
		codif = "DER";
		listaCert = listarArchivosPorExtension(ruta+codif, false, ".cer");			
		for(File arch: listaCert) {
			lista_arch = listaArchivosMismoPrefijo(arch, rc, null);	
			
			cert = lista_arch.certificado();
			algfirma = cert.getSigAlgName();
			pubk = lista_arch.publicKey();
			prik = lista_arch.privateKey();
			
			assertEquals(true, rc.validarParDeKeys(prik, pubk, algfirma));		}		
		
		codif = "PEM_enc";
		listaCert = listarArchivosPorExtension(ruta+codif, false, ".cer");			
		for(File arch: listaCert) {
			lista_arch = listaArchivosMismoPrefijo(arch, rc, "pass");
			
			cert = lista_arch.certificado();
			algfirma = cert.getSigAlgName();
			pubk = lista_arch.publicKey();
			prik = lista_arch.privateKey();
			
			assertEquals(true, rc.validarParDeKeys(prik, pubk, algfirma));
		}		
		
		codif = "DER_enc";
		listaCert = listarArchivosPorExtension(ruta+codif, false, ".cer");			
		for(File arch: listaCert) {
			lista_arch = listaArchivosMismoPrefijo(arch, rc, "pass");
			
			cert = lista_arch.certificado();
			algfirma = cert.getSigAlgName();
			pubk = lista_arch.publicKey();
			prik = lista_arch.privateKey();
			
			assertEquals(true, rc.validarParDeKeys(prik, pubk, algfirma));
		}
		
	}
	

	
	/*******************************************************************
	 * U T I L I D A D E S
	*******************************************************************/
	
	private File[] listarArchivosPorExtension(String ruta, boolean show, String ext) 
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
		
		System.out.println("\n\nruta carpeta: "+rutaCarpeta);
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
	
	private File[] listarArchivosPorPrefijo(String ruta, boolean show, String pre) 
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
		
	private File[] listarArchivosPorExtPrefijo(String ruta, boolean show, String ext, String pre) 
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
			Predicate<String> fPrefijo = name -> name.toLowerCase().startsWith(prefijo); 
			//Predicate<String> fContiene = name -> name.toLowerCase().contains(ruta); 
			Predicate<String> fExtension = name -> name.toLowerCase().endsWith(extension); 
			
			
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
	
	private File[] listarArchivosPorPreConteExt (String ruta, boolean show, String pre, String ext, String cont/*, String cont2*/) 
	throws IOException {
		
		File[] archivos = null;
		
		//Mostrar el Syso()
		System.out.println("ruta carpeta: "+ruta);
		System.out.println("prefijo: "+pre);
		System.out.println("extension: "+ext);
		System.out.println("contiene?: "+cont+"\n");
		
		//Objeto lógico File de la Ruta de la carpeta
		File carpeta = new File(ruta);
		
		if(carpeta.exists() && carpeta.isDirectory()) {
			/*Se crea una función lambda para establecer una regla
			 * con la que se pueda filtrar por extensión (de archivo)
			 * */
			Predicate<String> fPrefijo = name -> name.toLowerCase().startsWith(pre); 
			Predicate<String> fContiene = name -> name.contains(cont); 
			//Predicate<String> fContiene2 = name -> name.toLowerCase().contains(cont2); 
			Predicate<String> fExtension = name -> name.toLowerCase().endsWith(ext); 
			
			/*Se agrupan las claúsulas del filtro (Predicates)
			 * 
			 * -Metiendo null en el Stream 
			 * es para representar "este predicado no aplica".
			 * 
			 * -El .filter(Objects::nonNull) elimina los null 
			 * antes del reduce().
			 * 
			 * -s -> true es importante, ya que si todos los 
			 * inputs son null, se obtiene un predicado que acepta 
			 * cualquier nombre (parametro "name")
			 * */
			Predicate<String> agrupador = Stream.of
			(
				pre != null ? fPrefijo : null,
				ext != null ? fExtension : null,
				cont != null ? fContiene : null
				//cont2 != null ? fContiene2 : null
			)
			.filter(Objects::nonNull)
			.reduce(s -> true, Predicate::and);
			
			//Obtener la lista de archivos filtrados
			archivos = carpeta.listFiles(				
				(File dir, String name) -> {
						/*return name.toLowerCase().startsWith(pre) 
								&& name.toLowerCase().endsWith(ext);*/
					return agrupador.test(name);
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
		
		System.out.println("==============================================================================\n");
		return archivos;
	}
	
	private File seleccionArchivo(File[] lista) {
		SecureRandom ran = new SecureRandom();
		int cont = lista.length;		 
		
		File arch = lista[ ran.nextInt(lista.length) ];		
		return arch;
	}

	private String[] partirNombreArchivo(String nom) {
		return nom.split("_");
	}
	
	/**
	 * Devuelve una lista de objetos de los archivos leídos del 
	 * Certificado, PublicKey y PrivateKey
	 * 
	 * La lista se conforma de:
	 *<pre> 
	 * 0: Certifcado (X509Certificate)
	 * 1: Public Key (java.security.PublicKey)
	 * 2: Private Key (java.security.PrivateKey)
	 * 3: Password
	 *</pre>
	 * @param ruta
	 * @param nombre
	 * @param rc
	 * @return Archivos {@code (record)}
	 * @throws IOException
	 * @throws CertificateException
	 * @throws NoSuchProviderException
	 * @throws IllegalArgumentException
	 * @throws OperatorCreationException
	 * @throws PKCSException
	 */
	private Archivos listaArchivosMismoPrefijo(/*String ruta, String nombre,*/
			File _file, ReadCertificados rc, String pass) 
		throws IOException, CertificateException, NoSuchProviderException, 
		IllegalArgumentException, OperatorCreationException, PKCSException
	{
		X509Certificate cert = null; 
		PublicKey pubk = null;
		PrivateKey prik = null;
		Object etc = null;
		
		System.out.println("parent path: \n" + _file.getParent());
		//System.out.println("getName: \n" + _file.getName());
		//System.out.println("absolute path: \n" + _file.getAbsolutePath());
		System.out.println("\n\n");
		
		//Obtenemos el prefijo que comparten todos los archivos
		String prefijo = _file.getName().split("_")[0];
		String ruta = _file.getParent();
		
		//Recupera una lista de los archivos con el mismo prefijo
		File[] listaArch = listarArchivosPorPrefijo(ruta, true, prefijo);
		
		//Separa a cada archivo con base a su extension
		for(File arch: listaArch) {
			
			String nomComp = arch.getAbsolutePath();
			String nom = arch.getName();
			
			System.out.println("\nnom: " + nom);
			
			switch(nom) {
				case String a when a.endsWith(".cer") ->{
					cert = 
						(X509Certificate) rc.leerCertificado(nomComp).get(0);					
				}
				case String a when a.endsWith(".pubkey") ->{
					pubk = rc.leerPublicKey(nomComp);
				}
				case String a when a.endsWith(".key") ->{
					prik = rc.leerPrivateKey(nomComp, pass);					
				}
				case String a when a.endsWith(".txt") ->{
					etc = arch;
				}
				default -> {
					System.err.println("Archivo erroneo: " + nom);
					throw new IllegalArgumentException("El archivo no es de ningún formato reconocible.");
				}
			}
			
			System.out.println("-------------------------------------------------------------\n");
		}
		System.out.println("""
		=================================FIN DE LECTURA DE ARCHIVOS===============================
		================================ listaArchivosMismoPrefijo() =============================
		==========================================================================================\n				
		""");
		
		Archivos archivos = new Archivos(cert, pubk, prik, etc);
		
		return archivos;
	}
	
	private record Archivos(
		X509Certificate certificado,
		PublicKey publicKey,
		PrivateKey privateKey,
		Object etc
	) 
	{
		/* NOTA IMPORTANTE
		 * 
		 * Cualquier constructor sobrecargado debe delegar al 
		 * constructor canónico directa o indirectamente. 
		 * 
		 * No puedes hacer asignaciones de componentes 
		 * como si fuera una clase normal:
		 *
		 * public ArchivosCertificado(X509Certificate cert) {
		 *  	this.certificado = cert; // ❌
		 *  }
		 * */
		
		/*Constructor CANONICO EXPLICITO */
		public Archivos(
				X509Certificate certificado,
				PublicKey publicKey,
				PrivateKey privateKey,
				Object etc
			) 
			{
				this.certificado = certificado;			
				this.publicKey = publicKey;			
				this.privateKey = privateKey;			
				this.etc = etc;
			/* Con Objects.requireNonNull(T, String) se obliga
			 * a que los objetos pasados no sean nulos, o sino,
			 * desplegará el mensaje del segundo parametro
			 * junto a un NullPointerException
			 * * /
			/*	
				this.certificado = Objects.requireNonNull(
					certificado, 
					"El certificado no puede ser null."
				);
				
				this.pubk = Objects.requireNonNull(
					pubk, 
					"La PublicKey no puede ser null."
				);
				
				this.priv = Objects.requireNonNull(
					priv, 
					"La PrivateKey no puede ser null."
				);
				*/			
			}
		
		/*Constructor sobrecargado EXPLICITO */
		public Archivos(
			X509Certificate certificado,
			PublicKey publicKey,
			PrivateKey privateKey
		) 
		{
			this(certificado, publicKey, privateKey, null);
		}
		
		/*Constructor sobrecargado EXPLICITO */
		public Archivos(
			X509Certificate certificado,
			PublicKey publicKey							
		) 
		{
			this(certificado, publicKey, null, null);
		}
		
		/*Constructor sobrecargado EXPLICITO */
		public Archivos(				
			PublicKey publicKey,
			PrivateKey privateKey
		) 
		{
			this(null, publicKey, privateKey, null);
		}

		/*Constructor CANONICO COMPACTO 
		 * 
		 * En Java, un record solo puede tener un constructor compacto, 
		 * porque el constructor compacto representa específicamente al 
		 * constructor canónico del record 
		 * (el que ocupa todos los parametros).
		 * 
		 * 
		 * * /
		public Archivos 
		{
			/* Con Objects.requireNonNull(T, String) se obliga
			 * a que los objetos pasados no sean nulos, o sino,
			 * desplegará el mensaje del segundo parametro
			 * junto a un NullPointerException
			 * * /
			Objects.requireNonNull(
				certificado, 
				"El certificado no puede ser null."
			);
			
			Objects.requireNonNull(
				pubk, 
				"La PublicKey no puede ser null."
			);
			
			Objects.requireNonNull(
				priv, 
				"La PrivateKey no puede ser null."
			);
			
			/* OMITIDO para permitir su nulidad,
			 * SIN EMBARGO, el constructor lo incluye 
			 * IMPLICITO
			 * 
			 * this.etc = etc;* /			
		}*/
		
	}
}
