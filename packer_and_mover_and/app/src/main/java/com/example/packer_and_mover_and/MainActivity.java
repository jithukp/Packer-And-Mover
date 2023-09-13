package com.example.packer_and_mover_and;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    EditText e1;
    Button b;
    SharedPreferences sh;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        e1=(EditText)findViewById(R.id.editText);
        b=(Button) findViewById(R.id.button);
        b.setOnClickListener(this);
        sh= PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        e1.setText(sh.getString("ip",""));
    }

    @Override
    public void onClick(View view) {
        final String ipaddress=e1.getText().toString();
        String url1="http://"+ipaddress+":4000/";
        SharedPreferences.Editor ed=sh.edit();
        ed.putString("ip",ipaddress);
        ed.putString("url",url1);
        ed.commit();
        Intent i=new Intent(getApplicationContext(),Login.class);
        startActivity(i);
    }
}
