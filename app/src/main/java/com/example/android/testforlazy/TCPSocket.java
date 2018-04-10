package com.example.android.testforlazy;

import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

/**
 * Created by segev on 10/04/2018.
 */

public class TCPSocket {

    private static final String TAG = TCPSocket.class.getSimpleName();

    /** Server Communication Variables */
    private Socket socket;
    private BufferedReader input;
    private PrintWriter output;

    public TCPSocket(String IP,int PORT){
        try {
            if (socket == null) {
                socket = new Socket(IP, PORT);
                input = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                output = new PrintWriter(socket.getOutputStream());
            }
        }catch (IOException e){
            Log.d(TAG,"Connecting the socket and it's components failed.");
            e.printStackTrace();
        }
    }

    public boolean sendMessage(String message){
        output.println(message);
        output.flush();
        boolean status  = output.checkError();

        if(!status) // means there was an error
            Log.d(TAG,"Failed to send message");

        return status;
    }
    public String receiveMessage(){
        String message = null;
        try{
            message = input.readLine();
        }catch (IOException e){
            Log.d(TAG,"Failed to receive message");
            e.printStackTrace();
        }

        return message;
    }

}
