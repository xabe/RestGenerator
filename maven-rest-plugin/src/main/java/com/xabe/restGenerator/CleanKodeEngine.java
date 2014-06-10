package com.xabe.restGenerator;


import java.io.File;
import java.util.List;

import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugins.annotations.LifecyclePhase;
import org.apache.maven.plugins.annotations.Mojo;

import com.xabe.restGenerator.tools.FileUtils;
import com.xabe.restGenerator.tools.ProjectProperties;
import com.xabe.restGenerator.tools.Table;


@Mojo(name="cleanRestGenerator", defaultPhase=LifecyclePhase.VALIDATE)
public class CleanKodeEngine extends BaseKodeEngine {	

	public void execute() throws MojoExecutionException {
		try
		{
			init();
			getLog().info("Empieza limpieza del proyecto");

			FileUtils fileUtils = new FileUtils();
				
			List<Table> tables = getTables(projectProperties.getTables(), projectProperties);			
			for (Table table : tables) {
				
				deleteDAO(fileUtils, projectProperties, table);
				deleteService(fileUtils, projectProperties, table);
				deleteWs(fileUtils, projectProperties, table);
			}
			
			getLog().info("Termina limpieza del proyecto");
		} catch (Exception e) {
			getLog().error("Error en el mojo de cleanKodeEngine motivo del error : "+e.getMessage(), e);
			System.exit(0);
		}
	}
	
	private void deleteDAO(FileUtils fileUtils, ProjectProperties projectProperties, Table table){
		// Delete Model
		fileUtils.deleteDir(new File(projectProperties
				.getProjectRootDir()
				+ projectProperties.getSource()
				+ File.separator
				+ projectProperties.getFullModelDir()
				+ table.getNamingConverter().getPackageName()));

		// Delete DAO SQL
		fileUtils.deleteDir(new File(projectProperties.getProjectRootDir()
				+ projectProperties.getResource() + File.separator
				+ projectProperties.getFullSqlDir() + table.getNamingConverter().getPackageName()));

		// Delete DAO
		fileUtils.deleteDir(new File(projectProperties
				.getProjectRootDir()
				+ projectProperties.getSource()
				+ File.separator
				+ projectProperties.getFullDaoDir()
				+ table.getNamingConverter().getPackageName()));
	}
	
	private void deleteService(FileUtils fileUtils, ProjectProperties projectProperties, Table table){
		// Delete Service
		fileUtils.deleteDir(new File(projectProperties
				.getProjectRootDir()
				+ projectProperties.getSource()
				+ File.separator
				+ projectProperties.getFullServiceDir()
				+ table.getNamingConverter().getPackageName()));
	}
	
	private void deleteWs(FileUtils fileUtils, ProjectProperties projectProperties, Table table){
		// Delete Ws
		fileUtils.deleteDir(new File(projectProperties
				.getProjectRootDir()
				+ projectProperties.getSource()
				+ File.separator
				+ projectProperties.getFullWsDir()
				+ table.getNamingConverter().getPackageName()));
	}
}
