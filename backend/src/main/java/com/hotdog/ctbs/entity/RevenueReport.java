package com.hotdog.ctbs.entity;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.hotdog.ctbs.repository.RevenueReportRepository;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.Immutable;

import java.math.BigDecimal;
import java.util.List;

/**
 * Mapping for DB view
 */
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
@Immutable
@Table(name = "revenue_report_base")
public class RevenueReport {
    @EmbeddedId
    protected RevenueReportId id;

    @Column(name = "t_type_price", precision = 10, scale = 2)
    protected BigDecimal typePrice;

    @Column(name = "t_type_sum_revenue")
    protected BigDecimal typeSumRevenue;

    @Column(name = "t_total_tickets")
    protected Long totalTickets;

    @Transient
    private static final ObjectMapper objectMapper = new ObjectMapper().registerModule(new JavaTimeModule());

    //////////////////////////////// Service /////////////////////////////////

    public static String readRevenueReport(RevenueReportRepository revenueReportRepository,
                                           String when)
    {
        List<RevenueReport> revenueReportList = switch (when) {
            case "lastMonth" -> revenueReportRepository.findRevenueReportLastMonth();
            case "lastWeek" -> revenueReportRepository.findRevenueReportLastWeek();
            case "yesterday" -> revenueReportRepository.findRevenueReportYesterday();
            default -> throw new IllegalStateException("Unexpected value: " + when);
        };

        ArrayNode an = objectMapper.createArrayNode();
        for (RevenueReport rr : revenueReportList) {
            ObjectNode on = objectMapper.createObjectNode();
            String typeName = rr.id.typeName.equals("total") ? "N/A" : rr.id.typeName;
            String purchaseDate = rr.id.typeName.equals("total") ? "Total" : rr.id.purchaseDate.toString();
            String typePrice = rr.id.typeName.equals("total") ? "N/A" : rr.typePrice.toString();
            String typeSumRevenue = rr.typeSumRevenue.toString();
            String totalTickets = rr.totalTickets.toString();

            on.put("typeName", typeName);
            on.put("purchaseDate", purchaseDate);
            on.put("typePrice", typePrice);
            on.put("typeSumRevenue", typeSumRevenue);
            on.put("totalTickets", totalTickets);

            an.add(on);
        }
        return an.toString();
    }
}