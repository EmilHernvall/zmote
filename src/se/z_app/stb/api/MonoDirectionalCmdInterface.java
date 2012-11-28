package se.z_app.stb.api;

import se.z_app.stb.MediaItem;
import se.z_app.stb.WebTVItem;


/**
 * Interface for the monodirectional commands send to the box (i.e. Remotecontrol commands)
 * @author Rasmus Holm, Linus Back
 *
 */
public interface MonoDirectionalCmdInterface {
	
	/**
	 * Sends a text string to the STB.
	 * @param text 
	 */
	public void sendText(String text);
	
	/**
	 * Sends a button command to the STB.
	 * @param button
	 */
	public void sendButton(RemoteControl.Button button);
	
	/**
	 * Launches the note corresponding to the url. 
	 * @param url
	 */
	public void launch(String url);
	
	/**
	 * Launches the note corresponding to the media item.
	 * @param item
	 */
	public void launch(MediaItem item);
	
	/**
	 * Plays the webTV item.
	 * @param item
	 */
	public void playWebTV(WebTVItem item);
	
	/**
	 * Queues the webTV item.
	 * @param item
	 */
	public void queueWebTV(WebTVItem item);
	
	/**
	 * Sends the facebook authorisation.
	 * @param accesstoken, expires, uid
	 */
	public void facebookAuth(String accesstoken, String expires, String uid);
	
	/**
	 * Sends a rawpost.
	 * @param rawPostData, uri
	 */
	public void rawPost(String rawPostData, String uri);
	
	/**
	 * Sends a raw get.
	 * @param uri
	 */
	public void rawGet(String uri);
}
 