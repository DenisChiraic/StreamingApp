package presentation;

import controller.UserController;
import model.Episode;
import model.Movie;
import model.Serial;
import repository.InMemoryRepo;
import service.ContentService;
import service.UserService;

import java.util.Arrays;
import java.util.Scanner;

public class ConsoleApp {
    private UserController userController;
    private ContentService contentService;
    private Scanner scanner;

    public ConsoleApp() {
        this.scanner = new Scanner(System.in);
        initServices();
    }

    private void initServices() {
        UserService userService = new UserService(new InMemoryRepo<>());
        contentService = new ContentService(new InMemoryRepo<>(), new InMemoryRepo<>());
        userController = new UserController(userService, contentService);
    }

    public void start() {
        boolean running = true;
    }

    private void login() {
        System.out.println("Username: ");
        String username = scanner.nextLine();
        System.out.println("Password: ");
        String password = scanner.nextLine();
        userController.login(username, password);
    }

    private void addToWatchList() {
        if (userController.getCurrentUser() != null) {
            System.out.println("1. Add movie / 2. Add serial");
            int option = scanner.nextInt();
            if (option == 1) {
                Movie movie = new Movie("X Man", 120, 4.2);
                userController.addToWatchList(movie);
            } else if (option == 2) {
                Serial serial = new Serial("The Flash", Arrays.asList(new Episode("The beginning", 1)), 4.7);
                userController.addToWatchList(serial);
            } else {
                System.out.println("Invalid option");
            }
        } else {
            System.out.println("Please login first");
        }
    }

    private void removeFromWatchList() {

    }

    private void watchEpisode() {

    }

    private void viewWatchList() {
        userController.displayWatchList();
    }

    private void viewHistoryList() {
        userController.displayHistoryList();
    }

    private void logout() {
        userController.logout();
    }

    public static void main(String[] args) {
        ConsoleApp app = new ConsoleApp();
        app.start();
    }
}
