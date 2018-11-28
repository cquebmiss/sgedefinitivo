package gui.portal.minuta;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;

import org.primefaces.PrimeFaces;

import modelo.minutas.Acuerdo;
import util.FacesUtils;

@ManagedBean
@ViewScoped
public class DialogoAcuerdoBean
{

	private Acuerdo acuerdo;

	public DialogoAcuerdoBean()
	{
		super();
	}

	@PostConstruct
	public void postConstruct()
	{
		addAcuerdoNuevo();
	}

	public void addAcuerdoNuevo()
	{
		MinutaBean minutaBean = (MinutaBean) FacesUtils.getManagedBean("minutaBean");
		this.acuerdo = new Acuerdo(-1, minutaBean.getMinuta(), "", -1, "", new java.util.Date());
	}

	public void actionGuardarAcuerdo()
	{
		MinutaBean minutaBean = (MinutaBean) FacesUtils.getManagedBean("minutaBean");
		boolean correcto = true;

		if (this.acuerdo.getOrden() == -1)
		{
			this.acuerdo.setMinuta(minutaBean.getMinuta());

			if (!minutaBean.getMinuta().addAcuerdoBD(this.acuerdo))
			{
				correcto = false;
			}
		}
		else
		{
			if (!minutaBean.getMinuta().updateAcuerdoBD(this.acuerdo))
			{
				correcto = false;
			}
		}

		PrimeFaces.current().ajax().addCallbackParam("acuerdoAñadido", correcto);

	}

	public Acuerdo getAcuerdo()
	{
		return acuerdo;
	}

	public void setAcuerdo(Acuerdo acuerdo)
	{
		this.acuerdo = acuerdo;
	}

}
