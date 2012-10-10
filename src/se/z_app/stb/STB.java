package se.z_app.stb;

import se.z_app.stb.api.RemoteControl;

public class STB {

	private STBEnum type;
	private String MAC;
	private String IP;
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
		return IP;
	}

	public void setIP(String iP) {
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
}


