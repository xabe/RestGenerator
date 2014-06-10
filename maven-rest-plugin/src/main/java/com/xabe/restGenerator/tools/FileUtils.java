package com.xabe.restGenerator.tools;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileFilter;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.io.RandomAccessFile;
import java.net.URI;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.maven.plugin.logging.Log;
import org.apache.maven.plugin.logging.SystemStreamLog;

/**
 * Clase de  utilidades para manejar ficheros
 * @author Chabir Atrahouch
 *
 */
public class FileUtils {
	private final static String LOG = "FILE_UTILS";
	private Log logger = new SystemStreamLog();

	/**
	 * Devuelve el path absoluto de un fichero con path relativo al classpath
	 * 
	 * @param fileName
	 *            por ejemplo un fichero "some/path/to/a/file.txt"
	 * @return el path del fichero
	 */

	public static String getAbsolutePath(String fileName) {
		File file = new File(fileName);
		return file.getAbsolutePath();
	}

	/**
	 * Crea un fichero en la ubicacion y con el contenido de texto indicado
	 * 
	 * @param path
	 * @param name
	 * @param content
	 * @throws Exception
	 */
	public void writeFile(String path, String name, String content)
			throws Exception {

		try {
			String ruta = path + File.separator + name;

			// Create file
			FileWriter fstream = new FileWriter(ruta);
			BufferedWriter out = new BufferedWriter(fstream);
			out.write(content);
			// Close the output stream
			out.close();
		} catch (Exception e) {
			logger.error("Error escribiendo fichero", e);
		}

	}

	/**
	 * Ayade fichero el contenido
	 * 
	 * @param path
	 * @param name
	 * @param content
	 * @throws Exception
	 */
	public void appendFile(String path, String content) throws Exception {

		try {

			// Create file
			FileWriter fstream = new FileWriter(path, true);
			BufferedWriter out = new BufferedWriter(fstream);
			out.append('\n');
			out.append(content);
			// Close the output stream
			out.close();
		} catch (Exception e) {
			logger.error("Error escribiendo fichero", e);
		}

	}

	/**
	 * Crea un directorio.
	 * 
	 * @param path
	 *            la ruta al directorio para ser creado.
	 * @throws Exception
	 *             Excepcion levantada en caso de error.
	 */
	public void crearDirectorio(String path) throws Exception {
		File f = null;
		try {
			f = new File(path);
			if (f.exists()) {
				// el directorio ya existe
				if(logger.isDebugEnabled())
					logger.debug("El directorio '" + path + "' ya existe");
			} else {
				// crear el directorio asociado y niveles superiores si es
				// necesario
				if(logger.isDebugEnabled())
					logger.debug("Creando directorio '" + path + "'");
				boolean creado = f.mkdirs();
				if (!creado)
					logger.debug("Warning: No se ha creado el directorio '"
							+ path + "'");
			}
		} catch (Exception e) {

			logger.error("Error creando directorio. ", e);
		}
	}

	/**
	 * Copies src file to dst file. If the dst file does not exist, it is
	 * created
	 * 
	 * @param src
	 * @param dst
	 * @throws IOException
	 */

	public void copyFile(String src, String dst) throws IOException {

		File srcFile = new File(src);
		File dstFile = new File(dst);

		copyFile(srcFile, dstFile);
	}

	/**
	 * Reemplaza en sigular y el plural con y sin CamelCase. The method reads a
	 * file line by line, replaces a string in each line if any, appends the
	 * resulting line to a StringBuffer. After all the file has been read to a
	 * StringBuffer, it is written to the same file. The argumens to the
	 * String.replaceAll method must follow the regular expression pattern
	 * rules. Usage: readReplace("filename.txt", "Russia", "China");
	 * 
	 * @param fname
	 *            nombre del fichero
	 * @param oldPattern
	 *            En singular
	 * @param replPattern
	 *            En singular
	 */
	public void readReplace(String fname, String oldPattern, String replPattern) {
		String line;

		StringBuffer sb = new StringBuffer();
		try {
			FileInputStream fis = new FileInputStream(fname);
			BufferedReader reader = new BufferedReader(new InputStreamReader(
					fis));
			while ((line = reader.readLine()) != null) {
				Matcher matcher = Pattern.compile(oldPattern).matcher(line);
				if (matcher.find()){
					matcher = Pattern.compile(oldPattern).matcher(line);
					line = matcher.replaceAll(replPattern);
				}
				sb.append(line + Constants.RETURN);
			}
			reader.close();
			BufferedWriter out = new BufferedWriter(new FileWriter(fname));
			out.write(sb.toString());
			out.close();
			// logger.debug("Fichero remplazado con exito " + fname);
		} catch (Exception e) {
			logger.error(LOG, e);
		}
	}
	
	public void readAddBeforePattern(String fname, String oldPattern, String addPattern[]) {
		String line;

		StringBuffer sb = new StringBuffer();
		try {
			FileInputStream fis = new FileInputStream(fname);
			BufferedReader reader = new BufferedReader(new InputStreamReader(fis));
			boolean search = false;
			while ((line = reader.readLine()) != null) {
				if (!search)
				{
					Matcher matcher = Pattern.compile(oldPattern).matcher(line);
					if (matcher.find()){
						search = true;
						for(String addLine: addPattern){
							if(addLine != null && addLine.length() > 0)
							{
								sb.append(addLine + Constants.RETURN);
							}
						}
					}
				}
				sb.append(line + Constants.RETURN);
			}
			reader.close();
			BufferedWriter out = new BufferedWriter(new FileWriter(fname));
			out.write(sb.toString());
			out.close();
			// logger.debug("Fichero remplazado con exito " + fname);
		} catch (Exception e) {
			logger.error(LOG, e);
		}
	}

	/**
	 * Borra una determinada linea de texto de un fichero donde encuentre un
	 * patron
	 * 
	 * @param fname
	 * @param pattern
	 */
	public void readDelete(String fname, String pattern) {
		String line;

		StringBuffer sb = new StringBuffer();
		try {
			FileInputStream fis = new FileInputStream(fname);
			BufferedReader reader = new BufferedReader(new InputStreamReader(
					fis));
			while ((line = reader.readLine()) != null) {
				if (line.indexOf(pattern) == -1)// Si no esta el patron en la
												// linea la guarda de lo
												// contrario la salta
					sb.append(line + Constants.RETURN);

			}
			reader.close();
			BufferedWriter out = new BufferedWriter(new FileWriter(fname));
			out.write(sb.toString());
			out.close();
			if(logger.isDebugEnabled())
				logger.debug("Borrado " + pattern + " del fichero " + fname);
		} catch (Exception e) {
			logger.error(LOG, e);
		}
	}

	/**
	 * Busca una determinada linea de texto de un fichero donde encuentre un
	 * patron
	 * 
	 * @param fname
	 * @param pattern
	 */
	public boolean readPattern(String fname, String pattern) {
		String line;

		try {
			FileInputStream fis = new FileInputStream(fname);
			BufferedReader reader = new BufferedReader(new InputStreamReader(
					fis));
			while ((line = reader.readLine()) != null) {
				if (line.indexOf(pattern) != -1)// Si no esta el patron en la
												// linea la guarda de lo
												// contrario la salta
				{
					reader.close();
					return true;
				}

			}
			reader.close();
			if(logger.isDebugEnabled())
				logger.debug("Encontrado el patron " + pattern + " en el fichero "
					+ fname);
			return false;
		} catch (Exception e) {
			return false;
		}
	}

	/**
	 * Obtiene los ficheros de un directorio
	 * 
	 * @param directoryName
	 * @return
	 */
	public File[] getFiles(String directoryName) {
		File dir = new File(directoryName);
		if(dir.exists())
			logger.info("no es nullo");
		else
			logger.info("es nulo");

		// The list of files can also be retrieved as File objects
		File[] files = dir.listFiles();

		// This filter only returns files
		FileFilter fileFilter = new FileFilter() {
			public boolean accept(File file) {
				return file.isFile();
			}
		};
		if (files == null)// Si no hay ficheros retornamos null
			return null;
		files = dir.listFiles(fileFilter);
		for (int i = 0; i < files.length; i++) {
			logger.error(files[i].getAbsoluteFile().getName());
		}
		return files;

	}
	
	public File[] getFiles(URI directoryName) {
		File dir = new File(directoryName);
		if(dir.exists())
			logger.info("no es nullo");
		else
			logger.info("es nulo");

		// The list of files can also be retrieved as File objects
		File[] files = dir.listFiles();

		// This filter only returns files
		FileFilter fileFilter = new FileFilter() {
			public boolean accept(File file) {
				return file.isFile();
			}
		};
		if (files == null)// Si no hay ficheros retornamos null
			return null;
		files = dir.listFiles(fileFilter);
		for (int i = 0; i < files.length; i++) {
			logger.error(files[i].getAbsoluteFile().getName());
		}
		return files;

	}

	/**
	 * Copia usando string como entrada
	 * 
	 * @param srcDirName
	 * @param dstDirName
	 * @throws IOException
	 */
	public void copyDirectory(String srcDirName, String dstDirName)
			throws IOException {
		File srcDir = new File(srcDirName);
		File dstDir = new File(dstDirName);
		copyDirectory(srcDir, dstDir);
	}

	/**
	 * Copies all files under srcDir to dstDir. If dstDir does not exist, it
	 * will be created. No copia los directorios que empiezan por . que son los
	 * ocultos
	 * 
	 * @param srcDir
	 * @param dstDir
	 * @throws IOException
	 */
	public void copyDirectory(File srcDir, File dstDir) throws IOException {
		if (srcDir.isDirectory()) {
			if (srcDir.getName().startsWith(".")) {// Si es un directorio oculto
													// no lo copiamos
				return;
			}

			// Si el directorio no existe
			if (!dstDir.exists()) {
				if(logger.isDebugEnabled())
					logger.debug("El directorio no existe lo creamos. " + dstDir);
				dstDir.mkdirs();
			}

			String[] children = srcDir.list();
			for (int i = 0; i < children.length; i++) {

				copyDirectory(new File(srcDir, children[i]), new File(dstDir,
						children[i]));

			}
		} else {
			// This method is implemented in e1071 Copying a File
			copyFile(srcDir, dstDir);
		}
	}

	/**
	 * Copies src file to dst file. If the dst file does not exist, it is
	 * created
	 * 
	 * @param src
	 * @param dst
	 * @throws IOException
	 */

	public void copyFile(File src, File dst) throws IOException {
		InputStream in = new FileInputStream(src);
		OutputStream out = new FileOutputStream(dst);

		// Transfer bytes from in to out
		byte[] buf = new byte[1024];
		int len;
		while ((len = in.read(buf)) > 0) {
			if(logger.isDebugEnabled())
				logger.debug("Copiando fichero " + src.getAbsolutePath() + " a "
					+ dst.getAbsolutePath());
			out.write(buf, 0, len);
		}
		in.close();
		out.close();
	}
	
	public void copyFile(InputStream in, File dst) throws IOException {
		OutputStream out = new FileOutputStream(dst);

		// Transfer bytes from in to out
		byte[] buf = new byte[1024];
		int len;
		while ((len = in.read(buf)) > 0) {
			if(logger.isDebugEnabled())
				logger.debug("Copiando fichero  a "
					+ dst.getAbsolutePath());
			out.write(buf, 0, len);
		}
		in.close();
		out.close();
	}

	/**
	 * Renombra un fichero
	 * 
	 * @param oldFileName
	 * @param newFileName
	 */
	public void renameFile(String oldFileName, String newFileName) {

		// Construct the file object. Does NOT create a file on disk!
		File f = new File(oldFileName); // backup of this source file.

		// Rename the backup file to "junk.dat"
		// Renaming requires a File object for the target.
		f.renameTo(new File(newFileName));

	}

	/**
	 * Inserta una linea en un fichero despues de una marca igual a mark
	 * 
	 * @param inFileName
	 * @param pattern
	 * @param lineToBeInserted
	 */
	// Añade una linea dada a partir de la marca
	public void insertAfterMark(String inFileName, String mark,
			String lineToBeInserted, boolean deleteTemp) {
		try {

			// temp file
			File outFile = new File(inFileName + ".tmp");

			File inFile = new File(inFileName);

			// input
			FileInputStream fis = new FileInputStream(inFile);
			BufferedReader in = new BufferedReader(new InputStreamReader(fis));

			// output
			FileOutputStream fos = new FileOutputStream(outFile);
			PrintWriter out = new PrintWriter(fos);

			String thisLine = "";
			boolean insertar = false;
			// Busca la linea donde encuentre la marca mark y si la encuentra
			// despues inserta la linea
			while ((thisLine = in.readLine()) != null) {
				if (insertar) {
					out.println(lineToBeInserted);
					insertar = false;
					if(logger.isDebugEnabled())
						logger.debug("Insertada la linea \"" + lineToBeInserted
							+ "\" despues de la marca \"" + mark
							+ "\" en el fichero " + inFileName);
				}
				if (thisLine.indexOf(mark) != -1) {
					insertar = true;// Inserta en la proxima
				}

				out.println(thisLine);
			}

			out.flush();
			out.close();
			in.close();
			if (deleteTemp) {
				inFile.delete();
				outFile.renameTo(inFile);
			}

		} catch (Exception e) {
			logger.error(LOG, e);
		}

	}

	// Añade el contenido de un fichero dado a partir de la marca
	public void insertAfterMark(String srcFileName, String customFileName,
			String mark) {
		try {

			// temp file
			File outFile = new File(srcFileName + ".tmp");

			File srcFile = new File(srcFileName);
			File customFile = new File(customFileName);

			// input
			FileInputStream fisrc = new FileInputStream(srcFile);
			BufferedReader insrc = new BufferedReader(new InputStreamReader(
					fisrc));

			FileInputStream fic = new FileInputStream(customFile);
			BufferedReader inc = new BufferedReader(new InputStreamReader(fic));

			// output
			FileOutputStream fos = new FileOutputStream(outFile);
			PrintWriter out = new PrintWriter(fos);

			String thisLine = "";
			boolean insertar = false;
			// Busca la linea donde encuentre la marca mark y si la encuentra
			// despues inserta el fichero
			while (((thisLine = insrc.readLine()) != null) && !insertar) {
				if ((thisLine.indexOf(mark) != -1)
						|| (thisLine.indexOf("Custom") != -1)) {
					insertar = true;// Inserta en la proxima
				} else {
					out.println(thisLine);
				}
			}

			while ((thisLine = inc.readLine()) != null) {
				out.println(thisLine);
			}
			out.println(mark);
			out.flush();
			out.close();
			inc.close();
			insrc.close();
			srcFile.delete();
			outFile.renameTo(srcFile);
		} catch (Exception e) {
			logger.error(LOG, e);
		}

	}
	
	// Añade el contenido de un fichero dado a partir de la marca
		public void insertAfterPoint(String srcFileName, String customFileName,
				String mark) {
			try {

				// temp file
				File outFile = new File(srcFileName + ".tmp");

				File srcFile = new File(srcFileName);
				File customFile = new File(customFileName);

				// input
				FileInputStream fisrc = new FileInputStream(srcFile);
				BufferedReader insrc = new BufferedReader(new InputStreamReader(
						fisrc));

				FileInputStream fic = new FileInputStream(customFile);
				BufferedReader inc = new BufferedReader(new InputStreamReader(fic));

				// output
				FileOutputStream fos = new FileOutputStream(outFile);
				PrintWriter out = new PrintWriter(fos);

				String thisLine = "";
				// Busca la linea donde encuentre la marca mark y si la encuentra
				// despues inserta el fichero
				while (((thisLine = insrc.readLine()) != null)) {
					if (thisLine.indexOf(mark) != -1) {
						while ((thisLine = inc.readLine()) != null) {
							out.println(thisLine);
						}
					} else {
						out.println(thisLine);
					}
				}
				
				out.flush();
				out.close();
				inc.close();
				insrc.close();
				srcFile.delete();
				outFile.renameTo(srcFile);
			} catch (Exception e) {
				logger.error(LOG, e);
			}

		}

	/**
	 * Deletes all files and subdirectories under dir. If the directory is not
	 * empty, it is necessary to first recursively delete all files and
	 * subdirectories in the directory. Here is a method that will delete a
	 * non-empty directory. Returns true if all deletions were successful. If a
	 * deletion fails, the method stops attempting to delete and returns false.
	 */

	public boolean deleteDir(File dir) {
		if (dir.isDirectory()) {
			String[] children = dir.list();
			for (int i = 0; i < children.length; i++) {
				boolean success = deleteDir(new File(dir, children[i]));
				if (!success) {
					return false;
				}
			}
		}

		// The directory is now empty so delete it
		return dir.delete();
	}

	public boolean fileExists(String file) {

		File f = new File(file);
		logger.info(f + (f.exists() ? " is found " : " is missing "));

		return f.exists() ? true : false;
	}

	// Clase que añade texto al final del fichero
	public void appendVMFileToClass(String classfileName, String vmFile,
			String className, String tableName,String text) {
		FileReader fr = null;
		BufferedReader br = null;
		FileWriter fw = null;
		PrintWriter pw = null;

		try {
			deleteLinesAtTheBottom(classfileName, text);
			fw = new FileWriter(classfileName, true);
			pw = new PrintWriter(fw);
			fr = new FileReader(vmFile);
			br = new BufferedReader(fr);

			// Lectura del fichero
			String linea;
			pw.println("");
			while ((linea = br.readLine()) != null) {
				linea = linea.replace("${clase}", className);
				linea = linea.replace("${table}", tableName);
				pw.println(linea);
			}
			pw.println("}");
		} catch (Exception e) {
			logger.error(LOG, e);
		} finally {
			// Cerramos ficheros
			try {
				if (fw != null)
					fw.close();
				if (fr != null)
					fr.close();
			} catch (Exception e2) {
				e2.printStackTrace();
			}
		}
	}

	// clase que elimina la ultima fila del fichero
	public void deleteLinesAtTheBottom(String file,String text) {
		try {
			RandomAccessFile raf = new RandomAccessFile(file, "rw");
			long length = raf.length();
			boolean endFile = false;
			String linea;
			while(!endFile && length > 0){
				length--;
				raf.seek(length);
				linea = raf.readLine();
				if(linea != null && linea.indexOf(text) == 0)
				{
					endFile = true;
					length--;
				}
			}
			raf.setLength(length);
			raf.close();
			logger.info("Eliminada ultima linea del fichero " + file);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	// Clase que añade texto al final del fichero
	public void appendFiles(String classfileName, String templateFile,String text) {
		FileReader fr = null;
		BufferedReader br = null;
		FileWriter fw = null;
		PrintWriter pw = null;

		try {
			deleteLinesAtTheBottom(classfileName,text);
			fw = new FileWriter(classfileName, true);
			pw = new PrintWriter(fw);
			fr = new FileReader(templateFile);
			br = new BufferedReader(fr);

			// Lectura del fichero
			String linea;
			pw.println("");
			while ((linea = br.readLine()) != null) {
				pw.println(linea);
			}
			pw.println("}");
		} catch (Exception e) {
			logger.error(LOG, e);
		} finally {
			// Cerramos ficheros
			try {
				if (fw != null)
					fw.close();
				if (fr != null)
					fr.close();
			} catch (Exception e2) {
				e2.printStackTrace();
			}
		}
	}

	public void deleteFile(String fileName) {
		boolean success = (new File(fileName)).delete();
		if (!success) {
			logger.error("Error delete " + fileName);
		}
	}

}
