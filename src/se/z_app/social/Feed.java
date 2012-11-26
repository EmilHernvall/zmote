package se.z_app.social;

import java.util.Collection;
import java.util.Date;
import java.util.Iterator;
import se.z_app.stb.Program;
import edu.emory.mathcs.backport.java.util.concurrent.ConcurrentSkipListMap;

/**
 * Class that describes a comment feed that is associated with a program
 * 
 * @author Rasmus Holm refractored Linus Back
 */
public class Feed implements Iterable<Post>{
	
	private ConcurrentSkipListMap posts = new ConcurrentSkipListMap();
	private Program program;
	private Date lastUpdated;
	
	/**
	 * Constructor for Feed
	 * @param program the program that the feed is associated with
	 */
	public Feed(Program program){
		this.program = program;
		lastUpdated = new Date(0);
	}
	
	/**
	 * Getter for the program that this feed is associated with
	 * @return the program this feed is associated with
	 */
	public Program getProgram() {
		return program;
	}
	
	/**
	 * Adds a post to this comment feed
	 * @param post the post to be added to the feed
	 */
	public void addPost(Post post){
		post.setFeed(this);
		posts.put(post.getLastUpdate(), post);
	}
	
	/**
	 * Removes a post from this comment feed
	 * @param post The post to be removed from the feed
	 * @return The post that was removed from the feed
	 */
	public Post removePost(Post post){
		return (Post) posts.remove(post.getLastUpdate());
	}
	
	/**
	 * Get an iterator for all posts made to this feed
	 * @return an iterator with all posts in this feed
	 */
	@SuppressWarnings("unchecked")
	@Override
	public Iterator<Post> iterator() {
		return posts.values().iterator();
	}
	
	/**
	 * Returns all posts in a feed relating to a specific program.
	 * @return Post[] ordered by the lastUpdate of the post, from oldest to newest
	 */
	@SuppressWarnings("unchecked")
	public Post[] getPosts(){
		Collection<Post> col = posts.values();
		Post[] tbr = new Post[col.size()];
		col.toArray(tbr);
		return tbr;
	}

	/**
	 * Gets the date when the feed was last updated.
	 * Added so that easy comparison between feeds should work.
	 * @return
	 */
	public Date getLastUpdated() {
		return lastUpdated;
	}

	/**
	 * Setter for the date when the feed was last updated.
	 * Added so that easy comparison between feeds should work.
	 * @param lastUpdated
	 */
	public void setLastUpdated(Date lastUpdated) {
		this.lastUpdated = lastUpdated;
	}
	
}
