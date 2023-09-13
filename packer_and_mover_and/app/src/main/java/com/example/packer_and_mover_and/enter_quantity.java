package com.example.packer_and_mover_and;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
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

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class enter_quantity extends AppCompatActivity {
    EditText ed;
    Button b;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_enter_quantity);
        ed=findViewById(R.id.editText15);
        b=findViewById(R.id.button2);
        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String count=ed.getText().toString();
                if(Integer.parseInt(count)<0){
                    ed.setError("Invalid count");
                } else{
                    SharedPreferences sh= PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
                    String ip=sh.getString("url","");
                    String url=ip+"user_add_items";
                    RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
                    StringRequest postRequest = new StringRequest(Request.Method.POST, url,
                            new Response.Listener<String>() {
                                @Override
                                public void onResponse(String response) {
                                    //  Toast.makeText(getApplicationContext(), response, Toast.LENGTH_LONG).show();

                                    try {
                                        JSONObject jsonObj = new JSONObject(response);
                                        if (jsonObj.getString("status").equalsIgnoreCase("ok")) {
                                Toast.makeText(getApplicationContext(), "Item added", Toast.LENGTH_SHORT).show();
                                                Intent i = new Intent(getApplicationContext(), user_home.class);
                                                startActivity(i);

                                        } else {
                                            Toast.makeText(getApplicationContext(), "Failed", Toast.LENGTH_LONG).show();
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

                            params.put("itemid", sh.getString("itemid", ""));//passing to python
                            params.put("lid", sh.getString("lid", ""));//passing to python
                            params.put("qty", count);
                            params.put("lati", Locationservice.lati);
                            params.put("logi", Locationservice.logi);


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
        });
    }
}
