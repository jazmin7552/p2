package com.example.demo.shared.util;

import java.math.BigDecimal;
import java.math.RoundingMode;

public final class PriceUtil {

    // Factor de conversión entre centavos y pesos
    private static final BigDecimal CENTAVOS_TO_PESOS = new BigDecimal("0.01");
    private static final BigDecimal PESOS_TO_CENTAVOS = new BigDecimal("100");

    /**
     * Convierte centavos a pesos
     * 
     * @param centavos Valor en centavos
     * @return Valor en pesos
     */
    public static BigDecimal centavosToPesos(Integer centavos) {
        if (centavos == null) {
            return BigDecimal.ZERO;
        }
        return new BigDecimal(centavos).multiply(CENTAVOS_TO_PESOS);
    }

    /**
     * Convierte pesos a centavos
     * 
     * @param pesos Valor en pesos
     * @return Valor en centavos
     */
    public static Integer pesosToCentavos(BigDecimal pesos) {
        if (pesos == null) {
            return 0;
        }
        return pesos.multiply(PESOS_TO_CENTAVOS).intValue();
    }

    /**
     * Convierte pesos (double) a centavos
     * 
     * @param pesos Valor en pesos como double
     * @return Valor en centavos
     */
    public static Integer pesosToCentavos(double pesos) {
        return pesosToCentavos(BigDecimal.valueOf(pesos));
    }

    /**
     * Calcula el subtotal de una línea de pedido
     * 
     * @param precioUnitario Precio unitario en centavos
     * @param cantidad       Cantidad
     * @return Subtotal en centavos
     */
    public static Integer calcularSubtotal(Integer precioUnitario, Integer cantidad) {
        if (precioUnitario == null || cantidad == null) {
            return 0;
        }
        return precioUnitario * cantidad;
    }

    /**
     * Calcula el total de una comanda
     * 
     * @param subtotales Lista de subtotales en centavos
     * @return Total en centavos
     */
    public static Integer calcularTotal(Integer... subtotales) {
        if (subtotales == null) {
            return 0;
        }
        int total = 0;
        for (Integer subtotal : subtotales) {
            if (subtotal != null) {
                total += subtotal;
            }
        }
        return total;
    }

    /**
     * Formatea un precio en centavos a string con formato de moneda
     * 
     * @param centavos Valor en centavos
     * @return String formateado (ej: "$12.50")
     */
    public static String formatPrice(Integer centavos) {
        if (centavos == null) {
            return "$0.00";
        }
        BigDecimal pesos = centavosToPesos(centavos);
        return "$" + pesos.setScale(2, RoundingMode.HALF_UP).toString();
    }

    /**
     * Valida que un precio en centavos sea válido
     * 
     * @param centavos Valor en centavos
     * @return true si es válido
     */
    public static boolean isValidPrice(Integer centavos) {
        return centavos != null && centavos >= 0;
    }

    private PriceUtil() {
        // Constructor privado para evitar instanciación
    }
}