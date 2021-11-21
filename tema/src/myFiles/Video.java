package myFiles;

import entertainment.Genre;

import java.util.ArrayList;

/**
 * base class that will be extended
 * by movies and shows
 */

public class Video {
    public final String title;
    public final int launchYear;
    public final ArrayList<String> genres;
    public final ArrayList<String> cast;
    public int nrViews;
    public int nrFavorites;

    public Video() {
        this.title = "";
        this.launchYear = 0;
        this.genres = null;
        this.cast = null;
        this.nrViews = 0;
        this.nrFavorites = 0;
    }

    public Video(final String title, final int launchYear, final ArrayList<String> genres, final ArrayList<String> cast) {
        this.title = title;
        this.launchYear = launchYear;
        this.genres = genres;
        this.cast = cast;
        this.nrViews = 0;
        this.nrFavorites = 0;
    }

    public String getTitle() {
        return title;
    }

    public int getLaunchYear() {
        return launchYear;
    }

    public ArrayList<String> getGenres() {
        return genres;
    }

    public ArrayList<String> getCast() {
        return cast;
    }

    public int getNrViews() {
        return nrViews;
    }

    public int getNrFavorites() {
        return nrFavorites;
    }

    public void addView() {
        this.nrViews++;
    }

    public void addFavorite() {
        this.nrFavorites++;
    }

    @Override
    public String toString() {
        return "Video{" +
                "title='" + title + '\'' +
                ", launchYear=" + launchYear +
                ", genres=" + genres +
                ", cast=" + cast +
                '}';
    }
}
