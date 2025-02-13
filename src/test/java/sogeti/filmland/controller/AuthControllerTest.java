package sogeti.filmland.controller;

import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;
import sogeti.filmland.model.AuthRequest;
import sogeti.filmland.model.AuthResponse;
import sogeti.filmland.security.JwtUtil;
import sogeti.filmland.service.AuthService;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

class AuthControllerTest {

    @Mock
    private AuthService authService;

    @Mock
    private JwtUtil jwtUtil;

    @InjectMocks
    private AuthController authController;

    public AuthControllerTest() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void authenticate_ShouldReturn200AndToken_WhenCredentialsAreValid() {
        // Arrange
        AuthRequest authRequest = new AuthRequest("user@example.com", "password");
        when(authService.validateUser(authRequest.getEmail(), authRequest.getPassword())).thenReturn(true);
        when(jwtUtil.generateToken(authRequest.getEmail())).thenReturn("mock-jwt-token");

        // Act
        ResponseEntity<AuthResponse> response = authController.authenticate(authRequest);

        // Assert
        assertThat(response.getStatusCode().value()).isEqualTo(200);
        assertNotNull(response.getBody());
        assertEquals("Login successful", response.getBody().getMessage());
        assertEquals("mock-jwt-token", response.getBody().getToken());
    }

    @Test
    void authenticate_ShouldReturn401_WhenCredentialsAreInvalid() {
        // Arrange
        AuthRequest authRequest = new AuthRequest("user@example.com", "wrongpassword");
        when(authService.validateUser(authRequest.getEmail(), authRequest.getPassword())).thenReturn(false);

        // Act
        ResponseEntity<AuthResponse> response = authController.authenticate(authRequest);

        // Assert
        assertThat(response.getStatusCode().value()).isEqualTo(401);

        assertNotNull(response.getBody());
        assertEquals("Login failed", response.getBody().getMessage());
        assertNull(response.getBody().getToken());
    }
}
