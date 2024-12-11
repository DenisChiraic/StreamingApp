package org.stream.controller;

import org.stream.model.*;
import org.stream.model.exceptions.*;
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
        try {
            userService.registerUser(newUser);
        } catch (ValidationException e) {
            throw new BusinessLogicException("Error by register the User: " + e.getMessage());
        }
    }

    /**
     * Autentifică utilizatorul pe baza numelui de utilizator și parolei.
     * În cazul autentificării cu succes, începe o sesiune pentru utilizatorul respectiv.
     * @param username Numele de utilizator.
     * @param password Parola utilizatorului.
     * @return true dacă autentificarea a fost realizată cu succes, false altfel.
     */
    public boolean login(String username, String password) {
        try {
            Optional<User> user = userService.authenticateUser(username, password);
            if (user.isPresent()) {
                sessionService.startSession(user.get());
                return true;
            } else {
                throw new EntityNotFoundException("Log in failed. User not found. Pleas register");
            }
        } catch (ValidationException e) {
            throw new BusinessLogicException("Invalid data" + e.getMessage());
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
            throw new BusinessLogicException("No user logged in.");
        }
    }

    /**
     * Adaugă un conținut (film sau serial) la lista de vizionare a utilizatorului curent.
     * @param content Conținutul care urmează a fi adăugat.
     */
    public void addToWatchList(Object content) {
        if (!sessionService.isUserLoggedIn()) {
            throw new BusinessLogicException("Pleas log in first.");
        }

        try {
            if (content instanceof Movie) {
                sessionService.getCurrentUser().getWatchList().addMovie((Movie) content);
            } else if (content instanceof Serial) {
                sessionService.getCurrentUser().getWatchList().addSerial((Serial) content);
            } else {
                throw new ValidationException("Content type invalid.");
            }
        } catch (Exception e) {
            throw new BusinessLogicException("Error by adding to WatchList" + e.getMessage());
        }
    }

    /**
     * Elimină un conținut (film sau serial) din lista de vizionare a utilizatorului curent.
     * @param content Conținutul care urmează a fi eliminat.
     */
    public void removeFromWatchList(Object content) {
        if (!sessionService.isUserLoggedIn()) {
            throw new BusinessLogicException("Log in first");
        }

        try {
            if (content instanceof Movie) {
                sessionService.getCurrentUser().getWatchList().removeMovie((Movie) content);
            } else if (content instanceof Serial) {
                sessionService.getCurrentUser().getWatchList().removeSerial((Serial) content);
            } else {
                throw new ValidationException("Invalid type");
            }
        } catch (Exception e) {
            throw new BusinessLogicException("Error by removing from WatchList " + e.getMessage());
        }
    }

    /**
     * Afișează lista de vizionare a utilizatorului curent.
     */
    public void displayWatchList() {
        if (!sessionService.isUserLoggedIn()) {
            throw new BusinessLogicException("Log in first");
        }
        sessionService.getCurrentUser().getWatchList().displayWatchList();
    }

    /**
     * Permite utilizatorului să vizioneze un film. Adaugă filmul la istoricul de vizionare.
     * @param movie Filmul care urmează a fi vizionat.
     */
    public void watchMovie(Movie movie) {
        if (!sessionService.isUserLoggedIn()) {
            throw new BusinessLogicException("Log in first");
        }
        System.out.println("Now playing " + movie.getTitle());
        sessionService.getCurrentUser().getHistoryList().addContent(movie.getTitle(), "Movie");
    }

    /**
     * Permite utilizatorului să vizioneze un episod dintr-un serial. Adaugă episodul la istoricul de vizionare.
     * @param serial Serialul care conține episodul.
     * @param episode Episodul care urmează a fi vizionat.
     */
    public void watchSerial(Serial serial, Episode episode) {
        if (!sessionService.isUserLoggedIn()) {
            throw new BusinessLogicException("Log in first");
        }
        System.out.println("Now playing " + episode.getEpisodeName() + " of Serial " + serial.getTitle());
        sessionService.getCurrentUser().getHistoryList().addContent(serial.getTitle(), "Episode");
    }

    /**
     * Afișează istoricul de vizionare al utilizatorului curent.
     */
    public void displayHistoryList() {
        if (!sessionService.isUserLoggedIn()) {
            throw new BusinessLogicException("Log in first");
        }
        sessionService.getCurrentUser().getHistoryList().displayHistory();
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

    /**
     * Returnează utilizatorul curent autentificat.
     * @return Utilizatorul curent.
     */
    public User getCurrentUser() {
        return sessionService.getCurrentUser();
    }

}
