package com.example.packer_and_mover_and;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
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

public class payment2 extends AppCompatActivity implements View.OnClickListener {
    EditText ed_bank, ed_acc, ed_ifsc;
    Button b;

    @Override
    public void onBackPressed() {
        Intent ij=new Intent(getApplicationContext(), view_worker_bill.class);
        startActivity(ij);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment2);

        ed_bank=findViewById(R.id.ed1);
        ed_acc=findViewById(R.id.ed2);
        ed_ifsc=findViewById(R.id.ed3);
        b=findViewById(R.id.b1);
        b.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        String bank=ed_bank.getText().toString();
        String acc=ed_acc.getText().toString();
        String ifsc=ed_ifsc.getText().toString();
        if(bank.equalsIgnoreCase("")){
            ed_bank.setError("Required");
        } else if(acc.equalsIgnoreCase("")){
            ed_acc.setError("Required");
        } else if(ifsc.equalsIgnoreCase("")){
            ed_ifsc.setError("Required");
        } else {
            SharedPreferences sh= PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
            String ip=sh.getString("url","");
            String url=ip+"and_payment2";
            RequestQueue requestQueue1 = Volley.newRequestQueue(getApplicationContext());
            StringRequest postRequest1 = new StringRequest(Request.Method.POST, url,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            //  Toast.makeText(getApplicationContext(), response, Toast.LENGTH_LONG).show();

                            try {
                                JSONObject jsonObj = new JSONObject(response);
                                if (jsonObj.getString("status").equalsIgnoreCase("ok")) {

                                    Toast.makeText(getApplicationContext(), "Paid successfully", Toast.LENGTH_SHORT).show();
                                    Intent ij=new Intent(getApplicationContext(), view_worker_request.class);
                                    startActivity(ij);

                                } else if(jsonObj.getString("status").equalsIgnoreCase("insuff")){
                                    Toast.makeText(getApplicationContext(), "Insufficient Balance", Toast.LENGTH_SHORT).show();
                                }
                                else {
                                    Toast.makeText(getApplicationContext(), "Invalid Details", Toast.LENGTH_SHORT).show();

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
                    params.put("bank", bank);
                    params.put("acc", acc);
                    params.put("ifsc", ifsc);
                    params.put("wr_id", sh.getString("wr_id", ""));


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
