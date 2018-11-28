package modelo.gestion;

public class Edad
{
	private String	descripcion;

	//auxiliar para reportes
	private int		total;

	public Edad()
	{
		super();
		// TODO Auto-generated constructor stub
	}

	public void incrementar()
	{
		this.total++;
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
