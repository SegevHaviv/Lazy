package com.example.android.testforlazy;

import android.content.DialogInterface;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;


public class AppActivity extends AppCompatActivity{

    private ImageButton backward;
    private ImageButton playAndPause;
    private ImageButton forward;

    private MediaPlayer mp;

    boolean play = true;

    private boolean isInFront;

    int[] tracks = new int[3];
    int currentTrack = 0;


    private static final String TAG = AppActivity.class.getSimpleName();

    private String cmd;
    private TCPSocket tcpSocket;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_app);

        backward = findViewById(R.id.backward);
        playAndPause = findViewById(R.id.playAndPause);
        forward = findViewById(R.id.forward);

        mp = MediaPlayer.create(this,R.raw.file);

        playAndPause.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(play){
                    play = false;
                    playAndPause.setImageDrawable(getResources().getDrawable(R.drawable.pause));
                    mp.start();
                }else{
                    play = true;
                    playAndPause.setImageDrawable(getResources().getDrawable(R.drawable.play));
                    mp.pause();
                }
            }
        });

        forward.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Implement forward button
            }
        });

        backward.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Implement backward button
            }
        });



//
//        tcpSocket = TCPSocket.getInstance();
//        execute();


    }

    /**
     * Executes the commands given.
     */
    public void execute() {

        Thread t = new Thread(new Runnable() {
            @Override
            public void run() {
                while (true) {

                    Log.d(TAG, "Waiting for a message");
                    cmd = tcpSocket.receiveMessage();

                    cmd = cmd.toLowerCase();

                    Log.d(TAG, "Received command : " + cmd);

                    switch (cmd) {
                        case "camera":
                            Intent intent = new Intent("android.media.action.IMAGE_CAPTURE");
                            startActivity(intent);
                            break;
                        case "mediaplayer":

                        case "stop":
                            onBackPressed();
                    }
                }
            }


        });

        t.start();
    }


    @Override
    protected void onPause() {
        super.onPause();
        isInFront = false;
    }

    @Override
    protected void onResume() {
        super.onResume();
        isInFront = true;
    }

    @Override
    public void onBackPressed() {

        if(isInFront) {
            new android.app.AlertDialog.Builder(this)
                    .setIcon(android.R.drawable.ic_dialog_alert)
                    .setTitle("Exit?")
                    .setMessage("You sure you'd like to exit?")
                    .setPositiveButton("CLOSE", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            finishAffinity();
                            System.exit(0);
                        }

                    }).setNegativeButton("STAY", null)
                    .show();
        }else{
            super.onBackPressed();
        }
    }
}
