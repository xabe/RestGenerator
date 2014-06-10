package com.xabe.restGenerator.tools;

import java.text.MessageFormat;
import java.util.List;
import java.util.Map;

/**
 * Clase que representa una tabla de una base de datos
 * @author Chabir Atrahouch
 *
 */
public class Table {
	private NamingConverter namingConverter;
	private String table;
	private String typeId;
	private String identity;
	private boolean view;
	private boolean primaryComposite;
	private boolean hasPrimaryKey;
	private List<String> primaryKey;
	private Map<String, Column> columns;
	
	public Table(String table, Map<String, Column> columns, String dataBase,boolean view,boolean primaryComposite,List<String> primaryKey) {
		this.table = table;
		this.namingConverter = new NamingConverter(table, primaryComposite, primaryKey);
		if (dataBase.equalsIgnoreCase(Constants.MYSQL)) 
		{
			this.typeId = Constants.TYPE_ID_MYSQL;
			this.identity = Constants.TRUE_TEXT;
		}
		else if (dataBase.equalsIgnoreCase(Constants.ORACLE)) 
		{
			this.typeId = MessageFormat.format(Constants.TYPE_ID_ORACLE,table, this.namingConverter.getTableKey());
			this.identity = Constants.FALSE_TEXT;
		}
		else
		{
			this.typeId = Constants.TYPE_ID_SQLSERVER;
			this.identity = Constants.TRUE_TEXT;
		}
		this.view = view;
		this.hasPrimaryKey = true;
		if(getKey().equalsIgnoreCase(""))
		{
			this.hasPrimaryKey = false;
		}
		this.primaryComposite = primaryComposite;
		this.primaryKey = primaryKey;
		this.columns = columns;
	}
	
	public Map<String, Column> getColumns() {
		return columns;
	}

	public NamingConverter getNamingConverter() {
		return namingConverter;
	}

	public void setNamingConverter(NamingConverter namingConverter) {
		this.namingConverter = namingConverter;
	}

	public String getTable() {
		return table;
	}

	public void setTable(String table) {
		this.table = table;
	}

	public String getTypeId() {
		return typeId;
	}

	public void setTypeId(String typeId) {
		this.typeId = typeId;
	}

	public String getIdentity() {
		return identity;
	}

	public void setIdentity(String identity) {
		this.identity = identity;
	}

	public boolean isView() {
		return view;
	}

	public void setView(boolean view) {
		this.view = view;
	}

	public boolean isPrimaryComposite() {
		return primaryComposite;
	}

	public void setPrimaryComposite(boolean primaryComposite) {
		this.primaryComposite = primaryComposite;
	}

	public List<String> getPrimaryKey() {
		return primaryKey;
	}
	
	public String getTableDomain() {
		return namingConverter.getPackageName()+Constants.POINT+namingConverter.getClassName();
	}
	
	public String getKey() {
		return namingConverter.getTableKey();
	}
	
	public String getKeyIbator() {
		return namingConverter.getKeyIbator();
	}
	
	public boolean isHasPrimaryKey() {
		return hasPrimaryKey;
	}
}
