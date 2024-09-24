package org.naukma.analytics.analytics;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/analytics")
@RequiredArgsConstructor
public class AnalyticsController {
    private final AnalyticsService analyticsService;

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<AnalyticsDto> getAll() {
        return analyticsService.getAll();
    }

    @GetMapping("/today")
    @ResponseStatus(HttpStatus.OK)
    public AnalyticsDto getToday() {
        return analyticsService.getToday();
    }

    @GetMapping("/{date}")
    @ResponseStatus(HttpStatus.OK)
    public AnalyticsDto getByDate(@PathVariable LocalDate date) {
        return analyticsService.getByDate(date);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<String> handleException(Exception e) {
        String errorMessage = "ERROR: " + e.getMessage();
        log.error(errorMessage);
        return new ResponseEntity<>(errorMessage, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.OK)
    public void reportEvent(@RequestBody AnalyticsEvent analyticsEvent) {
        analyticsService.reportEvent(analyticsEvent);
    }
}
