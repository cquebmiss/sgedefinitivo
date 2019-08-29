package persistence.dynamodb;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBAttribute;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBHashKey;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTable;

@DynamoDBTable(tableName = "CategoriaGestion")
public class CategoriaGestionAWS
{
	private int		idCategoriaGestion;
	private String	descripcion;

	//auxiliar en la presentación de resultados, para colocar el total de gestiones de categoría
	private int		total;


	public void incrementa()
	{
		this.total++;
	}
	@DynamoDBHashKey(attributeName = "idCategoriaGestion")
	public int getIdCategoriaGestion()
	{
		return idCategoriaGestion;
	}

	public void setIdCategoriaGestion(int idCategoriaGestion)
	{
		this.idCategoriaGestion = idCategoriaGestion;
	}

	@DynamoDBAttribute(attributeName = "Descripcion")
	public String getDescripcion()
	{
		return descripcion;
	}

	public void setDescripcion(String descripcion)
	{
		this.descripcion = descripcion;
	}

	public int getTotal()
	{
		return total;
	}

	public void setTotal(int total)
	{
		this.total = total;
	}

}
