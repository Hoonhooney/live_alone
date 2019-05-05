package com.example.kante.live_alone;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.IntentCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreSettings;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

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


        getUser();

        Button btn_logout = findViewById(R.id.button_logout);
        btn_logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fbAuth = FirebaseAuth.getInstance();
                fbAuth.signOut();
                Intent intent = new Intent(getApplicationContext(), SplashScreen.class);
                finishAffinity();
//                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
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
}