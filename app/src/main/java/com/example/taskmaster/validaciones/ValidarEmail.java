package com.example.taskmaster.validaciones;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ValidarEmail {

    private static final String EMAIL_PATTERN =
            "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";

    private static final Pattern pattern = Pattern.compile(EMAIL_PATTERN);

    public static boolean emailValido(String email) {
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }
}

