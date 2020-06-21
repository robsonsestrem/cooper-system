package br.com.empresa.coopersystem.dao;

import br.com.empresa.coopersystem.model.Produtor;
import br.com.empresa.coopersystem.db.DataConnect;
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

public class ProdutorDAO {

    private final Connection connection;
    private String sql;
    public CallableStatement cstmt;

    public ProdutorDAO() {
        this.connection = DataConnect.getConexao();
        this.cstmt = null;
        this.sql = "";
    }

    public List<Produtor> buscaCompletaProdutores(String matriculaNome) throws SQLException {
        List<Produtor> listaProd = new ArrayList<Produtor>();
        ResultSet rs = null;
        cstmt = connection.prepareCall("{call System.sp_PesquisaProdNomeMatricula(?)}",
                ResultSet.TYPE_SCROLL_INSENSITIVE,
                ResultSet.CONCUR_READ_ONLY);
        cstmt.setString("nomeMatricula", matriculaNome);
        //ResultSet rs = cstmt.getResultSet();
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
            Produtor oProdutor = new Produtor();
            oProdutor.setMatricula(rs.getInt(1));
            oProdutor.setNome(rs.getString(2));
            listaProd.add(oProdutor);
        }
        DataConnect.fechaTudo(rs, cstmt, connection);
        return listaProd;
    }

    public int countIntegraProdutores() throws SQLException {
        int total = 0;
        
        //realiza inserção e retorna quantidade
        cstmt = connection.prepareCall("{call System.sp_TotalProdNaoIntegrado(?)}");
        cstmt.registerOutParameter(1, java.sql.Types.INTEGER);
        cstmt.execute();
        total = cstmt.getInt(1);
        DataConnect.fechaTudo(cstmt, connection);
        return total;
    }

    public List<Produtor> produtoresNaoIntegrados() throws SQLException {
        List<Produtor> listaProd = new ArrayList<Produtor>();
        cstmt = connection.prepareCall("{call System.sp_BuscaProdNaoIntegrado}");
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
            Produtor oProdutor = new Produtor();
            oProdutor.setMatricula(rs.getInt(1));
            oProdutor.setNome(rs.getString(2));
            listaProd.add(oProdutor);
        }
        DataConnect.fechaTudo(rs, cstmt, connection);
        return listaProd;
    }

    public void insereProdNaoIntegrado() throws SQLException {
        
        //realiza inserção
        cstmt = connection.prepareCall("{call System.sp_IntegraProdutorERP}");
        cstmt.executeUpdate();
        DataConnect.fechaTudo(cstmt, connection);
    }

}
