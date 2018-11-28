package modelo.gestion;

public class TipoGestion
{
	private int		idTipoGestion;
	private String	descripcion;

	public TipoGestion()
	{
		super();
		// TODO Auto-generated constructor stub
	}

	public TipoGestion(int idTipoGestion, String descripcion)
	{
		super();
		this.idTipoGestion = idTipoGestion;
		this.descripcion = descripcion;
	}

	public int getIdTipoGestion()
	{
		return idTipoGestion;
	}

	public void setIdTipoGestion(int idTipoGestion)
	{
		this.idTipoGestion = idTipoGestion;
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
