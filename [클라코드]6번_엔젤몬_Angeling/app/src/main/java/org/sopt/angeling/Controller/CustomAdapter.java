package org.sopt.angeling.Controller;

/**
 * Created by DongHyun on 2016-01-13.
 */
import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import org.sopt.angeling.Model.Thumbnail;
import org.sopt.angeling.R;
import org.sopt.angeling.View.ViewHolder;

import java.util.ArrayList;
public class CustomAdapter extends BaseAdapter{

    public ArrayList<Thumbnail> thumbnails = null;
    private LayoutInflater layoutInflater = null;
    public static Typeface mTypeface = null;//폰트
    Context Ctx;
    MarkerListener listener;

    public CustomAdapter(ArrayList<Thumbnail> thumbnails, Context ctx, MarkerListener listener){
        this.thumbnails = thumbnails;
        this.layoutInflater = (LayoutInflater)ctx.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        Ctx = ctx;
        this.listener = listener;
    }

    public void setthumbnails(ArrayList<Thumbnail> thumbnails){
        this.thumbnails = thumbnails;
        this.notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return (thumbnails != null) ? thumbnails.size() : 0;
    }

    @Override
    public Object getItem(int position) {
        return (thumbnails != null && (0 <= position && position < thumbnails.size()) ? thumbnails.get(position) : null );
    }

    @Override
    public long getItemId(int position) {
        return (thumbnails != null && (0 <= position && position < thumbnails.size()) ? position : 0);
    }
    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        ViewHolder viewHolder = new ViewHolder();
        final Thumbnail thumbnail = thumbnails.get(position);

        if(convertView == null){

            convertView = layoutInflater.inflate(R.layout.list_item, parent, false);


            ImageView markerSettingimg = (ImageView)convertView.findViewById(R.id.setMarkerButton);
            markerSettingimg.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.pickMarker(thumbnails.get(position));
                }
            });

            viewHolder.txtName_item = (TextView)convertView.findViewById(R.id.viewName);
            viewHolder.txtAddress_item = (TextView)convertView.findViewById(R.id.viewAddress);
            viewHolder.txtNumber_item = (TextView)convertView.findViewById(R.id.viewNumber);

            viewHolder.distance_item1 = (TextView)convertView.findViewById(R.id.distance_from_me1);
            viewHolder.txtDistance = (TextView)convertView.findViewById(R.id.loc_distance_from_me2);
            viewHolder.distance_item3 = (TextView)convertView.findViewById(R.id.distance_from_me3);



            viewHolder.setmarker_img = (ImageView)convertView.findViewById(R.id.setMarkerButton);
            viewHolder.setmarker_img.setFocusable(false);
            convertView.setFocusable(false);

            convertView.setTag(viewHolder);
        }

        else{
            viewHolder = (ViewHolder)convertView.getTag();
        }



        Typeface font = Typeface.createFromAsset(Ctx.getAssets(), "fonts/BMJUA_ttf.ttf");

        viewHolder.txtName_item.setText(thumbnail.progrmSj);
        viewHolder.txtName_item.setTypeface(font);
        viewHolder.txtAddress_item.setText(thumbnail.postAdres);
        viewHolder.txtAddress_item.setTypeface(font);
        viewHolder.txtNumber_item.setText(thumbnail.astelno);
        viewHolder.txtNumber_item.setTypeface(font);
        viewHolder.txtDistance.setText(String.format("%.1f", thumbnail.distance));

        viewHolder.distance_item1.setTypeface(font);
        viewHolder.txtDistance.setTypeface(font);
        viewHolder.distance_item3.setTypeface(font);


        if (position%2 == 1) {
            convertView.setBackgroundColor(Color.parseColor("#F9F4F5"));
        }
        else {
            convertView.setBackgroundColor(Color.parseColor("#FFFFFF"));
        }

        return convertView;
    }
}