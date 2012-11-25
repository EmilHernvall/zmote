package se.z_app.stb;

/**
 * Class that represents a STB
 * 
 * Author Rasmus Holm
 */
public class STB {

	private STBEnum type;
	private String mac;
	private String ip;
	private String boxName;

	/**
	 * Getter for the STB's type, as specified by STBEnum
	 * @return The type of the STB
	 */
	public STBEnum getType() {
		return type;
	}

	/**
	 * Setter for the STB's type, as specified by STBEnum
	 * @param type - The STB's type, must be DEFAULT or ZENTERIO
	 */
	public void setType(STBEnum type) {
		this.type = type;
	}
	
	/**
	 * Defines the STB enum type with the values DEFAULT and ZENTERIO.
	 * @author Rasmus Holm
	 */
	public enum STBEnum {
		DEFAULT,ZENTERIO;	
	}

	/**
	 * Getter for the STB's MAC address
	 * @return The MAC address of the STB
	 */
	public String getMAC() {
		return mac;
	}
	
	/**
	 * Setter for the STB's MAC address
	 * @param mac - The MAC address of the STB
	 */
	public void setMAC(String mac) {
		this.mac = mac;
	}

	/**
	 * Getter for the STB's IP address
	 * @return The IP address of the STB
	 */
	public String getIP() {
		return ip;
	}

	/**
	 * Getter for the STB's name
	 * @return The name of the STB
	 */
	public String getBoxName() {
		return boxName;
	}

	/**
	 * Setter for the STB's name
	 * @param boxName - The new name of the STB
	 */
	public void setBoxName(String boxName) {
		this.boxName = boxName;
	}

	/**
	 * Makes a String with the STB's name
	 * @return The name of the STB
	 */
	public String toString() {
		 return boxName;
	}

	/**
	 * Setter for the STB's IP address
	 * @param ip - The IP address of the STB
	 */
	public void setIP(String ip) {
		this.ip = ip;
	}
}


