package modelo.gestion;

public class LugarResidencia
{
	private int		idLugarResidencia;
	private String	descripcion;

	//Auxiliar para los reportes
	private int		total;

	public LugarResidencia()
	{
		super();
		this.total = 1;
		// TODO Auto-generated constructor stub
	}

	public LugarResidencia(int idLugarResidencia, String descripcion)
	{
		super();
		this.idLugarResidencia = idLugarResidencia;
		this.descripcion = descripcion;
	}

	public void incrementar()
	{
		this.total++;
	}

	public int getIdLugarResidencia()
	{
		return idLugarResidencia;
	}

	public void setIdLugarResidencia(int idLugarResidencia)
	{
		this.idLugarResidencia = idLugarResidencia;
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
