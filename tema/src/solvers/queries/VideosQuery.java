package solvers.queries;

import common.Constants;
import fileio.ActionInputData;
import databases.Database;
import solvers.QuerySolver;
import watchables.Video;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;

public final class VideosQuery {
    private VideosQuery() { }

    /**
     * function that calls the appropriate function
     * to solve a Video Query
     * @param query the specific query for Videos
     * that has to be answered
     * @param type the type of Video in the query
     * (m -> movie, s -> show)
     * @return the result of the query under a String
     * type that will be passed to QuerySolver
     */
    public static String solveVideos(final ActionInputData query, final char type) {
        if (Objects.equals(query.getCriteria(), Constants.RATINGS)) {
            if (type == 'm') {
                return solveRating(query, Database.getInstance().getMoviesListCasted());
            } else {
                return solveRating(query, Database.getInstance().getSerialsListCasted());
            }
        }
        if (Objects.equals(query.getCriteria(), Constants.FAVORITE)) {
            if (type == 'm') {
                return solveFavorite(query, Database.getInstance().getMoviesListCasted());
            } else {
                return solveFavorite(query, Database.getInstance().getSerialsListCasted());
            }
        }
        if (Objects.equals(query.getCriteria(), Constants.LONGEST)) {
            if (type == 'm') {
                return solveLongest(query, Database.getInstance().getMoviesListCasted());
            } else {
                return solveLongest(query, Database.getInstance().getSerialsListCasted());
            }
        }
        if (Objects.equals(query.getCriteria(), Constants.MOST_VIEWED)) {
            if (type == 'm') {
                return solveMostViewed(query, Database.getInstance().getMoviesListCasted());
            } else {
                return solveMostViewed(query, Database.getInstance().getSerialsListCasted());
            }
        }
        return "error -> criteria unknown";
    }

    /**
     * function that solves the MostViewed query
     * @param query the query to be answered
     * @param videos a list with all videos in the database
     * @return the list of most viewed videos under a String form
     */
    public static String solveMostViewed(final ActionInputData query, final List<Video> videos) {
        List<Video> ratedVideos = new ArrayList<>();
        for (Video video : videos) {
            if (video.checkFilters(query.getFilters()) && video.getNrViews() > 0) {
                ratedVideos.add(new Video(video));
            }
        }
        return sortVideos(query.getSortType(), query.getNumber(), ratedVideos, 'v');
    }

    /**
     * function that solves the Longest query
     * @param query the query to be answered
     * @param videos a list with all videos in the database
     * @return the list of the longest videos that match a certain
     * criteria under a String form
     */
    public static String solveLongest(final ActionInputData query, final List<Video> videos) {
        List<Video> ratedVideos = new ArrayList<>();
        for (Video video : videos) {
            if (video.checkFilters(query.getFilters())) {
                ratedVideos.add(new Video(video));
            }
        }
        return sortVideos(query.getSortType(), query.getNumber(), ratedVideos, 'd');
    }

    /**
     * function that solves the Favorite query
     * @param query the query to be answered
     * @param videos a list with all videos in the database
     * @return the list of the videos that match a certain criteria
     * and have been added as favorite at least once under a String form
     */
    public static String solveFavorite(final ActionInputData query, final List<Video> videos) {
        List<Video> ratedVideos = new ArrayList<>();
        for (Video video : videos) {
            if (video.checkFilters(query.getFilters()) && video.getNrFavorites() > 0) {
                ratedVideos.add(new Video(video));
            }
        }
        return sortVideos(query.getSortType(), query.getNumber(), ratedVideos, 'f');
    }

    /**
     * function that solves the Rating query
     * @param query the query to be answered
     * @param videos a list with all videos in the database
     * @return the list of the videos that match a certain criteria
     * and have received at least one rating under a String form
     */
    public static String solveRating(final ActionInputData query, final List<Video> videos) {
        List<Video> ratedVideos = new ArrayList<>();
        for (Video video : videos) {
            if (video.checkFilters(query.getFilters()) && video.getOverallRating() > 0) {
                ratedVideos.add(new Video(video));
            }
        }
        return sortVideos(query.getSortType(), query.getNumber(), ratedVideos, 'r');
    }

    /**
     * function that sorts a list of videos according to certain criteria
     * @param order tells whether the order is ascending or descending
     * @param number will be passed to the function that extracts a certain
     * amount of videos from the list
     * @param sortedVideos the list of videos to be sorted
     * @param field tells the criteria of comparison
     * (r -> rating, f -> favourite, d -> duration, v -> views)
     * @return a string with number sorted movies
     */
    public static String sortVideos(final String order, final int number,
                                    final List<Video> sortedVideos, final char field) {
        if (order.compareTo(Constants.ASCENDING) != 0
                && order.compareTo(Constants.DESCENDING) != 0) {
            return "error -> order unknown";
        }
        sortedVideos.sort(new Comparator<Video>() {
            @Override
            public int compare(final Video o1, final Video o2) {
                double cmp = -1;
                if (field == 'r') {
                    cmp = o1.getOverallRating() - o2.getOverallRating();
                }
                if (field == 'f') {
                    cmp = o1.getNrFavorites() - o2.getNrFavorites();
                }
                if (field == 'd') {
                    cmp = o1.getDuration() - o2.getDuration();
                }
                if (field == 'v') {
                    cmp = o1.getNrViews() - o2.getNrViews();
                }
                if (cmp == 0) {
                    // if equal average, sort by name
                    if (Objects.equals(order, Constants.ASCENDING)) {
                        return o1.getTitle().compareTo(o2.getTitle());
                    }
                    return o2.getTitle().compareTo(o1.getTitle());
                }
                // if objects are not equal, return the sorting flag depending on the order type
                return QuerySolver.checkCmpNotZero(cmp, order);
            }
        });
        return createStringVideosList(sortedVideos, number);
    }

    /**
     * function that extracts n videos from the sorted
     * list and returns them in a string form
     * @param sortedVideos the sorted list of videos
     * @param n the number of videos to be extracted
     * @return a string that represents the n extracted videos
     */
    public static String createStringVideosList(final List<Video> sortedVideos, final int n) {
        StringBuilder out = new StringBuilder();
        int cnt = 0, videosNumber = n;
        // if I need to extract more videos than the size of the list,
        // I will simply extract all of them
        if (videosNumber > sortedVideos.size()) {
            videosNumber = sortedVideos.size();
        }

        // put each video's title in the output string
        for (Video video : sortedVideos) {
            // when I have all the videos extracted, stop
            if (cnt == videosNumber) {
                break;
            }

            out.append(video.getTitle());
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
