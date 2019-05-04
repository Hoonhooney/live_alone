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
public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.ViewHolder>{
    Context context;
    List<Post> posts;
    int item_layout;
    FirebaseStorage storage;
    StorageReference storageRef;
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
//        if(post.getImageURL() != null){
//            String filepath = post.image_url;
//            Glide.with(this.context).using()
//            storageRef.child(filepath).getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
//                @Override
//                public void onSuccess(Uri uri) {
//                }
//            });
//            storageRef.child(filepath).getMetadata()
//                    .addOnSuccessListener(new OnSuccessListener<StorageMetadata>() {
//                        @Override
//                        public void onSuccess(StorageMetadata storageMetadata) {
//
//                        }
//                    });
//            ImageLoaderTask task = new ImageLoaderTask(holder.image, "https://firebasestorage.googleapis.com/v0/b/hcslivealone.appspot.com/o/images%2F08ecb9ef-3f84-44ff-93bf-b2a1cf853e6a?alt=media&token=4595b31c-f05f-4f8a-9a35-8841c6eb1573");
//            task.onPostExecute(task.doInBackground());
        }
//            ImageLoaderTask task = new ImageLoaderTask(holder.image, "gs://hcslivealone.appspot.com"+post.image_url);
//            task.onPostExecute(task.doInBackground());
//        }
//        if(post.getImageURL() != null){
//            holder.image.setImageBitmap(post.urlToBitmap());
//        }
//        if(post.getImageURL() != null){
//            final String iu = post.getImageURL();
//            Thread mThread = new Thread(){
//                @Override
//                public void run(){
//                    try{
//                        Log.d("image", "abc"+iu);
//                        URL url = new URL(post.getImageURL());
//                        HttpURLConnection conn = (HttpURLConnection)url.openConnection();
//                        conn.setDoInput(true);
//                        conn.connect();
//                        InputStream is = conn.getInputStream();
//                        bitmap = BitmapFactory.decodeStream(is);
//                    } catch(IOException ex){}
//                }
//            };
//            mThread.start();
//            try{
//                mThread.join();
//                holder.image.setImageBitmap(bitmap);
//            }catch(InterruptedException e){}catch (NullPointerException ne){Log.d("exception", "NullPointer");}
//        }
    }

    @Override
    public int getItemCount() {
        return this.posts.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView title;
        TextView body;
        TextView userId;
        CardView cardview;
        ImageView image;

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
