package myFiles;

import entertainment.Genre;
import entertainment.Season;

import java.util.ArrayList;
import java.util.List;

public class Serial extends Video{
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

    public Serial(final String title, final int launchYear, final ArrayList<String> genres, final ArrayList<String> cast, final int nrSeasons, final ArrayList<Season> seasons) {
        super(title, launchYear, genres, cast, -1);
        this.nrSeasons = nrSeasons;
        this.seasons = seasons;
        computeDuration(seasons);
    }

    private void computeDuration(final ArrayList<Season> seasons) {
        for (Season season : seasons)
            this.duration += season.getDuration();
    }

    public int getDuration() {
        return this.duration;
    }

    public void computeRating() {
        boolean ok = false;
        double totalRatingSum = 0;
        // if serial has no seasons => it has no ratings, exit
        if (this.seasons == null)
            return;
        for (Season season : this.seasons) {
            // facem rating pt fiecare sezon in parte
            double seasonAvgRating = 0;
            // cel putin un sezon are rating si e calculabil
            if (season.getRatings().size() != 0) {
                // luam ratingurile sezonului sa le facem media
                for (Double rate : season.getRatings())
                    seasonAvgRating += rate;
                ok = true;
                seasonAvgRating /= season.getRatings().size();
                totalRatingSum += seasonAvgRating;
            }
        }
        if (ok) {
            // daca cel putin un sezon are rating
            this.overallRating = totalRatingSum / this.nrSeasons;
        }
    }

    public int getNrSeasons() {
        return nrSeasons;
    }

    public double getOverallRating() {
        computeRating();
        return overallRating;
    }

    public List<Season> getSeasons() {
        return seasons;
    }

    public Season getSeason(int seasonNumber) {
        if (seasonNumber > this.nrSeasons)
            return null;
        return this.seasons.get(seasonNumber - 1);
    }

    public void addRating(int seasonNumber, Double rating) {
        Season season = getSeason(seasonNumber);
        // if the season was invalid
        if (season == null)
            return;
        // get the season's rating list and update it
        List<Double> seasonRatings = season.getRatings();
        seasonRatings.add(rating);
        season.setRatings(seasonRatings);
        computeRating();
    }

    @Override
    public String toString() {
        return "Serial{" +
                "nrSeasons=" + nrSeasons +
                ", seasons=" + seasons +
                '}';
    }
}
