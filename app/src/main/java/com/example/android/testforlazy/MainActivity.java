package com.example.android.testforlazy;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Html;
import android.text.Spanned;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.net.Socket;

public class MainActivity extends AppCompatActivity{

    private static final String TAG = MainActivity.class.getSimpleName();

    private EditText email_ed;
    private EditText password_ed;
    private Button loginButton;
    private User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initialize();
    }

    private void initialize(){
        email_ed = (EditText)findViewById(R.id.login_email_ed); // Email input
        password_ed = (EditText)findViewById(R.id.login_password_ed); // Password input
        loginButton = (Button)findViewById(R.id.login_button); // Login button

        // Adding listener to the login button.
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String inputEmail = email_ed.getText().toString();
                String inputPassword = password_ed.getText().toString();
                try {
                    if(user == null) {
                        user = new User(inputEmail, inputPassword);
                    }
                    user.Login();

                }catch(IOException exception){
                    String exceptionMessage = exception.getMessage();
                    createToast(exceptionMessage);
                }

            }
        });
    }

    public void createToast(String message){
        Toast.makeText(MainActivity.this,message ,Toast.LENGTH_LONG).show();
    }
}
