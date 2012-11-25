package se.z_app.httpserver;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Properties;

/**
 * Class that creates and handles a HTTP server
 * 
 * @author Rasmus Holm
 */
public class ZmoteHTTPD extends NanoHTTPD {

	private HashMap<String, ZmoteHTTPDRequestHandler> handlers = new HashMap<String, ZmoteHTTPDRequestHandler>();
	private File wwwroot;
	
	/**
	 * Creates a HTTP server to a specified port. Throws an IOException if the socket is already in use.
	 * @param port the port to use
	 * @param wwwroot 
	 * @throws IOException if the socket is already in use
	 */
	public ZmoteHTTPD(int port, File wwwroot) throws IOException {
		super(port, wwwroot);
		this.wwwroot = wwwroot;
	}
	
	/**
	 * Calls for the specified HTTP response from the server.
	 * @param uri Percent-decoded URI without parameters, for example "/index.cgi"
	 * @param method "GET", "POST" etc.
	 * @param parms Parsed, percent decoded parameters from URI and, in case of POST, data.
	 * @param header Header entries, percent decoded
	 * @param files 
	 * @return An HTTP response
	 */
	@Override
	public synchronized Response serve( String uri, String method, Properties header, Properties parms, Properties files )
	{

		ZmoteHTTPDRequestHandler handler;
		synchronized(handlers){
			handler = handlers.get(uri);
		}
		
		if(handler == null)
			return new Response(HTTP_NOTFOUND, MIME_PLAINTEXT, "404: not found");
		return handler.clone().serve(uri, method, header, parms, files, this);
	}
	
	/**
	 * Adds a handler to the list of handlers.
	 * @param handler the handler to add to the list
	 */
	public void addHandler(ZmoteHTTPDRequestHandler handler){
		synchronized(handlers){
			//Log.i("WebServer", "Adding handler: " + handler.getURI());
			handlers.put(handler.getURI(), handler);
		}
	}
	
	/**
	 * Removes a handler from the list of handlers.
	 * @param handler the handler to remove from the list
	 */
	public  void removeHandler(ZmoteHTTPDRequestHandler handler){
		synchronized(handlers){
			handlers.remove(handler.getURI().trim());
		}
	}
	
	

}
