package se.z_app.httpserver;

import java.io.IOException;
import java.net.Inet4Address;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;

public class HTTPServer implements Runnable{
	
	private HTTPSessionHandler handler = new HTTPSessionHandler();
	private Thread thread;
	private int port = 80;
	private int backlog = 10;
	private Inet4Address localAddress;
	private boolean isRunning = true;
	private ServerSocket serverSocket;
	
	@Override
	public void run() {
		try {
			serverSocket = new ServerSocket(port, backlog, localAddress);
			while(true){
				Socket socket = serverSocket.accept();
				handler.handle(socket);
			}
		} catch (IOException e) {
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

	public Inet4Address getLocalAddress() {
		return localAddress;
	}

	public void setLocalAddress(Inet4Address localAddress) {
		this.localAddress = localAddress;
	}

	public HTTPServer(){
	}
	public void start(){
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
