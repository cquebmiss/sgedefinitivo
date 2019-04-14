package controller;

import javax.persistence.EntityManager;
import javax.transaction.Transactional;

import gui.Login;
import lombok.Getter;
import lombok.Setter;
import modelo.persistence.Decision;
import modelo.persistence.Persona;
import modelo.persistence.Usuario;
import resources.DataBase;
import util.FacesUtils;

@Getter
@Setter
public class CapturaController
{
	private EntityManager entityManagerCRM;
	private Usuario usuarioEnSesion;

	public CapturaController()
	{
		DataBase dataBaseBean = (DataBase) FacesUtils.getManagedBean("database");
		this.entityManagerCRM = dataBaseBean.getEntityManagerCRM();

		Login login = (Login) FacesUtils.getManagedBean("login");
		this.usuarioEnSesion = login.getUsuarioEnSesion();

	}

	@Transactional
	public void savePersona(Persona persona, Integer idDecision)
	{
		if( idDecision != null )
		{
			switch (idDecision)
			{
			case 0:
				persona.setDecision(new Decision(idDecision, "No acepta"));
				break;
				
			case 1:
				persona.setDecision(new Decision(idDecision, "Acepta"));
				break;
				
			case 2:
				persona.setDecision(new Decision(idDecision, "Quizá más adelante"));
				break;
			}
			
		}

		this.entityManagerCRM.getTransaction().begin();
		this.entityManagerCRM.merge(persona);
		this.entityManagerCRM.getTransaction().commit();

	}

}
