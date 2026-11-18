package modelo;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Pedido {
    private int idPedido;
    private List<Producto> productos;

    public Pedido(int idPedido) {
        this.idPedido = idPedido;
        this.productos = new ArrayList<>();
    }

    public int getIdPedido() {
        return idPedido;
    }

    public void agregarProducto(Producto producto) {
        if (producto != null) {
            this.productos.add(producto);
        }
    }

    public List<Producto> getProductos() {
        return Collections.unmodifiableList(productos);
    }

    /**
     * Suma el precio base de todos los productos incluidos en el pedido.
     * @return Monto bruto total del pedido.
     */
    public double calcularSubtotal() {
        double subtotal = 0.0;
        for (Producto p : productos) {
            subtotal += p.getPrecioBase();
        }
        return subtotal;
    }

    /**
     * Aplica una regla de descuento (expresión lambda) sobre el subtotal del pedido.
     * Cumple el principio Abierto/Cerrado (OCP) de SOLID.
     * @param regla Implementación funcional del descuento.
     * @return Monto final a pagar.
     */
    public double calcularTotalConDescuento(ReglaDescuento regla) {
        double subtotal = calcularSubtotal();
        return regla.aplicarDescuento(subtotal);
    }
}
