package com.example.kante.live_alone.MyMenuActivities;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.PopupMenu;
import android.widget.TextView;

import com.example.kante.live_alone.Classes.User;
import com.example.kante.live_alone.R;
import com.example.kante.live_alone.StartAppActivities.SplashScreen;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.text.SimpleDateFormat;

public class MyMenu extends AppCompatActivity {

    private FirebaseUser fbUser;
    private FirebaseFirestore fs;
    private TextView text_created_at;
    private TextView text_email;
    private TextView text_nickname;
    private User user;
    private FirebaseAuth fbAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_menu);

        //get User Info
        FirebaseFirestore.setLoggingEnabled(true);
        fs = FirebaseFirestore.getInstance();
//        FirebaseFirestoreSettings settings = new FirebaseFirestoreSettings.Builder()
//                .setTimestampsInSnapshotsEnabled(true)
//                .build();
//        fs.setFirestoreSettings(settings);


        fbUser = FirebaseAuth.getInstance().getCurrentUser();

        //Find Text View by Id
        text_email = findViewById(R.id.mymenu_email);
        text_created_at = findViewById(R.id.mymenu_created_at);
        text_nickname = findViewById(R.id.mymenu_nickname);

        String timeStamp = new SimpleDateFormat("yyyy년 MM월 dd일").format(fbUser.getMetadata().getCreationTimestamp());
        text_email.setText(fbUser.getEmail().toString());
        text_created_at.setText(timeStamp);


//        getUser();
        text_nickname.setText(getIntent().getStringExtra("nickname"));

        //로그아웃
        Button btn_logout = findViewById(R.id.button_logout);
        btn_logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!MyMenu.this.isFinishing()){
                    AlertDialog.Builder builder = new AlertDialog.Builder(MyMenu.this);
                    builder.setTitle("로그아웃");
                    builder.setMessage("로그아웃 하시겠습니까?");
                    builder.setPositiveButton("예", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            fbAuth = FirebaseAuth.getInstance();
                            fbAuth.signOut();
                            Intent intent = new Intent(MyMenu.this, SplashScreen.class);
                            finishAffinity();
//                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            startActivity(intent);
                        }
                    });
                    builder.setNegativeButton("아니오", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            return;
                        }
                    });
                    builder.show();
                }
            }
        });

        //내가 쓴 게시글 보기
        Button goMyPosts = findViewById(R.id.my_posts);
        goMyPosts.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MyMenu.this, MyPosts.class);
                startActivity(intent);
            }
        });

        //내가 좋아하는 게시글 보기
        Button goLikingPosts = findViewById(R.id.go_liking_posts);
        goLikingPosts.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MyMenu.this, LikingPosts.class);
                intent.putExtra("nickname",text_nickname.getText().toString());
                startActivity(intent);
            }
        });
    }

    public void restartApp(Context context) {
    }

    private void getUser(){
        fs.collection("users").whereEqualTo("user_id",fbUser.getUid()).get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                    }
                })
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        if(queryDocumentSnapshots.isEmpty()){
                            return;
                        }else{
                            user = queryDocumentSnapshots.toObjects(User.class).get(0);
                            text_nickname.setText(user.getNickname());
                        }
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                    }
                });
    }
    public void menuClick(View v){
        PopupMenu popup = new PopupMenu(getApplicationContext(), v);
        popup.getMenuInflater().inflate(R.menu.popup_menu, popup.getMenu());
        popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch(item.getItemId()){
//                    case R.id.go_mymenu:
//                        Intent intent = new Intent(MyMenu.this, MyMenu.class);
//                        startActivity(intent);
//                        finish();
//                        break;
                    case R.id.messages:
                        Intent i = new Intent(MyMenu.this, MyMessages.class);
                        startActivity(i);
                        break;
                }
                return false;
            }
        });
        popup.show();
    }
}