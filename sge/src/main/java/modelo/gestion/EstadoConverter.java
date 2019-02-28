package modelo.gestion;

import java.util.Optional;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;

import gui.portal.gestion.NuevaGestionBean;
import modelo.gestion.json.EstadoINEGI;
import util.FacesUtils;

@FacesConverter("estadoConverter")
public class EstadoConverter implements Converter
{

	@Override
	public Object getAsObject(FacesContext context, UIComponent component, String id)
	{
		NuevaGestionBean bean = (NuevaGestionBean) FacesUtils.getManagedBean("nuevaGestionBean");

		if (bean.getEstados() == null || bean.getEstados().isEmpty())
		{
			return null;
		}

		Optional<EstadoINEGI> res = bean.getEstados().stream()
				.filter(v -> v.getCve_agee().equalsIgnoreCase(id)).findFirst();

		return res.isPresent() ?  res.get() : null;

	}

	@Override
	public String getAsString(FacesContext context, UIComponent component, Object value)
	{

		if (value != null)
		{
			return "" + ((EstadoINEGI) value).getCve_agee();
		}

		return null;
	}

}
