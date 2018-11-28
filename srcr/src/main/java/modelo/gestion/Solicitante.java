package modelo.gestion;

public class Solicitante
{
	private String	descripcion;
	private int		total;

	public Solicitante()
	{
		super();
		this.total = 1;
		// TODO Auto-generated constructor stub
	}

	public void incrementaTotal()
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
