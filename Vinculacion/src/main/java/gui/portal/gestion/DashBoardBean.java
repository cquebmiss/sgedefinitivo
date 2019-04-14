package gui.portal.gestion;

import java.util.Date;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;

import lombok.Getter;
import lombok.Setter;
import modelo.gestion.Gestion;
import service.GraficasService;

@ManagedBean(name = "deprecated")
@SessionScoped
@Getter
@Setter
public class DashBoardBean
{
	private List<Gestion>	gestionesActivas;
	private List<Gestion>	gestionesFinalizadas;
	private int				totalGestiones;
	private int				totalGestionesActivas;
	private int				totalGestionesFinalizadas;
	private String			tiempoPromedioFinalizacion;
	private int				añoTendencia;

	private List<Gestion>	allGestiones;

	// fechas de recepción
	private Date			fechaInicial;
	private Date			fechaFinal;

	// fechas de finalización
	private Date			fechaFinalizacionInicial;
	private Date			fechaFinalizacionFinal;

	private GraficasService	graficasService;
	private String			jsonEncabezadoTendenciaGestiones;
	private String			jsonTendenciaGestiones;

	@PostConstruct
	public void postConstruct()
	{
		

	}

}
