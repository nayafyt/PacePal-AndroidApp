package com.example.dsgjn;

import static android.widget.Toast.LENGTH_SHORT;

import android.content.ContentResolver;
import android.app.Activity;
import android.content.Context;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.widget.Toast;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

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
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.ConnectException;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

public class ThreadAR extends Thread implements  Runnable{

    private File file;
    private String[] results = new String[4];
    String filename;
    Handler handler;
    Socket socket ;
    //FileInputStream fileInputStream;
//    public AsyncInterface delegate = null;

    public ThreadAR(File filePath, String filename, Handler handler){
        this.file = filePath;
        this.filename = filename;
        this.handler = handler;
    }

    @Override
    public void run() {
        try {

            socket = SocketHandler.getSocket();
            OutputStream outputStream = socket.getOutputStream();
            InputStream inputStream = socket.getInputStream();
            Log.d("Sockets","Its done");

            DataInputStream dis = SocketHandler.getBuf();
            DataOutputStream dos = new DataOutputStream(new BufferedOutputStream(outputStream));

            dos.writeUTF("ANALYZE");
            dos.flush();
            dos.writeUTF(filename);
            dos.flush();
            dos.writeLong(file.length());
            FileInputStream fileInputStream = new FileInputStream(file);
            byte[] buffer = new byte[1024*4];
            int bytesRead = 0;
            while ((bytesRead = fileInputStream.read(buffer)) != -1) {
//                Log.d("Dos", String.valueOf(bytesRead));
                dos.write(buffer, 0, bytesRead);
            }
            dos.flush();

            for(int i = 0; i < 4; i++){
                Double k = dis.readDouble();
//                Log.d("K", String.valueOf(k));
                results[i] = String.valueOf(k);

                Log.d("Results []",String.valueOf(results[i]));
            }

            fileInputStream.close();
            outputStream.close();

        } catch (IOException e) {
            e.printStackTrace();
            Log.d("Results", "Error");

        }
    }

    public String[] getResults(){
        return results;
    }

}



