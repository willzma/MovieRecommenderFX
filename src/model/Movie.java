package model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Movie implements Comparable<Movie> {
    private final String title;
    private List<Rating> ratings;
    private float averageRating;
    private Map<String, List<Float>> majorRatings;
    private String imgURL;

    public Movie(String title) {
        this.title = title;
        ratings = new ArrayList<Rating>();
        majorRatings = new HashMap<String, List<Float>>();
    }

    public void addRating(Rating r) {
        ratings.add(r);

        float aggregateRating = (ratings.size() - 1) * averageRating;

        averageRating = (aggregateRating + ratings.get(ratings.size() - 1).getRating()) / ratings.size();

        String major = r.getPoster().getProfile().getMajor();

        if (majorRatings.containsKey(major)) {
            majorRatings.get(major).add(r.getRating());
        } else {
            majorRatings.put(major, new ArrayList<Float>());
            majorRatings.get(major).add(r.getRating());
        }
    }

    public Map<String, List<Float>> getMajorRatings() { return majorRatings; }
    public void setMajorRatings(Map<String, List<Float>> majorRatings) { this.majorRatings = majorRatings; }

    public float getAverageMajorRating(String major) {
        if (majorRatings.containsKey(major)) {
            List<Float> listRatings = majorRatings.get(major);
            float aggregateRating = 0;

            for (float f : listRatings) {
                aggregateRating += f;
            }

            return aggregateRating / listRatings.size();
        } else {
            return 0;
        }
    }

    public String getURL() { return imgURL; }
    public String getTitle() { return title; }
    public float getAverageRating() { return averageRating; }
    public List<Rating> getRatings() { return ratings; }

    public void setURL(String imgURL) { this.imgURL = imgURL; }
    public void setAverageRating(float averageRating) { this.averageRating = averageRating; }
    public void setRatings(List<Rating> ratings) { this.ratings = ratings; }

    @Override
    public String toString() {
        return "Movie{" +
                "title='" + title + '\'' +
                ", ratings=" + ratings +
                ", averageRating=" + averageRating +
                ", majorRatings=" + majorRatings +
                ", imgURL='" + imgURL + '\'' +
                '}';
    }

    @Override
    public int compareTo(Movie o) {
        return (int) (this.getAverageRating() - o.getAverageRating());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Movie movie = (Movie) o;

        if (Float.compare(movie.averageRating, averageRating) != 0)
            return false;
        if (title != null ? !title.equals(movie.title) : movie.title != null)
            return false;
        if (ratings != null ? !ratings.equals(movie.ratings) : movie.ratings != null)
            return false;
        if (majorRatings != null ? !majorRatings.equals(movie.majorRatings) : movie.majorRatings != null)
            return false;
        return imgURL != null ? imgURL.equals(movie.imgURL) : movie.imgURL == null;

    }

    @Override
    public int hashCode() {
        int result = title != null ? title.hashCode() : 0;
        result = 31 * result + (ratings != null ? ratings.hashCode() : 0);
        result = 31 * result + (averageRating != +0.0f ? Float.floatToIntBits(averageRating) : 0);
        result = 31 * result + (majorRatings != null ? majorRatings.hashCode() : 0);
        result = 31 * result + (imgURL != null ? imgURL.hashCode() : 0);
        return result;
    }
}
