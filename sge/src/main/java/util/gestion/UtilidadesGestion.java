package util.gestion;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBQueryExpression;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBScanExpression;
import com.amazonaws.services.dynamodbv2.model.AttributeValue;

import modelo.Usuario;
import modelo.actividades.StatusActividad;
import modelo.gestion.CategoriaGestion;
import modelo.gestion.Gestion;
import modelo.gestion.SeguridadSocial;
import modelo.gestion.TipoDescuento;
import modelo.gestion.TipoGestion;
import modelo.gestion.UnidadSalud;
import persistence.dynamodb.CategoriaGestionAWS;
import persistence.dynamodb.GestionAWS;
import persistence.dynamodb.SeguridadSocialAWS;
import persistence.dynamodb.StatusActividadAWS;
import persistence.dynamodb.TipoDescuentoAWS;
import persistence.dynamodb.TipoGestionAWS;
import persistence.dynamodb.UnidadSaludAWS;
import resources.DataBase;
import util.FacesUtils;
import util.utilidades;

public class UtilidadesGestion
{
//	public static int		idListaGestiones	= 338456892;
	public static int		idListaGestiones		= 354697479;																// pruebas
	public static int		idListaPruebas			= 354697479;
	public static String	wunderlistWSBean		= "wunderlistWSBean";
	public static String	urlAccessToken			= "https://www.wunderlist.com/oauth/access_token";
	public static String	urlLists				= "https://a.wunderlist.com/api/v1/lists";
	public static String	urlTasks				= "https://a.wunderlist.com/api/v1/tasks";
	public static String	urlNotes				= "https://a.wunderlist.com/api/v1/notes";
	public static String	urlTaskComments			= "https://a.wunderlist.com/api/v1/task_comments";
	public static String	seriesColors			= "58BA27,F52F2F, FFCC33,2db5ff,A30303,bcd2dd,ceeeff,105a82,707d84,8e6e68,"
			+ "b4eab4,515b51,091e09,00ff00,00ffe9,002afc,b200ff,ff00dc,fc0093,f2002c,"
			+ "efb8c2,baf298,a1ef97,8ce2af,8aeacf,7fd7e2,76a7db,9068cc,af63c6,e26897,"
			+ "6b382f,6d512e,68632c,546028,415b25,276338,2a7064,26576d,253170,46216d ";

	public static String	UUID_SeguridadSocial	= "a5126a79-82e5-4014-988d-1bd741f5a019";
	public static String	UUID_CategoriaGestion	= "1148c0fd-3284-4f1b-a9a3-03d00e71140a";
	public static String	UUID_UnidadSalud		= "01338d24-a713-41d5-94ac-0eab3adfa51c";

	// Para consultar listas, tareas y notas en específico, se debe adicionar
	// después de la url el id correspondiente, ejemplo: notes/7263526

	// statusGestion -1 activas, 1 finalizadas, 0 todas
	private static List<Gestion> getGestiones(int idUsuario, int statusGestion, java.util.Date fechaInicial,
			java.util.Date fechaFinal, java.util.Date fechaFinalizacionInicial, java.util.Date fechaFinalizacionFinal)
	{
		// Se obtiene la gestión, solamente con los atributos de Folio, Fecha Recepción,
		// Solicitud, Status y Usuario
		PreparedStatement prep = null;
		ResultSet rBD = null;

		List<Gestion> gestiones = new ArrayList<>();

		String complemento = "";

		switch (statusGestion)
		{
			case -1:
				complemento += " AND ges.idStatusActividad < 1 ";
				break;

			case 1:
				complemento += " AND ges.idStatusActividad = 1 ";
				break;

		}

		if (idUsuario > -1)
		{
			complemento += " AND ges.idUsuario=? ";
		}

		if (fechaInicial != null)
		{
			complemento += " AND FechaRecepcion >= ? ";
		}

		if (fechaFinal != null)
		{
			complemento += " AND FechaRecepcion <= ?";
		}

		if (fechaFinalizacionInicial != null)
		{
			complemento += " AND FechaFinalizacion >= ?";
		}

		if (fechaFinalizacionFinal != null)
		{
			complemento += " AND FechaFinalizacion <= ?";
		}

		try (Connection conexion = ((DataBase) FacesUtils.getManagedBean("database")).getConnectionGestiones();)
		{
			prep = conexion.prepareStatement(
					" SELECT ges.idTareaWunderlist, ges.idListaWunderlist, ges.fechaFinalizacion, ges.resumenFinal, ges.idGestion,ges.Descripcion,ges.FechaRecepcion,ges.Solicitud,ges.SolicitadoA,ges.idUsuario,us.nombre as nombreUsuario, st.descripcion AS descStatus, ges.idStatusActividad, ges.idCategoriaGestion, cg.descripcion as descCategoriaGestion \n"
							+ "FROM sge.gestion ges, usuario us, statusactividad st, categoriagestion cg WHERE ges.idUsuario =  us.idUsuario AND ges.idStatusActividad = st.idStatusActividad AND ges.idCategoriaGestion = cg.idCategoriaGestion "
							+ complemento + " ORDER BY ges.idGestion DESC");

			int indice = 1;

			if (idUsuario > -1)
			{
				prep.setInt(indice, idUsuario);
				indice++;
			}

			if (fechaInicial != null)
			{
				prep.setDate(indice, new java.sql.Date(fechaInicial.getTime()));
				indice++;
			}

			if (fechaFinal != null)
			{
				prep.setDate(indice, new java.sql.Date(fechaFinal.getTime()));
				indice++;
			}

			if (fechaFinalizacionInicial != null)
			{
				prep.setDate(indice, new java.sql.Date(fechaFinalizacionInicial.getTime()));
				indice++;
			}

			if (fechaFinalizacionFinal != null)
			{
				prep.setDate(indice, new java.sql.Date(fechaFinalizacionFinal.getTime()));
				indice++;
			}

			rBD = prep.executeQuery();

			if (rBD.next())
			{
				do
				{
					Gestion gestion = new Gestion();
					gestion.setIdGestion(rBD.getInt("idGestion"));

					gestion.setIdTareaWunderlist(rBD.getString("idTareaWunderlist"));
					gestion.setIdListaWunderlist(rBD.getString("idListaWunderlist"));

					if (statusGestion == 1)
					{
						gestion.setFechaFinalizacion(rBD.getDate("FechaFinalizacion"));
						gestion.setResumenFinal(rBD.getString("resumenFinal"));
					}

					gestion.setDescripcion(rBD.getString("Descripcion"));
					gestion.setFechaRecepcion(rBD.getDate("FechaRecepcion"));
					gestion.setSolicitud(rBD.getString("Solicitud"));
					gestion.setSolicitadoA(rBD.getString("SolicitadoA"));
					gestion.setStatus(
							new StatusActividad(rBD.getInt("idStatusActividad"), rBD.getString("descStatus")));

					gestion.setCategoria(new CategoriaGestion(rBD.getInt("idCategoriaGestion"),
							rBD.getString("descCategoriaGestion")));

					Usuario usuario = new Usuario();
					usuario.setIdUsuario(rBD.getInt("idUsuario"));
					usuario.setNombre(rBD.getString("nombreUsuario"));
					gestion.setUsuario(usuario);

					gestiones.add(gestion);

				} while (rBD.next());

			}

		} catch (Exception e)
		{
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,
					"Excepción",
					"Ha ocurrido una excepción al obtener las gestiones activas, favor de contactar con el desarrollador del sistema."));

			e.printStackTrace();
		} finally
		{
			if (prep != null)
			{
				try
				{
					prep.close();
				} catch (SQLException e)
				{
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}

		return gestiones;

	}

	public static List<Gestion> getAllGestiones(int idUsuario)
	{
		return UtilidadesGestion.getGestiones(idUsuario, 0, null, null, null, null);

	}

	public static List<Gestion> getGestionesActivas(int idUsuario)
	{
		return UtilidadesGestion.getGestiones(idUsuario, -1, null, null, null, null);

	}

	public static List<Gestion> getGestionesFinalizadas(int idUsuario)
	{
		return UtilidadesGestion.getGestiones(idUsuario, 1, null, null, null, null);

	}

	public static List<Gestion> getAllGestionesPorPeriodo(int idUsuario, java.util.Date fechaInicio,
			java.util.Date fechaFin, java.util.Date fechaFinalizacionInicial, java.util.Date fechaFinalizacionFinal)
	{
		return UtilidadesGestion.getGestiones(idUsuario, 1, fechaInicio, fechaFin, fechaFinalizacionInicial,
				fechaFinalizacionFinal);

	}

	public static List<Gestion> getGestionesActivasPorPeriodo(int idUsuario, java.util.Date fechaInicio,
			java.util.Date fechaFin, java.util.Date fechaFinalizacionInicial, java.util.Date fechaFinalizacionFinal)
	{
		return UtilidadesGestion.getGestiones(idUsuario, -1, fechaInicio, fechaFin, fechaFinalizacionInicial,
				fechaFinalizacionFinal);

	}

	public static List<Gestion> getGestionesFinalizadasPorPeriodo(int idUsuario, java.util.Date fechaInicio,
			java.util.Date fechaFin, java.util.Date fechaFinalizacionInicial, java.util.Date fechaFinalizacionFinal)
	{
		return UtilidadesGestion.getGestiones(idUsuario, 1, fechaInicio, fechaFin, fechaFinalizacionInicial,
				fechaFinalizacionFinal);

	}

	public static List<StatusActividad> getCatStatusActividad()
	{
		PreparedStatement prep = null;
		ResultSet rBD = null;

		List<StatusActividad> catStatusActividad = new ArrayList<>();

		try (Connection conexion = ((DataBase) FacesUtils.getManagedBean("database")).getConnectionGestiones();)
		{
			prep = conexion.prepareStatement(" SELECT * FROM statusactividad");

			rBD = prep.executeQuery();

			if (rBD.next())
			{
				do
				{
					catStatusActividad
							.add(new StatusActividad(rBD.getInt("idStatusActividad"), rBD.getString("Descripcion")));

				} while (rBD.next());

			}

		} catch (Exception e)
		{
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,
					"Excepción",
					"Ha ocurrido una excepción al obtener el catálogo de status, favor de contactar con el desarrollador del sistema."));

			e.printStackTrace();
		} finally
		{
			if (prep != null)
			{
				try
				{
					prep.close();
				} catch (SQLException e)
				{
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}

		return catStatusActividad;

	}

	public static List<TipoGestion> getCatTipoGestion()
	{
		PreparedStatement prep = null;
		ResultSet rBD = null;

		List<TipoGestion> catTipoGestiones = new ArrayList<>();

		try (Connection conexion = ((DataBase) FacesUtils.getManagedBean("database")).getConnectionGestiones();)
		{
			prep = conexion.prepareStatement(" SELECT * FROM tipogestion");

			rBD = prep.executeQuery();

			if (rBD.next())
			{
				do
				{
					catTipoGestiones.add(new TipoGestion(rBD.getInt("idTipoGestion"), rBD.getString("Descripcion")));

				} while (rBD.next());

			}

		} catch (Exception e)
		{
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,
					"Excepción",
					"Ha ocurrido una excepción al obtener el catálogo de tipos de gestión, favor de contactar con el desarrollador del sistema."));

			e.printStackTrace();
		} finally
		{
			if (prep != null)
			{
				try
				{
					prep.close();
				} catch (SQLException e)
				{
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}

		return catTipoGestiones;

	}

	public static List<TipoGestionAWS> getCatTipoGestionAWS()
	{
		List<TipoGestionAWS> catTipoGestionAWS = null;

		DynamoDBMapper mapper = new DynamoDBMapper(utilidades.getAWSDynamoDBClient());

		catTipoGestionAWS = mapper.scan(TipoGestionAWS.class, new DynamoDBScanExpression());

		return catTipoGestionAWS;
	}

	public static List<SeguridadSocial> getCatSeguridadSocial()
	{
		PreparedStatement prep = null;
		ResultSet rBD = null;

		List<SeguridadSocial> catSeguridadSocial = new ArrayList<>();

		try (Connection conexion = ((DataBase) FacesUtils.getManagedBean("database")).getConnectionGestiones();)
		{
			prep = conexion.prepareStatement(" SELECT * FROM seguridadsocial");

			rBD = prep.executeQuery();

			if (rBD.next())
			{
				do
				{
					catSeguridadSocial
							.add(new SeguridadSocial(rBD.getInt("idSeguridadSocial"), rBD.getString("Descripcion")));

				} while (rBD.next());

			}

		} catch (Exception e)
		{
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,
					"Excepción",
					"Ha ocurrido una excepción al obtener el catálogo de seguridad social, favor de contactar con el desarrollador del sistema."));

			e.printStackTrace();
		} finally
		{
			if (prep != null)
			{
				try
				{
					prep.close();
				} catch (SQLException e)
				{
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}

		return catSeguridadSocial;

	}

	public static List<CategoriaGestion> getCatCategoriaGestion()
	{
		PreparedStatement prep = null;
		ResultSet rBD = null;

		List<CategoriaGestion> catSeguridadSocial = new ArrayList<>();

		try (Connection conexion = ((DataBase) FacesUtils.getManagedBean("database")).getConnectionGestiones();)
		{
			prep = conexion.prepareStatement(" SELECT * FROM categoriagestion order by idCategoriaGestion");

			rBD = prep.executeQuery();

			if (rBD.next())
			{
				do
				{
					catSeguridadSocial
							.add(new CategoriaGestion(rBD.getInt("idCategoriaGestion"), rBD.getString("Descripcion")));

				} while (rBD.next());

			}

		} catch (Exception e)
		{
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,
					"Excepción",
					"Ha ocurrido una excepción al obtener el catálogo de categorías de gestión, favor de contactar con el desarrollador del sistema."));

			e.printStackTrace();
		} finally
		{
			if (prep != null)
			{
				try
				{
					prep.close();
				} catch (SQLException e)
				{
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}

		return catSeguridadSocial;

	}

	public static List<UnidadSalud> getCatUnidadSalud()
	{
		PreparedStatement prep = null;
		ResultSet rBD = null;

		List<UnidadSalud> catUnidadSalud = new ArrayList<>();

		try (Connection conexion = ((DataBase) FacesUtils.getManagedBean("database")).getConnectionGestiones();)
		{
			prep = conexion.prepareStatement(" SELECT * FROM unidadsalud order by idUnidadSalud ASC");

			rBD = prep.executeQuery();

			if (rBD.next())
			{
				do
				{
					catUnidadSalud.add(new UnidadSalud(rBD.getInt("idUnidadSalud"), rBD.getString("Descripcion")));

				} while (rBD.next());

			}

		} catch (Exception e)
		{
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,
					"Excepción",
					"Ha ocurrido una excepción al obtener el catálogo de unidades de salud, favor de contactar con el desarrollador del sistema."));

			e.printStackTrace();
		} finally
		{
			if (prep != null)
			{
				try
				{
					prep.close();
				} catch (SQLException e)
				{
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}

		return catUnidadSalud;

	}

	public static List<TipoDescuento> getCatTipoDescuento()
	{
		PreparedStatement prep = null;
		ResultSet rBD = null;

		List<TipoDescuento> catTipoDescuento = new ArrayList<>();

		try (Connection conexion = ((DataBase) FacesUtils.getManagedBean("database")).getConnectionGestiones();)
		{
			prep = conexion.prepareStatement(" SELECT * FROM tipodescuento order by idTipoDescuento ASC");

			rBD = prep.executeQuery();

			if (rBD.next())
			{
				do
				{
					catTipoDescuento
							.add(new TipoDescuento(rBD.getInt("idTipoDescuento"), rBD.getString("Descripcion")));

				} while (rBD.next());

			}

		} catch (Exception e)
		{
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,
					"Excepción",
					"Ha ocurrido una excepción al obtener el catálogo de los tipos de descuento, favor de contactar con el desarrollador del sistema."));

			e.printStackTrace();
		} finally
		{
			if (prep != null)
			{
				try
				{
					prep.close();
				} catch (SQLException e)
				{
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}

		return catTipoDescuento;

	}

	public static List<String> getCoincidenciasSolicitantes(String query)
	{

		PreparedStatement prep = null;
		ResultSet rBD = null;

		List<String> catSolicitantes = new ArrayList<>();

		try (Connection conexion = ((DataBase) FacesUtils.getManagedBean("database")).getConnectionGestiones();)
		{
			prep = conexion.prepareStatement(" SELECT DISTINCT(SolicitadoA) FROM gestion WHERE SolicitadoA LIKE ? ");
			prep.setString(1, "%" + query + "%");

			rBD = prep.executeQuery();

			if (rBD.next())
			{
				do
				{
					catSolicitantes.add(rBD.getString("SolicitadoA"));

				} while (rBD.next());
			}
		} catch (Exception e)
		{
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,
					"Excepción",
					"Ha ocurrido una excepción al obtener el catálogo de solicitantes de gestión, favor de contactar con el desarrollador del sistema."));

			e.printStackTrace();
		} finally
		{
			if (prep != null)
			{
				try
				{
					prep.close();
				} catch (SQLException e)
				{
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}

		return catSolicitantes;

	}

	public static List<String> getCoincidenciasLugarOrigen(String query)
	{

		PreparedStatement prep = null;
		ResultSet rBD = null;

		List<String> coincidenciasLugarOrigen = new ArrayList<>();

		try (Connection conexion = ((DataBase) FacesUtils.getManagedBean("database")).getConnectionGestiones();)
		{
			prep = conexion
					.prepareStatement(" SELECT DISTINCT(Descripcion) FROM lugarresidencia WHERE Descripcion LIKE ? ");
			prep.setString(1, "%" + query + "%");

			rBD = prep.executeQuery();

			if (rBD.next())
			{
				do
				{
					coincidenciasLugarOrigen.add(rBD.getString("Descripcion"));

				} while (rBD.next());
			}
		} catch (Exception e)
		{
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,
					"Excepción",
					"Ha ocurrido una excepción al obtener el catálogo de lugar de origen, favor de contactar con el desarrollador del sistema."));

			e.printStackTrace();
		} finally
		{
			if (prep != null)
			{
				try
				{
					prep.close();
				} catch (SQLException e)
				{
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}

		return coincidenciasLugarOrigen;

	}

	// MÉTODOS PARA REPORTES
	public static Map<String, Integer> getTotalesSolicitantes()
	{
		PreparedStatement prep = null;
		ResultSet rBD = null;

		Map<String, Integer> solicitantes = new HashMap<>();

		try (Connection conexion = ((DataBase) FacesUtils.getManagedBean("database")).getConnectionGestiones();)
		{
			prep = conexion.prepareStatement(
					" SELECT SolicitadoA, COUNT(*) AS conteo FROM gestion GROUP BY SolicitadoA ORDER BY conteo DESC");

			rBD = prep.executeQuery();

			if (rBD.next())
			{
				do
				{
					solicitantes.put(rBD.getString("SolicitadoA"), rBD.getInt("conteo"));

				} while (rBD.next());

			}

		} catch (Exception e)
		{
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,
					"Excepción",
					"Ha ocurrido una excepción al obtener la estadística de solicitantes de gestión, favor de contactar con el desarrollador del sistema."));

			e.printStackTrace();
		} finally
		{
			if (prep != null)
			{
				try
				{
					prep.close();
				} catch (SQLException e)
				{
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}

		return solicitantes;

	}

	public static List<UnidadSaludAWS> getCatUnidadSaludAWS()
	{
		List<UnidadSaludAWS> catUnidadSaludAWS = null;

		DynamoDBMapper mapper = new DynamoDBMapper(utilidades.getAWSDynamoDBClient());

		Map<String, AttributeValue> eav = new HashMap<String, AttributeValue>();
		eav.put(":val1", new AttributeValue().withS(UUID_UnidadSalud));

		DynamoDBQueryExpression<UnidadSaludAWS> queryExpression = new DynamoDBQueryExpression<UnidadSaludAWS>()
				.withKeyConditionExpression("UUID_UnidadSalud = :val1 ").withExpressionAttributeValues(eav)
				.withScanIndexForward(true);

		catUnidadSaludAWS = mapper.query(UnidadSaludAWS.class, queryExpression);

		return catUnidadSaludAWS;
	}

	public static List<CategoriaGestionAWS> getCatCategoriaGestionAWS()
	{

		List<CategoriaGestionAWS> catCategoriaGestion = null;

		DynamoDBMapper mapper = new DynamoDBMapper(utilidades.getAWSDynamoDBClient());

		Map<String, AttributeValue> eav = new HashMap<String, AttributeValue>();
		eav.put(":val1", new AttributeValue().withS(UUID_CategoriaGestion));

		DynamoDBQueryExpression<CategoriaGestionAWS> queryExpression = new DynamoDBQueryExpression<CategoriaGestionAWS>()
				.withKeyConditionExpression("UUID_CategoriaGestion = :val1").withExpressionAttributeValues(eav)
				.withScanIndexForward(true);

		catCategoriaGestion = mapper.query(CategoriaGestionAWS.class, queryExpression);

		return catCategoriaGestion;
	}

	public static List<StatusActividadAWS> getCatStatusActividadAWS()
	{
		List<StatusActividadAWS> catStatusActividad = null;

		DynamoDBMapper mapper = new DynamoDBMapper(utilidades.getAWSDynamoDBClient());
		
		StatusActividadAWS sta = new StatusActividadAWS(-1, "Agendada");
		StatusActividadAWS sta2 = new StatusActividadAWS(0, "Iniciada");
		StatusActividadAWS sta3 = new StatusActividadAWS(2, "Finalizada");
		
		mapper.batchSave(Arrays.asList(sta, sta2, sta3));

		catStatusActividad = mapper.scan(StatusActividadAWS.class, new DynamoDBScanExpression());

		return catStatusActividad;

	}

	public static List<TipoDescuentoAWS> getCatTipoDescuentoAWS()
	{
		List<TipoDescuentoAWS> catTipoDescuentoAWS = null;

		DynamoDBMapper mapper = new DynamoDBMapper(utilidades.getAWSDynamoDBClient());

		catTipoDescuentoAWS = mapper.scan(TipoDescuentoAWS.class, new DynamoDBScanExpression());

		return catTipoDescuentoAWS;
	}

	public static List<SeguridadSocialAWS> getCatSeguridadSocialAWS()
	{
		List<SeguridadSocialAWS> catSeguridadSocialAWS = null;

		DynamoDBMapper mapper = new DynamoDBMapper(utilidades.getAWSDynamoDBClient());

		Map<String, AttributeValue> eav = new HashMap<String, AttributeValue>();
		eav.put(":val1", new AttributeValue().withS(UUID_SeguridadSocial));

		DynamoDBQueryExpression<SeguridadSocialAWS> queryExpression = new DynamoDBQueryExpression<SeguridadSocialAWS>()
				.withKeyConditionExpression("UUID_SeguridadSocial = :val1").withExpressionAttributeValues(eav)
				.withScanIndexForward(true);

		catSeguridadSocialAWS = mapper.query(SeguridadSocialAWS.class, queryExpression);

		return catSeguridadSocialAWS;
	}
	
	public static int countGestionAWS(LocalDateTime fechaCreacion)
	{
		DynamoDBMapper mapper = new DynamoDBMapper(utilidades.getAWSDynamoDBClient());

		Map<String, AttributeValue> eav = new HashMap<String, AttributeValue>();
		eav.put(":val1", new AttributeValue().withS(fechaCreacion.toString()));

		DynamoDBScanExpression queryExpression = new DynamoDBScanExpression()
				.withFilterExpression("fechaCreacion < :val1 ")
				.withExpressionAttributeValues(eav);
		
		return mapper.count(GestionAWS.class, queryExpression);
		
	}

}
