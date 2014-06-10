package com.xabe.restGenerator.tools;

import java.util.List;

import org.apache.maven.plugin.logging.Log;
import org.apache.maven.plugin.logging.SystemStreamLog;


public class NamingConverter {

	private String entity;// my_entity
	private String tableName;// t_my_entity
	private String entityTitle;// My Entity
	private String entitiesTitle;// My Entities
	private String className;// MyEntity
	private String classNames;// MyEntity
	private String classInstance;// myEnitity
	private String classInstances;// myEntities
	private String entityPackageName;// myentity
	private String tableKey = "";// table key
	private Log logger = new SystemStreamLog();
	private String keyIbator = "";


	public NamingConverter(String entity,boolean primaryKeyComposite,List<String> keys) {
		this.tableName = entity;
		if (entity.startsWith("t_") || entity.startsWith("T_"))
		{
			this.entity = entity.substring(2);
		}
		else
		{
			this.entity = entity;
		}
		this.entityTitle = WordsConverter.getInstance().tittletize(this.entity);
		this.entitiesTitle = WordsConverter.getInstance().pluralize(entityTitle);
		this.className = WordsConverter.getInstance().camelCase(this.entity);
		this.classNames = WordsConverter.getInstance().pluralize(WordsConverter.getInstance().camelCase(this.entity));
		this.classInstance = WordsConverter.getInstance().camelCaseFirstWordInLower(this.entity);
		this.classInstances = WordsConverter.getInstance().pluralize(classInstance);
		this.entityPackageName = WordsConverter.getInstance().packetize(this.entity);
		String tkey = null;
		if(primaryKeyComposite)
		{
			//Es compuesta
			this.tableKey = this.className + "Key";
			this.keyIbator = "";
		}
		else
		{
			if(keys.size() > 0)
			{
				tkey = keys.get(0);
			}
			if(tkey != null && tkey.length() >= 0)
			{
				this.tableKey = tkey.substring(0, 1).toUpperCase() + tkey.substring(1).toLowerCase();
				this.keyIbator = tkey;
			}
		}
		if(logger.isDebugEnabled())
		{
			debug();
		}
	}

	public void debug() {
		logger.info("entity: " + entity);
		logger.info("tableName: " + tableName);
		logger.info("entityTitle: " + entityTitle);
		logger.info("entitiesTitle: " + entitiesTitle);
		logger.info("className: " + className);
		logger.info("classNames: " + classNames);
		logger.info("classInstance: " + classInstance);
		logger.info("classInstances: " + classInstances);
		logger.info("entityPackageName: " + entityPackageName);
		logger.info("tableKey: " + tableKey);
	}

	public String getEntity() {

		return entity;
	}

	public String getTableName() {

		return tableName;
	}

	public String getEntityTitle() {

		return entityTitle;
	}

	public String getEntitiesTitle() {
		return entitiesTitle;
	}

	public String getClassName() {
		return className;
	}

	public String getClassNames() {
		return classNames;
	}

	public String getClassInstance() {
		return classInstance;
	}

	public String getClassInstances() {
		return classInstances;
	}

	public String getPackageName() {
		return entityPackageName;
	}

	public String getTableKey() {
		return tableKey;
	}
	
	public String getKeyIbator() {
		return keyIbator;
	}
}
