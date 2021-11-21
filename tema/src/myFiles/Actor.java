package myFiles;

import actor.ActorsAwards;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class Actor {
    private final String name;
    private final String description;
    private final List<String> filmography;
    private Map<ActorsAwards, Integer> awards;

    public Actor(final String name, final String description,
                 final ArrayList<String> filmography, Map<ActorsAwards,
                    Integer> awards) {
        this.name = name;
        this.description = description;
        this.filmography = filmography;
        this.awards = awards;
    }

    public Actor() {
        this.name = "";
        this.description = "";
        this.filmography = null;
        this.awards = null;
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

    @Override
    public String toString() {
        return "Actor{" +
                "name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", filmography=" + filmography +
                ", awards=" + awards +
                '}';
    }
}
