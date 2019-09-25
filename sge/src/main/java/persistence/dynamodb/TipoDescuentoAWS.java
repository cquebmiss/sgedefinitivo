package persistence.dynamodb;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBAttribute;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBDocument;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBHashKey;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBIgnore;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTable;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@DynamoDBDocument
@DynamoDBTable(tableName = "gestion_TipoDescuento")
@AllArgsConstructor
@Getter
@Setter
public class TipoDescuentoAWS
{

	@DynamoDBHashKey
	private int		idTipoDescuento;
	@DynamoDBAttribute
	private String	descripcion;

	// auxiliar para reportes
	@DynamoDBIgnore
	private int		total;

	public TipoDescuentoAWS()
	{
		super();
		this.total = 1;
		// TODO Auto-generated constructor stub
	}

	@DynamoDBIgnore
	public void incrementar()
	{
		this.total++;
	}

}
