package solvers;

import common.Constants;
import fileio.ActionInputData;

import java.util.Objects;

import solvers.queries.VideosQuery;
import solvers.queries.ActorsQuery;
import solvers.queries.UsersQuery;

public final class QuerySolver {
    private QuerySolver() { }

    /**
     * function that calls the appropriate function
     * to solve a certain query based on its type
     * @param query the query input that has to be
     * answered
     * @return the result of the query under a String
     * type that will be parsed as JSON
     */
    public static String solve(final ActionInputData query) {
        String result = "";
        if (Objects.equals(query.getObjectType(), Constants.ACTORS)) {
            result = ActorsQuery.solveActors(query);
        }
        if (Objects.equals(query.getObjectType(), Constants.MOVIES)) {
            result = VideosQuery.solveVideos(query, 'm');
        }
        if (Objects.equals(query.getObjectType(), Constants.SHOWS)) {
            result = VideosQuery.solveVideos(query, 's');
        }
        if (Objects.equals(query.getObjectType(), Constants.USERS)) {
            result = UsersQuery.solveUsers(query);
        }
        return "Query result: [" + result + "]";
    }

    /**
     * function that for different objects returns the
     * order in which they will be added in the sorted list
     * @param cmp the result of the fields' comparison
     * to be interpreted
     * @param order tells whether the sorting is in
     * ascending or descending order
     * @return the order in which the elements of a list
     * will be sorted
     */
    public static int checkCmpNotZero(final double cmp, final String order) {
        if (cmp < 0) {
            if (Objects.equals(order, Constants.ASCENDING)) {
                return -1;
            } else {
                return 1;
            }
        }
        if (Objects.equals(order, Constants.ASCENDING)) {
            return 1;
        }
        return -1;
    }
}
