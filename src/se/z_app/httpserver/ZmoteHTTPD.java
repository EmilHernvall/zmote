package se.z_app.httpserver;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Properties;



public class ZmoteHTTPD extends NanoHTTPD {

	private HashMap<String, ZmoteHTTPDRequestHandler> handlers = new HashMap<String, ZmoteHTTPDRequestHandler>();
	private File wwwroot;
	
	public ZmoteHTTPD(int port, File wwwroot) throws IOException {
		super(port, wwwroot);
		this.wwwroot = wwwroot;
	}
	
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
	
	public void addHandler(ZmoteHTTPDRequestHandler handler){
		synchronized(handlers){
			//Log.i("WebServer", "Adding handler: " + handler.getURI());
			handlers.put(handler.getURI(), handler);
		}
	}
	public  void removeHandler(ZmoteHTTPDRequestHandler handler){
		synchronized(handlers){
			handlers.remove(handler.getURI().trim());
		}
	}
	
	

}
