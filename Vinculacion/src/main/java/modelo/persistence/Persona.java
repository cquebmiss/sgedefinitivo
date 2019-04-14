package modelo.persistence;

import java.time.LocalDate;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity(name = "persona")
@Getter
@Setter
@NoArgsConstructor
public class Persona
{
	@Id
	private Integer idPersona;

	private String nombres;
	private String apPaterno;
	private String apMaterno;
	private Character sexo;


	private LocalDate fechaNacimiento;
	private String direccion;
	private String telefonoCelular;
	private Boolean whatsApp;
	private Boolean sMS;
	private String email;
	private String facebook;
	private String twitter;
	private String instagram;
	private Boolean apoyaCompartirRedesSociales;
	private String ocupacion;

	@ManyToOne
	@JoinColumn(name = "idLocalidad", referencedColumnName = "idLocalidad")
	private Localidad localidad;
	private Boolean vive;
	private String observaciones;

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "idDecision", referencedColumnName = "idDecision")
	private Decision decision;

	@Override
	public String toString()
	{
		return this.nombres + " " + this.apMaterno + " " + this.apMaterno;
	}

}
