package se.z_app.zmote.epg;

import se.z_app.stb.Channel;
import se.z_app.stb.EPG;
import se.z_app.stb.Program;
import se.z_app.stb.STB;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;


/**
 * First draft of the class, needs a lot of testing. Most of the content in the constructor should be removed, the database will have
 * been created earlier and are already available in the app.
 * Hade some help from the guide here: www.androidhive.info/2011/11/android-sqlite-database-tutorial
 * @author Christian
 *
 */
public class EPGdbHandler extends SQLiteOpenHelper {
	//	SQLiteDatabase database;
	private static final int DATABASE_VERSION=1;
	private static final String DATABASE_Name="EPGData";
	private static final String TABLE_CHANNEL="channel";
	private static final String CHANNEL_NAME="name";
	private static final String CHANNEL_ICONURL="iconurl";
	private static final String CHANNEL_NR="nr";
	private static final String CHANNEL_ONID="onid";
	private static final String CHANNEL_TSID="tsid";
	private static final String CHANNEL_SID="sid";
	
	private static final String TABLE_PROGRAM ="program";
	private static final String PROGRAM_NAME="name";
	private static final String PROGRAM_EVENTID="eventID";
	private static final String PROGRAM_START="start";
	private static final String PROGRAM_DURATION="duration";
	private static final String PROGRAM_SHORTTEXT="shortText";
	private static final String PROGRAM_LONGTEXT="longText";
	
	private static final String TABLE_STB ="stb";
	private static final String STB_TYPE="type";
	private static final String STB_MAC="mac";
	private static final String STB_IP="ip";
	private static final String STB_BOXNAME="boxName";
	
	public EPGdbHandler(Context context){
		super(context, DATABASE_Name,null,DATABASE_VERSION);
	}
	


	@Override
	public void onCreate(SQLiteDatabase db) {
		String query = "CREATE TABLE " +TABLE_CHANNEL +"("+CHANNEL_NAME+" TEXT,"+CHANNEL_ICONURL +" TEXT," +CHANNEL_NR +" INTEGER, "+ CHANNEL_ONID +" INTEGER,"+ CHANNEL_TSID +" INTEGER,"+CHANNEL_SID +" INTEGER);"; //The create string for channel
		db.execSQL(query); 
		query = "CREATE TABLE " +TABLE_PROGRAM +"("+PROGRAM_NAME+" TEXT,"+PROGRAM_EVENTID +" INTEGER," +PROGRAM_START +" INTEGER, "+ PROGRAM_DURATION +" INTEGER,"+ PROGRAM_SHORTTEXT +" TEXT,"+PROGRAM_LONGTEXT +" TEXT);";
		db.execSQL(query);
		query = "CREATE TABLE " +TABLE_STB +"("+STB_TYPE+" TEXT,"+STB_MAC +" TEXT," +STB_IP +" TEXT, "+ STB_BOXNAME +" TEXT);";
		db.execSQL(query);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		db.execSQL("DROP TABLE IF EXISTS "+TABLE_CHANNEL);  //not correct yet
		db.execSQL("DROP TABLE IF EXISTS "+TABLE_PROGRAM);
		db.execSQL("DROP TABLE IF EXISTS "+TABLE_STB);
		onCreate(db);
		}

	public EPG selectEPG(STB stb){
		//TODO: Implement me
		return null;
	}

	public void updateChannel(STB stb, Channel channel){
		//only do the beneath if the channel is not existing?
//		String query = "INSERT INTO channel(name, iconUrl, nr, onid, tsid, sid) VALUES ("+channel.getName()+", "+ channel.getIconUrl()+", "+channel.getNr()+", "+channel.getOnid()+", "+channel.getTsid()+", "+channel.getSid()+");";   //only if the channel is not in the database
//		database.execSQL(query);
		SQLiteDatabase db = this.getWritableDatabase();
		ContentValues values = new ContentValues();
		values.put(CHANNEL_NAME, channel.getName());
		values.put(CHANNEL_ICONURL, channel.getIconUrl());
		values.put(CHANNEL_NR, channel.getNr());
		values.put(CHANNEL_ONID, channel.getOnid());
		values.put(CHANNEL_TSID, channel.getTsid());
		values.put(CHANNEL_SID, channel.getSid());
		db.insert(TABLE_CHANNEL, null, values);
		db.close();


	}
	/**
	 * A function for update a Channel TODO: get this to work properly, according to which the input parameters
	 * @param stb
	 * @param channels
	 */
	public void updateChannels(STB stb, Channel[] channels){
		SQLiteDatabase db = this.getWritableDatabase();
		for (int i = 1; i<=channels.length;i++) { //not tested and I'm tired, starts from 1 or 0??
			ContentValues values = new ContentValues();
			values.put(CHANNEL_NAME, channels[i].getName());
			values.put(CHANNEL_ICONURL, channels[i].getIconUrl());
			values.put(CHANNEL_NR, channels[i].getNr());
			values.put(CHANNEL_ONID, channels[i].getOnid());
			values.put(CHANNEL_TSID, channels[i].getTsid());
			values.put(CHANNEL_SID, channels[i].getSid());
			db.insert(TABLE_CHANNEL, null, values);
		}
		db.close();
	}
	public Program[] selectPrograms(STB stb, Channel channel){
		Program[] programArray = null;  //BUGG?
		String selectQuery = "SELECT * FROM " +TABLE_CHANNEL; //TODO: Change to TABLE_PROGRAM
		SQLiteDatabase db = this.getReadableDatabase();
		Cursor cursor = db.rawQuery(selectQuery, null);
		int iterationCounter=0;
		while(cursor.moveToFirst()){
			Program program =new Program();
			program.setName(cursor.getString(0));  //ugly to use the column index, change later!
			program.setEventID(cursor.getInt(1));
			//program.setStart(Date)(cursor.getInt(2); //TODO:FIX THIS SHIT
			program.setDuration(cursor.getInt(3));
			program.setShortText(cursor.getString(4));
			program.setLongText(cursor.getString(5));
			programArray[iterationCounter]=program; //Null-pointer exception
			iterationCounter++;
			
			
		}
		return programArray;
	}
	/**
	 * TODO: Get this thing to work properly
	 * @param stb
	 * @param channel
	 * @param program
	 */
	public void updateProgram(STB stb, Channel channel, Program program){
		SQLiteDatabase db = this.getWritableDatabase();
		String query = "INSERT INTO channel (name, eventID, start, duration, shortText, longText) VALUES ("+program.getName()+", "+ program.getEventID()+", "+program.getStart()+", "+program.getDuration()+", "+program.getShortText()+", "+program.getLongText()+");";   //only if the channel is not in the database
		db.execSQL(query);
	}
	/**
	 * TODO: Get this to work properly
	 * @param stb
	 * @param channel
	 * @param programs
	 */
	public void updatePrograms(STB stb, Channel channel, Program[] programs){
		SQLiteDatabase db = this.getWritableDatabase();
		for (int i = 1; i<=programs.length;i++) { //not tested and I'm tired, starts from 1 or 0??
			String query = "INSERT INTO channel (name, eventID, start, duration, shortText, longText) VALUES ("+programs[i].getName()+", "+ programs[i].getEventID()+", "+programs[i].getStart()+", "+programs[i].getDuration()+", "+programs[i].getShortText()+", "+programs[i].getLongText()+");";   //only if the channel is not in the database
			db.execSQL(query);
		}
	}

	

}
