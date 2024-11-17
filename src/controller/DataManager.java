package controller;


import model.Movie;
import model.Episode;
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
}