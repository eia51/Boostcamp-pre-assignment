package kr.or.connect.boostcamp;

import android.databinding.BaseObservable;
import android.databinding.Bindable;


public class Movie extends BaseObservable {
    private String title;
    private String link;
    private String image;
    private String pubDate;
    private String director;
    private String actor;
    private float userRating;

    public Movie(String title, String link, String image, String pubDate, String director, String actors, float userRating) {
        this.title = title;
        this.link = link;
        this.image = image;
        this.pubDate = pubDate;
        this.director = director;
        this.actor = actors;
        this.userRating = userRating;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    @Bindable
    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public String getImageURL() {
        return image;
    }

    public void setImageURL(String imageURL) {
        this.image = imageURL;
    }

    public String getPubDate() {
        return pubDate;
    }

    public void setPubDate(String pubDate) {
        this.pubDate = pubDate;
    }

    public String getDirector() {
        return director;
    }

    public void setDirector(String director) {
        this.director = director;
    }

    public String getActors() {
        return actor;
    }

    public void setActors(String actors) {
        this.actor = actors;
    }

    public float getUserRating() {
        return userRating;
    }

    public void setUserRating(float userRating) {
        this.userRating = userRating;
    }
}
