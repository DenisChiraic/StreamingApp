package org.stream.controller;

import org.stream.model.*;
import org.stream.service.ContentService;
import org.stream.service.SessionService;
import org.stream.service.UserService;

import java.util.Optional;

/**
 * Controllerul utilizatorului care gestionează interacțiunile utilizatorului cu aplicația.
 * Permite utilizatorului să se înregistreze, să se autentifice, să adauge și să elimine conținut din lista de vizionare,
 * să vizioneze filme și episoade, și să vizualizeze istoricul de vizionare.
 */
public class UserController {
    private final UserService userService;
    private final ContentService contentService;
    private final SessionService sessionService;

    /**
     * Constructor pentru a inițializa controller-ul cu serviciile necesare.
     * @param userService Serviciul utilizatorilor.
     * @param contentService Serviciul de gestionare al conținutului.
     * @param sessionService Serviciul de sesiuni utilizator.
     */
    public UserController(UserService userService, ContentService contentService, SessionService sessionService) {
        this.userService = userService;
        this.contentService = contentService;
        this.sessionService = sessionService;
    }

    /**
     * Înregistrează un nou utilizator.
     * @param newUser Utilizatorul care urmează a fi înregistrat.
     */
    public void registerUser(User newUser) {
        userService.registerUser(newUser);
    }

    /**
     * Autentifică utilizatorul pe baza numelui de utilizator și parolei.
     * În cazul autentificării cu succes, începe o sesiune pentru utilizatorul respectiv.
     * @param username Numele de utilizator.
     * @param password Parola utilizatorului.
     * @return true dacă autentificarea a fost realizată cu succes, false altfel.
     */
    public boolean login(String username, String password) {
        Optional<User> user = userService.authenticateUser(username, password);
        if (user.isPresent()) {
            sessionService.startSession(user.get());
            return true;
        } else {
            System.out.println("Login failed");
            return false;
        }
    }

    /**
     * Închide sesiunea curentă a utilizatorului.
     * Afișează un mesaj în cazul în care nu există niciun utilizator logat.
     */
    public void logout() {
        if (sessionService.isUserLoggedIn()) {
            sessionService.endSession();
        } else {
            System.out.println("No user is currently logged in");
        }
    }

    /**
     * Adaugă un conținut (film sau serial) la lista de vizionare a utilizatorului curent.
     * @param content Conținutul care urmează a fi adăugat.
     */
    public void addToWatchList(Object content) {
        if (sessionService.isUserLoggedIn()) {
            if (content instanceof Movie) {
                sessionService.getCurrentUser().getWatchList().addMovie((Movie) content);
            } else if (content instanceof Serial) {
                sessionService.getCurrentUser().getWatchList().addSerial((Serial) content);
            } else {
                System.out.println("Invalid content type");
            }
        } else {
            System.out.println("Please log in first");
        }
    }

    /**
     * Elimină un conținut (film sau serial) din lista de vizionare a utilizatorului curent.
     * @param content Conținutul care urmează a fi eliminat.
     */
    public void removeFromWatchList(Object content) {
        if (sessionService.isUserLoggedIn()) {
            if (content instanceof Movie) {
                sessionService.getCurrentUser().getWatchList().removeMovie((Movie) content);
            } else if (content instanceof Serial) {
                sessionService.getCurrentUser().getWatchList().removeSerial((Serial) content);
            } else {
                System.out.println("Invalid content type");
            }
        } else {
            System.out.println("Please log in first");
        }
    }

    /**
     * Afișează lista de vizionare a utilizatorului curent.
     */
    public void displayWatchList() {
        if (sessionService.isUserLoggedIn()) {
            sessionService.getCurrentUser().getWatchList().displayWatchList();
        } else {
            System.out.println("Please log in first");
        }
    }

    /**
     * Permite utilizatorului să vizioneze un film. Adaugă filmul la istoricul de vizionare.
     * @param movie Filmul care urmează a fi vizionat.
     */
    public void watchMovie(Movie movie) {
        if (sessionService.isUserLoggedIn()) {
            System.out.println("Now playing " + movie.getTitle());
            sessionService.getCurrentUser().getHistoryList().addContent(movie.getTitle(), "Movie");
        } else {
            System.out.println("Please log in first.");
        }
    }

    /**
     * Permite utilizatorului să vizioneze un episod dintr-un serial. Adaugă episodul la istoricul de vizionare.
     * @param serial Serialul care conține episodul.
     * @param episode Episodul care urmează a fi vizionat.
     */
    public void watchSerial(Serial serial, Episode episode) {
        if (sessionService.isUserLoggedIn()) {
            System.out.println("Now playing " + episode.getEpisodeName() + " of Serial " + serial.getTitle());
            sessionService.getCurrentUser().getHistoryList().addContent(serial.getTitle(), "Episode");
        } else {
            System.out.println("Please log in first.");
        }
    }

    /**
     * Afișează istoricul de vizionare al utilizatorului curent.
     */
    public void displayHistoryList() {
        if (sessionService.isUserLoggedIn()) {
            sessionService.getCurrentUser().getHistoryList().displayHistory();
        } else {
            System.out.println("Please log in first.");
        }
    }

    /**
     * Returnează serviciul de conținut.
     * @return Serviciul de conținut.
     */
    public ContentService getContentService() {
        return contentService;
    }

    /**
     * Returnează serviciul de sesiune.
     * @return Serviciul de sesiune.
     */
    public SessionService getSessionService() {
        return sessionService;
    }

    /**
     * Returnează serviciul de utilizatori.
     * @return Serviciul de utilizatori.
     */
    public UserService getUserService() {
        return userService;
    }
}
