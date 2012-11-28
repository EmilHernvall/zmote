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
	
	private static final int DATABASE_VERSION=8;
	private static final String DATABASE_Name="WebTVServiceData";
	private static final String TABLE_WebTVService="WebTVService";
	private static final String ID = "id";
	private static final String NAME = "name";
	private static final String ICONURL = "iconurl";
	private static final String STB_MAC="stb_mac";
	
	// Database creation sql statement
	private static final String DATABASE_CREATE = "CREATE TABLE "
	      + TABLE_WebTVService + "(" + STB_MAC +" TEXT," + ID
	      + " TEXT, " + NAME + " TEXT," + ICONURL
	      + " TEXT);";
	
	
	/**
	 * Creates a handler for the database that can be used to access and manage it.
	 * @param context the application context
	 */
	public WebTVdbHandler(Context context) {
		super(context, DATABASE_Name, null, DATABASE_VERSION);
	}
	
	/**
	 * Creates an array with all WebTV services available on the specified STB
	 * @param stb - The STB from which to fetch WebTV services
	 * @return An array with all available WebTV services
	 */
	public WebTVService[] selectServices(STB stb){
		SQLiteDatabase db = this.getReadableDatabase();
		Cursor cursor = db.query(TABLE_WebTVService ,null,""+STB_MAC+"='"+stb.getMAC() + "'",
		         null, null, null, null);
		WebTVService[] serviceArr  = new WebTVService[cursor.getCount()];
		Log.w(WebTVdbHandler.class.getName(), "WebTVServises Cursor.length " + cursor.getCount());
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
	    Log.w(WebTVdbHandler.class.getName(), "WebTVServises WebTVServices from db " + serviceArr);
	    // Make sure to close the cursor
	    cursor.close();	
	    db.close();
		return serviceArr;
	}
	
	/**
	 * Updates the database information for all services in the array. 
	 * All services must belong to the same STB.
	 * @param stb - The STB to which the services belongs
	 * @param service - An array with all services to update
	 */
	public void updateServices(STB stb, WebTVService[] service){
		for (int i = 0; i < service.length; i++) { //not tested and I'm tired, starts from 1 or 0??
			updateServices(stb, service[i]);
		}
		
	}
	
	/**
	 * Updates the database information for the service specified
	 * @param stb - The STB to which the service belongs
	 * @param service - The service to update
	 */
	public void updateServices (STB stb, WebTVService service){
		System.out.println(DATABASE_CREATE);
		SQLiteDatabase db = this.getWritableDatabase();
		String whereClause = " "+STB_MAC+"='" +stb.getMAC()+"' AND "+ ID + "='" + service.getID() +"'";
		ContentValues values = new ContentValues();
		values.put(STB_MAC, stb.getMAC());
		values.put(ID, service.getID());
		values.put(NAME, service.getName());
		values.put(ICONURL, service.getIconURL());
		int nrOfRowsAffected = db.update(TABLE_WebTVService, values, whereClause, null);
		if (nrOfRowsAffected==0){
			db.insert(TABLE_WebTVService, null,values);
		}
		db.close();
	}

	/**
	 * Called to create the database for the first time.
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
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_WebTVService);
		onCreate(db);
	}

}
