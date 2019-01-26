package gui.portal.gestion;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.StringJoiner;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;

import lombok.Getter;
import lombok.Setter;
import modelo.Sesion;
import modelo.gestion.Gestion;
import service.GraficasService;
import util.FacesUtils;
import util.gestion.UtilidadesGestion;

@ManagedBean
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
		Sesion sesion = (Sesion) FacesUtils.getManagedBean("Sesion");

		this.gestionesActivas = UtilidadesGestion.getGestionesActivasPorPeriodo(Integer.parseInt(sesion.getIdUsuario()),
				this.fechaInicial, this.fechaFinal, this.fechaFinalizacionInicial, this.fechaFinalizacionFinal);
		this.gestionesFinalizadas = UtilidadesGestion.getGestionesFinalizadasPorPeriodo(
				Integer.parseInt(sesion.getIdUsuario()), this.fechaInicial, this.fechaFinal,
				this.fechaFinalizacionInicial, this.fechaFinalizacionFinal);

		this.allGestiones = new ArrayList<>();
		this.allGestiones.addAll(this.gestionesActivas);
		this.allGestiones.addAll(this.gestionesFinalizadas);

		int añoTendenciaBase = 2018;
		this.añoTendencia = LocalDate.now().getYear();
		
		this.graficasService = new GraficasService();

		this.jsonTendenciaGestiones = this.graficasService.getChartMensualTotalGestionesAño(añoTendencia,
				añoTendenciaBase);

	}

}
