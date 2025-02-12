package sogeti.filmland.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import sogeti.filmland.service.MemberService;

import java.io.IOException;

import static io.micrometer.common.util.StringUtils.isNotBlank;

@Slf4j
@Component
public class JwtRequestFilter extends OncePerRequestFilter {

    private final JwtUtil jwtUtil;
    private final MemberService memberService;

    public JwtRequestFilter(JwtUtil jwtUtil, MemberService memberService) {
        this.jwtUtil = jwtUtil;
        this.memberService = memberService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
            throws ServletException, IOException {
        final String authHeader = request.getHeader("Authorization");

        String email = null;
        String jwt = null;

        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            jwt = authHeader.substring(7);
        }

        if (jwt != null && jwtUtil.validateToken(jwt) ) {
            email = jwtUtil.extractEmail(jwt);
            log.info("Email: [{}]", email);
            if (isNotBlank(email)) {
                val authToken = memberService.loadUserByUsername(email);
                authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                try {
                    SecurityContextHolder.getContext().setAuthentication(authToken);
                } catch (Exception e) {
                    log.error("Error setting authentication", e);
                }
            }
        }
        chain.doFilter(request, response);
    }
}
