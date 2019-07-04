package com.example.gallery.Adaptors;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.media.ThumbnailUtils;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import com.example.gallery.R;
import com.example.gallery.PlayVideo;
import java.io.File;
import java.io.Serializable;
import java.util.List;

public class VideoAdaptor extends RecyclerView.Adapter<VideoAdaptor.ViewHolder> {
    private Context Context;
    private List<File> files;

    public VideoAdaptor(Context Context, List<File> files){
        this.files=files;
        this.Context=Context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(Context).inflate(R.layout.fragment_item,parent,false);
        return new VideoAdaptor.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, final int position) {
        final File file=files.get(position);
        Bitmap bitmap= ThumbnailUtils.createVideoThumbnail(file.getAbsolutePath(),MediaStore.Video.Thumbnails.FULL_SCREEN_KIND);
        viewHolder.filename.setText(file.getName().replace(".mp4",""));
        viewHolder.imageView.setImageBitmap(bitmap);
        //play video
        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(Context, PlayVideo.class);
                intent.putExtra("files", (Serializable) files);
                intent.putExtra("position",position);
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