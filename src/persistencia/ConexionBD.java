package persistencia;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class ConexionBD {
    private static final String URL_BD = "jdbc:sqlite:tienda_local.db";

    private static final String SQL_CREAR_TABLA =
            "CREATE TABLE IF NOT EXISTS registro_pedidos (" +
            "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
            "subtotal REAL, " +
            "total_final REAL)";

    public String verificarConexion() {
        try (Connection conexion = DriverManager.getConnection(URL_BD);
             Statement sentencia = conexion.createStatement()) {

            sentencia.execute(SQL_CREAR_TABLA);
            return "[BD OK] Conexión exitosa a la base de datos local (tienda_local.db).";

        } catch (SQLException e) {
            return "[BD ERROR] No se pudo conectar a la base de datos local.\n" +
                   "Detalle técnico: " + e.getMessage();
        }
    }

    public String guardarPedido(double subtotal, double totalFinal) {
        String sqlInsert = "INSERT INTO registro_pedidos (subtotal, total_final) VALUES (?, ?)";
        String sqlCount = "SELECT COUNT(*) AS total FROM registro_pedidos";

        try (Connection conexion = DriverManager.getConnection(URL_BD);
             Statement stmt = conexion.createStatement();
             PreparedStatement pstmt = conexion.prepareStatement(sqlInsert)) {

            stmt.execute(SQL_CREAR_TABLA);

            pstmt.setDouble(1, subtotal);
            pstmt.setDouble(2, totalFinal);
            pstmt.executeUpdate();

            try (ResultSet rs = stmt.executeQuery(sqlCount)) {
                if (rs.next()) {
                    return "[BD OK] Pedido guardado en SQLite. Total de registros en BD: " + rs.getInt("total");
                }
            }
            return "[BD OK] Pedido guardado correctamente.";

        } catch (SQLException e) {
            return "[BD ERROR] No se pudo guardar el pedido: " + e.getMessage();
        }
    }
}

