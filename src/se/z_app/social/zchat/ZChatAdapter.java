package se.z_app.social.zchat;

import se.z_app.social.Comment;
import se.z_app.social.Feed;
import se.z_app.social.Post;
import se.z_app.stb.Program;

public class ZChatAdapter {
	
	//TODO: Implement this with the ZChat server
	public Feed getFeed(Program program){
		return null;
	}
	
	//TODO: Implement this with ZChat server
	public Feed commitPost(Feed targetFeed, Post newPost){
		return targetFeed;
	}
	
	//TODO: Implement this with ZChat server
	public Feed commitComment(Feed targetFeed, Post targetPost, Comment newComment){
		return targetFeed;
	}
	
	
	
	
}
