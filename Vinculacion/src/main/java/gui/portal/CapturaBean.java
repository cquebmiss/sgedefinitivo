package gui.portal;

import java.io.Serializable;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;

import controller.CapturaController;
import controller.DashBoardController;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import modelo.persistence.Persona;

@ManagedBean
@SessionScoped
@Getter
@Setter
@NoArgsConstructor
public class CapturaBean implements Serializable
{
	DashBoardController dashBoardController;
	CapturaController capturaController;

	// Tabla de usuarios
	private List<Persona> personasEntrevistadas;
	private List<Persona> personasEntrevistadasFilter;
	private Persona personaSeleccionada;
	private Integer idDecision;

	@PostConstruct
	public void postConstruct()
	{
		this.dashBoardController = new DashBoardController();
		this.personasEntrevistadas = this.dashBoardController.getPersonasEntrevistadas();

		this.capturaController = new CapturaController();

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
	}

	public void actionGuardarDecision()
	{
		this.capturaController.savePersona(this.personaSeleccionada, this.idDecision);
	}
	
	public void actionGuardarFormularioAcepta()
	{
		this.capturaController.savePersona(this.personaSeleccionada, null);
		actionDetenerCaptura();
	}

}
