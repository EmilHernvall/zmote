package se.z_app.zmote.gui;

import se.z_app.stb.STB;
import se.z_app.stb.STB.STBEnum;
import se.z_app.stb.api.STBContainer;
import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;
import android.view.View;
import android.widget.Button;

public class MainTestActivity extends Activity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_test);
        
        Button stbProxy = (Button) findViewById(R.id.bLoadSTBProxy);
        stbProxy.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				STB stb = new STB();
				stb.setBoxName("STB Proxy");
				stb.setIP("130.236.248.226");
				stb.setType(STBEnum.ZENTERIO);
				stb.setMAC("00:07:67:9B:EB:33");
				STBContainer.instance().setSTB(stb);
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
				STBContainer.instance().setSTB(stb);
			}
		});
        Button stb2 = (Button) findViewById(R.id.bLoadSTB1);
        stb1.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				STB stb = new STB();
				stb.setBoxName("Zenterio228");
				stb.setIP("130.236.248.228");
				stb.setType(STBEnum.ZENTERIO);
				stb.setMAC("00:07:67:9B:EB:35");
				STBContainer.instance().setSTB(stb);
			}
		});
        
        
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_main_test, menu);
        return true;
    }
}
