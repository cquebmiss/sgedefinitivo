package gui.portal;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;

import org.primefaces.model.DualListModel;

import controller.AdministracionController;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import modelo.persistence.ConfUsuario;
import modelo.persistence.ConfUsuarioPK;
import modelo.persistence.Localidad;
import modelo.persistence.PermisoUsuario;
import modelo.persistence.StatusUsuario;
import modelo.persistence.Usuario;

@ManagedBean
@SessionScoped
@Getter
@Setter
@NoArgsConstructor
public class AdministracionBean
{
	private int tipoPanel;

	private AdministracionController administracionController;

	// Tabla de usuarios
	private List<Usuario> usuariosSistema;
	private List<Usuario> usuariosSistemaFilter;
	private Usuario usuarioSeleccionado;

	private DualListModel<Localidad> localidadesDualListModel;
	private List<Localidad> catLocalidades;

	private List<StatusUsuario> catStatusUsuario;
	private List<PermisoUsuario> catPermisoUsuario;

	@PostConstruct
	public void postConstruct()
	{
		this.administracionController = new AdministracionController();
		setTipoPanel(0);
	}

	private void inicializaFormulario()
	{
		this.catLocalidades = this.administracionController.getLocalidadesSistema();

		this.localidadesDualListModel = new DualListModel<Localidad>(this.administracionController.getLocalidadesSistema(), new ArrayList<Localidad>());
		this.catStatusUsuario = this.administracionController.getCatStatusUsuario();
		this.catPermisoUsuario = this.administracionController.getCatPermisoUsuario();
	}

	public void actionSeleccionUsuario(Usuario usuario)
	{
		setUsuarioSeleccionado(usuario);
		inicializaFormulario();
		setTipoPanel(2);

		List<Localidad> source = this.localidadesDualListModel.getSource();
		List<Localidad> target = this.localidadesDualListModel.getTarget();

		for (ConfUsuario confUsu : usuario.getConfUsuario())
		{
			for (int x = 0; x < source.size(); x++)
			{

				Localidad catLoc = source.get(x);

				if ((confUsu.getConfUsuarioPK().getIdEstado() + "-" + confUsu.getConfUsuarioPK().getIdMunicipio() + "-"
						+ confUsu.getConfUsuarioPK().getIdLocalidad()).equalsIgnoreCase(
								catLoc.getIdEstado() + "-" + catLoc.getIdMunicipio() + "-" + catLoc.getIdLocalidad()))
				{
					target.add(catLoc);
					source.remove(x);

				}

			}

		}

	}

	public void actionNuevoUsuario()
	{
		this.usuarioSeleccionado = new Usuario();
		inicializaFormulario();
		setTipoPanel(2);
	}

	public void actionGetUsuariosSistema()
	{
		setUsuariosSistema(this.administracionController.getUsuariosSistema());
		setTipoPanel(1);
	}

	public void actionGuardarUsuario()
	{

		if (this.usuarioSeleccionado.getPermisoUsuario().getIdPermisoUsuario() == 1
				&& this.localidadesDualListModel.getTarget().isEmpty())
		{
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,
					"Localidades Asignadas", "El usuario no tiene localidades seleccionadas"));
		} else
		{
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,
					"Administrador", "El usuario no necesita localidades asignadas"));
		}

		this.administracionController.saveUsuario(this.usuarioSeleccionado, this.localidadesDualListModel.getTarget(),
				this.catLocalidades);

		setTipoPanel(1);

	}

}
