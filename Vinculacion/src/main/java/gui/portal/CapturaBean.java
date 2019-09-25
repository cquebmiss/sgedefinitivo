package gui.portal;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;

import controller.AdministracionController;
import controller.CapturaController;
import controller.DashBoardController;
import gui.Login;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import persistence.dynamodb.Estado;
import persistence.dynamodb.LocalidadConf;
import persistence.dynamodb.Municipio;
import persistence.dynamodb.Persona;
import util.FacesUtils;

@ManagedBean
@SessionScoped
@Getter
@Setter
@NoArgsConstructor
public class CapturaBean implements Serializable
{
	DashBoardController					dashBoardController;
	CapturaController					capturaController;

	private AdministracionController	administracionController;

	// Tabla de usuarios
	private List<Persona>				personasNoEntrevistadas;
	private List<Persona>				personasNoEntrevistadasFilter;
	private Persona						personaNoEntrevistadaSeleccionada;

	private List<Persona>				personasEntrevistadas;
	private List<Persona>				personasEntrevistadasFilter;
	private Persona						personaSeleccionada;
	private Persona						personaAWS;

	private Integer						idDecision;

	private String						cve_agee;
	private String						cve_agem;
	private String						cve_loc;
	private List<String>				catLocalidades;
	private String						localidadSeleccionada;

	@PostConstruct
	public void postConstruct()
	{
		this.dashBoardController = new DashBoardController();
		this.capturaController = new CapturaController();
		this.catLocalidades = new ArrayList<>();
		updateTablas();

	}

	public void initAdministracionController()
	{
		if (this.administracionController == null)
		{
			this.administracionController = new AdministracionController();
		}
	}

	public void updateTablas()
	{
		Login loginBean = (Login) FacesUtils.getManagedBean("login");
		
		this.personasNoEntrevistadas = this.dashBoardController.getPersonasNoEntrevistadasAWS(loginBean.getUsuarioAWS().getLocalidades());
		this.personasEntrevistadas = this.dashBoardController.getPersonasEntrevistadasAWS(loginBean.getUsuarioAWS().getLocalidades());

	}

	public void actionNuevaPersona()
	{
		// Crea el usuario en AWS y sigue el mismo flujo del formulario
		this.personaAWS = new Persona();
		this.personaAWS.setLocalidad(new LocalidadConf());
	}

	public void actionBuscarLocalidad()
	{
		initAdministracionController();

		if (this.personaAWS.getLocalidad().getIdEstado().trim().isEmpty()
				|| this.personaAWS.getLocalidad().getIdMunicipio().trim().isEmpty())
		{
			return;
		}

		this.catLocalidades = this.administracionController.getLocalidadesSistemaAWS(
				this.personaAWS.getLocalidad().getIdEstado(), this.personaAWS.getLocalidad().getIdMunicipio(),
				this.personaAWS.getLocalidad().getIdLocalidad());

	}

	public void actionPersonaSelect(Persona personaSeleccionada)
	{
		this.personaSeleccionada = personaSeleccionada;
		this.idDecision = -1;
	}

	public void actionDetenerCaptura()
	{
		this.personaSeleccionada = null;
		this.personasEntrevistadasFilter = null;
		this.idDecision = -1;
		this.personaAWS = null;
		this.localidadSeleccionada = null;
	}

	public void actionCrearPersona()
	{
		Login loginBean = (Login) FacesUtils.getManagedBean("login");

		List<Estado> catEstados = this.administracionController.getCatEstadosAWS();
		List<Municipio> municipioAWS = this.administracionController.getMunicipoAWS(
				this.personaAWS.getLocalidad().getIdEstado(), this.personaAWS.getLocalidad().getIdMunicipio());

		Optional<Estado> estadoAGuardar = catEstados.stream()
				.filter(estado -> estado.getCve_agee().equalsIgnoreCase(this.personaAWS.getLocalidad().getIdEstado()))
				.findFirst();
		Estado estadoEncontrado = (Estado) estadoAGuardar.get();

		this.personaAWS.getLocalidad().setDescripcionEstado(estadoEncontrado.getNom_agee());
		this.personaAWS.getLocalidad().setDescripcionMunicipio(municipioAWS.get(0).getNom_agem());
		this.personaAWS.getLocalidad()
				.setIdLocalidad(this.localidadSeleccionada.substring(5, this.localidadSeleccionada.indexOf("-")));

		String descripcionLocalidad = this.localidadSeleccionada.substring(this.localidadSeleccionada.indexOf("-") + 2,
				this.localidadSeleccionada.length());
		this.personaAWS.getLocalidad().setDescripcionLocalidad(descripcionLocalidad);

		// Se valida que no exista una persona igual en la base de datos
		if (this.capturaController.existePersona(this.personaAWS.getNombres(), this.personaAWS.getApPaterno(),
				this.personaAWS.getApMaterno(), this.personaAWS.getSexo(), this.personaAWS.getLocalidad().getIdEstado(),
				this.personaAWS.getLocalidad().getDescripcionEstado(), this.personaAWS.getLocalidad().getIdMunicipio(),
				this.personaAWS.getLocalidad().getDescripcionMunicipio(),
				this.personaAWS.getLocalidad().getIdLocalidad(),
				this.personaAWS.getLocalidad().getDescripcionLocalidad()))
		{

			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,
					"Registro Duplicado", "La persona ya se encuentra dada de alta en la base de datos."));

			return;
		}

		this.capturaController.createNuevaPersona(this.personaAWS.getNombres(), this.personaAWS.getApPaterno(),
				this.personaAWS.getApMaterno(), this.personaAWS.getSexo(), this.personaAWS.getLocalidad().getIdEstado(),
				this.personaAWS.getLocalidad().getDescripcionEstado(), this.personaAWS.getLocalidad().getIdMunicipio(),
				this.personaAWS.getLocalidad().getDescripcionMunicipio(),
				this.personaAWS.getLocalidad().getIdLocalidad(),
				this.personaAWS.getLocalidad().getDescripcionLocalidad(), loginBean.getUsuarioAWS().getIdUsuario(),
				loginBean.getUsuarioAWS().getNombre());

		updateTablas();
		actionDetenerCaptura();

	}

	public void actionCancelarCrearPersona()
	{
		actionDetenerCaptura();
	}

	public void actionGuardarDecision()
	{
		Login loginBean = (Login) FacesUtils.getManagedBean("login");
		this.personaSeleccionada.setIdUsuarioCaptura(loginBean.getUsuarioAWS().getIdUsuario());
		this.personaSeleccionada.setNombreUsuarioCaptura(loginBean.getUsuarioAWS().getNombre());
		
		this.capturaController.savePersona(this.personaSeleccionada, this.idDecision);
		updateTablas();

		// Si se ha seleccionado que no
		if (this.idDecision == 0)
		{
			actionDetenerCaptura();
		}
	}

	public void actionGuardarFormularioAcepta()
	{
		Login loginBean = (Login) FacesUtils.getManagedBean("login");
		this.personaSeleccionada.setIdUsuarioCaptura(loginBean.getUsuarioAWS().getIdUsuario());
		this.personaSeleccionada.setNombreUsuarioCaptura(loginBean.getUsuarioAWS().getNombre());
		
		this.capturaController.savePersona(this.personaSeleccionada, null);
		actionDetenerCaptura();

	}

	public void actionGuardarPersona(Persona persona)
	{
		Login loginBean = (Login) FacesUtils.getManagedBean("login");
		persona.setIdUsuarioCaptura(loginBean.getUsuarioAWS().getIdUsuario());
		persona.setNombreUsuarioCaptura(loginBean.getUsuarioAWS().getNombre());

		this.capturaController.savePersona(persona, null);

	}

}
