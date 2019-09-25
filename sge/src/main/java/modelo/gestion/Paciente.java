package modelo.gestion;

import java.util.Date;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import persistence.dynamodb.SeguridadSocialAWS;

@NoArgsConstructor
@Getter
@Setter
public class Paciente
{
	private int				idPaciente;
	private Gestion			gestion;
	private String			nombre;
	private String			sexo;
	private int				edad;
	private Date			fechaNacimiento;
	private LugarResidencia	lugarResidencia;
	private String			diagnostico;
	private String			hospitalizadoEn;
	private SeguridadSocial	seguridadSocial;
	private String			afiliacion;
	private String			CURP;
	private UnidadSalud		atendidoEn;
	private UnidadSalud		referenciadoA;

	public Paciente(Gestion gestion)
	{
		super();
		this.gestion = gestion;
		this.nombre = "";
		this.sexo = "";
		this.edad = 0;
		this.fechaNacimiento = new java.util.Date();
		this.seguridadSocial = new SeguridadSocial(1, "Seguro Popular");
		this.lugarResidencia = new LugarResidencia(-1, "");
		this.diagnostico = "";
		this.hospitalizadoEn = "";
		this.afiliacion = "";
		this.CURP = "";
		// TODO Auto-generated constructor stub
	}

}
