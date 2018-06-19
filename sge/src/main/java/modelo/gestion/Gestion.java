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

	public Gestion()
	{
		super();
		this.idGestion = -1;
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

	//Actualizar todos los datos de la gestión desde la base de datos
	public void updateAllDataBD()
	{

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

}
