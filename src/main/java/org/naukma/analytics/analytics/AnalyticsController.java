package org.naukma.analytics.analytics;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
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

    @PostMapping
    @ResponseStatus(HttpStatus.OK)
    public void reportEvent(@RequestBody AnalyticsEvent analyticsEvent) {
        analyticsService.reportEvent(analyticsEvent);
    }
}
