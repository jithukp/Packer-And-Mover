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

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class custom_request_items extends BaseAdapter {
    String[]cid, pname, count, amt;
    private Context context;

    public custom_request_items(Context appcontext, String[]cid1, String[]pname, String[]count, String[] amt)
    {
        this.context=appcontext;
        this.cid=cid1;
        this.pname=pname;
        this.count=count;
        this.amt=amt;
    }

    @Override
    public int getCount() {
        return cid.length;
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
            gridView=inflator.inflate(R.layout.custom_request_items,null);
        }
        else
        {
            gridView=(View)view;
        }
        TextView tv1=(TextView)gridView.findViewById(R.id.txt_date);
        TextView tv2=(TextView)gridView.findViewById(R.id.txt_cmp);
        TextView tv3=(TextView)gridView.findViewById(R.id.tv_rep);

        tv1.setTextColor(Color.BLACK);
        tv2.setTextColor(Color.BLACK);
        tv3.setTextColor(Color.BLACK);

        tv1.setText(pname[i]);
        tv2.setText(count[i]);
        tv3.setText(amt[i]);



        return gridView;

    }
}
