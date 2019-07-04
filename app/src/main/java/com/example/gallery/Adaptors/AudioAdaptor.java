package com.example.gallery.Adaptors;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaMetadataRetriever;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.gallery.PlayAudio;
import com.example.gallery.R;

import java.io.File;
import java.io.Serializable;
import java.util.List;

public class AudioAdaptor extends RecyclerView.Adapter<AudioAdaptor.ViewHolder>{
    private android.content.Context Context;
    private List<File> files;

    public AudioAdaptor(Context Context, List<File> files){
        this.files=files;
        this.Context=Context;
    }

    @NonNull
    @Override
    public AudioAdaptor.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(Context).inflate(R.layout.fragment_item,parent,false);
        return new AudioAdaptor.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AudioAdaptor.ViewHolder viewHolder, final int position) {
        final File file=files.get(position);
        viewHolder.filename.setText(file.getName().replace(".mp3",""));

        //Extracting Audio Image from file metadata
        //Runtime exception handling
        try {
                MediaMetadataRetriever mmr = new MediaMetadataRetriever();
                mmr.setDataSource(file.getPath());
                byte[] bytes=mmr.getEmbeddedPicture();
                if(bytes!=null){
                    Bitmap bitmap=BitmapFactory.decodeByteArray(bytes,0,bytes.length);
                    viewHolder.imageView.setImageBitmap(bitmap);
                }
            } catch (RuntimeException ex) {
                // Do nothing
        }
        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(Context, PlayAudio.class);
                intent.putExtra("files", (Serializable) files).putExtra("position",position);
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
            imageView.setImageResource(R.drawable.rectangle);
        }
    }
}