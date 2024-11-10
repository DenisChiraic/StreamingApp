package service;

import model.User;
import repository.IRepo;

import java.util.Optional;

public class UserService {
    private final IRepo<User> userRepo;

    public UserService(IRepo<User> userRepo) {
        this.userRepo = userRepo;
    }

    public Optional<User> authenticateUser(String username, String password) {
        return userRepo.findAll().stream()
                .filter(user -> user.getUsername().equals(username) && user.getPassword().equals(password))
                .findFirst();
    }

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

    public IRepo<User> getUserRepo() {
        return userRepo;
    }
}
