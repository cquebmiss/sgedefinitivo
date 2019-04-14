package mx.gob.salud.reestructurador;

import lombok.Getter;
import mx.gob.salud.reestructurador.dao.*;
import mx.gob.salud.reestructurador.model.ListElement;
import mx.gob.salud.reestructurador.model.Note;
import mx.gob.salud.reestructurador.model.Task;
import mx.gob.salud.reestructurador.model.Token;
import mx.gob.salud.reestructurador.model.entity.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.time.*;
import java.util.List;

@SpringBootApplication
@Getter
public class ReestructuradorApplication {

    private static final Logger log = LoggerFactory.getLogger(ReestructuradorApplication.class);

    @Bean
    public RestTemplate restTemplate(RestTemplateBuilder builder) {
        return builder.build();
    }

    public static void main(String[] args) {
        SpringApplication.run(ReestructuradorApplication.class, args);
    }

    @Autowired
    GestionRepository gestionRepository;

    @Autowired
    PacienteRepository pacienteRepository;

    @Autowired
    LugarResidenciaRepository lugarResidenciaRepository;

    @Autowired
    UnidadSaludRepository unidadSaludRepository;

    @Autowired
    CategoriaGestionRepository categoriaGestionRepository;

    @Autowired
    SeguridadSocialRepository seguridadSocialRepository;

    @Bean
    public CommandLineRunner run(RestTemplate restTemplate) throws Exception {
        return args -> {

            boolean finalizadas = false;

            Token access_token = new Token();
            access_token.setClient_id("2f9718b85bd8ee41d5f7");
            access_token.setClient_secret("054e16a4b73698c0333f3fd8ac2f20a7406919a10a2d82b6a3426556197a");
            access_token.setCode("c9ee49a30ce376f21f20");


            HttpEntity<Token> request = new HttpEntity<>(access_token);

            ResponseEntity<Token> token = restTemplate.postForEntity("https://www.wunderlist.com/oauth/access_token", request, Token.class);

            access_token.setAccess_token(token.getBody().getAccess_token());

            log.info("El token de acceso obtenido de Wunderlist es: " + access_token.getAccess_token());

            URI targetUrl = UriComponentsBuilder.fromUriString("https://a.wunderlist.com/api")
                    .path("/v1/tasks")
                    .queryParam("list_id", 338456892)
                    .queryParam("completed", finalizadas)
                    .queryParam("client_id", access_token.getClient_id())
                    .queryParam("access_token", access_token.getAccess_token())
                    .build()
                    .encode()
                    .toUri();

            ResponseEntity<List<Task>> tareasResponse = restTemplate.exchange(targetUrl, HttpMethod.GET, null, new ParameterizedTypeReference<List<Task>>() {
            });

            List<Task> tareas = tareasResponse.getBody();

            Instant lastInstant = Instant.parse("2019-02-08T19:02:32.537Z");
            Instant instant;

            Gestion gestion;
            String descripcion;
            LocalDateTime ldCreacion;
            LocalDateTime ldFinalizacion = null;

            for (Task tarea : tareas) {

                //Selecciono solo las tareas que son del 9 de febrero a la fecha
                instant = Instant.parse(tarea.getCreated_at());

                if (!instant.isAfter(lastInstant)) {
                    continue;
                }

                descripcion = tarea.getTitle();

                ldCreacion =  LocalDateTime.ofInstant(instant, ZoneId.of("Mexico/General"));

                if( tarea.getCompleted_at() != null )
                {
                    instant = Instant.parse(tarea.getCompleted_at());
                    ldFinalizacion = LocalDateTime.ofInstant(instant, ZoneId.of("Mexico/General"));
                }


                gestion = new Gestion();
                gestion.setIdGestion(Integer.parseInt(descripcion.substring(14, 17)));
                gestion.setDescripcion(descripcion);
                gestion.setFechaRecepcion(ldCreacion.toLocalDate());

                if( ldFinalizacion != null )
                {
                    gestion.setFechaFinalizacion(ldFinalizacion.toLocalDate());
                }

                gestion.setSolicitadoA("");
                gestion.setSolicitud("");
                gestion.setDetallesGenerales("");
                gestion.setResumenFinal("");
                gestion.setIdUsuario(Integer.valueOf(2));
                gestion.setIdStatusActividad(Integer.valueOf(1));
                gestion.setIdTipoGestion(1);
                gestion.setIdTareaWunderlist(""+tarea.getId());
                gestion.setIdListaWunderlist("338456892");
                gestion.setIdCategoriaGestion(0);
                gestion.setIdStatusActividad(finalizadas ? 1 : -1);

                gestionRepository.save(gestion);

                log.info("Tarea " + tarea.getId() + " - " + tarea.getTitle() + " - " + tarea.isCompleted() + " creada en " + instant);

                //Se complementa la información con la nota de cada tarea..
                URI targetNoteUrl = UriComponentsBuilder.fromUriString("https://a.wunderlist.com/api")
                        .path("/v1/notes")
                        .queryParam("task_id", gestion.getIdTareaWunderlist())
                        .queryParam("client_id", access_token.getClient_id())
                        .queryParam("access_token", access_token.getAccess_token())
                        .build()
                        .encode()
                        .toUri();

                ResponseEntity<List<Note>> note = restTemplate.exchange(targetNoteUrl, HttpMethod.GET, null, new ParameterizedTypeReference<List<Note>>() {
                });

                List<Note> notes = note.getBody();

                String contenido = notes.get(0).getContent();

                int indiceSolicitadoA = contenido.indexOf("Solicitado por:")+15;
                int indiceCategoriaGestion = contenido.indexOf("Categoría de la gestión:")+24;
                int indiceNombre = contenido.indexOf("Nombre:")+7;
                int indiceEdad = contenido.indexOf("Edad:")+5;
                int indiceSexo = contenido.indexOf("Sexo:")+5;
                int indiceLugarOrigen = contenido.indexOf("Lugar de Origen:")+16;
                int indiceSeguridadSocial = contenido.indexOf("Seguridad Social:")+17;
                int indiceFolioAfiliacion = contenido.indexOf("Número o Folio de Afiliación:")+29;
                int indiceAtendidoEn = contenido.indexOf("Atendido en:")+12;
                int indiceReferenciadoA = contenido.indexOf("Referenciado a:")+15;
                int indiceDiagnostico = contenido.indexOf("<DIAGNÓSTICO>:")+14;
                int indiceObservaciones = contenido.indexOf("<OBSERVACIONES DEL CASO>:")+25;
                int indiceSolicitud = contenido.indexOf("<SOLICITUD>:")+12;
                int indiceContactos = contenido.indexOf("Contactos:")+10;
                int indiceConclusion = contenido.indexOf("<CONCLUSION DEL CASO>");

                log.info("Gestión: "+gestion.getIdGestion());


                String solicitadoA = contenido.substring(indiceSolicitadoA, contenido.indexOf("\n", indiceSolicitadoA)).trim();
                String catGestion = contenido.substring(indiceCategoriaGestion,contenido.indexOf("\n", indiceCategoriaGestion)).trim();
                String nombrePaciente = contenido.substring(indiceNombre,contenido.indexOf("\n", indiceNombre)).trim();
                String edadPaciente = contenido.substring(indiceEdad, contenido.indexOf("\n", indiceEdad)).trim();
                String sexoPaciente = contenido.substring(indiceSexo, contenido.indexOf("\n", indiceSexo)).trim().substring(0, 1);
                String lugarOrigen = contenido.substring(indiceLugarOrigen, contenido.indexOf("\n", indiceLugarOrigen)).trim();
                String seguridadSocial = contenido.substring(indiceSeguridadSocial, contenido.indexOf("\n", indiceSeguridadSocial)).trim();
                String folioAfiliacion = contenido.substring(indiceFolioAfiliacion, contenido.indexOf("\n", indiceFolioAfiliacion)).trim();
                String atendidoEn = contenido.substring(indiceAtendidoEn, contenido.indexOf("\n", indiceAtendidoEn)).trim();
                String referenciadoA = contenido.substring(indiceReferenciadoA, contenido.indexOf("\n", indiceReferenciadoA)).trim();
                String diagnostico = contenido.substring(indiceDiagnostico, contenido.indexOf("\n", indiceDiagnostico)).trim();
                String observaciones = contenido.substring(indiceObservaciones, contenido.indexOf("\n", indiceObservaciones)).trim();
                String solicitud = contenido.substring(indiceSolicitud, contenido.indexOf("\n", indiceSolicitud)).trim();
                String contactos = contenido.substring(indiceContactos, contenido.length()).trim();
                String conclusion = null;

                if( indiceConclusion > -1 )
                {
                    conclusion = contenido.substring(indiceConclusion+21, contenido.length()).trim();
                }


                LugarResidencia lugarResidencia = getLugarResidenciaRepository().findByDescripcion(lugarOrigen);

                if( lugarResidencia == null )
                {
                    lugarResidencia = new LugarResidencia();
                    lugarResidencia.setDescripcion(lugarOrigen);
                    getLugarResidenciaRepository().save(lugarResidencia);

                }

                if( atendidoEn.trim().isEmpty() )
                {
                    atendidoEn = "Sin información";
                }

                if( referenciadoA.trim().isEmpty() )
                {
                    referenciadoA = "Sin información";
                }

                if( catGestion.trim().isEmpty() )
                {
                    catGestion = "Otros";
                }

                if( seguridadSocial.trim().isEmpty() )
                {
                    seguridadSocial = "Sin afiliación";
                }

                UnidadSalud unidadSaludAtendidoEn = getUnidadSaludRepository().findByDescripcion(atendidoEn);
                UnidadSalud unidadSaludReferenciadoA = getUnidadSaludRepository().findByDescripcion(referenciadoA);
                CategoriaGestion categoriaGestion = getCategoriaGestionRepository().findByDescripcion(catGestion);
                SeguridadSocial seguridadSocial1 = getSeguridadSocialRepository().findByDescripcion(seguridadSocial);


                Paciente paciente = new Paciente();
                paciente.setIdGestion(gestion.getIdGestion());
                paciente.setNombre(nombrePaciente);
                paciente.setSexo(sexoPaciente);
                paciente.setEdad(Integer.parseInt(edadPaciente));
                paciente.setFechaNacimiento(null);
                paciente.setCURP("");

                paciente.setIdLugarResidencia(lugarResidencia.getIdLugarResidencia());
                paciente.setDiagnostico(diagnostico);
                paciente.setHospitalizadoEn("");

                paciente.setIdSeguridadSocial(null);
                paciente.setAfiliacion(folioAfiliacion);
                paciente.setAtendidoEn(unidadSaludAtendidoEn.getIdUnidadSalud());

                if( unidadSaludReferenciadoA.getIdUnidadSalud() == 0 )
                {
                    paciente.setReferenciadoA(null);
                }
                else
                {
                    paciente.setReferenciadoA(unidadSaludReferenciadoA.getIdUnidadSalud());
                }

                paciente.setIdSeguridadSocial(seguridadSocial1.getIdSeguridadSocial());

                getPacienteRepository().save(paciente);


                gestion.setSolicitadoA(solicitadoA);
                gestion.setSolicitud(solicitud);
                gestion.setDetallesGenerales(observaciones);
                gestion.setIdCategoriaGestion(categoriaGestion.getIdCategoriaGestion());

                if( conclusion != null )
                {
                    gestion.setResumenFinal(conclusion);
                }

                getGestionRepository().save(gestion);


                log.info("Nota encontraa: "+notes.toString());

            }


        };
    }

}
