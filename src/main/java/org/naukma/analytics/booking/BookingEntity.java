package org.naukma.analytics.booking;

import jakarta.persistence.Convert;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.naukma.analytics.common.converter.MapToJsonConverter;

import java.time.LocalDate;
import java.util.Map;

@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class BookingEntity {
    @Id
    @GeneratedValue
    private Long id;

    private Long eventId;

    @Convert(converter = MapToJsonConverter.class)
    private Map<LocalDate, Integer> numOfBookings;
}
