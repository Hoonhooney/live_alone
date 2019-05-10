package com.example.kante.live_alone;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

import com.google.firebase.database.Exclude;
import com.google.firebase.database.IgnoreExtraProperties;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

@IgnoreExtraProperties
public class Post {
    public String image_url;
    public String user_id;
    public String title;
    public String body;
    public Bitmap bitmap;

    public Post() {
        // Default constructor required for calls to DataSnapshot.getValue(Post.class)
    }

    public Post(String uid, String title, String body) {
        this.user_id = uid;
        this.title = title;
        this.body = body;
    }

    public String getImageURL() {
        return image_url;
    }
    String getTitle() {
        return this.title;
    }
    String getBody() {
        return this.body;
    }
    String getUid() { return this.user_id;}

    @Exclude
    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();
        result.put("user_id", user_id);
        result.put("title", title);
        result.put("body", body);
        result.put("image_url", image_url);
        return result;
    }
}
