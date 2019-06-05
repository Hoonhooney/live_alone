package com.example.kante.live_alone.Adapters;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.kante.live_alone.Classes.Like;
import com.example.kante.live_alone.Classes.Post;
import com.example.kante.live_alone.Classes.User;
import com.example.kante.live_alone.PostActivities.DetailedPost;
import com.example.kante.live_alone.R;
import com.google.android.gms.common.util.CollectionUtils;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.common.base.Predicate;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;

import javax.annotation.Nullable;

//피드 어댑터
public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.ViewHolder> {
    private Context context;
    private List<Post> posts;
    private int item_layout;
    private FirebaseStorage storage;
    private StorageReference storageRef;
    private FirebaseFirestore firestore;
    private FirebaseAuth firebaseAuth;
    private String posting_user_id;
    private DocumentReference documentReference;
    private List<User> users;
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
        firebaseAuth = FirebaseAuth.getInstance();
        // TODO : 여기에다가 likes 데이터 불러온거 저장해놓고 onBindViewHolder에서 settext만 해주면 될까?
        // TODO : users의 닉네임도 마찬가지

        return new ViewHolder(v);
    }


    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {

        final Post post = posts.get(position);
//        Iterator<User> iterator = users.iterator();
//        User user = null;
//        while (iterator.hasNext()) {
//            user = iterator.next();
//            if (user.user_id.equals(post.getUid())) {
//                break;
//            }
//        }
        holder.title.setText(post.getTitle());
        holder.body.setText(post.getBody());
        if(post.getBody().length() > 200){
            ViewGroup.LayoutParams l = holder.body.getLayoutParams();
            l.height = 200;
            holder.body.setLayoutParams(l);
        }
        holder.post_id= post.getId();
        holder.post_category = post.getCategory();

        //like count 설정
        firestore.collection("likes").whereEqualTo("post_id",post.id).whereEqualTo("status","active").get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        if(queryDocumentSnapshots.isEmpty()){
                            holder.like_counts.setText(String.valueOf(0));
                        }else {
                            holder.like_counts.setText(String.valueOf(queryDocumentSnapshots.size()));
                        }
                    }
                });

        firestore.collection("likes").whereEqualTo("post_id",post.id).whereEqualTo("user_id",firebaseAuth.getUid()).get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        if(!queryDocumentSnapshots.isEmpty()){
                            Like l = queryDocumentSnapshots.toObjects(Like.class).get(0);
                            if(l.status.equals("active")){
                                holder.like.setImageResource(R.drawable.like_clicked);
                            }
                        }
                    }
                });
//        holder.feed_nickname.setText(user.getNickname());

        //리사이클러뷰 각각의 아이템에 유저 닉네임 보이도록 표시
        firestore.collection("users").document(post.getUid()).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                if(!documentSnapshot.exists()){
                    return;
                }else{
                    User user = documentSnapshot.toObject(User.class);
                    String nickname = user.getNickname();
                    posting_user_id = user.getUser_id();
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
        else {
            holder.url = null;
            ViewGroup.LayoutParams l = holder.image.getLayoutParams();
            l.width = 0;
            l.height =0;
            holder.image.setLayoutParams(l);
        }
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
        private TextView like_counts;
        private ImageView like;
        private String post_category;

        public ViewHolder(View itemView) {
            super(itemView);
            image = (ImageView) itemView.findViewById(R.id.postedImage);

            title = (TextView) itemView.findViewById(R.id.title);
            body = (TextView) itemView.findViewById(R.id.detail);
            feed_nickname = (TextView) itemView.findViewById(R.id.feed_nickname);
            cardview = (CardView) itemView.findViewById(R.id.cardview);
            time = (TextView) itemView.findViewById(R.id.time);
            like_counts = itemView.findViewById(R.id.likes_count);
            like = itemView.findViewById(R.id.like);


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
                    intent.putExtra("posting_user_id",posting_user_id);
                    intent.putExtra("CATEGORY",post_category);
                    context.startActivity(intent);
                }
            });
        }
    }
}
