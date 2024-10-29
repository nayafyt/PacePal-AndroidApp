package com.example.dsgjn;

import android.icu.text.TimeZoneFormat;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

public class ViewStats extends AppCompatActivity{

        Button backBtn;
        TextView textView;
        ProgressBar progressBarTime;
        ProgressBar progressBarDist;
        ProgressBar progressBarEle;
        TextView pbDist;
        TextView pbTime;
        TextView pbEle;


        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.stats_view);

            progressBarDist = (ProgressBar) findViewById(R.id.progressBarDist) ;
            pbDist = (TextView) findViewById(R.id.pbDist);
            progressBarEle = (ProgressBar) findViewById(R.id.progressBarEle) ;
            pbEle = (TextView) findViewById(R.id.pbEle);
            progressBarTime = (ProgressBar) findViewById(R.id.progressBarTime) ;
            pbTime = (TextView) findViewById(R.id.pbTime);

            backBtn = (Button) findViewById(R.id.backBtn);
            backBtn.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View view) {
//                NavUtils.navigateUpFromSameTask(ViewInformationActivity.this);
                    finish();
                }
            });

            if (getIntent().getExtras() != null){
                if (getIntent().getStringExtra("Name").equals("PersonalStats")){
                    String[] ReceivedResults = getIntent().getStringArrayExtra("ResPersStat");
                    Log.d("ResPersStat", ReceivedResults[0]);
                    String [] ReceivedComp = getIntent().getStringArrayExtra("ResComp");
                    Log.d("ResComppppppp", String.valueOf(getIntent().getStringArrayExtra("ResComp")));
                    textView = (TextView) findViewById(R.id.label);
                    textView.setText(printResult(ReceivedResults));
                    Integer[] comps = printComp((ReceivedComp));
                    progressBarDist.setProgress(comps[0]);
                    pbDist.setText(comps[0] + "%");
                    progressBarEle.setProgress(comps[1]);
                    pbEle.setText(comps[1] + "%");
                    progressBarTime.setProgress(comps[2]);
                    pbTime.setText(comps[2] + "%");
//                    progressBarDist.setProgress(printComp(ReceivedComp)[0]);
//                    progressBarEle.setProgress(printComp(ReceivedComp)[1]);
//                    progressBarTime.setProgress(printComp(ReceivedComp)[2]);
                }

            }


        }

        public Integer[] printComp(String [] SResults){
            Double[] comparison = new Double[3];
            Integer [] formattingComp = new Integer[3];

            for (int i = 0; i < 3; i++){
                comparison[i] = 0.0;
                if (SResults[i] != null){
                    comparison[i] = Double.parseDouble(SResults[i]);
                }
            }
            if (comparison[0]<0){
                formattingComp[0] = Integer.parseInt(Math.round(comparison[0])+"");
            }
            else if (comparison[0]==100){
                formattingComp[0] = 0;
            }else{
                formattingComp[0] = Integer.parseInt(Math.round(comparison[0])+"") ;
            }
            if (comparison[1]<0){
                formattingComp[1] = Integer.parseInt(Math.round(comparison[1])+"") ;
            }else if (comparison[1]==100){
                formattingComp[1] = 0;
            }
            else{
                formattingComp[1] = Integer.parseInt(Math.round(comparison[1]) + "");
            }
            if (comparison[2]<0){
                formattingComp[2] = Integer.parseInt(Math.round(((-1)*comparison[2])) + "");
            }else if (comparison[2]==100){
                formattingComp[2] = 0;
            }
            else{
                formattingComp[2] = Integer.parseInt(Math.round(comparison[2]) + "");
            }


            String Comparison = "\n\n"+ formattingComp[0] + formattingComp[1] + formattingComp[2];
            return formattingComp;
        }

        public String printResult (String[] SResults){

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
                String giveAvgSpeed = "\nAvg Speed: " + String.valueOf(results[2]) + " " + countingMeasureSpeed;
                String giveEle = "\nElevation Gain: " + String.valueOf(results[1]) + "m";
                String giveTime = "\nTotal Time: " + String.format("%02d:%02d:%02d", hours, minutes, seconds);
                return giveDist + giveAvgSpeed + giveEle + giveTime;
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
                persFormatting[1] = "\nTotal Elevation " + pStats[1]  + "m";
                persFormatting[2] = "\nTotal Active Time " + String.format("%02d:%02d:%02d", hours, minutes, seconds);
                String PersonalStats = persFormatting[0] + persFormatting[1] + persFormatting[2];

                return PersonalStats;
            }
            return "";
        }



}
