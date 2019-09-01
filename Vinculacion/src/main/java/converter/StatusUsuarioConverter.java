package converter;

import java.util.Optional;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;

import gui.portal.AdministracionBean;
import persistence.dynamodb.StatusUsuario;
import util.FacesUtils;

@FacesConverter("statusUsuarioConverter")
public class StatusUsuarioConverter implements Converter
{

	@Override
	public Object getAsObject(FacesContext context, UIComponent component, String id)
	{
		AdministracionBean bean = (AdministracionBean) FacesUtils.getManagedBean("administracionBean");

		if (bean.getCatStatusUsuario() == null || bean.getCatStatusUsuario().isEmpty())
		{
			return null;
		}

		Optional<StatusUsuario> res = bean.getCatStatusUsuario().stream()
				.filter(v -> v.getIdStatusUsuario() == Integer.parseInt(id)).findFirst();

		return res.get();
	}

	@Override
	public String getAsString(FacesContext context, UIComponent component, Object value)
	{
		if (value != null)
		{
			return "" + ((StatusUsuario) value).getIdStatusUsuario();
		}

		return null;
	}

}