package com.example.dsgjn;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NavUtils;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import java.io.File;
import java.io.IOException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

public class ViewInformationActivity extends AppCompatActivity {

    TextView distText, speedText, eleText, timeText;
    Button backBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.view_information_activity);

        backBtn = (Button) findViewById(R.id.backBtn);
        backBtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
//                NavUtils.navigateUpFromSameTask(ViewInformationActivity.this);
                finish();
            }
        });

        if (getIntent().getExtras() != null){
            if (getIntent().getStringExtra("Name").equals("AnalyzeRoute")){
                String[] ReceivedResults = getIntent().getStringArrayExtra("RouteRes");
                Log.d("RouteRes", ReceivedResults[0]);

                String[] analysisRes = printResult(ReceivedResults);

                distText = (TextView) findViewById(R.id.distText);
                distText.setText(analysisRes[0]);

                speedText = (TextView) findViewById(R.id.speedText);
                speedText.setText(analysisRes[1]);

                eleText = (TextView) findViewById(R.id.eleText);
                eleText.setText(analysisRes[2]);

                timeText = (TextView) findViewById(R.id.timeText);
                timeText.setText(analysisRes[3]);
            }

        }


    }


    public String[] printResult (String[] SResults){

        if (SResults.length == 4){
            Double[] results = new Double[4];

            for (int i = 0; i < 4; i++){
                results[i] = 0.0;
                if (SResults[i] != null){
                    results[i] = Double.parseDouble(SResults[i]);
                }
            }

            double totalTime = results[3];
            int seconds = (int) totalTime % 60;
            int minutes = (int) totalTime / 60;
            int hours = 0;
            if (minutes >= 60) {
                hours = minutes / 60;
                minutes = minutes % 60;
            }
            String countingMeasure = "m";
            String countingMeasureSpeed = "m/sec";
            if (minutes > 0) {
                results[2] = results[0] / minutes;
                countingMeasureSpeed = "m/min";
            }
            if (results[0] / 1000 > 1) {
                results[0] = results[0] / 1000; //convert to km
                countingMeasure = "km";
            }
            if (hours > 0) {
                results[2] = results[0] / hours;
                countingMeasureSpeed = "km/h";
            }
            results[0] = Math.round(results[0] * 100.0) / 100.0;
            results[1] = Math.round(results[1] * 100.0) / 100.0;
            results[2] = Math.round(results[2] * 100.0) / 100.0;

            String giveDist = "Distance: " + String.valueOf(results[0] + countingMeasure);
            String giveAvgSpeed = "Avg Speed: " + String.valueOf(results[2]) + " " + countingMeasureSpeed;
            String giveEle = "Elevation Gain: " + String.valueOf(results[1]) + "m";
            String giveTime = "Total Time: " + String.format("%02d:%02d:%02d", hours, minutes, seconds);
            return new String[]{giveDist, giveAvgSpeed, giveEle, giveTime};
        }
        else if (SResults.length == 3){
            Double[] pStats = new Double[3];

            for (int i = 0; i < 3; i++){
                pStats[i] = 0.0;
                if (SResults[i] != null){
                    pStats[i] = Double.parseDouble(SResults[i]);
                }
            }

            String countingMeasure = "m";
            String[] persFormatting = new String[3];
            if (pStats[0]/1000>1){
                pStats[0] = pStats[0]/1000; //convert to km
                countingMeasure = "km";
            }
            pStats[0]= Math.round(pStats[0]*100.0)/100.0;
            pStats[1]= Math.round(pStats[1]*100.0)/100.0;

            double totalTime = pStats[2];
            int seconds = (int)totalTime % 60;
            int minutes = (int) totalTime/60;
            int hours = 0;
            if (minutes>=60){
                hours = minutes / 60;
                minutes = minutes %60;
            }

            persFormatting[0] = "Total Distance " + pStats[0] + countingMeasure;
            persFormatting[1] = "Total Elevation " + pStats[1]  + "m";
            persFormatting[2] = "Total Active Time " + String.format("%02d:%02d:%02d", hours, minutes, seconds);

            return persFormatting;
        }
        return null;
    }

}
