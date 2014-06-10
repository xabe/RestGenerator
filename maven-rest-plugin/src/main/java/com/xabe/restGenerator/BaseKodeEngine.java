package com.xabe.restGenerator;

import java.io.File;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugins.annotations.Parameter;

import com.xabe.restGenerator.tools.Column;
import com.xabe.restGenerator.tools.Constants;
import com.xabe.restGenerator.tools.ProjectProperties;
import com.xabe.restGenerator.tools.Table;
import com.xabe.restGenerator.tools.WordsConverter;

public abstract class BaseKodeEngine extends AbstractMojo {
	
	protected Properties configMap;
	protected ProjectProperties projectProperties;

	@Parameter(required = true,property = "config.project.ProjectName")
	protected String configProjectProjectName;

	@Parameter(required = true,property = "config.project.basePkg")
	protected String configProjectBasePkg;

	@Parameter(required = true,property = "project.build.outputDirectory")
	protected File outPutProject;

	@Parameter(required = true,property = "project.basedir")
	protected File baseProject;

	@Parameter(required = true,property = "config.database.driver")
	protected String configDatabaseDriver;

	@Parameter(required = true,property = "config.database.username")
	protected String configDatabaseUsername;

	@Parameter(required = true,property = "config.database.password")
	protected String configDatabasePassword;

	@Parameter(required = true,property = "config.database.url")
	protected String configDatabaseUrl;
	
	@Parameter(required = true,property = "config.database.schema")
	protected String configDatabaseSchema;
	
	@Parameter(required = true,property = "config.database.catalog")
	protected String configDatabaseCatalog;

	@Parameter(required = true,property = "config.database.tables")
	protected String configDatabaseTables;
	
	
	public void init() throws SQLException, ClassNotFoundException{
		configMap = new Properties();
		configMap.put("config.project.outPutDir",outPutProject.getAbsolutePath());
		configMap.put("config.project.basePkg", configProjectBasePkg);
		configMap.put("config.project.ProjectName",configProjectProjectName);
		configMap.put("config.project.rootDir",baseProject.getAbsolutePath());
		configMap.put("config.database.driver", configDatabaseDriver);
		configMap.put("config.database.username",configDatabaseUsername);
		configMap.put("config.database.password",configDatabasePassword);
		configMap.put("config.database.url", configDatabaseUrl);
		configMap.put("config.database.schema", configDatabaseSchema);
		configMap.put("config.database.catalog", configDatabaseCatalog);
		configMap.put("config.database.tables", configDatabaseTables);
		configMap.put("config.sourceDir", "src/main/java");
		configMap.put("config.resourceDir", "src/main/resources");
		configMap.put("config.sourceTest", "src/test/java");
		configMap.put("config.resourceTest", "src/test/resources");
		configMap.put("config.mybatis.pkg.model", "model");
		configMap.put("config.mybatis.pkg.dao", "persistence");
		configMap.put("config.mybatis.pkg.sql", "persistence.sql");
		configMap.put("config.mybatis.pkg.util", "util");
		configMap.put("config.mybatis.pkg.service", "service");
		configMap.put("config.mybatis.pkg.ws", "ws");
		configMap.put("config.project.webRootName","src/main/webapp");
		projectProperties = new ProjectProperties(configMap);
		
		//Obtenemos informacion de la tablas
		Connection conexion = Constants.getConnection(projectProperties.getDataBaseUrl(), projectProperties.getDataBaseUsername(), projectProperties.getDataBasePassword(), projectProperties.getDataBaseDriver());
		
		DatabaseMetaData metaDatos = conexion.getMetaData();
		
		configMap.put("config.database.type", Constants.getTypeDataBase(metaDatos.getDatabaseProductName()));
		
		conexion.close();
	}
	
	public List<Table> getTables(String listTables, ProjectProperties properties) throws SQLException, ClassNotFoundException, MojoExecutionException {
		Connection conexion = Constants.getConnection(projectProperties.getDataBaseUrl(), projectProperties.getDataBaseUsername(), projectProperties.getDataBasePassword(), projectProperties.getDataBaseDriver());
		DatabaseMetaData metaDatos = conexion.getMetaData();
		
		List<Table> result = new ArrayList<Table>();
		String[] entities = listTables.split(";");				
		
		for(String entity : entities){
			if(!Constants.exitsAllTables(entity,projectProperties,getLog()))
			{
				throw new MojoExecutionException("No exite todas las tablas definidas en el pom.xml");
			}
			result.add(getTable(metaDatos, entity, properties));
		}	
		
		conexion.close();		
		return result;
	}
	
	private Table getTable(DatabaseMetaData metaDatos, String entity, ProjectProperties projectProperties) throws SQLException, ClassNotFoundException{

		ResultSet entitys;
		ResultSet column;
		ResultSet primaryKey;
		String catalogo;
		String schema;
		String table;
		String typeTable;
		int typeColumn;
		int tamayoColumna;
		String isNull;
		List<String> primaryKeys;
		int nunPrimaryKey;
		String dataBase = projectProperties.getDataBaseType();
		Map<String, Column> columns;
		Column colum;
		String nameColumn;
		
		//Datos de la tabla
		entitys = metaDatos.getTables(projectProperties.getDataBaseCatalog(), projectProperties.getDataBaseSchema(), entity, null);
		entitys.next();
		catalogo = entitys.getString(1);
		schema = entitys.getString(2);
		table = entitys.getString(3);
		typeTable = entitys.getString(4);
		primaryKey = metaDatos.getPrimaryKeys(catalogo, schema, table);
		nunPrimaryKey = 0;
		primaryKeys = new ArrayList<String>();
		while (primaryKey.next()) {
			primaryKeys.add(WordsConverter.getInstance().camelCaseFirstWordInLower(primaryKey.getString(4)));
			nunPrimaryKey++;
		}
		primaryKey.close();
		
		//Miramos la columnas
		columns = new HashMap<String, Column>();
		column = metaDatos.getColumns(catalogo, schema, table, null);
		while (column.next()) {
			nameColumn = column.getString(4);
			typeColumn = column.getInt(5);
		    tamayoColumna = column.getInt(7);
		    isNull = column.getString(18);
		    colum = new Column(nameColumn, typeColumn, tamayoColumna, isNull.equalsIgnoreCase("true"));
		    String columName = WordsConverter.getInstance().camelCaseFirstWordInLower(nameColumn);
		    columns.put(columName, colum);
		}
		column.close();
		
		entitys.close();
		
		//Creamos la tabla
		return new Table(entity, columns, dataBase, typeTable.equalsIgnoreCase("VIEW"), nunPrimaryKey > 1, primaryKeys);
	}

}
