package entities;

import common.Constants;
import databases.Database;
import watchables.Movie;
import watchables.Serial;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.HashMap;

public final class User {
    private final String username;
    private final String subscription;
    private Map<String, Integer> history;
    private List<String> favorites;
    private int nrRatings;
    private Map<String, ArrayList<Integer>> ratedVideos;

    public User() {
        this.username = "";
        this.subscription = "";
        this.history = null;
        this.favorites = null;
        this.nrRatings = 0;
        this.ratedVideos = null;
    }

    public User(final String username, final String subscription) {
        this.username = username;
        this.subscription = subscription;
        this.history = new HashMap<String, Integer>();
        this.favorites = new ArrayList<String>();
        this.nrRatings = 0;
        this.ratedVideos = new HashMap<String, ArrayList<Integer>>();
    }

    public User(final String username, final String subscription,
                final Map<String, Integer> history, final ArrayList<String> favorites) {
        this.username = username;
        this.subscription = subscription;
        setHistory(history);
        setFavorites(favorites);
        this.nrRatings = 0;
        this.ratedVideos = new HashMap<String, ArrayList<Integer>>();
    }

    public String getUsername() {
        return username;
    }

    public String getSubscription() {
        return subscription;
    }

    public Map<String, Integer> getHistory() {
        return history;
    }

    public int getNrRatings() {
        return nrRatings;
    }

    private void setHistory(final Map<String, Integer> history) {
        this.history = history;
        for (Map.Entry<String, Integer> entry : this.history.entrySet()) {
            if (Database.getInstance().getMovie(entry.getKey()) != null) {
                for (int i = 0; i < entry.getValue(); i++) {
                    Database.getInstance().getMovie(entry.getKey()).addView();
                }
            } else {
                for (int i = 0; i < entry.getValue(); i++) {
                    Database.getInstance().getSerial(entry.getKey()).addView();
                }
            }
        }
    }

    private void setFavorites(final List<String> favorites) {
        this.favorites = favorites;
        for (String name : this.favorites) {
            if (Database.getInstance().getMovie(name) != null) {
                Database.getInstance().getMovie(name).addFavorite();
            } else {
                Database.getInstance().getSerial(name).addFavorite();
            }
        }
    }

    /**
     * function that checks if the user has viewed
     * a certain video based on his/her history Map
     * @param title the name of the video to be checked
     * @return true/false
     */
    public boolean hasViewed(final String title) {
        if (this.history.containsKey(title)) {
            return true;
        }
        return false;
    }

    /**
     * function that checks if the user has added
     * a cerain video to the favorites list
     * @param title the name of the video to be checked
     * @return true/false
     */
    public boolean hasFavorite(final String title) {
        if (this.favorites.contains(title)) {
            return true;
        }
        return false;
    }

    /**
     * function that checks if the user has  viewed
     * a certain video based on his/her history Map
     * @param title the name of the video to be checked
     * @param season the number of the season (if title -> serial)
     * @return true/false
     */
    public boolean hasRated(final String title, final int season) {
        // doesn't show in the list, so it hasn't been rated
        if (!this.ratedVideos.containsKey(title)) {
            return false;
        }
        // it is in the list, but it may be a serial and the certain season
        // may have not been rated
        ArrayList<Integer> seasons = this.ratedVideos.get(title);
        // if it is not a serial, then it is a movie and has been rated
        if (seasons == null) {
            return true;
        }
        // if it is a serial and the season has been rated
        if (seasons.contains(season)) {
            return true;
        }
        return false;
    }

    /**
     * function that adds a video to the favorites
     * list if it has been viewed and it is not
     * already present in the list
     * @param title the name of the video to be added
     * @return error/success message
     */
    public String addToFavorites(final String title) {
        if (hasViewed(title)) {
            if (!hasFavorite(title)) {
                this.favorites.add(title);
                // increment nr of favorites for current video
                Database.getInstance().getVideo(title).addFavorite();
                return "success -> " + title + " was added as favourite";
            }
            // already in favorites
            return "error -> " + title + " is already in favourite list";
        }
        // not viewed
        return "error -> " + title + " is not seen";
    }

    /**
     * function that adds a video to the history
     * Map and increments its number of views
     * @param title the name of the video to be viewed
     * @return message to be parsed as JSON
     */
    public String addToHistory(final String title) {
        if (!hasViewed(title)) {
            this.history.put(title, 1);
        } else {
            this.history.replace(title, this.history.get(title) + 1);
        }
        // increment number of views for current video
        Database.getInstance().getVideo(title).addView();
        return "success -> " + title + " was viewed with total views of " + this.history.get(title);
    }

    /**
     * function that rates a movie if it has been
     * previously viewed and not yet rated
     * @param movie the name of the movie to be rated
     * @param rating the value of the rate
     * @return error/success message
     */
    public String rateMovie(final Movie movie, final Double rating) {
        if (hasViewed(movie.getTitle())) {
            if (!hasRated(movie.getTitle(), 0)) {
                movie.addRating(rating);
                this.nrRatings++;
                this.ratedVideos.put(movie.getTitle(), null);
                return "success -> " + movie.getTitle() + " was rated with "
                        + rating + " by " + this.username;
            }
            // already rated
            return "error -> " + movie.getTitle() + " has been already rated";
        }
        // user has not seen this video
        return "error -> " + movie.getTitle() + " is not seen";
    }

    /**
     * function that rates a serial's season if it
     * has been previously viewed and not yet rated
     * @param serial the name of the serial to be rated
     * @param season the season to be rated
     * @param rating the value of the rate
     * @return error/success message
     */
    public String rateSerial(final Serial serial, final int season, final Double rating) {
        if (hasViewed(serial.getTitle())) {
            if (!hasRated(serial.getTitle(), season)) {
                serial.addRating(season, rating);
                this.nrRatings++;
                ArrayList<Integer> seasons;
                if (this.ratedVideos.containsKey(serial.getTitle())) {
                    // other seasons have been rated
                    seasons = this.ratedVideos.get(serial.getTitle());
                    seasons.add(season);
                    this.ratedVideos.replace(serial.getTitle(), seasons);
                } else {
                    // if this is the first rated season in the serial
                    seasons = new ArrayList<Integer>();
                    seasons.add(season);
                    this.ratedVideos.put(serial.getTitle(), seasons);
                }
                return "success -> " + serial.getTitle() + " was rated with "
                        + rating + " by " + this.username;
            }
            // already rated this season
            return "error -> " + serial.getTitle() + " has been already rated";
        }
        // user has not seen this video
        return "error -> " + serial.getTitle() + " is not seen";
    }

    /**
     * function that checks the subscription
     * type of a user
     * @return true/false
     */
    public boolean isPremium() {
        if (this.subscription.equalsIgnoreCase(Constants.PREMIUM)) {
            return true;
        }
        return false;
    }
}
