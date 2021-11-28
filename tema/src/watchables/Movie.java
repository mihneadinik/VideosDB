package watchables;

import java.util.ArrayList;
import java.util.List;

public final class Movie extends Video {
    private List<Double> ratings;

    public Movie() {
        this.duration = 0;
        this.ratings = null;
    }

    public Movie(final String title, final int launchYear, final ArrayList<String> genres,
                 final ArrayList<String> cast, final int duration) {
        super(title, launchYear, genres, cast, duration);
        this.ratings = new ArrayList<Double>();
    }

    public int getDuration() {
        return duration;
    }

    public List<Double> getRatings() {
        return ratings;
    }

    /**
     * getter for the overall rating of a movie
     * @return double value -> the average of the
     * ratings a movie has received from users
     */
    public double getOverallRating() {
        computeRating();
        return overallRating;
    }

    /**
     * function that computes the average
     * rating of a movie based on its list
     * of ratings previously from users
     */
    public void computeRating() {
        if (this.ratings == null) {
            return;
        }

        double ratingSum = 0;
        for (Double rate : this.ratings) {
            ratingSum += rate;
        }

        if (this.ratings.size() != 0) {
            this.overallRating = ratingSum / this.ratings.size();
        }
    }

    /**
     * function that adds a new rating
     * received from a user to the movie's
     * ratings list
     * @param rating the value of the rating received
     */
    public void addRating(final Double rating) {
        this.ratings.add(rating);
        computeRating();
    }
}
