package modelo.gestion;

public class Contacto
{
	private int		idContacto;
	private String	nombres;
	private String	telefonos;
	private String	email;
	private String	observaciones;

	public Contacto()
	{
		super();
		// TODO Auto-generated constructor stub
	}

	public Contacto(int idContacto, String nombres, String telefonos, String email, String observaciones)
	{
		super();
		this.idContacto = idContacto;
		this.nombres = nombres;
		this.telefonos = telefonos;
		this.email = email;
		this.observaciones = observaciones;
	}

	public int getIdContacto()
	{
		return idContacto;
	}

	public void setIdContacto(int idContacto)
	{
		this.idContacto = idContacto;
	}

	public String getNombres()
	{
		return nombres;
	}

	public void setNombres(String nombres)
	{
		this.nombres = nombres;
	}

	public String getTelefonos()
	{
		return telefonos;
	}

	public void setTelefonos(String telefonos)
	{
		this.telefonos = telefonos;
	}

	public String getEmail()
	{
		return email;
	}

	public void setEmail(String email)
	{
		this.email = email;
	}

	public String getObservaciones()
	{
		return observaciones;
	}

	public void setObservaciones(String observaciones)
	{
		this.observaciones = observaciones;
	}

}
