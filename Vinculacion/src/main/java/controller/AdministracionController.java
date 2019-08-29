package controller;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityExistsException;
import javax.persistence.EntityManager;

import gui.Login;
import lombok.Getter;
import lombok.Setter;
import modelo.persistence.ConfUsuario;
import modelo.persistence.ConfUsuarioPK;
import modelo.persistence.Localidad;
import modelo.persistence.PermisoUsuario;
import modelo.persistence.StatusUsuario;
import persistence.dynamodb.Usuario;
import resources.DataBase;
import util.FacesUtils;

@Getter
@Setter
public class AdministracionController
{
	private EntityManager entityManagerCRM;
	private Usuario usuarioEnSesion;

	public AdministracionController()
	{
		DataBase dataBaseBean = (DataBase) FacesUtils.getManagedBean("database");
		this.entityManagerCRM = dataBaseBean.getEntityManagerCRM();

		Login login = (Login) FacesUtils.getManagedBean("login");
		this.usuarioEnSesion = login.getUsuarioAWS();

	}

	public List<Usuario> getUsuariosSistema()
	{
		return this.entityManagerCRM.createQuery("FROM usuario u", Usuario.class).getResultList();
	}

	public List<Localidad> getLocalidadesSistema()
	{
		return this.entityManagerCRM.createQuery("FROM localidad order by descripcion asc", Localidad.class)
				.getResultList();
	}

	public List<PermisoUsuario> getCatPermisoUsuario()
	{
		return this.entityManagerCRM.createQuery("FROM permisousuario order by descripcion asc", PermisoUsuario.class)
				.getResultList();
	}

	public List<StatusUsuario> getCatStatusUsuario()
	{
		return this.entityManagerCRM.createQuery("FROM statususuario order by descripcion asc", StatusUsuario.class)
				.getResultList();
	}

	public void saveUsuario(Usuario usuario, List<Localidad> localidadesSelec, List<Localidad> catLocalidades)
	{
		this.entityManagerCRM.getTransaction().begin();

		if (usuario.getIdUsuario() == null)
		{
			this.entityManagerCRM.persist(usuario);
		}
		
	//	usuario.setConfUsuario(null);

		List<ConfUsuario> listaConfUsuario = new ArrayList<>();

		for (Localidad locString : localidadesSelec)
		{
			for (Localidad loc : catLocalidades)
			{
				if ((loc.toString()).equalsIgnoreCase(locString.toString()))
				{
					ConfUsuario confUsuario = new ConfUsuario();
					ConfUsuarioPK pk = new ConfUsuarioPK();
			//		pk.setIdUsuario(usuario.getIdUsuario());
					pk.setIdEstado(loc.getIdEstado());
					pk.setIdMunicipio(loc.getIdMunicipio());
					pk.setIdLocalidad(loc.getIdLocalidad());

					confUsuario.setConfUsuarioPK(pk);

					listaConfUsuario.add(confUsuario);

					break;
				}
			}

		}

		for (ConfUsuario conf : listaConfUsuario)
		{
			try
			{
				this.entityManagerCRM.persist(conf);

			} catch (EntityExistsException e)
			{
				this.entityManagerCRM.merge(conf);
			}
		}
	
	//	usuario.setConfUsuario(listaConfUsuario);
		this.entityManagerCRM.merge(usuario);

		this.entityManagerCRM.getTransaction().commit();

	}

}
