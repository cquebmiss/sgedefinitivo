package persistence.dynamodb;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBAttribute;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBDocument;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBHashKey;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBRangeKey;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTable;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@DynamoDBTable(tableName = "gestion_SeguridadSocial")
@DynamoDBDocument
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class SeguridadSocialAWS
{

	private String	UUID_SeguridadSocial;

	private int		idSeguridadSocial;

	@DynamoDBAttribute
	private String	descripcion;

	@DynamoDBHashKey(attributeName = "UUID_SeguridadSocial")
	public String getUUID_SeguridadSocial()
	{
		return UUID_SeguridadSocial;
	}

	public void setUUID_SeguridadSocial(String uUID_SeguridadSocial)
	{
		UUID_SeguridadSocial = uUID_SeguridadSocial;
	}

	@DynamoDBRangeKey(attributeName = "idSeguridadSocial")
	public int getIdSeguridadSocial()
	{
		return idSeguridadSocial;
	}

	public void setIdSeguridadSocial(int idSeguridadSocial)
	{
		this.idSeguridadSocial = idSeguridadSocial;
	}

}
