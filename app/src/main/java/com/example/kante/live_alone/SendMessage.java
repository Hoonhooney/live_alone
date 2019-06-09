package com.example.kante.live_alone;

import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.beardedhen.androidbootstrap.BootstrapLabel;
import com.example.kante.live_alone.Classes.Message;
import com.example.kante.live_alone.Classes.User;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.WriteBatch;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class SendMessage extends AppCompatActivity {

    private TextView title;
    private TextView context;
    private FirebaseFirestore firebaseFirestore;
    private FirebaseAuth firebaseAuth;
    private String receiver_id;
    private String sender_nickname;
    private String receiver_nickname;
    private BootstrapLabel sendmessagelabel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_send_message);

        context = findViewById(R.id.message_context);
        firebaseFirestore = FirebaseFirestore.getInstance();
        firebaseAuth = FirebaseAuth.getInstance();
        receiver_id = getIntent().getStringExtra("receiver_id");
        receiver_nickname = getIntent().getStringExtra("receiver_nickname");
        sendmessagelabel = findViewById(R.id.send_message_label);
        sendmessagelabel.setText(receiver_nickname+"님에게 보내는 쪽지");
        firebaseFirestore.collection("users").document(firebaseAuth.getUid()).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                User user = documentSnapshot.toObject(User.class);
                sender_nickname = user.getNickname();
            }
        });

    }



    public void onClickSendButton(View v){

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("쪽지 보내기");
        builder.setMessage("쪽지를 보내시겠습니까?");
        builder.setPositiveButton("예",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {

                        WriteBatch batch = firebaseFirestore.batch();
                        DocumentReference message = firebaseFirestore.collection("messages").document();
                        Map<String, Object> docData = new HashMap<>();
                        docData.put("id",message.getId());
                        docData.put("sender_id", firebaseAuth.getUid());
                        docData.put("receiver_id", receiver_id);
                        docData.put("sender_nickname",sender_nickname);
                        docData.put("receiver_nickname",receiver_nickname);
                        docData.put("context",context.getText().toString());

                        SimpleDateFormat s = new SimpleDateFormat("yyyyMMddkkmmss");
                        String format = s.format(new Date());

                        docData.put("created_at",format);
                        docData.put("status","active");
                        batch.set(message, docData);
                        batch.commit();
                        Toast.makeText(getApplicationContext(),"쪽지보내기가 완료되었습니다.",Toast.LENGTH_LONG).show();
                        finish();





                    }
                });
        builder.setNegativeButton("아니오",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        Toast.makeText(getApplicationContext(),"아니오를 선택했습니다.",Toast.LENGTH_LONG).show();
                        finish();
                    }
                });
        builder.show();
    }
}
