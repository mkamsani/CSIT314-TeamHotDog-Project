package com.hotdog.ctbs.controller.manager;

// Application imports.
import com.hotdog.ctbs.entity.Screening;
import com.hotdog.ctbs.repository.MovieRepository;
import com.hotdog.ctbs.repository.ScreeningRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin(origins = "*", allowedHeaders = "*")
@RequestMapping("/manager/screening")
public class ManagerScreeningReadController {

    private final ScreeningRepository screeningRepo;
    private final MovieRepository movieRepo;

    public ManagerScreeningReadController(ScreeningRepository screeningRepo,
                                          MovieRepository movieRepo)
    {
        this.screeningRepo = screeningRepo;
        this.movieRepo = movieRepo;
    }

    // curl.exe -X GET http://localhost:8000/api/manager/screening/read/all
    // curl.exe -X GET http://localhost:8000/api/manager/screening/read/Thor
    @GetMapping(value = "/read/{param}")
    public ResponseEntity<String> Read(@PathVariable final String param)
    {
        try {
            String json = Screening.readScreeningManager(screeningRepo, movieRepo, param);
            return ResponseEntity.ok().body(json);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
