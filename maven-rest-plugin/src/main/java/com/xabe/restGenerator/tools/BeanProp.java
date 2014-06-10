package com.xabe.restGenerator.tools;

/**
 * Clase que recoge la información de un Tabla
 * @author Chabir Atrahouch
 *
 */
public class BeanProp {
	private String name;
	private String key;
	private String value;
	private String type;
	private String columnName="";
	private String columnType;
	private String keyMessage;
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getKey() {
		return key;
	}
	public void setKey(String key) {
		this.key = key;
	}
	public String getValue() {
		return value;
	}
	public void setValue(String value) {
		this.value = value;
	}
	public String getColumnName() {
		return columnName;
	}
	public void setColumnName(String columnName) {
		this.columnName = columnName;
	}
	public String getColumnType() {
		return columnType;
	}
	public void setColumnType(String columnType) {
		this.columnType = columnType;
	}
	public String getKeyMessage() {
		return keyMessage;
	}
	public void setKeyMessage(String keyMessage) {
		this.keyMessage = keyMessage;
	}
}
