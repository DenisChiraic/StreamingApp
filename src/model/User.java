package model;

import java.util.UUID;

/**
 * Clasa User reprezintă un utilizator al aplicației, incluzând informațiile de autentificare
 * și tipul de cont.
 */
public class User {
    private UUID id;
    private String username;
    private String password;
    private Account account;
    private WatchList watchList;
    private HistoryList historyList;

    /**
     * Constructor pentru clasa User.
     *
     * @param username Numele de utilizator.
     * @param password Parola utilizatorului.
     * @param account  Tipul de cont asociat utilizatorului.
     */
    public User(String username, String password, Account account) {
        this.id = UUID.randomUUID();
        this.username = username;
        this.password = password;
        this.account = account;
        this.watchList = new WatchList();
        this.historyList = new HistoryList();
    }

    /**
     * Returnează ID-ul utilizatorului.
     *
     * @return ID-ul utilizatorului.
     */
    public UUID getId() {
        return id;
    }

    /**
     * Returnează numele de utilizator.
     *
     * @return Numele de utilizator.
     */
    public String getUsername() {
        return username;
    }

    /**
     * Returnează parola utilizatorului.
     *
     * @return Parola utilizatorului.
     */
    public String getPassword() {
        return password;
    }

    /**
     * Returnează tipul de cont al utilizatorului.
     *
     * @return Tipul de cont.
     */
    public Account getAccount() {
        return account;
    }

    public WatchList getWatchList() {
        return watchList;
    }

    public HistoryList getHistoryList() {
        return historyList;
    }
}
