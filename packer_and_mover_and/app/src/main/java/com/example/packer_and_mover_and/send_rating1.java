package com.example.packer_and_mover_and;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.Button;
import android.widget.RatingBar;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class send_rating1 extends AppCompatActivity {
    RatingBar rb;
    Button b;
    SharedPreferences sh;
    String ip,url;

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_send_rating1);

        b=findViewById(R.id.button5);
        rb=findViewById(R.id.ratingBar);

        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String val=String.valueOf(rb.getRating());
                Toast.makeText(getApplicationContext(), val+"", Toast.LENGTH_SHORT).show();
                if (val.equalsIgnoreCase("0.0")) {
                    Toast.makeText(getApplicationContext(), "Choose rating", Toast.LENGTH_SHORT).show();
                } else{
                    sh= PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
                    ip=sh.getString("url","");
                    url=ip+"and_send_rating";
                    RequestQueue requestQueue1 = Volley.newRequestQueue(getApplicationContext());
                    StringRequest postRequest1 = new StringRequest(Request.Method.POST, url,
                            new Response.Listener<String>() {
                                @Override
                                public void onResponse(String response) {
                                    //  Toast.makeText(getApplicationContext(), response, Toast.LENGTH_LONG).show();

                                    try {
                                        JSONObject jsonObj = new JSONObject(response);
                                        if (jsonObj.getString("status").equalsIgnoreCase("ok")) {

                                            Toast.makeText(getApplicationContext(), "Rating sent", Toast.LENGTH_SHORT).show();
                                            Intent ij=new Intent(getApplicationContext(), user_home.class);
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
                            params.put("lid",sh.getString("lid",""));
                            params.put("did",sh.getString("did",""));
                            params.put("rtype",sh.getString("rtype",""));
                            params.put("rate", val);


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
    }
}
