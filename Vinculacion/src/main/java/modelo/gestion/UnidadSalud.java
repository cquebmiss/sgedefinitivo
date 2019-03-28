package modelo.gestion;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@Getter
@Setter
public class UnidadSalud
{
	private int		idUnidadSalud;
	private String	descripcion;

	// auxiliar para reportes
	private int		total;

	public UnidadSalud()
	{
		super();
		this.total = 1;
		// TODO Auto-generated constructor stub
	}

	public UnidadSalud(int idUnidadSalud, String descripcion)
	{
		super();
		this.idUnidadSalud = idUnidadSalud;
		this.descripcion = descripcion;
	}

	public void incrementar()
	{
		this.total++;
	}

}
