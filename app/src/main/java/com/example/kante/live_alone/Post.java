package com.example.kante.live_alone;

import com.google.firebase.database.Exclude;
import com.google.firebase.database.IgnoreExtraProperties;

import java.util.HashMap;
import java.util.Map;

@IgnoreExtraProperties
public class Post {
    public String imageURL;
    public String uid;
    public String title;
    public String body;

    public Post() {
        // Default constructor required for calls to DataSnapshot.getValue(Post.class)
    }

    public Post(String uid, String title, String body) {
        this.uid = uid;
        this.title = title;
        this.body = body;
    }

    String getImage() {
        return this.imageURL;
    }
    String getTitle() {
        return this.title;
    }
    String getBody() {
        return this.body;
    }
    String getUid() {
        return this.uid;
    }

    @Exclude
    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();
        result.put("uid", uid);
        result.put("title", title);
        result.put("body", body);

        return result;
    }

}
