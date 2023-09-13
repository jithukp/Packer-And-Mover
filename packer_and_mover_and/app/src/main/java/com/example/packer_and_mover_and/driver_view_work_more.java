package com.example.packer_and_mover_and;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
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
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class driver_view_work_more extends AppCompatActivity {
    TextView tv_name, tv_phone, tv_address;
    Button bt_locate, bt_reschedule, bt_submit;
    EditText ed_dist, ed_charge;

    SharedPreferences sh;
    String ip,url, lati="", logi="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_driver_view_work_more);

        tv_name=findViewById(R.id.tv1);
        tv_phone=findViewById(R.id.tv2);
        tv_address=findViewById(R.id.tv3);

        ed_dist=findViewById(R.id.ed1);
        ed_charge=findViewById(R.id.ed2);

        //  locating user
        bt_locate=findViewById(R.id.b1);
        bt_locate.setEnabled(false);
        bt_locate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String url="http://maps.google.com/maps?q=" + lati + "," + logi;
                Intent myIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                startActivity(myIntent);
            }
        });




        //  rescheduling
        bt_reschedule=findViewById(R.id.b2);
        bt_reschedule.setEnabled(false);
        bt_reschedule.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent myIntent = new Intent(getApplicationContext(), submit_reason.class);
                startActivity(myIntent);
            }
        });

        bt_submit=findViewById(R.id.b3);
        bt_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String dist=ed_dist.getText().toString();
                String charge=ed_charge.getText().toString();
                if(dist.equalsIgnoreCase("")||(dist.equalsIgnoreCase("0"))){
                    ed_dist.setError("Invalid value for distance");
                } else if(charge.equalsIgnoreCase("")|| charge.equalsIgnoreCase("0")){
                    ed_charge.setError("Invalid value for charge");
                } else {
                    sh= PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
                    ip=sh.getString("url","");
                    url=ip+"driver_add_bill";
                    RequestQueue requestQueue1 = Volley.newRequestQueue(getApplicationContext());
                    StringRequest postRequest1 = new StringRequest(Request.Method.POST, url,
                            new Response.Listener<String>() {
                                @Override
                                public void onResponse(String response) {
                                    //  Toast.makeText(getApplicationContext(), response, Toast.LENGTH_LONG).show();

                                    try {
                                        JSONObject jsonObj = new JSONObject(response);
                                        if (jsonObj.getString("status").equalsIgnoreCase("ok")) {

                                            String name=jsonObj.getString("path");
                                            SharedPreferences.Editor ed=sh.edit();
                                            ed.putString("path", name);
                                            ed.commit();
                                            Intent ij=new Intent(getApplicationContext(), driver_qr.class);
                                            startActivity(ij);
                                        }
                                        else {
//                                Toast.makeText(getApplicationContext(), jsonObj.getString("status"), Toast.LENGTH_SHORT).show();

                                        }

                                    } catch (Exception e) {
                                        Toast.makeText(getApplicationContext(), "Error" + e.getMessage().toString(), Toast.LENGTH_SHORT).show();
                                    }
                                }
                            },
                            new Response.ErrorListener() {
                                @Override
                                public void onErrorResponse(VolleyError error) {
                                    // error
                                    Toast.makeText(getApplicationContext(), "eeeee" + error.toString(), Toast.LENGTH_SHORT).show();
                                }
                            }
                    ) {

                        //                value Passing android to python
                        @Override
                        protected Map<String, String> getParams() {
                            SharedPreferences sh = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
                            Map<String, String> params = new HashMap<String, String>();
//                params.put("wid", ww);//passing to python
//                params.put("w", sh.getString("ww",""));
                            params.put("wid",sh.getString("wid",""));
                            params.put("dist", dist);
                            params.put("charge", charge);


                            return params;
                        }
                    };


                    int MY_SOCKET_TIMEOUT_MS1 = 100000;

                    postRequest1.setRetryPolicy(new DefaultRetryPolicy(
                            MY_SOCKET_TIMEOUT_MS1,
                            DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                            DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
                    requestQueue1.add(postRequest1);


                }
            }
        });


        sh= PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        ip=sh.getString("url","");
        url=ip+"driver_view_works_more";
        RequestQueue requestQueue1 = Volley.newRequestQueue(getApplicationContext());
        StringRequest postRequest1 = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        //  Toast.makeText(getApplicationContext(), response, Toast.LENGTH_LONG).show();

                        try {
                            JSONObject jsonObj = new JSONObject(response);
                            if (jsonObj.getString("status").equalsIgnoreCase("ok")) {

                                String name=jsonObj.getString("name");
                                tv_name.setTextColor(Color.BLACK);
                                tv_name.setText(name);
                                String phone=jsonObj.getString("phone");
                                tv_phone.setTextColor(Color.BLACK);
                                tv_phone.setText(phone);
                                String address=jsonObj.getString("address");
                                tv_address.setTextColor(Color.BLACK);
                                tv_address.setText(address);

                                lati=jsonObj.getString("lati");
                                logi=jsonObj.getString("logi");
                                bt_locate.setEnabled(true);
                                bt_reschedule.setEnabled(true);

                            }
                            else {
//                                Toast.makeText(getApplicationContext(), jsonObj.getString("status"), Toast.LENGTH_SHORT).show();

                            }

                        } catch (Exception e) {
                            Toast.makeText(getApplicationContext(), "Error" + e.getMessage().toString(), Toast.LENGTH_SHORT).show();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // error
                        Toast.makeText(getApplicationContext(), "eeeee" + error.toString(), Toast.LENGTH_SHORT).show();
                    }
                }
        ) {

            //                value Passing android to python
            @Override
            protected Map<String, String> getParams() {
                SharedPreferences sh = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
                Map<String, String> params = new HashMap<String, String>();
//                params.put("wid", ww);//passing to python
//                params.put("w", sh.getString("ww",""));
                params.put("wid",sh.getString("wid",""));


                return params;
            }
        };


        int MY_SOCKET_TIMEOUT_MS1 = 100000;

        postRequest1.setRetryPolicy(new DefaultRetryPolicy(
                MY_SOCKET_TIMEOUT_MS1,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        requestQueue1.add(postRequest1);


    }
}
