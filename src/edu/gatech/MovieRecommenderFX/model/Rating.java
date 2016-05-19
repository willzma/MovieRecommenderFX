package edu.gatech.MovieRecommenderFX.model;

import java.util.HashMap;
import java.util.Map;

public class Rating implements Comparable<Rating> {
    private final float rating;
    private final String comment;
    private final User poster;
    private final String date;

    public Rating(float rating, String comment, User poster, String date) {
        this.rating = rating;
        this.comment = comment;
        this.poster = poster;
        this.date = date;
    }

    public float getRating() { return rating; }
    public String getComment() { return comment; }
    public User getPoster() { return poster; }
    public String getDate() { return date; }

    public Map<String, String> toMap() {
        Map<String, String> toMap = new HashMap<String, String>();

        toMap.put("rating", String.valueOf(rating));
        toMap.put("comment", comment);
        toMap.put("user", poster.getUsername());
        toMap.put("date", date);

        return toMap;
    }

    @Override
    public String toString() {
        return "Rating{" +
                "rating=" + rating +
                ", comment='" + comment + '\'' +
                ", poster=" + poster +
                ", date='" + date + '\'' +
                '}';
    }

    @Override
    public int compareTo(Rating o) {
        return Float.compare(o.getRating(), this.getRating());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Rating rating1 = (Rating) o;

        if (Float.compare(rating1.rating, rating) != 0) return false;
        if (!comment.equals(rating1.comment)) return false;
        if (!poster.equals(rating1.poster)) return false;
        return date.equals(rating1.date);
    }

    @Override
    public int hashCode() {
        int result = (rating != +0.0f ? Float.floatToIntBits(rating) : 0);
        result = 31 * result + comment.hashCode();
        result = 31 * result + poster.hashCode();
        result = 31 * result + date.hashCode();
        return result;
    }
}
