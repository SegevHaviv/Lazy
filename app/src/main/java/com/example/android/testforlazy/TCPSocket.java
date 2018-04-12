package com.example.android.testforlazy;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class TCPSocket extends AsyncTask<String,Void,Boolean>{

    private static final String TAG = TCPSocket.class.getSimpleName();

    /** Server Communication Variables */
    private Socket socket;
    private BufferedReader input;
    private PrintWriter output;

    public boolean init(String ip,String port){
        return doInBackground(ip,port);
    }

    @Override
    protected Boolean doInBackground(String... strings) {
        final String currIP = strings[0];
        final String currPassword = strings[1];

        Thread t = new Thread(new Runnable(){ // TODO In case the server does not run.
            @Override
            public void run() {
                try {
                    if (socket == null) {
                        socket = new Socket(currIP, Integer.parseInt(currPassword));
                        input = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                        output = new PrintWriter(socket.getOutputStream());
                    }
                }catch (IOException e){
                    Log.d(TAG,"Connecting the socket and it's components failed.");
                    e.printStackTrace();
                }
            }
        });
        t.start();

        try{
            Thread.sleep(1000);
        }catch(InterruptedException e){
            e.printStackTrace();
        }


        return socket.isConnected();
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
