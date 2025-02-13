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
    @Autowired
    private JwtUtil jwtUtil;

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
        final String token = jwtUtil.generateToken("info@filmland-assessment.nl");
        String requestBody = """
        {
            "email": "info@filmland-assessment.nl",
            "availableCategory": "Dutch Films"
        }
        """;

        mockMvc.perform(post("/subscriptions")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", "Bearer %s".formatted(token))
                        .content(requestBody))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("Abonnement succesvol aangemaakt."));
    }

    @Test
    void shouldReturnBadRequestIfAlreadySubscribed() throws Exception {
        final String token = jwtUtil.generateToken("info@filmland-assessment.nl");
        String requestBody = """
        {
            "email": "info@filmland-assessment.nl",
            "availableCategory": "Dutch Films"
        }
        """;

        // First subscription should succeed
        mockMvc.perform(post("/subscriptions")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", "Bearer %s".formatted(token))
                        .content(requestBody))
                .andExpect(status().isOk());

        // Second subscription should fail (already subscribed)
        mockMvc.perform(post("/subscriptions")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", "Bearer %s".formatted(token))
                        .content(requestBody))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("U bent al geabonneerd op deze categorie."));
    }

    @Test
    void shouldReturnBadRequestIfUserNotFound() throws Exception {
        final String token = jwtUtil.generateToken("info@filmland-assessment.nl");

        String requestBody = """
        {
            "email": "nonexistent@example.com",
            "availableCategory": "Dutch Films"
        }
        """;

        mockMvc.perform(post("/subscriptions")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", "Bearer %s".formatted(token))
                        .content(requestBody))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("Member not found with email: nonexistent@example.com"));
    }

    @Test
    void shouldReturnBadRequestIfCategoryNotFound() throws Exception {
        final String token = jwtUtil.generateToken("info@filmland-assessment.nl");

        String requestBody = """
        {
            "email": "info@filmland-assessment.nl",
            "availableCategory": "Unknown Category"
        }
        """;

        mockMvc.perform(post("/subscriptions")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", "Bearer %s".formatted(token))
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




