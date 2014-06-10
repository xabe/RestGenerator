package com.xabe.restGenerator;

import java.io.File;
import java.util.List;

import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugins.annotations.LifecyclePhase;
import org.apache.maven.plugins.annotations.Mojo;

import com.xabe.restGenerator.tools.Constants;
import com.xabe.restGenerator.tools.Table;
import com.xabe.restGenerator.tools.TemplateUtils;




@Mojo(name="jaxRsKodeEngine", defaultPhase=LifecyclePhase.COMPILE)
public class JaxRsKodeEngine extends BaseKodeEngine {
	
	public void execute() throws MojoExecutionException {
		try
		{
			init();
			getLog().info("Empieza el mojo de jaxRsKodeEngine");
				
			List<Table> tables = getTables(projectProperties.getTables(), projectProperties);

			
			File file = new File(projectProperties.getProjectRootDir()+projectProperties.getSource()+File.separator+projectProperties.getFullWsDir());
			if(!file.exists())
			{
				file.mkdir();
			}
			TemplateUtils templateUtils;
			for (Table table : tables) {
				templateUtils = new TemplateUtils(table, projectProperties);
				
				//Service Impl
				templateUtils.generateClassFromTemplateFile(
						"/com/xabe/restGenerator/template/jax-rs/jax-rs.vm",
						table.getNamingConverter().getClassName()+ Constants.JAX_RS_IMPL,
						projectProperties.getProjectRootDir()
								+ projectProperties.getSource()
								+ File.separator
								+ projectProperties.getFullWsDir()
								+ File.separator
								+ table.getNamingConverter()
										.getPackageName());
			}
			getLog().info("Termina el mojo de jaxRsKodeEngine");
		} 
		catch (Exception e) {
			getLog().error("Error en el mojo de jaxRsKodeEngine motivo del error : "+e.getMessage(), e);
			System.exit(0);
		}
	}
	
}
