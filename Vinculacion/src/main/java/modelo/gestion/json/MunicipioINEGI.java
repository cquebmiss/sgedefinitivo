package modelo.gestion.json;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class MunicipioINEGI
{
	private String	cve_agee;
	private String	cve_agem;
	private String	nom_agem;
	private String	cve_cab;
	private String	pob;
	private String	pob_fem;
	private String	pob_mas;
	private String	viv;
	
	private String result;
	private String mensaje;
	
	public MunicipioINEGI(String cve_agem, String nom_agem)
	{
		super();
		this.cve_agem = cve_agem;
		this.nom_agem = nom_agem;
	}
	
	
}
