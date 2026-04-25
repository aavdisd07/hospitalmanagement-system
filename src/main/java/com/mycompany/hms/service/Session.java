package com.mycompany.hms.service;

import com.mycompany.hms.exception.AuthException;
import com.mycompany.hms.model.Role;
import com.mycompany.hms.model.User;

public final class Session {

    private static volatile User current;

    private Session() {}

    public static void set(User u) { current = u; }
    public static User get() {
        if (current == null) throw new AuthException("Not logged in");
        return current;
    }
    public static boolean isAuthenticated() { return current != null; }
    public static void clear() { current = null; }
    public static Role role() { return get().role(); }
}
