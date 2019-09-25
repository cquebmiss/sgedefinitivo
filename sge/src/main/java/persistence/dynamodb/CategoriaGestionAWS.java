package persistence.dynamodb;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBAttribute;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBDocument;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBHashKey;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBIgnore;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBRangeKey;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTable;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@DynamoDBDocument
@DynamoDBTable(tableName = "gestion_CategoriaGestion")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class CategoriaGestionAWS
{

	private String	UUID_CategoriaGestion;

	private int		idCategoriaGestion;
	@DynamoDBAttribute
	private String	descripcion;

	@DynamoDBIgnore
	// auxiliar en la presentación de resultados, para colocar el total de gestiones
	// de categoría
	private int		total;

	public void incrementa()
	{
		this.total++;
	}

	@DynamoDBHashKey(attributeName = "UUID_CategoriaGestion")
	public String getUUID_CategoriaGestion()
	{
		return UUID_CategoriaGestion;
	}

	public void setUUID_CategoriaGestion(String uUID_CategoriaGestion)
	{
		UUID_CategoriaGestion = uUID_CategoriaGestion;
	}

	@DynamoDBRangeKey(attributeName = "idCategoriaGestion")
	public int getIdCategoriaGestion()
	{
		return idCategoriaGestion;
	}

	public void setIdCategoriaGestion(int idCategoriaGestion)
	{
		this.idCategoriaGestion = idCategoriaGestion;
	}

}
