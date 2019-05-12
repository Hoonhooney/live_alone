package com.example.kante.live_alone;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class HomeFeed extends AppCompatActivity {

    public static String TAG="HomeFeed";

    private FirebaseFirestore firebaseFirestore;
    private FirebaseUser firebaseUser;
    private Fragment fr;

    @Override
    protected void onStart() {
        super.onStart();
        DocumentReference docRef = firebaseFirestore.collection("users").document(firebaseUser.getUid());
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document != null && document.exists()) {
                        Log.d(TAG, "DocumentSnapshot data: " + document.getData());
                    } else {
                        Intent i = new Intent(HomeFeed.this,EnterDetailed.class);
                        startActivityForResult(i, 1);
                        Log.d(TAG, "No such document");
                    }
                } else {
                    Log.d(TAG, "get failed with ", task.getException());
                }

            }
        });
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_homefeed);

        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        firebaseFirestore = FirebaseFirestore.getInstance();
        fr = new FCook();
        getSupportFragmentManager().beginTransaction().add(R.id.fragment_container,fr).commit();

        Button bCook = (Button) findViewById(R.id.cook);
        Button bRoom = (Button) findViewById(R.id.room);
        Button bActivities = (Button) findViewById(R.id.activities);
        Button bTips = (Button) findViewById(R.id.tips);
        Button btn = (Button) findViewById(R.id.addpost);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goPosting();
            }
        });
    }

    public void selectCategory(View view){
        fr = null;
        switch(view.getId()){
            case R.id.cook:
                fr = new FCook();
                break;
            case R.id.room:
                fr = new FRoom();
                break;
            case R.id.activities:
                fr = new FActivities();
                break;
            case R.id.tips:
                fr = new FTips();
                break;
        }
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        ft.replace(R.id.fragment_container, fr);
        ft.commit();
    }
    private void goPosting(){
        Intent intent = new Intent(this, Posting.class);
        intent.putExtra("Category",fr.getClass().getSimpleName());
        startActivity(intent);
    }
}
