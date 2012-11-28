package se.z_app.zmote.webtv;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import se.z_app.stb.STB;
import se.z_app.stb.WebTVService;

/**
 * Class that handles the SQLite database for the WebTV features.
 * 
 * @author Sebastian Rauhala
 */
public class WebTVdbHandler extends SQLiteOpenHelper{
	
	private static final int DATABASE_VERSION=1;
	private static final String DATABASE_Name="WebTVServiceData";
	private static final String TABLE_WEBTVSERVICE="WebTVService";
	private static final String ID = "id";
	private static final String NAME = "name";
	private static final String ICONURL = "iconurl";
	private static final String STB_MAC="stb_mac";
	
	// Database creation sql statement
	private static final String DATABASE_CREATE = "CREATE TABLE "
	      + TABLE_WEBTVSERVICE + "(" + STB_MAC +" TEXT," + ID
	      + " TEXT, " + NAME + " TEXT," + ICONURL
	      + " TEXT);";
	
	
	/**
	 * The constructor of WebTvdbHandeler
	 * @param context the application context
	 */
	public WebTVdbHandler(Context context) {
		super(context, DATABASE_Name, null, DATABASE_VERSION);
	}
	
	/**
	 * Returns an array of all WebTV services available in the database for a specified STB
	 * @param stb - The STB from which to fetch WebTV services
	 * @return An array with all available WebTV services
	 */
	public WebTVService[] selectServices(STB stb){
		SQLiteDatabase db = this.getReadableDatabase();
		Cursor cursor = db.query(TABLE_WEBTVSERVICE ,null,""+STB_MAC+"='"+stb.getMAC() + "'",
		         null, null, null, null);
		WebTVService[] serviceArr  = new WebTVService[cursor.getCount()];

		if (cursor.getCount() == 0){
			return null;
		}
		else{
			int iterationCounter = 0;
			cursor.moveToFirst();
		    while (!cursor.isAfterLast()) {
		    	WebTVService service = new WebTVService();
		    	service.setID(cursor.getString(cursor.getColumnIndex(ID)));
		    	service.setName(cursor.getString(cursor.getColumnIndex(NAME)));
		    	service.setIconURL(cursor.getString(cursor.getColumnIndex(ICONURL)));
		    	serviceArr[iterationCounter] = service;
		    	iterationCounter++;
		    	cursor.moveToNext();
		    }
		    cursor.close();	
		    db.close();
			return serviceArr;
		}
	}
	
	/**
	 * Updates the database information for all WebTVservices in the array for a specific STB. 
	 * @param stb - The STB to which the services belongs
	 * @param service - An array with all services to update
	 */
	public void updateServices(STB stb, WebTVService[] service){
		for (int i = 0; i < service.length; i++) {
			updateServices(stb, service[i]);
		}
		
	}
	
	/**
	 * Updates the database information for the WebTVservices specified
	 * @param stb - The STB to which the service belongs
	 * @param service - The service to update
	 */
	public void updateServices (STB stb, WebTVService service){
		SQLiteDatabase db = this.getWritableDatabase();
		String whereClause = " "+STB_MAC+"='" +stb.getMAC()+"' AND "+ ID + "='" + service.getID() +"'";
		ContentValues values = new ContentValues();
		values.put(STB_MAC, stb.getMAC());
		values.put(ID, service.getID());
		values.put(NAME, service.getName());
		values.put(ICONURL, service.getIconURL());
		int nrOfRowsAffected = db.update(TABLE_WEBTVSERVICE, values, whereClause, null);
		if (nrOfRowsAffected==0){
			db.insert(TABLE_WEBTVSERVICE, null,values);
		}
		db.close();
	}

	/**
	 * Called to create the database on creation of the class
	 * @param db - The database to create
	 */
	@Override
	public void onCreate(SQLiteDatabase db) {
		db.execSQL(DATABASE_CREATE);
	}

	/**
	 * Called when the database needs to be updated
	 * @param db - The database to update
	 * @param oldVersion - The version of the old database
	 * @param newVersion - The version of the new database
	 */
	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		Log.w(WebTVdbHandler.class.getName(),
		        "Upgrading WebTVdbHandler database from version " + oldVersion + " to "
		            + newVersion + ", which will destroy all old data");
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_WEBTVSERVICE);
		onCreate(db);
	}

}
