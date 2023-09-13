package com.example.packer_and_mover_and;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
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

public class Login extends AppCompatActivity implements View.OnClickListener {
    EditText e1,e2;
    Button b1;
    TextView t1;
    SharedPreferences sh;
    String url,ip;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        e1=(EditText)findViewById(R.id.uname);
        e2=(EditText)findViewById(R.id.pwd);
        t1=(TextView) findViewById(R.id.reg);
        b1=(Button)findViewById(R.id.loginbtn);
        b1.setOnClickListener(this);
        t1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent iii=new Intent(getApplicationContext(),registration_main.class);
                startActivity(iii);
            }
        });

        sh= PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        ip=sh.getString("url","");
        url=ip+"andlogin";

        e1.setText(sh.getString("s1", ""));
        e2.setText(sh.getString("s2", ""));

    }

    @Override
    public void onClick(View view) {

        final String s1=e1.getText().toString();
        final String s2=e2.getText().toString();
        SharedPreferences.Editor ed=sh.edit();
        ed.putString("s1", s1);
        ed.putString("s2", s2);
        ed.commit();

        if(s1.equals(""))
        {
            e1.setError("*");
            e1.requestFocus();
        }
        else if(s2.equals(""))
        {
            e2.setError("*");
            e2.requestFocus();
        }
        else {
            RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
            StringRequest postRequest = new StringRequest(Request.Method.POST, url,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            //  Toast.makeText(getApplicationContext(), response, Toast.LENGTH_LONG).show();

                            try {
                                JSONObject jsonObj = new JSONObject(response);
                                if (jsonObj.getString("status").equalsIgnoreCase("ok")) {
//                                Toast.makeText(Login.this, "welcome", Toast.LENGTH_SHORT).show();
                                    String typ = jsonObj.getString("type");
                                    String id = jsonObj.getString("id");
                                    SharedPreferences.Editor ed = sh.edit();
                                    ed.putString("lid", id);
                                    ed.putString("usertype", typ);
                                    ed.commit();
                                    if (typ.equalsIgnoreCase("user")) {
                                        Toast.makeText(getApplicationContext(), "Welcome user", Toast.LENGTH_LONG).show();
//
                                        Intent ij = new Intent(getApplicationContext(), Locationservice.class);
                                        startService(ij);

                                        Intent i = new Intent(getApplicationContext(), user_home.class);
                                        startActivity(i);
                                    }
                                    if (typ.equalsIgnoreCase("driver")) {
                                        Toast.makeText(getApplicationContext(), "Welcome driver", Toast.LENGTH_LONG).show();
//
                                        Intent ij = new Intent(getApplicationContext(), Locationservice.class);
                                        startService(ij);

                                        Intent i = new Intent(getApplicationContext(), driver_home.class);
                                        startActivity(i);
                                    }

                                } else if (jsonObj.getString("status").equalsIgnoreCase("rej")) {
                                    Toast.makeText(getApplicationContext(), "This account has not been approved", Toast.LENGTH_LONG).show();
                                } else {
                                    Toast.makeText(getApplicationContext(), "Invalid details", Toast.LENGTH_LONG).show();
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

                    params.put("username", s1);//passing to python
                    params.put("password", s2);


                    return params;
                }
            };


            int MY_SOCKET_TIMEOUT_MS = 100000;

            postRequest.setRetryPolicy(new DefaultRetryPolicy(
                    MY_SOCKET_TIMEOUT_MS,
                    DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                    DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
            requestQueue.add(postRequest);
        }
    }
}
