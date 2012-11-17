package se.z_app.zmote.gui;



import java.io.File;

import se.z_app.stb.MediaItem;
import se.z_app.stb.STB;
import se.z_app.stb.STB.STBEnum;
import se.z_app.stb.api.EPGData;
import se.z_app.stb.api.RemoteControl;
import se.z_app.stb.api.STBContainer;
import se.z_app.zmote.epg.EPGContentHandler;
import se.z_app.zmote.webtv.MediaStreamer;
import android.app.Activity;
import android.content.Intent;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.Button;


//Pleas add your view or setting on this activity to make it easier for testing and accsess 
public class MainTestActivity extends Activity {

	private MediaStreamer ms;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_test);
        EPGData.instance();
        EPGContentHandler.setContext(this.getApplicationContext());
        EPGContentHandler.instance();
        
        STBContainer.instance();
        
        
		WifiManager myWifiManager = (WifiManager) getSystemService(WIFI_SERVICE);
    	WifiInfo myWifiInfo = myWifiManager.getConnectionInfo();
    	int ipAddress = myWifiInfo.getIpAddress();    	
    	@SuppressWarnings("deprecation")
		String ip = android.text.format.Formatter.formatIpAddress(ipAddress);
        
    	ms = MediaStreamer.instance();
    	ms.setLocalIP(ip);
        
    	STB stb = new STB();
		stb.setBoxName("STB Proxy");
		stb.setIP("130.236.248.56");
		stb.setType(STBEnum.ZENTERIO);
		stb.setMAC("00:07:67:9B:EB:33");

		STBContainer.instance().setActiveSTB(stb);
    	
        
        
        Button stbProxy = (Button) findViewById(R.id.bLoadSTBProxy);
        stbProxy.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				
				STB stb = new STB();
				stb.setBoxName("STB Proxy");
				stb.setIP("130.236.248.226");
				stb.setType(STBEnum.ZENTERIO);
				stb.setMAC("00:07:67:9B:EB:33");
		
				STBContainer.instance().setActiveSTB(stb);
				
				
			}
		});
        Button stb1 = (Button) findViewById(R.id.bLoadSTB1);
        stb1.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
			
				STB stb = new STB();
				stb.setBoxName("Zenterio227");
				stb.setIP("130.236.248.227");
				stb.setType(STBEnum.ZENTERIO);
				stb.setMAC("00:07:67:9B:EB:34");
			
				STBContainer.instance().setActiveSTB(stb);
				
			}
		});
        
        Button stb2 = (Button) findViewById(R.id.bLoadSTB2);
        stb2.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				
				STB stb = new STB();
				stb.setBoxName("Zenterio228");
				stb.setIP("130.236.248.228");
				stb.setType(STBEnum.ZENTERIO);
				stb.setMAC("00:07:67:9B:EB:35");
			
				STBContainer.instance().setActiveSTB(stb);
			
			}
		});
        
        Button stb3 = (Button) findViewById(R.id.LoadLocalSTB);
        stb3.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// Write in your local STB
				STB stb = new STB();
				stb.setBoxName("Zenterio227");
				stb.setIP("130.236.248.227");
				stb.setType(STBEnum.ZENTERIO);
				stb.setMAC("00:07:67:9B:EB:35");
			
				STBContainer.instance().setActiveSTB(stb);
				
			}
		});
        
        Button splash = (Button) findViewById(R.id.bSplash);
        splash.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				
				Intent intent = new Intent(MainTestActivity.this, SplashActivity.class);
				MainTestActivity.this.startActivity(intent);
			}
		});
        

        Button selectSTB = (Button) findViewById(R.id.bSelectSTBActivity);
        selectSTB.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
			
				Intent intent = new Intent(MainTestActivity.this, SelectSTBActivity.class);
				MainTestActivity.this.startActivity(intent);
			}
		});
        
        
        Button mainTab = (Button) findViewById(R.id.bMainTabActivity);
        mainTab.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(MainTestActivity.this, MainTabActivity.class);
				MainTestActivity.this.startActivity(intent);
			}
		});
        
        
        Button playClip = (Button) findViewById(R.id.PlayClip);
        playClip.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {

				
				MediaItem item = ms.addFile(new File("/mnt/sdcard/download/test2.mp4"));
				System.out.println("MediaItem: " + item.getName());
				System.out.println("MediaItem: " + item.getUrl());
				
				RemoteControl.instance().launch(item);				
				
			}
		});
        
        

        
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_main_test, menu);
        return super.onCreateOptionsMenu(menu);
    }
}
