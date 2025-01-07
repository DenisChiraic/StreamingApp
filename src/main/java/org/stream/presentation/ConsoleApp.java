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

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.SQLOutput;
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

        System.out.println("--- Select Data Storage Method ---");
        System.out.println("1. In-memory");
        System.out.println("2. File");
        System.out.println("3. Database");
        System.out.print("Choose an option: ");
        int storageOption;
        try {
            storageOption = scanner.nextInt();
            scanner.nextLine();
        } catch (Exception e) {
            System.out.println("Invalid option, defaulting to Database storage.");
            storageOption = 3;
            scanner.nextLine();
        }
        String storageMethod;
        switch (storageOption) {
            case 1:
                storageMethod = "inmemory";
                break;
            case 2:
                storageMethod = "file";
                break;
            case 3:
                storageMethod = "db";
                break;
            default:
                System.out.println("Invalid option, defaulting to Database storage.");
                storageMethod = "db";
                break;
        }

        if ("inmemory".equalsIgnoreCase(storageMethod)) {
            ContentService contentService = new ContentService(storageMethod);
            populateInMemoryData(contentService);
        }

        switch (storageOption) {
            case 1:
                storageMethod = "inmemory";
                break;
            case 2:
                storageMethod = "file";
                break;
            case 3:
                storageMethod = "db";
                break;
            default:
                System.out.println("Invalid option, defaulting to Database storage.");
                storageMethod = "db";
                break;
        }

        while(true) {
            System.out.println("--- Select your role ---");
            System.out.println("1. Admin");
            System.out.println("2. Customer");
            System.out.println("0. Exit");
            System.out.print("Choose an option: ");

            int roleChoice;

            try {
                roleChoice = scanner.nextInt();
                scanner.nextLine();
            } catch (Exception e) {
                System.out.println("Invalid option. Please try again.");
                scanner.nextLine();
                continue;
            }

            if (roleChoice == 0) {
                System.out.println("Exit");
                break;
            }

            if (roleChoice == 1) {
                adminAuthenticationFlow();
            } else if (roleChoice == 2) {
                customerFlow();
            }
        }
    }

    /**
     * Fluxul de autentificare sau înregistrare pentru un utilizator de tip admin.
     */
    private void adminAuthenticationFlow() {
        boolean running = true;

        while (running) {
            System.out.println("\n--- Admin Options ---");
            System.out.println("1. Register");
            System.out.println("2. Login");
            System.out.println("0. Exit");
            System.out.print("Choose an option: ");
            int choice;
            try {
                choice = scanner.nextInt();
                scanner.nextLine();
            } catch (Exception e) {
                System.out.println("Invalid option. Please try again.");
                scanner.nextLine();
                continue;
            }

            switch (choice) {
                case 1 -> registerAdmin();
                case 2 -> {
                    if (loginAdmin()) {
                        adminMenu();
                    }
                }
                case 0 -> running = false;
            }
        }
    }

    /**
     * Permite înregistrarea unui utilizator de tip admin.
     */
    private void registerAdmin() {
        System.out.print("Enter admin username: ");
        String adminUsername = scanner.nextLine();
        System.out.print("Enter admin password: ");
        String adminPassword = scanner.nextLine();

        User adminUser = new User(adminUsername, adminPassword, new PremiumAccount(), true);
        userController.registerUser(adminUser);

        System.out.println("Admin user registered successfully!");
    }

    /**
     * Permite autentificarea unui utilizator de tip admin.
     * @return True daca autentificarea este reusita, false altfel.
     */
    private boolean loginAdmin() {
        System.out.print("Enter admin username: ");
        String adminUsername = scanner.nextLine();
        System.out.print("Enter admin password: ");
        String adminPassword = scanner.nextLine();

        boolean success = userController.login(adminUsername, adminPassword);
        if (success && userController.getCurrentUser().isAdmin()) {
            System.out.println("Admin login successful!");
            return true;
        } else {
            System.out.println("Invalid admin credentials or user is not an admin.");
            userController.logout();
            return false;
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
            System.out.println("3. Add episode to serial");
            System.out.println("4. Add User");
            System.out.println("5. Delete Movie");
            System.out.println("6. Delete Serial");
            System.out.println("7. Delete User");
            System.out.println("0. Exit");
            System.out.print("Choose an option: ");
            int choice;
            try {
                choice = scanner.nextInt();
                scanner.nextLine();
            } catch (Exception e) {
                System.out.println("Invalid option. Please try again.");
                scanner.nextLine();
                continue;
            }
            switch (choice) {
                case 1 -> addMovie();
                case 2 -> addSerial();
                case 3 -> addEpiosdesToSerial();
                case 4 -> addUser();
                case 5 -> deleteMovie();
                case 6 -> deleteSerial();
                case 7 -> deleteUser();
                case 0 -> running = false;
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

        Serial serial = new Serial(serialId, title, episodes, rating);

        userController.getContentService().addSerial(serial);
        System.out.println("Serial added successfully!");
    }

    private void addEpiosdesToSerial() {
        System.out.println("Enter serial title to add episodes: ");
        String title = scanner.nextLine();

        Serial serial = userController.getContentService().getSerialByTitle(title);

        if (serial == null) {
            System.out.println("Serial not found!");
            return;
        }
        System.out.println("Enter the number of episodes to add: ");
        int numberOfEpisodes = scanner.nextInt();
        scanner.nextLine();

        for (int i = 0; i < numberOfEpisodes; i++) {
            System.out.println("Enter episode " + (i + 1) + " title : ");
            String episodeTitle = scanner.nextLine();

            UUID episodeId = UUID.randomUUID();
            Episode episode = new Episode(serial.getId(), episodeId, title, i + 1);
        }
        System.out.println("Episodes added successfully!");
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
                int choice;
                try {
                    choice = scanner.nextInt();
                    scanner.nextLine();
                } catch (Exception e) {
                    System.out.println("Invalid option. Please try again.");
                    scanner.nextLine();
                    continue;
                }

                switch (choice) {
                    case 1 -> register();
                    case 2 -> login();
                    case 0 -> running = false;
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
        boolean running = true;
        while(running) {
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
            System.out.println("11. Logout");
            System.out.println("0. Exit");
            System.out.print("Choose an option: ");
            int choice;
            try {
                choice = scanner.nextInt();
                scanner.nextLine();
            } catch (Exception e) {
                System.out.println("Invalid option. Please try again.");
                scanner.nextLine();
                continue;
            }
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
                case 11 -> logout();
                case 0 -> running = false;
            }
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
        mainMenu();
    }

    void populateInMemoryData(ContentService contentService) {
        try {
            contentService.addMovie(new Movie(UUID.randomUUID(), "Inception", 2010, 8.8));
            contentService.addMovie(new Movie(UUID.randomUUID(), "The Dark Knight", 2008, 9.0));
            contentService.addMovie(new Movie(UUID.randomUUID(), "Interstellar", 2014, 8.6));
            contentService.addMovie(new Movie(UUID.randomUUID(), "Parasite", 2019, 8.6));
            contentService.addMovie(new Movie(UUID.randomUUID(), "Fight Club", 1999, 8.8));
            contentService.addMovie(new Movie(UUID.randomUUID(), "Forrest Gump", 1994, 8.8));
            contentService.addMovie(new Movie(UUID.randomUUID(), "The Matrix", 1999, 8.7));
            contentService.addMovie(new Movie(UUID.randomUUID(), "Pulp Fiction", 1994, 8.9));
            contentService.addMovie(new Movie(UUID.randomUUID(), "The Shawshank Redemption", 1994, 9.3));
            contentService.addMovie(new Movie(UUID.randomUUID(), "The Godfather", 1972, 9.2));
            contentService.addMovie(new Movie(UUID.randomUUID(), "The Godfather Part II", 1974, 9.0));
            contentService.addMovie(new Movie(UUID.randomUUID(), "The Lord of the Rings: The Return of the King", 2003, 9.0));
            contentService.addMovie(new Movie(UUID.randomUUID(), "The Social Network", 2010, 7.7));
            contentService.addMovie(new Movie(UUID.randomUUID(), "Joker", 2019, 8.4));
            contentService.addMovie(new Movie(UUID.randomUUID(), "Avengers: Endgame", 2019, 8.4));

            // Serial 1
            List<Episode> breakingBadEpisodes = new ArrayList<>();
            breakingBadEpisodes.add(new Episode("Pilot", 1 ));
            breakingBadEpisodes.add(new Episode("Cat's in the Bag...", 2 ));
            breakingBadEpisodes.add(new Episode("...And the Bag's in the River", 3 ));
            Serial breakingBad = new Serial("Breaking Bad", breakingBadEpisodes, 9.3);
            contentService.addSerial(breakingBad);

            List<Episode> gameOfThronesEpisodes = new ArrayList<>();
            gameOfThronesEpisodes.add(new Episode("Winter is Coming", 1 ));
            gameOfThronesEpisodes.add(new Episode("The Kingsroad", 2 ));
            gameOfThronesEpisodes.add(new Episode("Lord Snow", 3 ));
            Serial gameOfThrones = new Serial("Game of Thrones", gameOfThronesEpisodes, 9.5);
            contentService.addSerial(gameOfThrones);

            List<Episode> theOfficeEpisodes = new ArrayList<>();
            theOfficeEpisodes.add(new Episode("Pilot", 1 ));
            theOfficeEpisodes.add(new Episode("Diversity Day", 2 ));
            theOfficeEpisodes.add(new Episode("Health Care", 3 ));
            Serial theOffice = new Serial("The Office", theOfficeEpisodes, 8.7);
            contentService.addSerial(theOffice);

            List<Episode> friendsEpisodes = new ArrayList<>();
            friendsEpisodes.add(new Episode("The One Where Monica Gets a Roommate", 1 ));
            friendsEpisodes.add(new Episode("The One with the Sonogram at the End", 2 ));
            friendsEpisodes.add(new Episode("The One with the Thumb", 3 ));
            Serial friends = new Serial("Friends", friendsEpisodes, 8.3);
            contentService.addSerial(friends);

            List<Episode> strangerThingsEpisodes = new ArrayList<>();
            strangerThingsEpisodes.add(new Episode("The Vanishing of Will Byers", 1 ));
            strangerThingsEpisodes.add(new Episode("The Weirdo on Maple Street", 2 ));
            strangerThingsEpisodes.add(new Episode("Holly, Jolly", 3 ));
            Serial strangerThings = new Serial("Stranger Things", strangerThingsEpisodes, 8.9);
            contentService.addSerial(strangerThings);

            List<Episode> houseOfCardsEpisodes = new ArrayList<>();
            houseOfCardsEpisodes.add(new Episode("Chapter 1", 1 ));
            houseOfCardsEpisodes.add(new Episode("Chapter 2", 2 ));
            houseOfCardsEpisodes.add(new Episode("Chapter 3", 3 ));
            Serial houseOfCards = new Serial("House Of Cards", houseOfCardsEpisodes, 8.1);
            contentService.addSerial(houseOfCards);

            List<Episode> sherlockEpisodes = new ArrayList<>();
            sherlockEpisodes.add(new Episode("A Study in Pink", 1 ));
            sherlockEpisodes.add(new Episode("The Blind Banker", 2 ));
            sherlockEpisodes.add(new Episode("The Great Game", 3 ));
            Serial sherlock = new Serial("Sherlock", sherlockEpisodes, 8.1);
            contentService.addSerial(sherlock);

            List<Episode> theCrownEpisodes = new ArrayList<>();
            theCrownEpisodes.add(new Episode("Wolferton Splash", 1 ));
            theCrownEpisodes.add(new Episode("Hyde Park Corner", 2 ));
            theCrownEpisodes.add(new Episode("Windsor", 3 ));
            Serial theCrown = new Serial("The Crown", theCrownEpisodes, 8.8);
            contentService.addSerial(theCrown);

            List<Episode> theMandalorianEpisodes = new ArrayList<>();
            theMandalorianEpisodes.add(new Episode("The Mandalorian", 1 ));
            theMandalorianEpisodes.add(new Episode("The Child", 2 ));
            theMandalorianEpisodes.add(new Episode("The Sin", 3 ));
            Serial theMandalorian = new Serial("The Mandolarian", theMandalorianEpisodes, 9.0);
            contentService.addSerial(theMandalorian);

            List<Episode> westWorldEpisodes = new ArrayList<>();
            westWorldEpisodes.add(new Episode("The Original", 1 ));
            westWorldEpisodes.add(new Episode("Chestnut", 2 ));
            westWorldEpisodes.add(new Episode("The Stray", 3 ));
            Serial westWorld = new Serial("West Word", westWorldEpisodes, 8.0);
            contentService.addSerial(westWorld);

            List<Episode> blackMirrorEpisodes = new ArrayList<>();
            blackMirrorEpisodes.add(new Episode("Nosedive", 1 ));
            blackMirrorEpisodes.add(new Episode("Playtest", 2 ));
            blackMirrorEpisodes.add(new Episode("Shut Up and Dance", 3 ));
            Serial blackMirror = new Serial("Black Mirror", blackMirrorEpisodes, 8.3);
            contentService.addSerial(blackMirror);

            List<Episode> ozarkEpisodes = new ArrayList<>();
            ozarkEpisodes.add(new Episode("Sugarwood", 1 ));
            ozarkEpisodes.add(new Episode("Blue Cat", 2 ));
            ozarkEpisodes.add(new Episode("My Dripping Sleep", 3 ));
            Serial ozark = new Serial("Ozark", ozarkEpisodes, 9.2);
            contentService.addSerial(ozark);

            List<Episode> betterCallSaulEpisodes = new ArrayList<>();
            betterCallSaulEpisodes.add(new Episode("Uno", 1 ));
            betterCallSaulEpisodes.add(new Episode("Mijo", 2 ));
            betterCallSaulEpisodes.add(new Episode("Nacho", 3 ));
            Serial betterCallSaul = new Serial("Better Call Saul", betterCallSaulEpisodes, 8.4);
            contentService.addSerial(betterCallSaul);

            List<Episode> theBoysEpisodes = new ArrayList<>();
            theBoysEpisodes.add(new Episode("The Name of the Game", 1 ));
            theBoysEpisodes.add(new Episode("Cherry", 2 ));
            theBoysEpisodes.add(new Episode("Get Some", 3 ));
            Serial theBoys = new Serial("The Boys", theBoysEpisodes, 8.6);
            contentService.addSerial(theBoys);

            List<Episode> arcaneEpisodes = new ArrayList<>();
            arcaneEpisodes.add(new Episode("Welcome to the Playground", 1 ));
            arcaneEpisodes.add(new Episode("Some Mysteries Are Better Left Unsolved", 2 ));
            arcaneEpisodes.add(new Episode("The Base Violence Necessary for Change", 3 ));
            Serial arcane = new Serial("Arcane", arcaneEpisodes, 9.9);
            contentService.addSerial(arcane);

        } catch (RuntimeException e) {
            System.out.println("Error populating in-memory data: " + e.getMessage());
        }
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
