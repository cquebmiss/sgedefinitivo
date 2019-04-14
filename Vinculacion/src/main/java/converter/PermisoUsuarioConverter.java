package converter;

import java.util.Optional;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;

import gui.portal.AdministracionBean;
import modelo.persistence.PermisoUsuario;
import modelo.persistence.StatusUsuario;
import util.FacesUtils;

@FacesConverter("permisoUsuarioConverter")
public class PermisoUsuarioConverter implements Converter
{

	@Override
	public Object getAsObject(FacesContext context, UIComponent component, String id)
	{
		AdministracionBean bean = (AdministracionBean) FacesUtils.getManagedBean("administracionBean");

		if (bean.getCatPermisoUsuario() == null || bean.getCatPermisoUsuario().isEmpty())
		{
			return null;
		}

		Optional<PermisoUsuario> res = bean.getCatPermisoUsuario().stream()
				.filter(v -> v.getIdPermisoUsuario() == Integer.parseInt(id)).findFirst();

		return res.get();
	}

	@Override
	public String getAsString(FacesContext context, UIComponent component, Object value)
	{
		if (value != null)
		{
			return "" + ((PermisoUsuario) value).getIdPermisoUsuario();
		}

		return null;
	}

}