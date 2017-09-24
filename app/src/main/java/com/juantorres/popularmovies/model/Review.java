package com.juantorres.popularmovies.model;

/**
 * Created by juantorres on 16/9/17.
 */

public class Review {
    private String reviewID;
    private String author;
    private String content;
    private String url;

    public Review(String reviewID, String author, String content, String url) {
        this.reviewID = reviewID;
        this.author = author;
        this.content = content;
        this.url = url;
    }

    public String getReviewID() {
        return reviewID;
    }

    public void setReviewID(String reviewID) {
        this.reviewID = reviewID;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
