package com.example.gauravagarwal.quotes.Quote;

/**
 * Created by Gaurav Agarwal on 16-02-2017.
 */

public class Quote {
    private String id;
    private String author;
    private String quote;
    private String tags;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getQuote() {
        return quote;
    }

    public void setQuote(String quote) {
        this.quote = quote;
    }

    public String getTags() {
        return tags;
    }

    public void setTags(String tags) {
        this.tags = tags;
    }

    public Quote(String id, String author, String quote, String tags) {
        this.id = id;
        this.author = author;
        this.quote = quote;
        this.tags = tags;
    }
}
