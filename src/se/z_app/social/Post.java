package se.z_app.social;

import java.util.Collection;
import java.util.Date;
import java.util.Iterator;
import edu.emory.mathcs.backport.java.util.concurrent.ConcurrentSkipListMap;

/**
 * Class that describes a post made to a feed
 * 
 * @author Rasmus Holm, refractored by Linus Back
 */
public class Post implements Iterable<Comment>, PostInterface{
	
	private ConcurrentSkipListMap comments = new ConcurrentSkipListMap();
	private String userName;
	private String content;
	private Date dateOfCreation;
	private Date lastUpdate;
	private Feed feed;
	private int id;

	
	/**
	 * Getter for userName
	 * @return the name of the user that made the post
	 */
	public String getUserName() {
		return userName;
	}
	
	/**
	 * Setter for userName
	 * @param userName the name to be set as userName for the post
	 */
	public void setUserName(String userName) {
		this.userName = userName;
	}
	
	/**
	 * Getter for content
	 * @return the a String with the content of the post
	 */
	public String getContent() {
		return content;
	}
	
	/**
	 * Setter for content
	 * @param content the content to be placed in the post
	 */
	public void setContent(String content) {
		this.content = content;
	}
	
	/**
	 * Getter for dateOfCreation
	 * @return the Date on which the post was created
	 */
	public Date getDateOfCreation() {
		return dateOfCreation;
	}
	
	/**
	 * Setter for dateOfCreation
	 * @param dateOfCreation the Date to be set as the post's dateOfCreation
	 */
	public void setDateOfCreation(Date dateOfCreation) {
		this.dateOfCreation = dateOfCreation;
	}
	
	/**
	 * Getter for lastUpdate
	 * @return the Date of the post's last update
	 */
	public Date getLastUpdate() {
		return lastUpdate;
	}
	
	/**
	 * Setter for lastUpdate
	 * @param lastUpdate the Date to be set as the lastUpdate for the post
	 */
	public void setLastUpdate(Date lastUpdate) {
		this.lastUpdate = lastUpdate;
	}
	
	/**
	 * Getter for feed
	 * @return the feed the post was made to
	 */
	public Feed getFeed() {
		return feed;
	}
	
	/**
	 * Setter for feed
	 * @param feed the feed the post is to be associated with
	 */
	public void setFeed(Feed feed) {
		this.feed = feed;
	}
	
	/**
	 * Adds a comment to a post
	 * @param comment the comment to be added
	 */
	public void addComment(Comment comment){
		comments.put(comment.getDateOfCreation(), comment);
	}
	
	/**
	 * Get all comments to a post as an iterator
	 * @return an iterator with all comments made to this post
	 */
	@SuppressWarnings("unchecked")
	@Override
	public Iterator<Comment> iterator() {
		return comments.values().iterator();
	}
	
	/**
	 * Get all comments to a post as an array
	 * @return an array with all comments to this post in chronological order
	 */
	@SuppressWarnings("unchecked")
	public Comment[] getComments(){
		Collection<Comment> com = comments.values();
		Comment[] tbr = new Comment[com.size()];
		com.toArray(tbr);
		return tbr;
	}
	/**
	 * Get all comments as a collection of an array.
	 * 
	 * @return a collection of comments.
	 */
	public Collection<Comment> getCommentsAsCollection(){
		return comments.values();
	}
	
	/**
	 * Getter for Id
	 * @return the ID number of the post
	 */
	public int getId() {
		return id;
	}
	
	/**
	 * Setter for Id
	 * @param id the to-be Id for the post
	 */
	public void setId(int id) {
		this.id = id;
	}

	/**
	 * Implemented to easier find out if value is a post or not.
	 */
	public boolean isPost() {
		
		return true;
	}
	
}
