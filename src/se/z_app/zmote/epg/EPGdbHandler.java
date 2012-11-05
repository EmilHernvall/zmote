package se.z_app.zmote.epg;

import java.util.Date;

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
 * Had some help from the guide here: www.androidhive.info/2011/11/android-sqlite-database-tutorial
 * @author Christian
 *
 */
public class EPGdbHandler extends SQLiteOpenHelper {
	//	SQLiteDatabase database;
	private static final int DATABASE_VERSION=3;  //Basically, if one have made any changes to the onCreate(), change version number here will update the db
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
	/**
	 * Creates the database if not exist, does this on the create of the instance
	 */
	public void onCreate(SQLiteDatabase db) {
		String query = "CREATE TABLE " +TABLE_CHANNEL +"("+STB_MAC+" TEXT,"+CHANNEL_NAME+" TEXT,"+CHANNEL_ICONURL +" TEXT," +CHANNEL_NR +" INTEGER, "+ CHANNEL_ONID +" INTEGER,"+ CHANNEL_TSID +" INTEGER,"+CHANNEL_SID +" INTEGER);"; //The create string for channel
		db.execSQL(query); 
		query = "CREATE TABLE " +TABLE_PROGRAM +"("+STB_MAC+" TEXT,"+CHANNEL_NR+" INTEGER,"+PROGRAM_NAME+" TEXT,"+PROGRAM_EVENTID +" INTEGER," +PROGRAM_START +" INTEGER, "+ PROGRAM_DURATION +" INTEGER,"+ PROGRAM_SHORTTEXT +" TEXT,"+PROGRAM_LONGTEXT +" TEXT);"; //TODO: Is the channel_nr uniqe?
		db.execSQL(query);
		query = "CREATE TABLE " +TABLE_STB +"("+STB_TYPE+" TEXT,"+STB_MAC +" TEXT," +STB_IP +" TEXT, "+ STB_BOXNAME +" TEXT);";
		db.execSQL(query);
	}

	@Override
	/**
	 * Can be used to update the database, not yet tested
	 */
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		db.execSQL("DROP TABLE IF EXISTS "+TABLE_CHANNEL);  //TODO: not correct yet ??not??
		db.execSQL("DROP TABLE IF EXISTS "+TABLE_PROGRAM);
		db.execSQL("DROP TABLE IF EXISTS "+TABLE_STB);
		onCreate(db);
		}

	/**
	 * A method for getting and EPG object given and STB
	 * @param stb The STB which for get the EPG from
	 * @return and EPG object
	 */
	public EPG selectEPG(STB stb){
		EPG epg = new EPG();
		epg.setStb(stb);
		
		SQLiteDatabase db = this.getWritableDatabase();
//		String whereClause = ""+STB_MAC+"='"+stb.getMAC()+"'";
		Cursor cursor = db.query(TABLE_CHANNEL,null,""+STB_MAC+"='"+stb.getMAC() + "'", null,null,null,null);
//		Channel[] channels = new Channel[cursor.getCount()];
//		int iterationCounter=0;
		if(cursor.moveToFirst()) {
			do{
			Channel channel = new Channel();
			channel.setName(cursor.getString(cursor.getColumnIndex(CHANNEL_NAME)));
			channel.setIconUrl(cursor.getString(cursor.getColumnIndex(CHANNEL_ICONURL)));
			channel.setNr(cursor.getInt(cursor.getColumnIndex(CHANNEL_NR)));
			channel.setOnid(cursor.getInt(cursor.getColumnIndex(CHANNEL_ONID)));
			channel.setSid(cursor.getInt(cursor.getColumnIndex(CHANNEL_SID)));
			channel.setTsid(cursor.getInt(cursor.getColumnIndex(CHANNEL_TSID)));
			epg.addChannel(channel); 
//			iterationCounter++;
		}while (cursor.moveToNext());
			
		}
		return epg;
	}
	/**
	 * A method for update the a channel in the database, given an STB, TODO: to test if this overwrite the old channel or just creates a new record
	 * @param stb The STB in which the Channel should be update
	 * @param channel The channel to be updated
	 */
	public void updateChannel(STB stb, Channel channel){
		SQLiteDatabase db = this.getWritableDatabase();
		ContentValues values = new ContentValues();
		String whereClause = ""+STB_MAC+"='"+stb.getMAC()+"'";
		System.out.println(whereClause);
		values.put(STB_MAC, stb.getMAC());
		values.put(CHANNEL_NAME, channel.getName());
		values.put(CHANNEL_ICONURL, channel.getIconUrl());
		values.put(CHANNEL_NR, channel.getNr());
		values.put(CHANNEL_ONID, channel.getOnid());
		values.put(CHANNEL_TSID, channel.getTsid());
		values.put(CHANNEL_SID, channel.getSid());
		System.out.println(channel.getName()); //TODO: remove
//		db.insert(TABLE_CHANNEL, null, values); //TODO: Remove this, use update instead
		db.update(TABLE_CHANNEL, values, whereClause, null); //seams to fail, add new records instead of updating
		db.close();
	}
	
	/**
	 * A method for update the a channel in the database, given an STB
	 * @param stb The STB in which the Channel should be update
	 * @param channels The channels to be updated
	 */
	public void updateChannels(STB stb, Channel[] channels){
		SQLiteDatabase db = this.getWritableDatabase();
		for (int i = 0; i<channels.length;i++) { //not tested and I'm tired, starts from 1 or 0??
			updateChannel(stb, channels[i]);
		}
		db.close();
	}
	/**
	 * A method for selecting programs, given an STB and a channel
	 * @param stb The STB to get the data from
	 * @param channel The channel which for fetching the program
	 * @return an arraylist of the Programs
	 */
	@SuppressWarnings("deprecation")
	public Program[] selectPrograms(STB stb, Channel channel){
		
		//String selectQuery = "SELECT * FROM " +TABLE_PROGRAM; //no where statement, TODO: add where
		SQLiteDatabase db = this.getReadableDatabase();
		//Cursor cursor = db.rawQuery(selectQuery, null); //old
		Cursor cursor = db.query(TABLE_PROGRAM,null,""+STB_MAC+"='"+stb.getMAC() + "' AND "+CHANNEL_NR+"="+channel.getNr()+"", null,null,null,null);
		Program[] programArray = new Program[cursor.getCount()]; //length is null
		System.out.println("cursor coint is " +cursor.getCount());
		int iterationCounter=0;
		if(cursor.moveToFirst()) {
			do{
			Program program =new Program();
			program.setName(cursor.getString(cursor.getColumnIndex(PROGRAM_NAME)));
			program.setEventID(cursor.getInt(cursor.getColumnIndex(PROGRAM_EVENTID)));
			int temp = cursor.getInt(cursor.getColumnIndex(PROGRAM_START));
			Date tempDate = new Date();
			tempDate.setDate(temp);
			program.setStart(tempDate); //TODO: needs testing
			program.setShortText(cursor.getString(cursor.getColumnIndex(PROGRAM_SHORTTEXT)));
			program.setLongText(cursor.getString(cursor.getColumnIndex(PROGRAM_LONGTEXT)));
			programArray[iterationCounter]=program; 
			iterationCounter++;
		}while (cursor.moveToNext());
			
			
		}
		return programArray;
	}
	/**
	 * A method for update a program, TODO: check if it overwrites the old program which it should, or just creates a new record
	 * @param stb The given STB
	 * @param channel The channel to update
	 * @param program The program to update
	 */
	public void updateProgram(STB stb, Channel channel, Program program){
		SQLiteDatabase db = this.getWritableDatabase();
		ContentValues values = new ContentValues();
		values.put(STB_MAC, stb.getMAC());
		values.put(CHANNEL_NR, channel.getNr());
		values.put(PROGRAM_NAME, program.getName());
		values.put(PROGRAM_EVENTID, program.getEventID());
		values.put(PROGRAM_START, program.getStart().toString()); //will this work to make the date a string, needs testing
		values.put(PROGRAM_DURATION, program.getDuration());
		values.put(PROGRAM_SHORTTEXT, program.getShortText());
		values.put(PROGRAM_LONGTEXT, program.getLongText());
		db.insert(TABLE_PROGRAM, null, values);
		
	}
	/**
	 *A method for update programs
	 * @param stb The given STB
	 * @param channel The channel to update
	 * @param programs The programs to update
	 */
	public void updatePrograms(STB stb, Channel channel, Program[] programs){
		for (int i = 0; i<programs.length;i++) { //not tested and I'm tired, starts from 1 or 0??
			updateProgram(stb,channel,programs[i]);
		}
	}
}
