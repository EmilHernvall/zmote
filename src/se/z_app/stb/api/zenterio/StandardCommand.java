package se.z_app.stb.api.zenterio;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.Date;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.graphics.Bitmap;
import se.z_app.stb.Channel;
import se.z_app.stb.EPG;
import se.z_app.stb.Program;
import se.z_app.stb.STB;
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
		
		String jsonString = new GetHTTPResponse().getJSON("http://" + ip + "/mdio/epg");
		//System.out.println("EPG-> "+jsonString);
		
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
					Program program = new Program();
					program.setName(jsonProgram.getString("name"));
					program.setShortText(jsonProgram.getString("shorttext"));
					program.setLongText(jsonProgram.getString("exttext"));
					program.setEventID(jsonProgram.getInt("eventId"));
					
					/*
					String start = jsonProgram.getString("start");
					start = start.replace(" ", "-");
					start = start.replace(":", "-");
					String startAr[] = start.split("-");
					
				
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
					System.out.println("Duration ----------> " + duration);
					String durationAr[] = duration.split(".");
					int time = Integer.parseInt(durationAr[0])*3600;
					time += Integer.parseInt(durationAr[1])*60;
					time += Integer.parseInt(durationAr[2]);
					program.setDuration(time);
					*/
					channel.addProgram(program);
				}
				
			}
			
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
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
		
		return null;
	}

	
	public WebTVService[] getWebTVServices() {
		// TODO Auto-generated method stub
		return null;
	}

	
	public Bitmap getWebTVServiceIcon(WebTVService serivce) {
		// TODO Auto-generated method stub
		return null;
	}


	public Bitmap getWebTVItemIcon(WebTVItem item) {
		// TODO Auto-generated method stub
		return null;
	}

	public WebTVItem[] searchWebTVService(String query, WebTVService service) {
		// TODO Auto-generated method stub
		return null;
	}

	private class GetHTTPResponse{
		
		
		public String getJSON(String urlStr){
			
			
			String json = "";	
			try {
				URL url = new URL(urlStr);
				InputStream in = url.openStream();
				
				
		    	String str;
		    	byte buffer[] = new byte[2048];
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
