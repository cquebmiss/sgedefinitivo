package persistence.dynamodb;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBAttribute;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBDocument;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBHashKey;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBIgnore;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTable;

import lombok.NoArgsConstructor;

@NoArgsConstructor
@DynamoDBDocument
public class LocalidadConf {

	private String idEstado;
	private String descripcionEstado;
	private String idMunicipio;
	private String descripcionMunicipio;
	private String idLocalidad;
	private String descripcionLocalidad;

	
	public LocalidadConf(String idEstado, String descripcionEstado, String idMunicipio, String descripcionMunicipio,
			String idLocalidad, String descripcionLocalidad) {
		super();
		this.idEstado = idEstado;
		this.descripcionEstado = descripcionEstado;
		this.idMunicipio = idMunicipio;
		this.descripcionMunicipio = descripcionMunicipio;
		this.idLocalidad = idLocalidad;
		this.descripcionLocalidad = descripcionLocalidad;
	}

	@DynamoDBAttribute
	public String getIdEstado() {
		return idEstado;
	}

	public void setIdEstado(String idEstado) {
		this.idEstado = idEstado;
	}

	@DynamoDBAttribute
	public String getDescripcionEstado() {
		return descripcionEstado;
	}

	public void setDescripcionEstado(String descripcionEstado) {
		this.descripcionEstado = descripcionEstado;
	}

	@DynamoDBAttribute
	public String getIdMunicipio() {
		return idMunicipio;
	}

	public void setIdMunicipio(String idMunicipio) {
		this.idMunicipio = idMunicipio;
	}

	@DynamoDBAttribute
	public String getDescripcionMunicipio() {
		return descripcionMunicipio;
	}

	public void setDescripcionMunicipio(String descripcionMunicipio) {
		this.descripcionMunicipio = descripcionMunicipio;
	}

	@DynamoDBHashKey(attributeName = "idLocalidad")
	public String getIdLocalidad() {
		return idLocalidad;
	}

	public void setIdLocalidad(String idLocalidad) {
		this.idLocalidad = idLocalidad;
	}

	@DynamoDBAttribute
	public String getDescripcionLocalidad() {
		return descripcionLocalidad;
	}

	public void setDescripcionLocalidad(String descripcionLocalidad) {
		this.descripcionLocalidad = descripcionLocalidad;
	}
	
	@DynamoDBIgnore
	public String getClaveCombinada()
	{
		return this.idEstado+this.idMunicipio+this.idLocalidad+" - "+this.descripcionLocalidad;
	}

}
