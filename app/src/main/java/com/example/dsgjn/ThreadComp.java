package com.example.dsgjn;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import java.io.BufferedOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

public class ThreadComp extends Thread implements Runnable{
    Handler handler;
    Socket socket ;
    String arg;
    String [] comparison= new String[3];
    public ThreadComp(String arg,Handler handler){
        this.arg = arg;
        this.handler = handler;
    }
    @Override
    public void run(){
        try {
            socket = SocketHandler.getSocket();
            OutputStream outputStream = socket.getOutputStream();
            InputStream inputStream = socket.getInputStream();
            Log.d("Sockets", "Its done");

            DataInputStream dis = SocketHandler.getBuf();
            DataOutputStream dos = new DataOutputStream(new BufferedOutputStream(outputStream));

            dos.writeUTF("COMPARISONATOR");
            dos.flush();

            Message msg = new Message();
            Bundle bundle = new Bundle();
            bundle.putString("result", arg);
            String user_name = bundle.getString("result");
            Log.d(" WE PASS ON COMPAR", user_name);

            dos.writeUTF(user_name);
            dos.flush();

            for(int i = 0; i < 3; i++){
                Double k = dis.readDouble();
                comparison[i] = String.valueOf(k);
                Log.d("TOTAL []",String.valueOf(comparison[i]));
            }

            dis.close();
            outputStream.close();


        }catch(IOException e){
            e.printStackTrace();
        }
    }
    public String[] getComp(){return comparison;}

}
