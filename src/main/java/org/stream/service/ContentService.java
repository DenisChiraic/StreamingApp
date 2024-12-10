package org.stream.service;

import org.stream.model.Episode;
import org.stream.model.Movie;
import org.stream.model.Serial;
import org.stream.repository.DatabaseRepo;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Serviciul pentru gestionarea conținutului media (filme și seriale).
 * Oferă metode pentru adăugarea, eliminarea și obținerea de filme și seriale.
 */
public class ContentService {
    private DatabaseRepo<Movie> movieRepo;
    private DatabaseRepo<Serial> serialRepo;

    /**
     * Constructorul serviciului de conținut, care primește două repo-uri: unul pentru filme și unul pentru seriale.
     * @param movieRepo Repository-ul pentru filme.
     * @param serialRepo Repository-ul pentru seriale.
     */
    public ContentService(DatabaseRepo<Movie> movieRepo, DatabaseRepo<Serial> serialRepo) {
        this.movieRepo = movieRepo;
        this.serialRepo = serialRepo;
    }

    /**
     * Adaugă un film în repo-ul de filme.
     * @param movie Filmul care urmează a fi adăugat.
     */
    public void addMovie(Movie movie) {
        movieRepo.save(movie);
    }

    /**
     * Elimină un film din repo-ul de filme, căutând după titlu.
     * @param title Titlul filmului care urmează a fi eliminat.
     */
    public void removeMovie(String title) {
        movieRepo.findAll().stream()
                .filter(movie -> movie.getTitle().equals(title))
                .findFirst()
                .ifPresent(movie -> movieRepo.delete(movie.getId()));
    }

    /**
     * Adaugă un serial în repo-ul de seriale.
     * @param serial Serialul care urmează a fi adăugat.
     */
    public void addSerial(Serial serial) {
        serialRepo.save(serial);
    }

    /**
     * Elimină un serial din repo-ul de seriale, căutând după titlu.
     * @param title Titlul serialului care urmează a fi eliminat.
     */
    public void removeSerial(String title) {
        serialRepo.findAll().stream()
                .filter(serial -> serial.getTitle().equals(title))
                .findFirst()
                .ifPresent(serial -> serialRepo.delete(serial.getId()));
    }

    /**
     * Returnează primele 10 filme după rating, ordonate descrescător.
     * @return Lista cu primele 10 filme.
     */
    public List<Movie> getTop10Movies() {
        return movieRepo.findAll().stream()
                .sorted(Comparator.comparingDouble(Movie::getRating).reversed())
                .limit(10)
                .collect(Collectors.toList());
    }

    /**
     * Returnează primele 10 seriale după rating, ordonate descrescător.
     * @return Lista cu primele 10 seriale.
     */
    public List<Serial> getTop10Serials() {
        return serialRepo.findAll().stream()
                .sorted(Comparator.comparingDouble(Serial::getRating).reversed())
                .limit(10)
                .collect(Collectors.toList());
    }

    /**
     * Returnează toate filmele disponibile.
     * @return Lista cu toate filmele.
     */
    public List<Movie> getAllMovies() {
        return movieRepo.findAll();
    }

    /**
     * Returnează toate serialele disponibile.
     * @return Lista cu toate serialele.
     */
    public List<Serial> getAllSerials() {
        return serialRepo.findAll();
    }

    /**
     * Căută un film după titlu.
     * @param title Titlul filmului care urmează a fi căutat.
     * @return Filmul găsit sau null dacă nu există un astfel de film.
     */
    public Movie getMovieByTitle(String title) {
        return getAllMovies().stream()
                .filter(movie -> movie.getTitle().equalsIgnoreCase(title))
                .findFirst()
                .orElse(null);
    }

    /**
     * Căută un serial după titlu.
     * @param title Titlul serialului care urmează a fi căutat.
     * @return Serialul găsit sau null dacă nu există un astfel de serial.
     */
    public Serial getSerialByTitle(String title) {
        return getAllSerials().stream()
                .filter(serial -> serial.getTitle().equalsIgnoreCase(title))
                .findFirst()
                .orElse(null);
    }

    /**
     * Găsește următorul episod al unui serial, bazat pe episodul curent.
     * @param serial Serialul din care face parte episodul.
     * @param currentEpisode Episodul curent.
     * @return Următorul episod, sau null dacă nu mai există episoade.
     */
    public Episode getNextEpisode(Serial serial, Episode currentEpisode) {
        if (serial == null || currentEpisode == null || serial.getEpisodes() == null) {
            return null;
        }
        int currentIndex = serial.getEpisodes().indexOf(currentEpisode);
        if (currentIndex != -1 && currentIndex + 1 < serial.getEpisodes().size()) {
            return serial.getEpisodes().get(currentIndex + 1);
        }
        return null;
    }
}
