package modelo.gestion;

public class CategoriaGestion
{
	private int		idCategoriaGestion;
	private String	descripcion;

	public CategoriaGestion()
	{
		super();
		// TODO Auto-generated constructor stub
	}

	public CategoriaGestion(int idCategoriaGestion, String descripcion)
	{
		super();
		this.idCategoriaGestion = idCategoriaGestion;
		this.descripcion = descripcion;
	}

	public int getIdCategoriaGestion()
	{
		return idCategoriaGestion;
	}

	public void setIdCategoriaGestion(int idCategoriaGestion)
	{
		this.idCategoriaGestion = idCategoriaGestion;
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
