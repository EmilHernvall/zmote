package se.z_app.stb.api.zenterio;

import android.graphics.Bitmap;
import se.z_app.stb.Channel;
import se.z_app.stb.EPG;
import se.z_app.stb.WebTVItem;
import se.z_app.stb.WebTVService;
import se.z_app.stb.api.BiDirectionalCmdInterface;

public class StandardCommand implements BiDirectionalCmdInterface{

	@Override
	public EPG getEPG() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Channel getChannel() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Bitmap getChannelIcon(Channel channel) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public WebTVService[] getWebTVServices() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Bitmap getWebTVServiceIcon(WebTVService serivce) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Bitmap getWebTVItemIcon(WebTVItem item) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public WebTVItem[] searchWebTVService(String query, WebTVService service) {
		// TODO Auto-generated method stub
		return null;
	}

}
