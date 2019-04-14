package modelo.persistence;

import javax.persistence.Entity;
import javax.persistence.Id;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

public class SeguridadSocial {

	@Id
	private Integer idSeguridadSocial;

	private String descripcion;


}
