package br.com.empresa.coopersystem.dao;

import br.com.empresa.coopersystem.model.Atividade;
import br.com.empresa.coopersystem.db.DataConnect;
import br.com.empresa.coopersystem.model.CulturasAgroPec;
import br.com.empresa.coopersystem.model.TipoAtividade;
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

public class AtividadeDAO {

    private final Connection connection;
    private String sql;
    public CallableStatement cstmt;

    public AtividadeDAO() {
        this.connection = DataConnect.getConexao();
        this.cstmt = null;
        this.sql = "";
    }

    public void adicionarAtividade(Atividade oAtividade) throws SQLException {
        cstmt = connection.prepareCall("{call System.sp_InsereAtividade(?,?,?,?)}");
        cstmt.setString(1, oAtividade.getTipo());
        cstmt.setString(2, oAtividade.getCultura());
        cstmt.setString(3, oAtividade.getProducao());
        cstmt.setString(4, oAtividade.getoUsuario().getNomeLogin());
        cstmt.executeUpdate();
        DataConnect.fechaTudo(cstmt, connection);
    }

    public List<Atividade> listaAtividades() throws SQLException {
        List<Atividade> listaAtividades = new ArrayList<Atividade>();
        cstmt = connection.prepareCall("{call System.sp_ListaAtividade}");
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
            Atividade oAtividade = new Atividade();
            oAtividade.setIdAtividade(rs.getInt(1));
            oAtividade.setUsuarioSelect(rs.getString(2));
            oAtividade.setTipo(rs.getString(3));
            oAtividade.setCultura(rs.getString(4));
            oAtividade.setProducao(rs.getString(5));
            listaAtividades.add(oAtividade);
        }
        DataConnect.fechaTudo(rs, cstmt, connection);
        return listaAtividades;
    }

    public boolean validaProducao(String producao) throws SQLException {
        boolean achou;

        //verifica se registro já existe
        cstmt = connection.prepareCall("{call System.sp_ValidaProdAtividade(?, ?)}");
        cstmt.setString(1, producao);
        cstmt.registerOutParameter(2, java.sql.Types.BOOLEAN);
        cstmt.execute();
        achou = cstmt.getBoolean(2);
        DataConnect.fechaTudo(cstmt, connection);
        return achou;
    }

    public List<Atividade> pesquisaAtividadeAgricola(String producao) throws SQLException {
        List<Atividade> listaAtividades = new ArrayList<Atividade>();
        ResultSet rs = null;
        cstmt = connection.prepareCall("{call System.sp_PesquisaAgricola(?)}",
                ResultSet.TYPE_SCROLL_INSENSITIVE,
                ResultSet.CONCUR_READ_ONLY);
        cstmt.setString(1, producao);

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
            Atividade oAtividade = new Atividade();
            oAtividade.setIdAtividade(rs.getInt(1));
            oAtividade.setTipo(rs.getString(2));
            oAtividade.setCultura(rs.getString(3));
            oAtividade.setProducao(rs.getString(4));
            listaAtividades.add(oAtividade);
        }
        DataConnect.fechaTudo(rs, cstmt, connection);
        return listaAtividades;
    }

    public List<Atividade> pesquisaAtividadePecuaria(String producao) throws SQLException {
        List<Atividade> listaAtividades = new ArrayList<Atividade>();
        ResultSet rs = null;
        cstmt = connection.prepareCall("{call System.sp_PesquisaPecuaria(?)}",
                ResultSet.TYPE_SCROLL_INSENSITIVE,
                ResultSet.CONCUR_READ_ONLY);
        cstmt.setString(1, producao);

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
            Atividade oAtividade = new Atividade();
            oAtividade.setIdAtividade(rs.getInt(1));
            oAtividade.setTipo(rs.getString(2));
            oAtividade.setCultura(rs.getString(3));
            oAtividade.setProducao(rs.getString(4));
            listaAtividades.add(oAtividade);
        }
        DataConnect.fechaTudo(rs, cstmt, connection);
        return listaAtividades;
    }

    public void EditarAtividade(Atividade oAtividade) throws SQLException {
        cstmt = connection.prepareCall("{call System.sp_EditaAtividade(?, ?, ?, ?)}");
        cstmt.setString(1, oAtividade.getTipo());
        cstmt.setString(2, oAtividade.getCultura());
        cstmt.setString(3, oAtividade.getProducao());
        cstmt.setInt(4, oAtividade.getIdAtividade());
        cstmt.executeUpdate();
        DataConnect.fechaTudo(cstmt, connection);
    }

    public List<TipoAtividade> buscaTipoAtividade() {
        List<TipoAtividade> tipo = new ArrayList<>();
        tipo.add(new TipoAtividade("AGRICULTURA"));
        tipo.add(new TipoAtividade("PECUARIA"));
        return tipo;
    }

    public List<CulturasAgroPec> buscaCulturasAgroPec(TipoAtividade oTipoAtividade) {
        List<CulturasAgroPec> culturaAgroPec = new ArrayList<>();
        if (oTipoAtividade.getTipo().equalsIgnoreCase("AGRICULTURA")) {
            culturaAgroPec.add(new CulturasAgroPec("CEREAIS"));
            culturaAgroPec.add(new CulturasAgroPec("FRUTICULTURA"));
            culturaAgroPec.add(new CulturasAgroPec("HORTALIÇAS"));
            culturaAgroPec.add(new CulturasAgroPec("LEGUMINOSAS"));
            culturaAgroPec.add(new CulturasAgroPec("PASTAGEM"));
            culturaAgroPec.add(new CulturasAgroPec("SILVICULTURA"));
        }
        if (oTipoAtividade.getTipo().equalsIgnoreCase("PECUARIA")) {
            culturaAgroPec.add(new CulturasAgroPec("AVICULTURA DE CORTE"));
            culturaAgroPec.add(new CulturasAgroPec("AVICULTURA DE POSTURA"));
            culturaAgroPec.add(new CulturasAgroPec("BOVINOCULTURA DE CORTE"));
            culturaAgroPec.add(new CulturasAgroPec("BOVINOCULTURA DE LEITE"));
            culturaAgroPec.add(new CulturasAgroPec("PISCICULTURA"));
            culturaAgroPec.add(new CulturasAgroPec("SUINOCULTURA"));
        }
        return culturaAgroPec;
    }

}
