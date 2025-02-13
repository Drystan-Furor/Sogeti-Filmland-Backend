package sogeti.filmland.security;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.test.web.servlet.MockMvc;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Slf4j
@SpringBootTest
@AutoConfigureMockMvc
@Import(SecurityConfig.class)
public class SecurityConfigTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private SecurityConfig securityConfig;

    @Autowired
    private AuthenticationConfiguration authenticationConfiguration;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Test
    void contextLoads() {
        assertThat(securityConfig).isNotNull();
    }

    @Test
    void authenticationManagerBeanShouldBeCreated() throws Exception {
        assertThat(authenticationManager).isNotNull();
    }

    @Test
    void csrfShouldBeDisabledForAuthenticateEndpoint() throws Exception {
        String requestBody = """
        {
            "email": "member@example.com",
            "password": "filmland"
        }
    """;

        mockMvc.perform(post("/api/auth/authenticate")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isOk());
    }

    @Test
    void filmlandAssessmentExists() throws Exception {
        String requestBody = """
        {
            "email": "info@filmland-assessment.nl",
            "password": "hashedpassword123"
        }
    """;

        mockMvc.perform(post("/api/auth/authenticate")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isOk());
    }

    @Test
    void shouldSubscribeSuccessfully() throws Exception {
        final String VALID_JWT_TOKEN = "Bearer eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJpbmZvQGZpbG1sYW5kLWFzc2Vzc21lbnQubmwiLCJpYXQiOjE3Mzk0NDI3NzQsImV4cCI6MTczOTQ0NjM3NH0.TGqD1jrUdxEkTVSMw0A8uxCldCFB0Bo3Ge-VKzlDrXU";
        String requestBody = """
        {
            "email": "info@filmland-assessment.nl",
            "availableCategory": "Dutch Films"
        }
        """;

        mockMvc.perform(post("/subscriptions")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", VALID_JWT_TOKEN)
                        .content(requestBody))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("Abonnement succesvol aangemaakt."));
    }

    @Test
    void shouldReturnBadRequestIfAlreadySubscribed() throws Exception {
        final String VALID_JWT_TOKEN = "Bearer eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJpbmZvQGZpbG1sYW5kLWFzc2Vzc21lbnQubmwiLCJpYXQiOjE3Mzk0NDA0NTcsImV4cCI6MTczOTQ0NDA1N30.jK18p_Xms4zzvRwBvaOv09XqoVAJWPTZtc31akRibQA";
        String requestBody = """
        {
            "email": "info@filmland-assessment.nl",
            "availableCategory": "Dutch Films"
        }
        """;

        // First subscription should succeed
        mockMvc.perform(post("/subscriptions")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", VALID_JWT_TOKEN)
                        .content(requestBody))
                .andExpect(status().isOk());

        // Second subscription should fail (already subscribed)
        mockMvc.perform(post("/subscriptions")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", VALID_JWT_TOKEN)
                        .content(requestBody))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("U bent al geabonneerd op deze categorie."));
    }

    @Test
    void shouldReturnBadRequestIfUserNotFound() throws Exception {
        String requestBody = """
        {
            "email": "nonexistent@example.com",
            "availableCategory": "Dutch Films"
        }
        """;

        mockMvc.perform(post("/subscriptions")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", "Bearer VALID_JWT_TOKEN")
                        .content(requestBody))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("Member not found with email: nonexistent@example.com"));
    }

    @Test
    void shouldReturnBadRequestIfCategoryNotFound() throws Exception {
        String requestBody = """
        {
            "email": "info@filmland-assessment.nl",
            "availableCategory": "Unknown Category"
        }
        """;

        mockMvc.perform(post("/subscriptions")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", "Bearer VALID_JWT_TOKEN")
                        .content(requestBody))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("Category not found with name: Unknown Category"));
    }

    @Test
    void shouldReturnForbiddenIfUnauthorized() throws Exception {
        String requestBody = """
        {
            "email": "info@filmland-assessment.nl",
            "availableCategory": "Dutch Films"
        }
        """;

        mockMvc.perform(post("/subscriptions")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isForbidden()); // 403 expected when no JWT token is provided
    }
}




