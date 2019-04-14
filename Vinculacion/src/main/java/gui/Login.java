/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gui;

import java.io.Serializable;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.persistence.EntityManager;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import modelo.persistence.Usuario;
import resources.DataBase;
import util.FacesUtils;

/**
 *
 * @author desarolloyc
 */

@ManagedBean
@SessionScoped
@Getter
@Setter
@NoArgsConstructor
public class Login implements Serializable
{
	private String usuario;
	private String contrasena;
	private String mensajeError;
	private EntityManager entityManagerCRM;
	private Usuario usuarioEnSesion;


	@PostConstruct
	public void postConstruct()
	{
		DataBase dataBaseBean = (DataBase) FacesUtils.getManagedBean("database");
		this.entityManagerCRM = dataBaseBean.getEntityManagerCRM();
	}


	public String isInSession()
	{
		if( this.usuarioEnSesion != null )
		{
			return usuarioEnSesion.getPermisoUsuario().getDescripcion();
		}
		else
		{
			return "null";
		}
	}


	public void actionBotonAcceder(ActionEvent e)
	{

		try
		{

			EntityManager em = ((DataBase) FacesUtils.getManagedBean("database")).getEntityManagerCRM();

			List<Usuario> usuario = em
					.createQuery("SELECT u FROM usuario u WHERE u.nombre = :nombre AND u.contraseña = :contraseña",
							Usuario.class)
					.setParameter("nombre", getUsuario()).setParameter("contraseña", getContrasena()).setMaxResults(1)
					.getResultList();

			if (usuario.isEmpty())
			{
				FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,
						"Datos Incorrectos", "Nombre de usuario y/o contraseña incorrecta."));

				// setMensajeError("Nombre de usuario y/o contraseña incorrecta.");
				return;

			} else
			{
				this.usuarioEnSesion = usuario.get(0);
				
				FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,
						"Datos Correctos", "Bienvenido, redireccionando..."));
			}


			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,
					"Datos Incorrectos", "Nombre de usuario y/o contraseña incorrecta."));
			// setMensajeError("Nombre de usuario y/o contraseña incorrectos.");

		} catch (Exception ex)
		{
			ex.printStackTrace();
		}

	}

	public void actionCerrarSesion()
	{
		try
		{
			this.usuarioEnSesion = null;

			FacesContext.getCurrentInstance().getExternalContext().invalidateSession();

			ExternalContext ec = FacesContext.getCurrentInstance().getExternalContext();
			ec.redirect(ec.getRequestContextPath() + "/");

		}
		catch (Exception ex)
		{
			ex.printStackTrace();
		}
	}
	
}
