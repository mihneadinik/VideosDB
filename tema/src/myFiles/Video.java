package myFiles;

import entertainment.Genre;

import java.util.ArrayList;
import java.util.List;

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
    public double overallRating;
    public int duration;
    public int position;

    public Video(Video otherVideo) {
        this.title = otherVideo.getTitle();
        this.launchYear = otherVideo.getLaunchYear();
        this.genres = otherVideo.getGenres();
        this.cast = otherVideo.getCast();
        this.nrViews = otherVideo.getNrViews();
        this.nrFavorites = otherVideo.getNrFavorites();
        this.overallRating = otherVideo.getOverallRating();
        this.duration = otherVideo.getDuration();
        this.position = otherVideo.getPosition();
    }

    public Video() {
        this.title = "";
        this.launchYear = 0;
        this.genres = null;
        this.cast = null;
        this.nrViews = 0;
        this.nrFavorites = 0;
        this.overallRating = -1;
        this.duration = 0;
        this.position = 0;
    }

    public Video(final String title, final int launchYear, final ArrayList<String> genres, final ArrayList<String> cast, final int duration) {
        this.title = title;
        this.launchYear = launchYear;
        this.genres = genres;
        this.cast = cast;
        this.duration = duration;
        this.nrViews = 0;
        this.nrFavorites = 0;
        this.overallRating = -1;
        this.position = 0;
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
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

    public int getDuration() {
        return duration;
    }

    public int getNrFavorites() {
        return nrFavorites;
    }

    public double getOverallRating() {
        return overallRating;
    }

    public void addView() {
        this.nrViews++;
    }

    public void addFavorite() {
        this.nrFavorites++;
    }

    public boolean checkFilters(final List<List<String>> filters) {
        // get the filters that apply to videos
        if (filters == null)
            return true;
        List<String> year = filters.get(0);
        List<String> genre = filters.get(1);
        if (year != null) {
            if (year.get(0) != null && this.launchYear != Integer.parseInt(year.get(0)))
                return false;
        }
        if (genre != null) {
            for (int i = 0; genre.get(i) != null && !this.genres.contains(genre.get(i)); i++)
                return false;
        }
        return true;
    }

    public boolean checkGenre(String genre) {
        if (this.genres.contains(genre))
            return true;
        return false;
    }

    @Override
    public String toString() {
        return "Video{" +
                "title='" + title + '\'' +
                ", overallRating=" + overallRating +
                '}';
    }
}
