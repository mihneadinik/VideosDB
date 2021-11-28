package solvers;

import common.Constants;
import fileio.ActionInputData;
import databases.Database;
import watchables.Video;

import java.util.List;
import java.util.Objects;
import java.util.Map;
import java.util.Comparator;

public final class RecommendationSolver {
    private RecommendationSolver() { }

    /**
     * function that responds to each recommendation depending
     * on what the input requires
     * @param recommendation the recommendation to be given for a user
     * @return string output message to be parsed as JSON
     */
    public static String solve(final ActionInputData recommendation) {
        String result = "";
        // if the result is an error, nothing to be recommended
        if (Objects.equals(recommendation.getType(), Constants.STANDARD)) {
            result = solveStandard(recommendation);
            if (result.equals(Constants.ERROR)) {
                return "StandardRecommendation cannot be applied!";
            }
            return "StandardRecommendation result: " + result;
        }
        if (Objects.equals(recommendation.getType(), Constants.BEST_UNSEEN)) {
            result = solveBestUnseen(recommendation);
            if (result.equals(Constants.ERROR)) {
                return "BestRatedUnseenRecommendation cannot be applied!";
            }
            return "BestRatedUnseenRecommendation result: " + result;
        }
        if (Objects.equals(recommendation.getType(), Constants.POPULAR)) {
            // check if user is premium
            if (!Database.getInstance().getUser(recommendation.getUsername()).isPremium()) {
                return "PopularRecommendation cannot be applied!";
            }
            result = solvePopular(recommendation);
            if (result.equals(Constants.ERROR)) {
                return "PopularRecommendation cannot be applied!";
            }
            return "PopularRecommendation result: " + result;
        }
        if (Objects.equals(recommendation.getType(), Constants.FAVORITE)) {
            // check if user is premium
            if (!Database.getInstance().getUser(recommendation.getUsername()).isPremium()) {
                return "FavoriteRecommendation cannot be applied!";
            }
            result = solveFavorite(recommendation);
            if (result.equals(Constants.ERROR)) {
                return "FavoriteRecommendation cannot be applied!";
            }
            return "FavoriteRecommendation result: " + result;
        }
        if (Objects.equals(recommendation.getType(), Constants.SEARCH)) {
            // check if user is premium
            if (!Database.getInstance().getUser(recommendation.getUsername()).isPremium()) {
                return "SearchRecommendation cannot be applied!";
            }
            result = solveSearch(recommendation);
            if (result.equals(Constants.ERROR)) {
                return "SearchRecommendation cannot be applied!";
            }
            return "SearchRecommendation result: " + result;
        }
        return "error -> unknown recommendation";
    }

    /**
     * function that responds to a certain Search recommendation
     * @param recommendation the recommendation to be given for a user
     * @return string that corresponds to the list of videos that
     * match the searched criteria
     */
    public static String solveSearch(final ActionInputData recommendation) {
        // get the filtered videos
        List<Video> allVideos = Database.getInstance().getVideosList();
        allVideos.removeIf(video -> !video.getGenres().contains(recommendation.getGenre()));
        allVideos = sortVideos(allVideos, 'r', 'a', 'n');
        StringBuilder result = new StringBuilder("[");
        for (Video video : allVideos) {
            if (!Database.getInstance().getUser(recommendation.getUsername()).
                    hasViewed(video.getTitle())) {
                result.append(video.getTitle()).append(", ");
            }
        }
        if (!result.toString().equals("[")) {
            result.delete(result.length() - 2, result.length());
            result.append(']');
            return result.toString();
        }
        return Constants.ERROR;
    }

    /**
     * function that responds to a certain Favorite recommendation
     * @param recommendation the recommendation to be given for a user
     * @return string that corresponds to the first video in the database
     * that might be recommended to the user
     */
    public static String solveFavorite(final ActionInputData recommendation) {
        List<Video> allVideos = Database.getInstance().getVideosList();
        allVideos = sortVideos(allVideos, 'f', 'd', 'p');
        for (Video video : allVideos) {
            if (!Database.getInstance().getUser(recommendation.getUsername()).
                    hasViewed(video.getTitle()) && video.getNrFavorites() != 0) {
                return video.getTitle();
            }
        }
        return Constants.ERROR;
    }

    /**
     * function that responds to a certain Popular recommendation
     * @param recommendation the recommendation to be given for a user
     * @return string that corresponds to the first video in the database
     * that might be recommended to the user
     */
    public static String solvePopular(final ActionInputData recommendation) {
        List<Video> allVideos = Database.getInstance().getVideosList();
        // initialise a hashmap of genres with the number of views
        Map<String, Integer> viewsByGenres = Database.getInstance().popularGenres();
        int userViews = Database.getInstance().getUser(recommendation.
                getUsername()).getHistory().size();
        while (!viewsByGenres.isEmpty() && userViews != 0) {
            String mostPopular = mostPopularGenre(viewsByGenres);
            viewsByGenres.remove(mostPopular);
            for (Video video : allVideos) {
                if (video.checkGenre(mostPopular) && !Database.getInstance().getUser(
                        recommendation.getUsername()).hasViewed(video.getTitle())) {
                    return video.getTitle();
                }
                userViews--;
            }
        }
        return Constants.ERROR;
    }

    /**
     * function finds the most popular genre so far
     * @param viewsByGenres the HashMap that stores all genres according
     * to their popularity
     * @return string that matches the most popular genre
     */
    public static String mostPopularGenre(final Map<String, Integer> viewsByGenres) {
        String out = Constants.ERROR;
        int max = 0;
        for (Map.Entry<String, Integer> entry : viewsByGenres.entrySet()) {
            if (entry.getValue() > max) {
                max = entry.getValue();
                out = entry.getKey();
            }
        }
        return out;
    }

    /**
     * function that responds to a certain Standard recommendation
     * @param recommendation the recommendation to be given for a user
     * @return string that corresponds to the first video in the database
     * that might be recommended to the user
     */
    public static String solveStandard(final ActionInputData recommendation) {
        // get a list with all the videos and return the first unseen
        List<Video> allVideos = Database.getInstance().getVideosList();
        for (Video video : allVideos) {
            if (!Database.getInstance().getUser(recommendation.getUsername()).
                    hasViewed(video.getTitle())) {
                return video.getTitle();
            }
        }
        return Constants.ERROR;
    }

    /**
     * function that responds to a certain Best_Unseen recommendation
     * @param recommendation the recommendation to be given for a user
     * @return string that corresponds to the first video in the database
     * that might be recommended to the user
     */
    public static String solveBestUnseen(final ActionInputData recommendation) {
        // get a list with all the videos and sort them ratings and order of appearance
        // -> return the first unseen
        List<Video> allVideos = Database.getInstance().getVideosList();
        allVideos = sortVideos(allVideos, 'r', 'd', 'p');
        for (Video video : allVideos) {
            if (!Database.getInstance().getUser(recommendation.getUsername()).
                    hasViewed(video.getTitle())) {
                return video.getTitle();
            }
        }
        return Constants.ERROR;
    }

    /**
     * function that sorts a list of videos according to certain criteria
     * @param allVideos the list to be sorted
     * @param type tells whether to sort by rating (r) or number of
     * favorites (f)
     * @param sortType tells whether the order is ascending or descending
     * @param second tells the second criteria for the search (position -> p
     * or name -> n)
     * @return the sorted list
     */
    public static List<Video> sortVideos(final List<Video> allVideos, final char type,
                                         final char sortType, final char second) {
        allVideos.sort(new Comparator<Video>() {
            @Override
            public int compare(final Video o1, final Video o2) {
                double cmp = 0;
                if (type == 'r') {
                    cmp = o1.getOverallRating() - o2.getOverallRating();
                }
                if (type == 'f') {
                    cmp = o1.getNrFavorites() - o2.getNrFavorites();
                }
                if (cmp == 0) {
                    if (second == 'p') {
                        return o1.getPosition() - o2.getPosition();
                    }
                    return o1.getTitle().compareTo(o2.getTitle());
                }
                if (cmp < 0) {
                    if (sortType == 'd') {
                        return 1;
                    } else {
                        return -1;
                    }
                }
                if (sortType == 'd') {
                    return -1;
                }
                return 1;
            }
        });
        return allVideos;
    }
}
