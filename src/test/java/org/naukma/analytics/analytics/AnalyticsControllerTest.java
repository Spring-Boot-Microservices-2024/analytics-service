package org.naukma.analytics.analytics;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.naukma.analytics.authentication.AuthenticationService;
import org.naukma.analytics.utils.AnalyticsUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Collections;

import static org.hamcrest.Matchers.containsString;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(AnalyticsController.class)
class AnalyticsControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private AnalyticsService analyticsService;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private AuthenticationService authenticationService;

    @Value("${auth.key:booking}")
    private String apiKey;

    @Test
    @WithMockUser(username = "testuser")
    void testGetAll() throws Exception {
        // GIVEN
        AnalyticsDto dto = new AnalyticsDto();
        when(analyticsService.getAll()).thenReturn(Collections.singletonList(dto));

        // WHEN THEN
        mockMvc.perform(get("/analytics")
                        .header("X-API-KEY", apiKey)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$[0]").exists());

        verify(analyticsService, times(1)).getAll();
    }

    @Test
    @WithMockUser(username = "testuser")
    void testGetToday() throws Exception {
        // GIVEN
        AnalyticsDto dto = AnalyticsUtils.getRandomAnalyticsDto();
        when(analyticsService.getToday()).thenReturn(dto);

        // WHEN THEN
        mockMvc.perform(get("/analytics/today")
                        .header("X-API-KEY", apiKey)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.newUsers").value(dto.getNewUsers()))
                .andExpect(jsonPath("$.newEvents").value(dto.getNewEvents()))
                .andExpect(jsonPath("$.newReviews").value(dto.getNewReviews()))
                .andExpect(jsonPath("$.newBookings").value(dto.getNewBookings()));

        verify(analyticsService, times(1)).getToday();
    }

    @Test
    @WithMockUser(username = "testuser")
    void testHtmlReport() throws Exception {
        // GIVEN
        AnalyticsDto dto = AnalyticsUtils.getRandomAnalyticsDto();
        when(analyticsService.getAll()).thenReturn(Collections.singletonList(dto));

        // WHEN THEN
        mockMvc.perform(get("/analytics")
                        .header("X-API-KEY", apiKey)
                        .accept(MediaType.TEXT_HTML))
                .andExpect(status().isOk())
                .andExpect(content().contentType("text/html;charset=UTF-8"))
                .andExpect(content().string(containsString("<html>")))
                .andExpect(content().string(containsString("<body>")))
                .andExpect(content().string(containsString("<h1>Analytics Report</h1>")))
                .andExpect(content().string(containsString(String.valueOf(dto.getNewUsers()))))
                .andExpect(content().string(containsString(String.valueOf(dto.getNewEvents()))))
                .andExpect(content().string(containsString(String.valueOf(dto.getNewReviews()))))
                .andExpect(content().string(containsString(String.valueOf(dto.getNewBookings()))));
        verify(analyticsService, times(1)).getAll();
    }

    @Test
    @WithMockUser(username = "testuser")
    void testCsvReport() throws Exception {
        // GIVEN
        String csvContent = "date,newUsers,newEvents,newReviews,newBookings\n" +
                "2024-10-03,100,200,300,400";
        when(analyticsService.generateCsvReport()).thenReturn(csvContent);

        // WHEN THEN
        mockMvc.perform(get("/analytics")
                        .header("X-API-KEY", "test-key")
                        .accept("text/csv"))
                .andExpect(status().isOk())
                .andExpect(content().contentType("text/csv"))
                .andExpect(header().string(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"analytics_report.csv\""))
                .andExpect(content().string(csvContent));

        verify(analyticsService, times(1)).generateCsvReport();
    }
}
