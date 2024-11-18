package service;

import model.User;
import repository.IRepo;

import java.util.List;
import java.util.Optional;

/**
 * Serviciu pentru gestionarea utilizatorilor.
 */
public class UserService {
    private final IRepo<User> userRepo;

    /**
     * Constructor pentru UserService.
     *
     * @param userRepo Repository-ul pentru utilizatori.
     */
    public UserService(IRepo<User> userRepo) {
        this.userRepo = userRepo;
    }


    /**
     * Autentifică un utilizator pe baza numelui de utilizator și a parolei.
     *
     * @param username Numele de utilizator.
     * @param password Parola.
     * @return Un obiect Optional cu utilizatorul, dacă autentificarea reușește.
     */
    public Optional<User> authenticateUser(String username, String password) {
        return userRepo.findAll().stream()
                .filter(user -> user.getUsername().equals(username) && user.getPassword().equals(password))
                .findFirst();
    }

    /**
     * Înregistrează un utilizator nou.
     *
     * @param newUser Utilizatorul de înregistrat.
     */
    public void registerUser(User newUser) {
        long userCount = userRepo.findAll().stream()
                .filter(user -> user.getAccount().getType().equals(newUser.getAccount().getType()))
                .count();

        if ((newUser.getAccount() instanceof model.FreeAccount && userCount >= 1) ||
                (newUser.getAccount() instanceof model.PremiumAccount && userCount >= 5)) {
            System.out.println("Account limit reached for " + newUser.getAccount().getType());
        } else {
            userRepo.create(newUser);
            System.out.println("User registered successfully");
        }
    }

    /**
     * Verifică dacă un utilizator există în sistem pe baza numelui de utilizator.
     *
     * @param username Numele de utilizator de verificat.
     * @return `true` dacă utilizatorul există, altfel `false`.
     */
    public boolean userExists(String username) {
        return userRepo.findAll().stream()
                .anyMatch(user -> user.getUsername().equals(username));
    }

    /**
     * Returnează lista completă de utilizatori.
     *
     * @return Lista de utilizatori.
     */
    public List<User> getAllUsers() {
        return userRepo.findAll();
    }

    /**
     * Returnează repository-ul utilizatorilor.
     *
     * @return Obiectul repository.
     */
    public IRepo<User> getUserRepo() {
        return userRepo;
    }

}
