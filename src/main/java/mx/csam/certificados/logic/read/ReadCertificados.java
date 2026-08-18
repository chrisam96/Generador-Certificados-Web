package mx.csam.certificados.logic.read;

import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Path;
import java.security.PrivateKey;
import java.security.Security;

import org.bouncycastle.asn1.pkcs.PrivateKeyInfo;
import org.bouncycastle.asn1.x509.X509Name;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.bouncycastle.openssl.PEMParser;
import org.bouncycastle.openssl.jcajce.JcaPEMKeyConverter;
import org.bouncycastle.openssl.jcajce.JceOpenSSLPKCS8DecryptorProviderBuilder;
import org.bouncycastle.operator.InputDecryptorProvider;
import org.bouncycastle.operator.OperatorCreationException;
import org.bouncycastle.pkcs.PKCS8EncryptedPrivateKeyInfo;
import org.bouncycastle.pkcs.PKCSException;

import mx.csam.certificados.utilerias.constantes.CertificadosUtils;


public class ReadCertificados {
	
	private Path pathGral = Path.of("");
	
	public ReadCertificados() {
		
	}
	
	
	public PrivateKey descifrarPriavteKey(String ruta, String pass)
		throws IOException, IllegalArgumentException, 
		OperatorCreationException, PKCSException{
		
		//Por defecto, se agrega el Provider de BouncyCastle a la configuracion del Certificado
		Security.addProvider(new BouncyCastleProvider());
				
		CertificadosUtils.obtenFormatoCharset();
		
		/*
		try(
		//Lectura del archivo a partir de una ruta URI/String			
		InputStream is = Files.newInputStream(Path.of(""));
		//Convierte esa entrada de stream de bytes (InputStream<byte>)
		//	en un Reader 
		InputStreamReader isr = new InputStreamReader(is);
		//La lectura de bytes va hacia el PEMParser
		PEMParser ps =	new PEMParser(isr)) {
			
		} catch (IOException e) {
		
		}
		*/
		
		/*1. Abre el archivo con el PemParser*/
		Object pemObj = null; 
		
		try(FileReader fr = new FileReader(ruta);
			PEMParser pemParser = new PEMParser(fr)) {
			/*2. Lee el objeto PEM y convertirlo en un 
			 * objeto de BouncyCastle.*/
			pemObj = pemParser.readPemObject();
			
		} catch (IOException e) {
			e.printStackTrace();
			throw new IOException("""
				Imposible leer el archivo
				No es un objeto (PrivateKey - PKCS#8)
				PEM legible o la ruta esta mal"""
			);
		}
		
		 /*
         * 3. Comprueba que realmente tenemos un
         * PKCS8EncryptedPrivateKeyInfo.
         * 
         *  El PEM debe contener:
         *  -----BEGIN ENCRYPTED PRIVATE KEY-----
         */
		PKCS8EncryptedPrivateKeyInfo encriptadaPKInfo = null;
		//if(!(pemObj instanceof PKCS8EncryptedPrivateKeyInfo)) {
		if(pemObj instanceof PKCS8EncryptedPrivateKeyInfo) {
			encriptadaPKInfo = (PKCS8EncryptedPrivateKeyInfo) pemObj;
		}else {			
			throw new IllegalArgumentException ("El archivo no contiene una "
					+ "Private Key (PKCS#8) cifrada");
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
	
	
	public X509Name cargaCertificado(String ruta) {
		
		
		return null;
	}
	
	
}