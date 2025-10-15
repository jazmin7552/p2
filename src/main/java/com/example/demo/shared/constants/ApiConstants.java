package com.example.demo.shared.constants;

public final class ApiConstants {

    // Base paths
    public static final String API_BASE_PATH = "/api";
    public static final String AUTH_PATH = "/auth";
    public static final String USUARIOS_PATH = "/usuarios";
    public static final String ROLES_PATH = "/roles";
    public static final String TELEFONOS_PATH = "/telefonos";
    public static final String MESAS_PATH = "/mesas";
    public static final String COMANDAS_PATH = "/comandas";
    public static final String PRODUCTOS_PATH = "/productos";
    public static final String CATEGORIAS_PATH = "/categorias";
    public static final String ESTADOS_PATH = "/estados";
    public static final String DASHBOARD_PATH = "/dashboard";

    // Mensajes comunes
    public static final String SUCCESS_MESSAGE = "Operación realizada exitosamente";
    public static final String CREATED_MESSAGE = "Recurso creado exitosamente";
    public static final String UPDATED_MESSAGE = "Recurso actualizado exitosamente";
    public static final String DELETED_MESSAGE = "Recurso eliminado exitosamente";

    // Mensajes de error
    public static final String NOT_FOUND_MESSAGE = "Recurso no encontrado";
    public static final String BAD_REQUEST_MESSAGE = "Solicitud incorrecta";
    public static final String UNAUTHORIZED_MESSAGE = "No autorizado";
    public static final String FORBIDDEN_MESSAGE = "Acceso denegado";
    public static final String INTERNAL_ERROR_MESSAGE = "Error interno del servidor";

    // JWT
    public static final String BEARER_PREFIX = "Bearer ";
    public static final String AUTHORIZATION_HEADER = "Authorization";

    // Paginación por defecto
    public static final int DEFAULT_PAGE_SIZE = 10;
    public static final int DEFAULT_PAGE_NUMBER = 0;

    // Límites
    public static final int MAX_NOMBRE_LENGTH = 50;
    public static final int MAX_EMAIL_LENGTH = 100;
    public static final int MAX_TELEFONO_LENGTH = 15;
    public static final int MAX_UBICACION_LENGTH = 50;
    public static final int MAX_ESTADO_LENGTH = 10;

    private ApiConstants() {
        // Constructor privado para evitar instanciación
    }
}