package com.juantorres.popularmovies.model;

/**
 * Created by juantorres on 11/9/17.
 */

public class Trailer {
    private String trailerID;
    private String youtubeKey;
    private String name;

    public Trailer(String trailerID, String youtubeKey, String name){
        this.trailerID = trailerID;
        this.youtubeKey = youtubeKey;
        this.name = name;
    }

    public Trailer(){
        super();
    }

    public String getTrailerID() {
        return trailerID;
    }

    public void setTrailerID(String trailerID) {
        this.trailerID = trailerID;
    }

    public String getYoutubeKey() {
        return youtubeKey;
    }

    public void setYoutubeKey(String youtubeKey) {
        this.youtubeKey = youtubeKey;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
