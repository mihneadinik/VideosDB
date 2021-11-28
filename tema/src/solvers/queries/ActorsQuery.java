package solvers.queries;

import actor.ActorsAwards;
import common.Constants;
import entities.Actor;
import fileio.ActionInputData;
import databases.Database;
import solvers.QuerySolver;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;

import static utils.Utils.stringToAwards;

public final class ActorsQuery {
    private ActorsQuery() { }

    /**
     * function that calls the appropriate function
     * to solve an Actors Query
     * @param query the specific query for Actors
     * that has to be answered
     * @return the result of the query under a String
     * type that will be passed to QuerySolver
     */
    public static String solveActors(final ActionInputData query) {
        if (Objects.equals(query.getCriteria(), Constants.AVERAGE)) {
            return solveAverage(query, Database.getInstance().getActorList());
        }
        if (Objects.equals(query.getCriteria(), Constants.AWARDS)) {
            return solveAwards(query, Database.getInstance().getActorList());
        }
        if (Objects.equals(query.getCriteria(), Constants.FILTER_DESCRIPTIONS)) {
            return solveDescription(query, Database.getInstance().getActorList());
        }
        return "error -> criteria unknown";
    }

    /**
     * function that solves the Average query
     * @param query the query to be answered
     * @param actors a list with all actors in the database
     * @return the list of sorted actors under a String form
     */
    public static String solveAverage(final ActionInputData query, final List<Actor> actors) {
        List<Actor> sortedActors = new ArrayList<>();
        for (Actor actor : actors) {
            sortedActors.add(new Actor(actor));
        }

        return sortActors(query.getSortType(), query.getNumber(), sortedActors, 'r', true);
    }

    /**
     * function that solves the Awards query
     * @param query the query to be answered
     * @param actors a list with all actors in the database
     * @return the list of actors that have certain awards
     * under a String form
     */
    public static String solveAwards(final ActionInputData query, final List<Actor> actors) {
        // extract the list of awards from query
        List<String> awardsString = query.getFilters().get(Constants.FILTER_NUMBER_AWARDS);
        // convert the strings to enum items
        List<ActorsAwards> awardsEnum = new ArrayList<>();
        for (String award : awardsString) {
            awardsEnum.add(stringToAwards(award));
        }

        List<Actor> sortedActors = new ArrayList<>();
        for (Actor actor : actors) {
            if (actor.checkAwards(awardsEnum)) {
                actor.computeTotalAwards();
                sortedActors.add(new Actor(actor));
            }
        }
        // sort the actors
        return sortActors(query.getSortType(), sortedActors.size(), sortedActors, 'a', false);
    }

    /**
     * function that solves the Filter description query
     * @param query the query to be answered
     * @param actors a list with all actors in the database
     * @return the list of actors that have certain words
     * in their description under a String form
     */
    public static String solveDescription(final ActionInputData query, final List<Actor> actors) {
        // extract the list of keywords from query
        List<String> keywords = query.getFilters().get(Constants.FILTER_NUMBER_WORDS);

        List<Actor> sortedActors = new ArrayList<>();
        for (Actor actor : actors) {
            if (actor.containsKeywords(keywords)) {
                sortedActors.add(new Actor(actor));
            }
        }
        // sort the actors
        return sortActors(query.getSortType(), sortedActors.size(), sortedActors, 'n', false);
    }

    /**
     * function that sorts a list of actors according to certain criteria
     * @param order tells whether the order is ascending or descending
     * @param number will be passed to the function that extracts a certain
     * amount of actors from the list
     * @param sortedActors the list of actors to be sorted
     * @param field tells the criteria of comparison
     * (a -> awards, r -> ranking/average, n -> name)
     * @param checkVideoRating will be passed to the function that
     * extracts certain actors from the sorted list
     * @return a string with number sorted actors
     */
    public static String sortActors(final String order, final int number,
                                    final List<Actor> sortedActors, final char field,
                                    final boolean checkVideoRating) {
        if (order.compareTo(Constants.ASCENDING) != 0
                && order.compareTo(Constants.DESCENDING) != 0) {
            return "error -> order unknown";
        }
        sortedActors.sort(new Comparator<Actor>() {
            @Override
            public int compare(final Actor o1, final Actor o2) {
                double cmp = -1;
                if (field == 'a') {
                    cmp = o1.getTotalAwards() - o2.getTotalAwards();
                }
                if (field == 'r') {
                    cmp = o1.getAverage() - o2.getAverage();
                }
                if (field == 'n') {
                    cmp = 0;
                }
                if (cmp == 0) {
                    // if equal average, sort by name
                    if (Objects.equals(order, Constants.ASCENDING)) {
                        return o1.getName().compareTo(o2.getName());
                    }
                    return o2.getName().compareTo(o1.getName());
                }
                // if objects are not equal, return the sorting flag depending on the order type
                return QuerySolver.checkCmpNotZero(cmp, order);
            }
        });
        return createStringActorsList(sortedActors, number, checkVideoRating);
    }

    /**
     * function that extracts n actors from the sorted
     * list and returns them in a string form
     * @param sortedActors the sorted list of actors
     * @param n the number of actors to be extracted
     * @param checkVideoRating a flag that tells whether
     * is needed to check the rating of the videos the
     * actor has played in or not
     * @return a string that represents the n extracted actors
     */
    public static String createStringActorsList(final List<Actor> sortedActors,
                                                final int n, final boolean checkVideoRating) {
        StringBuilder out = new StringBuilder();
        int cnt = 0, actorsNumber = n;
        // if I need to extract more actors than the size of the list,
        // I will simply extract all of them
        if (actorsNumber > sortedActors.size()) {
            actorsNumber = sortedActors.size();
        }

        // put each actor's name in the output string
        for (Actor actor : sortedActors) {
            // when I have all the actors extracted, stop
            if (cnt == actorsNumber) {
                break;
            }
            // check if the videos he has played in have rating
            if (checkVideoRating) {
                if (actor.getAverage() != 0) {
                    out.append(actor.getName());
                    out.append(", ");
                    cnt++;
                }
            } else {
                out.append(actor.getName());
                out.append(", ");
                cnt++;
            }
        }

        // delete last 2 characters (", ") if they exist
        if (!out.isEmpty()) {
            out.delete(out.length() - 2, out.length());
        }
        return out.toString();
    }
}
