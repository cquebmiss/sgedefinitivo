package persistence.dynamodb;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBAttribute;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBDocument;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBHashKey;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTable;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@DynamoDBTable(tableName = "gestion_StatusActividad")
@DynamoDBDocument
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class StatusActividadAWS
{
	@DynamoDBHashKey
	private int		idStatusActividad;
	
	@DynamoDBAttribute
	private String	descripcion;

}
