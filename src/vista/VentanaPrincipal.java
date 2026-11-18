package vista;

import javax.swing.*;

import modelo.Bebida;
import modelo.Comida;
import modelo.Pedido;
import modelo.Producto;
import modelo.ReglaDescuento;
import persistencia.ConexionBD;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class VentanaPrincipal extends JFrame {
    private JTextField txtMontoTotal;
    private JButton btnCalcularDescuento;
    private JTextArea txtAreaResultados;
    private int contadorPedidos = 1;

    // Expresión Lambda (Unidad II): Si el total > $10 aplica 15% de descuento; si no, 0%
    private final ReglaDescuento reglaDescuentoLambda = total -> (total > 10.0) ? total * 0.85 : total;

    public VentanaPrincipal() {
        setTitle("Sistema de Gestión de Pedidos y Descuentos");
        setSize(520, 380);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        inicializarComponentes();
        probarConexionInicial();
    }

    private void inicializarComponentes() {
        JPanel panelPrincipal = new JPanel(new BorderLayout(10, 10));
        panelPrincipal.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        JPanel panelSuperior = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 5));
        JLabel lblMonto = new JLabel("Monto total del pedido ($):");
        txtMontoTotal = new JTextField(12);
        btnCalcularDescuento = new JButton("Calcular Descuento");

        panelSuperior.add(lblMonto);
        panelSuperior.add(txtMontoTotal);
        panelSuperior.add(btnCalcularDescuento);

        txtAreaResultados = new JTextArea();
        txtAreaResultados.setEditable(false);
        txtAreaResultados.setFont(new Font("Monospaced", Font.PLAIN, 13));
        txtAreaResultados.setLineWrap(true);
        txtAreaResultados.setWrapStyleWord(true);
        JScrollPane scrollPane = new JScrollPane(txtAreaResultados);

        panelPrincipal.add(panelSuperior, BorderLayout.NORTH);
        panelPrincipal.add(scrollPane, BorderLayout.CENTER);
        add(panelPrincipal);

        btnCalcularDescuento.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                procesarCalculoPedido();
            }
        });
    }

    private void probarConexionInicial() {
        ConexionBD conexionBD = new ConexionBD();
        txtAreaResultados.append(conexionBD.verificarConexion() + "\n");
        txtAreaResultados.append("--------------------------------------------------\n");
    }

    private void procesarCalculoPedido() {
        String textoIngresado = txtMontoTotal.getText().trim();
        try {
            double montoIngresado = Double.parseDouble(textoIngresado);
            if (montoIngresado <= 0) {
                txtAreaResultados.append("[AVISO] El monto del pedido debe ser mayor a $0.\n\n");
                return;
            }

            // Creación del Pedido y agregado polimórfico de productos (Comida y Bebida)
            Pedido pedidoActual = new Pedido(contadorPedidos++);
            double precioComida = montoIngresado * 0.70;
            double precioBebida = montoIngresado * 0.30;

            pedidoActual.agregarProducto(new Comida("Plato Principal", precioComida, true));
            pedidoActual.agregarProducto(new Bebida("Bebida Acompañante", precioBebida, 400));

            double subtotal = pedidoActual.calcularSubtotal();
            double totalFinal = pedidoActual.calcularTotalConDescuento(reglaDescuentoLambda);
            double ahorro = subtotal - totalFinal;
            String porcentaje = (subtotal > 10.0) ? "15%" : "0%";

            StringBuilder detalle = new StringBuilder();
            detalle.append(String.format("=== PEDIDO #%d ===\n", pedidoActual.getIdPedido()));
            for (Producto prod : pedidoActual.getProductos()) {
                detalle.append(String.format(" - %s [%s]: $%.2f\n",
                        prod.getNombre(), prod.getCategoria(), prod.getPrecioBase()));
            }
            detalle.append(String.format("Subtotal: $%.2f | Descuento (%s): -$%.2f\n",
                    subtotal, porcentaje, ahorro));
            detalle.append(String.format("TOTAL A PAGAR: $%.2f\n\n", totalFinal));

            txtAreaResultados.append(detalle.toString());

            ConexionBD bd = new ConexionBD();
                txtAreaResultados.append(bd.guardarPedido(subtotal, totalFinal) + "\n\n");

        } catch (NumberFormatException ex) {
            txtAreaResultados.append("[ERROR] Ingrese un valor numérico válido (ej. 18.50).\n\n");
        }
    }
}