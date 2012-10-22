package se.z_app.httpserver;

import java.io.InputStream;

public class HTTPResponse {
	
	public String getHeader(){
		return "HTTP/1.0 404 Not Found\r\n\r\n";
	}
	public InputStream getBody(){
		return null;
	}
}
