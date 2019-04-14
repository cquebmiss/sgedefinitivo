package modelo.persistence;

import javax.persistence.Entity;
import javax.persistence.Id;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity(name = "localidad")
@Getter
@Setter
@NoArgsConstructor
public class Localidad
{
	@Id
	private String idLocalidad;

	private String idEstado;
	private String idMunicipio;
	private String descripcion;

	@Override
	public String toString()
	{
		return this.idEstado + "-" + this.idMunicipio + "-" + this.idLocalidad + "-" + this.descripcion;
	}

}
