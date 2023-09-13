package com.example.packer_and_mover_and;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.Button;
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

public class view_bill extends AppCompatActivity {
    TextView tv_date, tv_amt, tv_bdate, tv_btime, tv_bdist, tv_bcharge;
    TextView tv_total;
    Button b;

    SharedPreferences sh;
    String ip,url;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_bill);

        tv_total=findViewById(R.id.tv_total);
        tv_date=findViewById(R.id.t1);
        tv_amt=findViewById(R.id.t2);
        tv_bdate=findViewById(R.id.t3);
        tv_btime=findViewById(R.id.t4);
        tv_bdist=findViewById(R.id.t5);
        tv_bcharge=findViewById(R.id.t6);
        b=findViewById(R.id.b1);

        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent ij=new Intent(getApplicationContext(), payment.class);
                startActivity(ij);
            }
        });

        sh= PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        ip=sh.getString("url","");
        url=ip+"and_view_driver_bill";
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
                                String amt=jsonObj.getString("amt");
                                String bdate=jsonObj.getString("bdate");
                                String btime=jsonObj.getString("btime");
                                String dist=jsonObj.getString("dist");
                                String charge=jsonObj.getString("charge");
                                String tot=jsonObj.getString("tot");

                                tv_date.setTextColor(Color.BLACK);
                                tv_date.setText(date);
                                tv_amt.setTextColor(Color.BLACK);
                                tv_amt.setText(amt);
                                tv_bdate.setTextColor(Color.BLACK);
                                tv_bdate.setText(bdate);
                                tv_btime.setTextColor(Color.BLACK);
                                tv_btime.setText(btime);
                                tv_bdist.setTextColor(Color.BLACK);
                                tv_bdist.setText(dist);
                                tv_bcharge.setTextColor(Color.BLACK);
                                tv_bcharge.setText(charge);

                                tv_total.setText("Total Amount : "+tot);


                            }
                            else {
                                Toast.makeText(getApplicationContext(), "Failed", Toast.LENGTH_SHORT).show();

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
