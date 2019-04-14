package controller;

import java.util.List;

import javax.persistence.EntityManager;

import gui.Login;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import modelo.persistence.ConfUsuario;
import modelo.persistence.Persona;
import modelo.persistence.Usuario;
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
		this.usuarioEnSesion = login.getUsuarioEnSesion();

	}

	public List<Persona> getPersonasEntrevistadas()
	{
		return this.entityManagerCRM.createQuery(
				"FROM persona p WHERE (p.localidad.idEstado,p.localidad.idMunicipio,p.localidad.idLocalidad) "
				+ "IN ( SELECT cnf.confUsuarioPK.idEstado,cnf.confUsuarioPK.idMunicipio,cnf.confUsuarioPK.idLocalidad FROM confusuario cnf "
				+ "WHERE cnf.confUsuarioPK.idUsuario= :idUsuario)",
				Persona.class).setParameter("idUsuario", this.usuarioEnSesion.getIdUsuario()).getResultList();
	}

}
