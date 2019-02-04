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
	private BigDecimal	costoCirugias;
	private BigDecimal	costoMateriales;
	private BigDecimal	costoEstudios;
	private BigDecimal	costoMedicamentos;
	private BigDecimal	descuentoCirugias;
	private BigDecimal	descuentoMateriales;
	private BigDecimal	descuentoEstudios;
	private BigDecimal	descuentoMedicamentos;

	public Costo()
	{
		this.costoCirugias = BigDecimal.valueOf(0);
		this.costoMateriales = BigDecimal.valueOf(0);
		this.costoEstudios = BigDecimal.valueOf(0);
		this.costoMedicamentos = BigDecimal.valueOf(0);
		this.descuentoCirugias = BigDecimal.valueOf(0);
		this.descuentoMateriales = BigDecimal.valueOf(0);
		this.descuentoEstudios = BigDecimal.valueOf(0);
		this.descuentoMedicamentos = BigDecimal.valueOf(0);

	}
}
