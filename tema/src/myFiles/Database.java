package myFiles;

import common.Constants;
import fileio.*;

import java.util.*;

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

    public Video getVideo(final String title) {
        // if it's a movie, return it
        Movie tryMovie = getMovie(title);
        if (tryMovie != null)
            return tryMovie;

        // otherwise it may either be a serial or not present at all in the database
        Serial trySerial = getSerial(title);
        return trySerial;
    }

    // look for specific movie
    public Movie getMovie(final String title) {
        for (Movie currMovie : this.moviesList)
            if (Objects.equals(currMovie.getTitle(), title))
                return currMovie;
        return null;
    }

    // look for specific serial
    public Serial getSerial(final String title) {
        for (Serial currSerial : this.serialsList)
            if (Objects.equals(currSerial.getTitle(), title))
                return currSerial;
        return null;
    }

    public List<Actor> getActorList() {
        return actorList;
    }

    public List<User> getUsersList() {
        return usersList;
    }

    public List<Movie> getMoviesList() {
        return moviesList;
    }

    public List<Serial> getSerialsList() {
        return serialsList;
    }

    public List<Video> getMoviesListCasted() {
        List<Video> out = new ArrayList<>();
        for (Movie movie : moviesList)
            out.add(movie);
        return out;
    }

    public List<Video> getSerialsListCasted() {
        List<Video> out = new ArrayList<>();
        for (Serial serial : serialsList)
            out.add(serial);
        return out;
    }

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

    public Map<String, Integer> popularGenres() {
        // initialize a hasmap of genres with the number of views
        Map<String, Integer> viewsByGenres = new HashMap<>();
        viewsByGenres.put(Constants.ACTION, 0);
        viewsByGenres.put(Constants.ADVENTURE, 0);
        viewsByGenres.put(Constants.DRAMA, 0);
        viewsByGenres.put(Constants.COMEDY, 0);
        viewsByGenres.put(Constants.CRIME, 0);
        viewsByGenres.put(Constants.ROMANCE, 0);
        viewsByGenres.put(Constants.HiSTORY, 0);
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

    public void clearDatabase() {
        this.serialsList.clear();
        this.moviesList.clear();
        this.usersList.clear();
        this.actorList.clear();
        instance = null;
    }
}
