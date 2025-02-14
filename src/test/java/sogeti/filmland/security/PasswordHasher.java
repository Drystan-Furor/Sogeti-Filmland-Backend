package sogeti.filmland.security;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class PasswordHasher {
    public static void main(String[] args) {
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        String hashedPassword = encoder.encode("sharesubscription");

        System.out.println("Gehasht wachtwoord: " + hashedPassword); //$2a$10$dzmLYXMupzAk/gPmLigyXevJaPbtxlwbwID0NeWKOt3oYy3X8HCd2
    }
}
