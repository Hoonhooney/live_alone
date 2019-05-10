package com.example.kante.live_alone;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class DetailedPost extends AppCompatActivity {
    private FirebaseStorage fs;
    private ImageView dImage;
    private TextView dTitle;
    private TextView dBody;
    private TextView dUid;
    private String dUrl;
    private StorageReference sr;
//    private Post p;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detailed_post);
        dImage = findViewById(R.id.dp_image);
        dTitle = findViewById(R.id.dp_title);
        dBody = findViewById(R.id.dp_body);
        dUid = findViewById(R.id.dp_user_id);

        dTitle.setText(getIntent().getStringExtra("TITLE"));
        dBody.setText(getIntent().getStringExtra("BODY"));
        dUid.setText(getIntent().getStringExtra("UID"));
        dUrl = getIntent().getStringExtra("URL");

        fs = FirebaseStorage.getInstance();
        sr = fs.getReferenceFromUrl("gs://hcslivealone.appspot.com");
        if(dUrl != null){
            StorageReference path = sr.child(dUrl);
            Glide.with(this).load(path).skipMemoryCache(true).into(dImage);
        }
    }
}
