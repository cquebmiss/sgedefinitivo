package gui.portal.gestion;

import java.io.IOException;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;

import modelo.Sesion;
import modelo.gestion.Gestion;
import util.FacesUtils;
import util.gestion.UtilidadesGestion;

@ManagedBean
@ViewScoped
public class HistorialGestionBean
{
	private List<Gestion>	gestionesActivas;
	private List<Gestion>	gestionesActivasFilter;
	private Gestion			gestionActivaSelec;

	private List<Gestion>	gestionesFinalizadas;
	private List<Gestion>	gestionesFinalizadasFilter;
	private Gestion			gestinoFinalizadaSelec;

	public HistorialGestionBean()
	{
		super();
		// TODO Auto-generated constructor stub
	}

	@PostConstruct
	public void postConstruct()
	{
		getHistorialGestiones();
	}

	private void getHistorialGestiones()
	{
		Sesion sesion = (Sesion) FacesUtils.getManagedBean("Sesion");

		this.gestionesActivas = UtilidadesGestion.getGestionesActivas(Integer.parseInt(sesion.getIdUsuario()));
		this.gestionesFinalizadas = UtilidadesGestion.getGestionesFinalizadas(Integer.parseInt(sesion.getIdUsuario()));

	}

	public void actionHistorialGestiones()
	{
		getHistorialGestiones();

		try
		{
			ExternalContext ec = FacesContext.getCurrentInstance().getExternalContext();
			ec.redirect(ec.getRequestContextPath() + "/portal/gestion/historialgestion.jsf");
		}
		catch (IOException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public List<Gestion> getGestionesActivas()
	{
		return gestionesActivas;
	}

	public void setGestionesActivas(List<Gestion> gestionesActivas)
	{
		this.gestionesActivas = gestionesActivas;
	}

	public List<Gestion> getGestionesFinalizadas()
	{
		return gestionesFinalizadas;
	}

	public void setGestionesFinalizadas(List<Gestion> gestionesFinalizadas)
	{
		this.gestionesFinalizadas = gestionesFinalizadas;
	}

	public List<Gestion> getGestionesActivasFilter()
	{
		return gestionesActivasFilter;
	}

	public void setGestionesActivasFilter(List<Gestion> gestionesActivasFilter)
	{
		this.gestionesActivasFilter = gestionesActivasFilter;
	}

	public Gestion getGestionActivaSelec()
	{
		return gestionActivaSelec;
	}

	public void setGestionActivaSelec(Gestion gestionActivaSelec)
	{
		this.gestionActivaSelec = gestionActivaSelec;
	}

	public List<Gestion> getGestionesFinalizadasFilter()
	{
		return gestionesFinalizadasFilter;
	}

	public void setGestionesFinalizadasFilter(List<Gestion> gestionesFinalizadasFilter)
	{
		this.gestionesFinalizadasFilter = gestionesFinalizadasFilter;
	}

	public Gestion getGestinoFinalizadaSelec()
	{
		return gestinoFinalizadaSelec;
	}

	public void setGestinoFinalizadaSelec(Gestion gestinoFinalizadaSelec)
	{
		this.gestinoFinalizadaSelec = gestinoFinalizadaSelec;
	}

}
