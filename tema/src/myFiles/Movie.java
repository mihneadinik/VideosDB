package myFiles;

import entertainment.Genre;

import java.util.ArrayList;
import java.util.List;

public class Movie extends Video{
    private final int duration;
    private List<Double> ratings;

    public Movie() {
        this.duration = 0;
        this.ratings = null;
    }

    public Movie(final int duration) {
        this.duration = duration;
        this.ratings = new ArrayList<Double>();
    }

    public Movie(final String title, final int launchYear, final ArrayList<String> genres, final ArrayList<String> cast, final int duration) {
        super(title, launchYear, genres, cast);
        this.duration = duration;
        this.ratings = new ArrayList<Double>();
    }

    public int getDuration() {
        return duration;
    }

    public List<Double> getRatings() {
        return ratings;
    }

    public void addRating(Double rating) {
        this.ratings.add(rating);
    }

    @Override
    public String toString() {
        return "Movie{" +
                "duration=" + duration +
                ", ratings=" + ratings +
                '}';
    }
}
