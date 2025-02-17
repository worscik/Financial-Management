package pl.financemanagement.PasswordTools;

public interface PasswordService {

    String hashPassword(String plainPassword);

    boolean verifyPassword(String plainPassword, String hashedPassword);

}
