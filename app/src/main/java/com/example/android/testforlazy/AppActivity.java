package com.example.android.testforlazy;

import android.content.BroadcastReceiver;
import android.content.DialogInterface;
import android.content.IntentFilter;
import android.media.MediaPlayer;
import android.net.ConnectivityManager;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;


public class AppActivity extends AppCompatActivity{

    private static final String TAG = AppActivity.class.getSimpleName();

    private ImageButton backward;
    private ImageButton playAndPause;
    private ImageButton forward;

    private MediaPlayer mp;

    private int volume = 100;

    boolean play = true;
    private boolean isInFront;

    CircularArrayList<Integer> songsList;
    Integer currentTrack = 0;

    private String cmd;
    private TCPSocket tcpSocket;

    private BroadcastReceiver mNetworkReceiver; //TODO CHANGE TO MAIN APP

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_app);

        mNetworkReceiver = new NetworkReceiver();//TODO
        registerNetworkBroadcastForNougat();//TODO

        songsList = new CircularArrayList<>();


        songsList.add(R.raw.toy);
        songsList.add(R.raw.crawlingback);
        songsList.add(R.raw.omeradam);

        backward = findViewById(R.id.backward);
        playAndPause = findViewById(R.id.playAndPause);
        forward = findViewById(R.id.forward);

        mp = MediaPlayer.create(this,songsList.get(currentTrack));

        playAndPause.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                playAndPause();
            }
        });

        forward.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                forwardSong(v);
            }
        });

        backward.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                backSong(v);
            }
        });




        tcpSocket = TCPSocket.getInstance();
        execute();


    }

    private void playAndPause(){
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

    private void forwardSong(View v){
        if(play){
            play = true;
            playAndPause.setImageDrawable(getResources().getDrawable(R.drawable.pause));
        }
        mp.stop();
        mp = MediaPlayer.create(v.getContext(),songsList.get(++currentTrack));
        mp.start();
    }

    private void backSong(View v){
        if(play){
            play = true;
            playAndPause.setImageDrawable(getResources().getDrawable(R.drawable.pause));
        }
        mp.stop();
        mp = MediaPlayer.create(v.getContext(),songsList.get(--currentTrack));
        mp.start();
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
                        case "play":
                            playAndPause();
                            break;
                        case "next":
                            forwardSong(findViewById(R.id.forward));
                            break;
                        case "back":
                            backSong(findViewById(R.id.backward));
                            break;
                        case "volumedown":
                            if(volume >= 5)
                                volume -= 5;
                            mp.setVolume(volume,volume);
                            break;
                        case "volumeup":
                            if(volume <= 95)
                                volume +=5;
                            mp.setVolume(volume,volume);
                            break;
                        case "mute":
                            mp.setVolume(0,0);
                            break;
                        default:
                            Log.d(TAG,"Unknown command.");
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

    private void registerNetworkBroadcastForNougat() {//TODO
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            registerReceiver(mNetworkReceiver, new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION));
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            registerReceiver(mNetworkReceiver, new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION));
        }
    }



    protected void unregisterNetworkChanges() {//TODO
        try {
            unregisterReceiver(mNetworkReceiver);
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        }
    }

    @Override //TODO
    protected void onDestroy() {
        unregisterNetworkChanges();
        super.onDestroy();
    }
}





