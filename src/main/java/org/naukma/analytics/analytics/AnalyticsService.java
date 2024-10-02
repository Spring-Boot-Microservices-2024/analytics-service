package org.naukma.analytics.analytics;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class AnalyticsService {
    private final AnalyticsRepository analyticsRepository;

    private AnalyticsEntity getOrCreateTodayEntity() {
        Optional<AnalyticsEntity> today = analyticsRepository.findToday();

        if (today.isPresent()) return today.get();

        AnalyticsEntity newEntity = new AnalyticsEntity();
        newEntity.setDate(LocalDate.now());

        return analyticsRepository.saveAndFlush(newEntity);
    }

    public AnalyticsDto getToday() {
        return AnalyticsMapper.INSTANCE.entityToDto(getOrCreateTodayEntity());
    }

    public List<AnalyticsDto> getAll() {
        return analyticsRepository.findAll().stream().map(AnalyticsMapper.INSTANCE::entityToDto).toList();
    }

    public AnalyticsDto getByDate(LocalDate date) {
        Optional<AnalyticsEntity> entity = analyticsRepository.findByDate(date);
        return entity.map(AnalyticsMapper.INSTANCE::entityToDto).orElse(null);
    }

    public String generateCsvReport() {
        List<AnalyticsDto> analyticsList = this.getAll();
        String header = "Date,New Users,New Events,New Reviews,New Bookings\n";

        SimpleDateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd");

        String rows = analyticsList.stream()
                .map(dto -> String.format("%s,%d,%d,%d,%d",
                        dateFormatter.format(dto.getDate()),   // Format the date correctly
                        dto.getNewUsers(),
                        dto.getNewEvents(),
                        dto.getNewReviews(),
                        dto.getNewBookings()))
                .collect(Collectors.joining("\n"));

        return header + rows;
    }

    public void reportEvent(AnalyticsEvent event) {
        log.info("Received analytics event of type: {}", event.getType());
        AnalyticsEntity entity = getOrCreateTodayEntity();

        switch (event.getType()) {
            case USER_REGISTERED -> entity.setNewUsers(entity.getNewUsers() + 1);
            case EVENT_CREATED -> entity.setNewEvents(entity.getNewEvents() + 1);
            case BOOKING_CREATED -> entity.setNewBookings(entity.getNewBookings() + 1);
            case REVIEW_CREATED -> entity.setNewReviews(entity.getNewReviews() + 1);
            default -> throw new IllegalArgumentException("Unsupported event type: " + event.getType());
        }

        analyticsRepository.save(entity);
    }
}
