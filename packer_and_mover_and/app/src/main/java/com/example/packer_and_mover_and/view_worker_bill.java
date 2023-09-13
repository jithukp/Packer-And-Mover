package com.example.packer_and_mover_and;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

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

public class view_worker_bill extends AppCompatActivity {
    TextView tv1, tv2, tv3, tv4;
    ImageView im;
    Button b;

    String charge="";

    SharedPreferences sh;
    String url, ip;

    @Override
    public void onBackPressed() {
        Intent ij=new Intent(getApplicationContext(), view_worker_request.class);
        startActivity(ij);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_worker_bill);

        tv1=(TextView)findViewById(R.id.txt_date);
        tv2=(TextView)findViewById(R.id.txt_cmp);
        tv3=(TextView)findViewById(R.id.tv_rep);
        tv4=(TextView)findViewById(R.id.tv_phone);
        im=(ImageView)findViewById(R.id.im1);
        b=(Button) findViewById(R.id.b1);

        im.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SharedPreferences sh=PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
                SharedPreferences.Editor ed=sh.edit();
                ed.putString("did", sh.getString("w_id", ""));
                ed.putString("rtype", "freight worker");
                ed.commit();
                Intent myIntent = new Intent(getApplicationContext(), send_rating1.class);
                startActivity(myIntent);
            }
        });

        sh=PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        sh= PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        ip=sh.getString("url","");
        url=ip+"and_view_worker_bill";
        RequestQueue requestQueue1 = Volley.newRequestQueue(getApplicationContext());
        StringRequest postRequest1 = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        //  Toast.makeText(getApplicationContext(), response, Toast.LENGTH_LONG).show();

                        try {
                            JSONObject jsonObj = new JSONObject(response);
                            if (jsonObj.getString("status").equalsIgnoreCase("ok")) {
                                String date=jsonObj.getString("date");
                                String time=jsonObj.getString("time");
                                charge=jsonObj.getString("charge");
                                String work_details=jsonObj.getString("work_details");

                                tv1.setText(date);
                                tv2.setText(time);
                                tv3.setText(charge);
                                tv4.setText(work_details);
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
                params.put("wr_id",sh.getString("wr_id",""));


                return params;
            }
        };


        int MY_SOCKET_TIMEOUT_MS1 = 100000;

        postRequest1.setRetryPolicy(new DefaultRetryPolicy(
                MY_SOCKET_TIMEOUT_MS1,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        requestQueue1.add(postRequest1);


        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SharedPreferences sh=PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
                SharedPreferences.Editor ed=sh.edit();
                ed.putString("charge", charge);
                ed.commit();
                Intent ij=new Intent(getApplicationContext(), payment2.class);
                startActivity(ij);
            }
        });
    }
}
