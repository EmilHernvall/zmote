package se.z_app.social.zchat;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
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
 * @author Marcus Widegren
 *
 */
public class ZChatAdapter {
	private String serverAdress = "rails.z-app.se"; 
	
	//TODO: Implement this with the ZChat server
	@SuppressWarnings("deprecation")
	public Feed getFeed(Program program){
		Feed theFeed = new Feed(program);
		String arg1 = "get_post_by_program";
		arg1 = URLEncoder.encode(arg1);
		String arg2 = program.getName();
		arg2 = URLEncoder.encode(arg2);
		String channelName = URLEncoder.encode(program.getChannel().getName());
		
		//String arg3 = URLEncoder.encode(program.getStart().toString());
		String year = "" + program.getStart().getYear();
		String month = "" + (program.getStart().getMonth() + 1);
		String day = "" + program.getStart().getDate();
		String hours = "" + program.getStart().getHours();
		String minutes = "" + program.getStart().getMinutes();
		//Log.e("ZCHAT:", "The Date: " + program.getStart().getMonth());
		//HttpGet httpGet = new GetHTTPResponse().getJSON("http://" + serverAdress +"post/"+arg1+"?program_name="+arg2);
		String urlStr = "http://" + serverAdress +"/post/"+arg1+
						"?program_name="+arg2+
						"&channel_name="+channelName+
						"&year="+year+
						"&month="+month+
						"&day="+day+
						"&hours="+hours+
						"&minutes="+minutes+
						"";
		
		String json = getJSON(urlStr, 4 * 4096);
		Log.e("ZCHAT", "urlStr: " + urlStr);
		// Code copied from "StandardCommand"

		try {
			JSONArray jsonarray = new JSONArray(json);
			
			for(int i = 0; i< jsonarray.length(); i++){
				JSONObject jsonPost = jsonarray.getJSONObject(i);
				
				Post thePost = new Post();
				thePost.setFeed(theFeed);
				
				
				thePost.setContent(jsonPost.getString("content"));
				
				String usernameID = URLEncoder.encode(jsonPost.getString("user_id"));
				//Log.e("ZCHAT", usernameID);
				String userURLString = "http://" + serverAdress +"/post/get_user_by_id?id="+usernameID;
				String jsonUser = getJSON(userURLString, 4096);
				JSONArray theUser = new JSONArray(jsonUser);
				thePost.setUserName(theUser.getJSONObject(0).getString("name"));
				
				String dateOfCreation = jsonPost.getString("created_at");
				dateOfCreation = dateOfCreation.replace(" ", "-");
				dateOfCreation = dateOfCreation.replace(":", "-");
				dateOfCreation = dateOfCreation.replace("T", "-");
				String startAr[] = dateOfCreation.split("\\-");
				
				/*
				String lastUpdate = jsonPost.getString("updated_at");
				lastUpdate = lastUpdate.replace(" ", "-");
				lastUpdate = lastUpdate.replace(":", "-");
				lastUpdate = lastUpdate.replace("T", "-");
				String lastUpdateSplit[] = lastUpdate.split("\\-");
				
				Date lastUpdateDate = new Date(
						Integer.parseInt(lastUpdateSplit[0])-1900,
						Integer.parseInt(lastUpdateSplit[1])-1,
						Integer.parseInt(lastUpdateSplit[2]),
						Integer.parseInt(lastUpdateSplit[3]),
						Integer.parseInt(lastUpdateSplit[4]),
						Integer.parseInt(lastUpdateSplit[5].substring(0,  2))
						);
				
				
						*/
				Date date = new Date(
						Integer.parseInt(startAr[0])-1900,
						Integer.parseInt(startAr[1])-1,
						Integer.parseInt(startAr[2]),
						Integer.parseInt(startAr[3]),
						Integer.parseInt(startAr[4]),
						Integer.parseInt(startAr[5].substring(0,  2))
						);
				thePost.setDateOfCreation(date);
				thePost.setLastUpdate(date);
				int postID = jsonPost.getInt("id");
				thePost.setId(postID);
				theFeed.addPost(thePost);
				
			}
			
		} catch (JSONException e) {
			Log.i("GetPosts", "ZChat adapter: JSON Failure: " + e.toString());
			
			return null;
		}
		return theFeed;
	}
	
	//TODO: Implement this with ZChat server
	@SuppressWarnings("deprecation")
	public Feed commitPost(Feed targetFeed, Post newPost){
		targetFeed.addPost(newPost);
		String arg1 = URLEncoder.encode(newPost.getUserName());
		String arg2 = URLEncoder.encode(targetFeed.getProgram().getName());
		String arg3 = URLEncoder.encode(newPost.getContent());
		//Date theDate = targetFeed.getProgram().getStart();
		/*
		String year = ""+theDate.getYear();
		
		String month = "" + (theDate.getMonth() > 9 ? "0" + theDate.getMonth() : theDate.getMonth());
		String day = "" + (theDate.getDay() > 9 ? "0" + theDate.getDay() : theDate.getDay());
		String hour = "" + (theDate.getHours() > 9 ? "0" + theDate.getHours() : theDate.getHours());
		String minutes = "" + (theDate.getMinutes() > 9 ? "0" + theDate.getMinutes() : theDate.getMinutes());
		*/
		String year = "" + targetFeed.getProgram().getStart().getYear();
		String month = "" + (targetFeed.getProgram().getStart().getMonth() + 1);
		String day = "" + targetFeed.getProgram().getStart().getDate();
		String hours = "" + targetFeed.getProgram().getStart().getHours();
		String minutes = "" + targetFeed.getProgram().getStart().getMinutes();
		//String dateString = URLEncoder.encode(theDate.toString());
		
		String userURLString = "http://" + serverAdress +
								"/post/insert_post?" +
								"username="+arg1+"" +
								"&program_name="+arg2+
								"&content="+arg3+
								//"&starttime="+dateString+
								"&year="+year+
								"&month="+month+
								"&day="+day+
								"&hours="+hours+
								"&minutes="+minutes+
								"";
		Log.e("ZCHAT", "userURLString" + userURLString);
		try {
			URL url = new URL(userURLString);
			url.openStream().close();
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		//Log.e("ZCHAT", "urlStr: " + userURLString);
		return targetFeed;
	}
	
	//TODO: Implement this with ZChat server
	public Feed commitComment(Feed targetFeed, Post targetPost, Comment newComment){
		return targetFeed;
	}
	
	private String getJSON(String urlStr, int bufferSize){
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
			// TODO: handle exception
		}
		return json.toString();
	}
	
	
}
