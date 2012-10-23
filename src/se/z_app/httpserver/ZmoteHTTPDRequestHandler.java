package se.z_app.httpserver;

import java.util.Properties;

import se.z_app.httpserver.NanoHTTPD.Response;

public interface ZmoteHTTPDRequestHandler{
	public String getURI();
	public Response serve(String method, Properties header, Properties parms, Properties files);	
	public ZmoteHTTPDRequestHandler clone();
}
