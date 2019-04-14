package modelo.persistence;

import javax.persistence.Embeddable;
import javax.persistence.MappedSuperclass;

import lombok.Getter;
import lombok.Setter;

@Embeddable
@MappedSuperclass
@Getter
@Setter
public class LocalidadPK
{
	private String idEstado;
	private String idMunicipio;
	private String idLocalidad;

}
