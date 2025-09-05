package security;

import org.mindrot.jbcrypt.BCrypt;

public class PasswordUtil {

    // Verify plain password against the hashed password
    public static boolean verifyPassword(String plainPassword, String hashedPassword) {
        return BCrypt.checkpw(plainPassword, hashedPassword);
    }

    // Optional: Generate hashed password for new registrations
    public static String hashPassword(String plainPassword) {
        return BCrypt.hashpw(plainPassword, BCrypt.gensalt());
    }
}
