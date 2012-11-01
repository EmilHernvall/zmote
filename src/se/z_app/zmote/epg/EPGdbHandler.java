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
	/**
	 * Creates the database if not exist, does this on the create of the instance
	 */
	public void onCreate(SQLiteDatabase db) {
		String query = "CREATE TABLE " +TABLE_CHANNEL +"("+CHANNEL_NAME+" TEXT,"+CHANNEL_ICONURL +" TEXT," +CHANNEL_NR +" INTEGER, "+ CHANNEL_ONID +" INTEGER,"+ CHANNEL_TSID +" INTEGER,"+CHANNEL_SID +" INTEGER);"; //The create string for channel
		db.execSQL(query); 
		query = "CREATE TABLE " +TABLE_PROGRAM +"("+PROGRAM_NAME+" TEXT,"+PROGRAM_EVENTID +" INTEGER," +PROGRAM_START +" INTEGER, "+ PROGRAM_DURATION +" INTEGER,"+ PROGRAM_SHORTTEXT +" TEXT,"+PROGRAM_LONGTEXT +" TEXT);";
		db.execSQL(query);
		query = "CREATE TABLE " +TABLE_STB +"("+STB_TYPE+" TEXT,"+STB_MAC +" TEXT," +STB_IP +" TEXT, "+ STB_BOXNAME +" TEXT);";
		db.execSQL(query);
	}

	@Override
	/**
	 * Can be used to update the database, not yet tested
	 */
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		db.execSQL("DROP TABLE IF EXISTS "+TABLE_CHANNEL);  //not correct yet ??not??
		db.execSQL("DROP TABLE IF EXISTS "+TABLE_PROGRAM);
		db.execSQL("DROP TABLE IF EXISTS "+TABLE_STB);
		onCreate(db);
		}

	public EPG selectEPG(STB stb){
		//TODO: Implement me
		return null;
	}
	/**
	 * A method for update the a channel in the database, given an STB, TODO: to test if this overwrite the old channel or just creates a new record
	 * @param stb The STB in which the Channel should be update
	 * @param channel The channel to be updated
	 */
	public void updateChannel(STB stb, Channel channel){
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
	 * A method for update the a channel in the database, given an STB
	 * @param stb The STB in which the Channel should be update
	 * @param channels The channels to be updated
	 */
	public void updateChannels(STB stb, Channel[] channels){
		SQLiteDatabase db = this.getWritableDatabase();
		for (int i = 1; i<=channels.length;i++) { //not tested and I'm tired, starts from 1 or 0??
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
		
		String selectQuery = "SELECT * FROM " +TABLE_PROGRAM; //no where statement, TODO: add where
		SQLiteDatabase db = this.getReadableDatabase();
		Cursor cursor = db.rawQuery(selectQuery, null);
		Program[] programArray = new Program[cursor.getCount()];
		int iterationCounter=0;
		if(cursor.moveToFirst()) {
			do{
			Program program =new Program();
			program.setName(cursor.getString(0));  //ugly to use the column index, change later!
			program.setEventID(cursor.getInt(1));
			int temp = cursor.getInt(2);
			Date tempDate = new Date();
			tempDate.setDate(temp);
			program.setStart(tempDate); //needs testing
			program.setShortText(cursor.getString(4));
			program.setLongText(cursor.getString(5));
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
		for (int i = 1; i<=programs.length;i++) { //not tested and I'm tired, starts from 1 or 0??
			updateProgram(stb,channel,programs[i]);
		}
	}

	

}
