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
@RequestMapping("/owner/revenue/daily")
public class OwnerRevenueDailyReadController {
    private final RevenueReportRepository revenueReportRepo;

    public OwnerRevenueDailyReadController(RevenueReportRepository revenueReportRepo)
    {
        this.revenueReportRepo = revenueReportRepo;
    }

    @GetMapping(value = "/read")
    public ResponseEntity<String> readRevenueReportYesterday()
    {
        try {
            String json = RevenueReport.readRevenueReport(revenueReportRepo, "yesterday");
            return ResponseEntity.ok().body(json);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
