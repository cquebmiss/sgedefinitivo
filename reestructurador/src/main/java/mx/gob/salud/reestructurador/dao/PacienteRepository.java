package mx.gob.salud.reestructurador.dao;

import mx.gob.salud.reestructurador.model.entity.Paciente;
import org.springframework.data.repository.CrudRepository;

public interface PacienteRepository extends CrudRepository<Paciente, Integer> {
}
