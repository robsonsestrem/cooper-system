package br.com.empresa.coopersystem.dao;

import br.com.empresa.coopersystem.db.DataConnect;
import br.com.empresa.coopersystem.model.Associado;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author ROBSON SESTREM
 *
 */

public class AssociadoDAO {

    private final Connection connection;
    private String sql;
    public CallableStatement cstmt;

    public AssociadoDAO() {
        this.connection = DataConnect.getConexao();
        this.cstmt = null;
        this.sql = "";
    }

    public int countIntegraAssociados() throws SQLException {
        int total = 0;

        //realiza inserção e retorna quantidade
        cstmt = connection.prepareCall("{call System.sp_TotalAssociadoNaoIntegrado(?)}");
        cstmt.registerOutParameter(1, java.sql.Types.INTEGER);
        cstmt.execute();
        total = cstmt.getInt(1);
        DataConnect.fechaTudo(cstmt, connection);
        return total;
    }

    public List<Associado> associadosNaoIntegrados() throws SQLException {
        List<Associado> listaAssoc = new ArrayList<Associado>();
        cstmt = connection.prepareCall("{call System.sp_BuscaAssociadoNaoIntegrado}");
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
            Associado oAssociado = new Associado();
            oAssociado.setMatricula(rs.getInt(1));
            oAssociado.setNome(rs.getString(2));
            listaAssoc.add(oAssociado);
        }
        DataConnect.fechaTudo(rs, cstmt, connection);
        return listaAssoc;
    }

    public void insereAssocNaoIntegrado() throws SQLException {

        //realiza inserção
        cstmt = connection.prepareCall("{call System.sp_IntegraAssociadoERP}");
        cstmt.executeUpdate();
        DataConnect.fechaTudo(cstmt, connection);
    }

}
