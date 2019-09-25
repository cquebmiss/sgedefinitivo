package persistence.dynamodb;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTable;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBAttribute;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBDocument;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBHashKey;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBIgnore;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBRangeKey;

@DynamoDBDocument
@DynamoDBTable(tableName = "gestion_UnidadSalud")
@AllArgsConstructor
@Getter
@Setter
public class UnidadSaludAWS
{

	private String	UUID_UnidadSalud;

	private int		idUnidadSalud;
	@DynamoDBAttribute
	private String	descripcion;

	// auxiliar para reportes
	@DynamoDBIgnore
	private int		total;

	public UnidadSaludAWS()
	{
		super();
		this.total = 1;
		// TODO Auto-generated constructor stub
	}

	public void incrementar()
	{
		this.total++;
	}

	@DynamoDBHashKey(attributeName = "UUID_UnidadSalud")
	public String getUUID_UnidadSalud()
	{
		return UUID_UnidadSalud;
	}

	public void setUUID_UnidadSalud(String uUID_UnidadSalud)
	{
		UUID_UnidadSalud = uUID_UnidadSalud;
	}

	@DynamoDBRangeKey(attributeName = "idUnidadSalud")
	public int getIdUnidadSalud()
	{
		return idUnidadSalud;
	}

	public void setIdUnidadSalud(int idUnidadSalud)
	{
		this.idUnidadSalud = idUnidadSalud;
	}

}
