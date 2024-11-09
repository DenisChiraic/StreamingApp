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
}
