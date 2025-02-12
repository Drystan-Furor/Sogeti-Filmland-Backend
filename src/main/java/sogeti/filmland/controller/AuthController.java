package sogeti.filmland.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import sogeti.filmland.dto.AuthRequest;
import sogeti.filmland.dto.AuthResponse;
import sogeti.filmland.security.JwtUtil;
import sogeti.filmland.service.AuthService;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthService authService;
    private final JwtUtil jwtUtil;

    public AuthController(AuthService authService, JwtUtil jwtUtil) {
        this.authService = authService;
        this.jwtUtil = jwtUtil;
    }

    @PostMapping("/authenticate")
    public ResponseEntity<AuthResponse> authenticate(@RequestBody AuthRequest authRequest) {
        boolean isAuthenticated = authService.validateUser(authRequest.getEmail(), authRequest.getPassword());

        if (!isAuthenticated) {
            return ResponseEntity.status(401).body(new AuthResponse("Login failed", null));
        }

        // BONUS: JWT Token genereren
        String token = jwtUtil.generateToken(authRequest.getEmail());
        return ResponseEntity.ok(new AuthResponse("Login successful", token));
    }
}
