package modelo.climalaboral;

import java.util.Optional;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;

import gui.portal.climalaboral.PersistEncuestaBean;
import util.FacesUtils;

@FacesConverter("areaConverter")
public class AreaConverter implements Converter
{
	@Override
	public Object getAsObject(FacesContext fc, UIComponent uic, String id)
	{
		PersistEncuestaBean bean = (PersistEncuestaBean) FacesUtils.getManagedBean("persistEncuestaBean");

		if (bean.getCatAreas() == null || bean.getCatAreas().isEmpty())
		{
			return null;
		}

		Optional<Area> res = bean.getCatAreas().stream().filter(v ->
		{
			try
			{
				if (v.getIdArea() == Integer.parseInt(id))
					return true;

			}
			catch (NumberFormatException e)
			{
				return false;
			}

			return false;

		}).findFirst();

		if (res.isPresent())
		{
			return res.get();
		}

		return null;
	}

	@Override
	public String getAsString(FacesContext fc, UIComponent uic, Object o)
	{
		if (o != null)
		{
			return "" + ((Area) o).getIdArea();
		}

		return null;

	}

}
