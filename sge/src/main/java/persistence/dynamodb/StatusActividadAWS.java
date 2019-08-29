package persistence.dynamodb;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBAttribute;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBHashKey;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTable;

@DynamoDBTable(tableName = "StatusActividad")
public class StatusActividadAWS {
	private int idStatusActividad;
	private String descripcion;


	@DynamoDBHashKey(attributeName = "idStatusActividad")
	public int getIdStatusActividad() {
		return idStatusActividad;
	}

	public void setIdStatusActividad(int idStatusActividad) {
		this.idStatusActividad = idStatusActividad;
	}

	@DynamoDBAttribute(attributeName = "Descripcion")
	public String getDescripcion() {
		return descripcion;
	}

	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}

}
