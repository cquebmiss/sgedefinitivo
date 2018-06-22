package gui.portal.gestion;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;

import org.primefaces.PrimeFaces;

import modelo.Sesion;
import modelo.gestion.Gestion;
import modelo.gestion.Task;
import modelo.gestion.TaskComment;
import modelo.gestion.WUsuario;
import resources.DataBase;
import util.FacesUtils;
import util.gestion.UtilidadesGestion;

@ManagedBean
@ViewScoped
public class HistorialGestionBean
{
	private List<Gestion>	gestionesActivas;
	private List<Gestion>	gestionesActivasFilter;
	private Gestion			gestionActivaSelec;

	private List<Gestion>	gestionesFinalizadas;
	private List<Gestion>	gestionesFinalizadasFilter;
	private Gestion			gestinoFinalizadaSelec;

	public HistorialGestionBean()
	{
		super();
		// TODO Auto-generated constructor stub
	}

	@PostConstruct
	public void postConstruct()
	{
		getHistorialGestiones();
	}

	private void getHistorialGestiones()
	{
		Sesion sesion = (Sesion) FacesUtils.getManagedBean("Sesion");

		this.gestionesActivas = UtilidadesGestion.getGestionesActivas(Integer.parseInt(sesion.getIdUsuario()));
		this.gestionesFinalizadas = UtilidadesGestion.getGestionesFinalizadas(Integer.parseInt(sesion.getIdUsuario()));

	}

	public void actionHistorialGestiones()
	{
		getHistorialGestiones();

		try
		{
			ExternalContext ec = FacesContext.getCurrentInstance().getExternalContext();
			ec.redirect(ec.getRequestContextPath() + "/portal/gestion/historialgestion.jsf");
		}
		catch (IOException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void actionEdicionGestion(Gestion gestion)
	{
		try
		{
			gestion.updateAllDataBD();
			NuevaGestionBean bean = (NuevaGestionBean) FacesUtils.getManagedBean("nuevaGestionBean");
			bean.iniciaEdicionGestion(gestion);

			ExternalContext ec = FacesContext.getCurrentInstance().getExternalContext();
			ec.redirect(ec.getRequestContextPath() + "/portal/gestion/nuevagestion.jsf");
		}
		catch (IOException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	//La gestión seleccionada
	public void actionFinalizarGestion()
	{
		String resumenFinal = this.gestionActivaSelec.getResumenFinal();

		this.gestionActivaSelec.updateAllDataBD();

		PreparedStatement prep = null;

		try (Connection conexion = ((DataBase) FacesUtils.getManagedBean("database")).getConnectionGestiones();)
		{
			prep = conexion.prepareStatement(
					"UPDATE gestion SET idStatusActividad=?, ResumenFinal=?, FechaFinalizacion=? WHERE idGestion=?");
			prep.setInt(1, 1);
			prep.setString(2, resumenFinal);
			prep.setDate(3, new java.sql.Date(new java.util.Date().getTime()));
			prep.setInt(4, this.gestionActivaSelec.getIdGestion());

			prep.executeUpdate();

		}
		catch (

		Exception e)
		{
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Excepción",
					"Ha ocurrido una excepción al finalizar la gestión, favor de contactar con el desarrollador del sistema."));

			e.printStackTrace();

			return;
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

		WUsuario wUsuario = new WUsuario();
		wUsuario.initClient();
		wUsuario.initToken();

		wUsuario.getTareasListWunderlist(this.gestionActivaSelec.getIdListaWunderlist());

		boolean gestionConcluida = false;

		for (Task tarea : wUsuario.getTareas())
		{
			System.out.println("Id tarea: " + tarea.getId());
			if (("" + tarea.getId()).equalsIgnoreCase(this.gestionActivaSelec.getIdTareaWunderlist()))
			{

				//Se obtiene la nota de la tarea para añadirle la conclusión del caso
				wUsuario.getNotaTareaWunderlist(tarea);

				wUsuario.getNotas().get(0).setContent(
						wUsuario.getNotas().get(0).getContent() + "\n\n <CONCLUSION DEL CASO>\n" + resumenFinal);

				wUsuario.patchNotaWunderlist(wUsuario.getNotas().get(0));

				//Se crea el comentario de gestión concluida 
				TaskComment taskComment = new TaskComment();
				taskComment.setTask_id(tarea.getId());
				taskComment.setList_id(tarea.getList_id());
				taskComment.setText("Gestión Concluida desde SGE: " + resumenFinal);

				wUsuario.postComentarioTareaWunderlist(taskComment);

				//Se obtiene nuevamente la tarea para actualizar la revision de la misma
				wUsuario.getTareaWunderlist("" + tarea.getId());

				wUsuario.getTarea().setCompleted(true);

				wUsuario.patchTareaWunderlist(wUsuario.getTarea());

				gestionConcluida = true;

				FacesContext.getCurrentInstance().addMessage(null,
						new FacesMessage(FacesMessage.SEVERITY_INFO, "Gestión Finalizada", "La gestión con folio: "
								+ this.gestionActivaSelec.getDescripcion() + " ha sido finalizada exitosamente."));

				break;

			}

		}

		if (gestionConcluida)
		{
			getHistorialGestiones();
		}
		else
		{
			FacesContext.getCurrentInstance().addMessage(null,
					new FacesMessage(FacesMessage.SEVERITY_ERROR, "Gestión NO Finalizada",
							"Han habido algunos problemas con el WebService, favor de contactar con el desarrollador"));
		}

		PrimeFaces.current().ajax().addCallbackParam("concluido", gestionConcluida);

	}

	public List<Gestion> getGestionesActivas()
	{
		return gestionesActivas;
	}

	public void setGestionesActivas(List<Gestion> gestionesActivas)
	{
		this.gestionesActivas = gestionesActivas;
	}

	public List<Gestion> getGestionesFinalizadas()
	{
		return gestionesFinalizadas;
	}

	public void setGestionesFinalizadas(List<Gestion> gestionesFinalizadas)
	{
		this.gestionesFinalizadas = gestionesFinalizadas;
	}

	public List<Gestion> getGestionesActivasFilter()
	{
		return gestionesActivasFilter;
	}

	public void setGestionesActivasFilter(List<Gestion> gestionesActivasFilter)
	{
		this.gestionesActivasFilter = gestionesActivasFilter;
	}

	public Gestion getGestionActivaSelec()
	{
		return gestionActivaSelec;
	}

	public void setGestionActivaSelec(Gestion gestionActivaSelec)
	{
		this.gestionActivaSelec = gestionActivaSelec;
	}

	public List<Gestion> getGestionesFinalizadasFilter()
	{
		return gestionesFinalizadasFilter;
	}

	public void setGestionesFinalizadasFilter(List<Gestion> gestionesFinalizadasFilter)
	{
		this.gestionesFinalizadasFilter = gestionesFinalizadasFilter;
	}

	public Gestion getGestinoFinalizadaSelec()
	{
		return gestinoFinalizadaSelec;
	}

	public void setGestinoFinalizadaSelec(Gestion gestinoFinalizadaSelec)
	{
		this.gestinoFinalizadaSelec = gestinoFinalizadaSelec;
	}

}
