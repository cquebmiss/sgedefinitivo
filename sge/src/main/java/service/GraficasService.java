package service;

import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.Optional;

import org.primefaces.model.chart.Axis;
import org.primefaces.model.chart.AxisType;
import org.primefaces.model.chart.CategoryAxis;
import org.primefaces.model.chart.ChartSeries;
import org.primefaces.model.chart.LineChartModel;

import dao.GestionesDAO;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
public class GraficasService
{
	// Gráfica lineal de total de gestiones mensuales
	private LineChartModel lineModel;

	public LineChartModel getChartMensualTotalGestionesAño(int año)
	{
		this.lineModel = new LineChartModel();
		Axis yAxis = lineModel.getAxis(AxisType.Y);

		lineModel.setTitle("Tendencia de Gestión");
		lineModel.setLegendPosition("e");
		lineModel.setShowPointLabels(true);
		lineModel.getAxes().put(AxisType.X, new CategoryAxis("" + año));
		yAxis = lineModel.getAxis(AxisType.Y);
		yAxis.setLabel("Total");
		yAxis.setMin(0);

		GestionesDAO gestionesDAO = new GestionesDAO();
		LinkedHashMap<String, Integer> resultados = gestionesDAO.getTotalGestionesMensuales(año);

		Optional<Integer> maximo = resultados.values().stream().max(Comparator.comparing(Integer::valueOf));

		if (maximo.isPresent())
		{
			yAxis.setMax(maximo.get()+10);
		} else
		{
			yAxis.setMax(0);

		}

		ChartSeries chartSerie = new ChartSeries();
		chartSerie.setLabel("Gestiones Registradas");

		resultados.entrySet().forEach(e ->
		{
			chartSerie.set(e.getKey(), e.getValue());

		});

		this.lineModel.addSeries(chartSerie);

		return lineModel;
	}

}
