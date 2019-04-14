package mx.gob.salud.reestructurador.model.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.time.LocalDate;

@Entity(name = "paciente")
@NoArgsConstructor
@Getter
@Setter
public class Paciente
{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer idPaciente;

    private Integer idGestion;
    private String nombre;
    private String sexo;
    private Integer edad;
    private LocalDate fechaNacimiento;
    private String cURP;
    private Integer idLugarResidencia;
    private String diagnostico;
    private String hospitalizadoEn;
    private Integer idSeguridadSocial;
    private String afiliacion;
    private Integer atendidoEn;
    private Integer referenciadoA;


}
