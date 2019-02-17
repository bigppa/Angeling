package org.sopt.angeling.Controller;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import org.sopt.angeling.Model.Thumbnail;
import org.sopt.angeling.R;
import org.sopt.angeling.View.ViewHolder;

import java.util.ArrayList;

/**
 * Created by DongHyun on 2016-01-14.
 */

public class LCustomAdapter extends BaseAdapter {

    public ArrayList<Thumbnail> thumbnails = null;
    private LayoutInflater layoutInflater = null;
    public static Typeface mTypeface = null;//폰트
    Context Ctx;


    public LCustomAdapter(ArrayList<Thumbnail> thumbnails, Context ctx){
        this.thumbnails = thumbnails;
        this.layoutInflater = (LayoutInflater)ctx.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        Ctx = ctx;
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
    public View getView(int position, View convertView, ViewGroup parent) {

        ViewHolder viewHolder = new ViewHolder();


        if(convertView == null){

            convertView = layoutInflater.inflate(R.layout.list_list_item, parent, false);


            viewHolder.txtName_item = (TextView)convertView.findViewById(R.id.list_viewName);
            viewHolder.txtAddress_item = (TextView)convertView.findViewById(R.id.list_viewAddress);
            viewHolder.txtNumber_item = (TextView)convertView.findViewById(R.id.list_viewNumber);


            viewHolder.distance_item1 = (TextView)convertView.findViewById(R.id.distance_from_me1);
            viewHolder.txtDistance = (TextView)convertView.findViewById(R.id.list_distance_from_me2);
            viewHolder.distance_item3 = (TextView)convertView.findViewById(R.id.distance_from_me3);



            convertView.setTag(viewHolder);
        } else{
            viewHolder = (ViewHolder) convertView.getTag();
        }

        Thumbnail thumbnail = thumbnails.get(position);

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