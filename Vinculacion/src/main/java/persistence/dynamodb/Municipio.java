package persistence.dynamodb;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBAttribute;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBHashKey;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTable;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@DynamoDBTable(tableName = "amigos_Municipio")
public class Municipio {

	private String cve_agee;
	private String cve_agem;
	private String nom_agem;
	private String cve_cab;
	private String nom_cab;
	private String pob;
	private String pob_fem;
	private String pob_mas;
	private String viv;

	@DynamoDBAttribute
	public String getCve_agee() {
		return cve_agee;
	}

	public void setCve_agee(String cve_agee) {
		this.cve_agee = cve_agee;
	}

	@DynamoDBHashKey
	public String getCve_agem() {
		return cve_agem;
	}

	public void setCve_agem(String cve_agem) {
		this.cve_agem = cve_agem;
	}

	@DynamoDBAttribute
	public String getNom_agem() {
		return nom_agem;
	}

	public void setNom_agem(String nom_agem) {
		this.nom_agem = nom_agem;
	}

	@DynamoDBAttribute
	public String getCve_cab() {
		return cve_cab;
	}

	public void setCve_cab(String cve_cab) {
		this.cve_cab = cve_cab;
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

	@DynamoDBAttribute
	public String getNom_cab() {
		return nom_cab;
	}

	public void setNom_cab(String nom_cab) {
		this.nom_cab = nom_cab;
	}

}
