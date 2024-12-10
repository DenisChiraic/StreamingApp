package org.stream.config;

/**
 * This class contains configuration constants for connecting to the PostgreSQL database
 * and accessing various tables used in the streaming application.
 */
public class DBConfig {

    /**
     * The database connection URL.
     * This specifies the location of the PostgreSQL database and the name of the database.
     */
    public static final String DB_URL = "jdbc:postgresql://localhost:5432/StreamingApp";

    /**
     * The database username used to authenticate the connection.
     */
    public static final String DB_USER = "postgres";

    /**
     * The database password used to authenticate the connection.
     */
    public static final String DB_PASSWORD = "1234";

    /**
     * The name of the Users table in the database.
     * This table stores the information of users registered in the streaming application.
     */
    public static final String USERS_TABLE = "Users";

    /**
     * The name of the Movies table in the database.
     * This table stores information about the movies available on the platform.
     */
    public static final String MOVIES_TABLE = "Movies";

    /**
     * The name of the Serials table in the database.
     * This table stores information about the serials (TV shows) available on the platform.
     */
    public static final String SERIALS_TABLE = "Serials";

    /**
     * The name of the Episodes table in the database.
     * This table stores information about the episodes of serials (TV shows).
     */
    public static final String EPISODES_TABLE = "Episodes";

    /**
     * The name of the Top10Movies table in the database.
     * This table stores the list of top 10 movies as rated by the users or the system.
     */
    public static final String TOP10MOVIES_TABLE = "Top10Movies";

    /**
     * The name of the Top10Serials table in the database.
     * This table stores the list of top 10 serials (TV shows) as rated by the users or the system.
     */
    public static final String TOP10SERIALS_TABLE = "Top10Series";

    /**
     * The name of the WatchList table in the database.
     * This table stores the list of movies and serials that a user has added to their watchlist.
     */
    public static final String WATCHLIST_TABLE = "WatchList";

    /**
     * The name of the HistoryList table in the database.
     * This table stores the history of movies and serials that a user has watched.
     */
    public static final String HISTORY_LIST_TABLE = "HistoryList";
}
