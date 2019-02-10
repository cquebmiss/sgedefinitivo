package modelo.gestion;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;

import gui.persistence.wunderlist.WunderlistWSBean;
import lombok.Getter;
import lombok.Setter;
import modelo.Sesion;
import modelo.Usuario;
import modelo.actividades.StatusActividad;
import resources.DataBase;
import util.FacesUtils;
import util.gestion.UtilidadesGestion;

@Getter
@Setter
public class Gestion
{
	private int					idGestion;
	private String				folio;
	private String				descripcion;
	private Date				fechaRecepcion;
	private String				solicitadoA;
	private String				solicitud;
	private String				detallesGenerales;
	private Date				fechaFinalizacion;
	private String				resumenFinal;
	private Usuario				usuario;
	private StatusActividad		status;
	private Costo				costo;
	private List<Contacto>		contactos;
	private Paciente			paciente;
	private TipoGestion			tipoGestion;
	private String				idTareaWunderlist;
	private String				idListaWunderlist;

	private CategoriaGestion	categoria;

	// Tarea en Wunderlist
	private Task				tareaW;

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

		// Se inicializa el objeto con el status en -1 que significa que la actividad
		// está agendada, pasará a 0 iniciada al momento de que sea revisada por el
		// departamento de gestión y enlace
		this.status = new StatusActividad(-1, "Agendada");
		// El tipo de gestión siempre será Atención y Servicios para éste módulo
		this.tipoGestion = new TipoGestion(1, "Atención y Mejoras");

		this.costo = new Costo();
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

	// Actualizar todos los datos de la gestión desde la base de datos
	public void updateAllDataBD()
	{
		PreparedStatement	prep	= null;
		ResultSet			rBD		= null;

		try (Connection conexion = ((DataBase) FacesUtils.getManagedBean("database")).getConnectionGestiones();)
		{
			try
			{

				Sesion sesion = (Sesion) FacesUtils.getManagedBean("Sesion");

				prep = conexion.prepareStatement(
						"SELECT  ges.idCategoriaGestion, cg.descripcion as descCategoriaGestion,ges.SolicitadoA, ges.DetallesGenerales,ges.FechaFinalizacion, ges.ResumenFinal, ges.idListaWunderlist, ges.idTareaWunderlist, ges.idTipoGestion, tg.descripcion as descTipoGestion, ges.idGestion,ges.Descripcion,ges.FechaRecepcion,ges.Solicitud,ges.idUsuario,us.nombre as nombreUsuario, st.descripcion AS descStatus, ges.idStatusActividad \n"
								+ "FROM sge.gestion ges, usuario us, statusactividad st, tipogestion tg, categoriagestion cg WHERE ges.idUsuario =  us.idUsuario AND ges.idStatusActividad = st.idStatusActividad AND ges.idTipoGestion = tg.idTipoGestion AND ges.idGestion=?  AND ges.idCategoriaGestion = cg.idCategoriaGestion");

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
					setCategoria(new CategoriaGestion(rBD.getInt("idCategoriaGestion"),
							rBD.getString("descCategoriaGestion")));

					Usuario usuario = new Usuario();
					usuario.setIdUsuario(rBD.getInt("idUsuario"));
					usuario.setNombre(rBD.getString("nombreUsuario"));
					setUsuario(usuario);

					// Se añade ahora el paciente a la gestión

					prep.close();

					prep = conexion.prepareStatement("SELECT p.*,\n"
							+ "	lr.descripcion AS descLugarResidencia,\n"
							+ "	ss.descripcion AS descSeguridadSocial,\n" + "	us1.descripcion AS descAtendidoEn,\n"
							+ "	us2.descripcion AS descReferenciadoA\n" + "FROM\n" + "	paciente p\n"
							+ "INNER JOIN lugarresidencia lr ON p.idLugarResidencia = lr.idLugarResidencia\n"
							+ "INNER JOIN seguridadsocial ss ON p.idSeguridadSocial = ss.idSeguridadSocial\n"
							+ "LEFT JOIN unidadsalud us1 ON p.AtendidoEn = us1.idUnidadSalud\n"
							+ "LEFT JOIN unidadsalud us2 ON p.ReferenciadoA = us2.idUnidadSalud\n"
							+ "WHERE idGestion=?" + "");

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
						
						String descAtendidoEn = rBD.getString("AtendidoEn");
						String descReferenciadoA = rBD.getString("ReferenciadoA");
						
						if( descAtendidoEn != null )
						{
							objPaciente.setAtendidoEn(new UnidadSalud(rBD.getInt("AtendidoEn"), rBD.getString("descAtendidoEn")));
							
						}
						
						if( descReferenciadoA != null )
						{
							objPaciente.setReferenciadoA(new UnidadSalud(rBD.getInt("ReferenciadoA"), rBD.getString("descReferenciadoA")));
						}

						setPaciente(objPaciente);

					}

					ajustarFolioSegunDescripcion();
					
					//Se obtienen los costos de la gestión
					prep = conexion.prepareStatement("SELECT g.*,c.CostoOriginal,c.TotalAPagar,c.idTipoDescuento, td.descripcion as descTipoDescuento FROM costo c RIGHT JOIN gestion g ON c.idGestion = g.idGestion \n" + 
							"LEFT JOIN tipodescuento td ON c.idTipoDescuento = td.idTipoDescuento WHERE g.idGestion=? \n" + 
							"ORDER BY g.idGestion DESC ");
					prep.setInt(1, getIdGestion());
					
					rBD = prep.executeQuery();
					
					Costo costo = new Costo();

					if( rBD.next())
					{
						costo.setCostoOriginal(rBD.getBigDecimal("CostoOriginal"));
						costo.setTotalAPagar(rBD.getBigDecimal("TotalAPagar"));
						
						if( rBD.getString("idTipoDescuento") != null )
						{
							TipoDescuento tipoDescuento = new TipoDescuento(rBD.getInt("idTipoDescuento"), rBD.getString("descTipoDescuento"));
							costo.setTipoDescuento(tipoDescuento);
							
						}
						
					}
					
					setCosto(costo);
					

					// Finalmente los contactos de la gestión
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

			} catch (Exception e)
			{
				e.printStackTrace();
				conexion.rollback();
			}

		} catch (Exception e)
		{
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,
					"Excepción",
					"Ha ocurrido una excepción al actualizar los datos de la gestión, favor de contactar con el desarrollador del sistema."));

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

	}

	public void crearGestionBD()
	{
		PreparedStatement	prep	= null;
		ResultSet			rBD		= null;

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

			} catch (Exception e)
			{
				e.printStackTrace();
				conexion.rollback();
			}

		} catch (Exception e)
		{
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,
					"Excepción",
					"Ha ocurrido una excepción al crear la gestión, favor de contactar con el desarrollador del sistema."));

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

	}

	public void updateTaskWunderlist()
	{
		WunderlistWSBean	wBean		= (WunderlistWSBean) FacesUtils
				.getManagedBean(UtilidadesGestion.wunderlistWSBean);
		WUsuario			wUsuario	= wBean.getwUsuario();
		wUsuario.getTareaWunderlist(this.getIdTareaWunderlist());
		setTareaW(wUsuario.getTarea());
	}

	public void updateTaskCommentsWunderlist()
	{
		if (this.tareaW == null)
		{
			updateTaskWunderlist();
		}

		WunderlistWSBean	wBean		= (WunderlistWSBean) FacesUtils
				.getManagedBean(UtilidadesGestion.wunderlistWSBean);
		WUsuario			wUsuario	= wBean.getwUsuario();

		wUsuario.getComentariosEnTarea(this.tareaW);

		Collections.reverse(this.tareaW.getComentarios());

	}


}
