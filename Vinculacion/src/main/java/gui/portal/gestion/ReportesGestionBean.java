package gui.portal.gestion;

import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;

import org.primefaces.model.chart.LegendPlacement;
import org.primefaces.model.chart.LineChartModel;
import org.primefaces.model.chart.PieChartModel;

import lombok.Getter;
import lombok.Setter;
import modelo.Sesion;
import modelo.gestion.CategoriaGestion;
import modelo.gestion.Costo;
import modelo.gestion.Edad;
import modelo.gestion.Gestion;
import modelo.gestion.LugarResidencia;
import modelo.gestion.SeguridadSocial;
import modelo.gestion.Sexo;
import modelo.gestion.Solicitante;
import modelo.gestion.TipoDescuento;
import modelo.gestion.UnidadSalud;
import service.GraficasService;
import util.FacesUtils;
import util.UtilidadesCalendario;
import util.gestion.UtilidadesGestion;

@ManagedBean
@SessionScoped
@Getter
@Setter
public class ReportesGestionBean
{
	private List<Gestion>			gestionesActivas;
	private List<Gestion>			gestionesFinalizadas;
	private int						totalGestiones;
	private int						totalGestionesActivas;
	private int						totalGestionesFinalizadas;
	private String					tiempoPromedioFinalizacion;

	private List<Gestion>			allGestiones;

	// fechas de recepción
	private Date					fechaInicial;
	private Date					fechaFinal;

	// fechas de finalización
	private Date					fechaFinalizacionInicial;
	private Date					fechaFinalizacionFinal;

	// Gráficas
	private PieChartModel			modeloGraficaGeneral;
	private List<Solicitante>		solicitantes;

	private List<LugarResidencia>	lugaresResidencia;

	private List<CategoriaGestion>	categorias;
	private List<SeguridadSocial>	seguridadSocial;
	private List<Sexo>				sexos;
	private List<Edad>				edades;
	private List<TipoDescuento>		tiposDescuento;
	private List<UnidadSalud>		atendidosEn;
	private List<UnidadSalud>		referenciadosA;

	// Gráfica lineal de total de gestiones mensuales
	private LineChartModel			lineModel;

	private GraficasService			graficasService;
	private int						añoTendencia;

	private String					jsonTotalGestiones;
	private String					jsonSolicitantes;
	private String					jsonLugaresResidencia;
	private String					jsonCategorias;
	private String					jsonSeguridadSocial;
	private String					jsonSexos;
	private String					jsonEdades;
	private String					jsonTipoApoyoEconomico;
	private String					jsonAtendidosEn;
	private String					jsonReferenciadosA;

	private int						totalGestionesConAhorro;
	private BigDecimal				montoAhorado;

	private String					alturaChartSolicitantes;
	private String					alturaChartLugaresResidencia;

	public ReportesGestionBean()
	{
		super();
	}

	public LineChartModel getLineModel()
	{
		return lineModel;
	}

	public void setLineModel(LineChartModel lineModel)
	{
		this.lineModel = lineModel;
	}

	@PostConstruct
	public void postConstruct()
	{
		this.añoTendencia = LocalDate.now().getYear() - 1;
		this.graficasService = new GraficasService();
	}

	public void actionInicializaReportes()
	{
		try
		{
			ExternalContext ec = FacesContext.getCurrentInstance().getExternalContext();
			ec.redirect(ec.getRequestContextPath() + "/portal/gestion/reportesgestion.jsf");
		} catch (IOException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private void getHistorialGestiones()
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

		this.totalGestiones = this.allGestiones.size();
		this.totalGestionesActivas = this.gestionesActivas.size();
		this.totalGestionesFinalizadas = this.gestionesFinalizadas.size();

		this.modeloGraficaGeneral = new PieChartModel();

		this.modeloGraficaGeneral.set("Gestiones Activas", this.totalGestionesActivas);
		this.modeloGraficaGeneral.set("Gestiones Finalizadas", this.totalGestionesFinalizadas);

		this.modeloGraficaGeneral.setSeriesColors(UtilidadesGestion.seriesColors);
		this.modeloGraficaGeneral.setLegendPlacement(LegendPlacement.OUTSIDEGRID);
		// modeloGrafica.setAnimate(true);

		this.modeloGraficaGeneral.setLegendPosition("e");

		this.jsonTotalGestiones = this.graficasService.getChartTotalGestiones(Integer.parseInt(sesion.getIdUsuario()),
				fechaInicial, fechaFinal, fechaFinalizacionInicial, fechaFinalizacionFinal);

	}

	public void updateEstadisticas()
	{
		getHistorialGestiones();

		// se actualiza la información de las gestiones
		for (Gestion ges : this.allGestiones)
		{
			ges.updateAllDataBD();
		}

		analizaPromedioDiasFinalizacion();
		analizaSolicitantes();
		analizaLugaresResidencia();
		analizaCategorias();
		analizaSeguridadSocial();
		analizaSexos();
		analizaEdades();
		analizaApoyoEconomico();
		analizaAtendidosEn();
		analizaReferenciadosA();

	}

	public void analizaPromedioDiasFinalizacion()
	{

		int		totalGestiones		= 0;
		long	totalSumarizacion	= 0;

		for (Gestion gestion : this.allGestiones)
		{

			java.util.Date	fInicio	= gestion.getFechaRecepcion();
			java.util.Date	fFin	= gestion.getFechaFinalizacion();

			if (fFin == null)
			{
				continue;
			}

			LocalDate	fInicioLocalDate	= UtilidadesCalendario.convertUtilDateToLocalDate(fInicio);
			LocalDate	fFinLocalDate		= UtilidadesCalendario.convertUtilDateToLocalDate(fFin);

			totalSumarizacion += fInicioLocalDate.until(fFinLocalDate, ChronoUnit.DAYS);
			totalGestiones++;

		}

		this.tiempoPromedioFinalizacion = "" + (int) (totalSumarizacion / totalGestiones);

	}

	public void analizaSolicitantes()
	{
		this.solicitantes = this.graficasService.getEstadisticaSolicitantes(this.allGestiones);
		this.jsonSolicitantes = this.graficasService.getChartSolicitantes(solicitantes);

		this.alturaChartSolicitantes = "" + (100 + (28 * this.solicitantes.size())) + "px";
	}

	public void analizaLugaresResidencia()
	{
		this.lugaresResidencia = this.graficasService.getEstadisticaLugarResidencia(this.allGestiones);
		this.jsonLugaresResidencia = this.graficasService.getChartLugarResidencia(this.lugaresResidencia);

		this.alturaChartLugaresResidencia = "" + (100 + (28 * this.lugaresResidencia.size())) + "px";
	}

	public void analizaCategorias()
	{
		this.categorias = this.graficasService.getEstadisticaCategorias(this.allGestiones);
		this.jsonCategorias = this.graficasService.getChartCategorias(this.categorias);
	}

	public void analizaSeguridadSocial()
	{
		this.seguridadSocial = this.graficasService.getEstadisticaSeguridadSocial(this.allGestiones);
		this.jsonSeguridadSocial = this.graficasService.getChartSeguridadSocial(this.seguridadSocial);
	}

	public void analizaSexos()
	{
		this.sexos = this.graficasService.getEstadisticaSexos(this.allGestiones);
		this.jsonSexos = this.graficasService.getChartSexos(this.sexos);
	}

	public void analizaEdades()
	{
		this.edades = this.graficasService.getEstadisticaEdades(this.allGestiones);
		this.jsonEdades = this.graficasService.getChartEdades(this.edades);
	}

	public void analizaApoyoEconomico()
	{
		this.tiposDescuento = this.graficasService.getEstadisticaTipoDescuento(this.allGestiones);
		this.jsonTipoApoyoEconomico = this.graficasService.getChartApoyoEconomico(this.tiposDescuento);

		this.totalGestionesConAhorro = 0;
		this.montoAhorado = BigDecimal.valueOf(0);

		Costo costo;

		// Analiza las estadísticas generales de ahorros
		for (Gestion gestion : this.allGestiones)
		{
			costo = gestion.getCosto();

			if (costo.getCostoOriginal() != null && costo.getCostoOriginal().compareTo(BigDecimal.ZERO) > 0
					|| costo.getTotalAPagar() != null && costo.getTotalAPagar().compareTo(BigDecimal.ZERO) > 0
					|| costo.getTipoDescuento() != null)
			{
				this.totalGestionesConAhorro++;
			}

			if (costo.getCostoOriginal() != null && costo.getCostoOriginal().compareTo(BigDecimal.ZERO) > 0
					&& costo.getTotalAPagar() != null && costo.getTotalAPagar().compareTo(BigDecimal.ZERO) > 0)
			{
				this.montoAhorado = this.montoAhorado.add((costo.getCostoOriginal().subtract(costo.getTotalAPagar())));
			}

		}

	}

	public void analizaAtendidosEn()
	{
		this.atendidosEn = this.graficasService.getEstadisticaCentroAtencion(this.allGestiones, 0);
		this.jsonAtendidosEn = this.graficasService.getChartCentroAtencion(this.atendidosEn);
	}

	public void analizaReferenciadosA()
	{
		this.referenciadosA = this.graficasService.getEstadisticaCentroAtencion(this.allGestiones, 1);
		this.jsonReferenciadosA = this.graficasService.getChartCentroAtencion(this.referenciadosA);
	}

}
