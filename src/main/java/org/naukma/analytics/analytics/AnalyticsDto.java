package org.naukma.analytics.analytics;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
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
