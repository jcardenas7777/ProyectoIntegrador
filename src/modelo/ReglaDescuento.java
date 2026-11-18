package modelo;

@FunctionalInterface
public interface ReglaDescuento {
    /**
     * Calcula el monto final luego de aplicar la regla de descuento.
     * @param montoTotal Monto bruto de la compra.
     * @return Monto neto a pagar.
     */
    double aplicarDescuento(double montoTotal);
}
