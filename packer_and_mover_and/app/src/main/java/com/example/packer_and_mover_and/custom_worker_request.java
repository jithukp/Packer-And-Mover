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
import android.widget.Toast;

public class custom_worker_request extends BaseAdapter {
    String[] id, wid, wname, no_of_workers, status, phone;
    private Context context;

    public custom_worker_request(Context appcontext, String[]id, String[]wid, String[]wname, String[]phone, String[]no_of_workers, String[]status)
    {
        this.context=appcontext;
        this.id=id;
        this.wid=wid;
        this.wname=wname;
        this.phone=phone;
        this.no_of_workers=no_of_workers;
        this.status=status;
    }

    @Override
    public int getCount() {
        return wname.length;
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
            gridView=inflator.inflate(R.layout.custom_worker_request,null);
        }
        else
        {
            gridView=(View)view;
        }
        TextView tv1=(TextView)gridView.findViewById(R.id.txt_date);
        TextView tv2=(TextView)gridView.findViewById(R.id.txt_cmp);
        TextView tv3=(TextView)gridView.findViewById(R.id.tv_rep);
        TextView tv4=(TextView)gridView.findViewById(R.id.tv_status);
        ImageView im=(ImageView)gridView.findViewById(R.id.im);

        tv1.setTextColor(Color.BLACK);
        tv2.setTextColor(Color.BLACK);
        tv3.setTextColor(Color.BLACK);

        tv1.setText(wname[i]);
        tv2.setText(phone[i]);
        tv3.setText(no_of_workers[i]);
        tv4.setText(status[i]);



        im.setTag(i);
        im.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int pos=(int)view.getTag();
                if(status[pos].equalsIgnoreCase("approved"))
                {
                    SharedPreferences sh= PreferenceManager.getDefaultSharedPreferences(context);
                    SharedPreferences.Editor ed=sh.edit();
                    ed.putString("wr_id", id[pos]);
                    ed.putString("w_id", wid[pos]);
                    ed.commit();

                    Intent ij=new Intent(context, view_worker_bill.class);
                    ij.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    context.startActivity(ij);
                }
                else {
                    Toast.makeText(context, "Bill not available", Toast.LENGTH_SHORT).show();
                }


            }
        });

        return gridView;

    }
}
