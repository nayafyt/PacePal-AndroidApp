package com.example.dsgjn;

import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.io.IOException;
import java.net.Socket;

public class SocketHandler {
    private static Socket socket;
    private static final String SERVER_IP = "192.165";
    private static final int SERVER_PORT = 54321;

    public static synchronized Socket getSocket() {

            try {
                socket = new Socket(SERVER_IP, SERVER_PORT);
            } catch (IOException e) {
                e.printStackTrace();
            }

        return socket;
    }

    public static synchronized void destory() {
        if (socket != null && !socket.isClosed()) {
            try {
                socket.close();
            } catch (IOException e) {
                e.printStackTrace();
                // Handle socket closure failure
            }
            socket = null;
        }
    }

    public static synchronized DataInputStream getBuf() {
        DataInputStream dis = null;
        try {
            dis = new DataInputStream(new BufferedInputStream(socket.getInputStream()));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return dis;
    }
}


