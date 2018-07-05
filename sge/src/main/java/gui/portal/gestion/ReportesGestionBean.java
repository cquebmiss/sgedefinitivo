package gui.portal.gestion;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;

import modelo.Sesion;
import modelo.gestion.CategoriaGestion;
import modelo.gestion.Gestion;
import util.FacesUtils;
import util.gestion.UtilidadesGestion;

public class ReportesGestionBean
{
	private List<Gestion>			gestionesActivas;
	private List<Gestion>			gestionesFinalizadas;
	private int						totalGestionesActivas;
	private int						totalGestionesFinalizadas;

	private List<Gestion>			allGestiones;

	private Date					fechaInicial;
	private Date					fechaFinal;
	private Map						solicitantes;
	private Map						lugaresResidencia;
	private List<CategoriaGestion>	categorias;
	private Map						edades;

	public ReportesGestionBean()
	{
		super();

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

		this.allGestiones = new ArrayList<>();
		this.allGestiones.addAll(this.gestionesActivas);
		this.allGestiones.addAll(this.gestionesFinalizadas);

		this.totalGestionesActivas = this.gestionesActivas.size();
		this.totalGestionesFinalizadas = this.gestionesFinalizadas.size();

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

	public int getTotalGestionesActivas()
	{
		return totalGestionesActivas;
	}

	public void setTotalGestionesActivas(int totalGestionesActivas)
	{
		this.totalGestionesActivas = totalGestionesActivas;
	}

	public int getTotalGestionesFinalizadas()
	{
		return totalGestionesFinalizadas;
	}

	public void setTotalGestionesFinalizadas(int totalGestionesFinalizadas)
	{
		this.totalGestionesFinalizadas = totalGestionesFinalizadas;
	}

	public List<Gestion> getAllGestiones()
	{
		return allGestiones;
	}

	public void setAllGestiones(List<Gestion> allGestiones)
	{
		this.allGestiones = allGestiones;
	}

	public Date getFechaInicial()
	{
		return fechaInicial;
	}

	public void setFechaInicial(Date fechaInicial)
	{
		this.fechaInicial = fechaInicial;
	}

	public Date getFechaFinal()
	{
		return fechaFinal;
	}

	public void setFechaFinal(Date fechaFinal)
	{
		this.fechaFinal = fechaFinal;
	}

}
