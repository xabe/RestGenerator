package com.xabe.restGenerator;

import java.io.File;
import java.text.MessageFormat;
import java.util.List;

import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugins.annotations.LifecyclePhase;
import org.apache.maven.plugins.annotations.Mojo;

import com.xabe.restGenerator.tools.ClassPathAdd;
import com.xabe.restGenerator.tools.Constants;
import com.xabe.restGenerator.tools.FileUtils;
import com.xabe.restGenerator.tools.Table;
import com.xabe.restGenerator.tools.TemplateUtils;


@Mojo(name="persistenceKodeEngine", defaultPhase=LifecyclePhase.COMPILE)
public class PersistenceKodeEngine extends BaseKodeEngine {

	public void execute() throws MojoExecutionException {
		try
		{
			init();
			getLog().info("Empieza el mojo de persistenceKodeEngine");
				
			List<Table> tables = getTables(projectProperties.getTables(), projectProperties);

			ClassPathAdd pathAdd = new ClassPathAdd();
			pathAdd.addDir(outPutProject);

			// Generate DAO
			String nameDAO;
			String nameModel;
			String file;
			String example;
			String modelDir;
			FileUtils fileUtils = new FileUtils();
			for (Table table : tables) {
				
				nameModel = table.getNamingConverter().getClassName();
				modelDir = projectProperties.getProjectRootDir() + projectProperties.getSource() + File.separator + projectProperties.getFullModelDir() + File.separator + table.getNamingConverter().getPackageName();
				
				// Mapper
				
				TemplateUtils templateUtils = new TemplateUtils(table, projectProperties);
				templateUtils.generateClassFromTemplateFile("/com/xabe/restGenerator/template/sql/Mapper.vm",
															nameModel+ "MapperTmpxml.xml",
															projectProperties.getProjectRootDir()+ projectProperties.getResource()+ File.separator+ projectProperties.getFullSqlDir()+File.separator + table.getNamingConverter().getPackageName());
				String endTag = "</mapper>";
				String xmlTmpFileName = projectProperties.getProjectRootDir() + projectProperties.getResource() + File.separator + projectProperties.getFullSqlDir() + File.separator +  table.getNamingConverter().getPackageName() + File.separator + nameModel + "MapperTmp.xml";
				String xmlFileName = projectProperties.getProjectRootDir() + projectProperties.getResource() + File.separator + projectProperties.getFullSqlDir() + File.separator + table.getNamingConverter().getPackageName() + File.separator + nameModel + "Mapper.xml";
				fileUtils.insertAfterMark(xmlFileName, xmlTmpFileName,endTag);
				fileUtils.deleteFile(xmlTmpFileName);


				// MODEL
				if(table.isPrimaryComposite())
				{
					//Es compuesta
					file = modelDir + File.separator + nameModel + Constants.KEY + Constants.JAVA;
					fileUtils.readReplace(file, MessageFormat.format(
						Constants.MODEL_ENTITY_OLD, nameModel + Constants.KEY),
						MessageFormat.format(Constants.MODEL_ENTITY_NEW_COMPOSITE,
								nameModel + Constants.KEY,
								projectProperties.getFullModelPkg(),
								projectProperties.getProjectRootPkg()));
					
					file = modelDir + File.separator + nameModel + Constants.JAVA;
					fileUtils.readReplace(file, MessageFormat.format(
						Constants.MODEL_ENTITY_NEW_COMPOSITE_KEY_OLD, nameModel),
						MessageFormat.format(Constants.MODEL_ENTITY_NEW_COMPOSITE_KEY,
								nameModel,
								projectProperties.getProjectRootPkg()));
				}
				else
				{
					file = modelDir + File.separator + nameModel + Constants.JAVA;
					fileUtils.readReplace(file, MessageFormat.format(
						Constants.MODEL_ENTITY_OLD, nameModel),
						MessageFormat.format(Constants.MODEL_ENTITY_NEW,
								nameModel,
								projectProperties.getFullModelPkg(),
								projectProperties.getProjectRootPkg()));
				}
						

				// EXAMPLE
				example = nameModel + Constants.EXAMPLE;
				file = modelDir + File.separator+ example + Constants.JAVA;
				fileUtils.readReplace(file, MessageFormat.format(
						Constants.EXAMPLE_ENTITY_OLD, example),
						MessageFormat.format(Constants.EXAMPLE_ENTITY_NEW,
								example,
								projectProperties.getFullModelPkg(),
								projectProperties.getFullModelPkg()));
				// CRITERIA
				
				String entity =  table.getNamingConverter().getClassName()+Constants.EXAMPLE;
				templateUtils.generateClassFromTemplateFile("/com/xabe/restGenerator/template/model/",Constants.EMTITY_EXAMPLE, entity + "Tmp",modelDir);
				String str = modelDir + File.separator + entity + "Tmp.java";
				entity = modelDir + File.separator + entity + Constants.JAVA;
				fileUtils.insertAfterPoint(entity, str, Constants.CRITERIA);
				fileUtils.deleteFile(str);

				// MAPPER INTERFACE
				nameDAO = table.getNamingConverter().getClassName() + Constants.MAPPER;
				
				file = projectProperties.getProjectRootDir() + projectProperties.getSource() + File.separator + projectProperties.getFullDaoDir() + File.separator + table.getNamingConverter().getPackageName() + File.separator + nameDAO + Constants.JAVA;
				fileUtils.readReplace(file, MessageFormat.format(
						Constants.DAO_INTERFACE_OLD, nameDAO),
						MessageFormat.format(Constants.DAO_INTERFACE_NEW,
								nameDAO, nameModel, example,
								projectProperties.getFullDaoPkg()));

						}
			getLog().info("Termina el mojo de persistenceKodeEngine");
		} 
		catch (Exception e) {
			getLog().error("Error en el mojo de persistenceKodeEngine motivo del error : "+e.getMessage(), e);
			System.exit(0);
		}
	}	
}
