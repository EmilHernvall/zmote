package se.z_app.stb.api.zenterio;


import java.io.IOException;
import java.io.InputStream;

import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;

import java.util.Date;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;
import se.z_app.stb.Channel;
import se.z_app.stb.EPG;
import se.z_app.stb.Program;
import se.z_app.stb.WebTVItem;
import se.z_app.stb.WebTVService;
import se.z_app.stb.api.BiDirectionalCmdInterface;

public class StandardCommand implements BiDirectionalCmdInterface{
	private String ip;

	public StandardCommand(String ip){
		this.ip = ip.trim();
	}
	
	public EPG getEPG() {
		EPG epg = new EPG();
		long time = System.currentTimeMillis();
		String jsonString = new GetHTTPResponse().getJSON("http://" + ip + "/mdio/epg", 32768);
		//Log.i("ZmoteTestLog", "Request: " + "http://" + ip + "/mdio/epg" );
		//Log.i("SearchTest", "JsonLength: " + jsonString.length());
		//Log.i("ZmoteTestLog", "Featching raw EPG: " + (System.currentTimeMillis() - time) + "ms");
		
		try {
			JSONArray jsonarray = new JSONArray(jsonString);
			
			for(int i = 0; i< jsonarray.length(); i++){
				JSONObject jsonChannel = jsonarray.getJSONObject(i);
				Channel channel = new Channel();
				channel.setName(jsonChannel.getString("name"));
				channel.setNr(jsonChannel.getInt("nr"));
				channel.setOnid(jsonChannel.getInt("onid"));
				channel.setTsid(jsonChannel.getInt("tsid"));
				channel.setSid(jsonChannel.getInt("sid"));
				channel.setUrl(jsonChannel.getString("url"));
				
				epg.addChannel(channel);
				
				JSONArray jsonPrograms = jsonChannel.getJSONArray("programs");
				for(int j = 0; j < jsonPrograms.length(); j++){
					JSONObject jsonProgram = jsonPrograms.getJSONObject(j);
					Program program = new Program(channel);
					program.setName(jsonProgram.getString("name"));
					program.setShortText(jsonProgram.getString("shorttext"));
					program.setLongText(jsonProgram.getString("exttext"));
					program.setEventID(jsonProgram.getInt("eventId"));
					
					
					String start = jsonProgram.getString("start");
					start = start.replace(" ", "-");
					start = start.replace(":", "-");
					String startAr[] = start.split("\\-");
					
				
					@SuppressWarnings("deprecation")
					Date date = new Date(
							Integer.parseInt(startAr[0])-1900,
							Integer.parseInt(startAr[1])-1,
							Integer.parseInt(startAr[2]),
							Integer.parseInt(startAr[3]),
							Integer.parseInt(startAr[4]),
							Integer.parseInt(startAr[5])
							);
					program.setStart(date);
				 
					
					String duration = jsonProgram.getString("duration");
					String durationAr[] = duration.split("\\.");
					if(durationAr.length > 2){
						int durationTime = Integer.parseInt(durationAr[0])*3600;
						durationTime += Integer.parseInt(durationAr[1])*60;
						durationTime += Integer.parseInt(durationAr[2]);
						program.setDuration(durationTime);
					}					
					channel.addProgram(program);
				}
				
			}
			
		} catch (JSONException e) {
			Log.i("SearchTest", "Standard Command: JSON Faliur: " + e.toString());
			
			return null;
		}
		
		
		return epg;
	}

	
	public Channel getCurrentChannel() {
		Channel channel = new Channel();
		
		String jsonString = new GetHTTPResponse().getJSON("http://" + ip + "/mdio/currentchannel");
		try {
			
//Bug here is listing currentChannel as a WEB tv item
	// ex. {"label": "Swedish House Mafia @ Madison Square Garden 16-12-2011 [FULL SET]","url": "webtv:youtube:media:cCDcvZr9BNc","type": "","sourceUrl": ""}
	// ex. {"label": "BBC Three","url": "http://download.ted.com/talks/TerryMoore_2012-480p.mp4?apikey=TEDDOWNLOAD&contentViewer=broadcast&onid=1&tsid=1&sid=1004&nid=1&clid=0","type": "","sourceUrl": ""}
			//System.out.println("JsonString ---->" + jsonString);
			JSONObject json = new JSONObject(jsonString);
			
			channel.setName(json.getString("label"));
			channel.setUrl(json.getString("url"));
			
			
			
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return channel;
	}

	
	public Bitmap getChannelIcon(Channel channel) {
		
		String url = "http://" + ip +"/mdio/channelicon?onid="+ channel.getOnid() +
			"&tsid=" + channel.getTsid() + "&sid=" + channel.getSid(); 
		
		return new GetHTTPResponse().getImage(url);
	}

	
	public WebTVService[] getWebTVServices() {
		String jsonString = new GetHTTPResponse().getJSON("http://" + ip + "/mdio/webtv/services");
		WebTVService webservices[] = null;
		try {
			JSONArray services = new JSONArray(jsonString);
			int len = services.length();
			webservices = new WebTVService[len];
			for(int i = 0; i < len; i++ ){
				JSONObject service = services.getJSONObject(i);
				webservices[i] = new WebTVService();
				webservices[i].setID(service.getString("id"));
				webservices[i].setName(service.getString("name"));
				webservices[i].setIconURL(service.getString("iconURL"));
			}
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return webservices;
	}

	
	public Bitmap getWebTVServiceIcon(WebTVService serivce) {
		String url = "http://" + ip +"/mdio/webtv/icon?locator=" + serivce.getIconURL();			
			return new GetHTTPResponse().getImage(url);
	}

	
	public Bitmap getWebTVItemIcon(WebTVItem item) {
		String url = "http://" + ip +"/mdio/webtv/icon?locator=" + item.getIconURL();			
		return new GetHTTPResponse().getImage(url);
	}
	/** 
	 * Serach for WebTv. Input query is the search string and service is the 
	 * WebTVServis i.e youtube or spotify. 
	 */
	public WebTVItem[] searchWebTVService(String query, WebTVService service) {
		@SuppressWarnings("deprecation")
		String jsonString = new GetHTTPResponse().getJSON(String.format("http://" + ip + "/mdio/webtv/search?service=" + 
		service.getID() + "&q=%s", URLEncoder.encode(query)));
		WebTVItem webResults[] = null;
		
		try {
			JSONObject Jobject = new JSONObject(jsonString);
			
			JSONArray results = Jobject.getJSONArray("media");
			int len = results.length();
			webResults = new WebTVItem[len];
			for (int i = 0; i < len; i++){
				webResults[i] = new WebTVItem();
				webResults[i].setId(results.getJSONObject(i).getString("id"));
				webResults[i].setTitle(results.getJSONObject(i).getString("title"));
				webResults[i].setAuthor(results.getJSONObject(i).getString("author"));
				webResults[i].setDuration(results.getJSONObject(i).getInt("duration"));
				webResults[i].setIconURL(results.getJSONObject(i).getString("iconURL"));
				webResults[i].setWebTVService(service);
			}
				
				
		
		} catch (JSONException e) {
			return null;
		}
		
		
		return webResults;
	}

	private class GetHTTPResponse{
		public Bitmap getImage(String urlStr){
			URL url;
			Bitmap theImage = null;
			try {
				url = new URL(urlStr);
				InputStream in = url.openStream();
				theImage = BitmapFactory.decodeStream(in);
			} catch (MalformedURLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			return theImage;
		}
		public String getJSON(String urlStr){
			return getJSON(urlStr, 4096);
		}
		public String getJSON(String urlStr, int bufferSize){
			String json = "";	
			try {
				URL url = new URL(urlStr);
				InputStream in = url.openStream();
				
				
		    	byte buffer[] = new byte[bufferSize];
		    	int len;
				while ((len = in.read(buffer)) != -1) {
					json = json + new String(buffer, 0, len);
		    	}
				in.close();
			}catch (Exception e) {
				// TODO: handle exception
			}
			return json;
		}
	}
}
