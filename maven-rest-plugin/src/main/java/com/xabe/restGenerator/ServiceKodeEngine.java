package com.xabe.restGenerator;

import java.io.File;
import java.util.List;

import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugins.annotations.LifecyclePhase;
import org.apache.maven.plugins.annotations.Mojo;

import com.xabe.restGenerator.tools.Constants;
import com.xabe.restGenerator.tools.Table;
import com.xabe.restGenerator.tools.TemplateUtils;




@Mojo(name="serviceKodeEngine", defaultPhase=LifecyclePhase.COMPILE)
public class ServiceKodeEngine extends BaseKodeEngine {
	
	public void execute() throws MojoExecutionException {
		try
		{
			init();
			getLog().info("Empieza el mojo de serviceKodeEngine");
				
			List<Table> tables = getTables(projectProperties.getTables(), projectProperties);

			for (int i = 0; i < tables.size(); i++) {
				TemplateUtils localTemplateUtils = new TemplateUtils(tables
						.get(i), projectProperties);
				

				//Service
				localTemplateUtils.generateClassFromTemplateFile(
						"/com/xabe/restGenerator/template/service/Service.vm",
						tables.get(i).getNamingConverter().getClassName()+ Constants.SERVICE,
						projectProperties.getProjectRootDir()
								+ projectProperties.getSource()
								+ File.separator
								+ projectProperties.getFullServiceDir()
								+ File.separator
								+ tables.get(i).getNamingConverter()
										.getPackageName());
				
				//Service Impl
				localTemplateUtils.generateClassFromTemplateFile(
						"/com/xabe/restGenerator/template/service/ServiceImpl.vm",
						tables.get(i).getNamingConverter().getClassName()+ Constants.SERVICE_IMPL,
						projectProperties.getProjectRootDir()
								+ projectProperties.getSource()
								+ File.separator
								+ projectProperties.getFullServiceDir()
								+ File.separator
								+ tables.get(i).getNamingConverter()
										.getPackageName());
			}
			getLog().info("Termina el mojo de serviceKodeEngine");
		} 
		catch (Exception e) {
			getLog().error("Error en el mojo de serviceKodeEngine motivo del error : "+e.getMessage(), e);
			System.exit(0);
		}
	}	
}
