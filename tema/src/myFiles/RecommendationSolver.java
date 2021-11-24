package myFiles;

import common.Constants;
import fileio.ActionInputData;

import java.util.*;

public class RecommendationSolver {
    public static String solve(final ActionInputData recommendation) {
        String result = "";
        // daca rezultatul intoarce mesaj de eroare, nu putem recomanda nimic
        if (Objects.equals(recommendation.getType(), Constants.STANDARD)) {
            result = solveStandard(recommendation);
            if (result.equals(Constants.ERROR))
                return "StandardRecommendation cannot be applied!";
            return "StandardRecommendation result: " + result;
        }
        if (Objects.equals(recommendation.getType(), Constants.BEST_UNSEEN)) {
            result = solveBestUnseen(recommendation);
            if (result.equals(Constants.ERROR))
                return "BestRatedUnseenRecommendation cannot be applied!";
            return "BestRatedUnseenRecommendation result: " + result;
        }
        if (Objects.equals(recommendation.getType(), Constants.POPULAR)) {
            // verificam daca userul este premium
            if (!Database.getInstance().getUser(recommendation.getUsername()).isPremium())
                return "PopularRecommendation cannot be applied!";
            result = solvePopular(recommendation);
            if (result.equals(Constants.ERROR))
                return "PopularRecommendation cannot be applied!";
            return "PopularRecommendation result: " + result;
        }
        if (Objects.equals(recommendation.getType(), Constants.FAVORITE)) {
            // verificam daca userul este premium
            if (!Database.getInstance().getUser(recommendation.getUsername()).isPremium())
                return "FavoriteRecommendation cannot be applied!";
            result = solveFavorite(recommendation);
            if (result.equals(Constants.ERROR))
                return "FavoriteRecommendation cannot be applied!";
            return "FavoriteRecommendation result: " + result;
        }
        if (Objects.equals(recommendation.getType(), Constants.SEARCH)) {
            // verificam daca userul este premium
            if (!Database.getInstance().getUser(recommendation.getUsername()).isPremium())
                return "SearchRecommendation cannot be applied!";
            result = solveSearch(recommendation);
            if (result.equals(Constants.ERROR))
                return "SearchRecommendation cannot be applied!";
            return "SearchRecommendation result: " + result;
        }
        if (result.equals(Constants.ERROR))
            return recommendation.getType().substring(0, 1).toUpperCase(Locale.ROOT) + recommendation.getType().substring(1) + "Recommenation cannot be applied!";
        return recommendation.getType().substring(0, 1).toUpperCase(Locale.ROOT) + recommendation.getType().substring(1) + "Recommendation result: " + result;
    }

    public static String solveSearch(final ActionInputData recommendation) {
        // get the filtered videos
        List<Video> allVideos = Database.getInstance().getVideosList();
        allVideos.removeIf(video -> !video.getGenres().contains(recommendation.getGenre()));
        allVideos = sortVideos(allVideos, 'r', 'a', 'n');
        // creez o lista de stringuri ca output
        StringBuilder result = new StringBuilder("[");
        for (Video video : allVideos) {
            if (!Database.getInstance().getUser(recommendation.getUsername()).hasViewed(video.getTitle()))
                result.append(video.getTitle()).append(", ");
        }
        if (result.length() > 3) {
            result.delete(result.length() - 2, result.length());
            result.append(']');
            return result.toString();
        }
        return Constants.ERROR;

    }

    public static String solveFavorite(final ActionInputData recommendation) {
        List<Video> allVideos = Database.getInstance().getVideosList();
        allVideos = sortVideos(allVideos, 'f', 'd', 'p');
        for (Video video : allVideos) {
            if (!Database.getInstance().getUser(recommendation.getUsername()).hasViewed(video.getTitle()) && video.getNrFavorites() != 0)
                return video.getTitle();
        }
        return Constants.ERROR;
    }

    public static String solvePopular(final ActionInputData recommendation) {
        List<Video> allVideos = Database.getInstance().getVideosList();
        // initialize a hasmap of genres with the number of views
        Map<String, Integer> viewsByGenres = Database.getInstance().popularGenres();
        int userViews = Database.getInstance().getUser(recommendation.getUsername()).getHistory().size();
        while (!viewsByGenres.isEmpty() && userViews != 0) {
            String mostPopular = mostPopularGenre(viewsByGenres);
            viewsByGenres.remove(mostPopular);
            for (Video video : allVideos) {
                if (video.checkGenre(mostPopular) && !Database.getInstance().getUser(recommendation.getUsername()).hasViewed(video.getTitle()))
                    return video.getTitle();
                userViews--;
            }
        }
        return Constants.ERROR;
    }

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

    public static String solveStandard(final ActionInputData recommendation) {
        // iau o lista cu toate videourile si il returnez pe primul nevazut
        List<Video> allVideos = Database.getInstance().getVideosList();
        for (Video video : allVideos) {
            if (!Database.getInstance().getUser(recommendation.getUsername()).hasViewed(video.getTitle()))
                return video.getTitle();
        }
        return Constants.ERROR;
    }

    public static String solveBestUnseen(final ActionInputData recommendation) {
        // iau lista cu toate videouri si o ordonez dupa rating + aparite -> dupa intorc primul nevazut
        List<Video> allVideos = Database.getInstance().getVideosList();
        allVideos = sortVideos(allVideos, 'r', 'd', 'p');
        for (Video video : allVideos) {
            if (!Database.getInstance().getUser(recommendation.getUsername()).hasViewed(video.getTitle()))
                return video.getTitle();
        }
        return Constants.ERROR;
    }

    // second p->position n->name
    // sortType a->ascending d->descending
    // type sortare dupa rating, nr fav
    public static List<Video> sortVideos(final List<Video> allVideos, char type, char sortType, char second) {
        // ordonez dupa un anumit criteriu si ordinea
        allVideos.sort(new Comparator<Video>() {
            @Override
            public int compare(Video o1, Video o2) {
                double cmp = 0;
                if (type == 'r')
                    cmp = o1.getOverallRating() - o2.getOverallRating();
                if (type == 'f')
                    cmp = o1.getNrFavorites() - o2.getNrFavorites();
                if (cmp == 0) {
                    if (second == 'p')
                        return o1.getPosition() - o2.getPosition();
                    return o1.getTitle().compareTo(o2.getTitle());
                }
                if (cmp < 0)
                    if (sortType == 'd')
                        return 1;
                    else
                        return -1;
                if (sortType == 'd')
                    return -1;
                return 1;
            }
        });
        return allVideos;
    }

}
