package br.com.empresa.coopersystem.converter;

import br.com.empresa.coopersystem.model.TipoAtividade;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;

/**
 *
 * @author ROBSON SESTREM
 */

@FacesConverter(forClass = TipoAtividade.class)
public class TipoAtividadeConverter implements Converter {

    @Override
    public Object getAsObject(FacesContext facesContext, UIComponent uiComponent, String value) {
        if (value != null && !value.isEmpty()) {
            return (TipoAtividade) uiComponent.getAttributes().get(value);
        }
        return null;
    }

    @Override
    public String getAsString(FacesContext facesContext, UIComponent uiComponent, Object value) {
        if (value instanceof TipoAtividade) {
            TipoAtividade entity = (TipoAtividade) value;
            if (entity != null && entity instanceof TipoAtividade && entity.getTipo() != null) {
                uiComponent.getAttributes().put(entity.getTipo().toString(), entity);
                return entity.getTipo().toString();
            }
        }
        return "";
    }

}
