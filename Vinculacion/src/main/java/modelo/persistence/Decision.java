package modelo.persistence;

import javax.persistence.Entity;
import javax.persistence.Id;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity(name = "decision")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Decision
{
	@Id
	private Integer idDecision;
	private String descripcion;

}
