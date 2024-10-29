package com.example.dsgjn;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.net.Socket;

public class MainActivity extends AppCompatActivity {

    Button btnAnR;
    Socket socket ;
    Button btnPersStat;
    ThreadUI thread;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnAnR = (Button) findViewById(R.id.buttonAR);
        btnPersStat = (Button) findViewById(R.id.buttonPS);

        btnAnR.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent gotoAnalyzeRoute = new Intent(MainActivity.this, AnalyzeRoute.class);
                gotoAnalyzeRoute.putExtra("Socket", (Serializable) socket);
                startActivity(gotoAnalyzeRoute);

            }
        });

        btnPersStat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent gotoPersonalStats = new Intent(MainActivity.this, PersonalStats.class);
               startActivity(gotoPersonalStats);
            }
        });

    }

}