package service;

import model.Movie;
import model.Serial;
import repository.IRepo;
import controller.DataManager;

import java.util.List;

public class ContentService {
    private final IRepo<Movie> movieRepo;
    private final IRepo<Serial> serialRepo;

    public ContentService(IRepo<Movie> movieRepo, IRepo<Serial> serialRepo) {
        this.movieRepo = movieRepo;
        this.serialRepo = serialRepo;
        loadMoviesAndSerials();
    }

    /**
     * Încărcăm filmele și serialele din fișierele CSV.
     */
    public void loadMoviesAndSerials() {
        List<Movie> movies = DataManager.loadMovies("movies.csv");
        List<Serial> serials = DataManager.loadSerials("serials.csv");

        movies.forEach(movieRepo::create);
        serials.forEach(serialRepo::create);
    }

    /**
     * Returnează toate filmele disponibile.
     *
     * @return Lista cu filme.
     */
    public List<Movie> getAllMovies() {
        return movieRepo.findAll();
    }

    /**
     * Returnează toate serialele disponibile.
     *
     * @return Lista cu seriale.
     */
    public List<Serial> getAllSerials() {
        return serialRepo.findAll();
    }

    /**
     * Adaugă un film în repo și îl salvează în fișierul CSV.
     *
     * @param movie Filmul de adăugat.
     */
    public void addMovie(Movie movie) {
        movieRepo.create(movie);
        DataManager.saveMovies(movieRepo.findAll(), "movies.csv");
    }

    /**
     * Adaugă un serial în repo și îl salvează în fișierul CSV.
     *
     * @param serial Serialul de adăugat.
     */
    public void addSerial(Serial serial) {
        serialRepo.create(serial);
        DataManager.saveSerials(serialRepo.findAll(), "serials.csv");
    }

    /**
     * Îndepărtează un film pe baza titlului și actualizează fișierul CSV.
     *
     * @param title Titlul filmului de șters.
     * @return `true` dacă filmul a fost șters, altfel `false`.
     */
    public boolean removeMovie(String title) {
        boolean removed = movieRepo.findAll().removeIf(movie -> movie.getTitle().equalsIgnoreCase(title));
        if (removed) {
            DataManager.saveMovies(movieRepo.findAll(), "movies.csv");
        }
        return removed;
    }


    /**
     * Îndepărtează un serial pe baza titlului și actualizează fișierul CSV.
     *
     * @param title Titlul serialului de șters.
     * @return `true` dacă serialul a fost șters, altfel `false`.
     */
    public boolean removeSerial(String title) {
        boolean removed = serialRepo.findAll().removeIf(serial -> serial.getTitle().equalsIgnoreCase(title));
        if (removed) {
            DataManager.saveSerials(serialRepo.findAll(), "serials.csv");
        }
        return removed;
    }
}
