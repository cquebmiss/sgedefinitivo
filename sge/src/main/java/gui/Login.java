/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gui;

import java.io.Serializable;
import java.net.InetAddress;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;

import org.joda.time.LocalDateTime;

import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClientBuilder;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.amazonaws.services.dynamodbv2.document.DynamoDB;
import com.amazonaws.services.dynamodbv2.document.Table;
import com.amazonaws.services.dynamodbv2.model.AttributeDefinition;
import com.amazonaws.services.dynamodbv2.model.CreateTableRequest;
import com.amazonaws.services.dynamodbv2.model.KeySchemaElement;
import com.amazonaws.services.dynamodbv2.model.KeyType;
import com.amazonaws.services.dynamodbv2.model.ProvisionedThroughput;
import com.amazonaws.services.dynamodbv2.model.TableDescription;

import modelo.Sesion;
import persistence.dynamodb.ControlAcceso;
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
		Sesion controlSesion = (Sesion) FacesUtils.getManagedBean("Sesion");
		return controlSesion.getSesionActiva();
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

		/*
		 * FirebaseOptions options = null;
		 * 
		 * try { options = new FirebaseOptions.Builder()
		 * .setCredentials(GoogleCredentials.fromStream(new
		 * FileInputStream("/home/desarrolloyc/service-account-file.json")))
		 * .setDatabaseUrl("https://hola-crayola.firebaseio.com/") .build(); } catch
		 * (IOException e1) { // TODO Au to-generated catch block e1.printStackTrace();
		 * }
		 * 
		 * FirebaseApp defaultApp = FirebaseApp.initializeApp(options);
		 * 
		 * System.out.println(defaultApp.getName());
		 * 
		 * FirebaseDatabase defaultDatabase = FirebaseDatabase.getInstance(defaultApp);
		 * 
		 * DatabaseReference ref =
		 * defaultDatabase.getReference("server/saving-data/fireblog");
		 * DatabaseReference usersRef = ref.child("users");
		 * 
		 * Map<String, LugarResidencia> users = new HashMap<>();
		 * users.put("alanisawesome", new LugarResidencia(1, "Culo"));
		 * users.put("gracehop", new LugarResidencia(2, "verga"));
		 * 
		 * usersRef.setValueAsync(users);
		 * 
		 * 
		 * System.out.println(defaultDatabase.getReference().getRepo());
		 */

		/*BasicAWSCredentials awsCreds = new BasicAWSCredentials("AKIAVWPSHQFDG3A65247",
				"jzZx6WPlUHIeVH0CxgEh9lael0ZtZQVp9gSVnN32");

		AmazonDynamoDB client = AmazonDynamoDBClientBuilder.standard()
				.withCredentials(new AWSStaticCredentialsProvider(awsCreds)).withRegion(Regions.US_WEST_2).build();

		DynamoDB dynamoDB = new DynamoDB(client);

		String tableName = "ControlAcceso";*/

		try {
			DynamoDB dynamoDB = new DynamoDB(utilidades.getAWSDynamoDBClient());
			String tableName = "ControlAcceso";

			List<AttributeDefinition> attributeDefinitions = new ArrayList<AttributeDefinition>();
			attributeDefinitions.add(new AttributeDefinition().withAttributeName("id").withAttributeType("N"));

			List<KeySchemaElement> keySchema = new ArrayList<KeySchemaElement>();
			keySchema.add(new KeySchemaElement().withAttributeName("id").withKeyType(KeyType.HASH)); // Partition
																										// key

			CreateTableRequest request = new CreateTableRequest().withTableName(tableName).withKeySchema(keySchema)
					.withAttributeDefinitions(attributeDefinitions).withProvisionedThroughput(
							new ProvisionedThroughput().withReadCapacityUnits(5L).withWriteCapacityUnits(5L));

			System.out.println("Issuing CreateTable request for " + tableName);
			Table table = dynamoDB.createTable(request);

			System.out.println("Waiting for " + tableName + " to be created...this may take a while...");
			table.waitForActive();

			System.out.println("Describing " + tableName);

			TableDescription tableDescription = dynamoDB.getTable(tableName).describe();
			System.out.format(
					"Name: %s:\n" + "Status: %s \n" + "Provisioned Throughput (read capacity units/sec): %d \n"
							+ "Provisioned Throughput (write capacity units/sec): %d \n",
					tableDescription.getTableName(), tableDescription.getTableStatus(),
					tableDescription.getProvisionedThroughput().getReadCapacityUnits(),
					tableDescription.getProvisionedThroughput().getWriteCapacityUnits());
			
			
			DynamoDBMapper mapper = new DynamoDBMapper(utilidades.getAWSDynamoDBClient());
			
			InetAddress inetAddress = InetAddress.getLocalHost();

			ControlAcceso item = new ControlAcceso();
			item.setId((int) (Math.random()*25000+1));
			item.setTitle("Accediendo al historial");
			item.setIp(inetAddress.getHostAddress());
			item.setFechahora(LocalDateTime.now().toString());

			mapper.save(item);        
			
			

		} catch (Exception e1) {
		//	System.err.println("CreateTable request failed for " + tableName);
			System.err.println(e1.getMessage());
		}

		
		if(1==1)return;
		
		try (Connection conexion = ((DataBase) FacesUtils.getManagedBean("database")).getConnection();) {
			Sesion controlSesion = (Sesion) FacesUtils.getManagedBean("Sesion");

			setDatosUsuarioSesion(controlSesion, null, null, null, null, null);

			if (getUsuario().length() < 1 || getContrasena().length() < 1) {
				FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,
						"Datos Incorrectos", "Nombre de usuario y/o contraseña incorrecta."));

				// setMensajeError("Nombre de usuario y/o contraseña incorrecta.");
				return;
			}

			String consulta = "SELECT * FROM usuario u WHERE  u.Nombre=? AND u.Contrasena=?";

			PreparedStatement prep = conexion.prepareStatement(consulta);

			prep.setString(1, getUsuario());
			prep.setString(2, utilidades.MD5(getContrasena()));

			ResultSet rBD = prep.executeQuery();

			if (rBD.next()) {
				String fInicio = rBD.getString("VigenciaInicial");
				String fFinal = rBD.getString("VigenciaFinal");

				java.util.Calendar fInicioC = null;

				if (fInicio != null) {
					fInicioC = java.util.Calendar.getInstance();
					fInicioC.clear(Calendar.HOUR);
					fInicioC.clear(Calendar.MINUTE);
					fInicioC.clear(Calendar.SECOND);
					fInicioC.clear(Calendar.MILLISECOND);
					fInicioC.set(Integer.parseInt(fInicio.substring(0, 4)), Integer.parseInt(fInicio.substring(5, 7)),
							Integer.parseInt(fInicio.substring(8, 10)));
					fInicioC.add(Calendar.MONTH, -1);
				}

				java.util.Calendar fFinalC = null;

				if (fFinal != null) {
					fFinalC = java.util.Calendar.getInstance();
					fFinalC.clear(Calendar.HOUR);
					fFinalC.clear(Calendar.MINUTE);
					fFinalC.clear(Calendar.SECOND);
					fFinalC.clear(Calendar.MILLISECOND);
					fFinalC.set(Integer.parseInt(fFinal.substring(0, 4)), Integer.parseInt(fFinal.substring(5, 7)),
							Integer.parseInt(fFinal.substring(8, 10)));
					fFinalC.add(Calendar.MONTH, -1);
				}

				if (fInicio != null | fFinal != null) {

					java.util.Calendar sqlDateActual = java.util.Calendar.getInstance();
					sqlDateActual.clear(Calendar.HOUR);
					sqlDateActual.clear(Calendar.MINUTE);
					sqlDateActual.clear(Calendar.SECOND);
					sqlDateActual.clear(Calendar.MILLISECOND);

					if (fFinalC != null & fInicioC == null) {
						if (sqlDateActual.after(fFinalC)) {
							setMensajeError("Vigencia de la cuenta expirada.");
							return;
						}
					}

					if (fInicioC != null & fFinalC == null) {
						if (sqlDateActual.before(fInicioC)) {
							setMensajeError("Cuenta fuera de vigencia.");
							return;
						}
					}

					if (fInicioC != null & fFinalC != null) {
						if (!(sqlDateActual.compareTo(fInicioC) >= 0 & sqlDateActual.compareTo(fFinalC) <= 0)) {
							setMensajeError("Cuenta fuera de vigencia.");
							return;
						}
					}

				}

				String tipoSesion = "";

				// En este sistema va a cambiar el esquema de permisos, por lo
				// que
				// no es necesario enviar a determinada página de acuerdo al
				// nivel
				// de permiso, sino se acumularán los permisos a los que tenga
				// derecho el usuario
				switch (rBD.getString("NivelPermiso")) {
				case "0":
					tipoSesion = "Admin";
					break;
				case "1":
					tipoSesion = "SuperAdmin";
					break;
				case "2":
					tipoSesion = "Usuario";
					break;

				}

				FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,
						"Datos Correctos", "Bienvenido, redireccionando..."));
				// setMensajeError("Bienvenido, redireccionando...");
				setDatosUsuarioSesion(controlSesion, tipoSesion, rBD.getString("idUsuario"), rBD.getString("Nombre"),
						rBD.getString("NombreReal"), rBD.getString("CuentaCorreo"));
			}

			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,
					"Datos Incorrectos", "Nombre de usuario y/o contraseña incorrecta."));
			// setMensajeError("Nombre de usuario y/o contraseña incorrectos.");

		} catch (Exception ex) {
			ex.printStackTrace();
		}

	}

}
