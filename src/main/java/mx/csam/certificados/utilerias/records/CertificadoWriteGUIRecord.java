package mx.csam.certificados.utilerias.records;

import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.cert.X509Certificate;
import java.util.Map;

/*
 * 	private record Archivos(
		X509Certificate certificado,
		PublicKey publicKey,
		PrivateKey privateKey,
		Object etc
	) {
		
		/*Constructor CANONICO EXPLICITO 
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
		}	
	}
	
	//----------------------------------------------------------------------------
	public record AExpress(
	    PublicKey pubk,
	    HttpClient httpClient,
	    String estatus // Ahora el campo final es un String, ANTES era Integer
	) {

    // Constructor secundario que acepta el Integer y realiza la transformación
    public AExpress(PublicKey pubk, HttpClient httpClient, Integer i) {
        // Llama al constructor canónico transformando el Integer a String mediante un método estático
        this(pubk, httpClient, transformarEstatus(i));
    }

    // Método auxiliar estático encargado de la lógica de conversión
    private static String transformarEstatus(Integer i) {
        if (i == null) throw new IllegalArgumentException("El valor no puede ser nulo.");
        
        return switch (i) {
            case Integer in when in > 0 && in < 10 -> "OK";
            case Integer in when in >= 10 && in < 20 -> "OK+";
            case Integer in when in >= 20 && in < 30 -> "OK Pro";
            case Integer in when in >= 30 && in < 40 -> "OK Pro+";
            default -> throw new IllegalArgumentException("Valor fuera de rango o negativo: " + i);
        };
    }
}

	 
 * */
public record CertificadoWriteGUIRecord(
		String algoritmo, //Algoritmo Asimetrico para generar el Certificado 
		String[] titularDN, //Datos del titular del Certificado 
		String[] emisorDN, //Datos del Emisor (quién firma el certificado) ---> .Cer + .key	
		
		String[] fecha_vig_y_serial,//Fechas de inicio de vigencia		
		String[] datosKPG, //Datos para la generación de las LLaves Publica y Privada
		boolean CerOrCsr, //Bandera para diferenciar entre Cer/CSR
		
		Object[] datosPKCifrado, //Datos para cifrar o no la PrivateKey		
		String codif_archivo,//Ruta del archivo doonde se guardará el .cer/.key o .csr
		Map<String, String[]> _SAN //Datos del Subjective Alternative Names ---> .csr
		
	) {
	
	/*CONSTRUCTOR CANONICO*/
	public CertificadoWriteGUIRecord(
		String algoritmo,  
		String[] titularDN, 
		String[] emisorDN, 	
		
		String[] fecha_vig_y_serial,		
		String[] datosKPG, 
		boolean CerOrCsr, 
		
		Object[] datosPKCifrado, 		
		String codif_archivo,
		Map<String, String[]> _SAN 
	) {
		this.algoritmo = algoritmo;
		this.titularDN = titularDN;
		this.emisorDN = emisorDN;
		this.fecha_vig_y_serial = fecha_vig_y_serial;
		this.datosKPG = datosKPG;
		this.CerOrCsr = CerOrCsr;
		this.datosPKCifrado = datosPKCifrado;
		this.codif_archivo = codif_archivo;
		this._SAN = _SAN;
	}
	
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
	
	/*CONSTRUCTOR SOBRECARGADO*/
	public CertificadoWriteGUIRecord(
		String algoritmo,  
		String[] titularDN, 
		//String[] emisorDN, 	
		
		String[] fecha_vig_y_serial,		
		String[] datosKPG, 
		boolean CerOrCsr, 
		
		Object[] datosPKCifrado, 		
		String codif_archivo
		//Map<String, String[]> _SAN 
	) {
		this(algoritmo, titularDN, null, fecha_vig_y_serial, datosKPG, CerOrCsr, datosPKCifrado, codif_archivo, null);
	}
	
	/*CONSTRUCTOR SOBRECARGADO*/
	public CertificadoWriteGUIRecord(
		String algoritmo,  
		String[] titularDN, 
		String[] emisorDN, 	
		
		String[] fecha_vig_y_serial,		
		String[] datosKPG, 
		boolean CerOrCsr, 
		
		Object[] datosPKCifrado, 		
		String codif_archivo
		//Map<String, String[]> _SAN 
	) {
		this(algoritmo, titularDN, emisorDN, fecha_vig_y_serial, datosKPG, CerOrCsr, datosPKCifrado, codif_archivo, null);
	}
	
	/*CONSTRUCTOR SOBRECARGADO*/
	public CertificadoWriteGUIRecord(
		String algoritmo,  
		String[] titularDN, 
		//String[] emisorDN, 	
		
		String[] fecha_vig_y_serial,		
		String[] datosKPG, 
		boolean CerOrCsr, 
		
		Object[] datosPKCifrado, 		
		String codif_archivo,
		Map<String, String[]> _SAN 
	) {
		this(algoritmo, titularDN, null, fecha_vig_y_serial, datosKPG, CerOrCsr, datosPKCifrado, codif_archivo, _SAN);
	}
}
