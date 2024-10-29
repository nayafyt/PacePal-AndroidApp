package com.example.dsgjn;

import android.content.Context;
import android.content.Intent;
import android.content.res.AssetManager;
import android.net.RouteInfo;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import org.w3c.dom.Text;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.nio.channels.FileChannel;
import java.util.concurrent.ExecutionException;

public class AnalyzeRoute extends AppCompatActivity {

    private static final int PICKFILE_RESULT_CODE = 1;
    Button btnUpload;
    Button btnNextAct;

    TextView label;
    TextView label2;

    FileInputStream fis;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.route_analyze);

        btnUpload = (Button) findViewById(R.id.btnUpload);
        btnUpload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Choose File from device
                Intent chooseFile = new Intent(Intent.ACTION_GET_CONTENT);
                chooseFile.setType("*/*");
                chooseFile = Intent.createChooser(chooseFile, "Choose a file");
                startActivityForResult(chooseFile, PICKFILE_RESULT_CODE);
            }
        });

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICKFILE_RESULT_CODE && resultCode == RESULT_OK) {
            Context context = getApplicationContext();

            //Take the selection of route from users click
            Uri content_describer = data.getData();
            String src = content_describer.getPath();
            File source = new File(src);
            Log.d("src is ", source.toString());
            int fileInd = content_describer.getLastPathSegment().indexOf("Download/");
            String filename = content_describer.getLastPathSegment().substring(fileInd + 9);

            //saved the choosen routes filename
            label = (TextView) findViewById(R.id.label);
            label.setText(filename);
            Log.d("FileName is ", filename);

            AssetManager am = context.getAssets();

            String yourFilePath = context.getFilesDir() + "/" + filename;
            File file = new File( yourFilePath );
            String path = file.getAbsolutePath();
            Log.d("Path",path);
            try {
                if (file.exists()) {
                    fis = new FileInputStream(file);
                    BufferedReader reader = new BufferedReader(new InputStreamReader(fis));
                    String line = reader.readLine();
                    while (line != null) {
                        line = reader.readLine();
                    }
                }

            } catch(IOException e){
                Log.d("Error", "Error");
            }
            label2 = (TextView) findViewById(R.id.label2);
            Handler handler = new Handler(Looper.getMainLooper(), new Handler.Callback() {
                @Override
                public boolean handleMessage(@NonNull Message message) {
                    String result = message.getData().getString("result");
                    label2.setText(result);
                    return true;
                }
            });


            btnNextAct = (Button) findViewById(R.id.btnNextAct);
            btnNextAct.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    ThreadAR t1 = new ThreadAR(file, filename,handler);
                    t1.start();
                    Log.d("Thread started",String.valueOf(t1.isAlive()));
                    try {
                        t1.join();
                        String [] results = t1.getResults();
                        Intent gotoInfoAct = new Intent(AnalyzeRoute.this, ViewInformationActivity.class);
                        gotoInfoAct.putExtra("RouteRes",results);
                        gotoInfoAct.putExtra("Name","AnalyzeRoute");
                        startActivity(gotoInfoAct);
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }

                }
            });

        }
    }


    private boolean InExternal() {
        boolean ExternalAvailable = true;
        String state = Environment.getExternalStorageState();
        if (!Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            Log.d("Error: External storage is not available.", "");
            ExternalAvailable = false;
        }
        return ExternalAvailable;
    }
}

