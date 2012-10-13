package se.z_app.stb;

import java.net.InetAddress;

/*
 * 
 */
public class STB {

	private STBEnum type;
	private String MAC;
	private InetAddress IP;
	private String boxName;

	public STBEnum getType() {

		
		return type;
	}

	public void setType(STBEnum type) {
		this.type = type;
	}
	
	public enum STBEnum {
		DEFAULT,ZENTERIO;	
	}

	public String getMAC() {
		return MAC;
	}

	public void setMAC(String mAC) {
		MAC = mAC;
	}

	public String getIP() {
		return IP.getHostAddress().toString();
	}

	/*
	 * The IP can be set by iP.getByName("ip-address as string")
	 */
	public void setIP(InetAddress iP) {
		IP = iP;
	}

	public String getBoxName() {
		return boxName;
	}

	public void setBoxName(String boxName) {
		this.boxName = boxName;
	}

	private void updateSTB() {
		// TODO implementation
	}
	
	public String toString() {
		 return boxName;
	}
}


