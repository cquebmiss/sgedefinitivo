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

import dao.LoginDAO;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
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

	private persistence.dynamodb.Usuario usuarioAWS;

	@PostConstruct
	public void postConstruct()
	{
		DataBase dataBaseBean = (DataBase) FacesUtils.getManagedBean("database");
	}


	public String isInSession()
	{
		if( this.usuarioAWS != null )
		{
			return usuarioAWS.getPermisoUsuario().getDescripcion();
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
				item.setContrasena("salud");
				item.setStatusUsuario(new StatusUsuario(1, "Activo"));
				item.setPermisoUsuario(new PermisoUsuario(2, "Administrador"));
				
				List<LocalidadConf> localidades = new ArrayList<>();
				localidades.add( new LocalidadConf("04","Campeche","01","Campeche","40010002","Isla Arena (Punta Arena)") );
				localidades.add( new LocalidadConf("04","Campeche","003","Carmen","40031776","Oxcabal") );
				localidades.add( new LocalidadConf("04","Campeche","003","Carmen","40033395","Gustavo Díaz Ordaz 18 de marzo") );
				
				item.setLocalidades(localidades);
				mapper.save(item);*/
				
		/*		
				DynamoDBMapper mapper = new DynamoDBMapper(utilidades.getAWSDynamoDBClient());
				
				//Poblar las tablas de localidades, estados y municipios
				
				INEGIService inegiService = new INEGIService();
				RespuestaEstadosJson respuesta = inegiService.getAllEstados();
				

				List<EstadoINEGI> estadosINEGI = respuesta.getDatos();
				List<MunicipioINEGI> municipiosINEGI = null;
				List<LocalidadINEGI> localidadesINEGI = null;
				List<Estado> estadosAWS = new ArrayList<>();
				List<Municipio> municipiosAWS = new ArrayList<>();
				List<Localidad> localidadesAWS = new ArrayList<>();

				if (estadosINEGI == null)
				{
					estadosINEGI = new ArrayList<>();
				}

				estadosINEGI.forEach(estado -> estadosAWS
						.add(new Estado(estado.getCve_agee(), estado.getNom_agee(), estado.getNom_abrev(),
								estado.getPob(), estado.getPob_fem(), estado.getPob_mas(), estado.getViv())));
				
				mapper.batchSave(estadosAWS);

				RespuestaMunicipiosJson respuestaMun = null;
				RespuestaLocalidadesJson respuestaLoca = null;
				
				//Recorrer todos los estados e ir insertando
				for(EstadoINEGI estado : estadosINEGI)
				{
					if( ! estado.getCve_agee().equalsIgnoreCase("04"))
					{
						continue;
					}
					
					municipiosAWS = new ArrayList<>();
					respuestaMun = inegiService.getMunicipios(estado.getCve_agee());
					municipiosINEGI = respuestaMun.getDatos();
					
					if(municipiosINEGI == null )
					{
						municipiosINEGI = new ArrayList<>();
					}
					
					
					for( MunicipioINEGI municipio : municipiosINEGI)
					{
						municipiosAWS.add(new Municipio(municipio.getCve_agee(),
								municipio.getCve_agem(), municipio.getNom_agem(), municipio.getCve_cab(), municipio.getNom_cab(),
								municipio.getPob(), municipio.getPob_fem(), municipio.getPob_mas(), municipio.getViv()));
					}
					
					System.out.println("Salvando municipios del estado: "+estado.getNom_agee());
					mapper.batchSave(municipiosAWS);
					System.out.println("Salvados los municipios del estado: "+estado.getNom_agee());
					
					for(MunicipioINEGI municipio : municipiosINEGI)
					{
						localidadesAWS = new ArrayList<>();
						respuestaLoca = inegiService.getLocalidades(estado.getCve_agee(), municipio.getCve_agem());
						localidadesINEGI = respuestaLoca.getDatos();
						
						if( localidadesINEGI == null )
						{
							localidadesINEGI = new ArrayList<>();
						}
						
						for(LocalidadINEGI loca : localidadesINEGI)
						{
							localidadesAWS.add(new Localidad(loca.getCve_agee(),
									loca.getCve_agem(), loca.getCve_loc(), loca.getNom_loc(), loca.getAmbito(),
									loca.getLatitud(), loca.getLongitud(), loca.getAltitud(), loca.getPob(), loca.getViv(),
									loca.getCve_carta(), loca.getEstatus(), loca.getPeriodo()));
						}
						
						
						System.out.println("Salvando localidades del municipio: "+estado.getNom_agee()+"-"+municipio.getNom_agem());
						mapper.batchSave(localidadesAWS);
						System.out.println("Salvados las localidades del del municipio: "+estado.getNom_agee()+" - "+municipio.getNom_agem());
						
					}
					
					
					
				}
				*/
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
