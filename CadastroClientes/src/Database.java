import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class Database {
    private static final String DB_URL = "jdbc:sqlite:cadastro.db";

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(DB_URL);
    }

    public static void createTables() {
        String createClienteTable = "CREATE TABLE IF NOT EXISTS cliente (" +
                "cpf TEXT PRIMARY KEY," +
                "nome TEXT NOT NULL" +
                ");";

        String createProdutoTable = "CREATE TABLE IF NOT EXISTS produto (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "nome TEXT NOT NULL," +
                "preco REAL NOT NULL," +
                "modelo TEXT NOT NULL," +
                "tamanho TEXT NOT NULL" +
                ");";

        String createVendaTable = "CREATE TABLE IF NOT EXISTS venda (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "cpf_cliente TEXT NOT NULL," +
                "produto_id INTEGER NOT NULL," +
                "FOREIGN KEY(cpf_cliente) REFERENCES cliente(cpf)," +
                "FOREIGN KEY(produto_id) REFERENCES produto(id)" +
                ");";

        try (Connection conn = getConnection(); Statement stmt = conn.createStatement()) {
            stmt.execute(createClienteTable);
            stmt.execute(createProdutoTable);
            stmt.execute(createVendaTable);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
