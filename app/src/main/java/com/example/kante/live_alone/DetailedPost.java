package com.example.kante.live_alone;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;
import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.WriteBatch;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.HashMap;
import java.util.Map;

public class DetailedPost extends AppCompatActivity {
    private FirebaseStorage fs;
    private ImageView dImage;
    private TextView dTitle;
    private TextView dBody;
    private TextView dUid;
    private TextView dTime;
    private String dUrl;
    private StorageReference sr;
    private Button deleteButton;
    private String currentUserId;
    private FirebaseFirestore firebaseFirestore;
    private FirebaseAuth firebaseAuth;
    private User user;
    private String post_id;
    private Button writeCommentButton;
    private EditText contextComment;
//    private Post p;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detailed_post);

        //유저정보와 글쓴이가 일치하다면 포스트 삭제버튼 활성화
        firebaseFirestore = FirebaseFirestore.getInstance();
        firebaseAuth = FirebaseAuth.getInstance();
        String user_id = firebaseAuth.getUid(); // 유저버튼 받아옴
        firebaseFirestore.collection("users").document(user_id).get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        if(documentSnapshot.exists()){
                            user = documentSnapshot.toObject(User.class);
                            if(user.getNickname().equals(dUid.getText().toString())){
                                deleteButton.setVisibility(View.VISIBLE);
                            }
                        }
                    }
                });


        dImage = findViewById(R.id.dp_image);
        dTitle = findViewById(R.id.dp_title);
        dBody = findViewById(R.id.dp_body);
        dUid = findViewById(R.id.dp_user_id);
        dTime = findViewById(R.id.dp_posted_time);
        deleteButton = findViewById(R.id.postDelete);
        writeCommentButton = findViewById(R.id.btn_comment_input);
        contextComment = findViewById(R.id.input_comment_context);

        Intent intent = getIntent();
        dTitle.setText(intent.getStringExtra("TITLE"));
        dBody.setText(intent.getStringExtra("BODY"));
        dUid.setText(intent.getStringExtra("UID"));
        dTime.setText(intent.getStringExtra("TIME"));
        dUrl = getIntent().getStringExtra("URL");
        post_id = getIntent().getStringExtra("POSTID");

        fs = FirebaseStorage.getInstance();
        sr = fs.getReferenceFromUrl("gs://hcslivealone.appspot.com");
        if(dUrl != null){
            StorageReference path = sr.child(dUrl);
            Glide.with(this).load(path).skipMemoryCache(true).into(dImage);
        }
    }

    public void menuClick(View v){
        PopupMenu popup = new PopupMenu(getApplicationContext(), v);
        popup.getMenuInflater().inflate(R.menu.popup_menu, popup.getMenu());
        popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch(item.getItemId()){
                    case R.id.go_mymenu:
                        Intent intent = new Intent(DetailedPost.this, MyMenu.class);
                        startActivity(intent);
                        break;
                }
                return false;
            }
        });
        popup.show();
    }

    public void deletePost(){
        firebaseFirestore.collection("posts").document(post_id).delete();
    }

    public void deletePostAffirm(View v)
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("포스트 삭제");
        builder.setMessage("정말 삭제하시겠습니까?");
        builder.setPositiveButton("예",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        deletePost();
                        Toast.makeText(getApplicationContext(),"포스트가 삭제되었습니다.",Toast.LENGTH_LONG).show();
                        finish();
                    }
                });
        builder.setNegativeButton("아니오",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        Toast.makeText(getApplicationContext(),"아니오를 선택했습니다.",Toast.LENGTH_LONG).show();
                    }
                });
        builder.show();
    }


    public void writeComment(){
        WriteBatch batch = firebaseFirestore.batch();
        DocumentReference comment = firebaseFirestore.collection("comments").document();
        Map<String, Object> docData = new HashMap<>();

        String context = contextComment.getText().toString();
        docData.put("context", context);
        docData.put("user_id", firebaseAuth.getUid());
        docData.put("post_id", post_id);
        docData.put("id", comment.getId());

        batch.set(comment,docData);
        batch.commit();
    }

    public void onClickwriteComment(View v){
        writeComment();
        Toast.makeText(this, "댓글이 등록되었습니다!", Toast.LENGTH_SHORT).show();
        finish();
        startActivity(getIntent());
    }
}
