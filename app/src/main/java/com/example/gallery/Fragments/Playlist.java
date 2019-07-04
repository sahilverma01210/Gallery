package com.example.gallery.Fragments;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.example.gallery.Adaptors.PlaylistAdaptor;
import com.example.gallery.R;

import java.io.File;
import java.util.ArrayList;

public class Playlist extends Fragment {
    private ArrayList<File> files;
    private PlaylistAdaptor playlist_adaptor;
    RecyclerView recyclerView;
    String[] playlist;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment_playlists,container,false);
        recyclerView=view.findViewById(R.id.playlistView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        playlist=reverse(playlist);

        //get files from storage and add to recyclerView
        files=read(Environment.getExternalStorageDirectory());
        playlist_adaptor=new PlaylistAdaptor(getContext(),files);
        recyclerView.setAdapter(playlist_adaptor);
        return view;
    }

    @Override
    public void setArguments(@Nullable Bundle args) {
        try{
            playlist=args.getStringArray("playlist");
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    private ArrayList<File> read(File root){
        ArrayList<File> list=new ArrayList<File>();
        for(int i=0;i<playlist.length;i++){
            list.addAll(search(root,playlist[i]));
        }
        return list;
    }

    private String[] reverse(String[] strings){
        String[] newString=new String[strings.length];
        int index=0;
        for (int i=strings.length-1;i>=0;i--){
            newString[index]=strings[i];
            index++;
        }
        //remove dublicacy
        for(int i=0;i<newString.length-1;i++){
            for(int j=i+1;j<newString.length;j++){
                if(newString[i].equals(newString[j])){
                    newString[j]="";
                }
            }
        }
        return newString;
    }

    private ArrayList<File> search(File dir,String filename){
        ArrayList<File> list=new ArrayList<File>();
        File file[]=dir.listFiles();
        for(File file1:file){
            if(file1.isDirectory()) {
                list.addAll(search(file1,filename));
            }
            else {
                if(file1.getName().equals(filename)){
                    list.add(file1);
                }
            }
        }
        return list;
    }
}