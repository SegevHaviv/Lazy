package com.example.android.testforlazy;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class RegisterActivity extends AppCompatActivity {


    final static String BASE_URL = "http://34.242.225.193:8080/sign";



    private static final String TAG = RegisterActivity.class.getSimpleName();

    private Button registerButton;
    private EditText register_email_ed;
    private EditText register_password_ed;

    HttpURLConnection urlConnection;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);


       register_email_ed = findViewById(R.id.register_email_ed);
       register_password_ed = findViewById(R.id.register_password_ed);
       registerButton = findViewById(R.id.register_button);

        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Thread t = new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            String email = register_email_ed.getText().toString();
                            String password = register_password_ed.getText().toString();

                            //TODO Do validation to email and password

                            URL url = buildUrl(email,password);
                            urlConnection = (HttpURLConnection) url.openConnection();
                            urlConnection.setRequestMethod("POST");
                            Log.d(TAG,urlConnection.getResponseMessage());

                            //TODO Check if register succeeded.
                            moveToActivity(AppActivity.class);

                        } catch(Exception e){
                            Log.d(TAG,e.getCause().toString());
                        }finally {
                            urlConnection.disconnect();
                        }

                    }
                }); // End of thread
                t.start();
            }
        });// End of onClickListener

    } // End of onCreate


    public static URL buildUrl(@NonNull String username,String password) {
        Uri builtUri = Uri.parse(BASE_URL).buildUpon()
                .appendQueryParameter("username", username)
                .appendQueryParameter("password",password)
                .appendQueryParameter("enabled","1")
                .build();

        URL url = null;
        try {
            url = new URL(builtUri.toString());
        } catch (MalformedURLException e) {
            Log.d(TAG,e.getCause().toString());
        }

        return url;
    }

    private void moveToActivity(Class destination){
        Intent intent = new Intent(this,destination);
        startActivity(intent);
    }
}