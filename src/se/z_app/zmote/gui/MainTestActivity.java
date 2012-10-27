package se.z_app.zmote.gui;

import se.z_app.stb.STB;
import se.z_app.stb.STB.STBEnum;
import se.z_app.stb.api.STBContainer;
import android.os.Bundle;
import android.content.Intent;
import android.view.Menu;
import android.view.View;
import android.widget.Button;


//Pleas add your view or setting on this activity to make it easier for testing and accsess 
public class MainTestActivity extends ZmoteActivity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_test);
        
        
        
        Button stbProxy = (Button) findViewById(R.id.bLoadSTBProxy);
        stbProxy.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				vibrate();
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
				vibrate();
				STB stb = new STB();
				stb.setBoxName("Zenterio227");
				stb.setIP("130.236.248.227");
				stb.setType(STBEnum.ZENTERIO);
				stb.setMAC("00:07:67:9B:EB:34");
				STBContainer.instance().setActiveSTB(stb);
			}
		});
        
        Button stb2 = (Button) findViewById(R.id.bLoadSTB1);
        stb2.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				vibrate();
				STB stb = new STB();
				stb.setBoxName("Zenterio228");
				stb.setIP("130.236.248.228");
				stb.setType(STBEnum.ZENTERIO);
				stb.setMAC("00:07:67:9B:EB:35");
				STBContainer.instance().setActiveSTB(stb);
			}
		});
        
        Button splash = (Button) findViewById(R.id.bSplash);
        splash.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				vibrate();
				Intent intent = new Intent(MainTestActivity.this, SplashActivity.class);
				MainTestActivity.this.startActivity(intent);
			}
		});
        
        Button remote = (Button) findViewById(R.id.bRemoteControl);
        remote.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				vibrate();
				Intent intent = new Intent(MainTestActivity.this, RemoteControlActivity.class);
				MainTestActivity.this.startActivity(intent);
			}
		});
        Button selectSTB = (Button) findViewById(R.id.bSelectSTBActivity);
        selectSTB.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				vibrate();
				Intent intent = new Intent(MainTestActivity.this, SelectSTBActivity.class);
				MainTestActivity.this.startActivity(intent);
			}
		});
        
        Button mainview2 = (Button) findViewById(R.id.bMainview2);
        mainview2.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				vibrate();
				Intent intent = new Intent(MainTestActivity.this, MainActivityView2.class);
				MainTestActivity.this.startActivity(intent);
			}
		});
        
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_main_test, menu);
        return true;
    }
}
