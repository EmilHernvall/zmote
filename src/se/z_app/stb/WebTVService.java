package se.z_app.stb;

import android.graphics.Bitmap;

public class WebTVService {
	private String id;
	private String name;
	private Bitmap icon;
	private String iconURL;
	
	
	public String getID() {
		return id;
	}
	public void setID(String id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public Bitmap getIcon() {
		return icon;
	}
	public void setIcon(Bitmap icon) {
		this.icon = icon;
	}
	public String getIconURL() {
		return iconURL;
	}
	public void setIconURL(String iconURL) {
		this.iconURL = iconURL;
	}
}
