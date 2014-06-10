package com.xabe.restGenerator.tools;

import java.io.File;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.List;

import org.apache.maven.plugin.logging.Log;
import org.apache.maven.plugin.logging.SystemStreamLog;
import org.apache.velocity.VelocityContext;
import org.mybatis.generator.api.MyBatisGenerator;
import org.mybatis.generator.config.Configuration;
import org.mybatis.generator.config.xml.ConfigurationParser;
import org.mybatis.generator.internal.DefaultShellCallback;

public class Generator {
	private ProjectProperties projectProperties;
	private File ibatorConfig;
	private Log log = new SystemStreamLog();
	private List<Table> tables;
	
	public Generator(ProjectProperties projectProperties,List<Table> tables) throws Exception {
		this.projectProperties = projectProperties;
		this.tables = tables;
		
		log.info("Generamos el IbatorConfig.xml.");
		generateIbatorConfig();
		log.info("Terminamos de generar el ibatorConfig.xml.");
		
		log.info("Llammos al generador de Mybatis.");
		generateMyBatis();
		log.info("Termina el generador de Mybatis.");
		
		log.info("Generamos el applicationContext");
		generateApplicationContext();
		log.info("Terminamos de generar applicationContext.");
		
		log.info("Generamos el log4j");
		generatelog4j();
		log.info("Terminamos de generar el log4j.");
		
		log.info("Generamos el context.xml");
		generateContext();
		log.info("Terminamos de generar el context.xml.");
		
		log.info("Generamos el jetty.xml");
		generateJettyContext();
		log.info("Terminamos de generar el jetty.xml.");
	}
	
	private void generateIbatorConfig() throws Exception {
		VelocityContext velocityContext = new VelocityContext();
		velocityContext.put("driver",projectProperties.getDataBaseDriver());
		velocityContext.put("url",projectProperties.getDataBaseUrl());
		velocityContext.put("user",projectProperties.getDataBaseUsername());
		velocityContext.put("password",projectProperties.getDataBasePassword());		
		velocityContext.put("source",projectProperties.getSource());
		velocityContext.put("resource",projectProperties.getResource());		
		velocityContext.put("dao",projectProperties.getFullDaoPkg());
		velocityContext.put("sql",projectProperties.getFullSqlPkg());
		velocityContext.put("model",projectProperties.getFullModelPkg());		
		velocityContext.put("tables", tables);
		StringWriter stringWriter = new StringWriter();
		TemplateUtils.generateTemplate("/com/xabe/restGenerator/template/ibator/ibatorConfig.vm", velocityContext, stringWriter);
		ibatorConfig = new File(projectProperties.getProjectRootDir()+projectProperties.getResource()+File.separator+Constants.IBATORCONFIG);
		TemplateUtils.writeTemplate(stringWriter, ibatorConfig);
	}
	
	private void generateMyBatis() throws Exception {
		List<String> warnings = new ArrayList<String>();
		boolean overwrite = true;
		ConfigurationParser cp = new ConfigurationParser(warnings);
		Configuration config = cp.parseConfiguration(ibatorConfig);
		DefaultShellCallback callback = new DefaultShellCallback(overwrite);
		MyBatisGenerator myBatisGenerator = new MyBatisGenerator(config,callback, warnings);
		myBatisGenerator.generate(null);
		for(String warning: warnings){
			log.warn(Constants.PROMP+" "+warning);
		}
	}
	
	private void generateApplicationContext() throws Exception {		
		if(!ProjectManager.existsApplicationContext(projectProperties))
		{
			VelocityContext velocityContext = new VelocityContext();
			velocityContext.put("persistenceSql",projectProperties.getFullSqlDir());
			velocityContext.put("persistence",projectProperties.getFullDaoPkg());
			velocityContext.put("root",projectProperties.getProjectRootPkg());
			velocityContext.put("project",projectProperties.getProjectName().replaceAll(" ", "_"));
			StringWriter stringWriter = new StringWriter();
			TemplateUtils.generateTemplate("/com/xabe/restGenerator/template/spring/applicationContext.vm", velocityContext, stringWriter);
			File file = new File(projectProperties.getProjectRootDir()+projectProperties.getResource()+File.separator+Constants.APPLICATION_CONTEXT);
			TemplateUtils.writeTemplate(stringWriter, file);
		}
		else
		{
			log.info(Constants.PROMP + Constants.APPLICATION_CONTEXT + " existe");
		}
	}
	
	private void generatelog4j() throws Exception {
		VelocityContext velocityContext = new VelocityContext();
		StringWriter stringWriter = new StringWriter();
		File file;
		
		if(!ProjectManager.existsLog4j(projectProperties))
		{
			velocityContext.put("proyecto", projectProperties.getProjectName());
			velocityContext.put("root", projectProperties.getProjectRootPkg());
			TemplateUtils.generateTemplate("/com/xabe/restGenerator/template/log4j/log4j.vm", velocityContext, stringWriter);
			file = new File(projectProperties.getProjectRootDir()+projectProperties.getResource()+File.separator+Constants.LOG4J);
			TemplateUtils.writeTemplate(stringWriter, file);
		}
		else
		{
			log.info(Constants.PROMP + Constants.LOG4J +" existe");  
		}
	}
	
	private void generateContext() throws Exception {
		if(ProjectManager.existsContext(projectProperties))
		{
			log.info(Constants.PROMP + Constants.CONTEXT +" existe");  
			return;
		}
		VelocityContext velocityContext = new VelocityContext();
		velocityContext.put("url", projectProperties.getDataBaseUrl());
		velocityContext.put("driver", projectProperties.getDataBaseDriver());
		velocityContext.put("user", projectProperties.getDataBaseUsername());
		velocityContext.put("password", projectProperties.getDataBasePassword());
		velocityContext.put("project",projectProperties.getProjectName().replaceAll(" ", "_"));
		StringWriter stringWriter = new StringWriter();
		TemplateUtils.generateTemplate("/com/xabe/restGenerator/template/context/context.vm", velocityContext, stringWriter);
		File file = new File(projectProperties.getProjectWebRootDir()+File.separator+Constants.META_INF+File.separator+Constants.CONTEXT);
		TemplateUtils.writeTemplate(stringWriter, file);
	}
	
	private void generateJettyContext() throws Exception {
		if(ProjectManager.existsJettyContext(projectProperties))
		{
			log.info(Constants.PROMP + Constants.JETTY_CONTEXT +" existe");  
			return;
		}
		VelocityContext velocityContext = new VelocityContext();
		velocityContext.put("url", projectProperties.getDataBaseUrl());
		velocityContext.put("driver", projectProperties.getDataBaseDriver());
		velocityContext.put("user", projectProperties.getDataBaseUsername());
		velocityContext.put("password", projectProperties.getDataBasePassword());
		velocityContext.put("project",projectProperties.getProjectName().replaceAll(" ", "_"));
		StringWriter stringWriter = new StringWriter();
		TemplateUtils.generateTemplate("/com/xabe/restGenerator/template/context/jetty.vm", velocityContext, stringWriter);
		File file = new File(projectProperties.getProjectWebRootDir()+File.separator+Constants.META_INF+File.separator+Constants.JETTY_CONTEXT);
		TemplateUtils.writeTemplate(stringWriter, file);
	}
}
