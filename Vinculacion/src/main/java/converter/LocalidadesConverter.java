package converter;

import java.util.NoSuchElementException;
import java.util.Optional;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;

import gui.portal.AdministracionBean;
import modelo.persistence.Localidad;
import util.FacesUtils;

@FacesConverter("localidadesConverter")
public class LocalidadesConverter implements Converter
{

	@Override
	public Object getAsObject(FacesContext context, UIComponent component, String id)
	{
		AdministracionBean bean = (AdministracionBean) FacesUtils.getManagedBean("administracionBean");

		if (bean.getCatLocalidades() == null || bean.getCatLocalidades().isEmpty())
		{
			return null;
		}

		Optional<Localidad> res = bean.getCatLocalidades().stream().filter(v -> v.getIdLocalidad().equalsIgnoreCase(id))
				.findFirst();

		try
		{
			return res.get();
			
		}
		catch(NoSuchElementException e)
		{
			System.out.println("id: "+id);
			return null;
		}
		
		
	}

	@Override
	public String getAsString(FacesContext context, UIComponent component, Object value)
	{
		if (value != null)
		{
			return "" + ((Localidad) value).getIdLocalidad();
		}

		return null;
	}

}