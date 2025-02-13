package sogeti.filmland.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import sogeti.filmland.model.Member;
import sogeti.filmland.repository.MemberRepository;

import java.util.Optional;

@Slf4j
@Service
public class AuthService {

    private final MemberRepository memberRepository;
    private final BCryptPasswordEncoder passwordEncoder;

    public AuthService(MemberRepository memberRepository, BCryptPasswordEncoder passwordEncoder) {
        this.memberRepository = memberRepository;
        this.passwordEncoder = passwordEncoder;
    }


    public boolean validateUser(String email, String password) {
        Optional<Member> userOptional = memberRepository.findByEmail(email);

        if (userOptional.isEmpty()) {
            log.warn("Gebruiker niet gevonden: {}", email);
            return false;
        }

        Member member = userOptional.get();
        log.info("Gebruiker gevonden: {}", member.getEmail());
        log.info("Gehasht wachtwoord in database: {}", member.getPassword());

        boolean isPasswordValid = passwordEncoder.matches(password, member.getPassword());

        if (isPasswordValid) {
            log.info("Wachtwoord komt overeen!");
        } else {
            log.warn("Wachtwoord komt NIET overeen!");
        }

        return isPasswordValid;
    }
}
