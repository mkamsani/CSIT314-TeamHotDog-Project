package com.hotdog.ctbs.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.Hibernate;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.Objects;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Embeddable
public class RevenueReportId implements Serializable {
    private static final long serialVersionUID = -8044916065216527532L;
    @Column(name = "t_purchase_date")
    protected LocalDate purchaseDate;

    @Column(name = "t_type_name", length = Integer.MAX_VALUE)
    protected String typeName;

    @Override
    public boolean equals(Object o)
    {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        RevenueReportId entity = (RevenueReportId) o;
        return Objects.equals(this.purchaseDate, entity.purchaseDate) &&
               Objects.equals(this.typeName, entity.typeName);
    }

    @Override
    public int hashCode()
    {
        return Objects.hash(purchaseDate, typeName);
    }

}