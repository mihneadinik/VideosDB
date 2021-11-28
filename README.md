# VideosDB
## Dinica Mihnea-Gabriel 323CA

1. Structurarea codului

For each newly created class used for implementing the program I created packages as follows:
* databases only contains the main database of the program
* entities contains 2 classes (Actor and User) that are related to certain people
* solvers contains the classes used for solving the actions received from the repository, and it also contains a subpackage named queries that has the specialised classes for solving certain query interogations
* watchables contains the Video class with its desccendants Movie and Serial, representing objects that can be watched

2. Workflow

I created a main database class of Singleton type for safety reasons that is used to store all the information about the platform read from the input in 4 different lists of objects (the users in the platform, the videos that it contains and the actors that have played in these videos). For each of the 4 objects it has a method that parses the input received from the repository and creates a list with all the objects. It also has different getters, either for a complete list or for a certain object in one of these, and a method that populates a HashTable with the genres and their total number of view.
Each object from the database has a class of its own with all the fiels required in the homeworks' description and a few extra ones that are needed to execute certain actions. For example:
- Actor class has 2 extra fields for keeping track of the number of an actor's awards and the average rating of the videos he played in. It has methods that compute these and methods that check its fields to tell wheter it matches the criteria received in certain queries or not.
- User class has 2 extra fields, one is a Map that keeps track of the videos he has rated and the rating he gave and another that counts how many ratings he gave in total. It also has methods used to solve each command (rate a video, view it, add as favorite) and methods that check whether he has rated, viewed or marked as favorite some video or what type of subscription he has.
- Video class is the base class for movies and serials, containing methods meant to be overriden by each class (such as the way to compute the total duration or the average rating for a serial) and extra fields necessary to resolve the actions: keeping track of the number of times a video has been viewed or marked as favorite or its position in the database. It has methods to check if a video matches certain filters received in queries)
- Movie class has a list of ratings of its own and overrides the method to compute average rating
- Serial class has a list of seasons and the number of seasons as extra fields, special methods to extract a certain season or to rate it and overriden methods to compute the total duration and average rating.

3. Solving the actions

The actions to be solved are stored and executed in a separate class, ActionSolver, that has 2 functions: firstly it calls the appropriate object to solve a certain type of action and then it transforms the output in a JSON style to be added in the final results array.
These actions are of 3 types: Command, Query and Recommendation, each of them has a different class that implements their sollution (Query also have 3 sub-classes for readability reasons, otherwise the code would have been too long).
Each command received is related to a certain user that can view, rate or mark as favorite a video. For doing this, CommandSolver class has a different method for each type of command in which the certain user and video are extracted from the database and the command is solved. At the end, each command returns either an error or success message.
Each query relates either to an actor, user or certain video (movie or serial) and it involves searching through the database to extract certain objects that match some given filters and are ordered by certain criteria. For doing this, the QuerySolver class calls the appropriate sub-class specialised in solving queries for each type of objects (for movie and serial it is the same class, called with a different flag, as both of them derive from the video class).
All the classes that answer queries have 2 common methods: one sorts a list of objects according to certain of their fields (according to what the query asks) and another one that extracts the name of the ordered objects and outputs them in a String form to the ActionSolver class..
- ActorsQuery has to solve 3 different type of queries and has a method for each of them. They take a list with all the actors in the database and for each of them checks if it corresponds to the query interrogation.
- UsersQuery only has to return the most active users
- VideosQuery has to solve 4 different type of queries and has a method for each of them. All of them are created in a manner that can solve both movies and queries interrogations by providing a list with the videos of a certain type.
Each recommendation is applied to a certain user and premium users have 3 special recommendations that can receive. Firstly, this class checks if the recommendation can be applied to the user and only after this it solves it. For doing this, a list with all the videos is required, then the methods only keep those that match the criteria imposed in the input and return the first video that has not been seen by that user. When solving the "Popular" recommendation it was usedd a HashTable that for each genre possible it stored the total number of views it got and later extracted and returned the entry with the most number of views; by doing this, I ensured that I can recursively apply the same method until I find a video from a popular genre that has not previously been seen by the user.
