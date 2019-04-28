package com.example.kante.live_alone;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;

import java.util.ArrayList;
import java.util.List;

public class HomeFeed extends AppCompatActivity {


    final int ITEM_SIZE = 5;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_homefeed);
        Button btn = (Button) findViewById(R.id.addpost);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goPosting();
            }
        });


        /*피드 카드뷰 생성(Test)*/
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.feeds);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(layoutManager);

        List<Item> items = new ArrayList<>();
        Item[] item = new Item[ITEM_SIZE];

        item[0] = new Item(R.drawable.splash_img, "#1", "11111111111111111111111111111111111111111111");
        item[1] = new Item(R.drawable.splash_img, "#2", "222222222222222");
        item[2] = new Item(R.drawable.splash_img, "#3", "333333333333333");
        item[3] = new Item(R.drawable.splash_img, "#4", "444444444444444");
        item[4] = new Item(R.drawable.splash_img, "#5", "555555555555555");

        for (int i = 0; i < ITEM_SIZE; i++) {
            items.add(item[i]);
        }

        recyclerView.setAdapter(new RecyclerAdapter(getApplicationContext(), items, R.layout.activity_homefeed));
    }

    private void goPosting(){
        Intent intent = new Intent(this, Posting.class);
        startActivity(intent);
    }
}
