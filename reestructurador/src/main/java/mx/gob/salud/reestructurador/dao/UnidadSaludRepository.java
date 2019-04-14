package mx.gob.salud.reestructurador.dao;

import mx.gob.salud.reestructurador.model.entity.UnidadSalud;
import org.springframework.data.repository.CrudRepository;

public interface UnidadSaludRepository extends CrudRepository<UnidadSalud, Integer> {

    public UnidadSalud findByDescripcion(String descripcion);
}
