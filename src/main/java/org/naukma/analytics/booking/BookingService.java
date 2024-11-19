package org.naukma.analytics.booking;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@Slf4j
@RequiredArgsConstructor
public class BookingService {

    private final BookingRepository bookingRepository;

    public List<BookingDto> getAll() {
        return bookingRepository.findAll().stream().map(BookingMapper.INSTANCE::entityToDto).toList();
    }

    public BookingDto getByEventId(Long id) {
        return BookingMapper.INSTANCE.entityToDto(bookingRepository.findByEventId(id)
                .orElseThrow(() -> new BookingNotFoundException("Booking for event with ID " + id + " not found")));
    }

    public BookingDto getById(Long id) {
        return BookingMapper.INSTANCE.entityToDto(bookingRepository.findById(id)
                .orElseThrow(() -> new BookingNotFoundException("Booking with ID " + id + " not found")));
    }

    @JmsListener(destination = "booking", containerFactory = "jmsTopicListenerFactory")
    private void receiveBookingEvent(BookingEvent bookingEvent) {
        log.info("Received booking event {}", bookingEvent);
        processBookingEvent(bookingEvent);
    }

    private void processBookingEvent(BookingEvent bookingEvent) {
        Long eventId = bookingEvent.getEvent().getId();
        BookingEntity bookingEntity = bookingRepository.findByEventId(eventId)
                .orElseGet(() -> createNewBookingEntity(eventId));
        Map<LocalDate, Integer> bookings = bookingEntity.getNumOfBookings();
        bookings.putIfAbsent(LocalDate.now(), 0);

        if (bookingEvent.getType() == BookingEventType.USER_REGISTERED_FOR_EVENT) {
            bookings.put(LocalDate.now(), bookings.get(LocalDate.now()) + 1);
        } else if (bookingEvent.getType() == BookingEventType.USER_UNREGISTERED_FROM_EVENT) {
            bookings.put(LocalDate.now(), Math.max(bookings.get(LocalDate.now()) - 1, 0));
        }

        bookingRepository.save(bookingEntity);
        log.info("Updated booking entity for event {}: {}", eventId, bookingEntity);
    }

    private BookingEntity createNewBookingEntity(Long eventId) {
        BookingEntity bookingEntity = BookingEntity.builder()
                .eventId(eventId)
                .numOfBookings(new HashMap<>())
                .build();
        return bookingRepository.save(bookingEntity);
    }
}
