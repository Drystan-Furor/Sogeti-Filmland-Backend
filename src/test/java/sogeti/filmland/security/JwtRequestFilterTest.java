package sogeti.filmland.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import sogeti.filmland.service.MemberService;

import java.io.IOException;
import java.util.Collections;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

class JwtRequestFilterTest {

    @Mock
    private JwtUtil jwtUtil;

    @Mock
    private MemberService memberService;

    @Mock
    private HttpServletRequest request;

    @Mock
    private HttpServletResponse response;

    @Mock
    private FilterChain filterChain;

    private JwtRequestFilter jwtRequestFilter;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        jwtRequestFilter = new JwtRequestFilter(jwtUtil, memberService);
        SecurityContextHolder.clearContext();
    }

//    @Test
//    void testValidToken_AuthenticationSet() throws ServletException, IOException {
//        String token = "valid.jwt.token";
//        String email = "user@example.com";
//
//        when(request.getHeader("Authorization")).thenReturn("Bearer " + token);
//        when(jwtUtil.validateToken(token)).thenReturn(true);
//        when(jwtUtil.extractEmail(token)).thenReturn(email);
//
//        // Zorg ervoor dat memberService een UserDetails-object teruggeeft
//        UserDetails userDetails = new User(email, "password", Collections.emptyList());
//        when(memberService.loadUserByUsername(email)).thenReturn(userDetails);
//
//        jwtRequestFilter.doFilterInternal(request, response, filterChain);
//
//        assertNotNull(SecurityContextHolder.getContext().getAuthentication());
//        assertEquals(email, SecurityContextHolder.getContext().getAuthentication().getName());
//
//        verify(filterChain, times(1)).doFilter(request, response);
//    }

//    @Test
//    void testInvalidToken_NoAuthentication() throws ServletException, IOException {
//        String token = "invalid.jwt.token";
//
//        when(request.getHeader("Authorization")).thenReturn("Bearer " + token);
//        when(jwtUtil.validateToken(token)).thenReturn(false);
//
//        jwtRequestFilter.doFilterInternal(request, response, filterChain);
//
//        assertNull(SecurityContextHolder.getContext().getAuthentication());
//        verify(filterChain, times(1)).doFilter(request, response);
//    }

    @Test
    void testNoToken_NoAuthentication() throws ServletException, IOException {
        when(request.getHeader("Authorization")).thenReturn(null);

        jwtRequestFilter.doFilterInternal(request, response, filterChain);

        assertNull(SecurityContextHolder.getContext().getAuthentication());
        verify(filterChain, times(1)).doFilter(request, response);
    }

    @Test
    void testBlankToken_NoAuthentication() throws ServletException, IOException {
        when(request.getHeader("Authorization")).thenReturn("Bearer ");

        jwtRequestFilter.doFilterInternal(request, response, filterChain);

        assertNull(SecurityContextHolder.getContext().getAuthentication());
        verify(filterChain, times(1)).doFilter(request, response);
    }
}
