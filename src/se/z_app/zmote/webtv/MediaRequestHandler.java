package se.z_app.zmote.webtv;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Properties;


import se.z_app.httpserver.NanoHTTPD.Response;
import se.z_app.httpserver.ZmoteHTTPD;
import se.z_app.httpserver.ZmoteHTTPDRequestHandler;

public class MediaRequestHandler implements ZmoteHTTPDRequestHandler{

	private String uri;
	private File file;
	
	public MediaRequestHandler(File file, String uri){
		this.uri = uri;
		this.file = file;
	}
	
	@Override
	public String getURI() {
		return uri;
	}

	@Override
	public Response serve(String uri, String method, Properties header,
			Properties parms, Properties files, ZmoteHTTPD httpd) {
		
		InputStream in = null;
		try {
			in = new FileInputStream(file);
		} catch (FileNotFoundException e) {
			return httpd.new Response(httpd.HTTP_NOTFOUND, httpd.MIME_PLAINTEXT, "Not found");
		}
				
		Response response = httpd.serveFile("/"+file.getName(), header, file.getParentFile(), false);
		response.isStreaming = false;

		
		return response;
	}
	
	public ZmoteHTTPDRequestHandler clone(){
		File newFile = file.getAbsoluteFile();
		MediaRequestHandler clone = new MediaRequestHandler(newFile, uri);
		return clone;
	}

}
