package com.xabe.restGenerator.tools;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.StringWriter;
import java.util.List;
import java.util.Map.Entry;
import java.util.Vector;

import org.apache.maven.plugin.MojoExecutionException;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.Velocity;
import org.apache.velocity.app.VelocityEngine;
import org.apache.velocity.context.Context;

/**
 * Clase que generar ficheros a traves de plantillas velocity
 * 
 * @author Chabir Atrahouch
 * 
 */
public class TemplateUtils {
	private static String LOG_VELOCITY = "Create template utils";
	private NamingConverter namingConverter;
	private String paquete;
	private String paquete_dir;
	private String subpaquete;
	private String clase;
	private String instancia;
	private String clase_plural;
	private String instancia_plural;
	private String web_root_dir;
	private String project_gui_dir;
	private String tabla;
	private String table_key;
	private String table_key_normalizado;
	private String table_key_lower;
	private String service;
	private String persistence;
	private String model;
	private String util;
	private String typeDataBase;
	private String columnaOrder;
	private boolean view;
	private boolean primaryKeyComposite;
	private boolean hasPrimaryKey;
	private List<String> primarysKeys;
	private ProjectProperties projectProperties;

	public TemplateUtils(Table table ,ProjectProperties projectProperties) {
		this.projectProperties = projectProperties;
		namingConverter = table.getNamingConverter();
		this.persistence = projectProperties.getFullDaoPkg();
		this.util = projectProperties.getFullUtilPkg();
		this.service = projectProperties.getFullServicePkg();
		this.model = projectProperties.getFullModelPkg();
		this.paquete = projectProperties.getProjectRootPkg();
		this.paquete_dir = projectProperties.getProjectRootPkgDir();
		this.subpaquete = namingConverter.getPackageName();
		this.clase = namingConverter.getClassName();
		this.instancia = namingConverter.getClassInstance();
		this.clase_plural = namingConverter.getClassNames();
		this.instancia_plural = namingConverter.getClassInstances();
		this.web_root_dir = projectProperties.getProjectWebRootDir();
		this.project_gui_dir = projectProperties.getFullModelDir();
		this.tabla = namingConverter.getTableName();
		this.table_key = namingConverter.getTableKey();
		this.table_key_normalizado = table_key.replaceAll("_", "");
		this.table_key_lower = namingConverter.getTableKey().toLowerCase();
		this.primarysKeys = table.getPrimaryKey();
		this.view = table.isView();
		this.typeDataBase = projectProperties.getDataBaseType();
		this.hasPrimaryKey = table.isHasPrimaryKey();
		this.primaryKeyComposite = table.isPrimaryComposite();
		for(Entry<String, Column> entry : table.getColumns().entrySet() ){
			this.columnaOrder = entry.getValue().getName();
			break;
		}
	}
	
	
	public static void generateTemplate(String template,Context velocityContext,StringWriter stringWriter) throws Exception {
		VelocityEngine velocityEngine = new VelocityEngine();
		velocityEngine.init();
		Reader reader = new InputStreamReader(TemplateUtils.class.getResourceAsStream(template) );
		Velocity.evaluate(velocityContext, stringWriter, LOG_VELOCITY, reader);
	}
	
	public static void writeTemplate(StringWriter stringWriter,File file) throws Exception{
		FileWriter w = null;
		try {
			w = new FileWriter(file);
			w.write(stringWriter.toString());
		} catch (IOException e) {
			throw new MojoExecutionException("Error creating file " + file, e);
		} finally {
			if (w != null) {
				try {
					w.close();
				} catch (IOException e) {
					// ignore
				}
			}
		}
	}
	
	public void generateClassFromTemplateFile(String templateDir,
			String templateName, String className, String outputDir) {
		try {
			String modelClass = projectProperties.getFullModelPkg() + "."+ namingConverter.getPackageName() + "." + this.clase;
			String templateFileName = templateDir + templateName + ".vm";
			VelocityContext context = new VelocityContext();
			context = setVelocityContext(context);
			Vector<BeanProp> v = BeanIntrospector.getBeanCriteria(Class.forName(modelClass));
			context.put("keyvalues", v);
			StringWriter writer = new StringWriter();
			generateTemplate(templateFileName, context, writer);

			FileUtils fu = new FileUtils();
			fu.crearDirectorio(outputDir);
			fu.writeFile(outputDir, className + ".java", writer.toString());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	
	
	public void generateClassFromTemplateFile(String templateFileName,
			String className, String outputDir) {
		try {			
			String modelClass = projectProperties.getFullModelPkg() + "."
					+ namingConverter.getPackageName() + "." + this.clase;
			/* create a context and add data */
			VelocityContext context = new VelocityContext();
			context = setVelocityContext(context);
			Vector<BeanProp> v = BeanIntrospector.getBeanCriteria(Class.forName(modelClass));
			context.put("keyvalues", v);
			/* now render the template into a StringWriter */
			StringWriter writer = new StringWriter();
			generateTemplate(templateFileName, context, writer);
			FileUtils fu = new FileUtils();
			fu.crearDirectorio(outputDir);
			fu.writeFile(outputDir, getComponentName(className), writer
					.toString());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Metodo que poner extension al fichero generado.
	 * @param component extension del fichero
	 * @return Extension del fichero
	 */
	private String getComponentName(String component) {
		String componentName = component.substring(0, component.indexOf(".")==-1?component.length():component.indexOf("."));
		if (componentName.contains("jsp")) {
			component = componentName.replace("jsp", "");
			component = component + ".jsp";
		} else if (componentName.contains("xhtml")) {
			component = componentName.replace("xhtml", "");
			component = component + ".xhtml";
		} else if (componentName.contains("xml")) {
			component = componentName.replace("xml", "");
			component = component + ".xml";
		} else if (componentName.contains("vm")) {
			component = componentName.replace("vm", "");
			component = component + ".vm";
		} else
			component = componentName + ".java";
		return component;
	}

	/**
	 * Metodo que añade atributos comunes en todas la plantillas
	 * @param context Contexto de velocity
	 * @return context de velocity
	 */
	public VelocityContext setVelocityContext(VelocityContext context) {
		context.put("paquete", paquete);
		context.put("paquete_dir", paquete_dir);
		context.put("subpaquete", subpaquete);
		context.put("clase", clase);
		context.put("claseUp", clase.toUpperCase());
		context.put("instancia", instancia);
		context.put("clase-plural", clase_plural);
		context.put("instancia-plural", instancia_plural);
		context.put("web-root-dir", web_root_dir);
		context.put("project-gui-dir", project_gui_dir);
		context.put("tabla", tabla);
		context.put("table-key", table_key);
		context.put("table-key-lower", table_key_lower);
		context.put("model", model);
		context.put("view", view);
		context.put("primaryKeyComposite", primaryKeyComposite);
		context.put("persistence", persistence);
		context.put("service", service);
		context.put("util", util);
		context.put("table_key_normalizado",table_key_normalizado);
		context.put("type_data_base", typeDataBase);
		context.put("root", projectProperties.getProjectRootPkg());
		context.put("hasPrimaryKey", hasPrimaryKey);
		context.put("columnaOrder", columnaOrder);
		context.put("primarysKeys", primarysKeys);
		return context;
	}
}
