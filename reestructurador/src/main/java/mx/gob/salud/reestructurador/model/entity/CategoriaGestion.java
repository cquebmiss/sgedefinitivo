package mx.gob.salud.reestructurador.model.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity(name = "categoriagestion")
@Getter
@Setter
@NoArgsConstructor
public class CategoriaGestion {

    @Id
    private Integer idCategoriaGestion;

    private String descripcion;
}
