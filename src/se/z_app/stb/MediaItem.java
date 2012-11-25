package se.z_app.stb;

/**
 * Class that represents a media item
 * 
 * @author Rasmus Holm
 */
public class MediaItem {
	private String url;
	private String name;
	private String iconURL;
	
	/**
	 * Getter for the item's URL
	 * @return The URL to the media item
	 */
	public String getUrl() {
		return url;
	}
	
	/**
	 * Setter for the item's URL
	 * @param url - The URL to the media item
	 */
	public void setUrl(String url) {
		this.url = url;
	}
	
	/**
	 * Getter for the item's name
	 * @return The name of the media item
	 */
	public String getName() {
		return name;
	}
	
	/**
	 * Setter for the item's name
	 * @param name - The name of the media item
	 */
	public void setName(String name) {
		this.name = name;
	}
	
	/**
	 * Getter for the item's icon's name
	 * @return The URL to the media item's icon
	 */
	public String getIconURL() {
		return iconURL;
	}
	
	/**
	 * Setter for the item's icon's URL
	 * @param iconURL - The URL to the media item's icon
	 */
	public void setIconURL(String iconURL) {
		this.iconURL = iconURL;
	}
	
	 
}
