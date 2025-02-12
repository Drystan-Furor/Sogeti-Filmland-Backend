package sogeti.filmland.service;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import sogeti.filmland.repository.UserRepository;
import sogeti.filmland.model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Optional;

@Service
public class AuthService {
    private static final Logger logger = LoggerFactory.getLogger(AuthService.class);

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder passwordEncoder;

    public AuthService(UserRepository userRepository, BCryptPasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }


    public boolean validateUser(String email, String password) {
        Optional<User> userOptional = userRepository.findByEmail(email);

        if (userOptional.isEmpty()) {
            logger.warn("Gebruiker niet gevonden: {}", email);
            return false;
        }

        User user = userOptional.get();
        logger.info("Gebruiker gevonden: {}", user.getEmail());
        logger.info("Gehasht wachtwoord in database: {}", user.getPassword());

        boolean isPasswordValid = passwordEncoder.matches(password, user.getPassword());

        if (isPasswordValid) {
            logger.info("Wachtwoord komt overeen!");
        } else {
            logger.warn("Wachtwoord komt NIET overeen!");
        }

        return isPasswordValid;
    }
}
