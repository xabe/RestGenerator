package com.xabe.restGenerator;

import java.util.List;

import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugins.annotations.LifecyclePhase;
import org.apache.maven.plugins.annotations.Mojo;

import com.xabe.restGenerator.tools.Generator;
import com.xabe.restGenerator.tools.Table;



@Mojo(name="generatorKodeEngine", defaultPhase=LifecyclePhase.VALIDATE)
public class GeneratorKodeEngine extends BaseKodeEngine {

	public void execute() throws MojoExecutionException {
		try
		{
			init();
			getLog().info("Empieza el mojo de generatoKodeEngine");
				
			List<Table> tables = getTables(projectProperties.getTables(), projectProperties);

			new Generator(projectProperties, tables);
			
			getLog().info("Termina el mojo de generatoKodeEngine");
		} catch (Exception e) {
			getLog().error("Error en el mojo de generatoKodeEngine motivo del error : "+e.getMessage(), e);
			System.exit(0);
		}
	}
}
