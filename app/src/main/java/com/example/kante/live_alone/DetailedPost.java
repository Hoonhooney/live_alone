package com.example.kante.live_alone;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

public class DetailedPost extends AppCompatActivity {
    private FirebaseFirestore fs;
    private ImageView dImage;
    private TextView dTitle;
    private TextView dBody;
    private TextView dUid;
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

//        fs = FirebaseFirestore.getInstance();
//        fs.collection("posts")
//                .whereEqualTo("user_id",true)
//                .get()
//                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
//                    @Override
//                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
//                        if (task.isSuccessful()) {
//                            for (QueryDocumentSnapshot document : task.getResult()) {
//                                Post p = document.toObject(Post.class);
//                                Log.d("ADAD", document.getId() + " => " + document.getData());
//                            }
//                        } else {
//                            Log.d("SFSF", "Error getting documents: ", task.getException());
//                        }
//                    }
//                });
//        title.setText(p.getTitle());
//        body.setText(p.getBody());
//        uid.setText(p.getUid());
    }
}
