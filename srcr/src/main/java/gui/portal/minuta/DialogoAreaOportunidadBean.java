package gui.portal.minuta;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;

import org.primefaces.PrimeFaces;

import modelo.minutas.Acuerdo;
import modelo.minutas.AreaOportunidad;
import util.FacesUtils;

@ManagedBean
@ViewScoped
public class DialogoAreaOportunidadBean
{

	private AreaOportunidad areaOportunidad;

	public DialogoAreaOportunidadBean()
	{
		super();
	}

	@PostConstruct
	public void postConstruct()
	{
		addAreaOportunidadNuevo();
	}

	public void addAreaOportunidadNuevo()
	{
		MinutaBean minutaBean = (MinutaBean) FacesUtils.getManagedBean("minutaBean");
		this.areaOportunidad = new AreaOportunidad(-1, minutaBean.getMinuta(), "", "", -1);
	}

	public void actionGuardarAreaOportunidad()
	{
		MinutaBean minutaBean = (MinutaBean) FacesUtils.getManagedBean("minutaBean");
		boolean correcto = true;

		if (this.areaOportunidad.getOrden() == -1)
		{
			this.areaOportunidad.setMinuta(minutaBean.getMinuta());

			if (!minutaBean.getMinuta().addAreaOportunidadBD(this.areaOportunidad))
			{
				correcto = false;
			}
		}
		else
		{
			if (!minutaBean.getMinuta().updateAreaOportunidadBD(this.areaOportunidad))
			{
				correcto = false;
			}
		}

		PrimeFaces.current().ajax().addCallbackParam("AreaOportunidadAñadido", correcto);

	}

	public AreaOportunidad getAreaOportunidad()
	{
		return areaOportunidad;
	}

	public void setAreaOportunidad(AreaOportunidad areaOportunidad)
	{
		this.areaOportunidad = areaOportunidad;
	}

}
