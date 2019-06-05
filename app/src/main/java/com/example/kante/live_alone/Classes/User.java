package com.example.kante.live_alone.Classes;

import com.google.firebase.Timestamp;
import com.google.firebase.database.Exclude;
import com.google.firebase.database.IgnoreExtraProperties;

import java.util.HashMap;
import java.util.Map;

@IgnoreExtraProperties
public class User {
    public String user_id;
    public String nickname;
    public String school;
    public Timestamp created_at;

    public User() {
        // Default constructor required for calls to DataSnapshot.getValue(Post.class)
    }

    public User(String uid, String title, String body) {
        this.user_id = uid;
        this.nickname = title;
    }

    public String getUser_id() {
        return user_id;
    }

    public String getNickname() {
        return nickname;
    }

    public String getSchool() {
        return school;
    }

    public Timestamp getCreated_at() {
        return created_at;
    }

    @Exclude
    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();
        result.put("user_id", user_id);
        result.put("nickname", nickname);
        result.put("school",school);
        result.put("created_at",created_at);
        return result;
    }

}
