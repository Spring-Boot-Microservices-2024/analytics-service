package org.naukma.analytics.analytics;

import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.Gauge;
import io.micrometer.core.instrument.MeterRegistry;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.web.bind.annotation.*;

import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/analytics")
@RequiredArgsConstructor
public class AnalyticsController {

    private final AnalyticsService analyticsService;
    private final Counter counter;

    @Autowired
    public AnalyticsController(AnalyticsService analyticsService, MeterRegistry registry) {
        this.analyticsService = analyticsService;
        this.counter = Counter.builder("analytics_events").
                tag("version", "v1").
                description("Total Event Count").
                register(registry);

        Gauge.builder("analytics_new_users_today", () -> getToday().getNewUsers()).
                tag("version", "v1").
                description("Number of new users created today").
                register(registry);
    }

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    public List<AnalyticsDto> getAll() {
        return analyticsService.getAll();
    }

    @GetMapping(value = "/today", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    public AnalyticsDto getToday() {
        return analyticsService.getToday();
    }

    @GetMapping(value = "/{date}", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    public AnalyticsDto getByDate(@PathVariable LocalDate date) {
        return analyticsService.getByDate(date);
    }

    @GetMapping(produces = MediaType.TEXT_HTML_VALUE)
    @ResponseStatus(HttpStatus.OK)
    public String htmlReport() {
        List<AnalyticsDto> analytics = analyticsService.getAll();
        StringBuilder htmlResponse = new StringBuilder("<html><body><h1>Analytics Report</h1><ul>");
        for (AnalyticsDto dto : analytics) {
            htmlResponse.append("<li>").append(dto.toString()).append("</li>");
        }
        htmlResponse.append("</ul></body></html>");
        return htmlResponse.toString();
    }

    @GetMapping(produces = "text/csv")
    public ResponseEntity<Resource> csvReport() {
        String csvContent = analyticsService.generateCsvReport();

        ByteArrayResource resource = new ByteArrayResource(csvContent.getBytes(StandardCharsets.UTF_8));

        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType("text/csv"))
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"analytics_report.csv\"")
                .body(resource);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<String> handleException(Exception e) {
        String errorMessage = "ERROR: " + e.getMessage();
        log.error(errorMessage);
        return new ResponseEntity<>(errorMessage, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @JmsListener(destination = "analytics", containerFactory = "jmsListenerFactory")
    public void receiveMessage(AnalyticsEvent event) {
        counter.increment();
        analyticsService.reportEvent(event);
        System.out.println("Received <" + event + ">");
    }

    @JmsListener(destination = "email", containerFactory = "jmsTopicListenerFactory")
    public void receiveEmailMessage(String email) {
        System.out.println("Received <" + email + ">");
    }
}
