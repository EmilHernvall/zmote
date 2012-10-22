package se.z_app.httpserver;

public class HTTPRequest {
	public enum Method{
		GET,POST,NONE
	}
	Method method = Method.NONE;
	String uri;
	String host;
	
	String rawHeader;
	String rawArguments;
	
	public HTTPRequest(String request) throws RuntimeException{
		
		String parts[] = request.split("\r\n\r\n", 1);
		
		if(parts.length > 1){
			rawHeader = parts[0];
			rawArguments = parts[1];
		}else{
			rawHeader = request;
		}
		parseHeader();
	}
		
	public Method getMethod() {
		return method;
	}

	public String getUri() {
		return uri;
	}

	public String getRawArguments() {
		return rawArguments;
	}

	private void parseHeader(){
		String arguments[] = rawHeader.split("\r\n");
		
		for (String line : arguments) {
			if(line.startsWith("GET ")){
				method = Method.GET;
				uri = line.split("\\ ")[1];
				
				String parts[] = uri.split("\\?", 1);
				if(parts.length > 1){
					rawArguments = parts[1];
					uri = parts[0];
				}
			}else if(line.startsWith("POST ")){
				method = Method.POST;
				uri = line.split("\\ ")[1];
			}
			
		}
		
	}
}
