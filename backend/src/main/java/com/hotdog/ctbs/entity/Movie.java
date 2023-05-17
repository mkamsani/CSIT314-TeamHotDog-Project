package com.hotdog.ctbs.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.hotdog.ctbs.repository.MovieRepository;
import com.hotdog.ctbs.repository.ScreeningRepository;
import com.hotdog.ctbs.repository.UserAccountRepository;
import com.hotdog.ctbs.repository.UserProfileRepository;
import jakarta.persistence.*;
import jakarta.transaction.Transactional;
import lombok.*;

import java.time.LocalDate;
import java.util.*;

@Builder
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
    protected boolean isActive;

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

    @OneToMany(mappedBy = "movie", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnore
    protected Set<Screening> screenings = new LinkedHashSet<>();

    @Transient
    public static ObjectMapper objectMapper = new ObjectMapper().registerModule(new JavaTimeModule());

    @SneakyThrows
    @Override
    public String toString() {
        return new ObjectMapper().registerModule(new JavaTimeModule())
                .writeValueAsString(this);
    }


    // ******** (ManagerMovieCreateController) ********
    // create a new movie
    public static void createMovie(MovieRepository movieRepo,
                                   final String title,
                                   final String genre,
                                   final String description,
                                   final LocalDate releaseDate,
                                   final String imageUrl,
                                   final String landscapeImageUrl,
                                   final String contentRating) {
        // the new movie Title must not be the same as any existing title in database (1st check)
        // the content rating must be in lowercase form (2nd check)
        // the content rating must be one of the valid content rating (3rd check)

        for (String existingMovieTitle : movieRepo.findAll().stream().map(Movie::getTitle).toList()) {
            if (existingMovieTitle.equalsIgnoreCase(title)) {

                // return the movie title message that already exists
                throw new IllegalArgumentException(title + "already exists. Cannot create new movie." +
                        " Please enter a new movie title.");
            }

        }

        if (!contentRating.equals(contentRating.toLowerCase())) {
            throw new IllegalArgumentException("The content rating given must be in lowercase form.");
        }

        if (!contentRating.toLowerCase().equals("g") &&
                !contentRating.toLowerCase().equals("pg") &&
                !contentRating.toLowerCase().equals("pg13") &&
                !contentRating.toLowerCase().equals("nc16") &&
                !contentRating.toLowerCase().equals("m18") &&
                !contentRating.toLowerCase().equals("r21")) {
            throw new IllegalArgumentException("The content rating given is invalid.");
        }

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

    // ******** (ManagerReadMovieController) ********
    // return a string of list of all movies details
    public static String readMovie(MovieRepository movieRepo, final String param) {

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
            on.put("isActive", movie.isActive);
            on.put("contentRating", movie.contentRating);
            an.add(on);
        }

        return an.toString();
    }

    // ******** (CustomerReadMovieController) ********
    // return a string of list of all movies details
    public static String readActiveMovie(MovieRepository movieRepo, final String param) {

        List<Movie> activeMovieList = switch (param) {

            case "all" -> movieRepo.findAll().stream().filter(Movie::isActive).toList();

            default -> {

                Movie movie = movieRepo.findMovieByTitleAndIsActiveTrue(param).orElse(null);
                if (movie == null) throw new IllegalArgumentException("The movie doesnt exist.");

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
            on.put("isActive", movie.isActive);
            on.put("contentRating", movie.contentRating);
            an.add(on);
        }

        return an.toString();
    }

    // ******** (ManagerUpdateMovieController) ********
    // update all attribute of movie except isActive (will be used in Suspend method)
    public static void updateMovie(MovieRepository movieRepo, String targetTitle, String newTitle, String newGenre, String newDescription,
                                   LocalDate newReleaseDate, String newImageUrl, String newLandscapeImageUrl,
                                   String newContentRating) {
        boolean movieFound = false;

        // make sure new movie title is not same as other existing movie titles
        for (String existingMovieTitle : movieRepo.findAll().stream().map(Movie::getTitle).toList()) {
            if (existingMovieTitle.equalsIgnoreCase(newTitle)) {
                throw new IllegalArgumentException("The new movie title" + newTitle + "already exists.");
            }

        }

        // update everything if found the existing movie title
        for (Movie exsitingMovie : movieRepo.findAll()) {
            if (exsitingMovie.title.equals(targetTitle)) {
                exsitingMovie.title = newTitle;
                exsitingMovie.genre = newGenre.toLowerCase();
                exsitingMovie.description = newDescription;
                exsitingMovie.releaseDate = newReleaseDate;
                exsitingMovie.imageUrl = newImageUrl;
                exsitingMovie.landscapeImageUrl = newLandscapeImageUrl;
                exsitingMovie.contentRating = newContentRating;
                movieRepo.save(exsitingMovie);
                movieFound = true;
                break;
            }

        }
        // if the movie that would to be updated is not found, throw an exception
        if (!movieFound) {
            throw new IllegalArgumentException("The movie title " + targetTitle + "you would like to update does not exist.");
        }

    }


    // ******** (ManagerSuspendMovieController) ********
    public static void suspendMovie(MovieRepository movieRepo, String targetTitle) {
        boolean movieFound = false;
        // update the movie's active status if found the existing movie title
        for (Movie exsitingMovie : movieRepo.findAll()) {
            if (exsitingMovie.title.equalsIgnoreCase(targetTitle)) {
                exsitingMovie.setActive(false);
                movieRepo.save(exsitingMovie);
                movieFound = true;
                break;
            }

        }
        // if the movie title that would to be updated is not found, throw an exception
        if (!movieFound) {
            // throw an exception including targetTitle name if the movie title is not found
            throw new IllegalArgumentException("The movie " + targetTitle + "you would like to suspend does not exist.");
        }

    }

    public static void deleteMovie(MovieRepository movieRepo,
                                   ScreeningRepository screeningRepo,
                                   String targetTitle) {

        // check if the movie title exists
        boolean movieFound = false;
        for (Movie existingMovie : movieRepo.findAll()) {
            if (existingMovie.title.equalsIgnoreCase(targetTitle)) {
                movieFound = true;
                break;
            }
        }

        if (!movieFound) {
            throw new IllegalArgumentException("The movie " + targetTitle + " you would like to delete does not exist.");
        }


        // make sure the movie has no future screening
        for (Screening existingScreening : screeningRepo.findAll()) {
            if (existingScreening.getMovie().getTitle().equalsIgnoreCase(targetTitle)) {
                LocalDate showDate = existingScreening.getShowDate();
                if (showDate.isAfter(LocalDate.now()))
                    throw new IllegalArgumentException("The movie " + targetTitle + " you would like to delete has the screening in the future.");
                if (showDate.isAfter(LocalDate.now().minusDays(30)))
                    throw new IllegalArgumentException("You cant delete a movie" + targetTitle + "that has the screening in the past less than 30 days.");
            }
        }

        Movie movie = movieRepo.findMovieByTitle(targetTitle).orElse(null);
        movieRepo.delete(movie);
    }
}
