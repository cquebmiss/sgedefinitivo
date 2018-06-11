package modelo.gestion;

import java.util.Optional;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;

import gui.portal.gestion.NuevaGestionBean;
import util.FacesUtils;

@FacesConverter("seguridadSocialConverter")
public class SeguridadSocialConverter implements Converter
{

	@Override
	public Object getAsObject(FacesContext context, UIComponent component, String id)
	{
		NuevaGestionBean bean = (NuevaGestionBean) FacesUtils.getManagedBean("NuevaGestionBean");

		if (bean.getCatSeguridadSocial() == null || bean.getCatSeguridadSocial().isEmpty())
		{
			return null;
		}

		Optional<SeguridadSocial> res = bean.getCatSeguridadSocial().stream()
				.filter(v -> v.getIdSeguridadSocial() == Integer.parseInt(id)).findFirst();

		return res.get();

	}

	@Override
	public String getAsString(FacesContext context, UIComponent component, Object value)
	{

		if (value != null)
		{
			return "" + ((SeguridadSocial) value).getIdSeguridadSocial();
		}

		return null;
	}

}
