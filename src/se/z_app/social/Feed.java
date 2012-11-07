package se.z_app.social;

import java.util.Collection;
import java.util.Iterator;

import se.z_app.stb.Program;
import edu.emory.mathcs.backport.java.util.concurrent.ConcurrentSkipListMap;

public class Feed implements Iterable<Post>{
	private ConcurrentSkipListMap posts = new ConcurrentSkipListMap();
	private Program program;
	
	public Feed(Program program){
		this.program = program;
	}
	
	public Program getProgram() {
		return program;
	}
	
	public void addPost(Post post){
		post.setFeed(this);
		posts.put(post.getLastUpdate(), post);
	}
	public Post removePost(Post post){
		return (Post) posts.remove(post.getLastUpdate());
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public Iterator<Post> iterator() {
		return posts.values().iterator();
	}
	
	
	/**
	 * Returns all posts in a feed relating to a specific program.
	 * @return Post[] ordered by the lastUpdate of the post, from oldest to newest
	 */
	public Post[] getPosts(){
		Collection<Post> com = posts.values();
		Post[] tbr = new Post[com.size()];
		com.toArray(tbr);
		return tbr;
	}
	
	
	
	
}
