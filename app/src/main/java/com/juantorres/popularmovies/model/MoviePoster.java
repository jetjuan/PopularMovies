package com.juantorres.popularmovies.model;

/**
 * Created by juantorres on 8/2/17.
 */

public class MoviePoster {
    private int id;
    private String title;
    private String posterPath;

    public MoviePoster(){
        this.id = 0;
        this.title = "";
        this.posterPath = "";
    }

    public MoviePoster(int id, String title, String posterPath){
        this.id = id;
        this.title = title;
        this.posterPath = posterPath;
    }

    public int getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getPosterPath() {
        return posterPath;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setPosterPath(String posterPath) {
        this.posterPath = posterPath;
    }
}
