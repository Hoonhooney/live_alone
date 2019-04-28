package com.example.kante.live_alone;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.text.SimpleDateFormat;
import java.util.Date;

public class MyMenu extends AppCompatActivity {

    private FirebaseUser fbUser;
    private TextView text_created_at;
    private TextView text_email;
    private TextView text_nickname;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_menu);

        text_email = findViewById(R.id.mymenu_email);
        text_created_at = findViewById(R.id.mymenu_created_at);
//        text_nickname = findViewById(R.id.mymenu_nickname);

        fbUser = FirebaseAuth.getInstance().getCurrentUser();
        String timeStamp = new SimpleDateFormat("yyyy년 MM월 dd일").format(fbUser.getMetadata().getCreationTimestamp());
        text_email.setText(fbUser.getEmail().toString());
        text_created_at.setText(timeStamp);

    }
}
