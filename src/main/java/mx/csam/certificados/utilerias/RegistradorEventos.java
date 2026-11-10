package mx.csam.certificados.utilerias;

import java.util.ArrayList;

public class RegistradorEventos {

	//PROPIEDADES
	private static StringBuilder msj = null;	
	private static StringBuilder err = null;
	private static ArrayList<CharSequence> logs = null;
	
	//CONSTRUCTOR
	public RegistradorEventos() {
		/*msj = new StringBuilder();
		err = new StringBuilder();
		logs = new ArrayList<CharSequence>();*/
	}
	
	static {
		msj = new StringBuilder();
		err = new StringBuilder();
		logs = new ArrayList<CharSequence>();
	}
	
	
	//GETTERS AND SETTERS
	public StringBuilder getMsj() {
		return msj;
	}


	/*
	public void setMsj(StringBuilder msj) {
		this.msj = msj;
	}*/



	public StringBuilder getErr() {
		return err;
	}


	/*
	public void setErr(StringBuilder err) {
		this.err = err;
	}*/	
	

	public static ArrayList<CharSequence> getLogs() {
		return logs;
	}


	public static void setLogs(ArrayList<CharSequence> newLogs) {
		logs = newLogs;
	}	
	
	//METODOS
	public static <T extends Throwable> StringBuilder registarExcepcion(T t) {
		String tClass = t.getClass().getName();
		String tMsj = t.getMessage();
		String tLocMsj = t.getLocalizedMessage();
		
		//Luego trabajar en recuperar el printStackTrace() e imprimirlo en pantalla
		StackTraceElement[] tCausa = t.getStackTrace();
		
		//Uso StringBuilder para no ahogar el pool String
		StringBuilder msj = new StringBuilder(
			"""
			====================================================
			E R R O R
			====================================================
			Imprimiendo contenido\n\n
			""" +	
			//Cabeceras
			"Clase: " + tClass + "\n" +
			"Causa Principal: " + tMsj + "\n" +
			"Mensaje Localizado: " + tLocMsj + "\n" +
			
			//Causas
			"Causas: " + null + "\n" +
			"\n" 
		);
		
		System.out.println(msj.toString()); 
		agregarAlLog(msj);
		
		return msj;
	}
	
	public static <T extends Throwable> StringBuilder registarExcepcionAlGUI(String origen, String causa) {		
		
		//Uso StringBuilder para no ahogar el pool String
		StringBuilder msj = new StringBuilder(
			"""
			====================================================
			E R R O R
			====================================================
			Imprimiendo contenido\n\n
			""" +	
			//Cabeceras
			"Origen: " + origen + "\n" +
			"Motivo: " + causa + "\n\n\n" 
		);
		
		System.out.println(msj.toString() + "\n\n\n"); 
		agregarAlLog(msj);
		
		return msj;
	}
	
	
	public static <T extends CharSequence> StringBuilder registarMensajeAlGUI(T t) {		
		//Uso StringBuilder para no ahogar el pool String
		StringBuilder msj = new StringBuilder(t);
		
		System.out.println(msj.toString() + "\n\n\n"); 
		agregarAlLog(msj);
		
		return msj;
	}
	
	public static <T extends CharSequence> void agregarAlLog(T t){
	//public <T extends Charset> ArrayList<T> agregarAlLog(){
		logs.add(t);
	}
	
	public static CharSequence getUltimoMsj() {
		return logs.getLast();
	}
	
	public static String getUltimoMsjToString() {
		return logs.getLast().toString();
	}
	
	public static String getUltimosMsjToString(int num) {
		String lista = "";
		
		int limite = logs.size() - num;
		
		if(num <= logs.size()) {
			//for (int i = logs.size()-1; i > limite; i--) {
			for (int i = limite; i < logs.size(); i++) {
				lista = lista.concat(logs.get(i).toString()).concat("\n\n");
			}
		}
		
		return lista;
	}
	
	
}
