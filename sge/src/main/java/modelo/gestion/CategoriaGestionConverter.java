package modelo.gestion;

import java.util.Optional;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;

import gui.portal.gestion.NuevaGestionBean;
import persistence.dynamodb.CategoriaGestionAWS;
import util.FacesUtils;

@FacesConverter("categoriaGestionConverter")
public class CategoriaGestionConverter implements Converter
{

	@Override
	public Object getAsObject(FacesContext context, UIComponent component, String id)
	{
		NuevaGestionBean bean = (NuevaGestionBean) FacesUtils.getManagedBean("nuevaGestionBean");

		if (bean.getCatCategoriaGestionAWS() == null || bean.getCatCategoriaGestionAWS().isEmpty())
		{
			return null;
		}

		Optional<CategoriaGestionAWS> res = bean.getCatCategoriaGestionAWS().stream()
				.filter(v -> v.getIdCategoriaGestion() == Integer.parseInt(id)).findFirst();

		return res.get();
	}

	@Override
	public String getAsString(FacesContext context, UIComponent component, Object value)
	{
		if (value != null)
		{
			return "" + ((CategoriaGestionAWS) value).getIdCategoriaGestion();
		}

		return null;
	}

}
