package com.hotdog.ctbs.repository;

import com.hotdog.ctbs.entity.RevenueReport;
import com.hotdog.ctbs.entity.RevenueReportId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface RevenueReportRepository extends JpaRepository<RevenueReport, RevenueReportId> {

    @Query(value = "SELECT * FROM revenue_report_last_month\n", nativeQuery = true)
    List<RevenueReport> findRevenueReportLastMonth();

    @Query(value = "SELECT * FROM revenue_report_last_week\n", nativeQuery = true)
    List<RevenueReport> findRevenueReportLastWeek();

    @Query(value = "SELECT * FROM revenue_report_yesterday\n", nativeQuery = true)
    List<RevenueReport> findRevenueReportYesterday();
}