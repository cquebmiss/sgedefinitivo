package modelo.gestion;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@Getter
@Setter
public class TipoDescuento
{
	private int		idTipoDescuento;
	private String	descripcion;

	// auxiliar para reportes
	private int		total;

	public TipoDescuento()
	{
		super();
		this.total = 1;
		// TODO Auto-generated constructor stub
	}

	public TipoDescuento(int idTipoDescuento, String descripcion)
	{
		super();
		this.idTipoDescuento = idTipoDescuento;
		this.descripcion = descripcion;
	}

	public void incrementar()
	{
		this.total++;
	}
}
