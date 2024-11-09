package controller;

import model.*;
import service.UserService;
import service.ContentService;

import java.util.Objects;
import java.util.Optional;

public class UserController {
    private UserService userService;
    private ContentService contentService;
    private User currentUser;
    private WatchList watchList;
    private HistoryList historyList;

    public UserController(UserService userService, ContentService contentService) {
        this.userService = userService;
        this.contentService = contentService;
        this.watchList = new WatchList();
        this.historyList = new HistoryList();
    }

    public boolean login(String username, String password) {
        Optional<User> user = userService.authenticateUser(username, password);
        if (user.isPresent()) {
            currentUser = user.get();
            System.out.println("Login successful");
            return true;
        } else {
            System.out.println("Login failed");
            return false;
        }
    }

    public boolean logout() {
       return false;
    }

    public void addToWatchList(Object content) {

    }

    public void displayWatchList() {
        watchList.displayWatchList();
    }

    public User getCurrentUser() {
        return currentUser;
    }

}
