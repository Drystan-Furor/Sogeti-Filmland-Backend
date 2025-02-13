package sogeti.filmland.service;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import sogeti.filmland.model.Member;
import sogeti.filmland.repository.MemberRepository;

import java.util.Collections;
import java.util.List;

@Service
public class MemberService {

    private final MemberRepository memberRepository;

    public MemberService(MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
    }


    @Transactional(readOnly = true)
    public UsernamePasswordAuthenticationToken loadUserByUsername(String email) throws UsernameNotFoundException {
        Member member = memberRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with email: " + email));

        UserDetails userDetails = new User(member.getEmail(), member.getPassword(),
                List.of(new SimpleGrantedAuthority("ROLE_USER")));

        return new UsernamePasswordAuthenticationToken(member.getId(), null, Collections.emptyList());
    }
}


// @Service
//public class MemberService implements UserDetailsService {
//
//    private final MemberRepository memberRepository;
//
//    public MemberService(MemberRepository memberRepository) {
//        this.memberRepository = memberRepository;
//    }
//
//    // Belangrijk: deze methode wordt gebruikt door Spring Security
//    @Override
//    @Transactional(readOnly = true)
//    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
//        Member member = memberRepository.findByEmail(email)
//                .orElseThrow(() -> new UsernameNotFoundException("User not found with email: " + email));
//
//        return new User(
//                member.getEmail(),
//                member.getPassword(),
//                List.of(new SimpleGrantedAuthority("ROLE_USER")) // Zorgt ervoor dat de gebruiker de juiste rol heeft
//        );
//    }
//
//    // Behoud de bestaande functionaliteit voor authenticatie elders in de codebase
//    @Transactional(readOnly = true)
//    public UsernamePasswordAuthenticationToken authenticateUserByEmail(String email) throws UsernameNotFoundException {
//        UserDetails userDetails = loadUserByUsername(email);
//
//        return new UsernamePasswordAuthenticationToken(
//                userDetails.getUsername(),
//                userDetails.getPassword(),
//                userDetails.getAuthorities() // Gebruik hier de juiste authorities
//        );
//    }
//}