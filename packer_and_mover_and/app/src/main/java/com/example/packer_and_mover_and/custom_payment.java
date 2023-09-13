package com.example.packer_and_mover_and;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class custom_payment extends BaseAdapter {
    String[] name, date, amount, phone;
    private Context context;

    public custom_payment(Context appcontext, String[]date, String[]name, String[]amount, String[] phone)
    {
        this.context=appcontext;
        this.date=date;
        this.name=name;
        this.amount=amount;
        this.phone=phone;
    }

    @Override
    public int getCount() {
        return date.length;
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
            gridView=inflator.inflate(R.layout.custom_payment,null);
        }
        else
        {
            gridView=(View)view;
        }
        TextView tv1=(TextView)gridView.findViewById(R.id.txt_date);
        TextView tv2=(TextView)gridView.findViewById(R.id.txt_cmp);
        TextView tv3=(TextView)gridView.findViewById(R.id.tv_rep);
        TextView tv4=(TextView)gridView.findViewById(R.id.tv_phone);

        tv1.setTextColor(Color.BLACK);
        tv2.setTextColor(Color.BLACK);
        tv3.setTextColor(Color.BLACK);
        tv4.setTextColor(Color.BLACK);

        tv1.setText(date[i]);
        tv2.setText(name[i]);
        tv3.setText(amount[i]);
        tv4.setText(phone[i]);



        return gridView;

    }
}
