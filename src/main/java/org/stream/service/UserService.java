package org.stream.service;

import org.stream.model.FreeAccount;
import org.stream.model.PremiumAccount;
import org.stream.model.User;
import org.stream.model.mappers.UserMapper;
import org.stream.repository.DatabaseRepo;

import java.util.Optional;
import java.util.UUID;

/**
 * Serviciul pentru gestionarea utilizatorilor.
 * Oferă metode pentru autentificare, înregistrare, ștergere și verificare a existenței utilizatorilor.
 */
public class UserService {
    private final DatabaseRepo<User> userRepo;

    /**
     * Constructor care inițializează serviciul de utilizatori și repo-ul acestora.
     */
    public UserService() {
        this.userRepo = new DatabaseRepo<>("users", new UserMapper());
    }

    /**
     * Autentifică un utilizator pe baza numelui de utilizator și parolei.
     * @param username Numele de utilizator al utilizatorului.
     * @param password Parola utilizatorului.
     * @return Un obiect Optional care conține utilizatorul autentificat dacă există, sau este gol dacă nu.
     */
    public Optional<User> authenticateUser(String username, String password) {
        return userRepo.findAll().stream()
                .filter(user -> user.getUsername().equals(username) && user.getPassword().equals(password))
                .findFirst();
    }

    /**
     * Înregistrează un nou utilizator.
     * Verifică dacă utilizatorul există deja și dacă limita de conturi pentru tipul de cont (Free sau Premium) a fost atinsă.
     * @param newUser Utilizatorul care urmează a fi înregistrat.
     * @throws IllegalArgumentException Dacă username-ul este deja utilizat de un alt utilizator.
     */
    public void registerUser(User newUser) {
        if (userExists(newUser.getUsername())) {
            throw new IllegalArgumentException("Username-ul este deja folosit.");
        }

        long userCount = userRepo.findAll().stream()
                .filter(user -> user.getAccount().getType().equals(newUser.getAccount().getType()))
                .count();

        if ((newUser.getAccount() instanceof FreeAccount && userCount >= 1) ||
                (newUser.getAccount() instanceof PremiumAccount && userCount >= 5)) {
            System.out.println("Limita de conturi a fost atinsă pentru " + newUser.getAccount().getType());
        } else {
            userRepo.create(newUser);
            System.out.println("Utilizator înregistrat cu succes");
        }
    }

    /**
     * Șterge un utilizator pe baza numelui de utilizator.
     * @param username Numele de utilizator al utilizatorului de șters.
     */
    public void deleteUser(String username) {
        Optional<User> userToDelete = findByUsername(username);

        if (userToDelete.isPresent()) {
            UUID userId = userToDelete.get().getId();
            userRepo.delete(userId);
            System.out.println("User with username " + username + " deleted successfully.");
        } else {
            System.out.println("User with username " + username + " not found.");
        }
    }

    /**
     * Verifică dacă există un utilizator cu un anumit nume de utilizator.
     * @param username Numele de utilizator de verificat.
     * @return true dacă utilizatorul există, false altfel.
     */
    public boolean userExists(String username) {
        return userRepo.findAll().stream()
                .anyMatch(user -> user.getUsername().equals(username));
    }

    /**
     * Căută un utilizator după numele de utilizator.
     * @param username Numele de utilizator al utilizatorului de căutat.
     * @return Un obiect Optional care conține utilizatorul găsit sau un Optional gol dacă nu a fost găsit.
     */
    public Optional<User> findByUsername(String username) {
        return userRepo.findAll().stream()
                .filter(user -> user.getUsername().equals(username))
                .findFirst();
    }
}
