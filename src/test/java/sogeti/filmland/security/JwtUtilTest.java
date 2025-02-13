package sogeti.filmland.security;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;
import org.springframework.test.util.ReflectionTestUtils;

import static org.junit.jupiter.api.Assertions.*;

class JwtUtilTest {

    @InjectMocks
    private JwtUtil jwtUtil;

    private final String testSecret = "mySuperSecretKeyForJwtTokenValidation123!";
    private final int testExpirationMs = 1000; // 1 second for testing

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        // Inject the secret key and expiration values
        ReflectionTestUtils.setField(jwtUtil, "jwtSecret", testSecret);
        ReflectionTestUtils.setField(jwtUtil, "jwtExpirationMs", testExpirationMs);
        jwtUtil.init(); // Initialize the secret key
    }

    @Test
    void testGenerateToken() {
        String token = jwtUtil.generateToken("test@example.com");
        assertNotNull(token, "Generated token should not be null");
    }

    @Test
    void testExtractEmail() {
        String token = jwtUtil.generateToken("test@example.com");
        String extractedEmail = jwtUtil.extractEmail(token);

        assertEquals("test@example.com", extractedEmail, "Extracted email should match the input email");
    }

    @Test
    void testValidateToken_ValidToken() {
        String token = jwtUtil.generateToken("test@example.com");
        assertTrue(jwtUtil.validateToken(token), "Valid token should be accepted");
    }

    @Test
    void testValidateToken_ExpiredToken() throws InterruptedException {
        String token = jwtUtil.generateToken("test@example.com");
        Thread.sleep(testExpirationMs + 500); // Wait for token to expire

        assertFalse(jwtUtil.validateToken(token), "Expired token should be rejected");
    }

    @Test
    void testValidateToken_InvalidSignature() {
        String fakeToken = jwtUtil.generateToken("test@example.com") + "tampered";
        assertFalse(jwtUtil.validateToken(fakeToken), "Tampered token should be rejected");
    }

    @Test
    void testValidateToken_MalformedToken() {
        String malformedToken = "this.is.not.a.jwt";
        assertFalse(jwtUtil.validateToken(malformedToken), "Malformed token should be rejected");
    }

    @Test
    void testValidateToken_EmptyToken() {
        assertFalse(jwtUtil.validateToken(""), "Empty token should be rejected");
    }

    @Test
    void testValidateToken_NullToken() {
        assertFalse(jwtUtil.validateToken(null), "Null token should be rejected");
    }
}
