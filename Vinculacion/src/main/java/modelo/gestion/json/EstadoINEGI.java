package modelo.gestion.json;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class EstadoINEGI
{
	private String	cve_agee;
	private String	nom_agee;
	private String	nom_abrev;
	private String	pob;
	private String	pob_fem;
	private String	pob_mas;
	private String	viv;
	
	public EstadoINEGI(String cve_agee, String nom_agee)
	{
		this.cve_agee = cve_agee;
		this.nom_agee = nom_agee;
	}
	

}
