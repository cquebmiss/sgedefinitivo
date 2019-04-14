package modelo.persistence;

import javax.persistence.Entity;
import javax.persistence.Id;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity(name = "permisousuario")
@Getter
@Setter
@NoArgsConstructor
public class PermisoUsuario
{
	@Id
	private Integer idPermisoUsuario;
	
	private String descripcion;

}
