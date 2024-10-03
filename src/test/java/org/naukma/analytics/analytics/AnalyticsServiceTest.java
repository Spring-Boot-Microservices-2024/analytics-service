package org.naukma.analytics.analytics;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.naukma.analytics.utils.AnalyticsUtils;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;

public class AnalyticsServiceTest {

    @Mock
    private AnalyticsRepository analyticsRepository;

    @InjectMocks
    private AnalyticsService analyticsService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void getToday_shouldReturnAnalyticsDto() {
        // GIVEN
        AnalyticsEntity entity = AnalyticsUtils.getRandomAnalyticsEntity(LocalDate.now());
        when(analyticsRepository.findToday()).thenReturn(Optional.of(entity));

        // WHEN
        AnalyticsDto result = analyticsService.getToday();

        // THEN
        assertEquals(AnalyticsMapper.INSTANCE.entityToDto(entity), result);
        verify(analyticsRepository, times(1)).findToday();
    }

    @Test
    void getAll_shouldReturnAllAnalytics() {
        // GIVEN
        AnalyticsEntity entity1 = AnalyticsUtils.getRandomAnalyticsEntity(LocalDate.of(2023, 10, 1));
        AnalyticsEntity entity2 = AnalyticsUtils.getRandomAnalyticsEntity(LocalDate.of(2023, 10, 2));
        when(analyticsRepository.findAll()).thenReturn(List.of(entity1, entity2));

        // WHEN
        List<AnalyticsDto> result = analyticsService.getAll();

        // THEN
        assertEquals(2, result.size());
        assertTrue(result.contains(AnalyticsMapper.INSTANCE.entityToDto(entity1)), "resultList should contain all analytics");
        assertTrue(result.contains(AnalyticsMapper.INSTANCE.entityToDto(entity2)), "resultList should contain all analytics");
        verify(analyticsRepository, times(1)).findAll();
    }

    @Test
    void getByDate_shouldReturnAnalyticsDto() {
        // GIVEN
        LocalDate date = LocalDate.of(2023, 10, 1);
        AnalyticsEntity entity = AnalyticsUtils.getRandomAnalyticsEntity(date);
        when(analyticsRepository.findByDate(date)).thenReturn(Optional.of(entity));

        // WHEN
        AnalyticsDto result = analyticsService.getByDate(date);

        // THEN
        assertEquals(AnalyticsMapper.INSTANCE.entityToDto(entity), result);
        verify(analyticsRepository, times(1)).findByDate(date);
    }

    @Test
    void generateCsvReport_shouldReturnCsvContent() {
        // GIVEN
        AnalyticsEntity entity1 = AnalyticsUtils.getRandomAnalyticsEntity(LocalDate.of(2023, 10, 1));
        AnalyticsEntity entity2 = AnalyticsUtils.getRandomAnalyticsEntity(LocalDate.of(2023, 10, 2));

        String expectedHeader = "Date,New Users,New Events,New Reviews,New Bookings\n";
        String expectedRow = "2023-10-01," + entity1.getNewUsers() + "," + entity1.getNewEvents() + "," + entity1.getNewReviews() + "," + entity1.getNewBookings() + "\n" +
                "2023-10-02," + entity2.getNewUsers() + "," + entity2.getNewEvents() + "," + entity2.getNewReviews() + "," + entity2.getNewBookings();

        when(analyticsRepository.findAll()).thenReturn(List.of(entity1, entity2));

        // WHEN
        String csvResult = analyticsService.generateCsvReport();

        // THEN
        assertEquals(expectedHeader + expectedRow, csvResult);
        verify(analyticsRepository, times(1)).findAll();
    }
}

