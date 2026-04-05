package com.enrollmentsystem.utils;

import org.mindrot.jbcrypt.BCrypt;

public class PasswordHasher {
    public static String hash(String password) {
        return BCrypt.hashpw(password, BCrypt.gensalt(12));
    }

    public static boolean compare(String plainPassword, String hashed) {
        return BCrypt.checkpw(plainPassword, hashed);
    }
}
