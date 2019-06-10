package com.example.kante.live_alone.HomeActivities;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.PopupMenu;

import com.example.kante.live_alone.Fragments.FChat;
import com.example.kante.live_alone.Fragments.FTrans;
import com.example.kante.live_alone.MyMenuActivities.MyMessages;
import com.example.kante.live_alone.StartAppActivities.EnterDetailed;
import com.example.kante.live_alone.Fragments.FActivities;
import com.example.kante.live_alone.Fragments.FCook;
import com.example.kante.live_alone.Fragments.FEatout;
import com.example.kante.live_alone.Fragments.FRoom;
import com.example.kante.live_alone.Fragments.FTips;
import com.example.kante.live_alone.MyMenuActivities.MyMenu;
import com.example.kante.live_alone.PostActivities.Posting;
import com.example.kante.live_alone.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.Locale;

public class HomeFeed extends AppCompatActivity {

    public static String TAG="HomeFeed";

    private FirebaseFirestore firebaseFirestore;
    private FirebaseUser firebaseUser;
    private Fragment fr;
    private String nickname;
    private ArrayList<Button> buttons;
    boolean visible = false;

    @Override
    protected void onStart() {
        super.onStart();
        final DocumentReference docRef = firebaseFirestore.collection("users").document(firebaseUser.getUid());
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document != null && document.exists()) {
//                        Log.d(TAG, "DocumentSnapshot data: " + document.getData());
//                        User user = document.toObject(User.class);
                        nickname = document.getString("nickname");

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
        fr = new FChat();
        getSupportFragmentManager().beginTransaction().add(R.id.fragment_container,fr).commit();

        buttons = new ArrayList<Button>();
        buttons.add((Button)findViewById(R.id.chat));
        buttons.add((Button)findViewById(R.id.cook));
        buttons.add((Button)findViewById(R.id.room));
        buttons.add((Button)findViewById(R.id.activities));
        buttons.add((Button)findViewById(R.id.tips));
        buttons.add((Button)findViewById(R.id.eatout));
        buttons.add((Button)findViewById(R.id.trans));
        buttons.get(0).setBackgroundResource(R.drawable.chat_2);

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
        Button btnSearch = (Button)findViewById(R.id.search);
        final Button btnGoSearch = (Button)findViewById(R.id.goSearch);
        final EditText searchingText = (EditText)findViewById(R.id.searchingText);
        btnSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(visible){
                    searchingText.setVisibility(EditText.GONE);
                    btnGoSearch.setVisibility(EditText.GONE);
                    visible = false;
                }
                else{
                    searchingText.setVisibility(EditText.VISIBLE);
                    btnGoSearch.setVisibility(EditText.VISIBLE);
                    visible = true;
                }
            }
        });
//        btnGoSearch.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if(!searchingText.getText().toString().isEmpty()){
//                    fr = new FChat();
//                    String searchingWord = searchingText.getText().toString();
//                    ((FChat)fr).getAdapter().filter(searchingWord);
//                }
//            }
//        });
        searchingText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                String searchWord = searchingText.getText().toString();
//                        .toLowerCase(Locale.getDefault());
                ((FChat)fr).getAdapter().filter(searchWord);
            }
        });
    }

    public void selectCategory(View view){
        buttons.get(0).setBackgroundResource(R.drawable.chat);
        buttons.get(1).setBackgroundResource(R.drawable.recipies);
        buttons.get(2).setBackgroundResource(R.drawable.roominfo);
        buttons.get(3).setBackgroundResource(R.drawable.schoolact);
        buttons.get(4).setBackgroundResource(R.drawable.tips);
        buttons.get(5).setBackgroundResource(R.drawable.eatout);
        buttons.get(6).setBackgroundResource(R.drawable.trans);

        fr = null;
        switch(view.getId()){
            case R.id.chat:
                fr = new FChat();
                Bundle b = new Bundle();
                buttons.get(0).setBackgroundResource(R.drawable.chat_2);
                break;
            case R.id.cook:
                fr = new FCook();
                buttons.get(1).setBackgroundResource(R.drawable.recipies_2);
                break;
            case R.id.room:
                fr = new FRoom();
                buttons.get(2).setBackgroundResource(R.drawable.room_2);
                break;
            case R.id.activities:
                fr = new FActivities();
                buttons.get(3).setBackgroundResource(R.drawable.activities_2);
                break;
            case R.id.tips:
                fr = new FTips();
                buttons.get(4).setBackgroundResource(R.drawable.tips_2);
                break;
            case R.id.eatout:
                fr = new FEatout();
                buttons.get(5).setBackgroundResource(R.drawable.eatout_2);
                break;
            case R.id.trans:
                fr = new FTrans();
                buttons.get(6).setBackgroundResource(R.drawable.trans_2);
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

    public void menuClick(View v){
        PopupMenu popup = new PopupMenu(getApplicationContext(), v);
        popup.getMenuInflater().inflate(R.menu.popup_menu, popup.getMenu());
        popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch(item.getItemId()){
                    case R.id.go_mymenu:
                        Intent intent = new Intent(HomeFeed.this, MyMenu.class);
                        intent.putExtra("nickname",nickname);
                        startActivity(intent);
                        break;
                    case R.id.messages:
                        Intent i = new Intent(HomeFeed.this, MyMessages.class);
                        startActivity(i);
                        break;
                }
                return false;
            }
        });
        popup.show();
    }
}