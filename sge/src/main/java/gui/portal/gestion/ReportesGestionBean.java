package gui.portal.gestion;

import java.io.IOException;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;

import org.primefaces.model.chart.Axis;
import org.primefaces.model.chart.AxisType;
import org.primefaces.model.chart.BarChartModel;
import org.primefaces.model.chart.ChartSeries;
import org.primefaces.model.chart.LegendPlacement;
import org.primefaces.model.chart.PieChartModel;

import modelo.Sesion;
import modelo.gestion.CategoriaGestion;
import modelo.gestion.Edad;
import modelo.gestion.Gestion;
import modelo.gestion.LugarResidencia;
import modelo.gestion.SeguridadSocial;
import modelo.gestion.Sexo;
import modelo.gestion.Solicitante;
import util.FacesUtils;
import util.UtilidadesCalendario;
import util.gestion.UtilidadesGestion;

@ManagedBean
@ViewScoped
public class ReportesGestionBean
{
	private List<Gestion>			gestionesActivas;
	private List<Gestion>			gestionesFinalizadas;
	private int						totalGestiones;
	private int						totalGestionesActivas;
	private int						totalGestionesFinalizadas;
	private String					tiempoPromedioFinalizacion;

	private List<Gestion>			allGestiones;

	private Date					fechaInicial;
	private Date					fechaFinal;

	//Gráficas
	private PieChartModel			modeloGraficaGeneral;
	private List<Solicitante>		solicitantes;
	private BarChartModel			modeloGraficaSolicitantes;

	private List<LugarResidencia>	lugaresResidencia;
	private BarChartModel			modeloGraficaLugarResidencia;

	private List<CategoriaGestion>	categorias;
	private List<SeguridadSocial>	seguridadSocial;
	private List<Sexo>				sexos;
	private List<Edad>				edades;

	public ReportesGestionBean()
	{
		super();

	}

	@PostConstruct
	public void postConstruct()
	{

	}

	public void actionInicializaReportes()
	{
		try
		{
			ExternalContext ec = FacesContext.getCurrentInstance().getExternalContext();
			ec.redirect(ec.getRequestContextPath() + "/portal/gestion/reportesgestion.jsf");
		}
		catch (IOException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private void getHistorialGestiones()
	{
		Sesion sesion = (Sesion) FacesUtils.getManagedBean("Sesion");

		this.gestionesActivas = UtilidadesGestion.getGestionesActivasPorPeriodo(Integer.parseInt(sesion.getIdUsuario()),
				this.fechaInicial, this.fechaFinal);
		this.gestionesFinalizadas = UtilidadesGestion.getGestionesFinalizadasPorPeriodo(
				Integer.parseInt(sesion.getIdUsuario()), this.fechaInicial, this.fechaFinal);

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
		//		modeloGrafica.setAnimate(true);

		this.modeloGraficaGeneral.setLegendPosition("e");

	}

	public void updateEstadisticas()
	{
		getHistorialGestiones();

		//se actualiza la información de las gestiones
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

	}

	public void analizaPromedioDiasFinalizacion()
	{

		int totalGestiones = 0;
		long totalSumarizacion = 0;

		for (Gestion gestion : this.allGestiones)
		{

			java.util.Date fInicio = gestion.getFechaRecepcion();
			java.util.Date fFin = gestion.getFechaFinalizacion();

			if (fFin == null)
			{
				continue;
			}

			LocalDate fInicioLocalDate = UtilidadesCalendario.convertUtilDateToLocalDate(fInicio);
			LocalDate fFinLocalDate = UtilidadesCalendario.convertUtilDateToLocalDate(fFin);

			totalSumarizacion += fInicioLocalDate.until(fFinLocalDate, ChronoUnit.DAYS);
			totalGestiones++;

		}

		this.tiempoPromedioFinalizacion = "" + (int) (totalSumarizacion / totalGestiones);

	}

	public void analizaSolicitantes()
	{

		this.solicitantes = new ArrayList<>();

		for (Gestion gestion : this.allGestiones)
		{

			Solicitante sol = new Solicitante();
			sol.setDescripcion(gestion.getSolicitadoA().trim());

			boolean encontrado = false;

			for (Solicitante solicitante : this.solicitantes)
			{
				if (solicitante.getDescripcion().equalsIgnoreCase(sol.getDescripcion()))
				{
					solicitante.incrementaTotal();
					encontrado = true;
					break;
				}
			}

			if (!encontrado)
			{
				this.solicitantes.add(sol);

			}

		}

		this.solicitantes.sort(Comparator.comparing(Solicitante::getTotal).reversed());

		System.out.println("Solicitantes: " + this.solicitantes);

		//Crea la gráfica correspondiente
		this.modeloGraficaSolicitantes = new BarChartModel();
		this.modeloGraficaSolicitantes.setSeriesColors(UtilidadesGestion.seriesColors);
		this.modeloGraficaSolicitantes.setLegendPlacement(LegendPlacement.OUTSIDEGRID);
		//		modeloGrafica.setAnimate(true);

		this.modeloGraficaSolicitantes.setLegendPosition("e");

		Axis xAxis = this.modeloGraficaSolicitantes.getAxis(AxisType.X);

		Axis yAxis = this.modeloGraficaSolicitantes.getAxis(AxisType.Y);
		yAxis.setLabel("Solicitudes");
		yAxis.setMin(0);
		yAxis.setMax(this.solicitantes.get(0).getTotal());

		for (Solicitante sol : this.solicitantes)
		{
			ChartSeries secChart = new ChartSeries();
			secChart.setLabel(sol.getDescripcion());
			secChart.set("Solicitantes", sol.getTotal());

			this.modeloGraficaSolicitantes.addSeries(secChart);

		}

	}

	public void analizaLugaresResidencia()
	{

		this.lugaresResidencia = new ArrayList<>();

		for (Gestion gestion : this.allGestiones)
		{

			LugarResidencia lug = new LugarResidencia();
			lug.setDescripcion(gestion.getPaciente().getLugarResidencia().getDescripcion());

			boolean encontrado = false;

			for (LugarResidencia lugarResidencia : this.lugaresResidencia)
			{
				if (lugarResidencia.getDescripcion().equalsIgnoreCase(lug.getDescripcion()))
				{
					lugarResidencia.incrementar();
					encontrado = true;
					break;
				}
			}

			if (!encontrado)
			{
				this.lugaresResidencia.add(lug);

			}

		}

		this.lugaresResidencia.sort(Comparator.comparing(LugarResidencia::getTotal).reversed());

		//Crea la gráfica correspondiente
		this.modeloGraficaLugarResidencia = new BarChartModel();
		this.modeloGraficaLugarResidencia.setSeriesColors(UtilidadesGestion.seriesColors);
		this.modeloGraficaLugarResidencia.setLegendPlacement(LegendPlacement.OUTSIDEGRID);
		//		modeloGrafica.setAnimate(true);

		this.modeloGraficaLugarResidencia.setLegendPosition("e");

		Axis xAxis = this.modeloGraficaLugarResidencia.getAxis(AxisType.X);

		Axis yAxis = this.modeloGraficaLugarResidencia.getAxis(AxisType.Y);
		yAxis.setLabel("Lugar Residencia");
		yAxis.setMin(0);
		yAxis.setMax(this.lugaresResidencia.get(0).getTotal());

		for (LugarResidencia lug : this.lugaresResidencia)
		{
			ChartSeries secChart = new ChartSeries();
			secChart.setLabel(lug.getDescripcion());
			secChart.set(" ", lug.getTotal());

			this.modeloGraficaSolicitantes.addSeries(secChart);

		}

	}

	public void analizaCategorias()
	{

		this.categorias = new ArrayList<>();

		for (Gestion gestion : this.allGestiones)
		{

			CategoriaGestion objCat = new CategoriaGestion();
			objCat.setDescripcion(gestion.getCategoria().getDescripcion());

			boolean encontrado = false;

			for (CategoriaGestion catGestion : this.categorias)
			{
				if (catGestion.getDescripcion().equalsIgnoreCase(objCat.getDescripcion()))
				{
					catGestion.incrementa();
					encontrado = true;
					break;
				}
			}

			if (!encontrado)
			{
				this.categorias.add(objCat);

			}

		}

		this.categorias.sort(Comparator.comparing(CategoriaGestion::getTotal).reversed());

	}

	public void analizaSeguridadSocial()
	{

		this.seguridadSocial = new ArrayList<>();

		for (Gestion gestion : this.allGestiones)
		{

			SeguridadSocial objSeguridadSocial = new SeguridadSocial();
			objSeguridadSocial.setDescripcion(gestion.getPaciente().getSeguridadSocial().getDescripcion());

			boolean encontrado = false;

			for (SeguridadSocial catSegSocial : this.seguridadSocial)
			{
				if (catSegSocial.getDescripcion().equalsIgnoreCase(objSeguridadSocial.getDescripcion()))
				{
					catSegSocial.incrementar();
					encontrado = true;
					break;
				}
			}

			if (!encontrado)
			{
				this.seguridadSocial.add(objSeguridadSocial);

			}

		}

		this.seguridadSocial.sort(Comparator.comparing(SeguridadSocial::getTotal).reversed());

	}

	public void analizaSexos()
	{

		this.sexos = new ArrayList<>();

		for (Gestion gestion : this.allGestiones)
		{

			Sexo objSexo = new Sexo();
			objSexo.setDescripcion(gestion.getPaciente().getSexo().equalsIgnoreCase("m") ? "Masculino" : "Femenino");

			boolean encontrado = false;

			for (Sexo catSexo : this.sexos)
			{
				if (catSexo.getDescripcion().equalsIgnoreCase(objSexo.getDescripcion()))
				{
					catSexo.incrementar();
					encontrado = true;
					break;
				}
			}

			if (!encontrado)
			{
				this.sexos.add(objSexo);

			}

		}

		this.sexos.sort(Comparator.comparing(Sexo::getTotal).reversed());

	}

	public void analizaEdades()
	{

		this.edades = new ArrayList<>();
		Edad edad0a5 = new Edad();
		edad0a5.setDescripcion("0-5 años");
		Edad edad6a11 = new Edad();
		edad6a11.setDescripcion("6-11 años");
		Edad edad12a18 = new Edad();
		edad12a18.setDescripcion("12-18 años");
		Edad edad19a26 = new Edad();
		edad19a26.setDescripcion("19-26 años");
		Edad edad27a59 = new Edad();
		edad27a59.setDescripcion("27-59 años");
		Edad edad60ymas = new Edad();
		edad60ymas.setDescripcion("60 años y más");

		this.edades.add(edad0a5);
		this.edades.add(edad6a11);
		this.edades.add(edad12a18);
		this.edades.add(edad19a26);
		this.edades.add(edad27a59);
		this.edades.add(edad60ymas);

		for (Gestion gestion : this.allGestiones)
		{

			Edad objEdad = new Edad();
			int edad = gestion.getPaciente().getEdad();

			if (edad < 6)
			{
				objEdad.setDescripcion("0-5 años");
			}
			else if (edad < 12)
			{
				objEdad.setDescripcion("6-11 años");
			}
			else if (edad < 19)
			{
				objEdad.setDescripcion("12-18 años");
			}
			else if (edad < 27)
			{
				objEdad.setDescripcion("19-26 años");
			}
			else if (edad < 60)
			{
				objEdad.setDescripcion("27-59 años");
			}
			else
			{
				objEdad.setDescripcion("60 años y más");
			}

			objEdad.incrementar();

			boolean encontrado = false;

			for (Edad catEdad : this.edades)
			{
				if (catEdad.getDescripcion().equalsIgnoreCase(objEdad.getDescripcion()))
				{
					catEdad.incrementar();
					encontrado = true;
					break;
				}
			}

			if (!encontrado)
			{
				this.edades.add(objEdad);

			}

		}

		this.edades.sort(Comparator.comparing(Edad::getTotal).reversed());

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

	public String getTiempoPromedioFinalizacion()
	{
		return tiempoPromedioFinalizacion;
	}

	public void setTiempoPromedioFinalizacion(String tiempoPromedioFinalizacion)
	{
		this.tiempoPromedioFinalizacion = tiempoPromedioFinalizacion;
	}

	public List<CategoriaGestion> getCategorias()
	{
		return categorias;
	}

	public void setCategorias(List<CategoriaGestion> categorias)
	{
		this.categorias = categorias;
	}

	public List<Solicitante> getSolicitantes()
	{
		return solicitantes;
	}

	public void setSolicitantes(List<Solicitante> solicitantes)
	{
		this.solicitantes = solicitantes;
	}

	public BarChartModel getModeloGraficaSolicitantes()
	{
		return modeloGraficaSolicitantes;
	}

	public void setModeloGraficaSolicitantes(BarChartModel modeloGraficaSolicitantes)
	{
		this.modeloGraficaSolicitantes = modeloGraficaSolicitantes;
	}

	public List<LugarResidencia> getLugaresResidencia()
	{
		return lugaresResidencia;
	}

	public void setLugaresResidencia(List<LugarResidencia> lugaresResidencia)
	{
		this.lugaresResidencia = lugaresResidencia;
	}

	public BarChartModel getModeloGraficaLugarResidencia()
	{
		return modeloGraficaLugarResidencia;
	}

	public void setModeloGraficaLugarResidencia(BarChartModel modeloGraficaLugarResidencia)
	{
		this.modeloGraficaLugarResidencia = modeloGraficaLugarResidencia;
	}

	public List<SeguridadSocial> getSeguridadSocial()
	{
		return seguridadSocial;
	}

	public void setSeguridadSocial(List<SeguridadSocial> seguridadSocial)
	{
		this.seguridadSocial = seguridadSocial;
	}

	public List<Sexo> getSexos()
	{
		return sexos;
	}

	public void setSexos(List<Sexo> sexos)
	{
		this.sexos = sexos;
	}

	public List<Edad> getEdades()
	{
		return edades;
	}

	public void setEdades(List<Edad> edades)
	{
		this.edades = edades;
	}

	public int getTotalGestiones()
	{
		return totalGestiones;
	}

	public void setTotalGestiones(int totalGestiones)
	{
		this.totalGestiones = totalGestiones;
	}

	public PieChartModel getModeloGraficaGeneral()
	{
		return modeloGraficaGeneral;
	}

	public void setModeloGraficaGeneral(PieChartModel modeloGraficaGeneral)
	{
		this.modeloGraficaGeneral = modeloGraficaGeneral;
	}

}
