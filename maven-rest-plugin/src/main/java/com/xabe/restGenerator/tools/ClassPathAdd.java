package com.xabe.restGenerator.tools;

import java.io.File;
import java.lang.reflect.Method;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;

import org.apache.maven.plugin.logging.Log;
import org.apache.maven.plugin.logging.SystemStreamLog;

/**
 * Clase para añadir al classpasth un directorio
 * @author Chabir Atrahouch
 *
 */
public class ClassPathAdd {
	private Log log = new SystemStreamLog();
    private static final String METODO_ADD_URL = "addURL";
    private static final Class<?>[] PARAMETRO_METODO = new Class[]{URL.class};
    private final URLClassLoader loader;
    private final Method metodoAdd;

    public ClassPathAdd() throws NoSuchMethodException {
        loader = (URLClassLoader) ClassLoader.getSystemClassLoader();
        metodoAdd = URLClassLoader.class.getDeclaredMethod(
                                          METODO_ADD_URL, PARAMETRO_METODO);
        metodoAdd.setAccessible(true);
    }

    public URL[] getURLs() {
        return loader.getURLs();
    }

    public void addURL(URL url) {
        if (url != null) {
            try {
                metodoAdd.invoke(loader, new Object[]{url});
            } catch (Exception ex) {
                log.error("Excepcion al guardar URL: " + ex.getLocalizedMessage());
            }
        }
    }

    public void addURLs(URL[] urls) {
        if (urls != null) {
            for (URL url : urls) {
                addURL(url);
            }
        }
    }

    public void addDir(File file) throws MalformedURLException {
        if (file != null) {
            addURL(file.toURI().toURL());
        }
    }

    public void addFile(String name) throws MalformedURLException {
        addDir(new File(name));
    }
}
