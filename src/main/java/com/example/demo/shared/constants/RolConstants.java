package com.example.demo.shared.constants;

public final class RolConstants {

    // IDs de roles
    public static final Integer ADMIN_ID = 1;
    public static final Integer MESERO_ID = 2;
    public static final Integer COCINERO_ID = 3;
    public static final Integer CAJERO_ID = 4;

    // Nombres de roles
    public static final String ADMIN = "ADMIN";
    public static final String MESERO = "MESERO";
    public static final String COCINERO = "COCINERO";
    public static final String CAJERO = "CAJERO";

    // Descripciones
    public static final String ADMIN_DESC = "Acceso total al sistema";
    public static final String MESERO_DESC = "Gestión de comandas y mesas";
    public static final String COCINERO_DESC = "Actualización de estados de preparación";
    public static final String CAJERO_DESC = "Procesamiento de pagos";

    private RolConstants() {
        // Constructor privado para evitar instanciación
    }
}