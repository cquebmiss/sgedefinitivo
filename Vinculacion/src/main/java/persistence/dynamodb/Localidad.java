package persistence.dynamodb;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBAttribute;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBHashKey;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBIgnore;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTable;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@DynamoDBTable(tableName = "amigos_Localidad")
public class Localidad {

	private String cve_agee;
	private String cve_agem;
	private String cve_loc;
	private String nom_loc;
	private String ambito;
	private String latitud;
	private String longitud;
	private String altitud;
	private String pob;
	private String viv;
	private String cve_carta;
	private String estatus;
	private String periodo;

	@DynamoDBAttribute
	public String getCve_agee() {
		return cve_agee;
	}

	public void setCve_agee(String cve_agee) {
		this.cve_agee = cve_agee;
	}

	@DynamoDBAttribute
	public String getCve_agem() {
		return cve_agem;
	}

	public void setCve_agem(String cve_agem) {
		this.cve_agem = cve_agem;
	}

	@DynamoDBHashKey
	public String getCve_loc() {
		return cve_loc;
	}

	public void setCve_loc(String cve_loc) {
		this.cve_loc = cve_loc;
	}

	@DynamoDBAttribute
	public String getNom_loc() {
		return nom_loc;
	}

	public void setNom_loc(String nom_loc) {
		this.nom_loc = nom_loc;
	}

	@DynamoDBAttribute
	public String getAmbito() {
		return ambito;
	}

	public void setAmbito(String ambito) {
		this.ambito = ambito;
	}

	@DynamoDBAttribute
	public String getLatitud() {
		return latitud;
	}

	public void setLatitud(String latitud) {
		this.latitud = latitud;
	}

	@DynamoDBAttribute
	public String getLongitud() {
		return longitud;
	}

	public void setLongitud(String longitud) {
		this.longitud = longitud;
	}

	@DynamoDBAttribute
	public String getAltitud() {
		return altitud;
	}

	public void setAltitud(String altitud) {
		this.altitud = altitud;
	}

	@DynamoDBAttribute
	public String getPob() {
		return pob;
	}

	public void setPob(String pob) {
		this.pob = pob;
	}

	@DynamoDBAttribute
	public String getViv() {
		return viv;
	}

	public void setViv(String viv) {
		this.viv = viv;
	}

	@DynamoDBAttribute
	public String getCve_carta() {
		return cve_carta;
	}

	public void setCve_carta(String cve_carta) {
		this.cve_carta = cve_carta;
	}

	@DynamoDBAttribute
	public String getEstatus() {
		return estatus;
	}

	public void setEstatus(String estatus) {
		this.estatus = estatus;
	}

	@DynamoDBAttribute
	public String getPeriodo() {
		return periodo;
	}

	public void setPeriodo(String periodo) {
		this.periodo = periodo;
	}
	
	@DynamoDBIgnore
	public String getClaveCombinada()
	{
		return this.cve_agee+this.cve_agem+this.cve_loc+" - "+this.nom_loc;
	}

}
