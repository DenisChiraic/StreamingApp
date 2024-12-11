package org.stream.service;

import org.stream.model.User;
import org.stream.model.exceptions.BusinessLogicException;

/**
 * Serviciul pentru gestionarea sesiunilor de utilizator.
 * Oferă metode pentru începutul și sfârșitul unei sesiuni, verificarea autentificării utilizatorului și obținerea utilizatorului curent.
 */
public class SessionService {
    private User currentUser;

    /**
     * Începe o sesiune pentru utilizatorul specificat.
     * Această metodă setează utilizatorul curent și afișează un mesaj de autentificare cu succes.
     * @param user Utilizatorul care se autentifică.
     */
    public void startSession(User user) {
        if (currentUser != null) {
            throw new BusinessLogicException("An user is already logged in");
        }
        this.currentUser = user;
        System.out.println("Login successful");
    }

    /**
     * Încheie sesiunea curentă, deconectând utilizatorul.
     * Dacă există un utilizator autentificat, sesiunea este închisă și se afișează un mesaj de deconectare cu succes.
     */
    public void endSession() {
       if (currentUser == null) {
           throw new BusinessLogicException("No user logged in");
       }
       currentUser = null;
       System.out.println("Logout successful");
    }

    /**
     * Verifică dacă un utilizator este autentificat în prezent.
     * @return true dacă un utilizator este autentificat, false altfel.
     */
    public boolean isUserLoggedIn() {
        return currentUser != null;
    }

    /**
     * Obține utilizatorul curent care este autentificat.
     * @return Utilizatorul curent sau null dacă nu există niciun utilizator autentificat.
     */
    public User getCurrentUser() {
        if (currentUser == null) {
            throw new BusinessLogicException("No user logged in");
        }
        return currentUser;
    }
}
