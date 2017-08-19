package com.juantorres.popularmovies.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.Calendar;
import java.util.Date;

/**
 * Created by juantorres on 8/2/17.
 */

public class Movie  implements Parcelable{
    private int id;
    private int voteCount;
    private boolean hasVideo;
    private double voteAverage;
    private String title;
    private double popularity;
    private String posterPath;
    private String originalLanguage;
    private String originalTitle;
    private String backDropPath;
    private String overview;
    private Date releaseDate;

    public Movie(){
        super();
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getVoteCount() {
        return voteCount;
    }

    public void setVoteCount(int voteCount) {
        this.voteCount = voteCount;
    }

    public boolean isHasVideo() {
        return hasVideo;
    }

    public void setHasVideo(boolean hasVideo) {
        this.hasVideo = hasVideo;
    }

    public double getVoteAverage() {
        return voteAverage;
    }

    public void setVoteAverage(double voteAverage) {
        this.voteAverage = voteAverage;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public double getPopularity() {
        return popularity;
    }

    public void setPopularity(double popularity) {
        this.popularity = popularity;
    }

    public String getPosterPath() {
        return posterPath;
    }

    public void setPosterPath(String posterPath) {
        this.posterPath = posterPath;
    }

    public String getOriginalLanguage() {
        return originalLanguage;
    }

    public void setOriginalLanguage(String originalLanguage) {
        this.originalLanguage = originalLanguage;
    }

    public String getOriginalTitle() {
        return originalTitle;
    }

    public void setOriginalTitle(String originalTitle) {
        this.originalTitle = originalTitle;
    }

    public String getBackDropPath() {
        return backDropPath;
    }

    public void setBackDropPath(String backDropPath) {
        this.backDropPath = backDropPath;
    }

    public String getOverview() {
        return overview;
    }

    public void setOverview(String overview) {
        this.overview = overview;
    }

    public Date getReleaseDate(){
        return this.releaseDate;
    }

    public void setReleaseDate(Date date){
        this.releaseDate = date;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(this.id);
        parcel.writeInt(this.voteCount);
        parcel.writeInt(this.hasVideo ? 1 : 0);
        parcel.writeDouble(this.voteAverage);
        parcel.writeString(this.title);
        parcel.writeDouble(this.popularity);
        parcel.writeString(this.posterPath);
        parcel.writeString(this.originalLanguage);
        parcel.writeString(this.originalTitle);
        parcel.writeString(this.backDropPath);
        parcel.writeString(this.overview);
        parcel.writeLong(this.releaseDate.getTime());
    }

    public Movie(Parcel pc){
        this.id                   = pc.readInt();
        this.voteCount            = pc.readInt();
        this.hasVideo             = (pc.readInt() ==1);
        this.voteAverage          = pc.readDouble();
        this.title                = pc.readString();
        this.popularity           = pc.readDouble();
        this.posterPath           = pc.readString();
        this.originalLanguage     = pc.readString();
        this.originalTitle        = pc.readString();
        this.backDropPath         = pc.readString();
        this.overview             = pc.readString();
        this.releaseDate          = new Date(pc.readLong());
    }

    public static final Parcelable.Creator<Movie> CREATOR = new Parcelable.Creator<Movie>() {
        public Movie createFromParcel(Parcel pc) {
            return new Movie(pc);
        }
        public Movie[] newArray(int size) {
            return new Movie[size];
        }
    };

    public int getReleaseYear(){
        Calendar cal = Calendar.getInstance();
        cal.setTime(this.releaseDate);
        return cal.get(Calendar.YEAR);
    }
}
