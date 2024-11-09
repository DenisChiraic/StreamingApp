package service;

import model.Movie;
import model.Serial;
import repository.IRepo;

import java.util.List;

public class ContentService {
    private final IRepo<Movie> movieRepo;
    private final IRepo<Serial> serialRepo;

    public ContentService(IRepo<Movie> movieRepo, IRepo<Serial> serialRepo) {
        this.movieRepo = movieRepo;
        this.serialRepo = serialRepo;
    }

    public List<Movie> getAllMovies() {
        return movieRepo.findAll();
    }

    public List<Serial> getAllSerials() {
        return serialRepo.findAll();
    }

    public void addMovie(Movie movie) {
        movieRepo.create(movie);
    }

    public void addSerial(Serial serial) {
        serialRepo.create(serial);
    }
}
