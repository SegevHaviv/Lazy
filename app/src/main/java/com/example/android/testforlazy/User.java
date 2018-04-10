package com.example.android.testforlazy;

import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class User extends AsyncTask<String,Void,Boolean>{

    /** Debugging Variables */
    private final static String TAG = User.class.getSimpleName();

    /** Server Information */
    private static final int PORT = 5000;
    private static final String IP =  "34.242.225.193";

    /** User Input Variables */
    private final String email;
    private final String password;

    /** Server Communication Variables */
    private TCPSocket tcpSocket;

    /** ONLY constructor for User class
     * @param email email to be set to the user
     * @param password Password to be set to the user
     * @throws IOException if the password does not meet the requirements, at this point, it can't be empty.
     */
    public User(@NonNull String email,@NonNull String password) throws IOException {

        //Checking validation of the details given, both might throw IOException if the information is invalid.
//        checkEmailValidation(email);
//        checkPasswordValidation(password);
        //TODO Remove the comments from validation functions.

        this.email = email;
        this.password = password;
    }


    @Override
    protected Boolean doInBackground(String... details) {
        String currEmail = details[0];
        String currPass = details[1];

        final String userLoginInformation = currEmail + " " + currPass + " app";

        new Thread(new Runnable(){
            @Override
            public void run() {
                    if(tcpSocket == null){
                        tcpSocket = new TCPSocket(IP,PORT);

                        /** Logging in to the server */
                        tcpSocket.sendMessage(userLoginInformation);
                        String s = tcpSocket.receiveMessage();
                        Log.i(TAG,s);
                    }else{
                        Log.i(TAG,"Attempting to log in even though logged in already");
                    }
            }
        }).start();

        return new Boolean(true);
    }


    public boolean Login(){
        return doInBackground(email,password);
    }

    /** Checking if the password matches the requirements
     * @param passwordToCheck String to check the requirements for
     * @return true if valid, false if invalid
     * @throws IOException if requirements aren't matched, the exception contains the reason.
     */
    public boolean checkPasswordValidation(String passwordToCheck) throws IOException{

        if(TextUtils.isEmpty(passwordToCheck))
            throw new IOException("Please enter a password.");

        else if(passwordToCheck.length() < 8)
            throw new IOException("Password must be at least 8 characters");

        return true;
    }

    /** Checking if the email matches the requirements
     * @param emailToCheck String to check the requirements for
     * @return true if valid, otherwise throws an exception
     * @throws IOException if requirements aren't matched, the exception contains the reason.
     */
    public boolean checkEmailValidation(String emailToCheck) throws  IOException{
        if(TextUtils.isEmpty(emailToCheck))
            throw new IOException("Please enter a email.");

        final Pattern VALID_EMAIL_ADDRESS_REGEX =
                Pattern.compile("^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$", Pattern.CASE_INSENSITIVE);

        Matcher matcher = VALID_EMAIL_ADDRESS_REGEX .matcher(emailToCheck);
        if(!matcher.find()){
            throw new IOException("Invalid email address.");
        }

        return true;
    }
}
