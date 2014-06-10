#set( $symbol_pound = '#' )
#set( $symbol_dollar = '$' )
#set( $symbol_escape = '\' )
package ${package}.ws;

import java.util.HashMap;
import java.util.Map;

import javax.ws.rs.core.MediaType;

import org.glassfish.jersey.jackson.JacksonFeature;
import org.glassfish.jersey.server.ResourceConfig;
import org.glassfish.jersey.server.ServerProperties;
import org.glassfish.jersey.server.spring.scope.RequestContextFilter;

public class MyApplication extends ResourceConfig {
	
	private Map<String, MediaType> mediaTypeMap;
	private Map<String, String> languageTypeMap;
	
    public MyApplication () {
    	packages("${package}.ws");
        register(RequestContextFilter.class);
        register(JacksonFeature.class);
        //property(ServerProperties.MEDIA_TYPE_MAPPINGS, "txt : text/plain, xml : application/xml, json : application/json");
        if (mediaTypeMap == null)
        {
            mediaTypeMap = new HashMap<String, MediaType>();
            mediaTypeMap.put("json", MediaType.APPLICATION_JSON_TYPE);
            mediaTypeMap.put("xml", MediaType.APPLICATION_XML_TYPE);
            mediaTypeMap.put("txt", MediaType.TEXT_PLAIN_TYPE);
            mediaTypeMap.put("html", MediaType.TEXT_HTML_TYPE);
            mediaTypeMap.put("xhtml", MediaType.APPLICATION_XHTML_XML_TYPE);
            MediaType jpeg = new MediaType("image", "jpeg");
            mediaTypeMap.put("jpg", jpeg);
            mediaTypeMap.put("jpeg", jpeg);
            mediaTypeMap.put("zip", new MediaType("application", "x-zip-compressed"));
        }
        property(ServerProperties.MEDIA_TYPE_MAPPINGS, mediaTypeMap);
        
        
        if(languageTypeMap == null)
        {
        	languageTypeMap = new HashMap<String, String>();
        	languageTypeMap.put("english", "en");
        	languageTypeMap.put("spanish", "es");
        }
        property(ServerProperties.LANGUAGE_MAPPINGS, languageTypeMap);
    }
    
}
