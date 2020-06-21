package br.com.empresa.coopersystem.controller;

import br.com.empresa.coopersystem.dao.MapsDAO;
import br.com.empresa.coopersystem.model.Maps;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import org.primefaces.model.map.DefaultMapModel;
import org.primefaces.model.map.LatLng;
import org.primefaces.model.map.MapModel;
import org.primefaces.model.map.Marker;

/**
 *
 * @author ROBSON SESTREM
 */

@ManagedBean(name = "MBmapaProdutores")
public class MapaProdutoresBean implements Serializable {

    private MapModel simpleModel;
    private boolean validaAcesso = false;
    @ManagedProperty(value = "#{MBlogin}")
    private LoginBean oLoginBean;

    /*    
     Aplicado as regras de permissão 
     */
    public String informativoPermissao() {
        for (int i = 0; i < oLoginBean.acessos.size(); i++) {
            if (oLoginBean.acessos.get(i).getGrupoAcesso().equalsIgnoreCase("GERENTEADM")
                    || oLoginBean.acessos.get(i).getGrupoAcesso().equalsIgnoreCase("ADMIN")
                    || oLoginBean.acessos.get(i).getGrupoAcesso().equalsIgnoreCase("DIRETORIA")) {
                validaAcesso = true;
            }
        }
        if (!validaAcesso) {
            return "Usuário sem privilégios nesta página.";
        } else {
            return null;
        }
    }

    @PostConstruct
    public void init() {
        //necessário validar para levar a informação à propriedade "rendered"
        for (int i = 0; i < oLoginBean.acessos.size(); i++) {
            if (oLoginBean.acessos.get(i).getGrupoAcesso().equalsIgnoreCase("GERENTEADM")
                    || oLoginBean.acessos.get(i).getGrupoAcesso().equalsIgnoreCase("ADMIN")
                    || oLoginBean.acessos.get(i).getGrupoAcesso().equalsIgnoreCase("DIRETORIA")) {
                validaAcesso = true;
            }
        }
        /*    
          Aplicado as regras de permissão 
         */
        if (validaAcesso) {
            simpleModel = new DefaultMapModel();
            List<Maps> listaCoordenadas = new ArrayList<>();
            MapsDAO busca = new MapsDAO();
            try {
                listaCoordenadas = busca.listCoordenadasProd();
                for (int i = 0; i < listaCoordenadas.size(); i++) {
                    //double latitude = Double.parseDouble(listaCoordenadas.get(i).getLatitude());
                    //double longitude = Double.parseDouble(listaCoordenadas.get(i).getLongitude());
                    Double latitude = Double.valueOf(listaCoordenadas.get(i).getLatitude());
                    Double longitude = Double.valueOf(listaCoordenadas.get(i).getLongitude());
                    //seta as coordenadas
                    LatLng coord = new LatLng(latitude, longitude);
                    //seta os marcadores
                    simpleModel.addOverlay(new Marker(coord, "Cep: " + listaCoordenadas.get(i).getCep() + "\nTotal de Produtores: " + listaCoordenadas.get(i).getTotalPessoaPorCep()));
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }

    public boolean isValidaAcesso() {
        return validaAcesso;
    }

    public void setValidaAcesso(boolean validaAcesso) {
        this.validaAcesso = validaAcesso;
    }

    public LoginBean getoLoginBean() {
        return oLoginBean;
    }

    public void setoLoginBean(LoginBean oLoginBean) {
        this.oLoginBean = oLoginBean;
    }

    public MapModel getSimpleModel() {
        return simpleModel;
    }
}
