package myFiles;

import actor.ActorsAwards;
import common.Constants;
import fileio.ActionInputData;

import java.util.*;

import static utils.Utils.stringToAwards;

public class QuerrySolver {
    public static String solve(final ActionInputData querry) {
        String result = "";
        if (Objects.equals(querry.getObjectType(), Constants.ACTORS)) {
            result = solveActors(querry);
        }
        if (Objects.equals(querry.getObjectType(), Constants.MOVIES)) {
            result = solveVideos(querry, 'm');
        }
        if (Objects.equals(querry.getObjectType(), Constants.SHOWS)) {
            result = solveVideos(querry, 's');
        }
        if (Objects.equals(querry.getObjectType(), Constants.USERS)) {
            result = solveUsers(querry);
        }
        return "Query result: [" + result + "]";
    }

    public static String solveUsers(final ActionInputData querry) {
        List<User> sortedUsers = new ArrayList<>();
        for (User user : Database.getInstance().getUsersList()) {
            if (user.getNrRatings() != 0)
                sortedUsers.add(user);
        }
        return sortUsers(querry.getSortType(), querry.getNumber(), sortedUsers);
    }

    public static String sortUsers(String order, int number, final List<User> sortedUsers) {
        if (order.compareTo(Constants.ASCENDING) != 0 && order.compareTo(Constants.DESCENDING) != 0)
            return "error -> order unknown";

        sortedUsers.sort(new Comparator<User>() {
            @Override
            public int compare(final User o1, final User o2) {
                int cmp = o1.getNrRatings() - o2.getNrRatings();
                if (cmp == 0) {
                    // if equal average, sort by name
                    if (Objects.equals(order, Constants.ASCENDING))
                        return o1.getUsername().compareTo(o2.getUsername());
                    return o2.getUsername().compareTo(o1.getUsername());
                }
                if (cmp < 0)
                    if (Objects.equals(order, Constants.ASCENDING))
                        return -1;
                    else
                        return 1;
                if (Objects.equals(order, Constants.ASCENDING))
                    return 1;
                return -1;
            }
        });

        return createStringUsersList(sortedUsers, number);
    }

    public static String createStringUsersList(List<User> sortedUsers, int n) {
        StringBuilder out = new StringBuilder();
        int cnt = 0;
        // daca avem de scos mai multe videos decat are lista, le scoatem pe toate si atat
        if (n > sortedUsers.size())
            n = sortedUsers.size();

        // imi bag numele fiecarui video in string
        for (User user : sortedUsers) {
            // cand am scos destui actori, ne oprim
            if (cnt == n)
                break;

            out.append(user.getUsername());
            out.append(", ");
            cnt++;
        }

        // sterg ultimele 2 caractere in plus daca exista
        if (out.length() > 3)
            out.delete(out.length() - 2, out.length());
        return out.toString();
    }

    // type m -> movie, s -> serial
    public static String solveVideos(final ActionInputData querry, char type) {
        if (Objects.equals(querry.getCriteria(), Constants.RATINGS)) {
            if (type == 'm')
                return solveRating(querry, Database.getInstance().getMoviesListCasted());
            else
                return solveRating(querry, Database.getInstance().getSerialsListCasted());
        }
        if (Objects.equals(querry.getCriteria(), Constants.FAVORITE)) {
            if (type == 'm')
                return solveFavorite(querry, Database.getInstance().getMoviesListCasted());
            else
                return solveFavorite(querry, Database.getInstance().getSerialsListCasted());
        }
        if (Objects.equals(querry.getCriteria(), Constants.LONGEST)) {
            if (type == 'm')
                return solveLongest(querry, Database.getInstance().getMoviesListCasted());
            else
                return solveLongest(querry, Database.getInstance().getSerialsListCasted());
        }
        if (Objects.equals(querry.getCriteria(), Constants.MOST_VIEWED)) {
            if (type == 'm')
                return solveMostViewed(querry, Database.getInstance().getMoviesListCasted());
            else
                return solveMostViewed(querry, Database.getInstance().getSerialsListCasted());
        }
        return "error -> criteria unknown";
    }

    public static String solveMostViewed (final ActionInputData querry, final List<Video> videos) {
        List<Video> ratedVideos = new ArrayList<>();
        for (Video video : videos) {
            if (video.checkFilters(querry.getFilters()) && video.getNrViews() > 0) {
                ratedVideos.add(new Video(video));
            }
        }
        return sortMovies(querry.getSortType(), querry.getNumber(), ratedVideos, 'v');
    }

    public static String solveLongest (final ActionInputData querry, final List<Video> videos) {
        List<Video> ratedVideos = new ArrayList<>();
        for (Video video : videos) {
            if (video.checkFilters(querry.getFilters()))
                ratedVideos.add(new Video(video));
        }
        return sortMovies(querry.getSortType(), querry.getNumber(), ratedVideos, 'd');
    }

    public static String solveFavorite (final ActionInputData querry, final List<Video> videos) {
        List<Video> ratedVideos = new ArrayList<>();
        for (Video video : videos) {
            if (video.checkFilters(querry.getFilters()) && video.getNrFavorites() > 0)
                ratedVideos.add(new Video(video));
        }
        return sortMovies(querry.getSortType(), querry.getNumber(), ratedVideos, 'f');
    }

    public static String solveRating(final ActionInputData querry, final List<Video> videos) {
        List<Video> ratedVideos = new ArrayList<>();
        for (Video video : videos) {
            if (video.checkFilters(querry.getFilters()) && video.getOverallRating() > 0)
                ratedVideos.add(new Video(video));
        }
        return sortMovies(querry.getSortType(), querry.getNumber(), ratedVideos, 'r');
    }

    // char field gives the field to be compared (r -> ratinng, f -> favourite, d -> duration, v -> views)
    public static String sortMovies(String order, int number, final List<Video> sortedVideos, char field) {
        if (order.compareTo(Constants.ASCENDING) != 0 && order.compareTo(Constants.DESCENDING) != 0)
            return "error -> order unknown";
        // sort by order
        sortedVideos.sort(new Comparator<Video>() {
            @Override
            public int compare(final Video o1, final Video o2) {
                double cmp = -1;
                if (field == 'r')
                    cmp = o1.getOverallRating() - o2.getOverallRating();
                if (field == 'f')
                    cmp = o1.getNrFavorites() - o2.getNrFavorites();
                if (field == 'd')
                    cmp = o1.getDuration() - o2.getDuration();
                if (field == 'v')
                    cmp = o1.getNrViews() - o2.getNrViews();
                if (cmp == 0) {
                    // if equal average, sort by name
                    if (Objects.equals(order, Constants.ASCENDING))
                        return o1.getTitle().compareTo(o2.getTitle());
                    return o2.getTitle().compareTo(o1.getTitle());
                }
                if (cmp < 0)
                    if (Objects.equals(order, Constants.ASCENDING))
                        return -1;
                    else
                        return 1;
                if (Objects.equals(order, Constants.ASCENDING))
                    return 1;
                return -1;
            }
        });
        return createStringVideosList(sortedVideos, number);
    }

    public static String createStringVideosList (final List<Video> videos, int n) {
        StringBuilder out = new StringBuilder();
        int cnt = 0;
        // daca avem de scos mai multe videos decat are lista, le scoatem pe toate si atat
        if (n > videos.size())
            n = videos.size();

        // imi bag numele fiecarui video in string
        for (Video video : videos) {
            // cand am scos destui actori, ne oprim
            if (cnt == n)
                break;

            out.append(video.getTitle());
            out.append(", ");
            cnt++;
        }

        // sterg ultimele 2 caractere in plus daca exista
        if (out.length() > 3)
            out.delete(out.length() - 2, out.length());
        return out.toString();
    }

    public static String solveActors(final ActionInputData querry) {
        if (Objects.equals(querry.getCriteria(), Constants.AVERAGE)) {
            return solveAverage(querry, Database.getInstance().getActorList());
        }
        if (Objects.equals(querry.getCriteria(), Constants.AWARDS)) {
            return solveAwards(querry, Database.getInstance().getActorList());
        }
        if (Objects.equals(querry.getCriteria(), Constants.FILTER_DESCRIPTIONS)) {
            return solveDescription(querry, Database.getInstance().getActorList());
        }
        return "error -> criteria unknown";
    }

    public static String solveAverage(final ActionInputData querry, final List<Actor> actors) {
        List<Actor> sortedActors = new ArrayList<>();
        for (Actor actor : actors) {
            sortedActors.add(new Actor(actor));
        }

        return sortActors(querry.getSortType(), querry.getNumber(), sortedActors, 'r', 'a');
    }

    public static String solveAwards(final ActionInputData querry, final List<Actor> actors) {
        // extract the list of awards from querry
        List<String> awardsString = querry.getFilters().get(3);
        // convert the strings to enum items
        List<ActorsAwards> awardsEnum = new ArrayList<>();
        for (String award : awardsString)
            awardsEnum.add(stringToAwards(award));

        List<Actor> sortedActors = new ArrayList<>();
        for (Actor actor : actors) {
            if (actor.checkAwards(awardsEnum)) {
                actor.computeTotalAwards();
                sortedActors.add(new Actor(actor));
            }
        }

        // sort the actors
        return sortActors(querry.getSortType(), sortedActors.size(), sortedActors, 'a', 'b');
    }

    public static String solveDescription(final ActionInputData querry, final List<Actor> actors) {
        // extract the list of keyword from querry
        List<String> keywords = querry.getFilters().get(2);

        List<Actor> sortedActors = new ArrayList<>();
        for (Actor actor : actors) {
            if (actor.containsKeywords(keywords)) {
                sortedActors.add(new Actor(actor));
            }
        }
        // sort the actors
        return sortActors(querry.getSortType(), sortedActors.size(), sortedActors, 'n', 'b');
    }

    // field gives the field to be compared (a -> awards, r -> ranking/average, n -> name)
    public static String sortActors(String order, int number, final List<Actor> sortedActors, char field, char querryType) {
        if (order.compareTo(Constants.ASCENDING) != 0 && order.compareTo(Constants.DESCENDING) != 0)
            return "error -> order unknown";
        // sort by order
        sortedActors.sort(new Comparator<Actor>() {
            @Override
            public int compare(final Actor o1, final Actor o2) {
                double cmp = -1;
                if (field == 'a')
                    cmp = o1.getTotalAwards() - o2.getTotalAwards();
                if (field == 'r')
                    cmp = o1.getAverage()- o2.getAverage();
                if (field == 'n')
                    cmp = 0;
                if (cmp == 0) {
                    // if equal average, sort by name
                    if (Objects.equals(order, Constants.ASCENDING))
                        return o1.getName().compareTo(o2.getName());
                    return o2.getName().compareTo(o1.getName());
                }
                if (cmp < 0)
                    if (Objects.equals(order, Constants.ASCENDING))
                        return -1;
                    else
                        return 1;
                if (Objects.equals(order, Constants.ASCENDING))
                    return 1;
                return -1;
            }
        });
        return createStringList(sortedActors, number, querryType);
    }

    // querryType flag tells if I have to check the average for actors
    public static String createStringList (final List<Actor> actors, int n, char querryType) {
        StringBuilder out = new StringBuilder();
        int cnt = 0;
        // daca avem de scos mai multi actori decat are lista, ii scoatem pe toti si atat
        if (n > actors.size())
            n = actors.size();

        // imi bag numele fiecarui actor in string
        for (Actor actor : actors) {
            // cand am scos destui actori, ne oprim
            if (cnt == n)
                break;
            // verific daca actorul are grade nenul
            if (querryType == 'a') {
                if (actor.getAverage() != 0 && querryType == 'a') {
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

        // sterg ultimele 2 caractere in plus daca exista
        if (out.length() > 3)
            out.delete(out.length() - 2, out.length());
        return out.toString();
    }
}
