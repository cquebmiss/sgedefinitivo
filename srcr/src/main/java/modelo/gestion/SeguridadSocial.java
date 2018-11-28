package modelo.gestion;

public class SeguridadSocial
{
	private int		idSeguridadSocial;
	private String	descripcion;

	//auxiliar para reportes
	private int		total;

	public SeguridadSocial()
	{
		super();
		this.total = 1;
		// TODO Auto-generated constructor stub
	}

	public SeguridadSocial(int idSeguridadSocial, String descripcion)
	{
		super();
		this.idSeguridadSocial = idSeguridadSocial;
		this.descripcion = descripcion;
	}

	public void incrementar()
	{
		this.total++;
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

	public int getTotal()
	{
		return total;
	}

	public void setTotal(int total)
	{
		this.total = total;
	}

}
