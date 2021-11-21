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
        super(title, launchYear, genres, cast);
        this.nrSeasons = nrSeasons;
        this.seasons = seasons;
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
    }

    @Override
    public String toString() {
        return "Serial{" +
                "nrSeasons=" + nrSeasons +
                ", seasons=" + seasons +
                '}';
    }
}
