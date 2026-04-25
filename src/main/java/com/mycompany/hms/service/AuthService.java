package com.mycompany.hms.service;

import com.mycompany.hms.dao.UserDao;
import com.mycompany.hms.exception.AuthException;
import com.mycompany.hms.exception.ValidationException;
import com.mycompany.hms.model.Role;
import com.mycompany.hms.model.User;
import com.mycompany.hms.util.Validators;
import org.mindrot.jbcrypt.BCrypt;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AuthService {

    private static final Logger log = LoggerFactory.getLogger(AuthService.class);
    private static final int BCRYPT_COST = 10;

    private final UserDao users;

    public AuthService() { this(new UserDao()); }
    public AuthService(UserDao users) { this.users = users; }

    public User login(String username, String rawPassword) {
        String u = Validators.requireNonBlank(username, "Username");
        String p = Validators.requireNonBlank(rawPassword, "Password");
        User user = users.findByUsername(u)
                .orElseThrow(() -> new AuthException("Invalid credentials"));
        if (!user.enabled()) throw new AuthException("Account disabled");
        if (!BCrypt.checkpw(p, user.passwordHash())) throw new AuthException("Invalid credentials");
        log.info("User logged in: {} ({})", user.username(), user.role());
        return user;
    }

    public User register(String username, String rawPassword, Role role, Integer doctorId) {
        String u = Validators.requireNonBlank(username, "Username");
        String p = Validators.requireNonBlank(rawPassword, "Password");
        if (p.length() < 6) throw new ValidationException("Password must be at least 6 characters");
        if (users.findByUsername(u).isPresent()) {
            throw new ValidationException("Username already taken");
        }
        String hash = BCrypt.hashpw(p, BCrypt.gensalt(BCRYPT_COST));
        return users.insert(User.newUser(u, hash, role, doctorId));
    }
}
