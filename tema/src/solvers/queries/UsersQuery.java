package solvers.queries;

import common.Constants;
import entities.User;
import fileio.ActionInputData;
import databases.Database;
import solvers.QuerySolver;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;

public final class UsersQuery {
    private UsersQuery() { }

    /**
     * function that solves a query for users
     * @param query the query to be answered
     * @return the result of the query under a String
     * type that will be passed to QuerySolver
     */
    public static String solveUsers(final ActionInputData query) {
        List<User> sortedUsers = new ArrayList<>();
        for (User user : Database.getInstance().getUsersList()) {
            if (user.getNrRatings() != 0) {
                sortedUsers.add(user);
            }
        }
        return sortUsers(query.getSortType(), query.getNumber(), sortedUsers);
    }

    /**
     * function that sorts a list of users according to certain criteria
     * @param order tells whether the order is ascending or descending
     * @param number will be passed to the function that extracts a certain
     * amount of users from the list
     * @param sortedUsers the list of videos to be sorted
     * @return a string with number sorted users
     */
    public static String sortUsers(final String order, final int number,
                                   final List<User> sortedUsers) {
        if (order.compareTo(Constants.ASCENDING) != 0
                && order.compareTo(Constants.DESCENDING) != 0) {
            return "error -> order unknown";
        }

        sortedUsers.sort(new Comparator<User>() {
            @Override
            public int compare(final User o1, final User o2) {
                int cmp = o1.getNrRatings() - o2.getNrRatings();
                if (cmp == 0) {
                    // if equal average, sort by name
                    if (Objects.equals(order, Constants.ASCENDING)) {
                        return o1.getUsername().compareTo(o2.getUsername());
                    }
                    return o2.getUsername().compareTo(o1.getUsername());
                }
                // if objects are not equal, return the sorting flag depending on the order type
                return QuerySolver.checkCmpNotZero(cmp, order);
            }
        });

        return createStringUsersList(sortedUsers, number);
    }

    /**
     * function that extracts n users from the sorted
     * list and returns them in a string form
     * @param sortedUsers the sorted list of users
     * @param n the number of users to be extracted
     * @return a string that represents the n extracted users
     */
    public static String createStringUsersList(final List<User> sortedUsers, final int n) {
        StringBuilder out = new StringBuilder();
        int cnt = 0, usersNumber = n;
        // if I need to extract more users than the size of the list,
        // I will simply extract all of them
        if (usersNumber > sortedUsers.size()) {
            usersNumber = sortedUsers.size();
        }

        // put each user's name in the output string
        for (User user : sortedUsers) {
            // when I have all the users extracted, stop
            if (cnt == usersNumber) {
                break;
            }

            out.append(user.getUsername());
            out.append(", ");
            cnt++;
        }

        // delete last 2 characters (", ") if they exist
        if (!out.isEmpty()) {
            out.delete(out.length() - 2, out.length());
        }
        return out.toString();
    }
}
