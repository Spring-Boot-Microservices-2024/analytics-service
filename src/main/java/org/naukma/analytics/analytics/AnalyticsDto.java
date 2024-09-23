package org.naukma.analytics.analytics;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

import java.util.Date;

@Data
public class AnalyticsDto {
    @JsonIgnore
    private Long id;

    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date date;

    private Long newUsers;
    private Long newEvents;
    private Long newReviews;
    private Long newBookings;
}
