package com.example.appinvoicing;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;

public class main extends AppCompatActivity {
    TextView bienvenida;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        bienvenida = findViewById(R.id.tvbienvenida);
        bienvenida.setText("Bienvenid@"+getIntent().getStringExtra("myname"));

    }
}