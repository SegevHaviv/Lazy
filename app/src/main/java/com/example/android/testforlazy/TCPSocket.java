package com.example.android.testforlazy;

import android.util.Log;
import android.content.Intent;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class TCPSocket{

    private static final String TAG = TCPSocket.class.getSimpleName();

    // TODO Handle the case the server crashes.

    /** Server Communication Variables */
    private Socket socket;
    private BufferedReader input;
    private PrintWriter output;

    /** Server Information */
    private static final String PORT = "5000";
    private static final String IP =  "193.106.55.107";

    private static final TCPSocket SingletonTCPSocket = new TCPSocket();

    public static TCPSocket getInstance() {
        return TCPSocket.SingletonTCPSocket;
    }

    private TCPSocket(){

        Thread thread = new Thread(new Runnable(){
            @Override
            public void run() {
                try{
                    socket = new Socket(IP,Integer.parseInt(PORT));

                    output = new PrintWriter(socket.getOutputStream());
                    input = new BufferedReader(new InputStreamReader(socket.getInputStream()));


                }catch (IOException e){
                    String message = e.getMessage();
                    Log.d(TAG,message);
                    Intent homeIntent = new Intent(Intent.ACTION_MAIN);
                    homeIntent.addCategory( Intent.CATEGORY_HOME );
                    homeIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);


                    //TODO if didn't manage to connect the server, close the application with a pop up
                }
            }
        });

        thread.start();
    }

    public void close(){
        try{
            socket.close();
            output.close();
            input.close();
        }catch (IOException e){
            Log.d(TAG,"Failed to close the resources, reason : " + e.getCause());
        }
    }


    public boolean sendMessage(String message){
        if(output != null){
            output.println(message);
            output.flush();
        }else{
            Log.d(TAG,"Attempting to send messages while 'output' is null");
        }

        return true;
    }
    public String receiveMessage(){

        if(input == null){
            Log.d(TAG,"Null");
        }
        String message = null;
        try{
            message = input.readLine();
        }catch (IOException e){
            Log.d(TAG,"Failed to receive message, reason : " + e.getCause());
        }
        return message;
    }

}
