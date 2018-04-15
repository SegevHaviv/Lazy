package com.example.android.testforlazy;

import android.content.DialogInterface;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.EditText;
import android.util.Log;
import android.view.View;
import android.text.InputType;

import java.util.Scanner;

public class AppActivity extends AppCompatActivity {

    /** Temporary variables only for debugging.
     * Currently working with an input text instead of reading the socket's input until the server will be ready. **/
    private Button btn;
    private EditText ed;


    private static final String TAG = AppActivity.class.getSimpleName();

    private String cmd;
    private TCPSocket tcpSocket;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_app);

        btn = findViewById(R.id.app_btn);
        ed = findViewById(R.id.input_ed_app);

        tcpSocket = TCPSocket.getInstance();

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cmd = ed.getText().toString();
                execute(cmd);
            }
        });
    }

    /**
     * Executes the given cmd
     * @param cmd specific cmd to execute
     */
    public void execute(String cmd) {
            cmd = cmd.toLowerCase();

            Log.d(TAG, cmd);

            switch (cmd) {
                case "camera":
                    Intent intent = new Intent("android.media.action.IMAGE_CAPTURE");
                    startActivity(intent);
                    break;
            }
        }



    @Override
    public void onBackPressed() {
        new android.app.AlertDialog.Builder(this)
                .setIcon(android.R.drawable.ic_dialog_alert)
                .setTitle("Exit?")
                .setMessage("You sure you'd like to exit?")
                .setPositiveButton("CLOSE", new DialogInterface.OnClickListener()
                {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        finishAffinity();
                        System.exit(0);
                    }

                }).setNegativeButton("STAY",null)
                .show();
    }
}
