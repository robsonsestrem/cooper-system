package br.com.empresa.coopersystem.converter;

import br.com.empresa.coopersystem.model.CulturasAgroPec;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;

/**
 *
 * @author ROBSON SESTREM
 */

@FacesConverter(forClass = CulturasAgroPec.class)
public class CulturasAgroPecConverter implements Converter {

    @Override
    public Object getAsObject(FacesContext facesContext, UIComponent uiComponent, String value) {
        if (value != null && !value.isEmpty()) {
            return (CulturasAgroPec) uiComponent.getAttributes().get(value);
        }
        return null;
    }

    @Override
    public String getAsString(FacesContext facesContext, UIComponent uiComponent, Object value) {
        if (value instanceof CulturasAgroPec) {
            CulturasAgroPec entity = (CulturasAgroPec) value;
            if (entity != null && entity instanceof CulturasAgroPec && entity.getCulturasAgroPec() != null) {
                uiComponent.getAttributes().put(entity.getCulturasAgroPec().toString(), entity);
                return entity.getCulturasAgroPec().toString();
            }
        }
        return "";
    }

}
