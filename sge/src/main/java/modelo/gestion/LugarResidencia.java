package modelo.gestion;

public class LugarResidencia
{
	private int		idLugarResidencia;
	private String	descripcion;

	public LugarResidencia()
	{
		super();
		// TODO Auto-generated constructor stub
	}

	public LugarResidencia(int idLugarResidencia, String descripcion)
	{
		super();
		this.idLugarResidencia = idLugarResidencia;
		this.descripcion = descripcion;
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

}
