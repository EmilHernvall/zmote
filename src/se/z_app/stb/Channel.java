package se.z_app.stb;

import java.util.Comparator;
import java.util.Iterator;
import java.util.concurrent.ConcurrentSkipListSet;

import android.graphics.Bitmap;

public class Channel implements Iterable<Program>, Comparable<Channel>, Comparator<Channel>{
	private String name;
	private Bitmap icon;
	private String iconUrl;
	private String url;
	private int nr; 
	private int onid;
	private int tsid;
	private int sid;
	private ConcurrentSkipListSet<Program> programs;
	
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
	public String getIconUrl() {
		return iconUrl;
	}
	public void setIconUrl(String iconUrl) {
		this.iconUrl = iconUrl;
	}
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	public int getNr() {
		return nr;
	}
	public void setNr(int nr) {
		this.nr = nr;
	}
	public int getOnid() {
		return onid;
	}
	public void setOnid(int onid) {
		this.onid = onid;
	}
	public int getTsid() {
		return tsid;
	}
	public void setTsid(int tsid) {
		this.tsid = tsid;
	}
	public int getSid() {
		return sid;
	}
	public void setSid(int sid) {
		this.sid = sid;
	}
	
	public void addProgram(Program program){
		programs.add(program);
	}
	
	@Override
	public Iterator<Program> iterator() {
		return programs.iterator();
	}
	@Override
	public int compareTo(Channel another) {
		return new Integer(getNr()).compareTo(new Integer(another.getNr()));
	}
	@Override
	public int compare(Channel lhs, Channel rhs) {
		return lhs.compareTo(rhs);
	}
	

	
}

