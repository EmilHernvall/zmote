package se.z_app.zmote.webtv;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Properties;


import se.z_app.httpserver.NanoHTTPD.Response;
import se.z_app.httpserver.ZmoteHTTPD;
import se.z_app.httpserver.ZmoteHTTPDRequestHandler;

/**
 * Class that handles requests to the HTTP server
 * 
 * @author Rasmus Holm
 */
public class MediaRequestHandler implements ZmoteHTTPDRequestHandler{
	private String uri;
	private File file;
	
	/**
	 * Creates a request handler with the specified URI and file
	 * @param file file to use with handler
	 * @param uri uri to use with handler
	 */
	public MediaRequestHandler(File file, String uri){
		this.uri = uri;
		this.file = file;
	}
	
	/**
	 * Getter for handler URI
	 * @return the URI of this handler
	 */
	@Override
	public String getURI() {
		return uri;
	}

	/**
	 * Calls for the specified HTTP response from the server.
	 * @param uri Percent-decoded URI without parameters, for example "/index.cgi"
	 * @param method "GET", "POST" etc.
	 * @param parms Parsed, percent decoded parameters from URI and, in case of POST, data.
	 * @param header Header entries, percent decoded
	 * @param files 
	 * @param httpd The HTTP server to serve the response
	 * @return An HTTP response
	 */
	@Override
	public Response serve(String uri, String method, Properties header,
			Properties parms, Properties files, ZmoteHTTPD httpd) {
		
			
			if(!file.exists() || !file.canRead() || !file.isFile())
				return httpd.new Response(httpd.HTTP_NOTFOUND, httpd.MIME_PLAINTEXT, "Not found");
		
				
		Response response = httpd.serveFile("/"+file.getName(), header, file.getParentFile(), false);
		response.isStreaming = false;

		return response;
	}
	
	/**
	 * Clones this handler and its file and returns the clone
	 * @return a clone of this handler with its own file
	 */
	public ZmoteHTTPDRequestHandler clone(){
		File newFile = file.getAbsoluteFile();
		MediaRequestHandler clone = new MediaRequestHandler(newFile, uri);
		return clone;
	}

}
