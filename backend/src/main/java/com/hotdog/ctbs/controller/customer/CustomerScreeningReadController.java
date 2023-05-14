package com.hotdog.ctbs.controller.customer;

// Application imports.
import com.hotdog.ctbs.entity.Screening;
import com.hotdog.ctbs.service.implementation.ScreeningImpl;

// Java imports.
import java.time.LocalDate;
import java.util.List;

// JSON serialization imports.
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

// Spring imports.
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@CrossOrigin(origins = "*", allowedHeaders = "*")
@RequestMapping("/customer/screening")
public class CustomerScreeningReadController {

    private final ScreeningImpl screeningImpl;
    private final ObjectMapper objectMapper;

    public CustomerScreeningReadController(ScreeningImpl screeningImpl)
    {
        this.screeningImpl = screeningImpl;
        this.objectMapper = new ObjectMapper().registerModule(new JavaTimeModule());
    }

    @GetMapping(value = "/read/{param}")
    public String CustomerReadScreening(@PathVariable final String param)
    {
        try {
            switch (param) {

                case "all" -> {
                    return screeningImpl.getAllActiveScreenings().toString();
                }

                default -> {
                    return String.valueOf(screeningImpl.getAllActiveScreeningsByMovieTitle(param));
                }

            }

        } catch (Exception e) {
            return "Error: " + e.getMessage();
        }

    }

}
