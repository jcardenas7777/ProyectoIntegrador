// Archivo: src/modelo/Bebida.java
package modelo;

public class Bebida extends Producto {
    private int volumenMl;

    public Bebida(String nombre, double precioBase, int volumenMl) {
        super(nombre, precioBase);
        this.volumenMl = volumenMl;
    }

    public int getVolumenMl() {
        return volumenMl;
    }

    @Override
    public String getCategoria() {
        return "Bebida (" + volumenMl + " ml)";
    }
}
