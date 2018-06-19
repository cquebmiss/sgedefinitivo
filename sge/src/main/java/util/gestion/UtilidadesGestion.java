package util.gestion;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;

import modelo.Usuario;
import modelo.actividades.StatusActividad;
import modelo.gestion.Gestion;
import modelo.gestion.SeguridadSocial;
import modelo.gestion.TipoGestion;
import resources.DataBase;
import util.FacesUtils;

public class UtilidadesGestion
{
	public static String	urlAccessToken	= "https://www.wunderlist.com/oauth/access_token";
	public static String	urlLists		= "https://a.wunderlist.com/api/v1/lists";
	public static String	urlTasks		= "https://a.wunderlist.com/api/v1/tasks";
	public static String	urlNotes		= "https://a.wunderlist.com/api/v1/notes";
	//Para consultar listas, tareas y notas en específico, se debe adicionar después de la url el id correspondiente, ejemplo: notes/7263526

	public static List<Gestion> getGestionesActivas(int idUsuario)
	{
		//Se obtiene la gestión, solamente con los atributos de Folio, Fecha Recepción, Solicitud, Status y Usuario
		PreparedStatement prep = null;
		ResultSet rBD = null;

		List<Gestion> gestiones = new ArrayList<>();

		try (Connection conexion = ((DataBase) FacesUtils.getManagedBean("database")).getConnectionGestiones();)
		{
			prep = conexion.prepareStatement(
					" SELECT ges.idGestion,ges.Descripcion,ges.FechaRecepcion,ges.Solicitud,ges.idUsuario,us.nombre as nombreUsuario, st.descripcion AS descStatus, ges.idStatusActividad \n"
							+ "FROM sge.gestion ges, usuario us, statusactividad st WHERE ges.idUsuario =  us.idUsuario AND ges.idStatusActividad = st.idStatusActividad AND ges.idUsuario=? AND ges.idStatusActividad < 1 ORDER BY ges.idGestion DESC");

			prep.setInt(1, idUsuario);

			rBD = prep.executeQuery();

			if (rBD.next())
			{
				do
				{
					Gestion gestion = new Gestion();
					gestion.setIdGestion(rBD.getInt("idGestion"));
					gestion.setDescripcion(rBD.getString("Descripcion"));
					gestion.setFechaRecepcion(rBD.getDate("FechaRecepcion"));
					gestion.setSolicitud(rBD.getString("Solicitud"));
					gestion.setStatus(
							new StatusActividad(rBD.getInt("idStatusActividad"), rBD.getString("descStatus")));

					Usuario usuario = new Usuario();
					usuario.setIdUsuario(rBD.getInt("idUsuario"));
					usuario.setNombre(rBD.getString("nombreUsuario"));
					gestion.setUsuario(usuario);

					gestiones.add(gestion);

				} while (rBD.next());

			}

		}
		catch (Exception e)
		{
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,
					"Excepción",
					"Ha ocurrido una excepción al obtener las gestiones activas, favor de contactar con el desarrollador del sistema."));

			e.printStackTrace();
		}
		finally
		{
			if (prep != null)
			{
				try
				{
					prep.close();
				}
				catch (SQLException e)
				{
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}

		return gestiones;

	}

	public static List<Gestion> getGestionesFinalizadas(int idUsuario)
	{
		PreparedStatement prep = null;
		ResultSet rBD = null;

		List<Gestion> gestiones = new ArrayList<>();

		try (Connection conexion = ((DataBase) FacesUtils.getManagedBean("database")).getConnectionGestiones();)
		{
			prep = conexion.prepareStatement(
					" SELECT ges.fechaFinalizacion, ges.resumenFinal, ges.idGestion,ges.Descripcion,ges.FechaRecepcion,ges.Solicitud,ges.idUsuario,us.nombre as nombreUsuario, st.descripcion AS descStatus, ges.idStatusActividad \n"
							+ "FROM sge.gestion ges, usuario us, statusactividad st WHERE ges.idUsuario =  us.idUsuario AND ges.idStatusActividad = st.idStatusActividad AND ges.idUsuario=? AND ges.idStatusActividad = 1 1 ORDER BY ges.idGestion DESC");

			prep.setInt(1, idUsuario);

			rBD = prep.executeQuery();

			if (rBD.next())
			{
				do
				{
					Gestion gestion = new Gestion();
					gestion.setFechaFinalizacion(rBD.getDate("FechaFinalizacion"));
					gestion.setResumenFinal(rBD.getString("resumenFinal"));
					gestion.setIdGestion(rBD.getInt("idGestion"));
					gestion.setDescripcion(rBD.getString("Descripcion"));
					gestion.setFechaRecepcion(rBD.getDate("FechaRecepcion"));
					gestion.setSolicitud(rBD.getString("Solicitud"));
					gestion.setStatus(
							new StatusActividad(rBD.getInt("idStatusActividad"), rBD.getString("descStatus")));

					Usuario usuario = new Usuario();
					usuario.setIdUsuario(rBD.getInt("idUsuario"));
					usuario.setNombre(rBD.getString("nombreUsuario"));
					gestion.setUsuario(usuario);

					gestiones.add(gestion);

				} while (rBD.next());

			}
		}
		catch (Exception e)
		{
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,
					"Excepción",
					"Ha ocurrido una excepción al obtener las gestiones finalizadas, favor de contactar con el desarrollador del sistema."));

			e.printStackTrace();
		}
		finally
		{
			if (prep != null)
			{
				try
				{
					prep.close();
				}
				catch (SQLException e)
				{
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}

		return gestiones;

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

		}
		catch (Exception e)
		{
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,
					"Excepción",
					"Ha ocurrido una excepción al obtener el catálogo de status, favor de contactar con el desarrollador del sistema."));

			e.printStackTrace();
		}
		finally
		{
			if (prep != null)
			{
				try
				{
					prep.close();
				}
				catch (SQLException e)
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

		}
		catch (Exception e)
		{
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,
					"Excepción",
					"Ha ocurrido una excepción al obtener el catálogo de tipos de gestión, favor de contactar con el desarrollador del sistema."));

			e.printStackTrace();
		}
		finally
		{
			if (prep != null)
			{
				try
				{
					prep.close();
				}
				catch (SQLException e)
				{
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}

		return catTipoGestiones;

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

		}
		catch (Exception e)
		{
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,
					"Excepción",
					"Ha ocurrido una excepción al obtener el catálogo de seguridad social, favor de contactar con el desarrollador del sistema."));

			e.printStackTrace();
		}
		finally
		{
			if (prep != null)
			{
				try
				{
					prep.close();
				}
				catch (SQLException e)
				{
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}

		return catSeguridadSocial;

	}
}
