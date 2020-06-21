package br.com.empresa.coopersystem.dao;

import br.com.empresa.coopersystem.db.DataConnect;
import br.com.empresa.coopersystem.model.Usuario;
import br.com.empresa.coopersystem.model.UsuarioGrupoAcesso;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author ROBSON SESTREM
 */

public class LoginDAO {

    private final Connection connection;
    private String sql;

    public LoginDAO() {
        this.connection = DataConnect.getConexao();
        this.sql = "";
    }

    public static boolean validate(String nomeLogin, String senha) throws SQLException {
        Connection con = null;
        PreparedStatement ps = null;
        boolean verifica = false;

        con = DataConnect.getConexao();
        ps = con.prepareStatement("Select NomeLogin, Senha from System.UsuarioSistema where NomeLogin = ? and Senha = ? and Situacao = 1");
        ps.setString(1, nomeLogin);
        ps.setString(2, senha);

        ResultSet rs = ps.executeQuery();

        if (rs.next()) {
            verifica = true;
        }
        DataConnect.fechaTudo(rs, ps, con);

        return verifica;
    }

    //carrega as informações de usuário para tratar permissões
    public Usuario usuarioDB(String Nomechave) throws SQLException {
        Usuario usuarioDB = null;
        sql = "select t1.NomeLogin, t1.Senha, t1.Situacao, t1.Email from System.UsuarioSistema as t1 "
                + "where t1.NomeLogin = '" + Nomechave + "' ";
        PreparedStatement stmt = connection.prepareStatement(sql);
        ResultSet rs = stmt.executeQuery();
        while (rs.next()) {
            usuarioDB = new Usuario();
            usuarioDB.setNomeLogin(rs.getString(1));
            usuarioDB.setSenha(rs.getString(2));
            usuarioDB.setSituacao(rs.getBoolean(3));
            usuarioDB.setEmail(rs.getString(4));
        }
        DataConnect.fechaTudo(rs, stmt, connection);

        return usuarioDB;
    }

    //traz a lista de acessos para determinado usuário
    public List<UsuarioGrupoAcesso> buscaGrupoAcesso(String nomeLogin) throws SQLException {
        List<UsuarioGrupoAcesso> gruposUsuario = new ArrayList<>();
        sql = "select t1.Grupo from System.UsuarioGrupoAcesso as t1 "
                + "where t1.NomeLogin = '" + nomeLogin + "' ";
        PreparedStatement stmt = connection.prepareStatement(sql);
        ResultSet rs = stmt.executeQuery();
        while (rs.next()) {
            UsuarioGrupoAcesso oUsuarioGrupoAcesso = new UsuarioGrupoAcesso();
            oUsuarioGrupoAcesso.setGrupoAcesso(rs.getString(1));
            gruposUsuario.add(oUsuarioGrupoAcesso);
        }
        DataConnect.fechaTudo(rs, stmt, connection);
        return gruposUsuario;
    }
}
