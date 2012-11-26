package se.z_app.social.zchat;


import java.io.InputStream;

import java.net.URL;
import java.net.URLEncoder;
import java.util.Date;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import se.z_app.social.Comment;
import se.z_app.social.Feed;
import se.z_app.social.Post;
import se.z_app.stb.Program;
import android.util.Log;

/**
 * Class that communicates with the ZChat server
 * @author Marcus Widegren refractored Linus Back
 *
 */
public class ZChatAdapter{
	private String serverAdress = "rails.z-app.se"; 

	/**
	 * Get a feed containing posts about a specific program
	 * @param program - the program the posts are about
	 * @return the feed with the posts
	 */
	@SuppressWarnings("deprecation")
	public Feed getFeed(Program program){
		Feed theFeed = new Feed(program);
		String arg1 = "get_post_by_program";
		arg1 = URLEncoder.encode(arg1);
		String arg2 = program.getName();
		arg2 = URLEncoder.encode(arg2);
		String channelName = URLEncoder.encode(program.getChannel().getName());

		/* Send the date as one string per parameter to simplify the conversion between
		 * java date and rails DateTime */
		String year = "" + program.getStart().getYear();
		String month = "" + (program.getStart().getMonth() + 1);
		String day = "" + program.getStart().getDate();
		String hours = "" + program.getStart().getHours();
		String minutes = "" + program.getStart().getMinutes();

		/* The string to the URL to get the posts */
		String urlStr = "http://" + serverAdress +"/post/"+arg1+
				"?program_name="+arg2+
				"&channel_name="+channelName+
				"&year="+year+
				"&month="+month+
				"&day="+day+
				"&hours="+hours+
				"&minutes="+minutes+
				"";

		/* Get the JSON string from the URL */
		String json = getJSON(urlStr, 4 * 4096);

		/* Try-catch block needed to get JSON objects. The app keeps running in case of a JSON error,
		 * 	it usually just means there are no posts in a feed or no comments in a post */
		try {
			JSONArray jsonarray = new JSONArray(json);

			for(int i = 0; i< jsonarray.length(); i++){
				JSONObject jsonPost = jsonarray.getJSONObject(i);

				Post thePost = new Post();
				thePost.setFeed(theFeed);
				thePost.setContent(jsonPost.getString("content"));
				thePost.setUserName(jsonPost.getString("username"));
				/* Get the name of the user who commited the post *//*
				String usernameID = URLEncoder.encode(jsonPost.getString("user_id"));
				String userURLString = "http://" + serverAdress +"/post/get_user_by_id?id="+usernameID;
				String jsonUser = getJSON(userURLString, 4096);
				JSONArray theUser = new JSONArray(jsonUser);
				thePost.setUserName(theUser.getJSONObject(0).getString("name"));

				/* Get the date of creation and update */
				String dateOfCreation = jsonPost.getString("created_at");
				String lastUpdate = jsonPost.getString("updated_at");
				Date creationDate = railsStringToDate(dateOfCreation);
				Date lastUpdateDate = railsStringToDate(lastUpdate);
				thePost.setDateOfCreation(creationDate);
				thePost.setLastUpdate(lastUpdateDate);

				/*Checks if the post was updated later then the feed itself
				 *
				 */
				if(theFeed.getLastUpdated().before(creationDate)){
					theFeed.setLastUpdated(creationDate);
				}


				/* Get the id (for comments) */
				int postID = jsonPost.getInt("id");
				thePost.setId(postID);


				/* URL to get the comments for the post */
				String getCommentStr = "http://" + serverAdress +"/post/get_comments_by_postid"+
						"?post_id=" + postID +
						"";



				/* Get the comments for the post */
				try{
					String getCommentJSONString = getJSON(getCommentStr, 4*4096);


					JSONArray commentJSONArray = new JSONArray(getCommentJSONString);
					for(int j = 0; j < commentJSONArray.length(); j ++){
						JSONObject jsonComment = commentJSONArray.getJSONObject(j);
						Comment newComment = new Comment(thePost);
						newComment.setId(jsonComment.getInt("id"));
						newComment.setContent(jsonComment.getString("content"));
						newComment.setUserName(jsonComment.getString("username"));

						String commentDateCreation = jsonComment.getString("created_at");
						Date commentDate = railsStringToDate(commentDateCreation);

						newComment.setDateOfCreation(commentDate);
						thePost.addComment(newComment);

						/*Checks if the post was updated later then the feed itself
						 *
						 */
						if(theFeed.getLastUpdated().before(commentDate)){
							theFeed.setLastUpdated(commentDate);
						}

					}
				}
				catch (JSONException e) {
					Log.i("GetComments", "ZChat adapter: JSON Failure: " + e.toString());

				}
				theFeed.addPost(thePost);


			}

		} catch (JSONException e) {
			Log.i("GetPosts", "ZChat adapter: JSON Failure: " + e.toString());

			return null;
		}
		return theFeed;
	}

	/**
	 * Function that commits a post to the server and returns the feed with the post added. Make sure the Post has a lastUpdateDate.
	 * @param targetFeed
	 * @param newPost
	 * @return targetFeed
	 */
	@SuppressWarnings("deprecation")
	public Feed commitPost(Feed targetFeed, Post newPost){

		/* The arguments for the post */
		String username = URLEncoder.encode(newPost.getUserName());

		String programName = URLEncoder.encode(targetFeed.getProgram().getName());
		String postContent = URLEncoder.encode(newPost.getContent());
		String channelName = URLEncoder.encode(targetFeed.getProgram().getChannel().getName());

		/* Send the date as one string per parameter to simplify the conversion between
		 * java date and rails DateTime */
		String year = "" + targetFeed.getProgram().getStart().getYear();
		String month = "" + (targetFeed.getProgram().getStart().getMonth() + 1);
		String day = "" + targetFeed.getProgram().getStart().getDate();
		String hours = "" + targetFeed.getProgram().getStart().getHours();
		String minutes = "" + targetFeed.getProgram().getStart().getMinutes();

		/* The string to be visited to commit the post */
		String userURLString = "http://" + serverAdress +
				"/post/insert_post?" +
				"username=" + username+"" +
				"&program_name=" + programName +
				"&channel_name=" + channelName +
				"&content=" + postContent +
				"&year=" + year +
				"&month=" + month +
				"&day=" + day +
				"&hours=" + hours +
				"&minutes=" + minutes +
				"";
		//Log.e("ZCHAT", "userURLString" + userURLString);

		/* Get the JSON object when saving the post so we can set the ID of the post */
		String postJSONString = getJSON(userURLString, 4096);
		try {
			JSONObject theReturnedPost = new JSONObject(postJSONString);
			int theId = theReturnedPost.getInt("id");
			newPost.setId(theId);

			newPost.setDateOfCreation(railsStringToDate(theReturnedPost.getString("created_at")));
			newPost.setLastUpdate(railsStringToDate(theReturnedPost.getString("updated_at")));
		} catch (JSONException e) {
			e.printStackTrace();
		} 

		/* Try-catch block needed to visit the URL *//*
		try {
			URL url = new URL(userURLString);
			url.openStream().close();
		} 
		catch (MalformedURLException e) {
			e.printStackTrace();
		} 
		catch (IOException e) {
			e.printStackTrace();
		}

		/*Checks if the post was updated later then the feed itself
		 *
		 */
		
		targetFeed.setLastUpdated(newPost.getDateOfCreation());
		

		/* Return the feed with the post commited */
		targetFeed.addPost(newPost);
		return targetFeed;
	}


	/**
	 * Function that commits a comment to the rails server
	 * @param targetFeed - the feed that contains the post
	 * @param targetPost - the post that is commented
	 * @param newComment - the comment to be commited
	 * @return the target feed with the comment commited
	 */
	@SuppressWarnings("deprecation")
	public Feed commitComment(Feed targetFeed, Post targetPost, Comment newComment){

		String commentContent = URLEncoder.encode(newComment.getContent());
		String postId = ""+newComment.getParentPost().getId();

		/* The URL to be visited */
		String urlString = "http://rails.z-app.se/post/insert_comment?"+
				"post_id="+postId+
				"&content="+commentContent+
				"&username="+newComment.getUserName()+
				"";

		/* Get the JSON object when saving the comment so we can set the dates and id correctly */
		String postJSONString = getJSON(urlString, 4096);
		try {
			JSONObject theReturnedPost = new JSONObject(postJSONString);
			int theId = theReturnedPost.getInt("id");
			newComment.setId(theId);

			newComment.setDateOfCreation(railsStringToDate(theReturnedPost.getString("created_at")));
		} 
		catch (JSONException e) {
			e.printStackTrace();
		} 
		/*
		 * Updates the feed to be set as modified.
		 */
		targetFeed.setLastUpdated(newComment.getDateOfCreation());
		
		
		targetPost.addComment(newComment);
		return targetFeed;
	}

	/**
	 * Function that converts a rails DateTime string to a Java Date
	 * @param dateString - the string to be converted
	 * @return A Java Date
	 */
	@SuppressWarnings("deprecation")
	private Date railsStringToDate(String dateString) {
		/* Replace all signs with "-" */
		dateString = dateString.replace(" ", "-");
		dateString = dateString.replace(":", "-");
		dateString = dateString.replace("T", "-");
		String splitted[] = dateString.split("\\-");

		/* Create a new Date and return it */
		return new Date(
				Integer.parseInt(splitted[0])-1900,
				Integer.parseInt(splitted[1])-1,
				Integer.parseInt(splitted[2]),
				Integer.parseInt(splitted[3]),
				Integer.parseInt(splitted[4]),
				Integer.parseInt(splitted[5].substring(0,  2))
				);
	}

	/**
	 * Function that gets a JSON String from a given URL
	 * @param urlStr - the URL to be visited
	 * @param bufferSize - the buffer size for each line
	 * @return the JSON string
	 */
	private String getJSON(String urlStr, int bufferSize){
		/* StringBuilder is a non-threadsafe (so more efficient) version of StringBuffer */
		StringBuilder json = new StringBuilder();	
		try {
			URL url = new URL(urlStr);
			InputStream in = url.openStream();


			byte buffer[] = new byte[bufferSize];
			int len;
			while ((len = in.read(buffer)) != -1) {
				json = json.append(new String(buffer, 0, len));
			}

			in.close();
		}catch (Exception e) {
			e.printStackTrace();
		}
		return json.toString();
	}


}
