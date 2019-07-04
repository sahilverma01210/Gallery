package com.example.gallery.Adaptors;
import android.content.Context;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.gallery.R;

import java.io.File;
import java.util.ArrayList;

public class ImageScreenAdaptor extends PagerAdapter {
    Context context;
    ArrayList<File> imageFiles;
    LayoutInflater layoutInflater;

    public ImageScreenAdaptor(Context imageScreen, ArrayList<File> images) {
        this.context=imageScreen;
        this.imageFiles=images;
    }

    @Override
    public int getCount() {
        return imageFiles.size();
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view==object;
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        layoutInflater=(LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view=layoutInflater.inflate(R.layout.show_image_full,null);
        ImageView imageView=view.findViewById(R.id.image);

        //User Glide to slide through images
        Glide.with(context).load(Uri.fromFile(imageFiles.get(position))).apply(new RequestOptions().centerInside()).into(imageView);
        ((ViewPager)container).addView(view,0);
        return view;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        ViewPager viewPager=(ViewPager)container;
        View view=(View)object;
        viewPager.removeView(view);
    }
}
