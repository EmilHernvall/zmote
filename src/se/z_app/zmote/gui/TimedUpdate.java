package se.z_app.zmote.gui;

import se.z_app.social.Feed;


/**
 * Class for updating the feed periodical. Reason for 
 * not having it as an inner class is that i am unsure if it
 * might still hold a reference to the activity if it exists 
 * inside of it.
 * @author Linus
 *
 */
class TimedUpdate implements Runnable{
	private Object syncLock1 = new Object();
	private Object syncLock2 = new Object();
	private ZChatActivity myActivity;
	private int timeBeforeFirstUpdate;
	private int timeBetweenUpdates;
	private boolean isRunning;
	
	/**
	 * Constructor for class.
	 * @param myActivity
	 * @param timeBeforeFirstUpdate
	 * @param timeBetweenUpdates
	 */
	public TimedUpdate(ZChatActivity myActivity, int timeBeforeFirstUpdate, 
			int timeBetweenUpdates) {
		setActivity(myActivity);
		this.timeBeforeFirstUpdate = timeBeforeFirstUpdate;
		this.timeBetweenUpdates = timeBetweenUpdates;
		isRunning = true;
		
	}
	/**
	 * Synchronized boolean for checking if the class is still running.
	 * @return
	 */
	public boolean isRunning(){
		synchronized (syncLock1){
			return isRunning;
		}
	}
	/**
	 * Called when the Class should stop executing.
	 */
	public void stopRunning(){
		synchronized (syncLock1){
			isRunning = false;
		}
	}
	/**
	 * Syncronized getter for the activity.
	 * @return
	 */
	public ZChatActivity getActivity(){
		synchronized (syncLock2){
			return myActivity;
		}
	}
	/**
	 * Syncronized setter for the activity needing to be updated.
	 * @param activity
	 */
	public void setActivity(ZChatActivity activity){
		synchronized (syncLock2){
			myActivity = activity;
		}
	}
	
	
	@Override
	public void run() {
		try {
			Thread.sleep(timeBeforeFirstUpdate);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		while(getActivity()!=null && isRunning()){
			Feed newFeed = getActivity().getAdapter().getFeed(getActivity().getMyProgram());
			
			if(newFeed != null && newFeed.getLastUpdated().after(getActivity().getFeed().getLastUpdated())){
				getActivity().setFeed(newFeed);
				getActivity().runOnUiThread(new Runnable() {

					@Override
					public void run() {
						getActivity().postList.setAdapter(getActivity().new MyListAdabter(getActivity()));

					}
				});
			}
			try {
				Thread.sleep(timeBetweenUpdates);
			} catch (InterruptedException e) {

				e.printStackTrace();
			}
		
		}
		
		

	}

}
