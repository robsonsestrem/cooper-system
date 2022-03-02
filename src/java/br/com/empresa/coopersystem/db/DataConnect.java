package br.com.empresa.coopersystem.db;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import javax.swing.JOptionPane;

/**
 *
 * @author ROBSON SESTREM
 */

public class DataConnect {

    public static final Connection getConexao() {
        String url = "jdbc:sqlserver://localhost\\mssqlserver:1433;databaseName=CooperSystem;integratedSecurity=false;encrypt=false;trustServerCertificate=false";
        String user = "agrosystem";
        String senha = "agrosystem.32767";
        try {
            Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
            return DriverManager.getConnection(url, user, senha);
        } catch (ClassNotFoundException | SQLException e) {
            return null;
        }

    }

    public void testaJDBC() {

        // Verificando se o driver JDBC está instalado e pode ser utilizado
        try {
            Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
            JOptionPane.showMessageDialog(null, "Driver SQL instalado");

        } catch (java.lang.ClassNotFoundException e) {
            System.err.print("ClassNotFoundException:");
            System.err.println(e.getMessage());
            JOptionPane.showMessageDialog(null, "Falha no Driver JDBC");
        }
    }

    public static final void fechaTudo(ResultSet rs, Statement st, CallableStatement cstmt, Connection conn) {
        if (rs != null) {
            try {
                rs.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        if (st != null) {
            try {
                st.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        if (cstmt != null) {
            try {
                cstmt.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        if (conn != null) {
            try {
                conn.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    public static final void fechaTudo(Statement st, CallableStatement cstmt, Connection conn) {
        fechaTudo(null, st, cstmt, conn);
    }

    public static final void fechaTudo(ResultSet rs, CallableStatement cstmt, Connection conn) {
        fechaTudo(rs, null, cstmt, conn);
    }

    public static final void fechaTudo(ResultSet rs, Statement st, Connection conn) {
        fechaTudo(rs, st, null, conn);
    }

    public static final void fechaTudo(Statement st, Connection conn) {
        fechaTudo(null, st, null, conn);
    }

    public static final void fechaTudo(Connection conn) {
        fechaTudo(null, null, null, conn);
    }
}
