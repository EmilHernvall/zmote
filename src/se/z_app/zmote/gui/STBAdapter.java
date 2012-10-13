package se.z_app.zmote.gui;

import java.util.Vector;

import se.z_app.stb.STB;
import se.z_app.stb.api.zenterio.Discovery;
import android.app.Activity;
import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
 
public class STBAdapter extends BaseAdapter {
 
    private Activity activity;
    private Vector<STB> data;
    private static LayoutInflater inflater=null; 
 
    public STBAdapter(Activity a, Vector<STB> d) {
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
 
        TextView boxName = (TextView)vi.findViewById(R.id.boxname);
        
        STB stb = new STB();
        stb = data.get(position);
        boxName.setText(stb.getBoxName());
        
        ListImageElement thumb_image=(ListImageElement)vi.findViewById(R.id.editimage); // thumb image
        thumb_image.setSTB(stb);
        thumb_image.setTextView(boxName);
        
        /* Listener for when the edit button is clicked */
        thumb_image.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
            	ListImageElement theView = (ListImageElement)view;
            	//theView.getSTB().setBoxName("hej");
            	theView.getTextView().setCursorVisible(true);
            	theView.getTextView().setFocusable(true);
            	theView.getTextView().setFocusableInTouchMode(true);
            	theView.getTextView().requestFocus();
            	theView.getTextView().setText("Hej");
            	//notifyDataSetChanged();
            }
        });
        
        return vi;
    }
}