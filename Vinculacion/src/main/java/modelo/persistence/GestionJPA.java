package modelo.persistence;

import javax.persistence.Column;
import javax.persistence.Id;


public class GestionJPA {

	
	@Id
	@Column(name="idGestion")
	private int idGestion;
	
	@Column(name="descripcion")
	private String descripcion;

	public GestionJPA() {
		super();
		// TODO Auto-generated constructor stub
	}

	public GestionJPA(int idGestion, String descripcion) {
		super();
		this.idGestion = idGestion;
		this.descripcion = descripcion;
	}

	public int getIdGestion() {
		return idGestion;
	}

	public void setIdGestion(int idGestion) {
		this.idGestion = idGestion;
	}

	public String getDescripcion() {
		return descripcion;
	}

	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}

}
