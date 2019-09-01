package persistence.dynamodb;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBAttribute;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBHashKey;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTable;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@DynamoDBTable(tableName = "amigos_Estado")
public class Estado {

	private String cve_agee;
	private String nom_agee;
	private String nom_abrev;
	private String pob;
	private String pob_fem;
	private String pob_mas;
	private String viv;

	@DynamoDBHashKey
	public String getCve_agee() {
		return cve_agee;
	}

	public void setCve_agee(String cve_agee) {
		this.cve_agee = cve_agee;
	}

	@DynamoDBAttribute
	public String getNom_agee() {
		return nom_agee;
	}

	public void setNom_agee(String nom_agee) {
		this.nom_agee = nom_agee;
	}

	@DynamoDBAttribute
	public String getNom_abrev() {
		return nom_abrev;
	}

	public void setNom_abrev(String nom_abrev) {
		this.nom_abrev = nom_abrev;
	}

	@DynamoDBAttribute
	public String getPob() {
		return pob;
	}

	public void setPob(String pob) {
		this.pob = pob;
	}

	@DynamoDBAttribute
	public String getPob_fem() {
		return pob_fem;
	}

	public void setPob_fem(String pob_fem) {
		this.pob_fem = pob_fem;
	}

	@DynamoDBAttribute
	public String getPob_mas() {
		return pob_mas;
	}

	public void setPob_mas(String pob_mas) {
		this.pob_mas = pob_mas;
	}

	@DynamoDBAttribute
	public String getViv() {
		return viv;
	}

	public void setViv(String viv) {
		this.viv = viv;
	}

}
