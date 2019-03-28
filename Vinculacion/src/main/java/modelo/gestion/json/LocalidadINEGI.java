package modelo.gestion.json;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class LocalidadINEGI
{
	private String	cve_agee;
	private String	cve_agem;
	private String	cve_loc;
	private String	nom_loc;
	private String	ambito;
	private String	latitud;
	private String	longitud;
	private String	altitud;
	private String	pob;
	private String	viv;
	private String	cve_carta;
	private String	estatus;
	private String	periodo;

	private String	result;
	private String	mensaje;

	public LocalidadINEGI(String cve_loc, String nom_loc)
	{
		super();
		this.cve_loc = cve_loc;
		this.nom_loc = nom_loc;
	}

}
