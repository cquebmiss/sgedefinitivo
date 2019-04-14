package mx.gob.salud.reestructurador.model.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity(name = "seguridadsocial")
@Getter
@Setter
@NoArgsConstructor
public class SeguridadSocial {

    @Id
    private Integer idSeguridadSocial;

    private String descripcion;
}
