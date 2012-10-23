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
			handler = handlers.get(uri.trim()).clone();
		}
		if(handler == null)
			return serveFile( uri, header, wwwroot, true );
		return handler.serve(method, header, parms, files);
	}
	
	public void addHandler(ZmoteHTTPDRequestHandler handler){
		synchronized(handlers){
			handlers.put(handler.getURI().trim(), handler);
		}
	}
	public  void removeHandler(ZmoteHTTPDRequestHandler handler){
		synchronized(handlers){
			handlers.remove(handler.getURI().trim());
		}
	}
	
	

}
