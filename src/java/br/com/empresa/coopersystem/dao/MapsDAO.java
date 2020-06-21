package br.com.empresa.coopersystem.dao;

import br.com.empresa.coopersystem.db.DataConnect;
import br.com.empresa.coopersystem.model.Maps;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author webti
 */
public class MapsDAO {

    private final Connection connection;
    private String sql;
    public CallableStatement cstmt;

    public MapsDAO() {
        this.connection = DataConnect.getConexao();
        this.cstmt = null;
        this.sql = "";
    }

    public List<Maps> listCoordenadasAssoc() throws SQLException {
        List<Maps> todos = new ArrayList<>();
        cstmt = connection.prepareCall("{call System.sp_TrataCoordenadasAssoc}");
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
            Maps oMaps = new Maps();            
            oMaps.setLatitude(rs.getString(1));
            oMaps.setLongitude(rs.getString(2));
            oMaps.setCep(rs.getString(3));
            oMaps.setTotalPessoaPorCep(rs.getString(4));
            todos.add(oMaps);
        }
        DataConnect.fechaTudo(rs, cstmt, connection);
        return todos;
    }

    public List<Maps> listCoordenadasProd() throws SQLException {
        List<Maps> todos = new ArrayList<>();
        cstmt = connection.prepareCall("{call System.sp_TrataCoordenadasProd}");
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
            Maps oMaps = new Maps();            
            oMaps.setLatitude(rs.getString(1));
            oMaps.setLongitude(rs.getString(2));
            oMaps.setCep(rs.getString(3));
            oMaps.setTotalPessoaPorCep(rs.getString(4));
            todos.add(oMaps);
        }
        DataConnect.fechaTudo(rs, cstmt, connection);
        return todos;
    }

}
