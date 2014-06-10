package com.xabe.restGenerator.tools;

import java.io.File;
import java.util.Properties;

/**
 * Clase que se encarga de obtener la información de proyecto
 * @author Chabir Atrahouch
 *
 */
public class ProjectProperties {
	private Properties properties;

	public ProjectProperties(Properties properties) {
		this.properties = properties;
	}
	
	
/*****************************************************************************************
 * DATABASE PROPERTIES READER
 *****************************************************************************************/
	public String getDataBaseDriver() {
		return properties.getProperty("config.database.driver").trim();
	}
	public String getDataBasePassword() {
		return properties.getProperty("config.database.password").trim();
	}
	public String getDataBaseUsername() {
		return properties.getProperty("config.database.username").trim();
	}
	public String getDataBaseUrl() {
		String url = null;
		url = properties.getProperty("config.database.url").trim();
		return url.trim();
	}
	public String getDataBaseSchema() {
		String url = null;
		url = properties.getProperty("config.database.schema").trim();
		if(url.trim().equalsIgnoreCase(""))
			return null;
		else
			return url.trim();
	}
	public String getDataBaseCatalog() {
		String url = null;
		url = properties.getProperty("config.database.catalog").trim();
		if(url.trim().equalsIgnoreCase(""))
			return null;
		else
			return url.trim();
	}
	
	public String getDataBaseType() {
		return properties.getProperty("config.database.type").trim();
	}

	
/*****************************************************************************************
 * PROJECT PROPERTIES READER
 *****************************************************************************************/

	public String getTables(){
		return properties.getProperty("config.database.tables");
	}	
	public String getSource(){
		return properties.getProperty("config.sourceDir");
	}	
	public String getResource(){
		return properties.getProperty("config.resourceDir");
	}
	public String getSourceTest(){
		return properties.getProperty("config.sourceTest");
	}	
	public String getResourceTest(){
		return properties.getProperty("config.resourceTest");
	}		
	private String getProjectBasePkg() {
		return properties.getProperty("config.project.basePkg").trim();
	}
	public String getProjectRootDir() {
		return properties.getProperty("config.project.rootDir").trim()+File.separator;
	}
	public String getProjectOutPutDir(){
		return properties.getProperty("config.project.outPutDir").trim()+File.separator;
	}
	private String getProjectWebRootName() {
		return properties.getProperty("config.project.webRootName").trim()+File.separator;
	}
	public String getProjectWebRootDir() {
		return (getProjectRootDir()+getProjectWebRootName() + File.separator).trim();
	}
	public String getProjectRootPkg() {
		return getProjectBasePkg().trim();
	}
	public String getProjectRootPkgDir() {
		return (getProjectRootPkg().replace('.', '/') + "/").trim();
	}
	public String getProjectGUI() {
		return properties.getProperty("config.project.gui").trim();
	}
	public String getProjectName(){
		return properties.getProperty("config.project.ProjectName").trim();
	}
	
/*****************************************************************************************
 * MYBATIS PROPERTIES READER
 *****************************************************************************************/

	public String getFullModelPkg (){
		return getProjectRootPkg() + "." + properties.getProperty("config.mybatis.pkg.model").trim();
	}
	public String getFullDaoPkg (){
		return getProjectRootPkg() + "." + properties.getProperty("config.mybatis.pkg.dao").trim();
	}
	public String getFullSqlPkg (){
		return getProjectRootPkg() + "." + properties.getProperty("config.mybatis.pkg.sql").trim();
	}
	public String getFullUtilPkg (){
		return getProjectRootPkg() + "." + properties.getProperty("config.mybatis.pkg.util").trim();
	}
	public String getFullServicePkg (){
		return getProjectRootPkg() + "." + properties.getProperty("config.mybatis.pkg.service").trim();
	}
	public String getFullWsPkg (){
		return getProjectRootPkg() + "." + properties.getProperty("config.mybatis.pkg.ws").trim();
	}
	public String getFullModelDir (){
		return getFullModelPkg().replace('.', '/') + "/";
	}
	public String getFullDaoDir (){
		return getFullDaoPkg().replace('.', '/') + "/";
	}
	public String getFullSqlDir (){
		return getFullSqlPkg().replace('.', '/') + "/";
	}
	public String getFullServiceDir (){
		return getFullServicePkg().replace('.', '/') + "/";
	}
	public String getFullUtilDir (){
		return getFullUtilPkg().replace('.', '/') + "/";
	}
	public String getFullWsDir (){
		return getFullWsPkg().replace('.', '/') + "/";
	}
}
