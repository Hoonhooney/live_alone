package com.example.kante.live_alone;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapRegionDecoder;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.Registry;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageMetadata;
import com.google.firebase.storage.StorageReference;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;

//피드 어댑터
public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.ViewHolder> {
    private Context context;
    private List<Post> posts;
    private int item_layout;
    private FirebaseStorage storage;
    private StorageReference storageRef;
//    Bitmap bitmap;

    public RecyclerAdapter(Context context, List<Post> posts, int item_layout) {
        this.context = context;
        this.posts = posts;
        this.item_layout = item_layout;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_feeds, null);
        storage = FirebaseStorage.getInstance();
        storageRef = storage.getReferenceFromUrl("gs://hcslivealone.appspot.com");

        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        final Post post = posts.get(position);
        holder.title.setText(post.getTitle());
        holder.body.setText(post.getBody());
        holder.userId.setText(post.getUid());
        if (post.getImageURL() != null) {
            StorageReference path = storageRef.child(post.image_url);
            Glide.with(this.context).load(path).into(holder.image);

        }
    }



    @Override
    public int getItemCount() {
        return this.posts.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView title;
        private TextView body;
        private TextView userId;
        private CardView cardview;
        private ImageView image;

        public ViewHolder(View itemView) {
            super(itemView);
            image = (ImageView) itemView.findViewById(R.id.postedImage);
            title = (TextView) itemView.findViewById(R.id.title);
            body = (TextView) itemView.findViewById(R.id.detail);
            userId = (TextView) itemView.findViewById(R.id.userId);
            cardview = (CardView) itemView.findViewById(R.id.cardview);
        }
    }

}
