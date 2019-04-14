package modelo.persistence;

import java.util.List;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity(name = "usuario")
@Getter
@Setter
@NoArgsConstructor
public class Usuario
{
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer idUsuario;

	private String nombre;
	private String contraseña;

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "idPermisoUsuario", referencedColumnName = "idPermisoUsuario")
	private PermisoUsuario permisoUsuario;

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "idStatusUsuario", referencedColumnName = "idStatusUsuario")
	private StatusUsuario statusUsuario;

	@OneToMany(fetch = FetchType.EAGER)
	@JoinColumn(name = "idUsuario", referencedColumnName = "idUsuario")
	private List<ConfUsuario> confUsuario;

}
