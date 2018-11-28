package modelo.minutas;

import java.util.Date;

public class Acuerdo
{
	private int		idAcuerdo;
	private Minuta	minuta;
	private String	descripcion;
	private int		orden;
	private String	observaciones;
	private Date	fechaInicio;

	public Acuerdo()
	{
		super();
		// TODO Auto-generated constructor stub
	}

	public Acuerdo(int idAcuerdo, Minuta minuta, String descripcion, int orden, String observaciones, Date fechaInicio)
	{
		super();
		this.idAcuerdo = idAcuerdo;
		this.minuta = minuta;
		this.descripcion = descripcion;
		this.orden = orden;
		this.observaciones = observaciones;
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

	public Date getFechaInicio()
	{
		return fechaInicio;
	}

	public void setFechaInicio(Date fechaInicio)
	{
		this.fechaInicio = fechaInicio;
	}

	public String getObservaciones()
	{
		return observaciones;
	}

	public void setObservaciones(String observaciones)
	{
		this.observaciones = observaciones;
	}

}
