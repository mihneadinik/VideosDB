package myFiles;

import actor.ActorsAwards;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class Actor {
    private final String name;
    private final String description;
    private final List<String> filmography;
    private Map<ActorsAwards, Integer> awards;
    private double average;
    private int totalAwards;

    public Actor(final String name, final String description,
                 final ArrayList<String> filmography, Map<ActorsAwards,
                    Integer> awards) {
        this.name = name;
        this.description = description;
        this.filmography = filmography;
        this.awards = awards;
        this.average = 0;
        this.totalAwards = 0;
    }

    public Actor() {
        this.name = "";
        this.description = "";
        this.filmography = null;
        this.awards = null;
        this.average = 0;
        this.totalAwards = 0;
    }

    public Actor(Actor otherActor) {
        this.name = otherActor.getName();
        this.description = otherActor.getDescription();
        this.filmography = otherActor.getFilmography();
        this.awards = otherActor.getAwards();
        this.average = otherActor.getAverage();
        this.totalAwards = otherActor.totalAwards;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public List<String> getFilmography() {
        return filmography;
    }

    public Map<ActorsAwards, Integer> getAwards() {
        return awards;
    }

    public void computeTotalAwards() {
        // compute total awards if I haven't already
        if (this.totalAwards != 0)
            return;
        List<Integer> values = new ArrayList<>(this.awards.values());
        for (Integer number : values)
            this.totalAwards += number;
    }

    // check if an actor contains the awards for a querry
    public boolean checkAwards(List<ActorsAwards> givenAwards) {
        for (ActorsAwards award : givenAwards)
            if (!this.awards.containsKey(award))
                return false;
        return true;
    }

    public void computeAverage() {
        int numberOfRatings = 0;
        double RatingSum = 0;
        if (this.filmography == null)
            return;
        for (String video : this.filmography) {
            Movie tryMovie = Database.getInstance().getMovie(video);
            if (tryMovie != null) {
                // get rating
                double rating = tryMovie.getOverallRating();
                if (rating != -1) {
                    numberOfRatings++;
                    RatingSum += rating;
                }
            } else {
                Serial trySerial = Database.getInstance().getSerial(video);
                if (trySerial != null) {
                    // get rating
                    double rating = trySerial.getOverallRating();
                    if (rating != -1) {
                        numberOfRatings++;
                        RatingSum += rating;
                    }
                }
            }
        }
        if (numberOfRatings != 0) {
            this.average = RatingSum / numberOfRatings;
        }
    }

    // verific daca descrierea contine toate cuvintele (case insensitive)
    public boolean containsKeywords(final List<String> keywords) {
        String insensitiveDescription = this.description.toLowerCase(Locale.ROOT);
        for (String word : keywords) {
            if (!insensitiveDescription.contains(word.toLowerCase(Locale.ROOT)))
                return false;
        }
        return true;
    }

    public double getAverage() {
        computeAverage();
        return average;
    }

    public int getTotalAwards() {
        return totalAwards;
    }

    @Override
    public String toString() {
        return "Actor{" +
                "name='" + name + '\'' +
                ", awards=" + awards +
                ", totalAwards=" + totalAwards +
                '}';
    }
}
