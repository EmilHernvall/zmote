package se.z_app.zmote.gui;

import java.util.Vector;

import se.z_app.stb.STB;
import se.z_app.stb.api.STBContainer;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
 
public class SelectSTBAdapter extends BaseAdapter {
 
	private Activity activity;
    private Vector<STB> data;
    private static LayoutInflater inflater=null; 
    private SelectSTBAdapter theAdapter = this;
 
    public SelectSTBAdapter(Activity a, Vector<STB> d) {
        activity = a;
        data=d;
        inflater = (LayoutInflater)activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }
 
    public int getCount() {
        return data.size();
    }
 
    public Object getItem(int position) {
        return position;
    }
 
    public long getItemId(int position) {
        return position;
    }
    
    public View getView(int position, View convertView, ViewGroup parent) {
        View vi=convertView;
        if(convertView==null)
            vi = inflater.inflate(R.layout.list_row, null);
 
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
				Intent mainIntent = new Intent(v.getContext(), RemoteControlActivity.class);
				theActivity.startActivity(mainIntent);
			}
		});
        
        ListImageElement thumb_image=(ListImageElement)vi.findViewById(R.id.editimage); // thumb image
        thumb_image.setSTB(stb);
        thumb_image.setTextView(boxName);
        thumb_image.setIndex(position);
        
        /* Listener for when the edit button is clicked */
        thumb_image.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
            	ListImageElement theView = (ListImageElement)view;
            	Intent mainIntent = new Intent(view.getContext(), EditSTBActivity.class);
            	mainIntent.putExtra("index", theView.getIndex());
                view.getContext().startActivity(mainIntent);
                theAdapter.notifyDataSetChanged();
            }
        });
        
        return vi;
    }
}
