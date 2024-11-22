package org.naukma.analytics.booking;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.naukma.analytics.event.Event;
import org.naukma.analytics.user.User;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class BookingEvent {
    private Event event;
    private User user;
    private BookingEventType type;
}
