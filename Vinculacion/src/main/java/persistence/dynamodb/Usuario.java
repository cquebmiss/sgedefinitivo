package persistence.dynamodb;

import java.util.ArrayList;
import java.util.List;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBAttribute;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBAutoGeneratedKey;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBHashKey;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBIgnore;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTable;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@DynamoDBTable(tableName = "amigos_Usuario")
public class Usuario {

	private String idUsuario;
	private String nombre;
	private String contrasena;

	private PermisoUsuario permisoUsuario;
	private StatusUsuario statusUsuario;

	private List<LocalidadConf> localidades;

	@DynamoDBHashKey(attributeName = "idUsuario")
	@DynamoDBAutoGeneratedKey
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

	@DynamoDBAttribute(attributeName = "localidades")
	public List<LocalidadConf> getLocalidades() {
		return localidades;
	}

	@DynamoDBIgnore
	public List<String> getLocalidadesString() {
		
		List<String> catLocalidadesString = new ArrayList<>();

		this.localidades.forEach(loc -> catLocalidadesString.add(loc.getClaveCombinada()));

		return catLocalidadesString;
	}

	public void setLocalidades(List<LocalidadConf> localidades) {
		this.localidades = localidades;
	}

	@DynamoDBAttribute
	public PermisoUsuario getPermisoUsuario() {
		return permisoUsuario;
	}

	public void setPermisoUsuario(PermisoUsuario permisoUsuario) {
		this.permisoUsuario = permisoUsuario;
	}

	@DynamoDBAttribute
	public StatusUsuario getStatusUsuario() {
		return statusUsuario;
	}

	public void setStatusUsuario(StatusUsuario statusUsuario) {
		this.statusUsuario = statusUsuario;
	}

}
