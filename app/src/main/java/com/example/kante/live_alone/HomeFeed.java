package com.example.kante.live_alone;

import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class HomeFeed extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_homefeed);

        Fragment firstFragment = new FCook();
        getSupportFragmentManager().beginTransaction().add(R.id.fragment_container,firstFragment).commit();

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
        Fragment fr = null;
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
        startActivity(intent);
    }
}
