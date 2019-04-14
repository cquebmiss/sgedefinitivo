package mx.gob.salud.reestructurador.dao;

import mx.gob.salud.reestructurador.model.entity.SeguridadSocial;
import org.springframework.data.repository.CrudRepository;

public interface SeguridadSocialRepository extends CrudRepository<SeguridadSocial, Integer> {

    SeguridadSocial findByDescripcion(String descripcion);
}
