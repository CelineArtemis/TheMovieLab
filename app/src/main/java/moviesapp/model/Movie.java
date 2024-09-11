package moviesapp.model;

import javafx.scene.image.Image;

import java.net.MalformedURLException;
import java.util.List;
import java.time.LocalDate;

/**
 * The Movie class represents a movie in the application.
 */
public class Movie {
    // Movie attributes
    private int id;
    private String title;
    private String overview;
    private boolean favorite;
    private int year;
    private LocalDate releaseDate;
    private double rating;
    private List<String> genres;
    private String posterPath;
    private int runtime;
    private List<String> cast;
    private String director;

// Getter and setter methods for each attribute

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }
    public String getOverview() { return overview; }
    public void setOverview(String overview) { this.overview = overview; }
    public boolean isFavorite() { return favorite; }
    public void setFavorite(boolean favorite) {
        this.favorite = favorite;
    }
    public int getYear() { return year; }
    public void setYear(int year) { this.year = year; }
    public LocalDate getReleaseDate() { return releaseDate; }
    public void setReleaseDate(LocalDate releaseDate) { this.releaseDate = releaseDate; }
    public double getRating() { return rating; }
    public void setRating(double rating) { this.rating = rating; }
    public List<String> getGenres() { return genres; }
    public void setGenres(List<String> genres) { this.genres = genres; }
    public String getPosterPath() {
        if (this.posterPath == null) {
            return "/icons/noimage.png";
        } else {
            return "https://image.tmdb.org/t/p/w500" + posterPath;
        }
    }
    public void setPosterPath(String posterPath) { this.posterPath = posterPath; }
    public int getRuntime() { return runtime; }
    public void setRuntime(int runtime) { this.runtime = runtime; }
    public List<String> getCast() { return cast; }
    public void setCast(List<String> cast) { this.cast = cast; }
    public String getDirector() { return director; }
    public void setDirector(String director) { this.director = director; }

    /**
     * Method to get the details of the movie as a formatted string.
     * @return A formatted string containing details of the movie.
     */
    public String getDetails() {
        String separator = "----------------------------------------";
        return separator + "\nTitle: " + title + "\nYear: " + year + "\nRelease Date: " + releaseDate + "\nRuntime: " + runtime + " minutes\nNote: " + rating + "\nGenres: " + String.join(", ", genres) + "\nCasting: " + String.join(", ", cast) + "\nDirector: " + director + "\n" + separator;
    }
}