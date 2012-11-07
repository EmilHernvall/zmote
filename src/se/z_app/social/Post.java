package se.z_app.social;

import java.util.Collection;
import java.util.Date;
import java.util.Iterator;

import se.z_app.stb.Program;

import edu.emory.mathcs.backport.java.util.concurrent.ConcurrentSkipListMap;



public class Post implements Iterable<Comment>{
	private ConcurrentSkipListMap comments = new ConcurrentSkipListMap();
	private String userName;
	private String content;
	private Date dateOfCreation;
	private Date lastUpdate;
	private Feed feed;
	private int id;
	
	
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public Date getDateOfCreation() {
		return dateOfCreation;
	}
	public void setDateOfCreation(Date dateOfCreation) {
		this.dateOfCreation = dateOfCreation;
	}
	public Date getLastUpdate() {
		return lastUpdate;
	}
	public void setLastUpdate(Date lastUpdate) {
		this.lastUpdate = lastUpdate;
	}
	public Feed getFeed() {
		return feed;
	}
	public void setFeed(Feed feed) {
		this.feed = feed;
	}
	
	
	public void addComment(Comment comment){
		comments.put(comment.getDateOfCreation(), comment);
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public Iterator<Comment> iterator() {
		return comments.values().iterator();
	}
	
	@SuppressWarnings("unchecked")
	public Comment[] getComments(){
		Collection<Comment> com = comments.values();
		Comment[] tbr = new Comment[com.size()];
		com.toArray(tbr);
		return tbr;
	}
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	
	

}
