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

public class custom_request_status extends BaseAdapter {
    String[] id, cdate, amount, status;
    private Context context;

    public custom_request_status(Context appcontext, String[]id, String[]cdate, String[]amount, String[]status)
    {
        this.context=appcontext;
        this.id=id;
        this.cdate=cdate;
        this.amount=amount;
        this.status=status;
    }

    @Override
    public int getCount() {
        return cdate.length;
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
            gridView=inflator.inflate(R.layout.custom_request_status,null);
        }
        else
        {
            gridView=(View)view;
        }
        TextView tv1=(TextView)gridView.findViewById(R.id.txt_date);
        TextView tv2=(TextView)gridView.findViewById(R.id.txt_cmp);
        TextView tv3=(TextView)gridView.findViewById(R.id.tv_rep);
        ImageView im=(ImageView)gridView.findViewById(R.id.im);

        tv1.setTextColor(Color.BLACK);
        tv2.setTextColor(Color.BLACK);
        tv3.setTextColor(Color.BLACK);

        tv1.setText(cdate[i]);
        tv2.setText(amount[i]);
        tv3.setText(status[i]);

        im.setTag(i);
        im.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int pos=(int)view.getTag();
                SharedPreferences sh= PreferenceManager.getDefaultSharedPreferences(context);
                SharedPreferences.Editor ed=sh.edit();
                ed.putString("rd_id", id[pos]);
                ed.putString("status", status[pos]);
                ed.commit();

                Intent ij=new Intent(context, view_request_items.class);
                ij.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(ij);
            }
        });

        return gridView;

    }
}
