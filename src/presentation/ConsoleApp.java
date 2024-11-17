package presentation;

import controller.DataManager;
import controller.UserController;
import model.*;
import repository.InMemoryRepo;
import service.ContentService;
import service.UserService;
import model.TopList;

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
        UserService userService = new UserService(new InMemoryRepo<>());
        contentService = new ContentService(new InMemoryRepo<>(), new InMemoryRepo<>());
        userController = new UserController(userService, contentService);

        List<Movie> movies = DataManager.loadMovies("movies.csv");
        List<Serial> serials = DataManager.loadSerials("serials.csv");
        movies.forEach(contentService::addMovie);
        serials.forEach(contentService::addSerial);

        topList = new TopList();
        updateTopList();

    }

    /**
     * Salvează datele în fișiere la închiderea aplicației.
     */
    private void saveData() {

        if (userController.getCurrentUser() != null) {
            DataManager.saveHistoryList(userController.getCurrentUser().getHistoryList(), "history.csv");
            DataManager.saveWatchList(userController.getCurrentUser().getWatchList(), "watch.csv");
        }
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
            System.out.println("\n1. Register\n2. LogIn\n3. View Movies and Serials\n4. Add to WatchList\n5. View WatchList\n6. Remove from WatchList\n7. Watch Movie\n8. Watch Serial\n9. View History\n10. View Top 10 Movies\n11. View Top 10 Serials\n12. LogOut\n0. Exit");
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
                case 10 -> viewTopMovies();
                case 11 -> viewTopSerials();
                case 12 -> logout();
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
