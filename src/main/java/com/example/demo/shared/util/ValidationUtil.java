package com.example.demo.shared.util;

import java.util.regex.Pattern;

public final class ValidationUtil {

    // Patrones de validación
    private static final Pattern EMAIL_PATTERN = Pattern.compile("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$");

    private static final Pattern PHONE_PATTERN = Pattern.compile("^[+]?[0-9\\s\\-\\(\\)]+$");

    private static final Pattern PASSWORD_PATTERN = Pattern
            .compile("^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{8,}$");

    /**
     * Valida formato de email
     */
    public static boolean isValidEmail(String email) {
        if (email == null || email.trim().isEmpty()) {
            return false;
        }
        return EMAIL_PATTERN.matcher(email.trim()).matches();
    }

    /**
     * Valida formato de teléfono
     */
    public static boolean isValidPhoneNumber(String phone) {
        if (phone == null || phone.trim().isEmpty()) {
            return false;
        }
        return PHONE_PATTERN.matcher(phone.trim()).matches() && phone.trim().length() <= 15;
    }

    /**
     * Valida contraseña fuerte
     * Al menos 8 caracteres, una mayúscula, una minúscula, un número y un carácter
     * especial
     */
    public static boolean isValidPassword(String password) {
        if (password == null) {
            return false;
        }
        return PASSWORD_PATTERN.matcher(password).matches();
    }

    /**
     * Valida que una cadena no sea null, vacía o solo espacios
     */
    public static boolean isNotBlank(String value) {
        return value != null && !value.trim().isEmpty();
    }

    /**
     * Valida longitud máxima de cadena
     */
    public static boolean isValidLength(String value, int maxLength) {
        return value == null || value.length() <= maxLength;
    }

    /**
     * Valida que un número sea positivo
     */
    public static boolean isPositive(Integer value) {
        return value != null && value > 0;
    }

    /**
     * Valida que un número sea no negativo
     */
    public static boolean isNonNegative(Integer value) {
        return value != null && value >= 0;
    }

    /**
     * Valida que un número esté dentro de un rango
     */
    public static boolean isInRange(Integer value, int min, int max) {
        return value != null && value >= min && value <= max;
    }

    /**
     * Valida ID de rol
     */
    public static boolean isValidRolId(Integer rolId) {
        return rolId != null && (rolId == 1 || rolId == 2 || rolId == 3 || rolId == 4);
    }

    /**
     * Valida que una cadena contenga solo letras y espacios
     */
    public static boolean isAlphabeticWithSpaces(String value) {
        if (value == null || value.trim().isEmpty()) {
            return false;
        }
        return value.trim().matches("^[a-zA-ZáéíóúÁÉÍÓÚñÑ\\s]+$");
    }

    /**
     * Valida que una cadena contenga solo números
     */
    public static boolean isNumeric(String value) {
        if (value == null || value.trim().isEmpty()) {
            return false;
        }
        return value.trim().matches("^[0-9]+$");
    }

    private ValidationUtil() {
        // Constructor privado para evitar instanciación
    }
}