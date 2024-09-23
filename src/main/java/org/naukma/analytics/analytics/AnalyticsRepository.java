package org.naukma.analytics.analytics;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.Optional;

@Repository
public interface AnalyticsRepository extends JpaRepository<AnalyticsEntity, Long> {
    @Query("SELECT a FROM AnalyticsEntity a WHERE a.date = CURRENT_DATE")
    Optional<AnalyticsEntity> findToday();

    Optional<AnalyticsEntity> findByDate(LocalDate date);
}
