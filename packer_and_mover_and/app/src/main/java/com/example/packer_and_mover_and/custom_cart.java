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

public class custom_cart extends BaseAdapter {
    String[]cid, pname, count, amt;
    private Context context;

    public custom_cart(Context appcontext, String[]cid1, String[]pname, String[]count, String[] amt)
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
            gridView=inflator.inflate(R.layout.custom_cart,null);
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

        ImageView im=(ImageView) gridView.findViewById(R.id.img_del);
        im.setTag(i);
        im.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final int pos=(int)view.getTag();
                SharedPreferences sh= PreferenceManager.getDefaultSharedPreferences(context);
                String hu = sh.getString("url", "");
                String url = hu + "and_delete_cart";

                RequestQueue requestQueue = Volley.newRequestQueue(context);
                StringRequest postRequest = new StringRequest(Request.Method.POST, url,
                        new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                //  Toast.makeText(context, response, Toast.LENGTH_LONG).show();

                                try {
                                    JSONObject jsonObj = new JSONObject(response);
                                    if (jsonObj.getString("status").equalsIgnoreCase("ok")) {
                                        Toast.makeText(context,"Deleted successfully", Toast.LENGTH_SHORT).show();
                                        Intent ij=new Intent(context, view_cart.class);
                                        ij.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                        context.startActivity(ij);

                                    }
                                    else {
                                        Toast.makeText(context, "Not found", Toast.LENGTH_LONG).show();
                                    }

                                }    catch (Exception e) {
                                    Toast.makeText(context, "Error" + e.getMessage().toString(), Toast.LENGTH_SHORT).show();
                                }
                            }
                        },
                        new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                // error
                                Toast.makeText(context, "eeeee" + error.toString(), Toast.LENGTH_SHORT).show();
                            }
                        }
                ) {
                    @Override
                    protected Map<String, String> getParams() {
                        SharedPreferences sh = PreferenceManager.getDefaultSharedPreferences(context);
                        Map<String, String> params = new HashMap<String, String>();

                        params.put("rd_id",cid[pos]);

                        return params;
                    }
                };

                int MY_SOCKET_TIMEOUT_MS=100000;

                postRequest.setRetryPolicy(new DefaultRetryPolicy(
                        MY_SOCKET_TIMEOUT_MS,
                        DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                        DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
                requestQueue.add(postRequest);
            }
        });

        return gridView;

    }
}
