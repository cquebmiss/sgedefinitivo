package modelo.minutas;

import java.util.Date;

public class Acuerdo
{
	private int		idAcuerdo;
	private Minuta	minuta;
	private String	descripcion;
	private int		orden;
	private String	involucrados;
	private Date	fechaInicio;

	public Acuerdo()
	{
		super();
		// TODO Auto-generated constructor stub
	}

	public Acuerdo(int idAcuerdo, Minuta minuta, String descripcion, int orden, String involucrados, Date fechaInicio)
	{
		super();
		this.idAcuerdo = idAcuerdo;
		this.minuta = minuta;
		this.descripcion = descripcion;
		this.orden = orden;
		this.involucrados = involucrados;
		this.fechaInicio = fechaInicio;
	}

	public int getIdAcuerdo()
	{
		return idAcuerdo;
	}

	public void setIdAcuerdo(int idAcuerdo)
	{
		this.idAcuerdo = idAcuerdo;
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

	public int getOrden()
	{
		return orden;
	}

	public void setOrden(int orden)
	{
		this.orden = orden;
	}

	public String getInvolucrados()
	{
		return involucrados;
	}

	public void setInvolucrados(String involucrados)
	{
		this.involucrados = involucrados;
	}

	public Date getFechaInicio()
	{
		return fechaInicio;
	}

	public void setFechaInicio(Date fechaInicio)
	{
		this.fechaInicio = fechaInicio;
	}

}
