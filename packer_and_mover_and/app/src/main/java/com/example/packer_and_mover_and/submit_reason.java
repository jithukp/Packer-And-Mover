package com.example.packer_and_mover_and;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.squareup.picasso.Picasso;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class submit_reason extends AppCompatActivity implements View.OnClickListener {
    EditText ed;
    Button b;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_submit_reason);
        ed=findViewById(R.id.ed1);
        b=findViewById(R.id.b1);

        b.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        String reason=ed.getText().toString();
        if(reason.equalsIgnoreCase("")){
            ed.setError("Required");
        } else {
            SharedPreferences sh= PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
            String ip=sh.getString("url","");
            String url=ip+"and_driver_reschedule";
            RequestQueue requestQueue1 = Volley.newRequestQueue(getApplicationContext());
            StringRequest postRequest1 = new StringRequest(Request.Method.POST, url,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            //  Toast.makeText(getApplicationContext(), response, Toast.LENGTH_LONG).show();

                            try {
                                JSONObject jsonObj = new JSONObject(response);
                                if (jsonObj.getString("status").equalsIgnoreCase("ok")) {
                                    Toast.makeText(getApplicationContext(), "Requested", Toast.LENGTH_SHORT).show();
                                    Intent ij=new Intent(getApplicationContext(), driver_home.class);
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
                    params.put("lid",sh.getString("lid",""));
                    params.put("reason", reason);


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
}
