package mx.csam.certificados.utilerias;

import java.util.ArrayList;

public class RegistradorEventos {

	//PROPIEDADES
	private StringBuilder msj = null;	
	private StringBuilder err = null;
	private ArrayList<CharSequence> logs = null;
	
	//CONSTRUCTOR
	public RegistradorEventos() {
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
	

	public ArrayList<CharSequence> getLogs() {
		return logs;
	}


	public void setLogs(ArrayList<CharSequence> logs) {
		this.logs = logs;
	}


	//METODOS
	public <T extends Throwable> StringBuilder registarExcepcion(T t) {
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
		
		return msj;
	}
	
	public <T extends CharSequence> StringBuilder registarMensajesAlGUI(T t) {
		String tClass = null;
		String tMsj = null;
		String tLocMsj = null;
		
		//Luego trabajar en recuperar el printStackTrace() e imprimirlo en pantalla
		StackTraceElement[] tCausa = null;
		
		//Uso StringBuilder para no ahogar el pool String
		StringBuilder msj = new StringBuilder(t);
		
		System.out.println(msj.toString()); 
		agregarAlLog(msj);
		
		return msj;
	}
	
	public <T extends CharSequence> void agregarAlLog(T t){
	//public <T extends Charset> ArrayList<T> agregarAlLog(){
		this.logs.add(t);
	}
}
