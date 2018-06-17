package gui.portal.climalaboral;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;

import org.primefaces.event.ItemSelectEvent;
import org.primefaces.model.chart.Axis;
import org.primefaces.model.chart.AxisType;
import org.primefaces.model.chart.BarChartModel;
import org.primefaces.model.chart.ChartSeries;
import org.primefaces.model.chart.PieChartModel;

import modelo.climalaboral.Area;
import modelo.climalaboral.Encuesta;
import modelo.climalaboral.Jornada;
import modelo.climalaboral.Pregunta;
import modelo.climalaboral.Profesion;
import modelo.climalaboral.Respuesta;
import modelo.climalaboral.Seccion;
import util.climalaboral.UtilidadesClimaLaboral;

@SessionScoped
@ManagedBean
public class ResultadosBean
{
	private BarChartModel	modeloGrafica;
	private BarChartModel	modeloSecSeleccionada;

	private PieChartModel	modeloTotalesArea;
	private PieChartModel	modeloTotalesProfesion;
	private PieChartModel	modeloTotalesJornada;

	private Encuesta		encuesta;
	private Seccion			secSeleccionada;

	//Lista que contiene las secciones que se representan en la gráfica, ya que pueden haber secciones que no entran en la gráfica por solo contener preguntas abiertas
	private List<Seccion>	secEnGrafica;

	//modoPanel 0 vista general, 1 detalle de sección
	private int				modoPanel;

	private Area			areaSelec;
	private Profesion		profesionSelec;
	private Jornada			jornadaSelec;
	private int				totalRegistros;

	public ResultadosBean()
	{

	}

	@PostConstruct
	public void postConstruct()
	{
		this.modoPanel = 0;
		updateGrafica();

	}

	public void updateGrafica()
	{
		getResultadosFromBD();
		createBarModel();
		createPieModel();
	}

	public void getResultadosFromBD()
	{
		this.encuesta = UtilidadesClimaLaboral.getEncuestaFromBD();
		this.encuesta.initEncuesta();
		this.encuesta.getResultadosEncuesta(this.areaSelec, this.profesionSelec, this.jornadaSelec);
		this.encuesta.getTotalesPorClasificacion(this.areaSelec, this.profesionSelec, this.jornadaSelec);
	}

	private void createBarModel()
	{

		modeloGrafica = initBarModel();
		modeloGrafica.setAnimate(true);

		modeloGrafica.setTitle("Resultados Generales por Categoría");
		modeloGrafica.setLegendPosition("ne");

		Axis xAxis = modeloGrafica.getAxis(AxisType.X);
		xAxis.setLabel("Categoría");

		Axis yAxis = modeloGrafica.getAxis(AxisType.Y);
		yAxis.setLabel("Grado de Satisfacción");
		yAxis.setMin(0);
		yAxis.setMax(100);

	}

	private void createPieModel()
	{

		this.modeloTotalesArea = new PieChartModel();
		this.modeloTotalesProfesion = new PieChartModel();
		this.modeloTotalesJornada = new PieChartModel();

		Map<String, Integer> mapTotalesAreas = this.encuesta.getTotalesClasificacion().get(0);
		Map<String, Integer> mapTotalesProfesion = this.encuesta.getTotalesClasificacion().get(1);
		Map<String, Integer> mapTotalesJornada = this.encuesta.getTotalesClasificacion().get(2);

		mapTotalesAreas.forEach((k, v) ->
		{
			this.modeloTotalesArea.set(k, v);

		});

		mapTotalesProfesion.forEach((k, v) ->
		{
			this.modeloTotalesProfesion.set(k, v);

		});

		mapTotalesJornada.forEach((k, v) ->
		{
			this.modeloTotalesJornada.set(k, v);

		});

		this.modeloTotalesArea.setTitle("Totales por Área");
		this.modeloTotalesProfesion.setTitle("Totales por Profesión");
		this.modeloTotalesJornada.setTitle("Totales por Jornada");

		this.modeloTotalesArea.setLegendPosition("s");
		this.modeloTotalesArea.setLegendRows(10);
		this.modeloTotalesArea.setFill(true);
		this.modeloTotalesArea.setShowDataLabels(true);
		this.modeloTotalesArea.setDiameter(500);

		this.modeloTotalesProfesion.setLegendPosition("s");
		this.modeloTotalesProfesion.setLegendRows(10);
		this.modeloTotalesProfesion.setFill(true);
		this.modeloTotalesProfesion.setShowDataLabels(true);
		this.modeloTotalesProfesion.setDiameter(500);

		this.modeloTotalesJornada.setLegendPosition("s");
		this.modeloTotalesJornada.setLegendRows(10);
		this.modeloTotalesJornada.setFill(true);
		this.modeloTotalesJornada.setShowDataLabels(true);
		this.modeloTotalesJornada.setDiameter(500);

	}

	private BarChartModel initBarModel()
	{
		BarChartModel model = new BarChartModel();

		this.secEnGrafica = new ArrayList<>();

		for (Seccion sec : this.encuesta.getSecciones())
		{
			ChartSeries secChart = new ChartSeries();
			secChart.setLabel(sec.getDescripcion());

			int totalPreguntasOpcionMultiple = 0;

			//totalRespuestas solo por cualquier detalle para un cálculo posterior lo dejaré
			int totalRespuestas = 0;
			int totalSiempre = 0;
			int porcentajeSatisfaccion = 0;

			for (Pregunta preg : sec.getPreguntas())
			{
				if (preg.getRespuestasResultados() != null)
				{
					totalRespuestas += preg.getRespuestasResultados().size();
				}

				if (preg.getTipoPregunta().getIdTipoPregunta() == 3)
				{
					totalPreguntasOpcionMultiple++;

					if (preg.getRespuestasResultados() == null)
					{
						continue;
					}

					for (Respuesta resp : preg.getRespuestasResultados())
					{

						if (resp.getOpcion().getDescripcion().toLowerCase().contains("siempre"))
						{
							totalSiempre++;
						}

					}

				}

			}

			if (totalPreguntasOpcionMultiple > 0)
			{

				if (totalSiempre > 0)
				{
					porcentajeSatisfaccion = (totalSiempre * 100) / totalRespuestas;
				}

				this.totalRegistros = totalRespuestas / sec.getPreguntas().size();
				secChart.set(" ", porcentajeSatisfaccion);
				secEnGrafica.add(sec);

				model.addSeries(secChart);

			}

		}

		return model;

	}

	private void createBarModelSeccion()
	{

		this.modeloSecSeleccionada = initBarModelSeccion();
		this.modeloSecSeleccionada.setAnimate(true);

		this.modeloSecSeleccionada.setTitle("");
		this.modeloSecSeleccionada.setLegendPosition("ne");

		Axis xAxis = this.modeloSecSeleccionada.getAxis(AxisType.X);
		xAxis.setLabel(" ");

		Axis yAxis = this.modeloSecSeleccionada.getAxis(AxisType.Y);
		yAxis.setLabel("Porcentaje");
		yAxis.setMin(0);
		yAxis.setMax(100);

	}

	private BarChartModel initBarModelSeccion()
	{
		BarChartModel model = new BarChartModel();

		int cont = 0;

		ChartSeries secChart = new ChartSeries();
		secChart.setLabel("Grado de Satisfacción");

		for (Pregunta preg : this.secSeleccionada.getPreguntas())
		{
			int totalRespuestas = 0;
			int totalSiempre = 0;
			int porcentajeSatisfaccion = 0;
			int totalPreguntasOpcionMultiple = 0;

			cont++;

			totalRespuestas += preg.getRespuestasResultados().size();

			if (preg.getTipoPregunta().getIdTipoPregunta() == 3)
			{
				totalPreguntasOpcionMultiple++;

				for (Respuesta resp : preg.getRespuestasResultados())
				{

					if (resp.getOpcion().getDescripcion().toLowerCase().contains("siempre"))
					{
						totalSiempre++;
					}

				}

			}

			if (totalPreguntasOpcionMultiple > 0)
			{

				if (totalSiempre > 0)
				{
					porcentajeSatisfaccion = (totalSiempre * 100) / totalRespuestas;
				}

				secChart.set(" " + cont, porcentajeSatisfaccion);

			}

		}

		model.addSeries(secChart);

		return model;

	}

	public void itemSelect(ItemSelectEvent event)
	{
		this.secSeleccionada = this.secEnGrafica.get(event.getSeriesIndex());
		this.modoPanel = 1;

		createBarModelSeccion();
	}

	public void actionRegresarAGraficaGeneral()
	{
		this.modoPanel = 0;
		this.modeloSecSeleccionada = null;
	}

	public BarChartModel getModeloGrafica()
	{
		return modeloGrafica;
	}

	public void setModeloGrafica(BarChartModel modeloGrafica)
	{
		this.modeloGrafica = modeloGrafica;
	}

	public Encuesta getEncuesta()
	{
		return encuesta;
	}

	public void setEncuesta(Encuesta encuesta)
	{
		this.encuesta = encuesta;
	}

	public Seccion getSecSeleccionada()
	{
		return secSeleccionada;
	}

	public void setSecSeleccionada(Seccion secSeleccionada)
	{
		this.secSeleccionada = secSeleccionada;
	}

	public BarChartModel getModeloSecSeleccionada()
	{
		return modeloSecSeleccionada;
	}

	public void setModeloSecSeleccionada(BarChartModel modeloSecSeleccionada)
	{
		this.modeloSecSeleccionada = modeloSecSeleccionada;
	}

	public List<Seccion> getSecEnGrafica()
	{
		return secEnGrafica;
	}

	public void setSecEnGrafica(List<Seccion> secEnGrafica)
	{
		this.secEnGrafica = secEnGrafica;
	}

	public int getModoPanel()
	{
		return modoPanel;
	}

	public void setModoPanel(int modoPanel)
	{
		this.modoPanel = modoPanel;
	}

	public Area getAreaSelec()
	{
		return areaSelec;
	}

	public void setAreaSelec(Area areaSelec)
	{
		this.areaSelec = areaSelec;
	}

	public Profesion getProfesionSelec()
	{
		return profesionSelec;
	}

	public void setProfesionSelec(Profesion profesionSelec)
	{
		this.profesionSelec = profesionSelec;
	}

	public Jornada getJornadaSelec()
	{
		return jornadaSelec;
	}

	public void setJornadaSelec(Jornada jornadaSelec)
	{
		this.jornadaSelec = jornadaSelec;
	}

	public int getTotalRegistros()
	{
		return totalRegistros;
	}

	public void setTotalRegistros(int totalRegistros)
	{
		this.totalRegistros = totalRegistros;
	}

	public PieChartModel getModeloTotalesArea()
	{
		return modeloTotalesArea;
	}

	public void setModeloTotalesArea(PieChartModel modeloTotalesArea)
	{
		this.modeloTotalesArea = modeloTotalesArea;
	}

	public PieChartModel getModeloTotalesProfesion()
	{
		return modeloTotalesProfesion;
	}

	public void setModeloTotalesProfesion(PieChartModel modeloTotalesProfesion)
	{
		this.modeloTotalesProfesion = modeloTotalesProfesion;
	}

	public PieChartModel getModeloTotalesJornada()
	{
		return modeloTotalesJornada;
	}

	public void setModeloTotalesJornada(PieChartModel modeloTotalesJornada)
	{
		this.modeloTotalesJornada = modeloTotalesJornada;
	}

}
