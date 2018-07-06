package modelo.gestion;

public class CategoriaGestion
{
	private int		idCategoriaGestion;
	private String	descripcion;

	//auxiliar en la presentación de resultados, para colocar el total de gestiones de categoría
	private int		total;

	public CategoriaGestion()
	{
		super();
		this.total = 1;
		// TODO Auto-generated constructor stub
	}

	public CategoriaGestion(int idCategoriaGestion, String descripcion)
	{
		super();
		this.idCategoriaGestion = idCategoriaGestion;
		this.descripcion = descripcion;
	}

	public void incrementa()
	{
		this.total++;
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

	public int getTotal()
	{
		return total;
	}

	public void setTotal(int total)
	{
		this.total = total;
	}

}
