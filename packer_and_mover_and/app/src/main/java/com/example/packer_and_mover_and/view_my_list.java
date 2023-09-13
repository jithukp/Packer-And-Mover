package com.example.packer_and_mover_and;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.ListView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class view_my_list extends AppCompatActivity {
    FloatingActionButton fab;
    ListView lv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_my_list);
        lv=findViewById(R.id.lv);

    }
}
