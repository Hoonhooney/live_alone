package com.example.kante.live_alone;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;

public class HomeFeed extends AppCompatActivity {

    Button btn = (Button) findViewById(R.id.add);
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_homefeed);

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goPosting();
            }
        });


        /*피드 카드뷰 생성*/
        /*RecyclerView recyclerView = (RecyclerView) findViewById(R.id.feeds);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(layoutManager);*/
    }

    private void goPosting(){
        Intent intent = new Intent(this, Posting.class);
        startActivity(intent);
    }
}
