package presentation;

import controller.UserController;
import model.Episode;
import model.Movie;
import model.Serial;
import repository.InMemoryRepo;
import service.ContentService;
import service.UserService;

import java.util.Arrays;
import java.util.List;
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

        contentService.addMovie(new Movie("X Man", 124, 4.3));
        contentService.addMovie(new Movie("Titanic", 186, 4.8));
        contentService.addMovie(new Movie("Lion King", 90, 4.1));

        List<Episode> gameOfThrones = Arrays.asList(
                new Episode("Winter is Coming", 1),
                new Episode("The Kingsroad", 2),
                new Episode("Lord Snow", 3)
        );

        List<Episode> strangerThingsEpisodes = Arrays.asList(
                new Episode("Chapter One: The Vanishing of Will Byers", 1),
                new Episode("Chapter Two: The Weirdo on Maple Street", 2),
                new Episode("Chapter Three: Holly, Jolly", 3)
        );

        List<Episode> breakingBad = Arrays.asList(
                new Episode("Pilot", 1),
                new Episode("Cat's in the bag...", 2),
                new Episode("...And the bag's in the river", 3)
        );

        contentService.addSerial(new Serial("Game of Thrones", gameOfThrones, 4.9));
        contentService.addSerial(new Serial("Stranger Things", strangerThingsEpisodes, 4.4));
        contentService.addSerial(new Serial("Breaking Bad", breakingBad, 4.8));
    }

    public void start() {
        boolean running = true;
        while (running) {
            System.out.println("\n1. LogIn\n2. View Movies and Serials\n3. Add to WatchList\n4. View WatchList\n5. Remove from WatchList\n6. Watch Movie\n7. Watch Serial\n8. View History\n9. LogOut\n0. Exit");
            System.out.println("Choose an option: ");
            int choice = scanner.nextInt();
            scanner.nextLine();

            switch (choice) {
                case 1 -> login();
                case 2 -> viewMoviesAndSerials();
                case 3 ->addToWatchList();
                case 4 -> viewWatchList();
                case 5 -> removeFromWatchList();
                case 6 -> watchMovie();
                case 7 -> watchEpisode();
                case 8 -> viewHistoryList();
                case 9 -> logout();
                case 0 -> running = false;
                default -> System.out.println("Invalid choice");
            }
        }
    }

    private void login() {
        System.out.println("Username: ");
        String username = scanner.nextLine();
        System.out.println("Password: ");
        String password = scanner.nextLine();
        userController.login(username, password);
    }

    private void viewMoviesAndSerials() {
        if (userController.getCurrentUser() != null) {
            System.out.println("Available Movies:");
            contentService.getAllMovies().forEach(movie -> System.out.println("- " + movie.getTitle()));
            System.out.println("Available Serials:");
            contentService.getAllSerials().forEach(serial -> System.out.println("- " + serial.getTitle()));
        }
    }

    private void addToWatchList() {
        if (userController.getCurrentUser() != null) {
            System.out.println("1. Add movie / 2. Add serial");
            int option = scanner.nextInt();
            scanner.nextLine();

            if (option == 1) {
                System.out.println("Enter the name of the movie: ");
                String movieTitle = scanner.nextLine();
                Movie movie = contentService.getAllMovies().stream()
                        .filter(m -> m.getTitle().equals(movieTitle)).findFirst().orElse(null);

                if (movie != null) {
                    userController.addToWatchList(movie);
                } else {
                    System.out.println("Movie not found");
                }
            } else if (option == 2) {
                System.out.println("Enter the name of the serial: ");
                String serialTitle = scanner.nextLine();
                Serial serial = contentService.getAllSerials().stream()
                        .filter(s -> s.getTitle().equals(serialTitle)).findFirst().orElse(null);

                if (serial != null) {
                    contentService.addSerial(serial);
                } else {
                    System.out.println("Serial not found");
                }
            } else {
                System.out.println("Invalid option");
            }
        } else {
            System.out.println("Please login first");
        }
    }

    private void removeFromWatchList() {
        if (userController.getCurrentUser() != null) {
            System.out.println("1. Remove movie / 2. Remove serial");
            int option = scanner.nextInt();
            if (option == 1) {
                System.out.println("Enter the name of the movie to remove: ");
                String movieTitle = scanner.nextLine();
                Movie movie = contentService.getAllMovies().stream().
                        filter(m -> m.getTitle().equals(movieTitle)).findFirst().orElse(null);

                if (movie != null) {
                    userController.removeFromWatchList(movie);
                } else {
                    System.out.println("Movie not found");
                }
            } else if (option == 2) {
                System.out.println("Enter the name of the serial to remove: ");
                String serialTitle = scanner.nextLine();
                Serial serial = contentService.getAllSerials().stream().
                        filter(s -> s.getTitle().equals(serialTitle)).findFirst().orElse(null);

                if (serial != null) {
                    userController.removeFromWatchList(serial);
                } else {
                    System.out.println("Serial not found");
                }
            } else {
                System.out.println("Invalid option");
            }
        } else {
            System.out.println("Please login first");
        }
    }

    private void watchMovie() {
        if (userController.getCurrentUser() != null) {
            System.out.println("Enter the name of the movie to watch: ");
            String movieTitle = scanner.nextLine();
            Movie movie = contentService.getAllMovies().stream().
                    filter(m -> m.getTitle().equals(movieTitle)).findFirst().orElse(null);

            if (movie != null) {
                userController.watchMovie(movie);
            } else {
                System.out.println("Movie not found");
            }
        } else {
            System.out.println("Please login first");
        }
    }

    private void watchEpisode() {
        if (userController.getCurrentUser() != null) {
            System.out.println("Enter the name of the serial to watch: ");
            String serialTitle = scanner.nextLine();
            Serial serial = contentService.getAllSerials().stream().
                    filter(s -> s.getTitle().equals(serialTitle)).findFirst().orElse(null);

            if (serial != null && !serial.getEpisodes().isEmpty()) {
                System.out.println("Enter the episode to watch: ");
                int episodeNr = scanner.nextInt();
                Episode episode = serial.getEpisodes().stream().
                        filter(e -> e.getEpisodeNumber() == episodeNr).findFirst().orElse(null);

                if (episode != null) {
                    userController.watchSerial(serial, episode);
                } else {
                    System.out.println("Episode not found");
                }
            } else {
                System.out.println("Serial not found or it has no episodes");
            }
        } else {
            System.out.println("Please login first");
        }
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
