package modelo.gestion;

public class SeguridadSocial
{
	private int		idSeguridadSocial;
	private String	descripcion;

	public SeguridadSocial()
	{
		super();
		// TODO Auto-generated constructor stub
	}

	public SeguridadSocial(int idSeguridadSocial, String descripcion)
	{
		super();
		this.idSeguridadSocial = idSeguridadSocial;
		this.descripcion = descripcion;
	}

	public int getIdSeguridadSocial()
	{
		return idSeguridadSocial;
	}

	public void setIdSeguridadSocial(int idSeguridadSocial)
	{
		this.idSeguridadSocial = idSeguridadSocial;
	}

	public String getDescripcion()
	{
		return descripcion;
	}

	public void setDescripcion(String descripcion)
	{
		this.descripcion = descripcion;
	}

}
