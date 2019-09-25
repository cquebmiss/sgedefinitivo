package persistence.dynamodb;

import java.util.Date;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBDocument;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import util.gestion.UtilidadesGestion;

@DynamoDBDocument
@AllArgsConstructor
@Getter
@Setter
public class PacienteAWS
{
	private int					idPaciente;
	private String				nombre;
	private String				sexo;
	private int					edad;
	private Date				fechaNacimiento;
	private LugarResidencia		lugarResidencia;
	private String				diagnostico;
	private String				hospitalizadoEn;
	private SeguridadSocialAWS	seguridadSocial;
	private String				afiliacion;
	private String				CURP;
	private UnidadSaludAWS		atendidoEn;
	private UnidadSaludAWS		referenciadoA;
	
	public PacienteAWS()
	{
		super();
		this.nombre = "";
		this.sexo = "";
		this.edad = 0;
		this.fechaNacimiento = new java.util.Date();
		this.seguridadSocial = new SeguridadSocialAWS(UtilidadesGestion.UUID_UnidadSalud,1, "Seguro Popular");
		this.lugarResidencia = new LugarResidencia(-1, "");
		this.diagnostico = "";
		this.hospitalizadoEn = "";
		this.afiliacion = "";
		this.CURP = "";
		// TODO Auto-generated constructor stub
	}

}
