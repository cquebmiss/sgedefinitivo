package util.gestion;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;

import modelo.actividades.StatusActividad;
import modelo.gestion.SeguridadSocial;
import modelo.gestion.TipoGestion;
import resources.DataBase;
import util.FacesUtils;

public class UtilidadesGestion
{

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
