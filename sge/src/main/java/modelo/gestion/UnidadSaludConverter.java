package modelo.gestion;

import java.util.Optional;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;

import gui.portal.gestion.NuevaGestionBean;
import util.FacesUtils;

@FacesConverter("unidadSaludConverter")
public class UnidadSaludConverter implements Converter
{

	@Override
	public Object getAsObject(FacesContext context, UIComponent component, String id)
	{
		NuevaGestionBean bean = (NuevaGestionBean) FacesUtils.getManagedBean("nuevaGestionBean");

		if (bean.getCatUnidadSalud() == null || bean.getCatUnidadSalud().isEmpty())
		{
			return null;
		}

		Optional<UnidadSalud> res = bean.getCatUnidadSalud().stream()
				.filter(v -> v.getIdUnidadSalud() == Integer.parseInt(id)).findFirst();

		return res.get();

	}

	@Override
	public String getAsString(FacesContext context, UIComponent component, Object value)
	{

		if (value != null)
		{
			return "" + ((UnidadSalud) value).getIdUnidadSalud();
		}

		return null;
	}

}
