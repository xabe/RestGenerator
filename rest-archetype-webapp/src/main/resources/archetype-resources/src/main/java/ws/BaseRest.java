#set( $symbol_pound = '#' )
#set( $symbol_dollar = '$' )
#set( $symbol_escape = '\' )
package ${package}.ws;

import java.net.URI;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.UriInfo;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class BaseRest {
protected Logger logger = LoggerFactory.getLogger(BaseRest.class);
	
	@Context
	protected HttpServletRequest httpServletRequest;
	
	@Context
	protected UriInfo uriInfo;
	
	
	public HttpServletRequest getHttpServletRequest() {
		return httpServletRequest;
	}
	
	public URI getAbsolutePath(){
		return uriInfo.getAbsolutePath();
	}

}
