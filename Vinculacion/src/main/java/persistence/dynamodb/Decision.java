package persistence.dynamodb;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBAttribute;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBDocument;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBHashKey;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTable;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@DynamoDBTable(tableName = "amigos_Decision")
@DynamoDBDocument
public class Decision {

	private Integer idDecision;
	private String descripcion;

	@DynamoDBHashKey
	public Integer getIdDecision() {
		return idDecision;
	}

	public void setIdDecision(Integer idDecision) {
		this.idDecision = idDecision;
	}

	@DynamoDBAttribute
	public String getDescripcion() {
		return descripcion;
	}

	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}

}
