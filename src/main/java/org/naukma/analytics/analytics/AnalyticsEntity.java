package org.naukma.analytics.analytics;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

@Data
@Entity
public class AnalyticsEntity {
    @Id
    @GeneratedValue
    private Long id;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate date;

    private Long newUsers = 0L;
    private Long newEvents = 0L;
    private Long newReviews = 0L;
    private Long newBookings = 0L;
}
