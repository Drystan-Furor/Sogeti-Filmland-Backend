package sogeti.filmland.service;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import sogeti.filmland.model.Member;
import sogeti.filmland.repository.MemberRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Optional;

@Service
public class AuthService {
    private static final Logger logger = LoggerFactory.getLogger(AuthService.class);

    private final MemberRepository memberRepository;
    private final BCryptPasswordEncoder passwordEncoder;

    public AuthService(MemberRepository memberRepository, BCryptPasswordEncoder passwordEncoder) {
        this.memberRepository = memberRepository;
        this.passwordEncoder = passwordEncoder;
    }


    public boolean validateUser(String email, String password) {
        Optional<Member> userOptional = memberRepository.findByEmail(email);

        if (userOptional.isEmpty()) {
            logger.warn("Gebruiker niet gevonden: {}", email);
            return false;
        }

        Member member = userOptional.get();
        logger.info("Gebruiker gevonden: {}", member.getEmail());
        logger.info("Gehasht wachtwoord in database: {}", member.getPassword());

        boolean isPasswordValid = passwordEncoder.matches(password, member.getPassword());

        if (isPasswordValid) {
            logger.info("Wachtwoord komt overeen!");
        } else {
            logger.warn("Wachtwoord komt NIET overeen!");
        }

        return isPasswordValid;
    }
}
