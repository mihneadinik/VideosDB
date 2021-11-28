package watchables;

import entertainment.Season;

import java.util.ArrayList;
import java.util.List;

public final class Serial extends Video {
    private final int nrSeasons;
    private final List<Season> seasons;

    public Serial() {
        this.nrSeasons = 0;
        this.seasons = null;
    }

    public Serial(final int nrSeasons, final ArrayList<Season> seasons) {
        this.nrSeasons = nrSeasons;
        this.seasons = seasons;
    }

    public Serial(final String title, final int launchYear, final ArrayList<String> genres,
                  final ArrayList<String> cast, final int nrSeasons,
                  final ArrayList<Season> seasons) {
        super(title, launchYear, genres, cast, -1);
        this.nrSeasons = nrSeasons;
        this.seasons = seasons;
        computeDuration(seasons);
    }

    public int getDuration() {
        return this.duration;
    }

    public List<Season> getSeasons() {
        return seasons;
    }

    /**
     * getter for a cerain season
     * @param seasonNumber the number of the season
     * that should be extracted
     * @return Season object -> the wanted season
     */
    public Season getSeason(final int seasonNumber) {
        if (seasonNumber > this.nrSeasons) {
            return null;
        }
        return this.seasons.get(seasonNumber - 1);
    }

    /**
     * getter for the average serial's rating
     * @return double value -> the average rating
     * of all serial's seasons
     */
    public double getOverallRating() {
        computeRating();
        return overallRating;
    }

    /**
     * function that computes the average rating
     * of all the serial's seasons if at least one
     * of them has been rated by a user
     */
    public void computeRating() {
        boolean ok = false;
        double totalRatingSum = 0;
        // if serial has no seasons => it has no ratings, exit
        if (this.seasons == null) {
            return;
        }
        for (Season season : this.seasons) {
            // compute rating for each season
            double seasonAvgRating = 0;
            // at least one season has rating
            if (season.getRatings().size() != 0) {
                // compute average
                for (Double rate : season.getRatings()) {
                    seasonAvgRating += rate;
                }
                ok = true;
                seasonAvgRating /= season.getRatings().size();
                totalRatingSum += seasonAvgRating;
            }
        }
        // at least one season has rating
        if (ok) {
            this.overallRating = totalRatingSum / this.nrSeasons;
        }
    }

    private void computeDuration(final ArrayList<Season> copySeasons) {
        for (Season season : copySeasons) {
            this.duration += season.getDuration();
        }
    }

    /**
     * function that adds a new rating received
     * from a user to the serial's season
     * @param seasonNumber the number of the rated season
     * @param rating the value of the rating received
     */
    public void addRating(final int seasonNumber, final Double rating) {
        Season season = getSeason(seasonNumber);
        // if the season was invalid
        if (season == null) {
            return;
        }
        // get the season's rating list and update it
        List<Double> seasonRatings = season.getRatings();
        seasonRatings.add(rating);
        season.setRatings(seasonRatings);
        computeRating();
    }
}
