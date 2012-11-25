package se.z_app.httpserver;

import java.util.Properties;

import se.z_app.httpserver.NanoHTTPD.Response;

/**
 * Interface for handlers of HTTPD requests
 * 
 * @author Rasmus Holm
 */
public interface ZmoteHTTPDRequestHandler{
	public String getURI();
	public Response serve(String uri, String method, Properties header, Properties parms, Properties files, ZmoteHTTPD httpd);	
	public ZmoteHTTPDRequestHandler clone();
}
