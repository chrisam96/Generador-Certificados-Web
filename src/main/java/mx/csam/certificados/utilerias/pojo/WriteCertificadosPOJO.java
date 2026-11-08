package mx.csam.certificados.utilerias.pojo;

import java.util.Arrays;
import java.util.Map;
import java.util.stream.Collectors;

public class WriteCertificadosPOJO {
	//Algoritmo Asimetrico para generar el Certificado 
	private String algoritmo = null; 
	//Datos del titular del Certificado 
	private String[] titularDN = null; 
	//Datos del Emisor (quién firma el certificado) ---> .Cer + .key	
	private String[] emisorDN = null; 
	
	//Fechas de inicio de vigencia		
	private String[] fecha_vig_y_serial = null;
	//Datos para la generación de las LLaves Publica y Privada
	private String[] datosKPG = null; 
	//Bandera para diferenciar entre Cer/CSR
		//true = .cer (Certificado), false = .csr (CSR)
	private boolean cerOrCsr = true; 
	
	//Datos para cifrar o no la PrivateKey		
	private Object[] datosPKCifrado = null; 
	//Ruta del archivo doonde se guardará el .cer/.key o .csr
	private String codif_archivo = null;
	//Datos del Subjective Alternative Names ---> .csr
	private Map<String, String[]> _SAN  = null;

	//CONSTRUCTORES
	public WriteCertificadosPOJO() {
		super();
	}

	public WriteCertificadosPOJO(String algoritmo, String[] titularDN, String[] emisorDN, String[] fecha_vig_y_serial,
			String[] datosKPG, boolean cerOrCsr, Object[] datosPKCifrado, String codif_archivo,
			Map<String, String[]> _SAN) {
		this.algoritmo = algoritmo;
		this.titularDN = titularDN;
		this.emisorDN = emisorDN;
		this.fecha_vig_y_serial = fecha_vig_y_serial;
		this.datosKPG = datosKPG;
		this.cerOrCsr = cerOrCsr;
		this.datosPKCifrado = datosPKCifrado;
		this.codif_archivo = codif_archivo;
		this._SAN = _SAN;
	}

	//METODOS
	private String toString_SANsMap() {
		//Es necesario que no este en uso
		//-> ni siquiera el inicializar con "" ni null
		final String map;
		
		if (_SAN == null) {
			return "null";
		} else {
			/*
			// Imprime los elementos sin corchetes (separados por comas)
			_SAN.forEach((clave, arreglo) -> {
			    	System.out.println(clave + ": " + String.join(", ", arreglo));
			    	map.concat(clave + ": " + Arrays.toString(arreglo) + "\n");
			    	//map = map.concat("\n");			    
				}
			);*/
			

			//String map2 = this._SAN.entrySet().stream()
			map = this._SAN.entrySet().stream()
				.map(entry -> entry.getKey() + " -> " + Arrays.toString( entry.getValue() ) )
				.collect(Collectors.joining("\n")
			); // Une cada línea con un salto de línea

			//return map2;
			
			//Permite devolver un String manipulable
			String mapClone = new StringBuilder(map).toString();
			
			return mapClone;
		}
	}
	
	//GETTERS AND SETTERS
	public String getAlgoritmo() {
		return algoritmo;
	}

	public void setAlgoritmo(String algoritmo) {
		this.algoritmo = algoritmo;
	}

	public String[] getTitularDN() {
		return titularDN;
	}

	public void setTitularDN(String[] titularDN) {
		this.titularDN = titularDN;
	}

	public String[] getEmisorDN() {
		return emisorDN;
	}

	public void setEmisorDN(String[] emisorDN) {
		this.emisorDN = emisorDN;
	}

	public String[] getFecha_vig_y_serial() {
		return fecha_vig_y_serial;
	}

	public void setFecha_vig_y_serial(String[] fecha_vig_y_serial) {
		this.fecha_vig_y_serial = fecha_vig_y_serial;
	}

	public String[] getDatosKPG() {
		return datosKPG;
	}

	public void setDatosKPG(String[] datosKPG) {
		this.datosKPG = datosKPG;
	}

	public boolean getCerOrCsr() {
		return cerOrCsr;
	}

	public void setCerOrCsr(boolean cerOrCsr) {
		this.cerOrCsr = cerOrCsr;
	}

	public Object[] getDatosPKCifrado() {
		return datosPKCifrado;
	}

	public void setDatosPKCifrado(Object[] datosPKCifrado) {
		this.datosPKCifrado = datosPKCifrado;
	}

	public String getCodif_archivo() {
		return codif_archivo;
	}

	public void setCodif_archivo(String codif_archivo) {
		this.codif_archivo = codif_archivo;
	}

	public Map<String, String[]> get_SAN() {
		return _SAN;
	}

	public void set_SAN(Map<String, String[]> _SAN) {
		this._SAN = _SAN;
	}

	
	@Override
	public String toString() {
		return 
			"[0] Algoritmo: " + this.getAlgoritmo() + "\n" +
			"[1] TitularDN: " + Arrays.toString(this.getTitularDN()) + "\n" +
			"[2] EmisorDN: " + Arrays.toString(this.getEmisorDN()) + "\n" +
			"[3] Fecha vig. y serial: " + Arrays.toString(this.getFecha_vig_y_serial()) + "\n" +
			"[4] Datos KPG: " + Arrays.toString(this.getDatosKPG()) + "\n" +
			"[5] Cer(true) o CSR(false): " + this.getCerOrCsr() + "\n" +
			"[6] Datos PK Cifrado: " + Arrays.toString(this.getDatosPKCifrado()) + "\n" +
			"[7] Codificacion archivo: " + this.getCodif_archivo() + "\n" +
			"[8] SAN: " + this.toString_SANsMap() 			
			;
		
	}

		
}
