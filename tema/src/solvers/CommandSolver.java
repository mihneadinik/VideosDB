package solvers;

import common.Constants;
import fileio.ActionInputData;
import databases.Database;
import watchables.Movie;
import watchables.Serial;
import entities.User;

import java.util.Objects;

public final class CommandSolver {
    private CommandSolver() { }

    /**
     * function that responds to each command depending
     * on what the input requires
     * @param command the action a user has to execute
     * @return string output message to be parsed as JSON
     */
    public static String solve(final ActionInputData command) {
        String result = "";
        if (Objects.equals(command.getType(), Constants.FAVORITE)) {
            result = solveFavorite(command);
        }
        if (Objects.equals(command.getType(), Constants.VIEW)) {
            result = solveView(command);
        }
        if (Objects.equals(command.getType(), Constants.RATING)) {
            result = solveRating(command);
        }
        return result;
    }

    /**
     * function that responds to a certain Favorite command
     * @param command the action a user has to execute
     * @return string that represents the outcome of the command
     */
    public static String solveFavorite(final ActionInputData command) {
        User user = Database.getInstance().getUser(command.getUsername());
        // user doesn't exist
        if (user == null) {
            return "error -> user not present";
        }
        // video name not present
        if (Database.getInstance().getVideo(command.getTitle()) == null) {
            return "error -> video not present";
        }
        return user.addToFavorites(command.getTitle());
    }

    /**
     * function that responds to a certain View command
     * @param command the action a user has to execute
     * @return string that represents the outcome of the command
     */
    public static String solveView(final ActionInputData command) {
        User user = Database.getInstance().getUser(command.getUsername());
        // user doesn't exist
        if (user == null) {
            return "error -> user not present";
        }
        // video name not present
        if (Database.getInstance().getVideo(command.getTitle()) == null) {
            return "error -> video not present";
        }
        return user.addToHistory(command.getTitle());
    }

    /**
     * function that responds to a certain Rating command
     * @param command the action a user has to execute
     * @return string that represents the outcome of the command
     */
    public static String solveRating(final ActionInputData command) {
        User user = Database.getInstance().getUser(command.getUsername());
        // user doesn't exist
        if (user == null) {
            return "error -> user not present";
        }

        Movie movie = Database.getInstance().getMovie(command.getTitle());
        if (movie != null) {
            return user.rateMovie(movie, command.getGrade());
        }

        Serial serial = Database.getInstance().getSerial(command.getTitle());
        if (serial != null) {
            return user.rateSerial(serial, command.getSeasonNumber(), command.getGrade());
        }
        // video name not present
        return "error -> video not present";
    }
}
