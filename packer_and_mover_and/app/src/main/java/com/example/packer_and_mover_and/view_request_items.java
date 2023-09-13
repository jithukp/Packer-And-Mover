package com.example.packer_and_mover_and;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TableLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

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

public class view_request_items extends AppCompatActivity {
    ListView lv;
    String[] id, pname, count, amount;
    SharedPreferences sh;
    String ip,url;
    TableLayout t1;
    TextView tv_name, tv_phone;
    Button b, b2;
    ImageView im2;
    String lati="", logi="", did="";

    @Override
    public void onBackPressed() {
        Intent ij=new Intent(getApplicationContext(), view_request.class);
        startActivity(ij);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_request_items);
        lv=findViewById(R.id.lv);
        tv_name=findViewById(R.id.tv1);
        tv_phone=findViewById(R.id.tv2);
        sh=PreferenceManager.getDefaultSharedPreferences(getApplicationContext());

        im2=findViewById(R.id.im2);
        im2.setEnabled(false);
        im2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SharedPreferences sh=PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
                SharedPreferences.Editor ed=sh.edit();
                ed.putString("did", did);
                ed.putString("rtype", "driver");
                ed.commit();
                Intent myIntent = new Intent(getApplicationContext(), send_rating1.class);
                startActivity(myIntent);
            }
        });

        b=findViewById(R.id.b1);
        b.setEnabled(false);
        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String url="http://maps.google.com/?q=" + lati + "," + logi;
                Intent myIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                startActivity(myIntent);
            }
        });


        b2=findViewById(R.id.b2);
        b2.setEnabled(false);
        b2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                scanQR(view);
            }
        });
        if(sh.getString("status","").equalsIgnoreCase("delivered")){
            b2.setVisibility(View.INVISIBLE);
            b.setVisibility(View.INVISIBLE);
         
        }
        t1=findViewById(R.id.t1);

        sh= PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        ip=sh.getString("url","");
        url=ip+"and_view_request_items";
        RequestQueue requestQueue1 = Volley.newRequestQueue(getApplicationContext());
        StringRequest postRequest1 = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        //  Toast.makeText(getApplicationContext(), response, Toast.LENGTH_LONG).show();

                        try {
                            JSONObject jsonObj = new JSONObject(response);
                            if (jsonObj.getString("status").equalsIgnoreCase("ok")) {
                                tv_name.setText(jsonObj.getString("dname"));
                                tv_phone.setText(jsonObj.getString("dphone"));
                                if(jsonObj.getString("stat").equalsIgnoreCase("yes")){
                                    b.setEnabled(true);
                                    b2.setEnabled(true);
                                    im2.setEnabled(true);
                                    lati=jsonObj.getString("lati");
                                    logi=jsonObj.getString("logi");
                                    did=jsonObj.getString("did");
                                }


                                JSONArray js= jsonObj.getJSONArray("data");//from python
                                id=new String[js.length()];
                                pname=new String[js.length()];
                                amount=new String[js.length()];
                                count=new String[js.length()];

                                for(int i=0;i<js.length();i++) {
                                    JSONObject u = js.getJSONObject(i);
                                    id[i] = u.getString("ur_id");//dbcolumn name
                                    pname[i] = u.getString("itemname");//dbcolumn name
                                    amount[i] = u.getString("amount");//dbcolumn name
                                    count[i] = u.getString("item_count");//dbcolumn name
                                }
                                lv.setAdapter(new custom_request_items(getApplicationContext(),id, pname, count, amount));//custom_view_service.xml and li is the listview object


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
                params.put("rd_id",sh.getString("rd_id",""));


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





    /////               QR SCANNING
    static final String ACTION_SCAN = "com.google.zxing.client.android.SCAN";
    public void scanQR(View v) {
        try {
            Intent intent = new Intent(ACTION_SCAN);
            intent.putExtra("SCAN_MODE", "QR_CODE_MODE");
            startActivityForResult(intent, 0);
        } catch (ActivityNotFoundException anfe) {
            showDialog(view_request_items.this, "No Scanner Found", "Download a scanner code activity?", "Yes", "No").show();
        }
    }

    private static AlertDialog showDialog(final Activity act, CharSequence title, CharSequence message, CharSequence buttonYes, CharSequence buttonNo) {
        AlertDialog.Builder downloadDialog = new AlertDialog.Builder(act);
        downloadDialog.setTitle(title);
        downloadDialog.setMessage(message);
        downloadDialog.setPositiveButton(buttonYes, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialogInterface, int i) {
                Uri uri = Uri.parse("market://search?q=pname:" + "com.google.zxing.client.android");
                Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                try {
                    act.startActivity(intent);
                } catch (ActivityNotFoundException anfe) {

                }
            }
        });
        downloadDialog.setNegativeButton(buttonNo, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialogInterface, int i) {
            }
        });
        return downloadDialog.show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 0) {
            if (resultCode == RESULT_OK) {
                String contents = data.getStringExtra("SCAN_RESULT");
                sh= PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
                ip=sh.getString("url","");
                url=ip+"and_scan_driver_qr";
                RequestQueue requestQueue1 = Volley.newRequestQueue(getApplicationContext());
                StringRequest postRequest1 = new StringRequest(Request.Method.POST, url,
                        new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                //  Toast.makeText(getApplicationContext(), response, Toast.LENGTH_LONG).show();

                                try {
                                    JSONObject jsonObj = new JSONObject(response);
                                    if (jsonObj.getString("status").equalsIgnoreCase("ok")) {

                                        Toast.makeText(getApplicationContext(), "QR code scanned successfully", Toast.LENGTH_SHORT).show();
                                        SharedPreferences.Editor ed=sh.edit();
                                        ed.putString("wid", jsonObj.getString("wid"));
                                        ed.commit();
                                        Intent ij=new Intent(getApplicationContext(), view_bill.class);
                                        startActivity(ij);

                                    }
                                    else {
                                Toast.makeText(getApplicationContext(), "Invalid QR code scanned", Toast.LENGTH_SHORT).show();

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
                        params.put("qr", contents);
                        params.put("rd_id", sh.getString("rd_id", ""));


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
}
