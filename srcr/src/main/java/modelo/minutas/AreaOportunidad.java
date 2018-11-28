package modelo.minutas;

public class AreaOportunidad
{
	private int		idAreaOportunidad;
	private Minuta	minuta;
	private String	descripcion;
	private String	propuestaSolucion;
	private int		orden;

	public AreaOportunidad()
	{
		super();
		// TODO Auto-generated constructor stub
	}

	public AreaOportunidad(int idAreaOportunidad, Minuta minuta, String descripcion, String propuestaSolucion,
			int orden)
	{
		super();
		this.idAreaOportunidad = idAreaOportunidad;
		this.minuta = minuta;
		this.descripcion = descripcion;
		this.propuestaSolucion = propuestaSolucion;
		this.orden = orden;
	}

	public int getIdAreaOportunidad()
	{
		return idAreaOportunidad;
	}

	public void setIdAreaOportunidad(int idAreaOportunidad)
	{
		this.idAreaOportunidad = idAreaOportunidad;
	}

	public Minuta getMinuta()
	{
		return minuta;
	}

	public void setMinuta(Minuta minuta)
	{
		this.minuta = minuta;
	}

	public String getDescripcion()
	{
		return descripcion;
	}

	public void setDescripcion(String descripcion)
	{
		this.descripcion = descripcion;
	}

	public String getPropuestaSolucion()
	{
		return propuestaSolucion;
	}

	public void setPropuestaSolucion(String propuestaSolucion)
	{
		this.propuestaSolucion = propuestaSolucion;
	}

	public int getOrden()
	{
		return orden;
	}

	public void setOrden(int orden)
	{
		this.orden = orden;
	}

}
