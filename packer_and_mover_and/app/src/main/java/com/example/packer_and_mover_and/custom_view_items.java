package com.example.packer_and_mover_and;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class custom_view_items extends BaseAdapter {

    private final Context context;
    String[] id, name, price;

    public custom_view_items(Context applicationContext, String[] id, String[] name, String[] price) {

        this.context = applicationContext;
        this.id = id;
        this.name = name;
        this.price = price;
    }

    @Override
    public int getCount() {
        return id.length;
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
    public View getView(final int i, View view, ViewGroup viewGroup) {
        LayoutInflater inflator = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View gridView;
        if (view == null) {
            gridView = new View(context);
            //gridView=inflator.inflate(R.layout.customview, null);
            gridView = inflator.inflate(R.layout.custom_view_items, null);//same class name

        } else {
            gridView = (View) view;

        }
        TextView tv1 = (TextView) gridView.findViewById(R.id.name);
        TextView tv2 = (TextView) gridView.findViewById(R.id.price);

        tv1.setTextColor(Color.BLACK);//color setting
        tv2.setTextColor(Color.BLACK);

        tv1.setText(name[i]);
        tv2.setText(price[i]);

        return gridView;
    }
}