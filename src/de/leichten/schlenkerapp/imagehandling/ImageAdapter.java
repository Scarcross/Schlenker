package de.leichten.schlenkerapp.imagehandling;

import android.app.Activity;
import android.content.Context;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import de.leichten.schlenkerapp.R;

public class ImageAdapter extends BaseAdapter{

	private Activity activity;
    private String[] data;
    private String[] name;
 
    private static LayoutInflater inflater = null;
    public ImageLoader imageLoader;
 
    public ImageAdapter(Activity a, String[] d, String[] n) {
        activity = a;
        data = d;
        name = n;
        inflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        imageLoader = new ImageLoader(activity.getApplicationContext());
    }
 
    public int getCount() {
        return data.length;
 
    }
 
    public Object getItem(int position) {
        return position;
    }
 
    public long getItemId(int position) {
        return position;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        View vi = convertView;
        if (convertView == null)
            vi = inflater.inflate(R.layout.gridview_item, null);
        // Locate the TextView in item.xml
        TextView text = (TextView) vi.findViewById(R.id.text);
        // Locate the ImageView in item.xml
        ImageView image = (ImageView) vi.findViewById(R.id.image);
        
        DisplayMetrics metrics = new DisplayMetrics();
        activity.getWindowManager().getDefaultDisplay().getMetrics(metrics);
        
        int height = metrics.heightPixels/4;
        int width = metrics.widthPixels/2;
        
        image.setLayoutParams(new RelativeLayout.LayoutParams(width, height));
        image.setScaleType(ImageView.ScaleType.CENTER_CROP);
        image.setPadding(8, 8, 8, 8);

        // Capture position and set to the TextView
        text.setText(name[position]);
        // Capture position and set to the ImageView
        imageLoader.displayImage(data[position], image);
        return vi;
    }

}
