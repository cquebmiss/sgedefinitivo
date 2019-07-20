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
	private List<Persona> personasNoEntrevistadas;
	private List<Persona> personasNoEntrevistadasFilter;
	private Persona personaNoEntrevistadaSeleccionada;
	
	private List<Persona> personasEntrevistadas;
	private List<Persona> personasEntrevistadasFilter;
	private Persona personaSeleccionada;
	private Integer idDecision;

	@PostConstruct
	public void postConstruct()
	{
		this.dashBoardController = new DashBoardController();
		this.capturaController = new CapturaController();
		updateTablas();

	}
	
	public void updateTablas()
	{
		this.personasNoEntrevistadas = this.dashBoardController.getPersonasNoEntrevistadas();
		this.personasEntrevistadas = this.dashBoardController.getPersonasEntrevistadas();
		
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
	}

	public void actionGuardarDecision()
	{
		this.capturaController.savePersona(this.personaSeleccionada, this.idDecision);
		updateTablas();
		
		//Si se ha seleccionado que no
		if( this.idDecision == 0 )
		{
			actionDetenerCaptura();
		}
	}
	
	public void actionGuardarFormularioAcepta()
	{
		this.capturaController.savePersona(this.personaSeleccionada, null);
		actionDetenerCaptura();
		
	}
	
	public void actionGuardarPersona(Persona persona)
	{
		this.capturaController.savePersona(persona, null);
		
	}

}
