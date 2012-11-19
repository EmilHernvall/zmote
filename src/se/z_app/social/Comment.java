package se.z_app.social;

import java.util.Date;

/**
 * Class that describes a comment made to a post
 * 
 * @author Rasmus Holm
 */
public class Comment implements PostInterface{
	
	private String userName;
	private String content;
	private Date dateOfCreation;
	private int id;
	private Post post;
	
	/**
	 * Constructor for Comment
	 * @param parentPost the post the comment is made to
	 */
	public Comment(Post parentPost){
		this.post = parentPost;
	}
	
	/**
	 * Getter for the comment's parent post
	 * @return the parent post of the comment
	 */
	public Post getParentPost(){
		return post;
	}
	
	/**
	 * Getter for userName
	 * @return the user name of the user that made the comment
	 */
	public String getUserName() {
		return userName;
	}
	
	/**
	 * Setter for userName
	 * @param userName the name to be set as userName of the comment
	 */
	public void setUserName(String userName) {
		this.userName = userName;
	}
	
	/**
	 * Getter for content
	 * @return the a String with the content of the comment
	 */
	public String getContent() {
		return content;
	}
	
	/**
	 * Setter for content
	 * @param content the content to be placed in the comment
	 */
	public void setContent(String content) {
		this.content = content;
	}
	
	/**
	 * Getter for dateOfCreation
	 * @return the Date on which the comment was created
	 */
	public Date getDateOfCreation() {
		return dateOfCreation;
	}
	
	/**
	 * Setter for dateOfCreation
	 * @param dateOfCreation the Date to be set as the comment's dateOfCreation
	 */
	public void setDateOfCreation(Date dateOfCreation) {
		this.dateOfCreation = dateOfCreation;
	}
	
	/**
	 * Getter for Id
	 * @return the ID number of the comment
	 */
	public int getId() {
		return id;
	}
	
	/**
	 * Setter for Id
	 * @param id the to-be Id for the comment
	 */
	public void setId(int id) {
		this.id = id;
	}
	
}
