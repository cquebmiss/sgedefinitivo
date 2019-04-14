package mx.gob.salud.reestructurador.dao;

import mx.gob.salud.reestructurador.model.entity.LugarResidencia;
import org.springframework.data.repository.CrudRepository;

public interface LugarResidenciaRepository extends CrudRepository<LugarResidencia, Integer> {

    public LugarResidencia findByDescripcion(String descripcion);

}
