package org.naukma.analytics.analytics;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.naukma.analytics.authentication.AuthenticationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(AnalyticsController.class)
public class AnalyticsSecurityTest {

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
    void testGetAllUnauthorized_shouldReturn401() throws Exception {
        // WHEN THEN
        mockMvc.perform(get("/analytics"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void testGetTodayUnauthorized_shouldReturn401() throws Exception {
        // WHEN THEN
        mockMvc.perform(get("/analytics/today")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void testGetByDateUnauthorized_shouldReturn401() throws Exception {
        // WHEN THEN
        mockMvc.perform(get("/analytics/{date}", "2024-10-03")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void testPostReportUnauthorized_shouldReturn401() throws Exception {
        // WHEN THEN
        mockMvc.perform(post("/analytics"))
                .andExpect(status().is4xxClientError());
    }

    @Test
    @WithMockUser(username = "testuser")
    void testGetAllAuthorized_shouldReturn200() throws Exception {
        // WHEN THEN
        mockMvc.perform(get("/analytics")
                .header("X-API-KEY", apiKey))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(username = "testuser")
    void testGetTodayAuthorized_shouldReturn200() throws Exception {
        // WHEN THEN
        mockMvc.perform(get("/analytics/today")
                        .header("X-API-KEY", apiKey)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(username = "testuser")
    void testGetByDateAuthorized_shouldReturn200() throws Exception {
        // WHEN THEN
        mockMvc.perform(get("/analytics/{date}", "2024-10-03")
                        .header("X-API-KEY", apiKey)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }
}
