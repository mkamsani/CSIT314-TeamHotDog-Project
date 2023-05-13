package com.hotdog.ctbs.initializer;

import com.hotdog.ctbs.entity.Screening;
import com.hotdog.ctbs.service.implementation.LoyaltyPointImpl;
import com.hotdog.ctbs.service.implementation.ScreeningImpl;
import com.hotdog.ctbs.service.implementation.UserAccountImpl;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

import java.time.LocalDate;
import java.util.List;

/**
 * This class is used to schedule checks.
 * Check if a screening room is suspended.
 * This class is not implemented yet.
 */
@Configuration
@EnableScheduling
@EnableAsync
public class ScheduleChecks {

    private final ScreeningImpl screeningImpl;

    private final LoyaltyPointImpl loyaltyPointImpl;

    private final UserAccountImpl userAccountImpl;

    public ScheduleChecks(ScreeningImpl screeningImpl, LoyaltyPointImpl loyaltyPointImpl,
                          UserAccountImpl userAccountImpl)
    {
        this.screeningImpl = screeningImpl;
        this.loyaltyPointImpl = loyaltyPointImpl;
        this.userAccountImpl = userAccountImpl;
    }

    @Scheduled(cron = "0 0 0 * * *") // Run every midnight.
    public void check() {
        List<Screening> screenings = screeningImpl.getAllScreenings()
                .stream()
                .filter(e -> !e.getStatus().equals("suspended"))

                .toList();
        for (Screening screening : screenings) {
            if (screening.getShowDate().isBefore(LocalDate.now())) {
                //screening.setIsActive(true);
                screening.setStatus("active");
                // screening.setIsActive("cancelled");
            }
            // TODO : code to award loyalty points to users who have watched the movie.
            // List<Ticket> tickets = screening.getTickets();
        }
    }
}
