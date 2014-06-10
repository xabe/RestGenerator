package com.xabe.restGenerator.tools;

import java.io.File;

/**
 * Clase que se encarga de comprar los ficheros 
 * @author Chabir Atrahouch
 *
 */
public class ProjectManager {
	
	public static boolean existsApplicationContext(ProjectProperties projectProperties){
		return new File(projectProperties.getProjectRootDir() + projectProperties.getResource() + File.separator + Constants.APPLICATION_CONTEXT).exists();
	}
	
	public static boolean existsLog4j(ProjectProperties projectProperties){
		return new File(projectProperties.getProjectRootDir() + projectProperties.getResource() + File.separator + Constants.LOG4J).exists();
	}
	
	public static boolean existsJaxRs(ProjectProperties projectProperties){
		return new File(projectProperties.getProjectRootDir() + projectProperties.getSource()+ File.separator+ projectProperties.getFullWsDir()+ File.separator + Constants.JAX_RS_BASE).exists();
	}
	
	public static boolean existsContext(ProjectProperties projectProperties){
		return new File(projectProperties.getProjectWebRootDir()+File.separator+Constants.META_INF+File.separator+Constants.CONTEXT).exists();
	}
	
	public static boolean existsJettyContext(ProjectProperties projectProperties){
		return new File(projectProperties.getProjectWebRootDir()+File.separator+Constants.META_INF+File.separator+Constants.JETTY_CONTEXT).exists();
	}
}
