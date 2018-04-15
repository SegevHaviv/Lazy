package com.example.android.testforlazy;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;

public class MainActivity extends AppCompatActivity{

    private static final String TAG = MainActivity.class.getSimpleName();

    /** Singleton instance of the socket */
    private TCPSocket tcpSocket;


    /** Server Information */
    private static final String PORT = "5000";
    private static final String IP =  "34.242.225.193";

    /** Layout elements **/
    private EditText email_ed;
    private EditText password_ed;
    private Button loginButton;
    private TextView register_hypertext_tv;

    private User user;



    /** Authentication Variables **/
    private Authentication authentication;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Check if there's an internet connection
        boolean result = haveNetworkConnection();
        if(!result) {
            Log.d(TAG, "No Internet Connection");
            noInternetAction();
            //TODO Check why initialize still happens if noInternetAction performed.
    }

        initialize();
    }

    /**
     * Initializes the variables from the layout and setting button listeners.
     */
    private void initialize(){
        email_ed = findViewById(R.id.login_email_ed); // Email input
        password_ed = findViewById(R.id.login_password_ed); // Password input
        loginButton = findViewById(R.id.login_button); // Login button
        register_hypertext_tv = findViewById(R.id.register_hypertext_tv);


        tcpSocket = TCPSocket.getInstance();

        authentication = new Authentication();

        // Adding listener to the login button.
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String inputEmail = email_ed.getText().toString();
                String inputPassword = password_ed.getText().toString();
                try {
                    if(user == null) { // first time trying to login
                        user = new User(inputEmail, inputPassword);
                    }else{
                        user.setEmail(inputEmail);
                        user.setPassword(inputPassword);
                    }

                    //Returns true if the login succeed, false otherwise.
                    Boolean result = authentication.login(user.getEmail(),user.getPassword());
                    Log.i(TAG,result.toString());

                    if(result){
                        moveToActivity(AppActivity.class);
                    }

                }catch(IOException exception){
                    String exceptionMessage = exception.getMessage();
                    Log.d(TAG,exceptionMessage);
                    createToast(exceptionMessage);
                }
            }
        });


        // Adding listener to move to register activity.
        register_hypertext_tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                moveToActivity(RegisterActivity.class);
            }
        });
    }

    /**
     * Creating a small pop up in the bottom of the activity.
     * @param message Message to present in the pop up.
     */
    public void createToast(String message){
        Toast.makeText(this,message ,Toast.LENGTH_LONG).show();
    }

    /**
     *  Moving to the given class'es activity
     * @param destination Activity to move to.
     */
    private void moveToActivity(Class destination){
        Intent intent = new Intent(this,destination);
        startActivity(intent);
    }


    /**
     * Checking whether there's an active internet connection
     * @return true if it has, false if not
     * @throws NullPointerException in case got null data
     */
    private boolean haveNetworkConnection() throws  NullPointerException{
        boolean haveConnectedWifi = false;
        boolean haveConnectedMobile = false;
        NetworkInfo[] netInfo;
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        try {
            netInfo = cm.getAllNetworkInfo();
        }catch(NullPointerException e){
            throw new NullPointerException("Cannot check the internet.");
        }
        for (NetworkInfo ni : netInfo) {
            if (ni.getTypeName().equalsIgnoreCase("WIFI"))
                if (ni.isConnected())
                    haveConnectedWifi = true;
            if (ni.getTypeName().equalsIgnoreCase("MOBILE"))
                if (ni.isConnected())
                    haveConnectedMobile = true;
        }
        return haveConnectedWifi || haveConnectedMobile;
    }

    /**
     * Open's an alert dialog and closing the application. To be called when there's no internet.
     */
    public void noInternetAction() {
        new AlertDialog.Builder(this)
                .setCancelable(false) // Not allowing the user to avoid the dialog
                .setIcon(android.R.drawable.ic_dialog_alert)
                .setTitle("No Internet")
                .setMessage("No internet, please check the internet connection")
                .setPositiveButton("CLOSE", new DialogInterface.OnClickListener()
                {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        finish();
                    }

                })
                .show();
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
//        TCPSocket.close();
    }
}