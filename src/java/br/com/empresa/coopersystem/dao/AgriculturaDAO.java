package br.com.empresa.coopersystem.dao;

import br.com.empresa.coopersystem.db.DataConnect;
import br.com.empresa.coopersystem.model.Agricultura;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author ROBSON SESTREM
 */

public class AgriculturaDAO {

    private final Connection connection;
    private String sql;
    public CallableStatement cstmt;

    public AgriculturaDAO() {
        this.connection = DataConnect.getConexao();
        this.cstmt = null;
        this.sql = "";
    }

    public void adicionarProdAgricultura(Agricultura oAgricultura) throws SQLException {
        cstmt = connection.prepareCall("{call System.sp_InsereProducaoAgricola(?,?,?,?,?,?)}");
        cstmt.setString(1, oAgricultura.getoUsuario().getNomeLogin());
        cstmt.setInt(2, oAgricultura.getoProdutor().getMatricula());
        cstmt.setInt(3, oAgricultura.getAtividade().getIdAtividade());
        cstmt.setDouble(4, oAgricultura.getAreaCultivo());
        cstmt.setDouble(5, oAgricultura.getAreaArrendada());
        cstmt.setString(6, oAgricultura.getSafra());
        cstmt.executeUpdate();
        DataConnect.fechaTudo(cstmt, connection);
    }

    public void editarProdAgricultura(Agricultura oAgricultura) throws SQLException {
        cstmt = connection.prepareCall("{call System.sp_EditaProducaoAgricola(?,?)}");
        cstmt.setInt(1, oAgricultura.getIdProdAgricultura());
        cstmt.setDouble(2, oAgricultura.getAreaCultivo());
        cstmt.executeUpdate();
        DataConnect.fechaTudo(cstmt, connection);
    }

    public void excluirProdAgricultura(Agricultura oAgricultura) throws SQLException {
        cstmt = connection.prepareCall("{call System.sp_ExcluiProducaoAgricola(?)}");
        cstmt.setInt(1, oAgricultura.getIdProdAgricultura());
        cstmt.executeUpdate();
        DataConnect.fechaTudo(cstmt, connection);
    }

    public List<Agricultura> listaProdAgricultura(String nomeUsuario) throws SQLException {
        List<Agricultura> todas = new ArrayList<>();
        ResultSet rs = null;
        cstmt = connection.prepareCall("{call System.sp_ListaProducaoAgroPorLogin(?)}",
                ResultSet.TYPE_SCROLL_INSENSITIVE,
                ResultSet.CONCUR_READ_ONLY);
        cstmt.setString(1, nomeUsuario);

        boolean results = cstmt.execute();
        int rowsAffected = 0;

        // Protege contra a falta de SET NOCOUNT no procedimento armazenado
        while (results || rowsAffected != -1) {
            if (results) {
                rs = cstmt.getResultSet();
                break;
            } else {
                rowsAffected = cstmt.getUpdateCount();
            }
            results = cstmt.getMoreResults();
        }
        while (rs.next()) {
            Agricultura oAgricultura = new Agricultura();
            oAgricultura.setUsuarioSelect(rs.getString(1));
            oAgricultura.setMatriculaSelect(rs.getInt(2));
            oAgricultura.setNomeProSelect(rs.getString(3));
            oAgricultura.setCultura(rs.getString(4));
            oAgricultura.setProducaoSelect(rs.getString(5));
            oAgricultura.setAreaCultivo(rs.getDouble(6));
            oAgricultura.setAreaArrendada(rs.getDouble(7));
            oAgricultura.setDataInsert(rs.getString(8));
            oAgricultura.setIdProdAgricultura(rs.getInt(9));
            oAgricultura.setSafra(rs.getString(10));
            todas.add(oAgricultura);
        }
        DataConnect.fechaTudo(rs, cstmt, connection);
        return todas;
    }

    //para a tela "movimentacaoAgricultura" com usuários de privilégios
    public List<Agricultura> listaProdAgriculturaAdmin() throws SQLException {
        List<Agricultura> todas = new ArrayList<>();
        cstmt = connection.prepareCall("{call System.sp_ListaMovProducaoAgro}");
        ResultSet rs = cstmt.getResultSet();
        boolean results = cstmt.execute();
        int rowsAffected = 0;

        //tratando "set nocount on"
        while (results || rowsAffected != -1) {
            if (results) {
                rs = cstmt.getResultSet();
                break;
            } else {
                rowsAffected = cstmt.getUpdateCount();
            }
            results = cstmt.getMoreResults();
        }
        while (rs.next()) {
            Agricultura oAgricultura = new Agricultura();
            oAgricultura.setUsuarioSelect(rs.getString(1));
            oAgricultura.setMatriculaSelect(rs.getInt(2));
            oAgricultura.setNomeProSelect(rs.getString(3));
            oAgricultura.setCultura(rs.getString(4));
            oAgricultura.setProducaoSelect(rs.getString(5));
            oAgricultura.setAreaCultivo(rs.getDouble(6));
            oAgricultura.setAreaArrendada(rs.getDouble(7));
            oAgricultura.setDataInsert(rs.getString(8));
            oAgricultura.setIdProdAgricultura(rs.getInt(9));
            oAgricultura.setSafra(rs.getString(10));
            todas.add(oAgricultura);
        }
        DataConnect.fechaTudo(rs, cstmt, connection);
        return todas;
    }

    public boolean validarProdAgricultura(int matricula, String producao, double area, double arrendada, String anoSafra) throws SQLException {
        boolean achou;

        //verifica se existem estes registros repetidos com esses parâmetros
        cstmt = connection.prepareCall("{call System.sp_ValidaProdAgricultura(?, ?, ?, ?, ?, ?)}");
        cstmt.setInt(1, matricula);
        cstmt.setString(2, producao);
        cstmt.setDouble(3, area);
        cstmt.setDouble(4, arrendada);
        cstmt.setString(5, anoSafra);
        cstmt.registerOutParameter(6, java.sql.Types.BOOLEAN);
        cstmt.execute();
        achou = cstmt.getBoolean(6);
        DataConnect.fechaTudo(cstmt, connection);
        return achou;
    }

}
