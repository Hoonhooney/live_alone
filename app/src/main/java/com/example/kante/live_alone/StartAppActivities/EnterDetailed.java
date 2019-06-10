package com.example.kante.live_alone.StartAppActivities;

import android.app.Activity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.widget.EditText;

import com.example.kante.live_alone.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.WriteBatch;

import java.util.HashMap;
import java.util.Map;

public class EnterDetailed extends Activity {

    private EditText enickname;
    private EditText eschoolName;
    private String nickname;
    private String school;

    private FirebaseFirestore firebaseFirestore;
    private FirebaseUser firebaseUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_enter_detailed);
        enickname = (EditText)findViewById(R.id.nicknameText);
        eschoolName = findViewById(R.id.schoolText);
        firebaseFirestore = FirebaseFirestore.getInstance();
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
    }
    public void mOnClose(View V){
        if(!validateForm()){
            return;
        }

        // 확인버튼 누르면 닉네임이랑 학교 이름 저장
        WriteBatch batch = firebaseFirestore.batch();
        DocumentReference users = firebaseFirestore.collection("users").document(firebaseUser.getUid());
        Map<String, Object> docData = new HashMap<>();
        docData.put("nickname", nickname);
        docData.put("user_id",firebaseUser.getUid());
        docData.put("school", school);
        batch.set(users, docData);
        batch.commit();

        finish();
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        //바깥레이어 클릭시 안닫히게
        if(event.getAction()==MotionEvent.ACTION_OUTSIDE){
            return false;
        }
        return true;
    }

    @Override
    public void onBackPressed() {
        //안드로이드 백버튼 막기
        return;
    }

    private boolean validateForm() {
        boolean valid = true;

        nickname = enickname.getText().toString();
        if (TextUtils.isEmpty(nickname)) {
            enickname.setError("Required.");
            valid = false;
        } else {
            enickname.setError(null);
        }

        school = eschoolName.getText().toString();
        if (TextUtils.isEmpty(school)) {
            eschoolName.setError("Required.");
            valid = false;
        } else {
            eschoolName.setError(null);
        }

        return valid;
    }

}