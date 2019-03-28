package modelo.gestion;

import java.math.BigDecimal;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Costo
{
	private int			idCosto;
	private Gestion		gestion;
	private BigDecimal costoOriginal;
	private BigDecimal 	totalAPagar;
	private TipoDescuento tipoDescuento;

	public Costo()
	{
		this.costoOriginal = BigDecimal.valueOf(0);
		this.totalAPagar = BigDecimal.valueOf(0);
	}
}
