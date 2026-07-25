package mx.csam.certificados.utilerias.constantes;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.not;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertLinesMatch;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
//import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assumptions.assumingThat;

import java.util.ArrayList;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.util.MultiValueMap;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class CertificadosUtilsTest {

	// ALGORITMOS ASIMETRICOS PARA KEY_PAIR_GENERATOR
	
	@Test
	@Order(value = 1)
	@DisplayName("Lista de algoritmos")
	void test() {
		String[] algoritmo_asimetrico = CertificadosUtils.algoritmo_asimetrico;
		
		//se supone hacen lo mismo
		assertThat(algoritmo_asimetrico).isNotNull(); //assertThat(cheese, is(not(nullValue(X.class))))
		assertTrue(algoritmo_asimetrico != null);
	}
	
	@Test
	@Order(value = 2)
	@DisplayName("MenuGUI - Lista de algoritmos")
	void test2() {
		//fail("Not yet implemented");
		assumingThat(CertificadosUtils.lista_menu_algoritmo_asimetrico() != null, 
			() -> {
				assertEquals(
					"EC", 
					CertificadosUtils.lista_menu_algoritmo_asimetrico().get("ECDSA")
				);
			}
		);
	}
	
	// --------------------------------------------------------------------
	// INICIALIZADORES PARA KEY_PAIR_GENERATOR
	@Test
	@Order(3)
	@DisplayName("Lista de tamalos de bits para el KeyPairGenerator")
	void test3() {
		MultiValueMap<String, String> map = CertificadosUtils.lista_tamanio_Bits_KPG_algoritmo_asimetrico();
		String[] tamaniosKPG = {"1024", "2048", "3072", "4096", "6144", "8192"};;
		
		ArrayList<String> listaEC = new ArrayList<>();
		ArrayList<String[]> matrizListaEC = new ArrayList<String[]>();
			matrizListaEC.add(CertificadosUtils.ALGORTIMOS_FAM_EC.SECG_o_NIST.NIST_mas_usadas);
			matrizListaEC.add(CertificadosUtils.ALGORTIMOS_FAM_EC.Brainpool.Brainpool_mas_usados);
		
		for (int i = 0;  i < matrizListaEC.size(); i++) {
			for (int j = 0; j < matrizListaEC.get(i).length; j++) {
				listaEC.add(matrizListaEC.get(i)[j]);
			}			
		}				
		
		assertAll("3 tipos de tamaños KPG ", 
			() -> assertTrue(CertificadosUtils.tamanio_de_clave_KPG != null),
			() -> assertTrue(map.get("Ed448").isEmpty()),
			//Compara array de String
			() -> assertLinesMatch(
					listaEC, 
					CertificadosUtils.lista_tamanio_Bits_KPG_algoritmo_asimetrico().get("EC"), 
					"No coinciden las List<String>"
				),
			//Compara arrays en general
			() -> assertArrayEquals(tamaniosKPG, CertificadosUtils.tamanio_de_clave_KPG, "Los tamaños no coinciden")
		);
	}
	
	@Test
	@Order(4)
	@Disabled
	@DisplayName("Validar tamaño de la Sal")
	public void test4() {		
		/*
		 * Hash			hLen
			SHA-1		20 bytes
			SHA-224		28 bytes
			SHA-256		32 bytes
			SHA-384		48 bytes
			SHA-512		64 bytes
			SHA-512/224	28 bytes
			SHA-512/256	32 bytes
		 * */
		
		//assertFalse(CertificadosUtils.validarSalt_TamSHA_ClaveBits(null, null, null));
		/* Sirve pero como ese método esta creado accidentalmente con el patron 
		 * Result, pues ya no lanzan las excepciones.
		 * 
		assertThrows(NumberFormatException.class, () -> {
			CertificadosUtils.validarSalt_TamSHA_ClaveBits(null, null, "3 2");
		}, "");
		*/
		String tamHashSHA = "SHA-256", tamClaveBits = "2048", tamSalt = "32";
		assertFalse(CertificadosUtils.validarSalt_TamSHA_ClaveBits(null, null, "3 2"));
		assertFalse(CertificadosUtils.validarSalt_TamSHA_ClaveBits(null, null, "-32"));
		assertFalse(CertificadosUtils.validarSalt_TamSHA_ClaveBits("SHA-256", "2048", "32000"));
		assertFalse(CertificadosUtils.validarSalt_TamSHA_ClaveBits(null, null, "32"));
		assertFalse(CertificadosUtils.validarSalt_TamSHA_ClaveBits("SHA-256", "letra", "32"));
	}

	@Test
	@Order(5)
	@DisplayName("Validar lista de los MGF1ParameterSpec validos en RSA-PSS")
	public void test5() {	
		assertNull(CertificadosUtils.listaPSSParameterSpec().get("MD2"));
		assertNull(CertificadosUtils.listaPSSParameterSpec().get("MD5"));
		assertNotNull(CertificadosUtils.listaPSSParameterSpec().get("SHA-256"));
		assertNotNull(CertificadosUtils.listaPSSParameterSpec().get("SHA3-256"));
	}
	
	@Test
	@Order(6)
	@DisplayName("Validar tamaños Máx y Min de la Clave de Bits en DSA")
	public void test6() {	
		assertFalse(CertificadosUtils.validarEnDSAMinMaxTamClave("letras"));
		assertFalse(CertificadosUtils.validarEnDSAMinMaxTamClave("4096"));
		assertFalse(CertificadosUtils.validarEnDSAMinMaxTamClave("1020"));
		assertTrue(CertificadosUtils.validarEnDSAMinMaxTamClave("1024"));
		assertTrue(CertificadosUtils.validarEnDSAMinMaxTamClave("2048"));
		assertTrue(CertificadosUtils.validarEnDSAMinMaxTamClave("3072"));
	}
	
	@Test
	@Order(7)
	@DisplayName("Validar en DSA tamaños de Hash SHA y de la Clave de Bits")
	public void test7() {	
		assertAll( "", 
			() -> assertFalse(CertificadosUtils.validarEnDSA_HashSHA_TamClave("SHA-256","4096")),
			() -> assertFalse(CertificadosUtils.validarEnDSA_HashSHA_TamClave("SHA-512","4096")),
			() -> assertFalse(CertificadosUtils.validarEnDSA_HashSHA_TamClave("SHA-512","2048")),
			() -> assertTrue(CertificadosUtils.validarEnDSA_HashSHA_TamClave("SHA-1","1024")),
			() -> assertTrue(CertificadosUtils.validarEnDSA_HashSHA_TamClave("SHA-224","2048")),
			() -> assertTrue(CertificadosUtils.validarEnDSA_HashSHA_TamClave("SHA-224","2048")),
			() -> assertTrue(CertificadosUtils.validarEnDSA_HashSHA_TamClave("SHA-256","3072"))
		);  
	}
	
	@Test
	@Order(8)
	@DisplayName("Validar String del Algoritmo de firma en GOST")
	public void test8() {	
		assertAll( "", 
			
			//() -> not(assertEquals("Tc26-Gost-3410-12-256-paramSetB", CertificadosUtils.firmaEnGOST("ECGOST3410","Tc26-Gost-3410-12-256-paramSetB"))),
			() -> assertEquals("GOST3411withECGOST3410",CertificadosUtils.firmaEnGOST("ECGOST3410","GostR3410-2001-CryptoPro-A")),
			() -> assertEquals("GOST3411-2012-256withECGOST3410-2012-256", CertificadosUtils.firmaEnGOST("ECGOST3410-2012","Tc26-Gost-3410-12-256-paramSetA")),
			() -> assertEquals("GOST3411-2012-256WITHECGOST3410-2012-512", CertificadosUtils.firmaEnGOST("ECGOST3410-2012","Tc26-Gost-3410-12-512-paramSetB"))
		);
	}
	
	@Test
	@Order(9)
	@DisplayName("Validar String del Algoritmo de firma en GOST")
	public void test9() {	
		assertAll( "", 				
				//() -> not(assertEquals("Tc26-Gost-3410-12-256-paramSetB", CertificadosUtils.firmaEnGOST("ECGOST3410","Tc26-Gost-3410-12-256-paramSetB"))),
			() -> assertEquals("SHA512",CertificadosUtils.firmaEnEC("secp521r1")),
			() -> assertEquals("SHA224", CertificadosUtils.firmaEnEC("sect233r1")),
			() -> assertEquals("SHA384", CertificadosUtils.firmaEnEC("brainpoolP384r1")),
			() -> assertEquals("SHA1", CertificadosUtils.firmaEnEC("sect113r2")),
			() -> assertEquals("SHA256", CertificadosUtils.firmaEnEC("secp256r1")),
			() -> assertEquals("SHA224", CertificadosUtils.firmaEnEC("brainpoolP224t1"))			
		);
	}
	
	
}
