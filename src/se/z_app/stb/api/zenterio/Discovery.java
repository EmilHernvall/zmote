package se.z_app.stb.api.zenterio;

import java.net.InetAddress;
import java.net.UnknownHostException;

import se.z_app.stb.STB;
import se.z_app.stb.api.DiscoveryInterface;

public class Discovery implements DiscoveryInterface {

	String findSubnet() {
	    InetAddress addr = null;
		try {
			addr = InetAddress.getLocalHost();
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	    byte[] ipAddr = addr.getAddress();
	    StringBuffer str = new StringBuffer();
	    String returnString;
	    for (int i=0;i < ipAddr.length - 1; i++) {
	    	int result = ipAddr[i];
	    	if (ipAddr[i] < 0) {
	    		result += 256;
	    	}
	    	str.append(Integer.toString(result)+".");
	    }
	    returnString = str.toString();
		return returnString;
	}
	
	@Override
	public STB[] find() {
		// TODO Auto-generated method stub
//		try {
			for (int i = 0; i < 256; i++) {
				StringBuffer urlString = new StringBuffer();
				urlString.append("http://"+findSubnet());
				urlString.append(Integer.toString(i));
				urlString.append("/cgi-bin/zids_discovery");
			}
//		catch (RuntimeException) {
//			continue;
//		}
		return null;
	}

	@Override
	public STB[] find(STB stb) {
		// TODO Auto-generated method stub
		return null;
	}

}
