package se.z_app.httpserver;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.HashMap;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;


public class HTTPSessionHandler {
	private ThreadPoolExecutor threadPool = new ThreadPoolExecutor(3, 10, 10, TimeUnit.SECONDS, new LinkedBlockingQueue<Runnable>(40));
	
	private HashMap<String, HTTPRequestHandlerIntf> targetUris = new HashMap<String, HTTPRequestHandlerIntf>();
	
	public void addHandler(HTTPRequestHandlerIntf handler){
		targetUris.put(handler.getTargetUri(), handler);
	}
	
	public HTTPRequestHandlerIntf getHandler(String uri){
		HTTPRequestHandlerIntf handler = targetUris.get(uri);
		if(handler == null)
			return null;
		return (HTTPRequestHandlerIntf)handler.clone();
	}
	
	
	public void handle(Socket socket){
		threadPool.execute(new RequestSession(socket, this));
	}
	
	
	
	
	
	private class RequestSession implements Runnable{
		Socket socket;
		InputStream in;
		OutputStream out;
		HTTPSessionHandler session;
		HTTPRequest request;
		
		public RequestSession(Socket socket, HTTPSessionHandler session){
			this.socket = socket;
			this.session = session;

			
		}
		
		@Override
		public void run() {
			
			try {
				String rawRequest = "";
				int len;
				byte buffer[] = new byte[1024];
				in = this.socket.getInputStream();
				out = this.socket.getOutputStream();
				while((len= in.read(buffer)) != -1){
					rawRequest = rawRequest + new String(buffer,0, len);
				}
				
				try{
					request = new HTTPRequest(rawRequest);
				}catch (Exception e) {
					return;
				}
				
				
				
				
				HTTPRequestHandlerIntf handler = session.getHandler(request.getUri());
				
				if(handler == null){
					//TODO: Implement 404
					return;
				}
				
				HTTPResponse response = handler.handle(request);
				
				buffer = response.getHeader().getBytes();
				out.write(buffer);
				
				buffer = new byte[4096];
				
				in = response.getBody();
				if(in == null)
					return;
				while((len = in.read(buffer)) != -1){
					out.write(buffer, 0, len);
				}
				
				
				
			} catch (IOException e) {
				e.printStackTrace();
			}finally{
				
				try {
					in.close();
					out.close();
					socket.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
			}
			
		}
		
	}
}
