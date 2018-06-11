package gui.portal.gestion;

import java.io.IOException;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;

import modelo.actividades.StatusActividad;
import modelo.gestion.Gestion;
import modelo.gestion.SeguridadSocial;
import modelo.gestion.TipoGestion;
import util.gestion.UtilidadesGestion;

@ManagedBean
@SessionScoped
public class NuevaGestionBean
{
	// Dado que una gestión es una actividad se comparte el catálogo de status de actividades
	private List<StatusActividad>	catStatusActividad;
	private List<TipoGestion>		catTiposGestion;
	private List<SeguridadSocial>	catSeguridadSocial;

	private Gestion					gestion;
	private int						estadoPanel;
	private int						editarGestion;

	public NuevaGestionBean()
	{
		super();
		// TODO Auto-generated constructor stub
	}

	@PostConstruct
	public void postConstruct()
	{
		this.estadoPanel = 0;
	}

	public void iniciaNuevaGestion()
	{
		this.catStatusActividad = UtilidadesGestion.getCatStatusActividad();
		this.catTiposGestion = UtilidadesGestion.getCatTipoGestion();
		this.catSeguridadSocial = UtilidadesGestion.getCatSeguridadSocial();

		this.gestion = new Gestion();
		this.gestion.setStatus(this.catStatusActividad.get(0));
		this.gestion.setTipoGestion(new TipoGestion(-1, ""));

	}

	public void actionNuevaGestion()
	{
		iniciaNuevaGestion();

		try
		{
			FacesContext.getCurrentInstance().getExternalContext().redirect("/sge/portal/gestion/nuevagestion.jsf");
		}
		catch (IOException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void activarModoEdicion()
	{
		this.editarGestion = 0;

		this.gestion.updateAllDataBD();

		try
		{
			FacesContext.getCurrentInstance().getExternalContext().redirect("historialgestion.jsf");
		}
		catch (IOException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void desactivarModoEdicion()
	{
		this.editarGestion = -1;
	}

	public Gestion getGestion()
	{
		return gestion;
	}

	public void setGestion(Gestion gestion)
	{
		this.gestion = gestion;
	}

	public int getEstadoPanel()
	{
		return estadoPanel;
	}

	public void setEstadoPanel(int estadoPanel)
	{
		this.estadoPanel = estadoPanel;
	}

	public int getEditarGestion()
	{
		return editarGestion;
	}

	public void setEditarGestion(int editarGestion)
	{
		this.editarGestion = editarGestion;
	}

	public List<StatusActividad> getCatStatusActividad()
	{
		return catStatusActividad;
	}

	public void setCatStatusActividad(List<StatusActividad> catStatusActividad)
	{
		this.catStatusActividad = catStatusActividad;
	}

	public List<TipoGestion> getCatTiposGestion()
	{
		return catTiposGestion;
	}

	public void setCatTiposGestion(List<TipoGestion> catTiposGestion)
	{
		this.catTiposGestion = catTiposGestion;
	}

	public List<SeguridadSocial> getCatSeguridadSocial()
	{
		return catSeguridadSocial;
	}

	public void setCatSeguridadSocial(List<SeguridadSocial> catSeguridadSocial)
	{
		this.catSeguridadSocial = catSeguridadSocial;
	}

}
