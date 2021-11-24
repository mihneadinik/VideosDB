package myFiles;

import common.Constants;

import java.util.*;

public class User {
    private final String username;
    private final String subscription;
    private Map<String, Integer> history;
    private List<String> favorites;
    int nrRatings;
    private Map<String, ArrayList<Integer>> ratedVideos; // la string e numele video, in dreapta e null pt film si lista de int cu sezoane pt serial

    public User() {
        this.username = "";
        this.subscription = "";
        this.history = null;
        this.favorites = null;
        this.nrRatings = 0;
        this.ratedVideos = null;
    }

    public User(String username, String subscription) {
        this.username = username;
        this.subscription = subscription;
        this.history = new HashMap<String, Integer>();
        this.favorites = new ArrayList<String>();
        this.nrRatings = 0;
        this.ratedVideos = new HashMap<String, ArrayList<Integer>>();
    }

    public User(String username, String subscription, Map<String, Integer> history, ArrayList<String> favorites) {
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

    private void setHistory(Map<String, Integer> history) {
        this.history = history;
        for (Map.Entry<String, Integer> entry : this.history.entrySet()) {
            if (Database.getInstance().getMovie(entry.getKey()) != null) {
                for (int i = 0; i < entry.getValue(); i++)
                    Database.getInstance().getMovie(entry.getKey()).addView();
            } else {
                for (int i = 0; i < entry.getValue(); i++)
                    Database.getInstance().getSerial(entry.getKey()).addView();
            }
        }
    }

    public List<String> getFavorites() {
        return favorites;
    }

    private void setFavorites(List<String> favorites) {
        this.favorites = favorites;
        for (String name : this.favorites) {
            if (Database.getInstance().getMovie(name) != null)
                Database.getInstance().getMovie(name).addFavorite();
            else
                Database.getInstance().getSerial(name).addFavorite();
        }
    }

    // check if user has viewed certain video
    public boolean hasViewed(String title) {
        if (this.history.containsKey(title))
            return true;
        return false;
    }

    // check if user has added certain video to favs
    public boolean hasFavorite(String title) {
        if (this.favorites.contains(title))
            return true;
        return false;
    }

    // check if user has already rated certain video
    public boolean hasRated(String title, int season) {
        // nu apare deloc in lista, deci nu aa dat rate
        if (!this.ratedVideos.containsKey(title))
            return false;
        // aici deja apare in lista, dar poate e vorba de serial
        ArrayList<Integer> seasons = this.ratedVideos.get(title);
        // daca nu e vorba de serial, inseamna ca e film si are rate
        if (seasons == null)
            return true;
        // daca am dat deja rate la acest sezon
        if (seasons.contains(season))
            return true;
        return false;
    }

    // check if video has been previously viewed and add it to favorites (if it is not already there)
    public String addToFavorites(String title) {
        if (hasViewed(title)) {
            if (!hasFavorite(title)) {
                this.favorites.add(title);
                // increment nr of favorites for curr movie
                Database.getInstance().getVideo(title).addFavorite();
                return "success -> " + title + " was added as favourite";
            }
            // e deja la favorite
            return "error -> " + title + " is already in favourite list";
        }
        // nu e vizionat
        return "error -> " + title + " is not seen";
    }

    // check if it exists to increment nr of views or create new entry
    public String addToHistory(String title) {
        if (!hasViewed(title))
            this.history.put(title, 1);
        else
            this.history.replace(title, this.history.get(title) + 1);
        // increment nr of views for curr movie
        Database.getInstance().getVideo(title).addView();
        return "success -> " + title + " was viewed with total views of " + this.history.get(title);
    }

    public String rateMovie(Movie movie, Double rating) {
        if (hasViewed(movie.getTitle())) {
            if (!hasRated(movie.getTitle(), 0)) {
                movie.addRating(rating);
                this.nrRatings++;
                this.ratedVideos.put(movie.getTitle(), null);
                return "success -> " + movie.getTitle() + " was rated with " + rating + " by " + this.username;
            }
            // a mai dat deja rate la video
            return "error -> " + movie.getTitle() + " has been already rated";
        }
        // user has not seen this video
        return "error -> " + movie.getTitle() + " is not seen";
    }

    public String rateSerial(Serial serial, int season, Double rating) {
        if (hasViewed(serial.getTitle())) {
            if (!hasRated(serial.getTitle(), season)) {
                serial.addRating(season, rating);
                this.nrRatings++;
                ArrayList<Integer> seasons;
                if (this.ratedVideos.containsKey(serial.getTitle())) {
                    // daca a mai dat rate la alte sezoane
                    seasons = this.ratedVideos.get(serial.getTitle());
                    seasons.add(season);
                    this.ratedVideos.replace(serial.getTitle(), seasons);
                } else {
                    // daca asta e primul sezon la care da rate
                    seasons = new ArrayList<Integer>();
                    seasons.add(season);
                    this.ratedVideos.put(serial.getTitle(), seasons);
                }
                return "success -> " + serial.getTitle() + " was rated with " + rating + " by " + this.username;
            }
            // a mai dat deja rate la sezon
            return "error -> " + serial.getTitle() + " has been already rated";
        }
        // user has not seen this video
        return "error -> " + serial.getTitle() + " is not seen";
    }

    public boolean isPremium() {
        if (this.subscription.equalsIgnoreCase(Constants.PREMIUM))
            return true;
        return false;
    }

    @Override
    public String toString() {
        return "User{" +
                "username='" + username + '\'' +
                ", subscription='" + subscription + '\'' +
                ", history=" + history +
                ", favorites=" + favorites +
                '}';
    }
}
