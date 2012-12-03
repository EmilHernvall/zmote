package se.z_app.zmote.gui;

import java.io.File;
import java.util.Vector;

import se.z_app.stb.MediaItem;
import se.z_app.stb.STB;
import se.z_app.stb.api.RemoteControl;
import se.z_app.stb.api.STBContainer;
import se.z_app.zmote.webtv.MediaStreamer;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

/**
 * Adapter for the list of STBs to be shown as 
 *  an android ListView.
 * @author Marcus Widegren, Christian Vestman
 */
public class SelectSTBAdapter extends BaseAdapter {
	private Activity activity;
    private Vector<STB> data;
    private static LayoutInflater inflater=null; 
    private SelectSTBAdapter theAdapter = this;
    private String filePath;
    
    /**
     * Constructor
     * @param The current activity
     * @param The list of STBs as a Vector<STB>
     */
    public SelectSTBAdapter(Activity a, Vector<STB> d, String filePath) {
        activity = a;
        data = d;
        this.filePath = filePath;
        inflater = (LayoutInflater)activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        
    }
 
    /**
     * Get size of the list of STBs
     * @return size of list
     */
    public int getCount() {
        return data.size();
    }
 
    /**
     * Get an item at a specified position
     * @param position - the position in the list
     * @return the item at that position
     */
    public Object getItem(int position) {
        return data.get(position);
    }
 
    /**
     * Get the id of an item
     * @param position
     * @return position
     */
    public long getItemId(int position) {
        return position;
    }
    
    /**
     * Get the view of a certain index, used automatically by android
     */
    public View getView(int position, View convertView, ViewGroup parent) {
        View vi=convertView;
        if(convertView==null){
            vi = inflater.inflate(R.layout.list_row, null);
        }
        
        ListTextViewElement boxName = (ListTextViewElement)vi.findViewById(R.id.boxname);
        boxName.setIndex(position);
        
        STB stb = new STB();
        stb = data.get(position);
        boxName.setText(stb.getBoxName());
        
        boxName.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				ListTextViewElement theView = (ListTextViewElement) v;
				STB theSelectedSTB = SelectSTBList.instance().getList().get(theView.getIndex());
				STBContainer.instance().setActiveSTB(theSelectedSTB);
				Activity theActivity = (Activity)v.getContext();
				theActivity.finish();
				
				//File path will be other then null if a file is lanched with Zmote as intent
				if(filePath !=null){
					File file = new File(filePath);
					if(file.exists()){
						MediaItem item = MediaStreamer.instance().addFile(file);
						RemoteControl.instance().launch(item);
					}
				}
				Intent mainIntent = new Intent(v.getContext(), MainTabActivity.class);
				theActivity.startActivity(mainIntent);
			}
		});
        
        //thumb image
        SelectSTBImageElement thumb_image=(SelectSTBImageElement)vi.findViewById(R.id.editimage); 
        thumb_image.setSTB(stb);
        thumb_image.setTextView(boxName);
        thumb_image.setIndex(position);
        
        /* Listener for when the edit button is clicked */
        thumb_image.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
            	SelectSTBImageElement theView = (SelectSTBImageElement)view;
            	Intent mainIntent = new Intent(view.getContext(), EditSTBActivity.class);
            	mainIntent.putExtra("index", theView.getIndex());
                view.getContext().startActivity(mainIntent);
                theAdapter.notifyDataSetChanged();
            }
        });
        
        return vi;
    }
}
