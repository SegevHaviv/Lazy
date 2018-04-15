package com.example.android.testforlazy;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.util.Log;

import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Authentication {

    private final static String TAG = Authentication.class.getSimpleName();
    private String reply;
    private TCPSocket tcpSocket;

    public Authentication(){
        tcpSocket = TCPSocket.getInstance();
    }

    /**
     * Sending the user's information to the server and waits for a reply if the given information is correct.
     * @param email user's email
     * @param password user's password
     * @return true if the details are correct, false otherwise
     * @throws IOException if given details doesn't meet the ground requirements.
     */
    public boolean login(String email,String password) throws  IOException{
        final String details = email + " " + password + " app";

        Log.i(TAG,"Trying to log in with : " + details);

        Thread t = new Thread(new Runnable(){
            @Override
            public void run() {
                tcpSocket.sendMessage(details);
                reply = tcpSocket.receiveMessage();
                Log.d(TAG,reply);
            }
        });

        t.start();

        try{
            t.join();
        }catch (InterruptedException e){
            e.printStackTrace();
        }

        return reply.equals("recive login");
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
    public boolean checkEmailValidation(String emailToCheck) throws IOException {
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