package com.example.kante.live_alone;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Map;

public class Posting extends AppCompatActivity {

    private FirebaseDatabase database;
    private Button postingButton;
    private DatabaseReference mDatabase;
    private TextView text_title;
    private TextView text_context;
    private String uid;
    private String email;
    private int starCount = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_posting);
        database = FirebaseDatabase.getInstance();

        postingButton = findViewById(R.id.btn_posting);
        text_context = findViewById(R.id.text_context);
        text_title = findViewById(R.id.text_title);

        postingButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("GGGG","QWEQWE");
                DatabaseReference myRef = database.getReference("message");
                myRef.setValue("Hello, World!");

                Log.d("finish_set","qweqwe");
            }
        });



    }


    private void getUserInfo(FirebaseUser user){
        uid = user.getUid();
        email = user.getEmail();

    }

    private void writeNewPost(String userId, String username, String title, String body) {

        mDatabase = FirebaseDatabase.getInstance().getReference();
        // Create new post at /user-posts/$userid/$postid and at
        // /posts/$postid simultaneously
        String key = mDatabase.child("posts").push().getKey();
        Post post = new Post(userId, username, title, body);
        Map<String, Object> postValues = post.toMap();

        Map<String, Object> childUpdates = new HashMap<>();
        childUpdates.put("/posts/" + key, postValues);
        childUpdates.put("/user-posts/" + userId + "/" + key, postValues);

        mDatabase.updateChildren(childUpdates);
    }


}
