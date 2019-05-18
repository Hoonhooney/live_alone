package com.example.kante.live_alone;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

public class DetailedPost extends AppCompatActivity {
    private FirebaseStorage fs;
    private ImageView dImage;
    private TextView dTitle;
    private TextView dBody;
    private TextView dUid;
    private TextView dTime;
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
        dTime = findViewById(R.id.dp_posted_time);

        Intent intent = getIntent();
        dTitle.setText(intent.getStringExtra("TITLE"));
        dBody.setText(intent.getStringExtra("BODY"));
        dUid.setText(intent.getStringExtra("UID"));
        dTime.setText(intent.getStringExtra("TIME"));
        dUrl = getIntent().getStringExtra("URL");

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
}
