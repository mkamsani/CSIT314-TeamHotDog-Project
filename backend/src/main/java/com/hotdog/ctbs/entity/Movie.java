package com.hotdog.ctbs.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.hotdog.ctbs.repository.MovieRepository;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "movie")
@JsonIgnoreProperties({"screenings"})
public class Movie {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "uuid", nullable = false)
    protected UUID id;

    @Column(name = "is_active", nullable = false)
    protected Boolean isActive;

    @Column(name = "title", nullable = false, length = Integer.MAX_VALUE)
    protected String title;

    @Column(name = "genre", nullable = false, length = Integer.MAX_VALUE)
    protected String genre;

    @Column(name = "description", nullable = false, length = Integer.MAX_VALUE)
    protected String description;

    @Column(name = "release_date", nullable = false)
    protected LocalDate releaseDate;

    @Column(name = "image_url", length = Integer.MAX_VALUE)
    protected String imageUrl;

    @Column(name = "landscape_image_url", length = Integer.MAX_VALUE)
    protected String landscapeImageUrl;

    @Column(name = "content_rating", nullable = false, length = Integer.MAX_VALUE)
    protected String contentRating;

    @OneToMany(mappedBy = "movie")
    @JsonIgnore
    protected Set<Screening> screenings = new LinkedHashSet<>();

    @Transient
    private static final ObjectMapper objectMapper = new ObjectMapper().registerModule(new JavaTimeModule());

    //////////////////////////////// Service /////////////////////////////////

    /** @see com.hotdog.ctbs.controller.manager.ManagerMovieCreateController */
    public static void createMovie(MovieRepository movieRepo,
                                   final String title,
                                   final String genre,
                                   final String description,
                                   final LocalDate releaseDate,
                                   final String imageUrl,
                                   final String landscapeImageUrl,
                                   final String contentRating)
    {
        // the new movie Title must not be the same as any existing title in database (1st check)
        // the content rating must be in lowercase form (2nd check)
        // the content rating must be one of the valid content rating (3rd check)

        if (title.equals("all") || title.equals("screening"))
            throw new IllegalArgumentException("Title is reserved: " + title);

        if (movieRepo.findAllTitles().contains(title.toLowerCase()))
            throw new IllegalArgumentException("Title already exists: " + title);

        if (!contentRating.equals(contentRating.toLowerCase()))
            throw new IllegalArgumentException("Content rating must be in lowercase: " + contentRating);

        if (!movieRepo.findAllContentRatings().contains(contentRating))
            throw new IllegalArgumentException("Content rating is invalid: " + contentRating);

        Movie movie = new Movie();
        movie.id = UUID.randomUUID();
        movie.isActive = true;
        movie.title = title;
        movie.genre = genre.toLowerCase();
        movie.description = description;
        movie.releaseDate = releaseDate;
        movie.imageUrl = imageUrl;
        movie.landscapeImageUrl = landscapeImageUrl;
        movie.contentRating = contentRating;
        movieRepo.save(movie);
    }

    /** @see com.hotdog.ctbs.controller.manager.ManagerMovieReadController */
    public static String readMovieManager(MovieRepository movieRepo,
                                          final String param)
    {
        List<Movie> movieList = switch (param) {
            case "all" -> movieRepo.findAll();
            default -> {
                Movie movie = movieRepo.findMovieByTitle(param).orElse(null);
                if (movie == null) throw new IllegalArgumentException("The movie doesnt exist.");
                yield List.of(movie);
            }
        };

        ArrayNode an = objectMapper.createArrayNode();
        for (Movie movie : movieList) {
            ObjectNode on = objectMapper.createObjectNode();
            on.put("title", movie.title);
            on.put("genre", movie.genre);
            on.put("description", movie.description);
            on.put("releaseDate", movie.releaseDate.toString());
            on.put("imageUrl", movie.imageUrl);
            on.put("landscapeImageUrl", movie.landscapeImageUrl);
            on.put("isActive", movie.isActive.toString());
            on.put("contentRating", movie.contentRating);
            an.add(on);
        }
        return an.toString();
    }

    /** @see com.hotdog.ctbs.controller.customer.CustomerMovieReadController */
    public static String readMovieCustomer(MovieRepository movieRepo,
                                           final String param)
    {
        List<Movie> activeMovieList = switch (param) {
            case "all" -> movieRepo.findAll().stream().filter(Movie::getIsActive).toList();
            case "active" -> movieRepo.findCustomerMovies();
            default -> {
                Movie movie = movieRepo.findMovieByTitleAndIsActiveTrue(param).orElse(null);
                if (movie == null)
                    throw new IllegalArgumentException("Movie does not exist: " + param);
                yield List.of(movie);
            }
        };

        ArrayNode an = objectMapper.createArrayNode();
        for (Movie movie : activeMovieList) {
            ObjectNode on = objectMapper.createObjectNode();
            on.put("title", movie.title);
            on.put("genre", movie.genre);
            on.put("description", movie.description);
            on.put("releaseDate", movie.releaseDate.toString());
            on.put("imageUrl", movie.imageUrl);
            on.put("landscapeImageUrl", movie.landscapeImageUrl);
            on.put("isActive", movie.isActive.toString());
            on.put("contentRating", movie.contentRating);
            an.add(on);
        }
        return an.toString();
    }

    /** @see com.hotdog.ctbs.controller.manager.ManagerMovieUpdateController */
    public static void updateMovie(MovieRepository movieRepo,
                                   String targetTitle,
                                   String newTitle,
                                   String newGenre,
                                   String newDescription,
                                   LocalDate newReleaseDate,
                                   String newImageUrl,
                                   String newLandscapeImageUrl,
                                   String newContentRating)
    {
        // make sure new movie title is not same as other existing movie titles
        for (String existingMovieTitle : movieRepo.findAll().stream().map(Movie::getTitle).toList())
            if (existingMovieTitle.equalsIgnoreCase(newTitle))
                throw new IllegalArgumentException("New movie title already exists: " + newTitle);

        // update everything if found the existing movie title
        for (Movie existingMovie : movieRepo.findAll()) {
            if (existingMovie.title.equals(targetTitle)) {
                existingMovie.title = newTitle;
                existingMovie.genre = newGenre.toLowerCase();
                existingMovie.description = newDescription;
                existingMovie.releaseDate = newReleaseDate;
                existingMovie.imageUrl = newImageUrl;
                existingMovie.landscapeImageUrl = newLandscapeImageUrl;
                existingMovie.contentRating = newContentRating;
                movieRepo.save(existingMovie);
                return; // End the method here.
            }
        }
        throw new IllegalArgumentException("Movie does not exist: " + targetTitle);
    }

    /** @see com.hotdog.ctbs.controller.manager.ManagerMovieSuspendController */
    public static void suspendMovie(MovieRepository movieRepo,
                                    String targetTitle)
    {
        for (Movie existingMovie : movieRepo.findAll()) {
            if (existingMovie.title.equalsIgnoreCase(targetTitle)) {
                existingMovie.isActive = false;
                movieRepo.save(existingMovie);
                return;
            }
        }
        throw new IllegalArgumentException("Movie does not exist: " + targetTitle);
    }
}
