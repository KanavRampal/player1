package com.noob.ggwp.player1;

import android.content.Intent;
import android.media.MediaMetadataRetriever;
import android.media.MediaPlayer;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.io.File;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    ArrayList<File> songs;
    ListView listView;
    ArrayAdapter<String> adapter;
    ArrayList<String> songNames;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        listView=(ListView)findViewById(R.id.list) ;
        songNames=new ArrayList<>();
        songs=new ArrayList<>();
        songs=getSongs(Environment.getExternalStorageDirectory());
//        Toast.makeText(this,songs.size()+"",Toast.LENGTH_SHORT);
        for(int i=0;i<songs.size();i++){
            MediaMetadataRetriever mmr = new MediaMetadataRetriever();
            mmr.setDataSource(songs.get(i).getPath());
            String albumName = mmr.extractMetadata(MediaMetadataRetriever.METADATA_KEY_TITLE);
           if(albumName!=null)
            songNames.add(albumName);
            else
               songNames.add(songs.get(i).getName());
//            songNames.add(songs.get(i).getName().toString());
        }
        adapter=new ArrayAdapter<>(this,android.R.layout.simple_list_item_1,songNames);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                startActivity(new Intent(getApplicationContext(),Player.class).putExtra("pos",position).putExtra("List",songs));
            }
        });
    }

    private ArrayList<File> getSongs(File root) {
    ArrayList<File> songs=new ArrayList<>();
        File[] files=root.getAbsoluteFile().listFiles();
        for(File singleFile:files){
            if(singleFile.isDirectory()&& !singleFile.isHidden()){
                songs.addAll(getSongs(singleFile));
            }
            else{
                if(singleFile.getName().endsWith(".mp3")){
                    MediaMetadataRetriever mmr = new MediaMetadataRetriever();
                    mmr.setDataSource(singleFile.getPath());
                    if(mmr.METADATA_KEY_DURATION<=50000){
                        songs.add(singleFile);
                    }
                }
            }
//            Toast.makeText(getApplicationContext(),files.length+"",Toast.LENGTH_LONG).show();
        }
        return songs;
    }
}
