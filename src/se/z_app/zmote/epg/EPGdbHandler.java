package se.z_app.zmote.epg;

import java.net.InetAddress;
import se.z_app.stb.Channel;
import se.z_app.stb.EPG;
import se.z_app.stb.STB;
import se.z_app.stb.Program;

import android.database.sqlite.*;


/**
 * First draft of the class, needs a lot of testing. Most of the content in the constructor should be removed, the database will have
 * been created earlier and are already available in the app.
 * @author Christian
 *
 */
public class EPGdbHandler {
	SQLiteDatabase database;

	//The create of the database should no be here, the database should already have been created
	public EPGdbHandler(){
		database= SQLiteDatabase.create(null);
		String query = "CREATE TABLE channel(name TEXT, iconUrl TEXT, nr INTEGER, onid INTEGER, tsid INTEGER, sid INTEGER);"; //The create string for channel
		database.execSQL(query); 
		query = "CREATE TABLE programs(name TEXT, eventID INTEGER, start INTEGER, duration INTEGER, shortText TEXT, longText TEXT);";
		database.execSQL(query);
		query = "CREATE TABLE stb(type TEXT, MAC TEXT, IP TEXT, boxName TEXT);";
		database.execSQL(query);
		//Create db if not exist??


	}

	public EPG selectEPG(STB stb){
		//TODO: Implement me
		return null;
	}
	
	public void updateChannel(STB stb, Channel channel){
		//only do the beneath if the channel is not existing?
		String query = "INSERT INTO channel(name, iconUrl, nr, onid, tsid, sid) VALUES ("+channel.getName()+", "+ channel.getIconUrl()+", "+channel.getNr()+", "+channel.getOnid()+", "+channel.getTsid()+", "+channel.getSid()+");";   //only if the channel is not in the database
		database.execSQL(query);


	}
	public void updateChannels(STB stb, Channel[] channels){
		for (int i = 1; i<=channels.length;i++) { //not tested and I'm tired, starts from 1 or 0??
			String query = "INSERT INTO channel (name, iconUrl, nr, onid, tsid, sid) VALUES ("+channels[i].getName()+", "+ channels[i].getIconUrl()+", "+channels[i].getNr()+", "+channels[i].getOnid()+", "+channels[i].getTsid()+", "+channels[i].getSid()+");";   //only if the channel is not in the database
			database.execSQL(query);
		}
	}
	public Program[] selectPrograms(STB stc, Channel channel){
		//String query ="SELECT * FROM programs"
		return null;
	}
	public void updateProgram(STB stb, Channel channel, Program program){
		String query = "INSERT INTO channel (name, eventID, start, duration, shortText, longText) VALUES ("+program.getName()+", "+ program.getEventID()+", "+program.getStart()+", "+program.getDuration()+", "+program.getShortText()+", "+program.getLongText()+");";   //only if the channel is not in the database
		database.execSQL(query);
	}
	public void updatePrograms(STB stb, Channel channel, Program[] programs){
		for (int i = 1; i<=programs.length;i++) { //not tested and I'm tired, starts from 1 or 0??
			String query = "INSERT INTO channel (name, eventID, start, duration, shortText, longText) VALUES ("+programs[i].getName()+", "+ programs[i].getEventID()+", "+programs[i].getStart()+", "+programs[i].getDuration()+", "+programs[i].getShortText()+", "+programs[i].getLongText()+");";   //only if the channel is not in the database
			database.execSQL(query);
		}
	}

}
