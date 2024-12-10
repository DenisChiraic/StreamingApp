package org.stream.controller;

import org.stream.model.*;
import org.stream.model.Serial;
import org.stream.service.ContentService;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Clasa DataManager gestionează încărcarea și salvarea datelor din/în fișiere CSV.
 */
public class DataManager {
    private static ContentService contentService;

//    public static void initializeContentService(IRepo<Movie> movieRepo, IRepo<Serial> serialRepo) {
//        contentService = new ContentService(movieRepo, serialRepo);
//    }

    /**
     * Salvează o listă de filme într-un fișier CSV.
     *
     * @param movies   Lista de filme de salvat.
     * @param fileName Calea către fișierul CSV unde datele vor fi salvate.
     */
    public static void saveMovies(List<Movie> movies, String fileName) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(fileName))) {
            writer.write("ID,Title,Duration,Rating");
            writer.newLine();

            for (Movie movie : movies) {
                String line = String.format("%s,%s,%d,%.1f",
                        movie.getId(),
                        movie.getTitle(),
                        movie.getDuration(),
                        movie.getRating());
                writer.write(line);
                writer.newLine();
            }
        } catch (IOException e) {
            System.out.println("Error saving movies: " + e.getMessage());
        }
    }

    /**
     * Încarcă o listă de filme dintr-un fișier CSV.
     *
     * @param fileName Calea către fișierul CSV de unde datele vor fi încărcate.
     * @return O listă de filme încărcate din fișierul CSV.
     */
    public static List<Movie> loadMovies(String fileName) {
        List<Movie> movies = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(fileName))) {
            String line = reader.readLine(); // Citim header-ul
            while ((line = reader.readLine()) != null) {
                String[] fields = line.split(",");
                String id = fields[0];
                String title = fields[1];
                int duration = Integer.parseInt(fields[2]);
                double rating = Double.parseDouble(fields[3]);
                movies.add(new Movie(title, duration, rating));
            }
        } catch (IOException e) {
            System.out.println("Error loading movies: " + e.getMessage());
        }
        return movies;
    }

    /**
     * Salvează o listă de seriale într-un fișier CSV.
     * @param serials Lista de seriale de salvat.
     * @param fileName Calea către fișierul CSV unde datele vor fi salvate.
     */
    public static void saveSerials(List<Serial> serials, String fileName) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(fileName))) {
            writer.write("ID,Title,Rating,Episodes");
            writer.newLine();

            for (Serial serial : serials) {
                String episodes = serial.getEpisodes().stream()
                        .map(e -> e.getEpisodeName() + "," + e.getEpisodeNumber())
                        .reduce((e1, e2) -> e1 + ";" + e2)
                        .orElse("");
                String line = String.format("%s,%s,%.1f,%s",
                        serial.getId(),
                        serial.getTitle(),
                        serial.getRating(),
                        episodes);
                writer.write(line);
                writer.newLine();
            }
        } catch (IOException e) {
            System.out.println("Error saving serials: " + e.getMessage());
        }
    }

    /**
     * Încarcă o listă de seriale dintr-un fișier CSV.
     * @param fileName Calea către fișierul CSV de unde datele vor fi încărcate.
     * @return O listă de seriale încărcate din fișierul CSV.
     */
    public static List<Serial> loadSerials(String fileName) {
        List<Serial> serials = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(fileName))) {
            String line = reader.readLine();
            while ((line = reader.readLine()) != null) {
                String[] fields = line.split(",", -1);
                String serialId = fields[0];
                String serialTitle = fields[1];
                double rating = Double.parseDouble(fields[2]);
                String episodesData = fields[3].replace("\"", "");

                List<Episode> episodes = new ArrayList<>();
                for (String episodeData : episodesData.split(";")) {
                    String[] parts = episodeData.split(":");
                    if (parts.length == 2) {
                        String episodeName = parts[0].trim();
                        int episodeNumber = Integer.parseInt(parts[1].trim());
                        episodes.add(new Episode(episodeName, episodeNumber));
                    }
                }
                serials.add(new Serial(serialTitle, episodes, rating));
            }
        } catch (IOException e) {
            System.out.println("Error loading serials: " + e.getMessage());
        }
        return serials;
    }

    /**
     * Salvează istoricul vizionărilor pentru utilizatorul curent în fișierul CSV,
     * inclusiv toate detaliile despre elementele din istoric.
     *
     * @param historyList Obiectul HistoryList de salvat.
     * @param fileName    Numele fișierului CSV.
     * @param username    Numele utilizatorului curent.
     */
    public static void saveHistoryList(HistoryList historyList, String fileName, String username) {
        try {
            File file = new File(fileName);
            List<String> lines = new ArrayList<>();

            boolean isUserSection = false;
            boolean userSectionFound = false;

            if (file.exists()) {
                try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
                    String line;
                    while ((line = reader.readLine()) != null) {
                        if (line.startsWith("User:" + username)) {
                            isUserSection = true;
                            userSectionFound = true;
                        }

                        if (isUserSection) {
                            if (line.startsWith("User:") && !line.equals("User:" + username)) {
                                isUserSection = false;  // Ieșim din secțiunea utilizatorului
                            }
                            lines.add(line);
                        } else {
                            lines.add(line);
                        }
                    }
                }
            }

            if (!userSectionFound) {
                lines.add("User:" + username);
            }

            for (HistoryItem item : historyList.getHistory()) {
                lines.add(String.format("%s,%s,%s",
                        item.getTitle(),
                        item.getType(),
                        item.getViewedDate()));
            }

            try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
                for (String line : lines) {
                    writer.write(line);
                    writer.newLine();
                }
            }
        } catch (IOException e) {
            System.out.println("Error saving history list: " + e.getMessage());
        }
    }

    /**
     * Încarcă istoricul vizionărilor pentru utilizatorul curent din fișierul CSV,
     * completând toate detaliile direct din fișier.
     *
     * @param fileName Numele fișierului CSV.
     * @param username Numele utilizatorului curent.
     * @return Obiectul HistoryList reconstruit pentru utilizatorul curent.
     */
    public static HistoryList loadHistoryList(String fileName, String username) {
        HistoryList historyList = new HistoryList();
        try (BufferedReader reader = new BufferedReader(new FileReader(fileName))) {
            String line;
            boolean isUserSection = false;

            while ((line = reader.readLine()) != null) {
                if (line.startsWith("User:" + username)) {
                    isUserSection = true;
                    continue;
                }

                if (line.startsWith("User:") && isUserSection) {
                    isUserSection = false;
                }

                if (isUserSection) {
                    String[] fields = line.split(",", -1);
                    if (fields.length == 3) {
                        String title = fields[0];
                        String type = fields[1];
                        String viewedDate = fields[2];
                        historyList.addContent(title, type);
                    }
                }
            }
        } catch (IOException e) {
            System.out.println("Error loading history list: " + e.getMessage());
        }
        return historyList;
    }

    /**
     * Salvează lista de utilizatori într-un fișier CSV.
     *
     * @param users    Lista de utilizatori de salvat.
     * @param fileName Calea către fișierul CSV.
     */
    public static void saveUsers(List<User> users, String fileName) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(fileName))) {
            writer.write("Username,Password,AccountType,Role");
            writer.newLine();

            for (User user : users) {
                String accountType = user.getAccount() instanceof PremiumAccount ? "Premium" : "Free";
                String role = user.isAdmin() ? "Admin" : "Customer";
                writer.write(String.format("%s,%s,%s,%s",
                        user.getUsername(),
                        user.getPassword(),
                        accountType,
                        role));
                writer.newLine();
            }
        } catch (IOException e) {
            System.out.println("Error saving users: " + e.getMessage());
        }
    }

    /**
     * Încarcă utilizatorii dintr-un fișier CSV, incluzând tipul de cont și rolul.
     *
     * @param fileName Numele fișierului CSV.
     * @return Lista de utilizatori încărcată.
     */
    public static List<User> loadUsers(String fileName) {
        List<User> users = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(fileName))) {
            String line = reader.readLine(); // Sărim peste header
            while ((line = reader.readLine()) != null) {
                String[] fields = line.split(",", -1);
                String username = fields[0];
                String password = fields[1];
                String accountType = fields[2];
                String role = fields[3];

                // Creăm tipul de cont
                Account account = "Premium".equalsIgnoreCase(accountType) ? new PremiumAccount() : new FreeAccount();

                // Determinăm dacă utilizatorul este Admin
                boolean isAdmin = "Admin".equalsIgnoreCase(role);

                // Adăugăm utilizatorul în listă
                users.add(new User(username, password, account, isAdmin));
            }
        } catch (IOException e) {
            System.out.println("Error loading users: " + e.getMessage());
        }
        return users;
    }
}
