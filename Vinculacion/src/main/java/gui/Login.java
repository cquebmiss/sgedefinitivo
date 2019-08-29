/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gui;

import java.io.Serializable;
import java.net.InetAddress;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.persistence.EntityManager;

import org.joda.time.LocalDateTime;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;

import dao.LoginDAO;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import modelo.persistence.Usuario;
import modelo.persistence.dynamodb.*;
import persistence.dynamodb.Localidad;
import resources.DataBase;
import util.FacesUtils;
import util.utilidades;

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

	private persistence.dynamodb.Usuario usuarioAWS;

	@PostConstruct
	public void postConstruct()
	{
		DataBase dataBaseBean = (DataBase) FacesUtils.getManagedBean("database");
		this.entityManagerCRM = dataBaseBean.getEntityManagerCRM();
	}


	public String isInSession()
	{
		if( this.usuarioAWS != null )
		{
			return usuarioAWS.getDescripcionPermisoUsuario();
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
			
			
			
			try {
/*
 				
 				DynamoDBMapper mapper = new DynamoDBMapper(utilidades.getAWSDynamoDBClient());
				persistence.dynamodb.Usuario item = new persistence.dynamodb.Usuario();
				item.setIdUsuario("1");
				item.setNombre("cquebmiss");
				item.setContraseña("salud");
				item.setIdStatusUsuario(1);
				item.setDescripcionStatusUsuario("Activo");
				item.setIdPermisoUsuario(2);
				item.setDescripcionPermisoUsuario("Administrador");
				
				List<Localidad> localidades = new ArrayList<Localidad>();
				localidades.add( new Localidad("04","Campeche","01","Campeche","40010002","Isla Arena (Punta Arena)") );
				localidades.add( new Localidad("04","Campeche","003","Carmen","40031776","Oxcabal") );
				localidades.add( new Localidad("04","Campeche","003","Carmen","40033395","Gustavo Díaz Ordaz 18 de marzo") );
				
				item.setLocalidades(localidades);*/
				
				List<persistence.dynamodb.Usuario> usuario = LoginDAO.getUsuario(getUsuario(), getContrasena());
				
				if( usuario == null )
				{
					System.out.println("Usuario no encontrado");
				}
				else
				{
					System.out.print("Usuario encontrado: "+usuario.get(0).getIdUsuario());
				}
				
				
				

			} catch (Exception e1) {
				System.out.println("Excepción en algo de Amazon");
				e1.printStackTrace();
			}
			
			
			List<persistence.dynamodb.Usuario> usuario = LoginDAO.getUsuario(getUsuario(), getContrasena());
			

			if (usuario == null || usuario.isEmpty())
			{
				FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,
						"Datos Incorrectos", "Nombre de usuario y/o contraseña incorrecta."));

				// setMensajeError("Nombre de usuario y/o contraseña incorrecta.");
				return;

			} else
			{
				this.usuarioAWS = usuario.get(0);
				
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
			this.usuarioAWS = null;

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
