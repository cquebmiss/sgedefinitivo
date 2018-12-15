/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gui;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Calendar;
import java.util.List;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

import modelo.Sesion;
import persistence.model.GestionJPA;
import persistence.model.SeguridadSocial;
import resources.DataBase;
import util.FacesUtils;
import util.utilidades;

/**
 *
 * @author desarolloyc
 */

@ManagedBean
@SessionScoped

public class Login implements Serializable {
	private String usuario;
	private String contrasena;
	private String mensajeError;

	public Login() {

	}

	/**
	 * @return the usuario
	 */
	public String getUsuario() {
		return usuario;
	}

	/**
	 * @param usuario the usuario to set
	 */
	public void setUsuario(String usuario) {
		this.usuario = usuario;
	}

	/**
	 * @return the contrasena
	 */
	public String getContrasena() {
		return contrasena;
	}

	/**
	 * @param contrasena the contrasena to set
	 */
	public void setContrasena(String contrasena) {
		this.contrasena = contrasena;
	}

	/**
	 * @return the mensajeError
	 */
	public String getMensajeError() {
		return mensajeError;
	}

	/**
	 * @param mensajeError the mensajeError to set
	 */
	public void setMensajeError(String mensajeError) {
		this.mensajeError = mensajeError;
	}

	public String isInSession() {
		return "portal/dashboard.jsf?faces-redirect=true";
	}

	private void setDatosUsuarioSesion(Sesion controlSesion, String tipoSesion, String idUsuario, String nombreUsuario,
			String nombreReal, String email) {
		controlSesion.setSesionActiva(tipoSesion);
		controlSesion.setIdUsuario(idUsuario);
		controlSesion.setNombreUsuario(nombreUsuario);
		controlSesion.setNombreReal(nombreReal);
		controlSesion.setEmail(email);

		if (idUsuario != null) {
			controlSesion.getPermisosUsuarioSistemas();
		}

	}

	public void actionBotonAcceder(ActionEvent e) {

		if (getUsuario().length() < 1 || getContrasena().length() < 1) {
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,
					"Datos Incorrectos", "Nombre de usuario y/o contraseña incorrecta."));

			return;
		}


		FacesContext.getCurrentInstance().addMessage(null,
				new FacesMessage(FacesMessage.SEVERITY_ERROR, "Datos Correctos", "Bienvenido, redireccionando..."));
		// setMensajeError("Bienvenido, redireccionando...");

	}

}
