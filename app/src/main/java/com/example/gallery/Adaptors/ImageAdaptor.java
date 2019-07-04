package com.example.gallery.Adaptors;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.ThumbnailUtils;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.gallery.ShowImage;
import com.example.gallery.R;
import java.io.File;
import java.util.ArrayList;

public class ImageAdaptor extends RecyclerView.Adapter<ImageAdaptor.ViewHolder>{
    private android.content.Context Context;
    private ArrayList<File> files;

    public ImageAdaptor(Context Context, ArrayList<File> files){
        this.files=files;
        this.Context=Context;
    }

    @NonNull
    @Override
    public ImageAdaptor.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(Context).inflate(R.layout.fragment_item,parent,false);
        return new ImageAdaptor.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ImageAdaptor.ViewHolder viewHolder, final int position) {
        final File file=files.get(position);
        Bitmap bitmap= ThumbnailUtils.extractThumbnail(BitmapFactory.decodeFile(file.getAbsolutePath()),500,500);
        viewHolder.filename.setText(file.getName().replace(".png",""));
        viewHolder.imageView.setImageBitmap(bitmap);
        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(Context, ShowImage.class);
                intent.putExtra("position",position).putExtra("files",files);
                Context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return files.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        public TextView filename;
        public ImageView imageView;
        public ViewHolder(View itemView){
            super(itemView);
            filename=itemView.findViewById(R.id.textView);
            imageView=itemView.findViewById(R.id.imageView);
        }
    }
}