package com.hotdog.ctbs.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.hotdog.ctbs.repository.CinemaRoomRepository;
import com.hotdog.ctbs.repository.MovieRepository;
import com.hotdog.ctbs.repository.ScreeningRepository;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.util.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "screening")
@JsonIgnoreProperties({"seats"})
public class Screening {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "uuid", nullable = false)
    protected UUID id;

    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "movie_id", nullable = false)
    protected Movie movie;

    /** 'morning', 'afternoon', 'evening', 'midnight' */
    @Column(name = "show_time", nullable = false)
    protected String showTime;

    /** 'active', 'suspended', 'cancelled' */
    @Column(name = "status", nullable = false)
    protected String status;

    @Column(name = "show_date", nullable = false)
    protected LocalDate showDate;

    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "cinema_room", nullable = false)
    protected CinemaRoom cinemaRoom;

    @Transient
    private static final ObjectMapper objectMapper = new ObjectMapper().registerModule(new JavaTimeModule());

    @OneToMany(mappedBy = "screening")
    protected Set<Ticket> tickets = new LinkedHashSet<>();

    /** @return available seats for this screening. */
    public List<Seat> getAvailableSeats()
    {
        Set<Seat> seats = cinemaRoom.seats;
        for (Ticket ticket : tickets)
            seats.remove(ticket.seat);
        // Sort the seats by the seatRow, then seatColumn.
        return seats.stream().sorted((s1, s2) -> {
            if (s1.seatRow.equals(s2.seatRow))
                return s1.seatColumn.compareTo(s2.seatColumn);
            else
                return s1.seatRow.compareTo(s2.seatRow);
        }).toList();
    }

    //////////////////////////////// Service /////////////////////////////////

    /**
     * Create a screening, the validation process is as follows:
     * <ol>
     * <li>MOVIE (exists? && isActive?)</li>
     * <li>SHOW DATE (is valid?)</li>
     * <li>SHOW TIME (is valid?)</li>
     * <li>CINEMA ROOM (exists? && isActive? && is full? (4 timings only per date))</li>
     * <li>SCREENING (exists?)</li>
     * </ol>
     */
    public static void createScreening(MovieRepository movieRepo,
                                       ScreeningRepository screeningRepo,
                                       CinemaRoomRepository cinemaRoomRepo,
                                       String movieTitle,
                                       String showTime,
                                       LocalDate showDate,
                                       Integer cinemaRoomId)
    {
        // Check if movie exists.
        Movie movie = movieRepo.findMovieByTitle(movieTitle).orElse(null);
        if (movie == null)
            throw new IllegalArgumentException("Movie does not exist.");
        if (!movie.isActive)
            throw new IllegalArgumentException("Movie is not active.");

        // Screening's date cannot be in the past.
        if (showDate.isBefore(LocalDate.now()))
            throw new IllegalArgumentException("Invalid date.");

        // Screening's time must be valid.
        if (!showTime.equals("morning") &&
            !showTime.equals("afternoon") &&
            !showTime.equals("evening") &&
            !showTime.equals("midnight"))
            throw new IllegalArgumentException("Invalid time.");

        CinemaRoom cinemaRoom = cinemaRoomRepo.findById(cinemaRoomId).orElse(null);
        if (cinemaRoom == null)
            throw new IllegalArgumentException("Cinema room does not exist.");
        else if (!cinemaRoom.getIsActive())
            throw new IllegalArgumentException("Cinema room is not active.");

        // A cinema room only can have 4 showTime in a day.
        List<Screening> screenings = screeningRepo.findScreeningsByShowDateAndCinemaRoom(showDate, cinemaRoom);
        if (screenings != null && screenings.size() >= 4) {
            throw new IllegalArgumentException("Cinema room is already full for the given date.");
        }

        // Screening cannot be a duplicate.
        for (Screening screening : screeningRepo.findAll()) {
            CinemaRoom tmp = cinemaRoomRepo.findById(cinemaRoomId).orElse(null);
            if (screening.showTime.equals(showTime) &&
                screening.cinemaRoom.equals(tmp) &&
                screening.showDate.equals(showDate))
                throw new IllegalArgumentException("Screening already exists");
        }

        Screening screening = new Screening();
        screening.id = UUID.randomUUID();
        screening.movie = movie;
        screening.showTime = showTime;
        screening.status = "active";
        screening.showDate = showDate;
        screening.cinemaRoom = cinemaRoom;
        screeningRepo.save(screening);
    }

    /**
     * ManagerReadScreeningController
     * @return a JSON array of all movie screenings.
     */
    public static String readScreeningManager(ScreeningRepository screeningRepo,
                                              MovieRepository movieRepo,
                                              final String param)
    {
        List<Screening> screeningList = switch (param) {
            case "all" ->
                    screeningRepo.findAll();

            default -> {
                Movie movie = movieRepo.findMovieByTitle(param).orElse(null);
                if (movie == null)
                    throw new IllegalArgumentException("Movie does not exist.");
                List<Screening> screenings = screeningRepo.findScreeningsByMovieTitle(param).orElse(null);
                if (screenings == null || screenings.isEmpty()) {
                    throw new IllegalArgumentException("No screenings found for the specified movie.");
                }
                yield screenings;
            }
        };

        ArrayNode an = objectMapper.createArrayNode();
        for (Screening screening : screeningList) {
            ObjectNode on = objectMapper.createObjectNode();
            on.put("movie", screening.movie.getTitle());
            on.put("showTime", screening.showTime);
            on.put("status", screening.status);
            on.put("showDate", screening.showDate.toString());
            on.put("cinemaRoom", screening.cinemaRoom.getId());
            an.add(on);
        }
        return an.toString();
    }

    /**
     * CustomerReadScreeningController
     * @return a JSON array of all active movie screenings after {@code LocalDate.now()}.
     */
    public static String readScreeningCustomer(ScreeningRepository screeningRepo,
                                               MovieRepository movieRepo,
                                               final String param)
    {
        List<Screening> activeScreeningList = switch (param) {
            case "all" -> {
                List<Screening> activeScreenings = screeningRepo.findActiveScreeningsLaterOrEqual(LocalDate.now());
                if ( activeScreenings == null ||activeScreenings.isEmpty())
                    throw new IllegalArgumentException("No active screenings found.");
                yield activeScreenings;
            }
            default -> {
                Movie movie = movieRepo.findMovieByTitle(param).orElse(null);
                if (movie == null)
                    throw new IllegalArgumentException("Movie does not exist.");
                List<Screening> activeScreeningsForMovie = screeningRepo.findActiveScreeningsForMovieAndLaterOrEqual(LocalDate.now(), movie);
                if (activeScreeningsForMovie == null ||activeScreeningsForMovie.isEmpty())
                    throw new IllegalArgumentException("No active screenings found for the specified movie.");
                yield activeScreeningsForMovie;
            }
        };

        // Sort the list by date, then a time of "morning", "afternoon", "evening", "midnight", then cinema room.
        activeScreeningList.sort((Screening o1, Screening o2) -> {
            if (o1.showDate.isBefore(o2.showDate)) return -1;
            if (o1.showDate.isAfter(o2.showDate)) return 1;
            if (o1.showTime.equals("morning") || o2.showTime.equals("morning"))
                return o1.showTime.equals("morning") ? -1 : 1;
            if (o1.showTime.equals("afternoon") || o2.showTime.equals("afternoon"))
                return o1.showTime.equals("afternoon") ? -1 : 1;
            if (o1.showTime.equals("evening") || o2.showTime.equals("evening"))
                return o1.showTime.equals("evening") ? -1 : 1;
            if (o1.showTime.equals("midnight") || o2.showTime.equals("midnight"))
                return o1.showTime.equals("midnight") ? -1 : 1;
            return o1.cinemaRoom.getId().compareTo(o2.cinemaRoom.getId());
        });

        ArrayNode an = objectMapper.createArrayNode();
        for (Screening screening : activeScreeningList) {
            ObjectNode on = objectMapper.createObjectNode();
            on.put("movie", screening.movie.getTitle());
            on.put("showTime", screening.showTime);
            on.put("showDate", screening.showDate.toString());
            on.put("cinemaRoom", screening.cinemaRoom.getId());
            an.add(on);
        }
        return an.toString();
    }

    public static String readScreeningSeatsCustomer(ScreeningRepository screeningRepo,
                                                    final LocalDate showDate,
                                                    final String showTime,
                                                    final Integer cinemaRoomId)
    {
        Screening screening = screeningRepo.findScreeningByShowTimeAndShowDateAndCinemaRoom_Id(showTime, showDate, cinemaRoomId)
                                           .orElse(null);
        if (screening == null)
            throw new IllegalArgumentException("Screening does not exist.");

        ArrayNode an = objectMapper.createArrayNode();
        for (Seat seat : screening.getAvailableSeats()) {
            ObjectNode on = objectMapper.createObjectNode();
            on.put("row", seat.seatRow.toString());
            on.put("column", seat.seatColumn.toString());
            on.put("concat", seat.seatRow.toString() + seat.seatColumn.toString());
            an.add(on);
        }
        return an.toString();
    }

    public static void updateScreening(MovieRepository movieRepo,
                                       ScreeningRepository screeningRepo,
                                       CinemaRoomRepository cinemaRoomRepo,
                                       String targetShowTime,
                                       LocalDate targetShowDate,
                                       Integer targetCinemaRoomId,
                                       String newMovieTitle,
                                       String newShowTime,
                                       LocalDate newShowDate,
                                       Integer newCinemaRoomId)
    {
        // find current screening objects first
        Screening currentScreening = screeningRepo.findScreeningByShowTimeAndShowDateAndCinemaRoom_Id(targetShowTime, targetShowDate, targetCinemaRoomId)
                                                  .orElse(null);
        if (currentScreening == null)
            throw new IllegalArgumentException();
        if (currentScreening.showDate.isBefore(LocalDate.now()))
            throw new IllegalArgumentException("Cannot update a screening that has already passed.");
        if (currentScreening.status.equals("suspended"))
            throw new IllegalArgumentException("Cannot update a screening that is suspended.");
        if (currentScreening.status.equals("cancelled"))
            throw new IllegalArgumentException("Cannot update a screening that is cancelled.");

        Movie newMovie = movieRepo.findMovieByTitle(newMovieTitle).orElse(null);
        if (newMovie == null)
            throw new IllegalArgumentException("New movie does not exist.");
        if (newShowDate.isBefore(LocalDate.now()))
            throw new IllegalArgumentException("Cannot set the date to past.");

        if (!newShowTime.equals("morning") &&
                !newShowTime.equals("afternoon") &&
                !newShowTime.equals("evening") &&
                !newShowTime.equals("midnight"))
            throw new IllegalArgumentException("Invalid time.");

        CinemaRoom newCinemaRoom = cinemaRoomRepo.findById(newCinemaRoomId).orElse(null);
        if (newCinemaRoom == null)
            throw new IllegalArgumentException("New cinema room does not exist.");
        if (!newCinemaRoom.isActive)
            throw new IllegalArgumentException("New cinema room is not active.");

        // showTime, cinemaRoom, showDate cannot be equal to each other.
        for (Screening screening : screeningRepo.findAll())
            if (screening.getShowTime().equals(newShowTime)
                    && screening.getCinemaRoom().equals(newCinemaRoom)
                    && screening.getShowDate().equals(newShowDate))
                throw new IllegalArgumentException("You cannot update to a screening that already exists.");

        currentScreening.movie = newMovie;
        currentScreening.showTime = newShowTime;
        currentScreening.showDate = newShowDate;
        currentScreening.cinemaRoom = newCinemaRoom;
        screeningRepo.save(currentScreening);
    }

    public static void suspendScreening(ScreeningRepository screeningRepo,
                                        String currentShowTime,
                                        LocalDate currentShowDate,
                                        Integer cinemaRoomId)
    {
        Screening currentScreening = screeningRepo.findScreeningByShowTimeAndShowDateAndCinemaRoom_Id(
                currentShowTime, currentShowDate, cinemaRoomId
        ).orElse(null);

        if (currentScreening == null)
            throw new IllegalArgumentException("Screening does not exist.");
        if (currentScreening.status.equals("suspended"))
            throw new IllegalArgumentException("Screening is already suspended.");
        if (currentScreening.status.equals("cancelled"))
            throw new IllegalArgumentException("Screening is already cancelled.");

        currentScreening.status = "suspended";
        screeningRepo.save(currentScreening);
    }
}