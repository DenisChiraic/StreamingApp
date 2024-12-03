package presentation;

import controller.DataManager;
import controller.UserController;
import model.*;
import repository.InMemoryRepo;
import service.ContentService;
import service.UserService;
import model.TopList;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 * Clasa ConsoleApp reprezintă interfața de linie de comandă a aplicației,
 * permițând utilizatorului să interacționeze cu funcționalitățile principale,
 * inclusiv înregistrare, autentificare, vizionare de conținut, gestionarea
 * listei de vizionare și vizualizarea istoricului.
 */
public class ConsoleApp {
    private UserController userController;
    private UserService userService;
    private ContentService contentService;
    private Scanner scanner;
    private TopList topList;

    /**
     * Constructor pentru ConsoleApp, care inițializează scannerul și serviciile.
     */
    public ConsoleApp() {
        this.scanner = new Scanner(System.in);
        initServices();
    }

    /**
     * Inițializează serviciile de utilizator și conținut, precum și repository-urile necesare.
     */
    private void initServices() {
        userService = new UserService(new InMemoryRepo<>());
        contentService = new ContentService(new InMemoryRepo<>(), new InMemoryRepo<>());
        userController = new UserController(userService, contentService);

        DataManager.initializeContentService(new InMemoryRepo<>(), new InMemoryRepo<>());

        List<User> users = DataManager.loadUsers("users.csv");
        users.forEach(userController::registerUser);

        List<Movie> movies = DataManager.loadMovies("movies.csv");
        List<Serial> serials = DataManager.loadSerials("serials.csv");
        movies.forEach(contentService::addMovie);
        serials.forEach(contentService::addSerial);

        topList = new TopList();
        updateTopList();
    }

    /**
     * Salvează datele în fișiere partajate la închiderea aplicației.
     */
    private void saveData() {
        if (userController.getCurrentUser() != null) {
            String username = userController.getCurrentUser().getUsername();

            DataManager.saveWatchList(
                    userController.getCurrentUser().getWatchList(),
                    "watchlist.csv",
                    username);

            DataManager.saveHistoryList(
                    userController.getCurrentUser().getHistoryList(),
                    "historylist.csv",
                    username);
        }
        DataManager.saveUsers(userService.getAllUsers(), "users.csv");
    }


    /**
     * Actualizează listele de topuri pentru filme și seriale.
     */
    private void updateTopList() {
        topList.updateTopMovies(contentService.getAllMovies());
        topList.updateTopSerials(contentService.getAllSerials());
    }

    /**
     * Punctul de intrare al aplicației, care afișează meniul principal și gestionează opțiunile utilizatorului.
     */
    public void start() {
        Runtime.getRuntime().addShutdownHook(new Thread(this::saveData)); // salveaza datele la inchidere

        boolean running = true;
        while (running) {
            if (userController.getCurrentUser() == null) {
                System.out.println("\n1. Register");
                System.out.println("2. Login");
                System.out.println("0. Exit");
                System.out.println("Choose an option: ");
                int choice = scanner.nextInt();
                scanner.nextLine();

                switch (choice) {
                    case 1 -> register();
                    case 2 -> login();
                    case 0 -> running = false;
                    default -> System.out.println("Invalid choice");
                }
            } else if (userController.getCurrentUser().isAdmin()) {
                adminMenu();
            } else {
                customerMenu();
            }
        }
    }
    private void customerMenu() {
        System.out.println("\nCustomer Menu:" +
                "\n1. View available Movies and Serial\n2. Add to WatchList\n3.Remove from WatchList\n4.Watch a Movie\n5.Watch a Serial\n6. View HistoryList\n7. View WatchList\n8. View TopMovies\n9. TopSerials\n10. Exit");

        int choice = scanner.nextInt();
        scanner.nextLine();

        switch (choice) {
            case 1 -> viewMoviesAndSerials();
            case 2 -> addToWatchList();
            case 3 -> removeFromWatchList();
            case 4 -> watchMovie();
            case 5 -> watchEpisode();
            case 6 -> viewHistoryList();
            case 7 -> viewWatchList();
            case 8 -> viewTopMovies();
            case 9 -> viewTopSerials();
            case 10 -> logout();
            default -> System.out.println("Invalid choice");
        }

    }

    private void adminMenu() {
        System.out.println("\nAdmin Menu:");
        System.out.println("1. Delete User");
        System.out.println("2. Add Movie");
        System.out.println("3. Add Serial");
        System.out.println("4. Remove Movie");
        System.out.println("5. Remove Serial");
        System.out.println("6. Exit");

        int choice = scanner.nextInt();
        scanner.nextLine();

        switch (choice) {
            case 1 -> deleteUser();
            case 2 -> addMovie();
            case 3 -> addSerial();
            case 4 -> removeMovie();
            case 5 -> removeSerial();
            case 6 -> logout();
            default -> System.out.println("Invalid choice");
        }
    }

    /**
     * Permite utilizatorului să se înregistreze în sistem.
     */
    private void register() {
        System.out.println("Enter Username: ");
        String username = scanner.nextLine();
        System.out.println("Enter Password: ");
        String password = scanner.nextLine();

        if (userService.userExists(username)) {
            System.out.println("An Account with this name already exists! Please log in.");
            return;
        }

        System.out.println("Choose the type of the user:");
        System.out.println("\n1. Customer\n2 Admin");
        int userType = scanner.nextInt();
        scanner.nextLine();

        boolean isAdmin = userType == 2;

        System.out.println("Choose account type:\n1. Free Account\n2. Premium Account");
        int accountType = scanner.nextInt();
        scanner.nextLine();

        Account account;
        if (accountType == 1) {
            account = new FreeAccount();
        } else if (accountType == 2) {
            account = new PremiumAccount();
        } else {
            System.out.println("Invalid account type");
            return;
        }
        // Creăm un nou utilizator și îl adăugăm în UserService
        User newUser = new User(username, password, account, isAdmin);
        userController.registerUser(newUser);

        System.out.println("Registration successful");
    }

    /**
     * Permite utilizatorului să se autentifice în sistem.
     */
    private void login() {
        System.out.println("Username: ");
        String username = scanner.nextLine();
        System.out.println("Password: ");
        String password = scanner.nextLine();

        if (userController.login(username, password)) {
            User currentUser = userController.getCurrentUser();
            currentUser.setWatchList(DataManager.loadWatchList("watchlist.csv", username));
            currentUser.setHistoryList(DataManager.loadHistoryList("historylist.csv", username));
        }
    }


    private void deleteUser() {
        System.out.println("Enter Username: ");
        String username = scanner.nextLine();

        boolean success = userController.deleteUser(username);
        if (success) {
            List<User> users = userService.getUserRepo().findAll();
            DataManager.saveUsers(users, "users.csv");

            System.out.println("User deleted successfully");
        } else {
            System.out.println("User not found");
        }
    }


    private void addMovie() {
        System.out.println("Enter Title: ");
        String title = scanner.nextLine();
        System.out.println("Enter Movie Duration: ");
        int duration = scanner.nextInt();
        System.out.println("Enter Rating: ");
        double rating = scanner.nextDouble();
        scanner.nextLine();

        Movie movie = new Movie(title, duration, rating);
        contentService.addMovie(movie);

        // Salvăm lista actualizată de filme în fișier
        List<Movie> movies = contentService.getAllMovies();
        DataManager.saveMovies(movies, "movies.csv");

        System.out.println("Movie added successfully");
    }


    private void removeMovie() {
        System.out.println("Enter The Movie To Remove: ");
        String name = scanner.nextLine();

        boolean success = contentService.removeMovie(name);
        if (success) {
            List<Movie> movies = contentService.getAllMovies();
            DataManager.saveMovies(movies, "movies.csv");

            System.out.println("Movie removed successfully");
        } else {
            System.out.println("Movie not found");
        }
    }


    private void addSerial() {
        System.out.println("Enter The Serial To Add: ");
        String serialTitle = scanner.nextLine();
        System.out.println("Enter Serial Rating: ");
        double rating = scanner.nextDouble();
        scanner.nextLine();

        List<Episode> episodes = new ArrayList<>();
        boolean adding = true;
        while (adding) {
            System.out.println("Enter the name of the episode: ");
            String episodeName = scanner.nextLine();
            System.out.println("Enter the episode number: ");
            int episodeNumber = scanner.nextInt();
            scanner.nextLine();

            episodes.add(new Episode(episodeName, episodeNumber));
            System.out.println("Do you want to add another episode (yes/no): ");
            String response = scanner.nextLine();
            adding = response.equalsIgnoreCase("yes");
        }

        Serial serial = new Serial(serialTitle, episodes, rating);
        contentService.addSerial(serial);

        List<Serial> serials = contentService.getAllSerials();
        DataManager.saveSerials(serials, "serials.csv");

        System.out.println("Serial added successfully");
    }


    private void removeSerial() {
        System.out.println("Enter the name of the Serial: ");
        String title = scanner.nextLine();

        boolean success = contentService.removeSerial(title);
        if (success) {
            // Salvăm lista actualizată de seriale în fișier
            List<Serial> serials = contentService.getAllSerials();
            DataManager.saveSerials(serials, "serials.csv");

            System.out.println("Serial removed successfully");
        } else {
            System.out.println("Serial not found");
        }
    }


    /**
     * Afișează toate filmele și serialele disponibile.
     */
    private void viewMoviesAndSerials() {
        if (userController.getCurrentUser() != null) {
            System.out.println("Available Movies:");
            contentService.getAllMovies().forEach(movie -> System.out.println("- " + movie.getTitle()));
            System.out.println("Available Serials:");
            contentService.getAllSerials().forEach(serial -> System.out.println("- " + serial.getTitle()));
        } else {
            System.out.println("Please login first");
        }
    }

    /**
     * Permite utilizatorului să adauge un film sau serial în lista de vizionare.
     */
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
                    userController.addToWatchList(serial);
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

    /**
     * Permite utilizatorului să elimine un film sau serial din lista de vizionare.
     */
    private void removeFromWatchList() {
        if (userController.getCurrentUser() != null) {
            System.out.println("1. Remove movie / 2. Remove serial");
            int option = scanner.nextInt();
            scanner.nextLine();
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

    /**
     * Permite utilizatorului să vizioneze un film și îl adaugă în istoricul vizionărilor.
     */
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

    /**
     * Permite utilizatorului să vizioneze un episod dintr-un serial și îl adaugă în istoricul vizionărilor.
     */
    private void watchEpisode() {
        if (userController.getCurrentUser() != null) {
            System.out.println("Enter the name of the serial to watch: ");
            String serialTitle = scanner.nextLine();
            Serial serial = contentService.getAllSerials().stream().
                    filter(s -> s.getTitle().equals(serialTitle)).findFirst().orElse(null);

            if (serial != null && !serial.getEpisodes().isEmpty()) {
                System.out.println("Enter the episode to watch: ");
                int episodeNr = scanner.nextInt();
                scanner.nextLine();

                Episode currentEpisode = serial.getEpisodes().stream().
                        filter(e -> e.getEpisodeNumber() == episodeNr).findFirst().orElse(null);

                if (currentEpisode != null) {
                    boolean continueWatching = true;

                    while (currentEpisode != null && continueWatching) {
                        userController.watchSerial(serial, currentEpisode);

                        System.out.println("Do you want to watch the next episode? Press 1 for Yes, any other key to exit");
                        String choice = scanner.nextLine();

                        if (choice.equals("1")) {
                            currentEpisode = serial.nextEpisode(currentEpisode);
                            if (currentEpisode == null) {
                                System.out.println("No more episodes available");
                            }
                        } else {
                            continueWatching = false;
                        }
                    }

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

    /**
     * Afișează lista de filme sau seriale pe care utilizatorul doreste sa le vada.
     */
    private void viewWatchList() {
        userController.displayWatchList();
    }

    /**
     * Afișează istoricul vizionărilor utilizatorului curent.
     */
    private void viewHistoryList() {
        userController.displayHistoryList();
    }

    /**
     * Afiseaza top 10 filme.
     */
    private void viewTopMovies() {
        if (userController.getCurrentUser() != null) {
            topList.displayTopMovies();
        } else {
            System.out.println("Please login first");
        }
    }

    /**
     * Afiseaza top 10 seriale.
     */
    private void viewTopSerials() {
        if (userController.getCurrentUser() != null) {
            topList.displayTopSerials();
        } else {
            System.out.println("Please login first");
        }
    }

    /**
     * Permite utilizatorului să se deconecteze din sistem.
     */
    private void logout() {
        if (userController.getCurrentUser() != null) {
            String username = userController.getCurrentUser().getUsername();
            userController.logout();
        } else {
            System.out.println("No user is currently logged in");
        }
    }


    /**
     * Metoda principală care pornește aplicația.
     *
     * @param args Argumentele liniei de comandă.
     */
    public static void main(String[] args) {
        ConsoleApp app = new ConsoleApp();
        app.start();
    }
}
