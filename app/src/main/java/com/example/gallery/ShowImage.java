package com.example.gallery;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;

import com.example.gallery.Adaptors.ImageScreenAdaptor;

import java.io.File;
import java.util.ArrayList;

public class ShowImage extends AppCompatActivity {
    ViewPager viewPager;
    ArrayList<File> images;
    int position;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.show_image);
        viewPager=findViewById(R.id.imageScreen);
        if(savedInstanceState==null){
            Intent intent=getIntent();
            images= (ArrayList<File>) intent.getSerializableExtra("files");
            position=intent.getIntExtra("position",0);
        }
        ImageScreenAdaptor imageScreenAdaptor=new ImageScreenAdaptor(this,images);
        viewPager.setAdapter(imageScreenAdaptor);
        viewPager.setCurrentItem(position,true);
    }
}