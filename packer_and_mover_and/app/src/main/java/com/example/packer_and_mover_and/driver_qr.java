package com.example.packer_and_mover_and;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
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

public class driver_qr extends AppCompatActivity {
    ImageView im;
    Button b;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_driver_qr);
        im=findViewById(R.id.im1);
        b=findViewById(R.id.b1);

        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                driver_qr.super.onBackPressed();
            }
        });


        SharedPreferences sh= PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        String path=sh.getString("path", "");
        String url1=sh.getString("url", "")+path;
        Picasso.with(getApplicationContext()).load(url1).into(im);



    }
}
