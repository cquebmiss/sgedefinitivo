package modelo.persistence;

import javax.persistence.EmbeddedId;
import javax.persistence.Entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity(name = "confusuario")
@Getter
@Setter
@NoArgsConstructor
public class ConfUsuario
{
	@EmbeddedId
	ConfUsuarioPK confUsuarioPK;

}
