package com.example.packer_and_mover_and;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
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

public class view_cart extends AppCompatActivity implements View.OnClickListener {
    ListView lv;
    TextView tv;
    Button b;
    SharedPreferences sh;
    String ip,url;
    String[]cid, pname, count, amt;

    @Override
    public void onBackPressed() {
        Intent ij=new Intent(getApplicationContext(), user_home.class);
        startActivity(ij);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_cart);
        lv=findViewById(R.id.lv);
        tv=findViewById(R.id.textView5);
        b=findViewById(R.id.button4);
        b.setOnClickListener(this);

        sh= PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        ip=sh.getString("url","");
        url=ip+"and_view_cart";
        RequestQueue requestQueue1 = Volley.newRequestQueue(getApplicationContext());
        StringRequest postRequest1 = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        //  Toast.makeText(getApplicationContext(), response, Toast.LENGTH_LONG).show();

                        try {
                            JSONObject jsonObj = new JSONObject(response);
                            if (jsonObj.getString("status").equalsIgnoreCase("ok")) {
                                String tot=jsonObj.getString("amt");
                                tv.setText("Total Amount : "+tot);

                                JSONArray js= jsonObj.getJSONArray("data");//from python
                                cid=new String[js.length()];
                                pname=new String[js.length()];
                                count=new String[js.length()];
                                amt=new String[js.length()];

                                for(int i=0;i<js.length();i++) {
                                    JSONObject u = js.getJSONObject(i);
                                    cid[i] = u.getString("rd_id");//dbcolumn name
                                    pname[i] = u.getString("itemname");//dbcolumn name
                                    count[i] = u.getString("item_count");//dbcolumn name
                                    amt[i] = u.getString("amount");//dbcolumn name
                                }
                                lv.setAdapter(new custom_cart(getApplicationContext(),cid, pname, count, amt));//custom_view_service.xml and li is the listview object


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

    @Override
    public void onClick(View view) {

        sh= PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        ip=sh.getString("url","");
        url=ip+"and_book_request";
        RequestQueue requestQueue1 = Volley.newRequestQueue(getApplicationContext());
        StringRequest postRequest1 = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        //  Toast.makeText(getApplicationContext(), response, Toast.LENGTH_LONG).show();

                        try {
                            JSONObject jsonObj = new JSONObject(response);
                            if (jsonObj.getString("status").equalsIgnoreCase("ok")) {
                                String cnt=jsonObj.getString("cnt");
                                SharedPreferences.Editor ed=sh.edit();
                                ed.putString("rid",cnt);
                                ed.commit();
                                Toast.makeText(getApplicationContext(), "Request sent", Toast.LENGTH_SHORT).show();
                                Intent ij=new Intent(getApplicationContext(), view_worker.class);
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
                params.put("amt", tv.getText().toString());
                params.put("lid",sh.getString("lid",""));
                params.put("lati", Locationservice.lati);
                params.put("logi", Locationservice.logi);


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
