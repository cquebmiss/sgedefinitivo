package mx.gob.salud.reestructurador.dao;

import mx.gob.salud.reestructurador.model.entity.CategoriaGestion;
import org.springframework.data.repository.CrudRepository;

public interface CategoriaGestionRepository extends CrudRepository<CategoriaGestion, Integer> {

    public CategoriaGestion findByDescripcion(String descripcion);
}
