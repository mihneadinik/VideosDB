package watchables;

import common.Constants;

import java.util.ArrayList;
import java.util.List;

/**
 * base class that will be extended
 * by movies and shows
 */

public class Video {
    private final String title;
    private final int launchYear;
    private final ArrayList<String> genres;
    private final ArrayList<String> cast;
    private int nrViews;
    private int nrFavorites;
    protected double overallRating;
    protected int duration;
    private int position;

    public Video(final Video otherVideo) {
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

    public Video(final String title, final int launchYear, final ArrayList<String> genres,
                 final ArrayList<String> cast, final int duration) {
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

    public final int getPosition() {
        return position;
    }

    public final void setPosition(final int position) {
        this.position = position;
    }

    public final String getTitle() {
        return title;
    }

    public final int getLaunchYear() {
        return launchYear;
    }

    public final ArrayList<String> getGenres() {
        return genres;
    }

    public final ArrayList<String> getCast() {
        return cast;
    }

    public final int getNrViews() {
        return nrViews;
    }

    /**
     * function that can be overwritten depending on the
     * actual vide type to correctly compute duration
     * (for serials it is the sum of all seasons' durations)
     * @return the video's duration
     */
    public int getDuration() {
        return duration;
    }

    public final int getNrFavorites() {
        return nrFavorites;
    }

    /**
     * function that can be overwritten depending on the
     * actual vide type to correctly compute overall rating
     * (for serials it is the average of all seasons' ratings)
     * @return the video's overall rating
     */
    public double getOverallRating() {
        return overallRating;
    }

    /**
     * function that increments the number
     * of views for a certain video
     */
    public void addView() {
        this.nrViews++;
    }

    /**
     * function that increments the number
     * of times a video has been added to
     * a user's favortites list
     */
    public void addFavorite() {
        this.nrFavorites++;
    }

    /**
     * function that checks if a video corresponds
     * to certain filters (apparition year or different genres)
     * received as parameter
     * @param filters the list of filters to be checked
     * @return true/false
     */
    public boolean checkFilters(final List<List<String>> filters) {
        // get the filters that apply to videos
        if (filters == null) {
            return true;
        }
        List<String> year = filters.get(Constants.FILTER_NUMBER_YEAR);
        List<String> genre = filters.get(Constants.FILTER_NUMBER_GENRE);
        if (year != null) {
            if (year.get(0) != null && this.launchYear != Integer.parseInt(year.get(0))) {
                return false;
            }
        }
        if (genre != null) {
            for (int i = 0; genre.get(i) != null && !this.genres.contains(genre.get(i)); i++) {
                return false;
            }
        }
        return true;
    }

    /**
     * function that checks if a video is of
     * a certain genre type
     * @param genre the genre to be checked
     * @return true/false
     */
    public boolean checkGenre(final String genre) {
        if (this.genres.contains(genre)) {
            return true;
        }
        return false;
    }
}
