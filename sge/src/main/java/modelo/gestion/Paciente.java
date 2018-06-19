package modelo.gestion;

import java.util.Date;

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
		// TODO Auto-generated constructor stub
	}

	public int getIdPaciente()
	{
		return idPaciente;
	}

	public void setIdPaciente(int idPaciente)
	{
		this.idPaciente = idPaciente;
	}

	public Gestion getGestion()
	{
		return gestion;
	}

	public void setGestion(Gestion gestion)
	{
		this.gestion = gestion;
	}

	public String getNombre()
	{
		return nombre;
	}

	public void setNombre(String nombre)
	{
		this.nombre = nombre;
	}

	public String getSexo()
	{
		return sexo;
	}

	public void setSexo(String sexo)
	{
		this.sexo = sexo;
	}

	public int getEdad()
	{
		return edad;
	}

	public void setEdad(int edad)
	{
		this.edad = edad;
	}

	public Date getFechaNacimiento()
	{
		return fechaNacimiento;
	}

	public void setFechaNacimiento(Date fechaNacimiento)
	{
		this.fechaNacimiento = fechaNacimiento;
	}

	public String getDiagnostico()
	{
		return diagnostico;
	}

	public void setDiagnostico(String diagnostico)
	{
		this.diagnostico = diagnostico;
	}

	public String getHospitalizadoEn()
	{
		return hospitalizadoEn;
	}

	public void setHospitalizadoEn(String hospitalizadoEn)
	{
		this.hospitalizadoEn = hospitalizadoEn;
	}

	public String getAfiliacion()
	{
		return afiliacion;
	}

	public void setAfiliacion(String afiliacion)
	{
		this.afiliacion = afiliacion;
	}

	public LugarResidencia getLugarResidencia()
	{
		return lugarResidencia;
	}

	public void setLugarResidencia(LugarResidencia lugarResidencia)
	{
		this.lugarResidencia = lugarResidencia;
	}

	public SeguridadSocial getSeguridadSocial()
	{
		return seguridadSocial;
	}

	public void setSeguridadSocial(SeguridadSocial seguridadSocial)
	{
		this.seguridadSocial = seguridadSocial;
	}

}
