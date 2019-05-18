package com.example.kante.live_alone;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import org.w3c.dom.Text;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

//피드 어댑터
public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.ViewHolder> {
    private Context context;
    private List<Post> posts;
    private int item_layout;
    private FirebaseStorage storage;
    private StorageReference storageRef;
    private FirebaseFirestore firestore;
    private DocumentReference documentReference;
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
        firestore = FirebaseFirestore.getInstance();
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        final Post post = posts.get(position);
        holder.title.setText(post.getTitle());
        holder.body.setText(post.getBody());
        holder.post_id= post.getId();


        //리사이클러뷰 각각의 아이템에 유저 닉네임 보이도록 표시
        firestore.collection("users").document(post.getUid()).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                if(!documentSnapshot.exists()){
                    return;
                }else{
                    User user = documentSnapshot.toObject(User.class);
                    String nickname = user.getNickname();
                    holder.feed_nickname.setText(nickname);
                }
            }
        });
        try{
            Date date = new SimpleDateFormat("yyyyMMddkkmmss").parse(post.getCreated_at());
            String format = new SimpleDateFormat("yyyy/MM/dd kk:mm").format(date);
            holder.time.setText(format);
        }catch(ParseException pe){
            pe.printStackTrace();
        }
        if (post.getImageURL() != null) {
            StorageReference path = storageRef.child(post.image_url);
            Glide.with(this.context).load(path).skipMemoryCache(true).into(holder.image);
            holder.url = post.getImageURL();
        }
        else
            holder.url = null;
            holder.image.setImageResource(R.drawable.splash_img);
    }

    @Override
    public int getItemCount() {
        return this.posts.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView title;
        private TextView body;
        private TextView feed_nickname;
        private CardView cardview;
        private ImageView image;
        private String url;
        private TextView time;
        private String post_id;

        public ViewHolder(View itemView) {
            super(itemView);
            image = (ImageView) itemView.findViewById(R.id.postedImage);
            title = (TextView) itemView.findViewById(R.id.title);
            body = (TextView) itemView.findViewById(R.id.detail);
            feed_nickname = (TextView) itemView.findViewById(R.id.feed_nickname);
            cardview = (CardView) itemView.findViewById(R.id.cardview);
            time = (TextView) itemView.findViewById(R.id.time);

            cardview.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String pTitle = title.getText().toString();
                    String pBody = body.getText().toString();
                    String pUid = feed_nickname.getText().toString();
                    String pTime = time.getText().toString();
                    Intent intent = new Intent(context, DetailedPost.class);
                    intent.putExtra("TIME", pTime);
                    intent.putExtra("TITLE", pTitle);
                    intent.putExtra("BODY", pBody);
                    intent.putExtra("UID", pUid);
                    intent.putExtra("TIME", pTime);
                    intent.putExtra("URL", url);
                    intent.putExtra("POSTID",post_id);
                    context.startActivity(intent);
                }
            });
        }
    }
}
