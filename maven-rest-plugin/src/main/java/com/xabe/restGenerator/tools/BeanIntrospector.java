package com.xabe.restGenerator.tools;

import java.beans.BeanInfo;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.util.Vector;

import org.apache.maven.plugin.logging.Log;
import org.apache.maven.plugin.logging.SystemStreamLog;

/**
 * Clase que obtiene información de los atributos y métodos de una clase
 * @author Chabir Atrahouch
 *
 */
public class BeanIntrospector {
	private static Log logger = new SystemStreamLog();
	
	/**
	 * Metodo que permite obtener en un vector la lista de BeanProp de un determiado bean.
	 * @param c Clase hacer introspeccion
	 * @return Vector con todos los metodos que contiene la clase
	 * @throws IntrospectionException
	 */
	public static Vector <BeanProp> getBeanCriteria(Class<?> c) throws IntrospectionException{
		BeanInfo info = Introspector.getBeanInfo(c);
		Vector <BeanProp> v =new Vector<BeanProp>();
		try {
			PropertyDescriptor pdarray []=info.getPropertyDescriptors();
			BeanProp kv=new BeanProp();
			for ( int i=0;i<pdarray.length;i++){
				PropertyDescriptor pd =pdarray[i];
				if(!pd.getName().equals("class")){
					if(logger.isDebugEnabled())
						logger.debug("beanCriteria. Property: " + pd.getName() + " Type: "+ pd.getPropertyType());
					kv=new BeanProp();
					kv.setName(pd.getName().substring(0, 1).toUpperCase()+pd.getName().substring(1, pd.getName().length()));
					kv.setKey(pd.getName());
					kv.setKeyMessage(WordsConverter.getInstance().camelCaseFirstWordInLower(c.getSimpleName())+"."+WordsConverter.getInstance().camelCaseFirstWordInLower(pd.getName()));
					kv.setValue(Integer.toString(i+11));//Pone una constante por encima del 11
					kv.setType(pd.getPropertyType().getSimpleName());
					kv.setColumnName(WordsConverter.desCamelCase(pd.getName()));
					kv.setColumnType(getColumnType(pd.getPropertyType().getSimpleName()));
					v.add(kv);
				}
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return v;
	}
	
	/**
	 * Metodo que permite obtener en un vector la lista de descriptores de un determiado bean.
	 * @param c clase de tipo Bean de la que se quiere obtener los descriptores
	 * @return Vector de objetos PropertyDescriptor
	 * @throws IntrospectionException
	 */
	public static Vector<PropertyDescriptor> getProperties(Class<?> c) throws IntrospectionException{
		BeanInfo info = Introspector.getBeanInfo(c);
		Vector<PropertyDescriptor> v = new Vector<PropertyDescriptor>();
		for (PropertyDescriptor pd : info.getPropertyDescriptors()){
			
			if(!pd.getName().equals("class")){
				if(logger.isDebugEnabled())
					logger.debug("getProperties. Property: " + pd.getName() + " Type: "+ pd.getPropertyType());
				v.add(pd);
			}
		}
		return v;
	}

	/**
	 * Metodo que obtiene el tipo 
	 * @param type tipo a analizar
	 * @return Varchar si es String,Integer int o mismo tipo en mayuscula
	 */
	private static String getColumnType(String type){
		String columnType="";
		if (type.equalsIgnoreCase("String"))
			columnType="VARCHAR";
		else if (type.equalsIgnoreCase("int"))
			columnType="INTEGER";
		else
			columnType=type.toUpperCase();
		return columnType;
	}
	
}

