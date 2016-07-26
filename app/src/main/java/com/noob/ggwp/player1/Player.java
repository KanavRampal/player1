package com.noob.ggwp.player1;

import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.Toast;

import java.io.File;
import java.util.ArrayList;

public class Player extends AppCompatActivity implements View.OnClickListener {
    MediaPlayer mp;
    ArrayList<File> songs;
    int position;
    Uri u;
    SeekBar sb;
    Button play;
    Button prev;
    Button next;
    Button stop;
    Thread updateSeekbar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_player);
        sb=(SeekBar)findViewById(R.id.seekBar);
        updateSeekbar=new Thread(){
            @Override
            public void run() {
               int totalDuration=mp.getDuration();
                int currentPosition=0;


                while(currentPosition<totalDuration){
                    try {
                        sleep(500);
                        currentPosition=mp.getCurrentPosition();
                        sb.setProgress(currentPosition);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        };

        play=(Button)findViewById(R.id.play);
        stop=(Button)findViewById(R.id.stop);
        prev=(Button)findViewById(R.id.prev);
        next=(Button)findViewById(R.id.next);


        play.setOnClickListener(this);
        stop.setOnClickListener(this);
        prev.setOnClickListener(this);
        next.setOnClickListener(this);

        if(mp!=null){
            mp.stop();
            mp.release();
        }

        Intent i=getIntent();
        Bundle b=i.getExtras();
        songs=(ArrayList)b.getParcelableArrayList("List");
        position=b.getInt("pos",0);
        u= Uri.parse(songs.get(position).toString());
        mp=MediaPlayer.create(getApplicationContext(),u);
        mp.start();
        sb.setMax(mp.getDuration());
        updateSeekbar.start();
        sb.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                mp.seekTo(seekBar.getProgress());
            }
        });

    }

    @Override
    public void onClick(View v) {
        int id=v.getId();
        switch (id){
            case R.id.play:
                Button b=(Button)findViewById(R.id.play);

                if((play.getText().toString().compareTo("PAUSE")==0))
                    {
                        Toast.makeText(getApplicationContext(),"PLAY_one",Toast.LENGTH_SHORT).show();
                        play.setText("PLAY");
                        mp.stop();
                    }
                if((play.getText().toString().compareTo("PLAY")==0))
                    {
                        play.setText("PAUSE");
                        mp.start();
                    }
                Toast.makeText(getApplicationContext(),"PLAY",Toast.LENGTH_SHORT).show();
                break;
            case R.id.next:
                mp.stop();
                mp.release();
                position=(position+1)%songs.size();
                play.setText("PLAY");
                u=Uri.parse(songs.get(position).toString());
                mp=MediaPlayer.create(getApplicationContext(),u);
                mp.start();
                sb.setMax(mp.getDuration());
                Toast.makeText(getApplicationContext(),"Next",Toast.LENGTH_SHORT).show();
                break;
            case R.id.prev:
                mp.stop();
                mp.release();
                position=(position-1)<0?songs.size()-1:position-1;
                u=Uri.parse(songs.get(position).toString());
                mp=MediaPlayer.create(getApplicationContext(),u);
                mp.start();
                sb.setMax(mp.getDuration());
                Toast.makeText(getApplicationContext(),"Prev",Toast.LENGTH_SHORT).show();
                break;
            case R.id.stop:
                mp.stop();
                mp.seekTo(0);
                play.setText("PLAY");
                Toast.makeText(getApplicationContext(),"STOP",Toast.LENGTH_SHORT).show();
                break;
        }
    }
}
