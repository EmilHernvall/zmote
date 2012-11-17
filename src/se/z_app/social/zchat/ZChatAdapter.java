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
		String arg3 = URLEncoder.encode(""+program.getDuration());
		
		
		//HttpGet httpGet = new GetHTTPResponse().getJSON("http://" + serverAdress +"post/"+arg1+"?program_name="+arg2);
		String urlStr = "http://" + serverAdress +"post/"+arg1+"?program_name="+arg2;
		String json = getJSON(urlStr, 4096);
		
		// Code copied from "StandardCommand"
		
		try {
			JSONArray jsonarray = new JSONArray(json);
			
			for(int i = 0; i< jsonarray.length(); i++){
				JSONObject jsonPost = jsonarray.getJSONObject(i);
				
				Post thePost = new Post();
				thePost.setFeed(theFeed);
				theFeed.addPost(thePost);
				
				thePost.setContent(jsonPost.getString("content"));
				
				String usernameID = URLEncoder.encode(jsonPost.getString("user_id"));
				Log.e("ZCHAT", usernameID);
				String userURLString = "http://" + serverAdress +"post/get_user_by_id?id="+usernameID;
				String jsonUser = getJSON(userURLString, 4096);
				JSONArray theUser = new JSONArray(jsonUser);
				thePost.setUserName(theUser.getJSONObject(0).getString("name"));
				
				String dateOfCreation = jsonPost.getString("created_at");
				dateOfCreation = dateOfCreation.replace(" ", "-");
				dateOfCreation = dateOfCreation.replace(":", "-");
				String startAr[] = dateOfCreation.split("\\-");
			
				Date date = new Date(
						Integer.parseInt(startAr[0])-1900,
						Integer.parseInt(startAr[1])-1,
						Integer.parseInt(startAr[2]),
						Integer.parseInt(startAr[3]),
						Integer.parseInt(startAr[4]),
						Integer.parseInt(startAr[5])
						);
				thePost.setDateOfCreation(date);
				
				/*thePost.setName(jsonPost.getString("name"));
				thePost.setNr(jsonPost.getInt("nr"));
				thePost.setOnid(jsonPost.getInt("onid"));
				thePost.setTsid(jsonPost.getInt("tsid"));
				thePost.setSid(jsonPost.getInt("sid"));
				thePost.setUrl(jsonPost.getString("url"));
				*/
				
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
		
		String userURLString = "http://" + serverAdress +"post/insert_post?username="+arg1+"&program_name="+arg2+"&content="+arg3;
		
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
