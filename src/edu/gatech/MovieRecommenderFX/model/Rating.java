package edu.gatech.MovieRecommenderFX.model;

import java.util.HashMap;
import java.util.Map;

public class Rating implements Comparable<Rating> {
    private final float rating;
    private final String comment;
    private final User poster;

    public Rating(float rating, String comment, User poster) {
        this.rating = rating;
        this.comment = comment;
        this.poster = poster;
    }

    public float getRating() { return rating; }
    public String getComment() { return comment; }
    public User getPoster() { return poster; }

    public Map<String, String> toMap() {
        Map<String, String> toMap = new HashMap<String, String>();

        toMap.put("rating", String.valueOf(rating));
        toMap.put("comment", comment);
        toMap.put("user", poster.getUsername());

        return toMap;
    }

    @Override
    public String toString() {
        return "Rating{" +
                "rating=" + rating +
                ", comment='" + comment + '\'' +
                ", poster=" + poster +
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
        if (comment != null ? !comment.equals(rating1.comment) : rating1.comment != null)
            return false;
        return poster != null ? poster.equals(rating1.poster) : rating1.poster == null;

    }

    @Override
    public int hashCode() {
        int result = (rating != +0.0f ? Float.floatToIntBits(rating) : 0);
        result = 31 * result + (comment != null ? comment.hashCode() : 0);
        result = 31 * result + (poster != null ? poster.hashCode() : 0);
        return result;
    }
}
