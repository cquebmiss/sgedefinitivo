package mx.gob.salud.reestructurador.model.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.Id;
import java.time.LocalDate;

@Entity(name = "gestion")
@Getter
@Setter
@NoArgsConstructor
public class Gestion
{
    @Id
    private Integer idGestion;

    private String descripcion;
    private LocalDate fechaRecepcion;
    private String solicitadoA;
    private String solicitud;
    private String detallesGenerales;
    private LocalDate fechaFinalizacion;
    private String resumenFinal;
    private Integer idUsuario;
    private Integer idStatusActividad;
    private Integer idTipoGestion;
    private String idTareaWunderlist;
    private String idListaWunderlist;
    private Integer idCategoriaGestion;

}
