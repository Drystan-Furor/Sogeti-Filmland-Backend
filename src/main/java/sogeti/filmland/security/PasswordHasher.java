package sogeti.filmland.security;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class PasswordHasher {
    public static void main(String[] args) {
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        String hashedPassword = encoder.encode("filmland");

        System.out.println("Gehasht wachtwoord: " + hashedPassword); //$2a$10$HtSvAyCN0d4yZMSZ4bEFwuteGxs5RDrWNV.s/Ls2fFB9EPnMYwRei
    }
}
