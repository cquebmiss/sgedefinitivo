package modelo.gestion;

import java.util.Optional;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;

import gui.portal.gestion.NuevaGestionBean;
import util.FacesUtils;

@FacesConverter("tipoDescuentoConverter")
public class TipoDescuentoConverter implements Converter
{

	@Override
	public Object getAsObject(FacesContext context, UIComponent component, String id)
	{
		NuevaGestionBean bean = (NuevaGestionBean) FacesUtils.getManagedBean("nuevaGestionBean");

		if (bean.getCatTipoDescuento() == null || bean.getCatTipoDescuento().isEmpty())
		{
			return null;
		}

		Optional<TipoDescuento> res = null;

		if (!id.equalsIgnoreCase("..."))
		{
			res = bean.getCatTipoDescuento().stream().filter(v -> v.getIdTipoDescuento() == Integer.parseInt(id))
					.findFirst();
			return res.get();
		}

		return null;

	}

	@Override
	public String getAsString(FacesContext context, UIComponent component, Object value)
	{
		if (value != null)
		{
			return "" + ((TipoDescuento) value).getIdTipoDescuento();
		}

		return null;
	}

}
