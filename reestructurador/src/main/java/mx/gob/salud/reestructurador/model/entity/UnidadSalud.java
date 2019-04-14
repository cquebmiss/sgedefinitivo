package mx.gob.salud.reestructurador.model.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity(name = "unidadsalud")
@Getter
@Setter
@NoArgsConstructor
public class UnidadSalud
{
    @Id
    private Integer idUnidadSalud;

    private String descripcion;
}
