package controller;


import model.*;
import model.Serial;

import java.io.*;
import java.util.List;
import java.util.ArrayList;

/**
 * Clas DataManager gestionează încărcarea și salvarea datelor din/în fișiere CSV.
 */
public class DataManager {

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
            String line = reader.readLine();
            while ((line = reader.readLine()) != null) {
                String[] filds = line.split(",");
                String id = filds[0];
                String title = filds[1];
                int duration = Integer.parseInt(filds[2]);
                double rating = Double.parseDouble(filds[3]);
                movies.add(new Movie(title, duration, rating));
            }
        } catch (IOException e) {
            System.out.println("Error loading movies: " + e.getMessage());
        }
        return movies;
    }

    /**
     * Salveaza o lista de seriale intr-un CSV.
     * @param serials Lista de seriale salvate.
     * @param fileName Calea cater fisierul CSV unde datele vor fi salvate.
     */
    public static void saveSerials(List<Serial> serials, String fileName) {
        try (BufferedWriter wreiter = new BufferedWriter(new FileWriter(fileName))) {
            wreiter.write("ID,Title,Rating,Episodes");
            wreiter.newLine();

            for (Serial serial : serials) {
                String episodes = serial.getEpisodes().stream()
                        .map(e -> e.getEpisodeName() + "," + e.getEpisodeNumber())
                        .reduce((e1, e2) -> e1 + ";" + e2)
                        .orElse("");
                String line = String.format("%s,%s,%.1f,%s",
                        serial.getSerialId(),
                        serial.getTitle(),
                        serial.getRating(),
                        episodes);
                wreiter.write(line);
                wreiter.newLine();
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
                String[] filds = line.split(",", -1);
                String serialId = filds[0];
                String serialTitle = filds[1];
                double rating = Double.parseDouble(filds[2]);
                String episodesData = filds[3].replace("\"", "");

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
     * Salvează istoricul vizionărilor (HistoryList) într-un fișier CSV.
     *
     * @param historyList Lista de istoric de salvat.
     * @param fileName    Calea către fișierul CSV unde datele vor fi salvate.
     */
    public static void saveHistoryList(HistoryList historyList, String fileName) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(fileName))) {
            writer.write("Title,Type,ViewDate");
            writer.newLine();

            for (HistoryItem item : historyList.getHistory()) {
                String line = String.format("%s,%s,%s",
                        item.getTitle(),
                        item.getType(),
                        item.getViewedDate());
                writer.write(line);
                writer.newLine();
            }
        }catch (IOException e) {
            System.out.println("Error saving history list: " + e.getMessage());
        }
    }

    public static void saveWatchList(WatchList watchList, String fileName) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(fileName))) {
            writer.write("Type,Title");
            writer.newLine();

            for (Movie movie : watchList.getMovies()) {
                String line = String.format("Movie,%s", movie.getTitle());
                writer.write(line);
                writer.newLine();
            }

            for (Serial serial : watchList.getSerials()) {
                String line = String.format("Serial,%s", serial.getTitle());
                writer.write(line);
                writer.newLine();
            }
        } catch (IOException e) {
            System.out.println("Error saving watch list: " + e.getMessage());
        }
    }

    /**
     * Salvează lista de utilizatori într-un fișier CSV.
     *
     * @param users    Lista de utilizatori de salvat.
     * @param fileName Calea către fișierul CSV.
     */
    public static void saveUsers(List<User> users, String fileName) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(fileName))) {
            writer.write("Username,Password,AccountType");
            writer.newLine();

            for (User user : users) {
                String accountType = user.getAccount() instanceof PremiumAccount ? "Premium" : "Free";
                writer.write(String.format("%s,%s,%s", user.getUsername(), user.getPassword(), accountType));
                writer.newLine();
            }
        }catch (IOException e) {
            System.out.println("Error saving users: " + e.getMessage());
        }
    }
}