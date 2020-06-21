package br.com.empresa.coopersystem.dao;

import br.com.empresa.coopersystem.db.DataConnect;
import br.com.empresa.coopersystem.model.Pecuaria;
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

public class PecuariaDAO {

    private final Connection connection;
    private String sql;
    public CallableStatement cstmt;

    public PecuariaDAO() {
        this.connection = DataConnect.getConexao();
        this.cstmt = null;
        this.sql = "";
    }

    public void adicionar(Pecuaria oPecuaria) throws SQLException {
        cstmt = connection.prepareCall("{call System.sp_InsereProducaoPecuaria(?,?,?,?)}");
        cstmt.setString(1, oPecuaria.getoUsuario().getNomeLogin());
        cstmt.setInt(2, oPecuaria.getoProdutor().getMatricula());
        cstmt.setInt(3, oPecuaria.getoAtividade().getIdAtividade());
        cstmt.setInt(4, oPecuaria.getQtdadeAnimais());
        cstmt.executeUpdate();
        DataConnect.fechaTudo(cstmt, connection);
    }

    public void editarPecuaria(Pecuaria oPecuaria) throws SQLException {
        cstmt = connection.prepareCall("{call System.sp_EditaProducaoPecuaria(?,?)}");
        cstmt.setInt(1, oPecuaria.getIdProdPecuaria());
        cstmt.setInt(2, oPecuaria.getQtdadeAnimais());
        cstmt.executeUpdate();
        DataConnect.fechaTudo(cstmt, connection);
    }

    public void excluirProPecuaria(Pecuaria oPecuaria) throws SQLException {
        cstmt = connection.prepareCall("{call System.sp_ExcluiProducaoPecuaria(?)}");
        cstmt.setInt(1, oPecuaria.getIdProdPecuaria());
        cstmt.executeUpdate();
        DataConnect.fechaTudo(cstmt, connection);
    }

    public List<Pecuaria> listaProdPecuaria(String nomeUsuario) throws SQLException {
        List<Pecuaria> todas = new ArrayList<>();
        ResultSet rs = null;
        cstmt = connection.prepareCall("{call System.sp_ListaProducaoPecPorLogin(?)}",
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
            Pecuaria oPecuaria = new Pecuaria();
            oPecuaria.setUsuarioSelect(rs.getString(1));
            oPecuaria.setMatriculaSelect(rs.getInt(2));
            oPecuaria.setNomeProselect(rs.getString(3));
            oPecuaria.setCultura(rs.getString(4));
            oPecuaria.setProducaoSelect(rs.getString(5));
            oPecuaria.setQtdadeAnimais(rs.getInt(6));
            oPecuaria.setDataInsert(rs.getString(7));
            oPecuaria.setIdProdPecuaria(rs.getInt(8));
            todas.add(oPecuaria);
        }
        DataConnect.fechaTudo(rs, cstmt, connection);
        return todas;
    }

    //para a tela "movimentacaoPecuaria" com usuários de privilégios
    public List<Pecuaria> listaProdPecuariaAdmin() throws SQLException {
        List<Pecuaria> todas = new ArrayList<>();
        cstmt = connection.prepareCall("{call System.sp_ListaMovProducaoPec}");
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
            Pecuaria oPecuaria = new Pecuaria();
            oPecuaria.setUsuarioSelect(rs.getString(1));
            oPecuaria.setMatriculaSelect(rs.getInt(2));
            oPecuaria.setNomeProselect(rs.getString(3)); 
            oPecuaria.setCultura(rs.getString(4));
            oPecuaria.setProducaoSelect(rs.getString(5));
            oPecuaria.setQtdadeAnimais(rs.getInt(6));
            oPecuaria.setDataInsert(rs.getString(7));
            oPecuaria.setIdProdPecuaria(rs.getInt(8));
            todas.add(oPecuaria);
        }
        DataConnect.fechaTudo(rs, cstmt, connection);
        return todas;
    }

    public boolean validarProdPecuaria(int matricula, String atividade, int animais) throws SQLException {
        boolean achou;
        
        //verifica se existem estes registros num período de 60 dias
        cstmt = connection.prepareCall("{call System.sp_ValidaProdPecuaria(?, ?, ?, ?)}");
        cstmt.setInt(1, matricula);
        cstmt.setString(2, atividade);
        cstmt.setInt(3, animais);
        cstmt.registerOutParameter(4, java.sql.Types.BOOLEAN);
        cstmt.execute();
        achou = cstmt.getBoolean(4);
        DataConnect.fechaTudo(cstmt, connection);
        return achou;
    }

}
