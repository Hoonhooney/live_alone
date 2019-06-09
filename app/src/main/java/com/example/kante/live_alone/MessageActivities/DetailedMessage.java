package com.example.kante.live_alone.MessageActivities;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.beardedhen.androidbootstrap.BootstrapLabel;
import com.example.kante.live_alone.Adapters.DetailedMessageAdapter;
import com.example.kante.live_alone.Classes.Message;
import com.example.kante.live_alone.Classes.User;
import com.example.kante.live_alone.R;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.WriteBatch;

import java.text.SimpleDateFormat;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DetailedMessage extends AppCompatActivity {

    private FirebaseFirestore firebaseFirestore;
    private FirebaseAuth firebaseAuth;
    private ListView messageListView;
    private Message message;
    private List<Message> messages;
    private List<Message> new_messages;
    private DetailedMessageAdapter adapter;
    private BootstrapLabel bootstrapLabel;
    private ImageButton sendMessagebtn;
    private String target_id;
    private TextView context;
    private String receiver_nickname;
    private String sender_nickname;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detailed_message);
        firebaseFirestore = FirebaseFirestore.getInstance();
        firebaseAuth = FirebaseAuth.getInstance();
        messageListView = findViewById(R.id.detailed_message_listview);
        bootstrapLabel = findViewById(R.id.detailed_message_label);
        sendMessagebtn = findViewById(R.id.btn_sendmessage_in_detail);
        context = findViewById(R.id.context_in_detailed_message);
        sendMessagebtn = findViewById(R.id.btn_sendmessage_in_detail);

        Intent intent = getIntent();
        message =  (Message) intent.getSerializableExtra("messageObject");
        if(message.getSender_id().equals(firebaseAuth.getUid())){
            bootstrapLabel.setText(message.getReceiver_nickname()+"님과의 쪽지");
            target_id = message.receiver_id;
        }else{
            bootstrapLabel.setText(message.getSender_nickname()+"님과의 쪽지");
            target_id = message.sender_id;
        }
        getMessages();

        firebaseFirestore.collection("users")
                .document(firebaseAuth.getUid()).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                sender_nickname = documentSnapshot.toObject(User.class).getNickname();
            }
        });
        firebaseFirestore.collection("users")
                .document(target_id).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                receiver_nickname = documentSnapshot.toObject(User.class).getNickname();
            }
        });

    }

    public void getMessages(){
        firebaseFirestore.collection("messages").whereEqualTo("sender_id",message.getSender_id())
                .whereEqualTo("receiver_id",message.getReceiver_id()).get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                messages = queryDocumentSnapshots.toObjects(Message.class);
                Log.d("qweqwe",String.valueOf(messages.size()));
                firebaseFirestore.collection("messages").whereEqualTo("receiver_id",message.getSender_id())
                        .whereEqualTo("sender_id",message.getReceiver_id()).get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        new_messages = queryDocumentSnapshots.toObjects(Message.class);
                        messages.addAll(new_messages);
                        Log.d("qweqwe",String.valueOf(messages.size()));
                        messages.sort(new MessageComparator().reversed());
                        adapter = new DetailedMessageAdapter(DetailedMessage.this, messages);
                        messageListView.setAdapter(adapter);
                        setListViewHeightBasedOnChildren(messageListView);
                    }
                });
            }
        });

    }
    public void setListViewHeightBasedOnChildren(ListView listView) {
        DetailedMessageAdapter listAdapter = (DetailedMessageAdapter) listView.getAdapter();
        if (listAdapter == null) {
            messageListView.setVisibility(View.GONE);
            // pre-condition
            return;
        }

        int totalHeight = 0;
        int desiredWidth = View.MeasureSpec.makeMeasureSpec(listView.getWidth(), View.MeasureSpec.AT_MOST);

        for (int i = 0; i < listAdapter.getCount(); i++) {
            View listItem = listAdapter.getView(i, null, listView);
            listItem.measure(desiredWidth, View.MeasureSpec.UNSPECIFIED);
            totalHeight += listItem.getMeasuredHeight();
        }

        ViewGroup.LayoutParams params = listView.getLayoutParams();
        params.height = totalHeight + (listView.getDividerHeight() * (listAdapter.getCount() - 1));
        listView.setLayoutParams(params);
        listView.requestLayout();
    }

    public class MessageComparator implements Comparator<Message> {
        @Override
        public int compare(Message o1, Message o2) {
            return o2.getCreated_at().compareTo(o1.getCreated_at());
        }
    }

    public void onClicksendMessageinDetail(View v){
        if(!context.getText().toString().isEmpty()){
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
                            docData.put("receiver_id", target_id);
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
                            startActivity(getIntent());


                        }
                    });
            builder.setNegativeButton("아니오",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            Toast.makeText(getApplicationContext(),"아니오를 선택했습니다.",Toast.LENGTH_LONG).show();
                            finish();
                            startActivity(getIntent());
                        }
                    });
            builder.show();
        }
    }

}
