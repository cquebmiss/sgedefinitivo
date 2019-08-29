package persistence.dynamodb;

import java.util.List;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBAttribute;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBHashKey;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTable;

import lombok.NoArgsConstructor;

@NoArgsConstructor
@DynamoDBTable(tableName = "amigos_Usuario")
public class Usuario {

	private String idUsuario;
	private String nombre;
	private String contrasena;
	private Integer idStatusUsuario;
	private String descripcionStatusUsuario;
	private Integer idPermisoUsuario;
	private String descripcionPermisoUsuario;

	private List<Localidad> localidades;

	@DynamoDBHashKey(attributeName = "idUsuario")
	public String getIdUsuario() {
		return idUsuario;
	}

	public void setIdUsuario(String idUsuario) {
		this.idUsuario = idUsuario;
	}

	@DynamoDBAttribute
	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	@DynamoDBAttribute
	public String getContrasena() {
		return contrasena;
	}

	public void setContrasena(String contrasena) {
		this.contrasena = contrasena;
	}

	@DynamoDBAttribute
	public Integer getIdStatusUsuario() {
		return idStatusUsuario;
	}

	public void setIdStatusUsuario(Integer idStatusUsuario) {
		this.idStatusUsuario = idStatusUsuario;
	}

	@DynamoDBAttribute
	public String getDescripcionStatusUsuario() {
		return descripcionStatusUsuario;
	}

	public void setDescripcionStatusUsuario(String descripcionStatusUsuario) {
		this.descripcionStatusUsuario = descripcionStatusUsuario;
	}

	@DynamoDBAttribute
	public Integer getIdPermisoUsuario() {
		return idPermisoUsuario;
	}

	public void setIdPermisoUsuario(Integer idPermisoUsuario) {
		this.idPermisoUsuario = idPermisoUsuario;
	}

	@DynamoDBAttribute
	public String getDescripcionPermisoUsuario() {
		return descripcionPermisoUsuario;
	}

	public void setDescripcionPermisoUsuario(String descripcionPermisoUsuario) {
		this.descripcionPermisoUsuario = descripcionPermisoUsuario;
	}

	@DynamoDBAttribute(attributeName = "localidades")
	public List<Localidad> getLocalidades() {
		return localidades;
	}

	public void setLocalidades(List<Localidad> localidades) {
		this.localidades = localidades;
	}

}
