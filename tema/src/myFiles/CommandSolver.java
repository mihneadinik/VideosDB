package myFiles;

import common.Constants;
import fileio.ActionInputData;

import java.util.Objects;

// primeste o comanda si este apelata cu tipul comenzii ca sa rezolve cerinta
public class CommandSolver {
//    private final ActionInputData command;
//
//    public CommandSolver(ActionInputData command) {
//        this.command = command;
//    }

    public static String solve(ActionInputData command) {
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

    public static String solveFavorite(ActionInputData command) {
        User user = Database.getInstance().getUser(command.getUsername());
        // user doesn't exist
        if (user == null)
            return "error -> user not present";
        // Video name not present
        if (Database.getInstance().getVideo(command.getTitle()) == null)
            return "error -> video not present";
        return user.addToFavorites(command.getTitle());
    }

    public static String solveView(ActionInputData command) {
        User user = Database.getInstance().getUser(command.getUsername());
        // user doesn't exist
        if (user == null)
            return "error -> user not present";
        // Video name not present
        if (Database.getInstance().getVideo(command.getTitle()) == null)
            return "error -> video not present";
        return user.addToHistory(command.getTitle());
    }

    public static String solveRating(ActionInputData command) {
        User user = Database.getInstance().getUser(command.getUsername());
        // user doesn't exist
        if (user == null)
            return "error -> user not present";

        Movie movie = Database.getInstance().getMovie(command.getTitle());
        if (movie != null) {
            return user.rateMovie(movie, command.getGrade());
        }

        Serial serial = Database.getInstance().getSerial(command.getTitle());
        if (serial != null) {
            return user.rateSerial(serial, command.getSeasonNumber(), command.getGrade());
        }
        // Video name not present
        return "error -> video not present";
    }
}
