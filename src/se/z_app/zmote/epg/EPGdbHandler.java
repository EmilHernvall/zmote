package se.z_app.zmote.epg;

import se.z_app.stb.Channel;
import se.z_app.stb.EPG;
import se.z_app.stb.STB;
import se.z_app.stb.Program;
import android.content.ContentValues;
import android.database.sqlite.*;
import android.util.Log;

/**
 * First draft of the class, needs a lot of testing. Most of the content in the constructor should be removed, the database will have
 * been created earlier and are already available in the app.
 * @author Christian
 *
 */
public class EPGdbHandler {
	SQLiteDatabase database;
	
	//The code in the constructor is most for testing purpose
	public EPGdbHandler(){
		database= SQLiteDatabase.create(null);
		String query = "CREATE TABLE channel(name String, iconUrl String, nr INTEGER, onid INTEGER, tsdi INTEGER, sid INTEGER);"; //The create string for channel
		database.execSQL(query); 
		//Create db if not exist??
		
	}

	public EPG selectEPG(STB stb){
		//TODO: Implement me
		return null;
	}
	public void updateEPG(STB stb, EPG epg){
		//TODO: Implement me
	}
	public Channel[] selectChannels(STB stb){
		//TODO: Implement
		return null;
	}
	public void updateChannel(STB stb, Channel channel){
		//only do the beneath if the channel is not existing?
		String query = "INSERT INTO channel (name, iconUrl, nr, onid, tsid, sid) VALUES ("+channel.getName()+", "+ channel.getIconUrl()+", "+channel.getNr()+", "+channel.getOnid()+", "+channel.getTsid()+", "+channel.getSid()+");";   //only if the channel is not in the database
		database.execSQL(query);
		
		
	}
	public void updateChannels(STB stb, Channel[] channels){
		for (int i = 1; i<=channels.length;i++) { //not tested and I'm tired, starts from 1 or 0??
		String query = "INSERT INTO channel (name, iconUrl, nr, onid, tsid, sid) VALUES ("+channels[i].getName()+", "+ channels[i].getIconUrl()+", "+channels[i].getNr()+", "+channels[i].getOnid()+", "+channels[i].getTsid()+", "+channels[i].getSid()+");";   //only if the channel is not in the database
		database.execSQL(query);
		}
	}
	public Program[] selectPrograms(STB stc, Channel channel){
		//TODO: Implement
		return null;
	}
	public void updateProgram(STB stb, Channel channel, Program program){
		//TODO: Implement
	}
	public void updatePrograms(STB stb, Channel channel, Program[] programs){
		//TODO: Implement
	}
	
}
