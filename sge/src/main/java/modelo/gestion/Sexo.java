package modelo.gestion;

public class Sexo
{
	private String	descripcion;

	//auxiliar para reportes
	private int		total;

	public Sexo()
	{
		super();
		this.total = 1;
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
