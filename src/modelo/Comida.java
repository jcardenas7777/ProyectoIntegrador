// Archivo: src/modelo/Comida.java
package modelo;

public class Comida extends Producto {
    private boolean esCaliente;

    public Comida(String nombre, double precioBase, boolean esCaliente) {
        super(nombre, precioBase);
        this.esCaliente = esCaliente;
    }

    public boolean isEsCaliente() {
        return esCaliente;
    }

    @Override
    public String getCategoria() {
        return esCaliente ? "Comida Caliente" : "Comida Fría";
    }
}