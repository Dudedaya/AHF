package xyz.dudedaya.retrofitexample;

import com.google.gson.annotations.SerializedName;

public class Post {
    private int userId;

    private Integer id; //Integer is set to make this field nullable. So we don't send it to the server.

    private String title;

    @SerializedName("body")
    private String text;

    public Post(int userId, String title, String text) {
        this.userId = userId;
        this.title = title;
        this.text = text;
    }

    public int getUserId() {
        return userId;
    }

    public int getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getText() {
        return text;
    }
}
