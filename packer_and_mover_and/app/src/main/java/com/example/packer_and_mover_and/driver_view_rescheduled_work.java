package com.example.packer_and_mover_and;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

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

public class driver_view_rescheduled_work extends AppCompatActivity {
    ListView lv;
    String[] id, cdate, cname, phone;
    SharedPreferences sh;
    String ip,url;

    @Override
    public void onBackPressed() {
        Intent ij=new Intent(getApplicationContext(), driver_home.class);
        startActivity(ij);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_driver_view_work);
        lv=findViewById(R.id.lv);

        sh= PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        ip=sh.getString("url","");
        url=ip+"driver_view_rescheduled_works";
        RequestQueue requestQueue1 = Volley.newRequestQueue(getApplicationContext());
        StringRequest postRequest1 = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        //  Toast.makeText(getApplicationContext(), response, Toast.LENGTH_LONG).show();

                        try {
                            JSONObject jsonObj = new JSONObject(response);
                            if (jsonObj.getString("status").equalsIgnoreCase("ok")) {

                                JSONArray js= jsonObj.getJSONArray("data");//from python
                                id=new String[js.length()];
                                cdate=new String[js.length()];
                                cname=new String[js.length()];
                                phone=new String[js.length()];

                                for(int i=0;i<js.length();i++) {
                                    JSONObject u = js.getJSONObject(i);
                                    id[i] = u.getString("w_id");//dbcolumn name
                                    cdate[i] = u.getString("date");//dbcolumn name
                                    cname[i] = u.getString("first_name")+" "+u.getString("last_name");//dbcolumn name
                                    phone[i] = u.getString("phone_no");//dbcolumn name
                                }
                                lv.setAdapter(new custom_work(getApplicationContext(),id, cdate, cname, phone));//custom_view_service.xml and li is the listview object


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
