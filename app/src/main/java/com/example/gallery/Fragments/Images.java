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

import com.example.gallery.Adaptors.ImageAdaptor;
import com.example.gallery.R;

import java.io.File;
import java.util.ArrayList;

public class Images extends Fragment {
    private ArrayList<File> images;
    private ImageAdaptor image_adaptor;
    RecyclerView recyclerView;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment_images,container,false);
        recyclerView=view.findViewById(R.id.imageFragmentView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        //get files from storage and add to recyclerView
        images=read(Environment.getExternalStorageDirectory());
        image_adaptor=new ImageAdaptor(getContext(),images);
        recyclerView.setAdapter(image_adaptor);
        return view;
    }

    private ArrayList<File> read(File root){
        ArrayList<File> list=new ArrayList<File>();
        File file[]=root.listFiles();
        for(File file1:file){
            if(file1.isDirectory()) {
                list.addAll(read(file1));
            }
            else {
                if(file1.getName().endsWith(".png")){
                    list.add(file1);
                }
            }
        }
        return list;
    }
}
