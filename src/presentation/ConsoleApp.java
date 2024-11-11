package presentation;

import controller.UserController;
import model.*;
import repository.InMemoryRepo;
import service.ContentService;
import service.UserService;

import java.util.Arrays;
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
    private ContentService contentService;
    private Scanner scanner;

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
        UserService userService = new UserService(new InMemoryRepo<>());
        contentService = new ContentService(new InMemoryRepo<>(), new InMemoryRepo<>());
        userController = new UserController(userService, contentService);


        // Adăugăm exemple de filme
        contentService.addMovie(new Movie("X Man", 124, 4.3));
        contentService.addMovie(new Movie("Titanic", 186, 4.8));
        contentService.addMovie(new Movie("Lion King", 90, 4.1));

        // Adăugăm exemple de seriale cu episoade
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

    /**
     * Punctul de intrare al aplicației, care afișează meniul principal și gestionează opțiunile utilizatorului.
     */
    public void start() {
        boolean running = true;
        while (running) {
            System.out.println("\n1. Register\n2. LogIn\n3. View Movies and Serials\n4. Add to WatchList\n5. View WatchList\n6. Remove from WatchList\n7. Watch Movie\n8. Watch Serial\n9. View History\n10. LogOut\n0. Exit");
            System.out.println("Choose an option: ");
            int choice = scanner.nextInt();
            scanner.nextLine();

            switch (choice) {
                case 1 -> register();
                case 2 -> login();
                case 3 -> viewMoviesAndSerials();
                case 4 ->addToWatchList();
                case 5 -> viewWatchList();
                case 6 -> removeFromWatchList();
                case 7 -> watchMovie();
                case 8 -> watchEpisode();
                case 9 -> viewHistoryList();
                case 10 -> logout();
                case 0 -> running = false;
                default -> System.out.println("Invalid choice");
            }
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
        User newUser = new User(username, password, account);
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
        userController.login(username, password);
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

    /**
     * Afișează lista de filme sau seriale pe care utilizatorul doreste sa le vada.
     */
    private void viewWatchList() {
        if (userController.getCurrentUser() != null) {
            userController.displayWatchList();
        } else {
            System.out.println("Please login first");
        }
    }

    /**
     * Afișează istoricul vizionărilor utilizatorului curent.
     */
    private void viewHistoryList() {
        if (userController.getCurrentUser() != null) {
            userController.displayHistoryList();
        } else {
            System.out.println("Please login first");
        }
    }

    /**
     * Permite utilizatorului să se deconecteze din sistem.
     */
    private void logout() {
        userController.logout();
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
