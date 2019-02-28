package modelo.gestion;

import lombok.Getter;
import lombok.Setter;
import modelo.gestion.json.EstadoINEGI;
import modelo.gestion.json.LocalidadINEGI;
import modelo.gestion.json.MunicipioINEGI;

@Getter
@Setter
public class LugarResidencia
{
	private int				idLugarResidencia;
	private String			descripcion;

	private EstadoINEGI		estadoINEGI;
	private MunicipioINEGI	municipioINEGI;
	private LocalidadINEGI	localidadINEGI;

	// Auxiliar para los reportes
	private int				total;

	public LugarResidencia()
	{
		super();
		this.total = 1;
		// TODO Auto-generated constructor stub
	}

	public LugarResidencia(int idLugarResidencia, String descripcion)
	{
		super();
		this.idLugarResidencia = idLugarResidencia;
		this.descripcion = descripcion;
	}

	public void incrementar()
	{
		this.total++;
	}

}
