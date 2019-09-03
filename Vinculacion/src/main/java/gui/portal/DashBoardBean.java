package gui.portal;

import java.io.Serializable;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;

import controller.DashBoardController;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@ManagedBean
@SessionScoped
@Getter
@Setter
@NoArgsConstructor
public class DashBoardBean implements Serializable
{
	DashBoardController							dashBoardController;

	// Tabla de usuarios
	private List<persistence.dynamodb.Persona>	personasEntrevistadas;
	private List<persistence.dynamodb.Persona>	personasEntrevistadasFilter;

	@PostConstruct
	public void postConstruct()
	{
		this.dashBoardController = new DashBoardController();

		this.personasEntrevistadas = this.dashBoardController.getPersonasEntrevistadasAWS();
	}

}
