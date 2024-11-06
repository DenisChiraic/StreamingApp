package model;

import java.util.List;

public class TopList {
    private List<Movie> topMovies;
    private List<Serial> topSerials;

    public TopList(List<Movie> topMovies, List<Serial> topSerials) {
        this.topMovies = topMovies;
        this.topSerials = topSerials;
    }

    public List<Movie> getTopMovies() {
        return topMovies;
    }

    public List<Serial> getTopSerials() {
        return topSerials;
    }
}
