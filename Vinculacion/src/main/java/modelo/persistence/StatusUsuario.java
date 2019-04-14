package modelo.persistence;

import javax.persistence.Entity;
import javax.persistence.Id;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity(name = "statususuario")
@Getter
@Setter
@NoArgsConstructor
public class StatusUsuario
{
	@Id
	private Integer idStatusUsuario;
	private String descripcion;

}
