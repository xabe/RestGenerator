#set( $symbol_pound = '#' )
#set( $symbol_dollar = '$' )
#set( $symbol_escape = '\' )
package ${package}.ws;

import java.io.IOException;

import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerResponseContext;
import javax.ws.rs.container.ContainerResponseFilter;


public class CORSFilter implements ContainerResponseFilter {
	
	@Override
	public void filter(ContainerRequestContext arg0,ContainerResponseContext arg1) throws IOException {
		arg1.getHeaders().add("Access-Control-Allow-Origin", "*");
		arg1.getHeaders().add("Access-Control-Allow-Methods","GET, POST, DELETE, OPTIONS, PUT");
		arg1.getHeaders().add("Access-Control-Allow-Headers","X-CSRF-Token, X-Requested-With, Accept, Accept-Version, Content-Length, Content-MD5, Content-Type, Date, X-Api-Version,X-HTTP-Method-Override, Origin, Authorization");
		arg1.getHeaders().add("Access-Control-Allow-Credentials", "true");
		arg1.getHeaders().add("Access-Control-Expose-Headers", "Location");
	}
}
