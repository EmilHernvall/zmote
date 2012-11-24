package se.z_app.social.zchat;

import java.util.ArrayList;
import java.util.Date;


import se.z_app.social.Comment;
import se.z_app.social.Feed;
import se.z_app.social.Post;
import se.z_app.stb.Channel;
import se.z_app.stb.Program;

/**
 * Class to test the Zhat view before the normal adapter is implemented.
 * @author Linus
 *
 */
public class ZChatTestAdapter extends ZChatAdapter {

	private Feed testFeed;
	private ArrayList<Post> posts;
	private ArrayList<Comment> comments1;
	private ArrayList<Comment> comments2;

	public ZChatTestAdapter(){
		testFeed = new Feed(new Program(new Channel()));
		posts = new ArrayList<Post>();
		comments1 = new ArrayList<Comment>();
		comments2 = new ArrayList<Comment>();
		for(int i=1;i<=3;i ++){

			Post post = new Post();
			post.setUserName("Linus Clone nr: "+i);
			post.setContent("Hi, i am Post nr: "+i);
			post.setDateOfCreation(new Date());
			post.setLastUpdate(post.getDateOfCreation());
			testFeed.addPost(post);
			posts.add(post);
		}

		for(int i=1;i<=3;i++){

			Comment comment = new Comment(posts.get(1));
			comment.setContent("And i am comment nr: "+1+"Of post 2");
			comment.setUserName("Evil commenters nr: "+1);
			comment.setDateOfCreation(new Date());
			posts.get(1).addComment(comment);
			comments1.add(comment);

		}
		for(int i=1;i<=2;i++){
			try {
				Thread.sleep(10);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			Comment comment = new Comment(posts.get(2));
			comment.setContent("And i am comment nr: "+1+"Of post 3");
			comment.setUserName("Evil commenters nr: "+1);
			comment.setDateOfCreation(new Date());
			posts.get(2).addComment(comment);
			comments2.add(comment);

		}


	}
	/**
	 * Overrides the old get feed
	 */
	public Feed getFeed(Program program){
		return testFeed;

	}

	/**
	 * Overrides commitpost.
	 */
	public Feed commitPost(Feed feed, Post post){
		testFeed.addPost(post);
		return testFeed;
	}

	/**
	 * OVerrides CommitComment.
	 */
	public Feed commitComment(Feed feed,Post post,Comment comment){
		for(Post post2 : feed){
			if(post2.equals(post)){
				post.addComment(comment);
			}
		}
		return testFeed;
	}

}
