package entities;

import actor.ActorsAwards;
import common.Constants;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import databases.Database;
import watchables.Movie;
import watchables.Serial;

public final class Actor {
    private final String name;
    private final String description;
    private final List<String> filmography;
    private final Map<ActorsAwards, Integer> awards;
    private double average;
    private int totalAwards;

    public Actor(final String name, final String description,
                 final ArrayList<String> filmography, final Map<ActorsAwards,
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

    public Actor(final Actor otherActor) {
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

    public int getTotalAwards() {
        return totalAwards;
    }

    /**
     * getter for the average videos rating
     * @return double value -> the average rating of all
     * the videos the actor has played in
     */
    public double getAverage() {
        computeAverage();
        return average;
    }

    /**
     * function that computes the number of total
     * awards for an actor based on the values
     * of each award contained in the "awards" map;
     * because an actor cannot receive a new award
     * after instantiation, if the number is already
     * computed the function will not do it again
     */
    public void computeTotalAwards() {
        if (this.totalAwards != 0) {
            return;
        }
        List<Integer> values = new ArrayList<>(this.awards.values());
        for (Integer number : values) {
            this.totalAwards += number;
        }
    }

    /**
     * function that checks if an actor has received
     * all the awards given as parameters
     * @param givenAwards the list of awards to be checked
     * @return true/false
     */
    public boolean checkAwards(final List<ActorsAwards> givenAwards) {
        for (ActorsAwards award : givenAwards) {
            if (!this.awards.containsKey(award)) {
                return false;
            }
        }
        return true;
    }

    /**
     * function that computes the average rating
     * of all the movies and serials an actor
     * has played in
     */
    public void computeAverage() {
        int numberOfRatings = 0;
        double ratingSum = 0;
        if (this.filmography == null) {
            return;
        }
        for (String video : this.filmography) {
            Movie tryMovie = Database.getInstance().getMovie(video);
            if (tryMovie != null) {
                double rating = tryMovie.getOverallRating();
                if (rating != -1) {
                    numberOfRatings++;
                    ratingSum += rating;
                }
            } else {
                Serial trySerial = Database.getInstance().getSerial(video);
                if (trySerial != null) {
                    double rating = trySerial.getOverallRating();
                    if (rating != -1) {
                        numberOfRatings++;
                        ratingSum += rating;
                    }
                }
            }
        }
        if (numberOfRatings != 0) {
            this.average = ratingSum / numberOfRatings;
        }
    }

    /**
     * function that checks if an actor's description contains
     * all the keywords received as parameters (case insensitive)
     * @param keywords the list of words to be checked
     * @return true/false
     */
    public boolean containsKeywords(final List<String> keywords) {
        for (String word : keywords) {
            if (!containsWord(word)) {
                return false;
            }
        }
        return true;
    }

    private boolean containsWord(final String word) {
        String insensitiveDescription = new String(this.description.toLowerCase(Locale.ROOT));
        while (insensitiveDescription.contains(word)) {
            int left = insensitiveDescription.indexOf(word);
            int right = left + word.length();

            if ((left == 0 || Constants.ACCEPTED_CHARACTERS.contains(String.valueOf(
                    insensitiveDescription.charAt(left - 1))))
                    && (right == insensitiveDescription.length()
                    || Constants.ACCEPTED_CHARACTERS.contains(
                            String.valueOf(insensitiveDescription.charAt(right))))) {
                return true;
            }
            insensitiveDescription = insensitiveDescription.substring(0, left - 1)
                    + insensitiveDescription.substring(right);
        }
        return false;
    }
}
