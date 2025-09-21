import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import javax.swing.JOptionPane;

public class conectaDAO {
    private static final String URL = "jdbc:mysql://localhost:3306/";
    private static final String USER = "root";
    private static final String PASSWORD = "2008";

    static {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            JOptionPane.showMessageDialog(null, "Driver JDBC não encontrado!");
        }
    }

    public Connection connectDB() {
        try {
            // Tenta conectar ao banco uc11
            Connection conn = DriverManager.getConnection(URL + "uc11", USER, PASSWORD);
            return conn;
        } catch (SQLException e) {
            // Se o banco não existir, cria ele
            if (e.getMessage().contains("Unknown database")) {
                try {
                    Connection tempConn = DriverManager.getConnection(URL, USER, PASSWORD);
                    var stmt = tempConn.createStatement();
                    stmt.execute("CREATE DATABASE uc11");
                    stmt.execute("USE uc11");
                    stmt.execute("CREATE TABLE produtos (" +
                                "id BIGINT UNSIGNED NOT NULL AUTO_INCREMENT, " +
                                "nome TEXT, valor INT, status TEXT, " +
                                "PRIMARY KEY (id))");
                    stmt.close();
                    tempConn.close();
                    
                    // Conecta ao banco recém-criado
                    return DriverManager.getConnection(URL + "uc11", USER, PASSWORD);
                } catch (SQLException ex) {
                    JOptionPane.showMessageDialog(null, "Erro ao criar banco: " + ex.getMessage());
                    return null;
                }
            }
            JOptionPane.showMessageDialog(null, "Erro de conexão: " + e.getMessage());
            return null;
        }
    }
}