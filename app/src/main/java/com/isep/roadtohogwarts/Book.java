package com.isep.roadtohogwarts;

import android.util.Log;

import org.json.JSONObject;

public class Book {
    private int id;
    private String title;
    private String author;
    private String publishingDateUK;
    private String url;


    public Book(JSONObject book) {
        try {
            this.id = book.getInt("id");
            this.author = book.getString("author");
            this.title = book.getString("title");
            this.publishingDateUK = book.getJSONArray("publish_date").getJSONObject(0).getString("UK");
            this.url =book.getJSONArray("book_covers").getJSONObject(0).getString("URL");
            this.url=url.replace("\\","");


        }catch (Exception err){
            err.printStackTrace();
        }

    }

    public int getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getAuthor() {
        return author;
    }

    public String getPublishingDateUK() {
        return publishingDateUK;
    }

    public String getUrl() {
        return url;
    }
}
