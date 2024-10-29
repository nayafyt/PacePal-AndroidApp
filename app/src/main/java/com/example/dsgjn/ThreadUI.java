package com.example.dsgjn;
import android.app.Notification;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

public class ThreadUI extends Thread{
    String arg;
    Handler handler;

    public ThreadUI(String arg, Handler handler){
        this.arg =arg;
        this.handler = handler;
    }

    @Override
    public void run() {
        try {
            sleep(500);
            Message msg = new Message();
            Bundle bundle = new Bundle();
            bundle.putString("result", "this is the result of "+arg);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
