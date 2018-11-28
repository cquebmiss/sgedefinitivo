package modelo.minutas;

public class TipoMinuta
{
	private int		idTipoMinuta;
	private String	descripcion;

	public TipoMinuta()
	{
		super();
		// TODO Auto-generated constructor stub
	}

	public TipoMinuta(int idTipoMinuta, String descripcion)
	{
		super();
		this.idTipoMinuta = idTipoMinuta;
		this.descripcion = descripcion;
	}

	public int getIdTipoMinuta()
	{
		return idTipoMinuta;
	}

	public void setIdTipoMinuta(int idTipoMinuta)
	{
		this.idTipoMinuta = idTipoMinuta;
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
