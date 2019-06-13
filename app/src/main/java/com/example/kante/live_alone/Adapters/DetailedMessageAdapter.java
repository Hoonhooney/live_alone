package com.example.kante.live_alone.Adapters;

import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.kante.live_alone.Classes.Message;
import com.example.kante.live_alone.R;
import com.google.firebase.auth.FirebaseAuth;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;


public class DetailedMessageAdapter extends BaseAdapter {

    private Context context;
    private List<Message> messages;
    private FirebaseAuth user;

    public DetailedMessageAdapter(Context context, List<Message> objects) {
        user = FirebaseAuth.getInstance();
        this.context = context;
        this.messages = objects;
    }


    @Override
    public int getCount() {
        return messages.size();
    }

    @Override
    public Object getItem(int position) {
        return messages.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        context = parent.getContext();
        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.item_detailed_messages, parent, false);
        }

        TextView uId = (TextView) convertView.findViewById(R.id.detailed_message_nickname);
        TextView cBody = (TextView) convertView.findViewById(R.id.detailed_message_context);
        TextView cTime = (TextView) convertView.findViewById(R.id.detailed_message_send_time);
        LinearLayout linearLayout = convertView.findViewById(R.id.detailed_message_linearlayout);
        LinearLayout item_detailed_message_layout = convertView.findViewById(R.id.item_detailed_message_layout);
        Message message = messages.get(position);

        if(message.getSender_id().equals(user.getUid())){
            uId.setText("나");
            linearLayout.removeViewAt(1);
            linearLayout.removeViewAt(0);
            linearLayout.addView(cTime,0);
            linearLayout.addView(uId,1);
            uId.setGravity(Gravity.RIGHT);
            cTime.setGravity(Gravity.LEFT);
            cBody.setGravity(Gravity.RIGHT);
            item_detailed_message_layout.setGravity(Gravity.RIGHT);
        }else{
            uId.setText(message.getSender_nickname());
        }
        cBody.setText(message.getContext());
        try {
            Date date = new SimpleDateFormat("yyyyMMddkkmmss").parse(message.getCreated_at());
            String format = new SimpleDateFormat("yyyy/MM/dd kk:mm").format(date);
            cTime.setText(format);
        } catch (ParseException pe) {
            pe.printStackTrace();
        }
        return convertView;
    }


}
