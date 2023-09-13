package com.example.packer_and_mover_and;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class custom_worker extends BaseAdapter {
    String[] id, name, place, district, phone;
    private Context context;

    public custom_worker(Context appcontext, String[]id, String[]name, String[]place, String[]district, String[]phone)
    {
        this.context=appcontext;
        this.id=id;
        this.name=name;
        this.place=place;
        this.district=district;
        this.phone=phone;
    }

    @Override
    public int getCount() {
        return name.length;
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        LayoutInflater inflator=(LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View gridView;
        if(view==null)
        {
            gridView=inflator.inflate(R.layout.custom_worker,null);
        }
        else
        {
            gridView=(View)view;
        }
        TextView tv1=(TextView)gridView.findViewById(R.id.txt_date);
        TextView tv2=(TextView)gridView.findViewById(R.id.txt_cmp);
        TextView tv3=(TextView)gridView.findViewById(R.id.tv_rep);
        TextView tv4=(TextView)gridView.findViewById(R.id.tv_phone);
        ImageView im=(ImageView)gridView.findViewById(R.id.im1);

        tv1.setTextColor(Color.BLACK);
        tv2.setTextColor(Color.BLACK);
        tv3.setTextColor(Color.BLACK);
        tv4.setTextColor(Color.BLACK);

        tv1.setText(name[i]);
        tv2.setText(place[i]);
        tv3.setText(district[i]);
        tv4.setText(phone[i]);

        im.setTag(i);
        im.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int pos=(int)view.getTag();
                SharedPreferences sh= PreferenceManager.getDefaultSharedPreferences(context);
                SharedPreferences.Editor ed=sh.edit();
                ed.putString("worker_id", id[pos]);
                ed.putString("wname", name[pos]);
                ed.putString("wplace", place[pos]);
                ed.putString("wdistrict", district[pos]);
                ed.putString("wphone", phone[pos]);
                ed.commit();

                Intent ij=new Intent(context, view_worker_more.class);
                ij.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(ij);
            }
        });

        return gridView;

    }
}
