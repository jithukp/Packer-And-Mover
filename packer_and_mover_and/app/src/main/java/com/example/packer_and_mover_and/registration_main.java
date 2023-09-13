package com.example.packer_and_mover_and;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.TableRow;

public class registration_main extends AppCompatActivity {
    TableRow t1, t2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration_main);
        t1=findViewById(R.id.t1);
        t2=findViewById(R.id.t2);


        t1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SharedPreferences sh= PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
                SharedPreferences.Editor ed=sh.edit();
                ed.putString("category", "driver");
                ed.commit();
                Intent ij=new Intent(getApplicationContext(), driver_registration.class);
                startActivity(ij);
            }
        });


        t2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SharedPreferences sh= PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
                SharedPreferences.Editor ed=sh.edit();
                ed.putString("category", "user");
                ed.commit();
                Intent ij=new Intent(getApplicationContext(), driver_registration.class);
                startActivity(ij);
            }
        });

    }
}
