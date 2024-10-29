package com.example.dsgjn;

import android.content.Intent;
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

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

public class PersonalStats extends AppCompatActivity {
    TextView label;
    String givenUserId;
    EditText input;
    Handler handler;

    Button btnGo;
    Handler handler1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.stats_personal);
        handler = new Handler(Looper.getMainLooper(), new Handler.Callback() {
            @Override
            public boolean handleMessage(@NonNull Message message) {
                givenUserId = message.getData().getString("result");
                label.setText(givenUserId);
                return true;
            }
        });
        handler1 = new Handler(Looper.getMainLooper(), new Handler.Callback() {
            @Override
            public boolean handleMessage(@NonNull Message message) {
                givenUserId = message.getData().getString("result");
                label.setText(givenUserId);
                return true;
            }
        });
        input = (EditText) findViewById(R.id.inputPS);
        label = (TextView) findViewById(R.id.label);
        btnGo = (Button) findViewById(R.id.btnGo);
        btnGo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //with threads
                String txt = input.getText().toString();
                ThreadPersStat t1 = new ThreadPersStat(txt,handler);
                t1.start();
                ThreadComp t2 = new ThreadComp(txt,handler1);


                Log.d("Thread started",String.valueOf(t1.isAlive()));
                try {
                    t1.join();
                    String [] resultsPerStat = t1.getPersStats();
                    t2.start();
                    t2.join();
                    String [] ResComp = t2.getComp();
                    Log.e("THREAD GS",String.valueOf(ResComp[0]));
                    Intent gotoInfoAct = new Intent(PersonalStats.this, ViewStats.class);
                    gotoInfoAct.putExtra("ResPersStat",resultsPerStat);
                    gotoInfoAct.putExtra("Name","PersonalStats");
                    gotoInfoAct.putExtra("ResComp", ResComp);
                    startActivity(gotoInfoAct);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }

            }
        });


    }
}
