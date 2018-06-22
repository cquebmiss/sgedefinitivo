package modelo.gestion;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;

import modelo.Sesion;
import modelo.Usuario;
import modelo.actividades.StatusActividad;
import resources.DataBase;
import util.FacesUtils;

public class Gestion
{
	private int				idGestion;
	private String			folio;
	private String			descripcion;
	private Date			fechaRecepcion;
	private String			solicitadoA;
	private String			solicitud;
	private String			detallesGenerales;
	private Date			fechaFinalizacion;
	private String			resumenFinal;
	private Usuario			usuario;
	private StatusActividad	status;
	private List<Contacto>	contactos;
	private Paciente		paciente;
	private TipoGestion		tipoGestion;
	private String			idTareaWunderlist;
	private String			idListaWunderlist;

	public Gestion()
	{
		super();
		this.idGestion = -1;
		this.folio = "";
		this.descripcion = "";
		this.fechaRecepcion = new java.util.Date();
		this.solicitadoA = "";
		this.solicitud = "";
		this.detallesGenerales = "";
		this.resumenFinal = "";
		this.paciente = new Paciente(this);
		this.contactos = new ArrayList<>();

		//Se inicializa el objeto con el status en -1 que significa que la actividad está agendada, pasará a 0 iniciada al momento de que sea revisada por el departamento de gestión y enlace
		this.status = new StatusActividad(-1, "Agendada");
		//El tipo de gestión siempre será Atención y Servicios para éste módulo
		this.tipoGestion = new TipoGestion(1, "Atención y Mejoras");

		// TODO Auto-generated constructor stub
	}

	public Gestion(int idGestion, String descripcion, Date fechaRecepcion, String solicitadoA, String solicitud,
			String detallesGenerales, Date fechaFinalizacion, String resumenFinal, Usuario usuario,
			StatusActividad status, List<Contacto> contactos, Paciente paciente, TipoGestion tipoGestion)
	{
		super();
		this.idGestion = idGestion;
		this.descripcion = descripcion;
		this.fechaRecepcion = fechaRecepcion;
		this.solicitadoA = solicitadoA;
		this.solicitud = solicitud;
		this.detallesGenerales = detallesGenerales;
		this.fechaFinalizacion = fechaFinalizacion;
		this.resumenFinal = resumenFinal;
		this.usuario = usuario;
		this.status = status;
		this.contactos = contactos;
		this.paciente = paciente;
		this.tipoGestion = tipoGestion;
	}

	public void ajustarFolioSegunDescripcion()
	{
		if (!this.descripcion.isEmpty())
		{
			this.folio = this.descripcion.substring(this.descripcion.indexOf(':') + 1, this.descripcion.indexOf('.'));
		}
	}

	//Actualizar todos los datos de la gestión desde la base de datos
	public void updateAllDataBD()
	{
		PreparedStatement prep = null;
		ResultSet rBD = null;

		try (Connection conexion = ((DataBase) FacesUtils.getManagedBean("database")).getConnectionGestiones();)
		{
			try
			{

				Sesion sesion = (Sesion) FacesUtils.getManagedBean("Sesion");

				prep = conexion.prepareStatement(
						"SELECT  ges.SolicitadoA, ges.DetallesGenerales,ges.FechaFinalizacion, ges.ResumenFinal, ges.idListaWunderlist, ges.idTareaWunderlist, ges.idTipoGestion, tg.descripcion as descTipoGestion, ges.idGestion,ges.Descripcion,ges.FechaRecepcion,ges.Solicitud,ges.idUsuario,us.nombre as nombreUsuario, st.descripcion AS descStatus, ges.idStatusActividad \n"
								+ "FROM sge.gestion ges, usuario us, statusactividad st, tipogestion tg WHERE ges.idUsuario =  us.idUsuario AND ges.idStatusActividad = st.idStatusActividad AND ges.idTipoGestion = tg.idTipoGestion AND ges.idGestion=?");

				prep.setInt(1, this.idGestion);

				rBD = prep.executeQuery();

				if (rBD.next())
				{
					setDescripcion(rBD.getString("Descripcion"));
					setFechaRecepcion(rBD.getDate("FechaRecepcion"));
					setSolicitadoA(rBD.getString("SolicitadoA"));
					setSolicitud(rBD.getString("Solicitud"));
					setDetallesGenerales(rBD.getString("DetallesGenerales"));
					setFechaFinalizacion(rBD.getDate("FechaFinalizacion"));
					setResumenFinal(rBD.getString("ResumenFinal"));
					setIdTareaWunderlist(rBD.getString("idTareaWunderlist"));
					setIdListaWunderlist(rBD.getString("idListaWunderlist"));

					setStatus(new StatusActividad(rBD.getInt("idStatusActividad"), rBD.getString("descStatus")));

					Usuario usuario = new Usuario();
					usuario.setIdUsuario(rBD.getInt("idUsuario"));
					usuario.setNombre(rBD.getString("nombreUsuario"));
					setUsuario(usuario);

					//Se añade ahora el paciente a la gestión

					prep.close();

					prep = conexion.prepareStatement(
							"SELECT p.*, lr.descripcion AS descLugarResidencia, ss.descripcion AS descSeguridadSocial\n"
									+ "FROM paciente p, lugarresidencia lr, seguridadsocial ss\n"
									+ "WHERE p.idGestion=? AND p.idLugarResidencia = lr.idLugarResidencia AND p.idSeguridadSocial = ss.idSeguridadSocial");

					prep.setInt(1, getIdGestion());

					rBD = prep.executeQuery();

					if (rBD.next())
					{

						Paciente objPaciente = new Paciente();
						objPaciente.setIdPaciente(rBD.getInt("idPaciente"));
						objPaciente.setGestion(this);
						objPaciente.setNombre(rBD.getString("Nombre"));
						objPaciente.setSexo(rBD.getString("Sexo"));
						objPaciente.setEdad(rBD.getInt("Edad"));
						objPaciente.setFechaNacimiento(rBD.getDate("FechaNacimiento"));
						objPaciente.setCURP(rBD.getString("CURP"));
						objPaciente.setLugarResidencia(new LugarResidencia(rBD.getInt("idLugarResidencia"),
								rBD.getString("descLugarResidencia")));
						objPaciente.setDiagnostico(rBD.getString("Diagnostico"));
						objPaciente.setHospitalizadoEn(rBD.getString("HospitalizadoEn"));
						objPaciente.setSeguridadSocial(new SeguridadSocial(rBD.getInt("idSeguridadSocial"),
								rBD.getString("descSeguridadSocial")));
						objPaciente.setAfiliacion(rBD.getString("Afiliacion"));

						setPaciente(objPaciente);

					}

					ajustarFolioSegunDescripcion();

					//Finalmente los contactos de la gestión
					prep = conexion.prepareStatement("SELECT * FROM contactogestion WHERE idGestion=?");

					prep.setInt(1, getIdGestion());

					rBD = prep.executeQuery();

					List<Contacto> contactos = new ArrayList<>();

					if (rBD.next())
					{
						do
						{
							contactos.add(new Contacto(rBD.getInt("idContactoGestion"), rBD.getString("Nombre"),
									rBD.getString("Telefonos"), rBD.getString("Email"),
									rBD.getString("Observaciones")));

						} while (rBD.next());
					}

					setContactos(contactos);

				}

				prep.close();
				rBD.close();

			}
			catch (Exception e)
			{
				e.printStackTrace();
				conexion.rollback();
			}

		}
		catch (Exception e)
		{
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,
					"Excepción",
					"Ha ocurrido una excepción al actualizar los datos de la gestión, favor de contactar con el desarrollador del sistema."));

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

	}

	public void crearGestionBD()
	{
		PreparedStatement prep = null;
		ResultSet rBD = null;

		try (Connection conexion = ((DataBase) FacesUtils.getManagedBean("database")).getConnectionGestiones();)
		{
			try
			{

				Sesion sesion = (Sesion) FacesUtils.getManagedBean("Sesion");

				conexion.setAutoCommit(false);
				conexion.rollback();

				prep = conexion.prepareStatement(
						" INSERT INTO gestion (Descripcion,FechaRecepcion,Solicitud,idUsuario,idStatusActividad,idTipoGestion) \n"
								+ "VALUES (?, ?, ?, ?, ?, ?) ; ",
						PreparedStatement.RETURN_GENERATED_KEYS);
				prep.setString(1, getDescripcion());
				prep.setDate(2, new java.sql.Date(getFechaRecepcion().getTime()));
				prep.setString(3, "");
				prep.setInt(4, Integer.parseInt(sesion.getIdUsuario()));
				prep.setInt(5, 1);
				prep.setInt(6, getTipoGestion().getIdTipoGestion());

				prep.executeUpdate();

				rBD = prep.getGeneratedKeys();

				if (rBD.next())
				{
					setIdGestion(rBD.getInt(1));
				}

				prep.close();
				rBD.close();

				conexion.commit();

			}
			catch (Exception e)
			{
				e.printStackTrace();
				conexion.rollback();
			}

		}
		catch (Exception e)
		{
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,
					"Excepción",
					"Ha ocurrido una excepción al crear la gestión, favor de contactar con el desarrollador del sistema."));

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

	}

	public int getIdGestion()
	{
		return idGestion;
	}

	public void setIdGestion(int idGestion)
	{
		this.idGestion = idGestion;
	}

	public String getDescripcion()
	{
		return descripcion;
	}

	public void setDescripcion(String descripcion)
	{
		this.descripcion = descripcion;
	}

	public Date getFechaRecepcion()
	{
		return fechaRecepcion;
	}

	public void setFechaRecepcion(Date fechaRecepcion)
	{
		this.fechaRecepcion = fechaRecepcion;
	}

	public String getSolicitadoA()
	{
		return solicitadoA;
	}

	public void setSolicitadoA(String solicitadoA)
	{
		this.solicitadoA = solicitadoA;
	}

	public String getSolicitud()
	{
		return solicitud;
	}

	public void setSolicitud(String solicitud)
	{
		this.solicitud = solicitud;
	}

	public String getDetallesGenerales()
	{
		return detallesGenerales;
	}

	public void setDetallesGenerales(String detallesGenerales)
	{
		this.detallesGenerales = detallesGenerales;
	}

	public Date getFechaFinalizacion()
	{
		return fechaFinalizacion;
	}

	public void setFechaFinalizacion(Date fechaFinalizacion)
	{
		this.fechaFinalizacion = fechaFinalizacion;
	}

	public String getResumenFinal()
	{
		return resumenFinal;
	}

	public void setResumenFinal(String resumenFinal)
	{
		this.resumenFinal = resumenFinal;
	}

	public Usuario getUsuario()
	{
		return usuario;
	}

	public void setUsuario(Usuario usuario)
	{
		this.usuario = usuario;
	}

	public StatusActividad getStatus()
	{
		return status;
	}

	public void setStatus(StatusActividad status)
	{
		this.status = status;
	}

	public List<Contacto> getContactos()
	{
		return contactos;
	}

	public void setContactos(List<Contacto> contactos)
	{
		this.contactos = contactos;
	}

	public Paciente getPaciente()
	{
		return paciente;
	}

	public void setPaciente(Paciente paciente)
	{
		this.paciente = paciente;
	}

	public TipoGestion getTipoGestion()
	{
		return tipoGestion;
	}

	public void setTipoGestion(TipoGestion tipoGestion)
	{
		this.tipoGestion = tipoGestion;
	}

	public String getIdTareaWunderlist()
	{
		return idTareaWunderlist;
	}

	public void setIdTareaWunderlist(String idTareaWunderlist)
	{
		this.idTareaWunderlist = idTareaWunderlist;
	}

	public String getIdListaWunderlist()
	{
		return idListaWunderlist;
	}

	public void setIdListaWunderlist(String idListaWunderlist)
	{
		this.idListaWunderlist = idListaWunderlist;
	}

	public String getFolio()
	{
		return folio;
	}

	public void setFolio(String folio)
	{
		this.folio = folio;
	}

}
