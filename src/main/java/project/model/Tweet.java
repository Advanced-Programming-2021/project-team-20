package project.model;

import com.google.gson.JsonObject;

public class Tweet {
    private int id;
    private String author;
    private String message;

    public Tweet(int id, String author, String message) {
        this.id = id;
        this.author = author;
        this.message = message;
    }

    public int getId() {
        return id;
    }

    public String getAuthor() {
        return author;
    }

    public String getMessage() {
        return message;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String toGsonString() {
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("id", id + "");
        jsonObject.addProperty("author", author);
        jsonObject.addProperty("message", message);
        return jsonObject.toString();
    }
}
