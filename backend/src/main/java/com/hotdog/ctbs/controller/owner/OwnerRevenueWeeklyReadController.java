package com.hotdog.ctbs.controller.owner;

import com.hotdog.ctbs.entity.RevenueReport;
import com.hotdog.ctbs.repository.RevenueReportRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@CrossOrigin(origins = "*", allowedHeaders = "*")
@RequestMapping("/owner/revenue/weekly")
public class OwnerRevenueWeeklyReadController {
    private final RevenueReportRepository revenueReportRepo;

    public OwnerRevenueWeeklyReadController(RevenueReportRepository revenueReportRepo)
    {
        this.revenueReportRepo = revenueReportRepo;
    }

    @GetMapping(value = "/read")
    public ResponseEntity<String> readRevenueReportLastWeek()
    {
        try {
            String json = RevenueReport.readRevenueReport(revenueReportRepo, "lastWeek");
            return ResponseEntity.ok().body(json);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
