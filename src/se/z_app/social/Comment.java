package se.z_app.social;

import java.util.Date;

public class Comment {
	private String userName;
	private String content;
	private Date dateOfCreation;
	private int id;
	private Post post;
	
	public Comment(Post parrentPost){
		this.post = parrentPost;
	}
	
	public Post getPatentPost(){
		return post;
	}
	
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
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	
}
