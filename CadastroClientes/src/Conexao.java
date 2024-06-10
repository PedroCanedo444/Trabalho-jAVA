import java.sql.*;

public class Conexao {

    public static Connection conectar() {
        Connection conn = null;
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            conn = DriverManager.getConnection("jdbc:mysql://localhost/javateste", "rootjava", "rootjava");
            System.out.println("Conectou no banco de dados.");
        } catch (SQLException ex) {
            System.out.println("Erro: Não conseguiu conectar no BD.");
        } catch (ClassNotFoundException ex) {
            System.out.println("Erro: Não encontrou o driver do BD.");
        }

        return conn;
    }

    public static void desconectar(Connection conn) {
        try {
            if (conn != null && !conn.isClosed()) {
                conn.close();
                System.out.println("Desconectou do banco de dados.");
            }
        } catch (SQLException ex) {
            System.out.println("Não conseguiu desconectar do BD.");
        }
    }

    public static void xCreate(String Nome, String Endereco) throws SQLException {
        Connection conn = null;
        PreparedStatement pstm = null;
        String sql = "INSERT INTO tblCliente (Nome, Endereco) VALUES (?, ?)";
        try {
            conn = conectar();
            pstm = conn.prepareStatement(sql);
            pstm.setString(1, Nome);
            pstm.setString(2, Endereco);
            pstm.execute();
            System.out.println("Dado inserido com sucesso");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            if (pstm != null) {
                pstm.close();
            }
            desconectar(conn);
        }
    }

    public static void xListar() throws SQLException {
        Connection conn = null;
        PreparedStatement pstm = null;
        ResultSet rs = null;
        String sql = "SELECT * FROM tblcliente";
        try {
            conn = conectar();
            pstm = conn.prepareStatement(sql);
            rs = pstm.executeQuery();
            while (rs.next()) {
                System.out.println(rs.getString("Nome"));
                System.out.println(rs.getString("Endereco"));
            }
            System.out.println("Dados recuperados com sucesso");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            if (rs != null) {
                rs.close();
            }
            if (pstm != null) {
                pstm.close();
            }
            desconectar(conn);
        }
    }
}
