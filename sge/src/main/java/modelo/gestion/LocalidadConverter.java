package modelo.gestion;

import java.util.Optional;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;

import gui.portal.gestion.NuevaGestionBean;
import modelo.gestion.json.LocalidadINEGI;
import util.FacesUtils;

@FacesConverter("localidadConverter")
public class LocalidadConverter implements Converter
{

	@Override
	public Object getAsObject(FacesContext context, UIComponent component, String id)
	{
		NuevaGestionBean bean = (NuevaGestionBean) FacesUtils.getManagedBean("nuevaGestionBean");

		if (bean.getLocalidades() == null || bean.getLocalidades().isEmpty())
		{
			return null;
		}

		Optional<LocalidadINEGI> res = bean.getLocalidades().stream().filter(v -> v.getCve_loc().equalsIgnoreCase(id)).findFirst();

		return res.isPresent() ? res.get() : null;

	}

	@Override
	public String getAsString(FacesContext context, UIComponent component, Object value)
	{

		if (value != null)
		{
			return "" + ((LocalidadINEGI) value).getCve_loc();
		}

		return null;
	}

}
