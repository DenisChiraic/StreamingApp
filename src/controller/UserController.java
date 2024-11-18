package controller;

import model.*;
import service.UserService;
import service.ContentService;

import javax.swing.text.AbstractDocument;
import java.util.Optional;

/**
 * Controller pentru gestionarea utilizatorilor și conținutului.
 */
public class UserController {
    private final UserService userService;
    private final ContentService contentService;
    private User currentUser;

    /**
     * Constructor pentru UserController.
     *
     * @param userService   Serviciul pentru gestionarea utilizatorilor.
     * @param contentService Serviciul pentru gestionarea conținutului.
     */
    public UserController(UserService userService, ContentService contentService) {
        this.userService = userService;
        this.contentService = contentService;
    }

    /**
     * Înregistrează un utilizator nou.
     *
     * @param newUser Utilizatorul de înregistrat.
     */
    public void registerUser(User newUser) {
        userService.registerUser(newUser);
    }

    /**
     * Autentifică un utilizator pe baza numelui și parolei.
     *
     * @param username Numele utilizatorului.
     * @param password Parola utilizatorului.
     * @return `true` dacă autentificarea este reușită, altfel `false`.
     */
    public boolean login(String username, String password) {
        Optional<User> user = userService.authenticateUser(username, password);
        if (user.isPresent()) {
            currentUser = user.get();
            System.out.println("Login successful");

            currentUser.setWatchList(DataManager.loadWatchList("watchlist_" + currentUser.getUsername() + ".csv"));
            currentUser.setHistoryList(DataManager.loadHistoryList("historylist_" + currentUser.getUsername() + ".csv"));

            return true;
        } else {
            System.out.println("Login failed");
            return false;
        }
    }

    public void logout() {
       if (currentUser != null) {
           DataManager.saveWatchList(currentUser.getWatchList(), "watchlist_" + currentUser.getUsername() + ".csv");
           DataManager.saveHistoryList(currentUser.getHistoryList(), "historylist_" + currentUser.getUsername() + ".csv");

           currentUser = null;
           System.out.println("Logout successful");
       } else {
           System.out.println("No user is currently logged in");
       }
    }

    public void addToWatchList(Object content) {
        if (currentUser != null) {
            if (content instanceof Movie) {
                currentUser.getWatchList().addMovie((Movie) content);
            } else if (content instanceof Serial) {
                currentUser.getWatchList().addSerial((Serial) content);
            }
        }
    }

    public void removeFromWatchList(Object content) {
       if (currentUser != null) {
           if (content instanceof Movie) {
               currentUser.getWatchList().removeMovie((Movie) content);
           } else if (content instanceof Serial) {
               currentUser.getWatchList().removeSerial((Serial) content);
           }
       }
    }

    public void displayWatchList() {
        if (currentUser != null) {
            currentUser.getWatchList().displayWatchList();
        } else {
            System.out.println("Pleas log in first");
        }
    }

    public void displayHistoryList() {
        if (currentUser != null) {
            currentUser.getHistoryList().getHistory().forEach(System.out::println);
        } else {
            System.out.println("Pleas log in first");
        }
    }

    public User getCurrentUser() {
        return currentUser;
    }

    public void watchMovie(Movie movie) {
        if (currentUser != null) {
            System.out.println("Now playing " + movie.getTitle());
            currentUser.getHistoryList().addContent(movie.getTitle(), "Movie");
        }
    }

    public void watchSerial(Serial serial, Episode episode) {
        if (currentUser != null) {
            System.out.println("Now playing episode:" + episode.getEpisodeName() + " of Serial: " + serial.getTitle() + " with EpisodeNumber: " + episode.getEpisodeNumber());
            currentUser.getHistoryList().addContent(serial.getTitle(), "Episode");
        }

    }

}
