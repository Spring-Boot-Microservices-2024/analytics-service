package org.naukma.analytics.analytics;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.modulith.events.ApplicationModuleListener;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

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
        return analyticsRepository.save(newEntity);
    }

    public AnalyticsDto getToday() {
        AnalyticsEntity entity = getOrCreateTodayEntity();
        return AnalyticsMapper.INSTANCE.entityToDto(entity);
    }

    public List<AnalyticsDto> getAll() {
        return analyticsRepository.findAll().stream().map(AnalyticsMapper.INSTANCE::entityToDto).toList();
    }

    public AnalyticsDto getByDate(LocalDate date) {
        Optional<AnalyticsEntity> entity = analyticsRepository.findByDate(date);
        return entity.map(AnalyticsMapper.INSTANCE::entityToDto).orElse(null);
    }

    @ApplicationModuleListener
    protected void onAnalyticsEvent(AnalyticsEvent event) {
        log.info("Received analytics event of type: {}", event.getType());
        AnalyticsEntity entity = getOrCreateTodayEntity();

        switch (event.getType()) {
            case USER_REGISTERED -> entity.setNewUsers(entity.getNewUsers() + 1);
            case EVENT_CREATED -> entity.setNewEvents(entity.getNewEvents() + 1);
            case BOOKING_CREATED -> entity.setNewBookings(entity.getNewBookings() + 1);
            case REVIEW_CREATED -> entity.setNewReviews(entity.getNewReviews() + 1);
        }

        analyticsRepository.save(entity);
    }
}
