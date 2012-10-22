package se.z_app.httpserver;

import java.io.IOException;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;

import android.util.Log;

public class HTTPServer implements Runnable{
	
	private HTTPSessionHandler handler = new HTTPSessionHandler();
	private Thread thread;
	private int port = 8080;
	private int backlog = 10;
	private InetAddress localAddress;
	private boolean isRunning = true;
	private ServerSocket serverSocket;
	
	@Override
	public void run() {
		Log.i("WebServer", "Thread is Running");
		try {
			serverSocket = new ServerSocket(port, backlog, localAddress);
			Log.i("WebServer", "Listening");
			while(true){
				Socket socket = serverSocket.accept();
				Log.i("WebServer", "Accepted Socket..");
				handler.handle(socket);
			}
		} catch (IOException e) {
			Log.i("WebServer", "Thread Failed: " + e.toString() );
			e.printStackTrace();
			
		}
		
	}
	
	public int getPort() {
		return port;
	}

	public void setPort(int port) {
		this.port = port;
	}

	public int getBacklog() {
		return backlog;
	}

	public void setBacklog(int backlog) {
		this.backlog = backlog;
	}


	public void setLocalAddress(InetAddress localAddress) {
		this.localAddress = localAddress;
	}

	public HTTPServer(){
	}	
	public void start(){
		Log.i("WebServer", "Starting Thread");
		thread = new Thread(this);
		thread.start();
	}
	public void stop(){
		isRunning = false;
		try {
			serverSocket.close();
		} catch (IOException e) {
			thread.destroy();
		}
		
	}
	public void restart(){
		stop();
		start();
	}

	public HTTPSessionHandler getHandler() {
		return handler;
	}

	public void setHandler(HTTPSessionHandler handler) {
		this.handler = handler;
	}

	
}
