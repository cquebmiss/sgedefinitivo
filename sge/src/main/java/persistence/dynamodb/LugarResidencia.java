package persistence.dynamodb;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBDocument;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBIgnore;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import modelo.gestion.json.EstadoINEGI;
import modelo.gestion.json.LocalidadINEGI;
import modelo.gestion.json.MunicipioINEGI;

@DynamoDBDocument
@AllArgsConstructor
@Getter
@Setter
public class LugarResidencia
{
	private int				idLugarResidencia;
	private String			descripcion;

	private String			cve_agee;
	private String			nom_agee;
	private String			cve_agem;
	private String			nom_agem;
	private String			cve_loc;
	private String			nom_loc;

	@DynamoDBIgnore
	private EstadoINEGI		estadoINEGI;
	@DynamoDBIgnore
	private MunicipioINEGI	municipioINEGI;
	@DynamoDBIgnore
	private LocalidadINEGI	localidadINEGI;

	// Auxiliar para los reportes
	@DynamoDBIgnore
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
