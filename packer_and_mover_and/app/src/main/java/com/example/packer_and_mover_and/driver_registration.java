package com.example.packer_and_mover_and;

import android.Manifest;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.provider.MediaStore;
import android.provider.Settings;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class driver_registration extends AppCompatActivity implements View.OnClickListener {
    EditText e_fname, e_lname, e_phone, e_dob, e_lic, e_hname, e_place,e_post,e_city,e_district, e_state, e_pin, e_username, e_password;
    Button b1;

    String fname, lname, phone, dob, lic, hname, place, post, city, district, state, pin, username, pswd;
    SharedPreferences sh;
    ProgressDialog pd;
    String url = "",url1;
    final Calendar myCalendar= Calendar.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_driver_registration);

        e_fname = (EditText) findViewById(R.id.editText1);
        e_lname = (EditText) findViewById(R.id.editText2);
        e_phone = (EditText) findViewById(R.id.editText3);
        e_dob = (EditText) findViewById(R.id.editText4);
        e_lic = (EditText) findViewById(R.id.editText5);
        e_hname = (EditText) findViewById(R.id.editText6);
        e_place = (EditText) findViewById(R.id.editText7);
        e_post = (EditText) findViewById(R.id.editText8);
        e_city = (EditText) findViewById(R.id.editText9);
        e_district = (EditText) findViewById(R.id.editText10);
        e_state = (EditText) findViewById(R.id.editText11);
        e_pin = (EditText) findViewById(R.id.editText12);
        e_username = (EditText) findViewById(R.id.editText13);
        e_password = (EditText) findViewById(R.id.editText14);

        b1 = (Button) findViewById(R.id.button3);



        sh= PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        url1=sh.getString("url","");
        url=url1+"regform";
        DatePickerDialog.OnDateSetListener date =new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int day) {
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH,month);
                myCalendar.set(Calendar.DAY_OF_MONTH,day);
                updateLabel();
            }
        };
        e_dob.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new DatePickerDialog(driver_registration.this,date,myCalendar.get(Calendar.YEAR),myCalendar.get(Calendar.MONTH),myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });

        b1.setOnClickListener(this);

    }

    @Override
    public void onClick(View view) {
        fname = e_fname.getText().toString();
        lname = e_lname.getText().toString();
        phone = e_phone.getText().toString();
        dob = e_dob.getText().toString();
        lic = e_lic.getText().toString();
        hname = e_hname.getText().toString();
        place = e_place.getText().toString();

        post = e_post.getText().toString();
        city = e_city.getText().toString();
        district = e_district.getText().toString();
        state = e_state.getText().toString();
        pin = e_pin.getText().toString();
        username = e_username.getText().toString();
        pswd = e_password.getText().toString();


        if(fname.equals(""))
        {
            e_fname.setError("*");
            e_fname.requestFocus();
        }

        else if(phone.length()!=10)
        {
            e_phone.setError("Invalid phone number");
            e_phone.requestFocus();
        }
        else if(dob.equals(""))
        {
            e_dob.setError("*");
            e_dob.requestFocus();
        }
//        else if(lic.equals(""))
//        {
//            e_lic.setError("*");
//            e_lic.requestFocus();
//        }
        else if(hname.equals(""))
        {
            e_hname.setError("*");
            e_hname.requestFocus();
        }
        else if(place.equals(""))
        {
            e_place.setError("*");
            e_place.requestFocus();
        }
        else if(post.equals(""))
        {
            e_post.setError("*");
            e_post.requestFocus();
        }
        else if(city.equals(""))
        {
            e_city.setError("*");
            e_city.requestFocus();
        }
        else if(district.equals(""))
        {
            e_district.setError("*");
            e_district.requestFocus();
        }
        else if(state.equals(""))
        {
            e_state.setError("*");
            e_state.requestFocus();
        }
        else if(pin.equals(""))
        {
            e_pin.setError("*");
            e_pin.requestFocus();
        }
        else if(username.equals(""))
        {
            e_username.setError("*");
            e_username.requestFocus();
        }
        else if(pswd.equals(""))
        {
            e_password.setError("*");
            e_password.requestFocus();
        }
        else {

            uploadBitmap(fname, lname, phone, dob, lic, hname, place, post, city, district, state, pin, username, pswd);
        }



    }


    private void uploadBitmap(final String fname, final String lname, final String phone,  final String dob,
                              final String lic, final String hname, final String place, final String post,
                              final String city, final String district, final String state, final String pin,
                              final String username, final String password) {


        pd=new ProgressDialog(driver_registration.this);
        pd.setMessage("Uploading....");
        pd.show();
        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        StringRequest postRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        //  Toast.makeText(getApplicationContext(), response, Toast.LENGTH_LONG).show();

                        try {
                            pd.dismiss();
                            JSONObject jsonObj = new JSONObject(response);
                            if (jsonObj.getString("status").equalsIgnoreCase("ok")) {
                                Toast.makeText(getApplicationContext(), "Registered", Toast.LENGTH_SHORT).show();
                                Intent i = new Intent(getApplicationContext(), Login.class);
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

                params.put("fname", fname);//passing to python
                params.put("lname", lname);//passing to python
                params.put("phone", phone);//passing to python
                params.put("dob", dob);
                params.put("lic", lic);
                params.put("hname", hname);
                params.put("place", place);
                params.put("post", post);
                params.put("city", city);
                params.put("district", district);
                params.put("state", state);
                params.put("pin", pin);

                params.put("pswd", pswd);
                params.put("username", username);
                params.put("category", sh.getString("category", ""));

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

    private void updateLabel(){
        String myFormat="MM/dd/yy";
        SimpleDateFormat dateFormat=new SimpleDateFormat(myFormat, Locale.US);
        e_dob.setText(dateFormat.format(myCalendar.getTime()));
    }


}
