package persistence.dynamodb;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBAttribute;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBDocument;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBHashKey;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTable;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@DynamoDBDocument
@AllArgsConstructor
@NoArgsConstructor
@DynamoDBTable(tableName = "amigos_StatusUsuario")
public class StatusUsuario {

	private Integer idStatusUsuario;
	private String descripcion;

	@DynamoDBHashKey
	public Integer getIdStatusUsuario() {
		return idStatusUsuario;
	}

	public void setIdStatusUsuario(Integer idStatusUsuario) {
		this.idStatusUsuario = idStatusUsuario;
	}

	@DynamoDBAttribute
	public String getDescripcion() {
		return descripcion;
	}

	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}

}
