package com.xabe.restGenerator.tools;

/**
 * Clase que tiene información de una columna de una tabla
 * @author Chabir Atrahouch
 *
 */
public class Column {
	
	private int tipoColumna;
	private int tamayoColumna;
	private boolean isNull;
	private String name;
	
	public Column(String name, int tipoColumna, int tamayoColumna,boolean isNull) {
		this.tipoColumna = tipoColumna;
		this.tamayoColumna = tamayoColumna;
		this.isNull = isNull;
		this.name = name;
	}
	
	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}

	public int getTipoColumna() {
		return tipoColumna;
	}

	public void setTipoColumna(int tipoColumna) {
		this.tipoColumna = tipoColumna;
	}

	public int getTamayoColumna() {
		return tamayoColumna;
	}

	public void setTamayoColumna(int tamayoColumna) {
		this.tamayoColumna = tamayoColumna;
	}

	public boolean isNull() {
		return isNull;
	}

	public void setNull(boolean isNull) {
		this.isNull = isNull;
	}

}
