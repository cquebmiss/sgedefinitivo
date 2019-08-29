package controller;

import java.util.List;

import javax.persistence.EntityManager;

import gui.Login;
import lombok.Getter;
import lombok.Setter;
import modelo.persistence.Persona;
import persistence.dynamodb.Usuario;
import resources.DataBase;
import util.FacesUtils;

@Getter
@Setter
public class DashBoardController
{
	private EntityManager entityManagerCRM;
	private Usuario usuarioEnSesion;

	public DashBoardController()
	{
		DataBase dataBaseBean = (DataBase) FacesUtils.getManagedBean("database");
		this.entityManagerCRM = dataBaseBean.getEntityManagerCRM();

		Login login = (Login) FacesUtils.getManagedBean("login");
		this.usuarioEnSesion = login.getUsuarioAWS();

	}
	
	public List<Persona> getPersonasNoEntrevistadas()
	{
		return this.entityManagerCRM.createQuery(
				"FROM persona p WHERE p.decision.idDecision = -1 AND (p.localidad.idEstado,p.localidad.idMunicipio,p.localidad.idLocalidad) "
				+ "IN ( SELECT cnf.confUsuarioPK.idEstado,cnf.confUsuarioPK.idMunicipio,cnf.confUsuarioPK.idLocalidad FROM confusuario cnf "
				+ "WHERE cnf.confUsuarioPK.idUsuario= :idUsuario)",
				Persona.class).setParameter("idUsuario", Integer.parseInt(this.usuarioEnSesion.getIdUsuario())).getResultList();
	}

	public List<Persona> getPersonasEntrevistadas()
	{
		return this.entityManagerCRM.createQuery(
				"FROM persona p WHERE p.decision.idDecision > -1 AND (p.localidad.idEstado,p.localidad.idMunicipio,p.localidad.idLocalidad) "
				+ "IN ( SELECT cnf.confUsuarioPK.idEstado,cnf.confUsuarioPK.idMunicipio,cnf.confUsuarioPK.idLocalidad FROM confusuario cnf "
				+ "WHERE cnf.confUsuarioPK.idUsuario= :idUsuario)",
				Persona.class).setParameter("idUsuario", Integer.parseInt(this.usuarioEnSesion.getIdUsuario())).getResultList();
	}

}
