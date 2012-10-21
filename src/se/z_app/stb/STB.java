package se.z_app.stb;
/*
 * 
 */
public class STB {

	private STBEnum type;
	private String mac;
	private String ip;
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
		return mac;
	}

	public void setMAC(String mac) {
		this.mac = mac;
	}

	public String getIP() {
		return ip;
	}

	public String getBoxName() {
		return boxName;
	}

	public void setBoxName(String boxName) {
		this.boxName = boxName;
	}

	
	public String toString() {
		 return boxName;
	}

	public void setIP(String ip) {
		this.ip = ip;
	}
}


