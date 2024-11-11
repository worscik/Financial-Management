package pl.financemanagement.PasswordTools;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service()
public class PasswordService {

    private final PasswordEncoder passwordEncoder;

    public PasswordService(PasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;
    }

    public String hashPassword(String plainPassword) {
        return passwordEncoder.encode(plainPassword);
    }

    public boolean verifyPassword(String plainPassword, String hashedPassword) {
        return passwordEncoder.matches(plainPassword, hashedPassword);
    }

}
