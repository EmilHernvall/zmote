package se.z_app.httpserver;

public interface HTTPRequestHandlerIntf extends Cloneable{
	public String getTargetUri(); 
	public HTTPResponse handle(HTTPRequest request);
	public Object clone();
}
