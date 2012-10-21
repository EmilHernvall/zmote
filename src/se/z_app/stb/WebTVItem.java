package se.z_app.stb;

import android.graphics.Bitmap;

public class WebTVItem {

	private WebTVService webTVService;
	private String id;
	private String title;
	private String author;
	private String info;
	private int duration = -1;
	private Bitmap icon;
	private String iconURL;
	
	
	
	public WebTVService getWebTVService() {
		return webTVService;
	}
	public void setWebTVService(WebTVService webTVService) {
		this.webTVService = webTVService;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getAuthor() {
		return author;
	}
	public void setAuthor(String author) {
		this.author = author;
	}
	public String getInfo() {
		return info;
	}
	public void setInfo(String info) {
		this.info = info;
	}
	public int getDuration() {
		return duration;
	}
	public void setDuration(int duration) {
		this.duration = duration;
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
