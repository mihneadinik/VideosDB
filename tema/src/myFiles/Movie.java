package myFiles;

import entertainment.Genre;

import java.util.ArrayList;
import java.util.List;

public class Movie extends Video{
//    private final int duration;
    private List<Double> ratings;

    public Movie() {
        this.duration = 0;
        this.ratings = null;
    }

    public Movie(final String title, final int launchYear, final ArrayList<String> genres, final ArrayList<String> cast, final int duration) {
        super(title, launchYear, genres, cast, duration);
//        this.duration = duration;
        this.ratings = new ArrayList<Double>();
    }

    public void computeRating() {
        // daca nu avem lista de ratings iesim
        if (this.ratings == null)
            return;

        double ratingSum = 0;
        for (Double rate : this.ratings) {
            ratingSum += rate;
        }

        // daca a primit ratinguri
        if (this.ratings.size() != 0) {
            this.overallRating = ratingSum / this.ratings.size();
        }
    }

    public int getDuration() {
        return duration;
    }

    public double getOverallRating() {
        computeRating();
        return overallRating;
    }

    public List<Double> getRatings() {
        return ratings;
    }

    public void addRating(Double rating) {
        this.ratings.add(rating);
        computeRating();
    }

    @Override
    public String toString() {
        return "Movie{" +
                "title='" + title + '\'' +
                ", launchYear=" + launchYear +
                ", genres=" + genres +
                ", nrViews=" + nrViews +
                '}';
    }
}
