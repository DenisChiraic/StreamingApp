package controller;

import model.*;
import service.UserService;
import service.ContentService;

import javax.swing.text.AbstractDocument;
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

    public void registerUser(User newUser) {
        userService.registerUser(newUser);
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

    public void logout() {
       currentUser = null;
        System.out.println("Logout successful");
    }

    public void addToWatchList(Object content) {
        if (content instanceof Movie) {
            watchList.addMovie((Movie) content);
        } else if (content instanceof Serial) {
            watchList.addSerial((Serial) content);
        } else {
            System.out.println("Invalid content");
        }
    }

    public void removeFromWatchList(Object content) {
        if (content instanceof Movie) {
            watchList.removeMovie((Movie) content);
        } else if (content instanceof Serial) {
            watchList.removeSerial((Serial) content);
        } else {
            System.out.println("Invalid content");
        }
    }

    public void displayWatchList() {
        watchList.displayWatchList();
    }

    public void displayHistoryList() {
        historyList.getHistory().forEach(System.out::println);
    }

    public User getCurrentUser() {
        return currentUser;
    }

    public void watchMovie(Movie movie) {
        System.out.println("Now playing movie: " + movie.getTitle());
        historyList.addContent(movie.getTitle(), "Movie");
    }

    public void watchSerial(Serial serial, Episode episode) {
        System.out.println("Now playing episode:" + episode.getEpisodeName() + " of Serial: " + serial.getTitle() + " with EpisodeNumber: " + episode.getEpisodeNumber());
        historyList.addContent(serial.getTitle(), "Episode");
    }

}
