package org.stream.presentation;

import org.stream.config.DBConfig;
import org.stream.controller.UserController;
import org.stream.model.*;
import org.stream.model.mappers.MovieMapper;
import org.stream.model.mappers.SerialMapper;
import org.stream.repository.DatabaseRepo;
import org.stream.service.ContentService;
import org.stream.service.SessionService;
import org.stream.service.UserService;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.UUID;

/**
 * Clasa ConsoleApp reprezintă interfața de linie de comandă a aplicației,
 * permițând utilizatorului să interacționeze cu funcționalitățile principale ale aplicației de streaming.
 * Aceasta include gestionarea utilizatorilor, vizualizarea și adăugarea de filme și seriale,
 * precum și gestionarea sesiunilor de utilizator.
 */
public class ConsoleApp {
    private final UserController userController;
    private final Scanner scanner;

    /**
     * Constructor pentru ConsoleApp, care inițializează serviciile și controalele necesare.
     * Creează o conexiune la baza de date și instanțiază serviciile pentru gestionarea utilizatorilor și conținutului.
     */
    public ConsoleApp() {
        this.scanner = new Scanner(System.in);

        Connection connection;
        try {
            connection = DriverManager.getConnection(DBConfig.DB_URL, DBConfig.DB_USER, DBConfig.DB_PASSWORD);
        } catch (SQLException e) {
            throw new RuntimeException("Nu s-a putut inițializa conexiunea la baza de date.", e);
        }

        UserService userService = new UserService();
        ContentService contentService = new ContentService(
                new DatabaseRepo<>("movies", new MovieMapper(connection)),
                new DatabaseRepo<>("serials", new SerialMapper(connection))
        );
        SessionService sessionService = new SessionService();

        this.userController = new UserController(userService, contentService, sessionService);
    }

    /**
     * Punctul de intrare al aplicației, care permite utilizatorului să selecteze rolul și să înceapă interacțiunea.
     */
    public void start() {
        System.out.println("Select your role:");
        System.out.println("1. Admin");
        System.out.println("2. Customer");
        System.out.print("Choose an option: ");
        int roleChoice = scanner.nextInt();
        scanner.nextLine();

        if (roleChoice == 1) {
            System.out.print("Enter admin username: ");
            String adminUsername = scanner.nextLine();
            System.out.print("Enter admin password: ");
            String adminPassword = scanner.nextLine();

            User adminUser = new User(adminUsername, adminPassword, new PremiumAccount(), true);
            userController.registerUser(adminUser);

            System.out.println("Admin user created successfully!");
            adminMenu();
        } else if (roleChoice == 2) {
            customerFlow();
        } else {
            System.out.println("Invalid choice!");
        }
    }

    /**
     * Meniu specific pentru administratori, care permite adăugarea, ștergerea și gestionarea utilizatorilor și conținutului.
     */
    private void adminMenu() {
        boolean running = true;

        while (running) {
            System.out.println("\nAdmin Menu:");
            System.out.println("1. Add Movie");
            System.out.println("2. Add Serial");
            System.out.println("3. Add User");
            System.out.println("4. Delete Movie");
            System.out.println("5. Delete Serial");
            System.out.println("6. Delete User");
            System.out.println("0. Exit");
            System.out.print("Choose an option: ");
            int choice = scanner.nextInt();
            scanner.nextLine();

            switch (choice) {
                case 1 -> addMovie();
                case 2 -> addSerial();
                case 3 -> addUser();
                case 4 -> deleteMovie();
                case 5 -> deleteSerial();
                case 6 -> deleteUser();
                case 0 -> running = false;
                default -> System.out.println("Invalid choice");
            }
        }
    }

    /**
     * Permite adăugarea unui film în baza de date.
     */
    private void addMovie() {
        System.out.println("Enter movie title: ");
        String title = scanner.nextLine();

        System.out.println("Enter movie duration in minutes: ");
        int duration = scanner.nextInt();

        System.out.println("Enter movie rating: ");
        double rating = scanner.nextDouble();

        Movie movie = new Movie(UUID.randomUUID(),title, duration, rating);

        userController.getContentService().addMovie(movie);
        System.out.println("Movie added successfully!");
    }

    /**
     * Permite adăugarea unui serial în baza de date.
     */
    private void addSerial() {
        System.out.println("Enter serial title: ");
        String title = scanner.nextLine();

        System.out.println("Enter serial rating: ");
        double rating = scanner.nextDouble();

        UUID serialId = UUID.randomUUID();

        List<Episode> episodes = new ArrayList<>();
        System.out.println("Enter number of episodes: ");
        int numberOfEpisodes = scanner.nextInt();
        for (int i = 0; i < numberOfEpisodes; i++) {
            System.out.println("Enter episode " + i + " name: ");
            String name = scanner.nextLine();

            episodes.add(new Episode(serialId, UUID.randomUUID(), name, i));
        }

        Serial serial = new Serial(serialId, title, episodes, rating);

        userController.getContentService().addSerial(serial);
        System.out.println("Serial added successfully!");
    }

    /**
     * Permite adăugarea unui utilizator în baza de date.
     */
    private void addUser() {
        System.out.print("Enter Username: ");
        String username = scanner.nextLine();
        System.out.print("Enter Password: ");
        String password = scanner.nextLine();

        System.out.println("Choose account type:\n1. Free Account\n2. Premium Account");
        int accountType = scanner.nextInt();
        scanner.nextLine();

        Account account = accountType == 1 ? new FreeAccount() : new PremiumAccount();
        User newUser = new User(username, password, account, false);
        userController.registerUser(newUser);
        System.out.println("User added successfully!");
    }

    /**
     * Permite ștergerea unui film din baza de date.
     */
    private void deleteMovie() {
        System.out.print("Enter movie title to delete: ");
        String title = scanner.nextLine();
        userController.getContentService().removeMovie(title);
        System.out.println("Movie deleted successfully!");
    }

    /**
     * Permite ștergerea unui serial din baza de date.
     */
    private void deleteSerial() {
        System.out.print("Enter serial title to delete: ");
        String title = scanner.nextLine();
        userController.getContentService().removeSerial(title);
        System.out.println("Serial deleted successfully!");
    }

    /**
     * Permite ștergerea unui utilizator din sistem.
     */
    private void deleteUser() {
        System.out.print("Enter username to delete: ");
        String username = scanner.nextLine();
        userController.getUserService().deleteUser(username);
        System.out.println("User deleted successfully!");
    }

    /**
     * Fluxul de interacțiune pentru utilizatorii clienți, inclusiv înregistrarea și autentificarea.
     */
    private void customerFlow() {
        boolean running = true;

        while (running) {
            if (!userController.getSessionService().isUserLoggedIn()) {
                System.out.println("\n1. Register");
                System.out.println("2. Login");
                System.out.println("0. Exit");
                System.out.print("Choose an option: ");
                int choice = scanner.nextInt();
                scanner.nextLine();

                switch (choice) {
                    case 1 -> register();
                    case 2 -> login();
                    case 0 -> running = false;
                    default -> System.out.println("Invalid choice");
                }
            } else {
                mainMenu();
            }
        }
    }

    /**
     * Permite înregistrarea unui utilizator nou.
     */
    private void register() {
        System.out.print("Enter Username: ");
        String username = scanner.nextLine();
        System.out.print("Enter Password: ");
        String password = scanner.nextLine();

        System.out.println("Choose account type:\n1. Free Account\n2. Premium Account");
        int accountType = scanner.nextInt();
        scanner.nextLine();

        Account account = accountType == 1 ? new FreeAccount() : new PremiumAccount();
        User newUser = new User(username, password, account, false);

        userController.registerUser(newUser);

        System.out.println("User registered successfully!");
    }

    /**
     * Permite autentificarea unui utilizator.
     */
    private void login() {
        System.out.print("Enter Username: ");
        String username = scanner.nextLine();
        System.out.print("Enter Password: ");
        String password = scanner.nextLine();

        boolean success = userController.login(username, password);
        if (success) {
            System.out.println("Login successful!");
        } else {
            System.out.println("Invalid username or password.");
        }
    }

    /**
     * Meniu principal pentru utilizatorii autentificați, permițându-le acestora să interacționeze cu filmele și serialele.
     */
    private void mainMenu() {
        System.out.println("\nMain Menu:");
        System.out.println("1. View Movies");
        System.out.println("2. View Serials");
        System.out.println("3. View Top 10 Movies");
        System.out.println("4. View Top 10 Serials");
        System.out.println("5. Add to WatchList");
        System.out.println("6. Remove from WatchList");
        System.out.println("7. Watch Movie");
        System.out.println("8. Watch Serial");
        System.out.println("9. View WatchList");
        System.out.println("10. View History ");
        System.out.println("0. Logout");
        System.out.print("Choose an option: ");
        int choice = scanner.nextInt();
        scanner.nextLine();

        switch (choice) {
            case 1 -> viewMovies();
            case 2 -> viewSerials();
            case 3 -> viewTop10Movies();
            case 4 -> viewTop10Serials();
            case 5 -> addToWatchList();
            case 6 -> removeFromWatchList();
            case 7 -> watchMovie();
            case 8 -> watchSerial();
            case 9 -> viewWatchList();
            case 10 -> viewHistoryList();
            case 0 -> logout();
            default -> System.out.println("Invalid choice");
        }
    }

    /**
     * Vizualizează lista celor mai populare 10 filme.
     */
    private void viewTop10Movies() {
        List<Movie> topMovies = userController.getContentService().getTop10Movies();
        System.out.println("\nTop 10 Movies:");
        topMovies.forEach(movie -> System.out.println("- " + movie.getTitle()));
    }

    /**
     * Vizualizează lista celor mai populare 10 seriale.
     */
    private void viewTop10Serials() {
        List<Serial> topSerials = userController.getContentService().getTop10Serials();
        System.out.println("\nTop 10 Serials:");
        topSerials.forEach(serial -> System.out.println("- " + serial.getTitle()));
    }

    /**
     * Vizualizează toate filmele disponibile în aplicație.
     */
    private void viewMovies() {
        List<Movie> movies = userController.getContentService().getAllMovies();
        movies.forEach(movie -> System.out.println("- " + movie.getTitle()));
    }

    /**
     * Vizualizează toate serialele disponibile în aplicație.
     */
    private void viewSerials() {
        List<Serial> serials = userController.getContentService().getAllSerials();
        serials.forEach(serial -> System.out.println("- " + serial.getTitle()));
    }

    /**
     * Adaugă un film sau serial la lista de vizionare.
     */
    private void addToWatchList() {
        System.out.print("Enter the title of the content to add: ");
        String title = scanner.nextLine();

        if (title != null && !title.isBlank()) {
            Movie movie = userController.getContentService().getMovieByTitle(title);
            if (movie != null) {
                userController.addToWatchList(movie);
                return;
            }

            Serial serial = userController.getContentService().getSerialByTitle(title);
            if (serial != null) {
                userController.addToWatchList(serial);
                return;
            }

            System.out.println("Content not found!");
        } else {
            System.out.println("Invalid title!");
        }
    }

    /**
     * Șterge un film sau serial din lista de vizionare.
     */
    private void removeFromWatchList() {
        System.out.print("Enter the title of the content to remove: ");
        String title = scanner.nextLine();

        if (title != null && !title.isBlank()) {
            Movie movie = userController.getContentService().getMovieByTitle(title);
            if (movie != null) {
                userController.removeFromWatchList(movie);
                return;
            }

            Serial serial = userController.getContentService().getSerialByTitle(title);
            if (serial != null) {
                userController.removeFromWatchList(serial);
                return;
            }

            System.out.println("Content not found!");
        } else {
            System.out.println("Invalid title!");
        }
    }

    /**
     * Permite vizionarea unui film din aplicație.
     */
    private void watchMovie() {
        System.out.println("Enter the title of the movie you want to watch: ");
        String title = scanner.nextLine();
        Movie movie = userController.getContentService().getMovieByTitle(title);
        if (movie != null) {
            userController.watchMovie(movie);
        } else {
            System.out.println("Movie not found!");
        }
    }

    /**
     * Permite vizionarea unui serial din aplicație.
     */
    private void watchSerial() {
        System.out.println("Enter the title of the serial you want to watch: ");
        String title = scanner.nextLine();
        System.out.println("Enter the episode number you want to watch: ");
        int episodeNumber = scanner.nextInt();
        scanner.nextLine();

        Serial serial = userController.getContentService().getSerialByTitle(title);
        if (serial != null) {
            if (episodeNumber < serial.getEpisodes().size()) {
                Episode currentEpisode = serial.getEpisodes().get(episodeNumber);
                boolean watching = true;

                while (watching && currentEpisode != null) {
                    userController.watchSerial(serial, currentEpisode);
                    System.out.println("You watched Episode " + currentEpisode.getEpisodeNumber());

                    System.out.println("Do you want to watch the next episode? Type YES to continue or NO to stop:");
                    String choice = scanner.nextLine();

                    if (choice.equalsIgnoreCase("yes")) {
                        currentEpisode = userController.getContentService().getNextEpisode(serial, currentEpisode);
                        if (currentEpisode == null) {
                            System.out.println("No more episodes available in this serial.");
                            watching = false;
                        }
                    } else {
                        watching = false;
                    }
                }
            } else {
                System.out.println("Episode number is invalid or does not exist.");
            }
        } else {
            System.out.println("Serial not found.");
        }
    }

    /**
     * Vizualizează lista de conținuturi din lista de vizionare a utilizatorului.
     */
    private void viewWatchList() {
        userController.displayWatchList();
    }

    /**
     * Vizualizează istoricul de vizionare al utilizatorului.
     */
    private void viewHistoryList() {
        userController.displayHistoryList();
    }

    /**
     * Închide sesiunea utilizatorului.
     */
    private void logout() {
        userController.logout();
    }

    /**
     * Punctul de intrare al aplicației, care pornește aplicația ConsoleApp.
     * @param args Argumente pentru aplicație (neutilizate).
     */
    public static void main(String[] args) {
        ConsoleApp app = new ConsoleApp();
        app.start();
    }
}
