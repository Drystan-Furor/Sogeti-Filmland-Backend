package sogeti.filmland.security;

import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

@Service
public class CustomUserDetailsService implements UserDetailsService {
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        // Dummy gebruiker (vervang dit later met database lookup)
        if ("info@filmland-assessment.nl".equals(username)) {
            return new User("info@filmland-assessment.nl", "{noop}Javaiscool90", new ArrayList<>());
        }
        throw new UsernameNotFoundException("Gebruiker niet gevonden");
    }
}
