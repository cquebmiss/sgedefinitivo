package modelo.gestion;

import java.util.Optional;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;

import gui.portal.gestion.NuevaGestionBean;
import modelo.gestion.json.MunicipioINEGI;
import util.FacesUtils;

@FacesConverter("municipioConverter")
public class MunicipioConverter implements Converter
{

	@Override
	public Object getAsObject(FacesContext context, UIComponent component, String id)
	{
		NuevaGestionBean bean = (NuevaGestionBean) FacesUtils.getManagedBean("nuevaGestionBean");

		if (bean.getMunicipios() == null || bean.getMunicipios().isEmpty())
		{
			return null;
		}

		Optional<MunicipioINEGI> res = bean.getMunicipios().stream()
				.filter(v -> v.getCve_agem().equalsIgnoreCase(id)).findFirst();

		return res.isPresent() ?  res.get() : null;

	}

	@Override
	public String getAsString(FacesContext context, UIComponent component, Object value)
	{

		if (value != null)
		{
			return "" + ((MunicipioINEGI) value).getCve_agem();
		}

		return null;
	}

}
