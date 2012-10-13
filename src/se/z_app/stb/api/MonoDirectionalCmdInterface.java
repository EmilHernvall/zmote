package se.z_app.stb.api;

import se.z_app.stb.WebTVItem;

public interface MonoDirectionalCmdInterface {
	public void sendText(String text);
	public void sendButton(RemoteControl.Button button);
	public void launch(String url);
	public void playWebTV(WebTVItem item);
	public void queueWebTV(WebTVItem item);

	
	public void facebookAuth(String accesstoken, String expires, String uid);
	public void rawPost(String rawPostData, String uri);
	public void rawGet(String uri);
}
 