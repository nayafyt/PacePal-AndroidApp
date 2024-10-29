package com.example.dsgjn;

import android.app.Notification;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.ContactsContract;
import android.util.Log;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

public class ThreadPersStat extends Thread implements Runnable{
    String arg;
    Handler handler;
    String [] persStats = new String[3];

    String[] comparison = new String[3];
    Socket socket;

    public ThreadPersStat(String arg, Handler handler){
        this.arg =arg;
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

            dos.writeUTF("PERSONAL_STATS");
            dos.flush();

            Message msg = new Message();
            Bundle bundle = new Bundle();
            bundle.putString("result", arg);
            String user_name = bundle.getString("result");
            Log.d(" WE PASS", user_name);

            dos.writeUTF(user_name);
            dos.flush();
            for(int i = 0; i < 3; i++){
                Double next = dis.readDouble();

                persStats[i] =String.valueOf(next);
                Log.d("ResPERSONStat[]",String.valueOf(persStats[i]));


            }

        } catch (IOException e) {
            e.printStackTrace();
            Log.e("you f","SORRY");
        }
    }
    public String[] getPersStats(){
        return persStats;
    }

}
