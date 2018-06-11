package modelo.gestion;

import java.util.Date;
import java.util.List;

public class Persona
{
	private int				idPersona;
	private String			nombres;
	private String			apPaterno;
	private String			apMaterno;
	private String			sexo;
	private int				edad;
	private Date			fechaNacimiento;
	private String			CURP;
	private LugarResidencia	lugarResidencia;
	private SeguridadSocial	seguridadSocial;
	private String			afiliacion;
	private List<Contacto>	contactos;

	public Persona()
	{
		super();
		// TODO Auto-generated constructor stub
	}

	public Persona(int idPersona, String nombres, String apPaterno, String apMaterno, String sexo, int edad,
			Date fechaNacimiento, String cURP, LugarResidencia lugarResidencia, SeguridadSocial seguridadSocial,
			String afiliacion, List<Contacto> contactos)
	{
		super();
		this.idPersona = idPersona;
		this.nombres = nombres;
		this.apPaterno = apPaterno;
		this.apMaterno = apMaterno;
		this.sexo = sexo;
		this.edad = edad;
		this.fechaNacimiento = fechaNacimiento;
		CURP = cURP;
		this.lugarResidencia = lugarResidencia;
		this.seguridadSocial = seguridadSocial;
		this.afiliacion = afiliacion;
		this.contactos = contactos;
	}

	public int getIdPersona()
	{
		return idPersona;
	}

	public void setIdPersona(int idPersona)
	{
		this.idPersona = idPersona;
	}

	public String getNombres()
	{
		return nombres;
	}

	public void setNombres(String nombres)
	{
		this.nombres = nombres;
	}

	public String getApPaterno()
	{
		return apPaterno;
	}

	public void setApPaterno(String apPaterno)
	{
		this.apPaterno = apPaterno;
	}

	public String getApMaterno()
	{
		return apMaterno;
	}

	public void setApMaterno(String apMaterno)
	{
		this.apMaterno = apMaterno;
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

	public String getCURP()
	{
		return CURP;
	}

	public void setCURP(String cURP)
	{
		CURP = cURP;
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

	public String getAfiliacion()
	{
		return afiliacion;
	}

	public void setAfiliacion(String afiliacion)
	{
		this.afiliacion = afiliacion;
	}

	public List<Contacto> getContactos()
	{
		return contactos;
	}

	public void setContactos(List<Contacto> contactos)
	{
		this.contactos = contactos;
	}

}
