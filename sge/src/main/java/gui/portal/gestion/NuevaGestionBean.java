package gui.portal.gestion;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;

import modelo.Sesion;
import modelo.actividades.StatusActividad;
import modelo.gestion.Contacto;
import modelo.gestion.CategoriaGestion;
import modelo.gestion.Gestion;
import modelo.gestion.ListElement;
import modelo.gestion.Note;
import modelo.gestion.SeguridadSocial;
import modelo.gestion.Task;
import modelo.gestion.TaskComment;
import modelo.gestion.TipoGestion;
import modelo.gestion.WUsuario;
import resources.DataBase;
import util.FacesUtils;
import util.gestion.UtilidadesGestion;

@ManagedBean
@SessionScoped
public class NuevaGestionBean
{
	// Dado que una gestión es una actividad se comparte el catálogo de status de actividades
	private List<StatusActividad>	catStatusActividad;
	private List<TipoGestion>		catTiposGestion;
	private List<SeguridadSocial>	catSeguridadSocial;
	private List<CategoriaGestion>	catCategoriaGestion;

	private Gestion					gestion;
	private int						editarGestion;
	private String					webservice;

	private List<ListElement>		listasUsuario;
	private List<Task>				listaTareas;

	public NuevaGestionBean()
	{
		super();
		// TODO Auto-generated constructor stub
	}

	@PostConstruct
	public void postConstruct()
	{

		try
		{
			this.gestion = new Gestion();

		}
		catch (Exception e)
		{
			this.webservice = e.getMessage();
			e.printStackTrace();
		}

	}

	public void iniciaNuevaGestion()
	{
		this.catStatusActividad = UtilidadesGestion.getCatStatusActividad();
		this.catTiposGestion = UtilidadesGestion.getCatTipoGestion();
		this.catSeguridadSocial = UtilidadesGestion.getCatSeguridadSocial();
		this.catCategoriaGestion = UtilidadesGestion.getCatCategoriaGestion();

		this.gestion = new Gestion();
		//Se hardcodea en lugar de elegir desde el catálogo
		//this.gestion.setStatus(this.catStatusActividad.get(0));
		//this.gestion.setTipoGestion(new TipoGestion(-1, ""));

	}

	public void iniciaEdicionGestion(Gestion gestionEdicion)
	{
		this.catStatusActividad = UtilidadesGestion.getCatStatusActividad();
		this.catTiposGestion = UtilidadesGestion.getCatTipoGestion();
		this.catSeguridadSocial = UtilidadesGestion.getCatSeguridadSocial();
		this.catCategoriaGestion = UtilidadesGestion.getCatCategoriaGestion();

		this.gestion = gestionEdicion;

		//Se indica cual es la categoría y la seguridad social
		for (CategoriaGestion cat : this.catCategoriaGestion)
		{
			if (this.gestion.getCategoria().getIdCategoriaGestion() == cat.getIdCategoriaGestion())
			{
				this.gestion.setCategoria(cat);
			}

		}

		for (SeguridadSocial ss : this.catSeguridadSocial)
		{
			if (this.gestion.getPaciente().getSeguridadSocial().getIdSeguridadSocial() == ss.getIdSeguridadSocial())
			{
				this.gestion.getPaciente().setSeguridadSocial(ss);
			}

		}

		//Se hardcodea en lugar de elegir desde el catálogo
		//this.gestion.setStatus(this.catStatusActividad.get(0));
		//this.gestion.setTipoGestion(new TipoGestion(-1, ""));

	}

	public void actionNuevaGestion()
	{
		iniciaNuevaGestion();

		try
		{
			ExternalContext ec = FacesContext.getCurrentInstance().getExternalContext();
			ec.redirect(ec.getRequestContextPath() + "/portal/gestion/nuevagestion.jsf");
		}
		catch (IOException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void activarModoEdicion()
	{
		this.editarGestion = 0;

		this.gestion.updateAllDataBD();

		try
		{
			FacesContext.getCurrentInstance().getExternalContext().redirect("historialgestion.jsf");
		}
		catch (IOException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	//MÉTODOS PARA CONTACTOS
	public void actionEliminarContact(Contacto contacto)
	{
		this.gestion.getContactos().remove(contacto);

	}

	//FIN DE MÉTODOS PARA CONTACTOS

	public void actionGuardarSolicitud()
	{

		System.out.println("Guardando solicitud");

		if (this.gestion.getPaciente().getNombre().trim().isEmpty() || this.gestion.getSolicitud().trim().isEmpty())
		{
			String mensaje = "";

			if (this.gestion.getPaciente().getNombre().trim().isEmpty())
			{
				mensaje = "Por favor, ingrese el nombre del paciente.";
			}
			else
			{
				mensaje = "Por favor, ingrese la solicitud deseada.";
			}

			FacesContext.getCurrentInstance().addMessage(null,
					new FacesMessage(FacesMessage.SEVERITY_ERROR, "Formulario Incorrecto", mensaje));
			return;
		}

		WUsuario wUsuario = new WUsuario();
		wUsuario.initClient();
		wUsuario.initToken();

		PreparedStatement prep = null;
		ResultSet rBD = null;

		boolean nuevaGestion = this.gestion.getIdGestion() < 0 ? true : false;

		try (Connection conexion = ((DataBase) FacesUtils.getManagedBean("database")).getConnectionGestiones();)
		{

			try
			{

				//Se obtiene el Usuario de la sesión
				Sesion sesion = (Sesion) FacesUtils.getManagedBean("Sesion");
				conexion.setAutoCommit(false);
				conexion.rollback();

				if (this.gestion.getIdGestion() < 0)
				{
					int indiceComplemento = 10;
					String complementoAtributo = "";
					String complementoValor = "";

					String complementoAtributoW = "";
					String complementoValorW = "";

					if (!this.gestion.getFolio().trim().isEmpty())
					{
						complementoAtributo = ", idGestion";
						complementoValor += ",?";
					}

					if (!this.gestion.getIdTareaWunderlist().trim().isEmpty())
					{
						complementoAtributoW = ", idTareaWunderlist";
						complementoValorW += ",?";

					}

					//Primer paso capturar la gestión en la base de datos del sistema
					prep = conexion.prepareStatement(
							"INSERT INTO gestion (Descripcion,FechaRecepcion,SolicitadoA,Solicitud,DetallesGenerales,ResumenFinal,"
									+ "idUsuario,idStatusActividad,idTipoGestion,idCategoriaGestion"
									+ complementoAtributo + complementoAtributoW + ")\n"
									+ "VALUES (?, ?, ?, ?, ?, '', ?, ?, ?, ?" + complementoValor + complementoValorW
									+ ") ; ",
							PreparedStatement.RETURN_GENERATED_KEYS);

					prep.setString(1, this.gestion.getDescripcion());
					prep.setDate(2, new java.sql.Date(this.gestion.getFechaRecepcion().getTime()));
					prep.setString(3, this.gestion.getSolicitadoA());
					prep.setString(4, this.gestion.getSolicitud());
					prep.setString(5, this.gestion.getDetallesGenerales());
					prep.setInt(6, Integer.parseInt(sesion.getIdUsuario()));
					prep.setInt(7, this.gestion.getStatus().getIdStatusActividad());
					prep.setInt(8, this.gestion.getTipoGestion().getIdTipoGestion());
					prep.setInt(9, this.gestion.getCategoria().getIdCategoriaGestion());

					if (!this.gestion.getFolio().trim().isEmpty())
					{
						prep.setInt(indiceComplemento, Integer.parseInt(this.gestion.getFolio()));
						indiceComplemento++;
					}

					if (!this.gestion.getIdTareaWunderlist().trim().isEmpty())
					{
						prep.setLong(indiceComplemento, Long.parseLong(this.gestion.getIdTareaWunderlist()));
						indiceComplemento++;
					}

					prep.executeUpdate();

					if (!this.gestion.getFolio().trim().isEmpty())
					{
						this.gestion.setIdGestion(Integer.parseInt(this.gestion.getFolio()));

					}
					else
					{
						rBD = prep.getGeneratedKeys();

						if (rBD.next())
						{
							this.gestion.setIdGestion(rBD.getInt(1));
						}

						rBD.close();

					}

					prep.close();
				}
				else
				{

					prep = conexion.prepareStatement(
							"UPDATE gestion SET descripcion=?, Solicitud=?, DetallesGenerales=?, idUsuario=?, SolicitadoA=?, idCategoriaGestion=? "
									+ " WHERE idGestion=?");

					prep.setString(1, this.gestion.getDescripcion());
					prep.setString(2, this.gestion.getSolicitud());
					prep.setString(3, this.gestion.getDetallesGenerales());
					prep.setInt(4, Integer.parseInt(sesion.getIdUsuario()));
					prep.setString(5, this.gestion.getSolicitadoA());
					prep.setInt(6, this.gestion.getCategoria().getIdCategoriaGestion());
					prep.setInt(7, this.gestion.getIdGestion());

					prep.executeUpdate();

					prep.close();

				}

				//Se busca el lugar de origen en el catálogo, si existe, se utiliza el id, en caso contrario se registra y se utiliza el nuevo Id

				prep = conexion.prepareStatement("SELECT * FROM lugarresidencia WHERE descripcion = ? ");

				prep.setString(1, this.gestion.getPaciente().getLugarResidencia().getDescripcion().trim());

				rBD = prep.executeQuery();

				int idLugarResidenciaGenerado = 1;

				if (rBD.next())
				{
					idLugarResidenciaGenerado = rBD.getInt("idLugarResidencia");
				}
				else
				{
					//Se inserta y se obtiene el idGenerado

					prep.close();

					prep = conexion.prepareStatement("INSERT INTO lugarresidencia (Descripcion) VALUES (?)",
							PreparedStatement.RETURN_GENERATED_KEYS);

					prep.setString(1, this.gestion.getPaciente().getLugarResidencia().getDescripcion().trim());

					prep.executeUpdate();

					rBD = prep.getGeneratedKeys();

					if (rBD.next())
					{
						idLugarResidenciaGenerado = rBD.getInt(1);
					}

				}

				prep.close();

				if (nuevaGestion)
				{
					//Se inserta al paciente en la bd
					prep = conexion.prepareStatement(
							" INSERT INTO paciente (idGestion,Nombre,Sexo,Edad,CURP,idLugarResidencia,Diagnostico,HospitalizadoEn,"
									+ "idSeguridadSocial,Afiliacion)\n" + "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?) ",
							PreparedStatement.RETURN_GENERATED_KEYS);

					prep.setInt(1, this.gestion.getIdGestion());
					prep.setString(2, this.gestion.getPaciente().getNombre());
					prep.setString(3, this.gestion.getPaciente().getSexo());
					prep.setInt(4, this.gestion.getPaciente().getEdad());
					prep.setString(5, "");
					prep.setInt(6, idLugarResidenciaGenerado);
					prep.setString(7, this.gestion.getPaciente().getDiagnostico());
					prep.setString(8, "");
					prep.setInt(9, this.gestion.getPaciente().getSeguridadSocial().getIdSeguridadSocial());
					prep.setString(10, this.gestion.getPaciente().getAfiliacion());

					prep.executeUpdate();

					rBD = prep.getGeneratedKeys();

					if (rBD.next())
					{
						this.gestion.getPaciente().setIdPaciente(rBD.getInt(1));
					}

					prep.close();

				}
				else
				{

					prep = conexion.prepareStatement(
							"UPDATE paciente SET Nombre=?, Sexo=?, Edad=?, idLugarResidencia=?, Diagnostico=?,"
									+ "idSeguridadSocial=?, Afiliacion=? WHERE idPaciente=?");

					prep.setString(1, this.gestion.getPaciente().getNombre());
					prep.setString(2, this.gestion.getPaciente().getSexo());
					prep.setInt(3, this.gestion.getPaciente().getEdad());
					prep.setInt(4, idLugarResidenciaGenerado);
					prep.setString(5, this.gestion.getPaciente().getDiagnostico());
					prep.setInt(6, this.gestion.getPaciente().getSeguridadSocial().getIdSeguridadSocial());
					prep.setString(7, this.gestion.getPaciente().getAfiliacion());
					prep.setInt(8, this.gestion.getPaciente().getIdPaciente());

					prep.executeUpdate();
					prep.close();

					//Se borran a los contactos para antes de volver a insertar los de la edición

					prep = conexion.prepareStatement("DELETE FROM contactogestion WHERE idGestion=?");
					prep.setInt(1, this.gestion.getIdGestion());

					prep.executeUpdate();
					prep.close();

				}

				//Ahora se insertan los contactos de la gestión
				for (Contacto contacto : this.gestion.getContactos())
				{

					prep = conexion.prepareStatement(
							"INSERT INTO sge.contactogestion (idGestion,Nombre,Telefonos,Email,Observaciones)\n"
									+ "VALUES (?, ?, ?, ?, ?) ",
							PreparedStatement.RETURN_GENERATED_KEYS);

					prep.setInt(1, this.gestion.getIdGestion());
					prep.setString(2, contacto.getNombres());
					prep.setString(3, contacto.getTelefonos());
					prep.setString(4, contacto.getEmail());
					prep.setString(5, contacto.getObservaciones());

					prep.executeUpdate();

					rBD = prep.getGeneratedKeys();

					if (rBD.next())
					{
						contacto.setIdContacto(rBD.getInt(1));
					}

					prep.close();

				}

				//Se crea la tarea que se va a registrar en el WebService 
				Task nuevaTarea = new Task();

				nuevaTarea = new Task();
				nuevaTarea.setList_id(UtilidadesGestion.idListaGestiones);

				LocalDate ldt = LocalDate.now();

				String folio = "Folio: F-" + ldt.getYear() + "-" + this.gestion.getIdGestion() + ". - Paciente: "
						+ this.gestion.getPaciente().getNombre();

				nuevaTarea.setTitle(folio);
				nuevaTarea.setCompleted(false);
				nuevaTarea.setStarred(false);

				if (nuevaGestion && this.gestion.getFolio().trim().isEmpty())
				{
					//Devuelve la tarea creada en el atributo Tarea del objeto
					wUsuario.postTareaWunderlist(nuevaTarea);
				}
				else
				{
					//Se obtiene la tarea a ser editada con el id en el objeto gestión
					wUsuario.getTareaWunderlist(this.gestion.getIdTareaWunderlist());
				}

				//Se actualiza en la base de datos el id de la tarea de wunderlist y se añade a su registro
				//Se inserta al paciente en la bd
				prep = conexion.prepareStatement(
						" UPDATE gestion SET descripcion=?, idTareaWunderlist=?, idListaWunderlist=? WHERE idGestion=? ");

				prep.setString(1, folio);
				prep.setString(2, "" + wUsuario.getTarea().getId());
				prep.setString(3, "" + nuevaTarea.getList_id());
				prep.setInt(4, this.gestion.getIdGestion());

				prep.executeUpdate();

				conexion.commit();

				String contenidoNota = "Fecha de Recepción: "
						+ new SimpleDateFormat("yyyy-MM-dd - HH:mm:dd").format(this.gestion.getFechaRecepcion()) + "\n";
				contenidoNota += "Usuario: " + sesion.getIdUsuario() + " - " + sesion.getNombreUsuario() + "\n\n";
				contenidoNota += "Solicitado por: " + this.gestion.getSolicitadoA() + "\n\n";
				contenidoNota += "Categoría de la gestión: " + this.gestion.getCategoria().getDescripcion() + "\n\n";
				contenidoNota += "Paciente: \n";
				contenidoNota += "Nombre: " + this.gestion.getPaciente().getNombre() + "\n";
				contenidoNota += "Edad: " + this.gestion.getPaciente().getEdad() + "\n";
				contenidoNota += "Sexo: "
						+ (this.gestion.getPaciente().getSexo().equals("m") ? "Masculino" : "Femenino") + "\n";
				contenidoNota += "Lugar de Origen: " + this.gestion.getPaciente().getLugarResidencia().getDescripcion()
						+ "\n";
				contenidoNota += "Seguridad Social: " + this.gestion.getPaciente().getSeguridadSocial().getDescripcion()
						+ "\n";
				contenidoNota += "Número o Folio de Afiliación: " + this.gestion.getPaciente().getAfiliacion() + "\n\n";
				contenidoNota += "<DIAGNÓSTICO>: " + this.gestion.getPaciente().getDiagnostico() + "\n\n";
				contenidoNota += "<OBSERVACIONES DEL CASO>: " + this.gestion.getDetallesGenerales() + "\n\n";
				contenidoNota += "<SOLICITUD>: " + this.gestion.getSolicitud() + "\n\n";
				contenidoNota += "Contactos: \n";

				for (Contacto contacto : this.gestion.getContactos())
				{
					contenidoNota += " - " + contacto.getNombres() + " "
							+ (contacto.getTelefonos().isEmpty() ? "" : ", Teléfonos: " + contacto.getTelefonos()) + " "
							+ (contacto.getEmail().isEmpty() ? "" : ", Email: " + contacto.getEmail()) + " "
							+ (contacto.getObservaciones().isEmpty() ? ""
									: ", Observación: " + contacto.getObservaciones())
							+ "\n";

				}

				if (nuevaGestion && this.gestion.getFolio().trim().isEmpty())
				{
					Note nota = new Note();
					nota.setTask_id(wUsuario.getTarea().getId());
					nota.setContent(contenidoNota);

					wUsuario.postNotaTareaWunderlist(nota);
					iniciaNuevaGestion();

					FacesContext.getCurrentInstance().addMessage(null,
							new FacesMessage(FacesMessage.SEVERITY_INFO, "Solicitud Enviada, Folio: " + folio,
									"La solicitud ha sido registrada exitosamente en el sistema."));

					System.out.println("Solicitud enviada");

				}
				else
				{
					
					//Se añade un comentario a la tarea indicando que la tarea o gestión fue editada
					wUsuario.getTareaWunderlist(this.gestion.getIdTareaWunderlist());

					//se pachea la nota
					wUsuario.getNotaTareaWunderlist(wUsuario.getTarea());

					//Se pachea la nota
					wUsuario.getNotas().get(0).setContent(contenidoNota);

					wUsuario.patchNotaWunderlist(wUsuario.getNotas().get(0));

					TaskComment taskComment = new TaskComment();
					taskComment.setTask_id(wUsuario.getTarea().getId());
					taskComment.setList_id(wUsuario.getTarea().getList_id());
					taskComment.setText("Datos Editados");

					wUsuario.postComentarioTareaWunderlist(taskComment);

					//Se actualiza el título de la tarea
					wUsuario.getTareaWunderlist(this.gestion.getIdTareaWunderlist());
					wUsuario.getTarea().setTitle(folio);
					wUsuario.patchTareaWunderlist(wUsuario.getTarea());

					FacesContext.getCurrentInstance().addMessage(null,
							new FacesMessage(FacesMessage.SEVERITY_INFO, "Solicitud Editada, Folio: " + folio,
									"La solicitud ha sido editada exitosamente en el sistema."));

					System.out.println("Solicitud editada");

				}

			}
			catch (Exception e)
			{
				e.printStackTrace();
				conexion.rollback();
			}

		}
		catch (

		Exception e)
		{
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Excepción",
					"Ha ocurrido una excepción al registrar el folio de la gestión, favor de contactar con el desarrollador del sistema."));

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

		/*
		 * wUsuario.getListsWunderlist(); wUsuario.getListWunderlist(338456892);
		 * 
		 * this.listasUsuario = new ArrayList<>();
		 * this.listasUsuario.add(wUsuario.getLista()); this.webservice =
		 * wUsuario.getAccess_token().getAccess_token();
		 * 
		 * //Crear una tarea Task nuevaTarea = new Task();
		 * nuevaTarea.setList_id(wUsuario.getLista().getId()); nuevaTarea.setTitle(
		 * "Ignorar: Pruebas SGE: Folio-" + this.gestion.getFechaRecepcion() +
		 * "-Nombre/Descripción Asunto"); nuevaTarea.setCompleted(false);
		 * nuevaTarea.setStarred(true);
		 * 
		 * wUsuario.postTareaWunderlist(nuevaTarea); this.webservice =
		 * wUsuario.getTarea().getTitle();
		 * 
		 * // wUsuario.getTarea().setTitle("IGNORAR:" + wUsuario.getTarea().getId() +
		 * " MODIFICADO por SGE");
		 * 
		 * // wUsuario.patchTareaWunderlist(wUsuario.getTarea()); this.webservice =
		 * wUsuario.getTarea().getTitle();
		 * 
		 * wUsuario.getNotaTareaWunderlist(wUsuario.getTarea());
		 * 
		 * Note nota = new Note(); nota.setTask_id(wUsuario.getTarea().getId());
		 * nota.setContent(
		 * "Prueba de la creación de las notas desde la plataforma del SGE modificado desde el código"
		 * );
		 * 
		 * wUsuario.postNotaTareaWunderlist(nota);
		 * 
		 * System.out.println("Contenido de nota: " + wUsuario.getNota().getContent());
		 */
	}

	public void desactivarModoEdicion()
	{
		this.editarGestion = -1;
	}

	public Gestion getGestion()
	{
		return gestion;
	}

	public void setGestion(Gestion gestion)
	{
		this.gestion = gestion;
	}

	public int getEditarGestion()
	{
		return editarGestion;
	}

	public void setEditarGestion(int editarGestion)
	{
		this.editarGestion = editarGestion;
	}

	public List<StatusActividad> getCatStatusActividad()
	{
		return catStatusActividad;
	}

	public void setCatStatusActividad(List<StatusActividad> catStatusActividad)
	{
		this.catStatusActividad = catStatusActividad;
	}

	public List<TipoGestion> getCatTiposGestion()
	{
		return catTiposGestion;
	}

	public void setCatTiposGestion(List<TipoGestion> catTiposGestion)
	{
		this.catTiposGestion = catTiposGestion;
	}

	public List<SeguridadSocial> getCatSeguridadSocial()
	{
		return catSeguridadSocial;
	}

	public void setCatSeguridadSocial(List<SeguridadSocial> catSeguridadSocial)
	{
		this.catSeguridadSocial = catSeguridadSocial;
	}

	public String getWebservice()
	{
		return webservice;
	}

	public void setWebservice(String webservice)
	{
		this.webservice = webservice;
	}

	public List<ListElement> getListasUsuario()
	{
		return listasUsuario;
	}

	public void setListasUsuario(List<ListElement> listasUsuario)
	{
		this.listasUsuario = listasUsuario;
	}

	public List<Task> getListaTareas()
	{
		return listaTareas;
	}

	public void setListaTareas(List<Task> listaTareas)
	{
		this.listaTareas = listaTareas;
	}

	public List<CategoriaGestion> getCatCategoriaGestion()
	{
		return catCategoriaGestion;
	}

	public void setCatCategoriaGestion(List<CategoriaGestion> catCategoriaGestion)
	{
		this.catCategoriaGestion = catCategoriaGestion;
	}

}
