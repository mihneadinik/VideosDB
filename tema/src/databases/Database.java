package databases;

import common.Constants;
import entities.Actor;
import entities.User;
import fileio.SerialInputData;
import fileio.MovieInputData;
import fileio.ActorInputData;
import fileio.UserInputData;
import watchables.Movie;
import watchables.Serial;
import watchables.Video;

import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import java.util.HashMap;
import java.util.Objects;

public final class Database {
    private static Database instance = null;
    private final List<Actor> actorList;
    private final List<User> usersList;
    private final List<Movie> moviesList;
    private final List<Serial> serialsList;

    private Database() {
        this.actorList = new ArrayList<Actor>();
        this.usersList = new ArrayList<User>();
        this.moviesList = new ArrayList<Movie>();
        this.serialsList = new ArrayList<Serial>();
    }

    /**
     * function that creates and returns the singleton
     * reference to the database
     * @return the reference to my database instance
     */
    public static Database getInstance() {
        if (instance == null) {
            instance = new Database();
        }

        return instance;
    }

    /**
     * function that creates the actor instances
     * @param data the list with input information
     * for all the actors
     */
    public void setActors(final List<ActorInputData> data) {
        for (ActorInputData currActor : data) {
            this.actorList.add(new Actor(currActor.getName(), currActor.getCareerDescription(),
                    currActor.getFilmography(), currActor.getAwards()));
        }
    }

    /**
     * function that creates the user instances
     * @param data the list with input information
     * for all the users
     */
    public void setUsers(final List<UserInputData> data) {
        for (UserInputData currUser : data) {
            this.usersList.add(new User(currUser.getUsername(), currUser.getSubscriptionType(),
                    currUser.getHistory(), currUser.getFavoriteMovies()));
        }
    }

    /**
     * function that creates the movie instances
     * @param data the list with input information
     * for all the movies
     */
    public void setMoviesList(final List<MovieInputData> data) {
        for (MovieInputData currMovie : data) {
            this.moviesList.add(new Movie(currMovie.getTitle(), currMovie.getYear(),
                    currMovie.getGenres(), currMovie.getCast(), currMovie.getDuration()));
        }
    }

    /**
     * function that creates the serials instances
     * @param data the list with input information
     * for all the serials
     */
    public void setSerialsList(final List<SerialInputData> data) {
        for (SerialInputData currSerial : data) {
            this.serialsList.add(new Serial(currSerial.getTitle(), currSerial.getYear(),
                    currSerial.getGenres(), currSerial.getCast(),
                    currSerial.getNumberSeason(), currSerial.getSeasons()));
        }
    }

    /**
     * function that returns a certain user from the database
     * @param username the name of the user
     * @return the reference to a user or null if it is
     * not present in the database
     */
    public User getUser(final String username) {
        for (User currUser : this.usersList) {
            if (Objects.equals(currUser.getUsername(), username)) {
                return currUser;
            }
        }
        return null;
    }

    public List<Actor> getActorList() {
        return actorList;
    }

    public List<User> getUsersList() {
        return usersList;
    }

    /**
     * function that returns a list of videos with movies inside only
     * @return the list of movies stored under a video reference
     */
    public List<Video> getMoviesListCasted() {
        List<Video> out = new ArrayList<>();
        for (Movie movie : moviesList) {
            out.add(movie);
        }
        return out;
    }

    /**
     * function that returns a list of videos with serials inside only
     * @return the list of serials stored under a video reference
     */
    public List<Video> getSerialsListCasted() {
        List<Video> out = new ArrayList<>();
        for (Serial serial : serialsList) {
            out.add(serial);
        }
        return out;
    }

    /**
     * function that returns a certain video from the database
     * @return the reference to the video or null if it is
     * not present in the database
     */
    public Video getVideo(final String title) {
        // if it's a movie, return it
        Movie tryMovie = getMovie(title);
        if (tryMovie != null) {
            return tryMovie;
        }

        // otherwise, it may either be a serial or not present at all in the database
        return getSerial(title);
    }

    /**
     * function that returns a certain movie from the database
     * @return the reference to the movie or null if it is
     * not present in the database
     */
    public Movie getMovie(final String title) {
        for (Movie currMovie : this.moviesList) {
            if (Objects.equals(currMovie.getTitle(), title)) {
                return currMovie;
            }
        }
        return null;
    }

    /**
     * function that returns a certain serial from the database
     * @return the reference to the serial or null if it is
     * not present in the database
     */
    public Serial getSerial(final String title) {
        for (Serial currSerial : this.serialsList) {
            if (Objects.equals(currSerial.getTitle(), title)) {
                return currSerial;
            }
        }
        return null;
    }

    /**
     * function that returns all the videos in the database
     * and assigns an order number to them
     * @return the list with videos in order of appearance
     */
    public List<Video> getVideosList() {
        List<Video> allVideos = new ArrayList<>();
        int count = 1;
        for (Movie movie : this.moviesList) {
            movie.computeRating();
            movie.setPosition(count++);
            allVideos.add(movie);
        }
        for (Serial serial : this.serialsList) {
            serial.computeRating();
            serial.setPosition(count++);
            allVideos.add(serial);
        }
        return allVideos;
    }

    /**
     * function that hashmap with all the genres as key
     * and total number of views for each genre as value
     * @return the hashmap
     */
    public Map<String, Integer> popularGenres() {
        Map<String, Integer> viewsByGenres = new HashMap<>();
        viewsByGenres.put(Constants.ACTION, 0);
        viewsByGenres.put(Constants.ADVENTURE, 0);
        viewsByGenres.put(Constants.DRAMA, 0);
        viewsByGenres.put(Constants.COMEDY, 0);
        viewsByGenres.put(Constants.CRIME, 0);
        viewsByGenres.put(Constants.ROMANCE, 0);
        viewsByGenres.put(Constants.HISTORY_G, 0);
        viewsByGenres.put(Constants.WAR, 0);
        viewsByGenres.put(Constants.THRILLER, 0);
        viewsByGenres.put(Constants.MYSTERY, 0);
        viewsByGenres.put(Constants.FAMILY, 0);
        viewsByGenres.put(Constants.HORROR, 0);
        viewsByGenres.put(Constants.FANTASY, 0);
        viewsByGenres.put(Constants.SCIENCE_FICTION, 0);
        viewsByGenres.put(Constants.SCI_FI_FANTASY, 0);
        viewsByGenres.put(Constants.ACTION_ADVENTURE, 0);
        viewsByGenres.put(Constants.ANIMATION, 0);
        viewsByGenres.put(Constants.KIDS, 0);
        viewsByGenres.put(Constants.WESTERN, 0);
        viewsByGenres.put(Constants.TV_MOVIE, 0);

        List<Video> allVideos = Database.getInstance().getVideosList();
        for (Video video : allVideos) {
            List<String> genres = video.getGenres();
            for (String genre : genres) {
                viewsByGenres.replace(genre, viewsByGenres.get(genre) + video.getNrViews());
            }
        }
        return viewsByGenres;
    }

    /**
     * function that clears the instances in the database
     */
    public void clearDatabase() {
        this.serialsList.clear();
        this.moviesList.clear();
        this.usersList.clear();
        this.actorList.clear();
        instance = null;
    }
}
