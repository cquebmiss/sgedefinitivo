package mx.gob.salud.reestructurador.model.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity(name = "lugarresidencia")
@Getter
@Setter
@NoArgsConstructor
public class LugarResidencia {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer idLugarResidencia;

    private String descripcion;
    private String cve_agee;
    private String estado;
    private String cve_agem;
    private String municipio;
    private String cve_loc;
    private String localidad;


}
