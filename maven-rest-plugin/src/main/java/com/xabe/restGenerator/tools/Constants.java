package com.xabe.restGenerator.tools;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Types;
import java.util.List;

import org.apache.maven.plugin.logging.Log;
import org.apache.maven.plugin.logging.SystemStreamLog;


/**
 * Clase que tiene las constantes del proyecto
 * @author Chabir Atrahouch
 *
 */
public class Constants {    
    public static final String JAX_RS_ACTION = "gui";
    public static final String ALL_ACTION = "all";
    
    public static final String ID = "id";
    
    public static final String IBATORCONFIG = "ibatorConfig.xml";

    public static final String SQLMAP = "_SqlMap.xml";
    public static final String FILTERS = "Filter.java";
    public static final String APPLICATION_CONTEXT = "applicationContext.xml";
    public static final String ENTITY_BASE = "EntityBase.java";
    public static final String EXAMPLE_BASE = "ExampleBase.java";
    public static final String PERSISTENCE_BASE = "PersistenceBase.java";
    public static final String LOG4J = "log4j.properties";
    public static final String SERVICE_BASE = "ServiceBase.java";
    public static final String SERVICE = "Service.java";
    public static final String SERVICE_IMPL = "ServiceImpl.java";
    public static final String JAX_RS_IMPL = "Rest.java";
    public static final String SERVICE_TEST = "ServiceTest.java";
    public static final String EMTITY_EXAMPLE = "Example";
    public static final String WEB_XML="web.xml";
    public static final String CONTEXT="context.xml";
    public static final String JETTY_CONTEXT="jetty.xml";
    public static final String WEB_INF="WEB-INF";
    public static final String COMMON="commons";
    public static final String META_INF="META-INF";
    public static final String ERROR="error";
    public static final String JAX_RS_BASE="BaseRest.java";
    
    public static final String SEPARATOR = "/";
    
    public static final String ORACLE = "oracle";
    public static final String SQLSERVER = "sqlServer";
    public static final String MYSQL = "mysql";
    
    public final static String TYPE_ID_MYSQL = "SELECT LAST_INSERT_ID()";
	public final static String TYPE_ID_ORACLE = "SELECT SEQUENCE_{0}.NEXTVAL AS {1} FROM DUAL";
	public final static String TYPE_ID_SQLSERVER = "SELECT SCOPE_IDENTITY()";
	
	public final static String TRUE_TEXT = "true"; 
	public final static String FALSE_TEXT = "false"; 
	
	public final static String MODEL_ENTITY_OLD = "public class {0} implements Serializable";
	public final static String MODEL_ENTITY_NEW = "import {1}.EntityBase;\n" +
												  "import javax.xml.bind.annotation.XmlRootElement;\n" +
												  "\n"+
												  "@XmlRootElement\n"+
												  "public class {0} extends EntityBase implements Serializable";
	
	public final static String MODEL_ENTITY_NEW_COMPOSITE = "import {1}.EntityBase;\n" +
															"\n"+
															"public class {0} extends EntityBase implements Serializable";
	
	
	public final static String MODEL_ENTITY_NEW_COMPOSITE_KEY_OLD = "public class {0} extends {0}Key implements Serializable";
	public final static String MODEL_ENTITY_NEW_COMPOSITE_KEY = "import javax.xml.bind.annotation.XmlRootElement;\n" +
			  										"\n"+
			  										"@XmlRootElement\n"+
			  										"public class {0} extends {0}Key implements Serializable";
	
	public final static String EXAMPLE_ENTITY_OLD = "public class {0}";
	public final static String EXAMPLE_ENTITY_NEW = "import {1}.ExampleBase;\n" +
													"import {2}.CriteriaBase;\n" +
												    "public class {0} extends ExampleBase";
	
	public final static String MAPPER = "Mapper";
	public final static String KEY = "Key";
	public final static String EXAMPLE = "Example";
	
	public final static String DAO_INTERFACE_OLD = "public interface {0}";
	public final static String DAO_INTERFACE_NEW = "import {3}.PersistenceBase;\n" +
												   "public interface {0} extends PersistenceBase<{1}, {2}>";
	
	
	public final static String CLASSES = "classes";
	public final static String JAVA = ".java";
	public final static String CLASS = ".class";
	public final static String POINT = ".";
	public final static String END_CLASS = "}";
	public final static String CRITERIA = "public static class Criteria";
	
	public final static Log LOG = new SystemStreamLog();
	public final static String PROMP = "----------------------------> ";
	public final static String RETURN = "\n";
	
	
	private final static String CHECK_TABLE_MYSQL = "select * from %s limit 1";
	private final static String CHECK_TABLE_ORACLE = "select * from %s where rownum=1";
	private final static String CHECK_TABLE_SQLSERVER = "select TOP 1 * from %s";
	

	public final static String PATTERN_IMPORT_NUMBER = "^(\\\\-)?\\\\d+(\\\\.\\\\d+)?$";
	public final static String PATTERN_IMPORT_STRING = ".+";
	public final static String PATTERN_IMPORT_DATE = "^^(0[1-9]|[12][0-9]|3[01])[- /.](0[1-9]|1[012])[- /.](19|20)\\\\d\\\\d$";
	
	
	public static String getTypeCheckTableDataBase(String version){
		if(version.toLowerCase().contains("mysql"))
		{
			return CHECK_TABLE_MYSQL;
		}
		else if(version.toLowerCase().contains("oracle"))
		{
			return CHECK_TABLE_ORACLE;
		}
		else
		{
			return CHECK_TABLE_SQLSERVER;
		}
	}
	
	public static String getTypeDataBase(String version){
		if(version.toLowerCase().contains("mysql"))
		{
			return MYSQL;
		}
		else if(version.toLowerCase().contains("oracle"))
		{
			return ORACLE;
		}
		else
		{
			return SQLSERVER;
		}
	}
	
	public static String getStringKeys(List<String> keys){
		String result = "";
		for(int i = 0; i < keys.size(); i++){
			if( i == keys.size() -1)
			{
				result += "\""+keys.get(i)+"\"";
			}
			else
			{
				result += "\""+keys.get(i)+"\""+",";
			}
		}
		return result;
	}
	
	public static boolean isPrimaryKey(Object[] keys,String nameColumn){
		for(Object key : keys){
			if(nameColumn.equalsIgnoreCase(key.toString()))
			{
				return true;
			}
		}
		return false;
	}
	
	
	public static boolean isString(int typeColumn){
		boolean result = false;
		switch (typeColumn) {
			case Types.LONGVARCHAR: result = true;
									break;
			case Types.NCHAR: result = true;
									break;
			case Types.NVARCHAR: result = true;
								break;
			case Types.VARCHAR: result = true;
								break;
		}
		return result;
	}
	
	public static boolean isDate(int typeColumn){
		boolean result = false;
		switch (typeColumn) {
			case Types.DATE: result = true;
									break;
			case Types.TIME: result = true;
									break;
			case Types.TIMESTAMP: result = true;
								break;
		}
		return result;
	}

	public static Connection getConnection(String url,String user,String pass,String driver) throws SQLException,ClassNotFoundException{
		Class.forName(driver);
		return  DriverManager.getConnection(url, user, pass);
	}
	
	public static boolean exitsAllTables(String table, ProjectProperties properties,Log log){
		JDBCTransactionDelegate delegate = null;
		final String query = Constants.getTypeCheckTableDataBase(properties.getDataBaseType());
		boolean exitsAll = true;
		try 
		{
			delegate = new JDBCTransactionDelegate(properties.getDataBaseUrl(),
					properties.getDataBaseUsername(),
					properties.getDataBasePassword(),
					properties.getDataBaseDriver());
			delegate.start();
			Statement statement = delegate.getStatement();
			try
			{
				log.info("Comprobamos si existe la tabla o vista : \""+table+"\"");
				statement.execute(String.format(query, table));
			}catch (SQLException e) {
				exitsAll = false;
				log.error("No existe la tabla o vista : \""+table+"\"");
			}
			delegate.commit();
			delegate.endCommint();
		}catch (Exception e) {
			log.error(e.getMessage());
			try 
			{
				delegate.rollback();
			} catch (Exception exception) {
					log.error(exception.getMessage());
			}
			log.error("Se termina la ejecucon de la aplicacion ");
			e.printStackTrace(System.out);
			System.exit(0);
		}
		finally{
			try
			{
				delegate.end();
			}catch (Exception e) {
				log.error("Error al cerrar las conexion a la BBDD "+e.getMessage());
			}
		}
		return exitsAll;
	}
}
