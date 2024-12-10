package org.stream.model;

import java.util.UUID;

/**
 * Clasa User reprezintă un utilizator al aplicației, incluzând informațiile de autentificare
 * și tipul de cont.
 */
public class User implements Identifiable {
    private UUID id;
    private String username;
    private String password;
    private Account account;
    private boolean isAdmin;
    private WatchList watchList;
    private HistoryList historyList;

    /**
     * Constructor pentru clasa User.
     *
     * @param username Numele de utilizator.
     * @param password Parola utilizatorului.
     * @param account  Tipul de cont asociat utilizatorului.
     *
     */
    public User(String username, String password, Account account, boolean isAdmin) {
        this.id = UUID.randomUUID();
        this.username = username;
        this.password = password;
        this.account = account;
        this.isAdmin = isAdmin;
        this.watchList = new WatchList();
        this.historyList = new HistoryList();
    }

    public User(UUID id, String username, String password, boolean isAdmin) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.isAdmin = isAdmin;
    }

    /**
     * Constructor pentru utilizator cu date existente.
     *
     * @param id         ID-ul utilizatorului.
     * @param username   Numele de utilizator.
     * @param password   Parola utilizatorului.
     * @param account    Tipul de cont.
     * @param watchList  Lista de vizionare.
     * @param historyList Istoricul vizionărilor.
     */
    public User(UUID id, String username, String password, Account account, WatchList watchList, HistoryList historyList) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.account = account;
        this.watchList = watchList;
        this.historyList = historyList;
    }

    public boolean isAdmin() {
        return isAdmin;
    }

    /**
     * Returnează ID-ul utilizatorului.
     *
     * @return ID-ul utilizatorului.
     */
    public UUID getId() {
        return id;
    }
    public void setId(UUID id) {
        this.id = id;
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

    /**
     * Returnează lista de vizionare a utilizatorului.
     *
     * @return Lista de vizionare.
     */
    public WatchList getWatchList() {
        return watchList;
    }

    /**
     * Setează lista de vizionare a utilizatorului.
     *
     * @param watchList Lista de vizionare.
     */
    public void setWatchList(WatchList watchList) {
        this.watchList = watchList;
    }

    /**
     * Returnează istoricul vizionărilor al utilizatorului.
     *
     * @return Istoricul vizionărilor.
     */
    public HistoryList getHistoryList() {
        return historyList;
    }

    /**
     * Setează istoricul vizionărilor al utilizatorului.
     *
     * @param historyList Istoricul vizionărilor.
     */
    public void setHistoryList(HistoryList historyList) {
        this.historyList = historyList;
    }

}
