package se.z_app.stb;

import java.util.Iterator;
import edu.emory.mathcs.backport.java.util.concurrent.ConcurrentSkipListMap;



import android.graphics.Bitmap;

public class Channel implements Iterable<Program>{
	private String name;
	private Bitmap icon;
	private String iconUrl;
	private String url;
	private int nr = -1; 
	private int onid = -1;
	private int tsid = -1;
	private int sid = -1;
	private ConcurrentSkipListMap programsByDate = new ConcurrentSkipListMap();
	
	
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
//		programs.add(program);
		programsByDate.put(program.getStart(), program);
	}

	
	@SuppressWarnings("unchecked")
	@Override
	public Iterator<Program> iterator() {
		return programsByDate.values().iterator();
		//return programs.iterator();
	}

	

	
}

