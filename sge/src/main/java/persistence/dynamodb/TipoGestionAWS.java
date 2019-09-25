package persistence.dynamodb;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBAttribute;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBDocument;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBHashKey;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTable;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@DynamoDBDocument
@DynamoDBTable(tableName = "gestion_TipoGestion")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class TipoGestionAWS
{
	@DynamoDBHashKey
	private int		idTipoGestion;
	@DynamoDBAttribute
	private String	descripcion;

}
