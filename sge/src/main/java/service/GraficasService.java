package service;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Optional;
import java.util.StringJoiner;

import org.primefaces.model.chart.Axis;
import org.primefaces.model.chart.AxisType;
import org.primefaces.model.chart.CategoryAxis;
import org.primefaces.model.chart.ChartSeries;
import org.primefaces.model.chart.LineChartModel;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import dao.GestionesDAO;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import modelo.gestion.CategoriaGestion;
import modelo.gestion.Edad;
import modelo.gestion.Gestion;
import modelo.gestion.LugarResidencia;
import modelo.gestion.SeguridadSocial;
import modelo.gestion.Sexo;
import modelo.gestion.Solicitante;
import util.gestion.UtilidadesGestion;

@NoArgsConstructor
@Getter
@Setter
public class GraficasService
{
	// Gráfica lineal de total de gestiones mensuales
	private LineChartModel	lineModel;
	private List<String>	mesesTendencia;
	private List<Integer>	numeroCasosMes;
	private int				totalMeses;
	private String			jsonResultadosTendencia;
	private String			jsonTotalGestiones;

	private String			jsonSolicitantes;
	private final String[]	meses	=
	{ "Enero", "Febrero", "Marzo", "Abril", "Mayo", "Junio", "Julio", "Agosto", "Septiembre", "Octubre", "Noviembre",
			"Diciembre" };

	public String getChartMensualTotalGestionesAño(int añoTendencia, int añoTendenciaBase)
	{
		GestionesDAO							gestionesDAO	= new GestionesDAO();
		List<LinkedHashMap<String, Integer>>	listaAñosTendencia	= new ArrayList<>();

		StringJoiner							stjFinal		= new StringJoiner(",", "[", "]");
		StringJoiner							stjAuxiliar		= new StringJoiner(",", "[", "]");
		stjAuxiliar.add("'Mes'");

		for (int x = añoTendenciaBase; x <= añoTendencia; x++)
		{
			stjAuxiliar.add("'" + x + "'");
			listaAñosTendencia.add(gestionesDAO.getTotalGestionesMensuales(x));
		}

		stjFinal.add(stjAuxiliar.toString());

		StringJoiner tendencia;
		String mes;
		LinkedHashMap<String, Integer> linkedAñoTendencia;
		Integer frecuenciaMes;

		for (int x = 0; x < 12; x++)
		{
			tendencia = new StringJoiner(",", "[", "]");
			
			mes = this.meses[x];
			tendencia.add("'"+mes+"'");
			
			for (int a = añoTendenciaBase; a <= añoTendencia; a++)
			{
				linkedAñoTendencia = listaAñosTendencia.get(a-añoTendenciaBase);
				frecuenciaMes = linkedAñoTendencia.get(mes);
				
				if( frecuenciaMes != null )
				{
					tendencia.add(""+frecuenciaMes.intValue()+"");
				}
				else
				{
					tendencia.add("0");
				}
			}
			
			stjFinal.add(tendencia.toString());
		}

		System.out.println("Json tendencia: "+stjFinal.toString());
		return stjFinal.toString();
	}

	public String getChartTotalGestiones(int idUsuario, Date fechaInicial, Date fechaFinal,
			Date fechaFinalizacionInicial, Date fechaFinalizacionFinal)
	{
		List<Gestion>	gestionesActivas		= UtilidadesGestion.getGestionesActivasPorPeriodo(idUsuario,
				fechaInicial, fechaFinal, fechaFinalizacionInicial, fechaFinalizacionFinal);
		List<Gestion>	gestionesFinalizadas	= UtilidadesGestion.getGestionesFinalizadasPorPeriodo(idUsuario,
				fechaInicial, fechaFinal, fechaFinalizacionInicial, fechaFinalizacionFinal);

		StringJoiner	stJ						= new StringJoiner(",");

		stJ.add("[" + "\"Activas (" + gestionesActivas.size() + ")\"," + gestionesActivas.size() + "]");
		stJ.add("[" + "\"Finalizadas (" + gestionesActivas.size() + ")\"," + gestionesFinalizadas.size() + "]");

		System.out.println("Total de gestiones: " + stJ.toString());
		this.jsonTotalGestiones = stJ.toString();

		return stJ.toString();
	}

	public List<Solicitante> getEstadisticaSolicitantes(List<Gestion> allGestiones)
	{
		List<Solicitante> solicitantes = new ArrayList<>();

		for (Gestion gestion : allGestiones)
		{
			Solicitante sol = new Solicitante();
			sol.setDescripcion(gestion.getSolicitadoA().trim());

			boolean encontrado = false;

			for (Solicitante solicitante : solicitantes)
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
				solicitantes.add(sol);
			}
		}

		solicitantes.sort(Comparator.comparing(Solicitante::getTotal).reversed());

		return solicitantes;
	}

	public String getChartSolicitantes(List<Solicitante> solicitantes)
	{
		StringJoiner stJ = new StringJoiner(",");

		for (Solicitante solicitante : solicitantes)
		{
			stJ.add("[\"" + solicitante.getDescripcion() + "\"," + solicitante.getTotal() + "]");
		}

		System.out.println("Json solicitantes: " + stJ.toString());
		return stJ.toString();
	}

	public List<LugarResidencia> getEstadisticaLugarResidencia(List<Gestion> allGestiones)
	{
		List<LugarResidencia> lugaresResidencia = new ArrayList<>();

		for (Gestion gestion : allGestiones)
		{
			LugarResidencia lug = new LugarResidencia();
			lug.setDescripcion(gestion.getPaciente().getLugarResidencia().getDescripcion());

			boolean encontrado = false;

			for (LugarResidencia lugarResidencia : lugaresResidencia)
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
				lugaresResidencia.add(lug);
			}
		}

		lugaresResidencia.sort(Comparator.comparing(LugarResidencia::getTotal).reversed());

		return lugaresResidencia;
	}

	public String getChartLugarResidencia(List<LugarResidencia> lugaresResidencia)
	{
		StringJoiner stJ = new StringJoiner(",");

		for (LugarResidencia lugarResidencia : lugaresResidencia)
		{
			stJ.add("[\"" + lugarResidencia.getDescripcion() + "\"," + lugarResidencia.getTotal() + "]");
		}

		System.out.println("Json Lugares Residencia: " + stJ.toString());
		return stJ.toString();
	}

	public List<CategoriaGestion> getEstadisticaCategorias(List<Gestion> allGestiones)
	{
		List<CategoriaGestion> categorias = new ArrayList<>();

		for (Gestion gestion : allGestiones)
		{

			CategoriaGestion objCat = new CategoriaGestion();
			objCat.setDescripcion(gestion.getCategoria().getDescripcion());

			boolean encontrado = false;

			for (CategoriaGestion catGestion : categorias)
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
				categorias.add(objCat);

			}

		}

		categorias.sort(Comparator.comparing(CategoriaGestion::getTotal).reversed());

		return categorias;
	}

	public String getChartCategorias(List<CategoriaGestion> categorias)
	{
		StringJoiner stJ = new StringJoiner(",");

		for (CategoriaGestion categoria : categorias)
		{
			stJ.add("[\"" + categoria.getDescripcion() + "\"," + categoria.getTotal() + "]");
		}

		System.out.println("Json Categorias: " + stJ.toString());
		return stJ.toString();
	}

	public List<SeguridadSocial> getEstadisticaSeguridadSocial(List<Gestion> allGestiones)
	{
		List<SeguridadSocial> seguridadSocial = new ArrayList<>();

		for (Gestion gestion : allGestiones)
		{

			SeguridadSocial objSeguridadSocial = new SeguridadSocial();
			objSeguridadSocial.setDescripcion(gestion.getPaciente().getSeguridadSocial().getDescripcion());

			boolean encontrado = false;

			for (SeguridadSocial catSegSocial : seguridadSocial)
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
				seguridadSocial.add(objSeguridadSocial);

			}

		}

		seguridadSocial.sort(Comparator.comparing(SeguridadSocial::getTotal).reversed());

		return seguridadSocial;
	}

	public String getChartSeguridadSocial(List<SeguridadSocial> seguridadSocial)
	{
		StringJoiner stJ = new StringJoiner(",");

		for (SeguridadSocial seguridadSoc : seguridadSocial)
		{
			stJ.add("[\"" + seguridadSoc.getDescripcion() + "\"," + seguridadSoc.getTotal() + "]");
		}

		System.out.println("Json Seguridad Social: " + stJ.toString());
		return stJ.toString();
	}

	public List<Sexo> getEstadisticaSexos(List<Gestion> allGestiones)
	{
		List<Sexo> sexos = new ArrayList<>();

		for (Gestion gestion : allGestiones)
		{
			Sexo objSexo = new Sexo();
			objSexo.setDescripcion(gestion.getPaciente().getSexo().equalsIgnoreCase("m") ? "Masculino" : "Femenino");

			boolean encontrado = false;

			for (Sexo catSexo : sexos)
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
				sexos.add(objSexo);
			}
		}

		sexos.sort(Comparator.comparing(Sexo::getTotal).reversed());

		return sexos;
	}

	public String getChartSexos(List<Sexo> sexos)
	{
		StringJoiner stJ = new StringJoiner(",");

		for (Sexo sexo : sexos)
		{
			stJ.add("[\"" + sexo.getDescripcion() + "\"," + sexo.getTotal() + "]");
		}

		System.out.println("Json Sexo: " + stJ.toString());
		return stJ.toString();
	}

	public List<Edad> getEstadisticaEdades(List<Gestion> allGestiones)
	{
		List<Edad>	edades	= new ArrayList<>();
		Edad		edad0a5	= new Edad();
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

		edades.add(edad0a5);
		edades.add(edad6a11);
		edades.add(edad12a18);
		edades.add(edad19a26);
		edades.add(edad27a59);
		edades.add(edad60ymas);

		for (Gestion gestion : allGestiones)
		{

			Edad	objEdad	= new Edad();
			int		edad	= gestion.getPaciente().getEdad();

			if (edad < 6)
			{
				objEdad.setDescripcion("0-5 años");
			} else if (edad < 12)
			{
				objEdad.setDescripcion("6-11 años");
			} else if (edad < 19)
			{
				objEdad.setDescripcion("12-18 años");
			} else if (edad < 27)
			{
				objEdad.setDescripcion("19-26 años");
			} else if (edad < 60)
			{
				objEdad.setDescripcion("27-59 años");
			} else
			{
				objEdad.setDescripcion("60 años y más");
			}

			objEdad.incrementar();

			boolean encontrado = false;

			for (Edad catEdad : edades)
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
				edades.add(objEdad);
			}

		}

		edades.sort(Comparator.comparing(Edad::getTotal).reversed());

		return edades;
	}

	public String getChartEdades(List<Edad> edades)
	{
		StringJoiner stJ = new StringJoiner(",");

		for (Edad edad : edades)
		{
			stJ.add("[\"" + edad.getDescripcion() + "\"," + edad.getTotal() + "]");
		}

		System.out.println("Json Edades: " + stJ.toString());
		return stJ.toString();
	}

}
