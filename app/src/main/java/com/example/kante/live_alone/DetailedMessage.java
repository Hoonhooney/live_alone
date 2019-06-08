package com.example.kante.live_alone;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.beardedhen.androidbootstrap.BootstrapLabel;
import com.example.kante.live_alone.Adapters.DetailedMessageAdapter;
import com.example.kante.live_alone.Adapters.MessageAdapter;
import com.example.kante.live_alone.Classes.Message;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.Comparator;
import java.util.List;

public class DetailedMessage extends AppCompatActivity {

    private FirebaseFirestore firebaseFirestore;
    private FirebaseAuth firebaseAuth;
    private ListView messageListView;
    private Message message;
    private List<Message> messages;
    private List<Message> new_messages;
    private DetailedMessageAdapter adapter;
    private BootstrapLabel bootstrapLabel;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detailed_message);
        firebaseFirestore = FirebaseFirestore.getInstance();
        firebaseAuth = FirebaseAuth.getInstance();
        messageListView = findViewById(R.id.detailed_message_listview);
        bootstrapLabel = findViewById(R.id.detailed_message_label);

        Intent intent = getIntent();
        message =  (Message) intent.getSerializableExtra("messageObject");
        if(message.getSender_id().equals(firebaseAuth.getUid())){
            bootstrapLabel.setText(message.getReceiver_nickname()+"님과의 쪽지");
        }else{
            bootstrapLabel.setText(message.getSender_nickname()+"님과의 쪽지");
        }
        getMessages();

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
}
