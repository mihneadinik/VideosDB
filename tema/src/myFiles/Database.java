package myFiles;

import common.Constants;
import fileio.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

// A database that stores all my information
public class Database {
    private static Database instance = null;
    private List<Actor> actorList;
    private List<User> usersList;
    private List<Movie> moviesList;
    private List<Serial> serialsList;

    private Database() {
        this.actorList = new ArrayList<Actor>();
        this.usersList = new ArrayList<User>();
        this.moviesList = new ArrayList<Movie>();
        this.serialsList = new ArrayList<Serial>();
    }

    public static Database getInstance() {
        if (instance == null)
            instance = new Database();

        return instance;
    }

    // takes each input from the list parsed in main
    public void setActors (final List<ActorInputData> data) {
        for (ActorInputData currActor : data) {
            this.actorList.add(new Actor(currActor.getName(), currActor.getCareerDescription(), currActor.getFilmography(), currActor.getAwards()));
        }
    }

    public void setUsers (final List<UserInputData> data) {
        for (UserInputData currUser : data) {
            this.usersList.add(new User(currUser.getUsername(), currUser.getSubscriptionType(), currUser.getHistory(), currUser.getFavoriteMovies()));
        }
    }

    public void setMoviesList(final List<MovieInputData> data) {
        for (MovieInputData currMovie : data) {
            this.moviesList.add(new Movie(currMovie.getTitle(), currMovie.getYear(), currMovie.getGenres(), currMovie.getCast(), currMovie.getDuration()));
        }
    }

    public void setSerialsList(final List<SerialInputData> data) {
        for (SerialInputData currSerial : data) {
            this.serialsList.add(new Serial(currSerial.getTitle(), currSerial.getYear(), currSerial.getGenres(), currSerial.getCast(), currSerial.getNumberSeason(), currSerial.getSeasons()));
        }
    }

    public User getUser(String username) {
        for (User currUser : this.usersList)
            if (Objects.equals(currUser.getUsername(), username))
                return currUser;

        return null;
    }

    public Video getVideo(String title) {
        // if it's a movie, return it
        Movie tryMovie = getMovie(title);
        if (tryMovie != null)
            return tryMovie;

        // otherwise it may either be a serial or not present at all in the database
        Serial trySerial = getSerial(title);
        return trySerial;
    }

    // look for specific movie
    public Movie getMovie(String title) {
        for (Movie currMovie : this.moviesList)
            if (Objects.equals(currMovie.getTitle(), title))
                return currMovie;
        return null;
    }

    // look for specific serial
    public Serial getSerial(String title) {
        for (Serial currSerial : this.serialsList)
            if (Objects.equals(currSerial.getTitle(), title))
                return currSerial;
        return null;
    }
}
