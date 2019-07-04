package com.example.gallery;
import android.Manifest;
import android.content.Intent;
import android.os.Environment;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.example.gallery.Fragments.Audios;
import com.example.gallery.Fragments.Images;
import com.example.gallery.Fragments.Playlist;
import com.example.gallery.Fragments.Videos;

import java.io.File;
import java.io.FileInputStream;
import java.io.Serializable;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    Toolbar toolbar;
    FloatingActionButton play_last;
    String[] list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ViewPager viewPager=findViewById(R.id.view_pager);
        TabLayout tabLayout=findViewById(R.id.tab_layout);
        toolbar=findViewById(R.id.toolbar);
        play_last=findViewById(R.id.floatingActionButton);
        ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},100);

        //initializing Fragmentation in a Activity
        ViewPagerAdapter viewPagerAdapter=new ViewPagerAdapter(getSupportFragmentManager());

        //adding Fragments in single Pager View
        viewPagerAdapter.addFragment(new Videos(),"VIDEOS");
        viewPagerAdapter.addFragment(new Images(),"IMAGES");
        viewPagerAdapter.addFragment(new Audios(),"AUDIOS");

        //adding playlist Fragment
        Playlist playlist=new Playlist();
        list=readFile("playlist");
        Bundle bundle=new Bundle();
        bundle.putStringArray("playlist",list);
        playlist.setArguments(bundle);
        viewPagerAdapter.addFragment(playlist,"PLAYLIST");

        //setting up the Fragments
        viewPager.setAdapter(viewPagerAdapter);
        tabLayout.setupWithViewPager(viewPager,true);

        //Floating buttons implementation
        play_last.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(MainActivity.this, PlayAudio.class);
                intent.putExtra("files", (Serializable) read(Environment.getExternalStorageDirectory(),list[list.length-1])).putExtra("position",0);
                startActivity(intent);
            }
        });
    }

    //read cache file
    private String[] readFile(String file){
        String text="";
        try{
            FileInputStream fileInputStream=openFileInput(file);
            int size=fileInputStream.available();
            byte[] buffer=new byte[size];
            fileInputStream.read(buffer);
            fileInputStream.close();
            text=new String(buffer);
        }catch (Exception e){
            e.printStackTrace();
        }
        return text.split("#");
    }

    //read media files
    private ArrayList<File> read(File root,String name) {
        ArrayList<File> list=new ArrayList<File>();
        File file[]=root.listFiles();
        for(File file1:file){
            if(file1.isDirectory()) {
                list.addAll(read(file1,name));
            }
            else {
                if(file1.getName().equals(name)){
                    list.add(file1);
                }
            }
        }
        return list;
    }

    //class to arrange objects of each fragment in created Pager View
    class ViewPagerAdapter extends FragmentPagerAdapter{
        private ArrayList<Fragment> fragments;
        private ArrayList<String> titles;

        ViewPagerAdapter(FragmentManager fm){
            super(fm);
            this.fragments=new ArrayList<>();
            this.titles=new ArrayList<>();
        }

        @Override
        public Fragment getItem(int position) {
            return fragments.get(position);
        }

        @Override
        public int getCount() {
            return fragments.size();
        }

        //method for adding objects of chat or user fragment
        public void addFragment(Fragment fragment,String title){
            fragments.add(fragment);
            titles.add(title);
        }

        @Nullable
        @Override
        public CharSequence getPageTitle(int position) {
            return titles.get(position);
        }
    }
}