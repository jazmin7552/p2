package com.example.demo.shared.constants;

public final class EstadoConstants {

    // Estados de Mesa
    public static final String MESA_DISPONIBLE = "DISPONIBLE";
    public static final String MESA_OCUPADA = "OCUPADA";
    public static final String MESA_RESERVADA = "RESERVADA";
    public static final String MESA_MANTENIMIENTO = "MANTENIMIENTO";

    // Estados de Comanda
    public static final String COMANDA_PENDIENTE = "PENDIENTE";
    public static final String COMANDA_EN_PREPARACION = "EN PREPARACIÓN";
    public static final String COMANDA_LISTA = "LISTA";
    public static final String COMANDA_SERVIDA = "SERVIDA";
    public static final String COMANDA_PAGADA = "PAGADA";

    // Estados de Producto
    public static final String PRODUCTO_ACTIVO = "ACTIVO";
    public static final String PRODUCTO_INACTIVO = "INACTIVO";

    // IDs de estados comunes
    public static final Integer DISPONIBLE_ID = 1;
    public static final Integer OCUPADA_ID = 2;
    public static final Integer PENDIENTE_ID = 3;
    public static final Integer ACTIVO_ID = 4;

    private EstadoConstants() {
        // Constructor privado para evitar instanciación
    }
}