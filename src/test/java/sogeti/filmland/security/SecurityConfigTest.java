package sogeti.filmland.security;

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
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

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


}
