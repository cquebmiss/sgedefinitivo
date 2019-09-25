package persistence.dynamodb;

import java.math.BigDecimal;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBDocument;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import modelo.gestion.Gestion;

@DynamoDBDocument
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class Costo
{
	private int				idCosto;
	private BigDecimal		costoOriginal;
	private BigDecimal		totalAPagar;

	private TipoDescuentoAWS	tipoDescuento;
}
