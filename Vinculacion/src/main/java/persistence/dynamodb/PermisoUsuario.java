package persistence.dynamodb;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBAttribute;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBDocument;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBHashKey;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTable;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@DynamoDBDocument
@DynamoDBTable(tableName = "amigos_PermisoUsuario")
public class PermisoUsuario {

	private Integer idPermisoUsuario;
	private String descripcion;

	@DynamoDBHashKey
	public Integer getIdPermisoUsuario() {
		return idPermisoUsuario;
	}

	public void setIdPermisoUsuario(Integer idPermisoUsuario) {
		this.idPermisoUsuario = idPermisoUsuario;
	}

	@DynamoDBAttribute
	public String getDescripcion() {
		return descripcion;
	}

	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}
}
