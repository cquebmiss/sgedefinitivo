package gui.portal.gestion;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;

import org.primefaces.event.SelectEvent;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMappingException;
import com.amazonaws.services.dynamodbv2.datamodeling.TransactionLoadRequest;
import com.amazonaws.services.dynamodbv2.datamodeling.TransactionWriteRequest;
import com.amazonaws.services.dynamodbv2.model.InternalServerErrorException;
import com.amazonaws.services.dynamodbv2.model.ResourceNotFoundException;
import com.amazonaws.services.dynamodbv2.model.TransactionCanceledException;

import lombok.Getter;
import lombok.Setter;
import modelo.gestion.ListElement;
import modelo.gestion.Task;
import modelo.gestion.json.EstadoINEGI;
import modelo.gestion.json.LocalidadINEGI;
import modelo.gestion.json.MunicipioINEGI;
import modelo.gestion.json.RespuestaEstadosJson;
import modelo.gestion.json.RespuestaLocalidadesJson;
import modelo.gestion.json.RespuestaMunicipiosJson;
import persistence.dynamodb.CategoriaGestionAWS;
import persistence.dynamodb.Contacto;
import persistence.dynamodb.GestionAWS;
import persistence.dynamodb.LugarResidencia;
import persistence.dynamodb.SeguridadSocialAWS;
import persistence.dynamodb.StatusActividadAWS;
import persistence.dynamodb.TipoDescuentoAWS;
import persistence.dynamodb.TipoGestionAWS;
import persistence.dynamodb.UnidadSaludAWS;
import service.INEGIService;
import util.utilidades;
import util.gestion.UtilidadesGestion;

@ManagedBean
@SessionScoped
@Getter
@Setter
public class NuevaGestionBean
{
	// Dado que una gestión es una actividad se comparte el catálogo de status de
	// actividades
	private List<StatusActividadAWS>	catStatusActividadAWS;
	private List<TipoGestionAWS>		catTiposGestionAWS;
	private List<SeguridadSocialAWS>	catSeguridadSocialAWS;
	private List<CategoriaGestionAWS>	catCategoriaGestionAWS;
	private List<UnidadSaludAWS>		catUnidadSaludAWS;
	private List<TipoDescuentoAWS>		catTipoDescuentoAWS;

	private GestionAWS					gestion;

	private int							editarGestion;
	private String						webservice;

	private List<ListElement>			listasUsuario;
	private List<Task>					listaTareas;

	// WebService de INEGI
	private INEGIService				inegiService	= new INEGIService();
	private List<EstadoINEGI>			estados;
	private List<MunicipioINEGI>		municipios;
	private List<LocalidadINEGI>		localidades;

	public NuevaGestionBean()
	{
		super();
		// TODO Auto-generated constructor stub
	}

	@PostConstruct
	public void postConstruct()
	{

		try
		{
			this.gestion = new GestionAWS();

		} catch (Exception e)
		{
			this.webservice = e.getMessage();
			e.printStackTrace();
		}

	}

	public void iniciaNuevaGestion()
	{
		this.catStatusActividadAWS = UtilidadesGestion.getCatStatusActividadAWS();
		this.catTiposGestionAWS = UtilidadesGestion.getCatTipoGestionAWS();
		this.catSeguridadSocialAWS = UtilidadesGestion.getCatSeguridadSocialAWS();
		this.catCategoriaGestionAWS = UtilidadesGestion.getCatCategoriaGestionAWS();
		setCatUnidadSaludAWS(UtilidadesGestion.getCatUnidadSaludAWS());
		setCatTipoDescuentoAWS(UtilidadesGestion.getCatTipoDescuentoAWS());

		this.gestion = new GestionAWS();
		// Se hardcodea en lugar de elegir desde el catálogo
		// this.gestion.setStatus(this.catStatusActividad.get(0));
		// this.gestion.setTipoGestion(new TipoGestion(-1, ""));

	}

	public void iniciaEdicionGestion(GestionAWS gestionEdicion)
	{
		this.catStatusActividadAWS = UtilidadesGestion.getCatStatusActividadAWS();
		this.catTiposGestionAWS = UtilidadesGestion.getCatTipoGestionAWS();
		this.catSeguridadSocialAWS = UtilidadesGestion.getCatSeguridadSocialAWS();
		this.catCategoriaGestionAWS = UtilidadesGestion.getCatCategoriaGestionAWS();
		setCatUnidadSaludAWS(UtilidadesGestion.getCatUnidadSaludAWS());
		setCatTipoDescuentoAWS(UtilidadesGestion.getCatTipoDescuentoAWS());

		this.gestion = gestionEdicion;

		/*
		 * // Se indica cual es la categoría y la seguridad social for (CategoriaGestion
		 * cat : this.catCategoriaGestion) { if
		 * (this.gestion.getCategoria().getIdCategoriaGestion() ==
		 * cat.getIdCategoriaGestion()) { this.gestion.setCategoria(cat); }
		 * 
		 * }
		 * 
		 * for (SeguridadSocial ss : this.catSeguridadSocial) { if
		 * (this.gestion.getPaciente().getSeguridadSocial().getIdSeguridadSocial() ==
		 * ss.getIdSeguridadSocial()) {
		 * this.gestion.getPaciente().setSeguridadSocial(ss); }
		 * 
		 * }
		 * 
		 * for (UnidadSalud us : this.catUnidadSalud) { if
		 * (this.gestion.getPaciente().getAtendidoEn() != null &&
		 * this.gestion.getPaciente().getAtendidoEn().getIdUnidadSalud() ==
		 * us.getIdUnidadSalud()) { this.gestion.getPaciente().setAtendidoEn(us); }
		 * 
		 * if (this.gestion.getPaciente().getReferenciadoA() != null &&
		 * this.gestion.getPaciente().getReferenciadoA().getIdUnidadSalud() ==
		 * us.getIdUnidadSalud()) { this.gestion.getPaciente().setReferenciadoA(us); }
		 * 
		 * }
		 * 
		 * if (this.gestion.getCosto().getTipoDescuento() != null) { for (TipoDescuento
		 * tipoDescuento : getCatTipoDescuento()) { if
		 * (this.gestion.getCosto().getTipoDescuento().getIdTipoDescuento() ==
		 * tipoDescuento .getIdTipoDescuento()) {
		 * this.gestion.getCosto().setTipoDescuento(tipoDescuento); break; } }
		 * 
		 * }
		 * 
		 * // Se llenan los catálogos de INEGI manualmente desde el objeto de gestión
		 * para // que los convertidores puedan hacer su función if
		 * (gestionEdicion.getPaciente().getLugarResidencia() != null) { if
		 * (gestionEdicion.getPaciente().getLugarResidencia().getEstadoINEGI() != null)
		 * { this.estados =
		 * Arrays.asList(gestionEdicion.getPaciente().getLugarResidencia().
		 * getEstadoINEGI()); }
		 * 
		 * if (gestionEdicion.getPaciente().getLugarResidencia().getMunicipioINEGI() !=
		 * null) { this.municipios =
		 * Arrays.asList(gestionEdicion.getPaciente().getLugarResidencia().
		 * getMunicipioINEGI()); }
		 * 
		 * if (gestionEdicion.getPaciente().getLugarResidencia().getLocalidadINEGI() !=
		 * null) { this.localidades =
		 * Arrays.asList(gestionEdicion.getPaciente().getLugarResidencia().
		 * getLocalidadINEGI()); }
		 * 
		 * }
		 */
	}

	public void actionNuevaGestion()
	{
		iniciaNuevaGestion();

		try
		{
			ExternalContext ec = FacesContext.getCurrentInstance().getExternalContext();
			ec.redirect(ec.getRequestContextPath() + "/portal/gestion/nuevagestion.jsf");
		} catch (IOException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void activarModoEdicion()
	{
		this.editarGestion = 0;

		// this.gestion.updateAllDataBD();

		try
		{
			FacesContext.getCurrentInstance().getExternalContext().redirect("historialgestion.jsf");
		} catch (IOException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void blur()
	{

		if (this.gestion.getPaciente().getLugarResidencia().getEstadoINEGI() == null)
		{
			this.gestion.getPaciente().getLugarResidencia().setMunicipioINEGI(null);
			this.gestion.getPaciente().getLugarResidencia().setLocalidadINEGI(null);
		}
		else if (this.gestion.getPaciente().getLugarResidencia().getMunicipioINEGI() == null)
		{
			this.gestion.getPaciente().getLugarResidencia().setLocalidadINEGI(null);
		}

		System.out.println("Estado: "
				+ (this.gestion.getPaciente().getLugarResidencia().getEstadoINEGI() != null
						? this.gestion.getPaciente().getLugarResidencia().getEstadoINEGI().getNom_agee()
						: " NA")
				+ (this.gestion.getPaciente().getLugarResidencia().getMunicipioINEGI() != null
						? this.gestion.getPaciente().getLugarResidencia().getMunicipioINEGI().getNom_agem()
						: "N/A")
				+ (this.gestion.getPaciente().getLugarResidencia().getLocalidadINEGI() != null
						? this.gestion.getPaciente().getLugarResidencia().getLocalidadINEGI().getNom_loc()
						: "N/A"));
	}

	// MÉTODOS PARA AUTOCOMPLETES
	public List<String> autoCompleteSolicitadoA(String query)
	{
		return UtilidadesGestion.getCoincidenciasSolicitantes(query);
	}

	public List<EstadoINEGI> autoCompleteLugarOrigen(String query)
	{
		return getEstadosINEGI(query);
		// return UtilidadesGestion.getCoincidenciasLugarOrigen(query);
	}

	public List<MunicipioINEGI> autoCompleteMunicipio(String query)
	{
		return this.municipios != null
				? (List<MunicipioINEGI>) this.municipios.stream()
						.filter(mun -> mun.getNom_agem().toLowerCase().contains(query.toLowerCase()))
						.collect(Collectors.toList())
				: new ArrayList<>();
	}

	public List<LocalidadINEGI> autoCompleteLocalidades(String query)
	{
		return this.localidades != null
				? (List<LocalidadINEGI>) this.localidades.stream()
						.filter(loc -> loc.getNom_loc().toLowerCase().contains(query.toLowerCase()))
						.collect(Collectors.toList())
				: new ArrayList<>();
	}

	public void onItemSelectEstado(SelectEvent event)
	{
		System.out.println("Estado: "
				+ (this.gestion.getPaciente().getLugarResidencia().getEstadoINEGI() != null
						? this.gestion.getPaciente().getLugarResidencia().getEstadoINEGI().getNom_agee()
						: " NA")
				+ (this.gestion.getPaciente().getLugarResidencia().getMunicipioINEGI() != null
						? this.gestion.getPaciente().getLugarResidencia().getMunicipioINEGI().getNom_agem()
						: "N/A")
				+ (this.gestion.getPaciente().getLugarResidencia().getLocalidadINEGI() != null
						? this.gestion.getPaciente().getLugarResidencia().getLocalidadINEGI().getNom_loc()
						: "N/A"));

		EstadoINEGI estadoSelec = (EstadoINEGI) event.getObject();

		setMunicipiosINEGI(estadoSelec.getCve_agee());

		System.out.println("Estado: "
				+ (this.gestion.getPaciente().getLugarResidencia().getEstadoINEGI() != null
						? this.gestion.getPaciente().getLugarResidencia().getEstadoINEGI().getNom_agee()
						: " NA")
				+ (this.gestion.getPaciente().getLugarResidencia().getMunicipioINEGI() != null
						? this.gestion.getPaciente().getLugarResidencia().getMunicipioINEGI().getNom_agem()
						: "N/A")
				+ (this.gestion.getPaciente().getLugarResidencia().getLocalidadINEGI() != null
						? this.gestion.getPaciente().getLugarResidencia().getLocalidadINEGI().getNom_loc()
						: "N/A"));
	}

	public void setMunicipiosINEGI(String cve_agee)
	{
		this.municipios = getMunicipiosINEGI(cve_agee);
		this.gestion.getPaciente().getLugarResidencia().setMunicipioINEGI(null);
		this.localidades = null;
		this.gestion.getPaciente().getLugarResidencia().setLocalidadINEGI(null);
	}

	public void onItemSelectMunicipio(SelectEvent event)
	{
		MunicipioINEGI municipioSelec = (MunicipioINEGI) event.getObject();
		this.localidades = getLocalidadesINEGI(municipioSelec.getCve_agee(), municipioSelec.getCve_agem());
		this.gestion.getPaciente().getLugarResidencia().setLocalidadINEGI(null);
	}

	public List<EstadoINEGI> getEstadosINEGI(String busqueda)
	{
		RespuestaEstadosJson respuesta = this.inegiService.getEstados(busqueda);

		this.estados = respuesta.getDatos();

		if (this.estados == null)
		{
			this.estados = new ArrayList<>();
		}

		this.estados.forEach(estado -> System.out.println(estado.getNom_agee()));

		return this.estados;
	}

	public List<MunicipioINEGI> getMunicipiosINEGI(String idEstado)
	{
		RespuestaMunicipiosJson respuesta = this.inegiService.getMunicipios(idEstado);

		this.municipios = respuesta.getDatos();

		if (this.municipios == null)
		{
			this.municipios = new ArrayList<>();
		}

		this.municipios.forEach(mun -> System.out.println(mun.getNom_agem()));

		return this.municipios;
	}

	public List<LocalidadINEGI> getLocalidadesINEGI(String idEstado, String idMunicipio)
	{
		RespuestaLocalidadesJson respuesta = this.inegiService.getLocalidades(idEstado, idMunicipio);

		this.localidades = respuesta.getDatos();

		if (this.localidades == null)
		{
			this.localidades = new ArrayList<>();
		}

		this.localidades.forEach(loc -> System.out.println(loc.getNom_loc()));

		return this.localidades;
	}

	// FIN DE MÉTODOS PARA AUTOCOMPLETES

	// MÉTODOS PARA CONTACTOS
	public void actionEliminarContact(Contacto contacto)
	{
		this.gestion.getContactos().remove(contacto);

	}

	// FIN DE MÉTODOS PARA CONTACTOS

	public void actionGuardarSolicitud()
	{

		System.out.println("Guardando solicitud");

		if (this.gestion.getPaciente().getNombre().trim().isEmpty() || this.gestion.getSolicitud().trim().isEmpty())
		{
			String mensaje = "";

			if (this.gestion.getPaciente().getNombre().trim().isEmpty())
			{
				mensaje = "Por favor, ingrese el nombre del paciente.";
			}
			else
			{
				mensaje = "Por favor, ingrese la solicitud deseada.";
			}

			FacesContext.getCurrentInstance().addMessage(null,
					new FacesMessage(FacesMessage.SEVERITY_ERROR, "Formulario Incorrecto", mensaje));
			return;
		}

		DynamoDBMapper mapper = new DynamoDBMapper(utilidades.getAWSDynamoDBClient());
		this.gestion.setFechaCreacion(LocalDateTime.now());
		this.gestion.setFechaFinalizacion(LocalDate.now());
		this.gestion.setFechaRecepcion(LocalDate.now());

		LugarResidencia lResidencia = this.gestion.getPaciente().getLugarResidencia();

		if (lResidencia.getEstadoINEGI() == null)
		{
			lResidencia.setDescripcion("Sin información");
		}
		else
		{
			String ubicFinal = lResidencia.getEstadoINEGI().getNom_agee();

			if (lResidencia.getMunicipioINEGI() != null)
			{
				ubicFinal += ", " + lResidencia.getMunicipioINEGI().getNom_agem();

				if (lResidencia.getLocalidadINEGI() != null)
				{
					ubicFinal += ", " + lResidencia.getLocalidadINEGI().getNom_loc();
				}

			}

			lResidencia.setDescripcion(ubicFinal);

		}

		// se reordena el índice de los contactos
		for (int x = 0; x < this.gestion.getContactos().size(); x++)
		{
			this.gestion.getContactos().get(x).setIdContacto(x);
		}

		if (this.gestion.getIdGestion() == -1)
		{
			// Se cuentan cuantas gestiones hay para asignar el nuevo Id
			int totalGestiones = UtilidadesGestion.countGestionAWS(this.gestion.getFechaCreacion());
			this.gestion.setIdGestion((totalGestiones + 1));
		}
		

		TransactionWriteRequest transactionWriteRequest = new TransactionWriteRequest();
		transactionWriteRequest.addPut(this.gestion);
		
		utilidades.executeTransactionWrite(transactionWriteRequest);
		
		
		/*
		 * WUsuario wUsuario = new WUsuario(); wUsuario.initClient();
		 * wUsuario.initToken();
		 */
		/*
		 * PreparedStatement prep = null; ResultSet rBD = null;
		 * 
		 * boolean nuevaGestion = this.gestion.getIdGestion() < 0 ? true : false;
		 * 
		 * try (Connection conexion = ((DataBase)
		 * FacesUtils.getManagedBean("database")).getConnectionGestiones();) {
		 * 
		 * try {
		 * 
		 * // Se obtiene el Usuario de la sesión Sesion sesion = (Sesion)
		 * FacesUtils.getManagedBean("Sesion"); conexion.setAutoCommit(false);
		 * conexion.rollback();
		 * 
		 * LugarResidencia lResidencia =
		 * this.gestion.getPaciente().getLugarResidencia();
		 * 
		 * if (lResidencia.getEstadoINEGI() == null) {
		 * lResidencia.setDescripcion("Sin información"); } else { String ubicFinal =
		 * lResidencia.getEstadoINEGI().getNom_agee();
		 * 
		 * if (lResidencia.getMunicipioINEGI() != null) { ubicFinal += ", " +
		 * lResidencia.getMunicipioINEGI().getNom_agem();
		 * 
		 * if (lResidencia.getLocalidadINEGI() != null) { ubicFinal += ", " +
		 * lResidencia.getLocalidadINEGI().getNom_loc(); }
		 * 
		 * }
		 * 
		 * lResidencia.setDescripcion(ubicFinal);
		 * 
		 * }
		 * 
		 * if (this.gestion.getIdGestion() < 0) { int indiceComplemento = 10; String
		 * complementoAtributo = ""; String complementoValor = "";
		 * 
		 * String complementoAtributoW = ""; String complementoValorW = "";
		 * 
		 * if (!this.gestion.getFolio().trim().isEmpty()) { complementoAtributo =
		 * ", idGestion"; complementoValor += ",?"; }
		 * 
		 * if (!this.gestion.getIdTareaWunderlist().trim().isEmpty()) {
		 * complementoAtributoW = ", idTareaWunderlist"; complementoValorW += ",?";
		 * 
		 * }
		 * 
		 * // Primer paso capturar la gestión en la base de datos del sistema prep =
		 * conexion.prepareStatement(
		 * "INSERT INTO gestion (Descripcion,FechaRecepcion,SolicitadoA,Solicitud,DetallesGenerales,ResumenFinal,"
		 * + "idUsuario,idStatusActividad,idTipoGestion,idCategoriaGestion" +
		 * complementoAtributo + complementoAtributoW + ")\n" +
		 * "VALUES (?, ?, ?, ?, ?, '', ?, ?, ?, ?" + complementoValor +
		 * complementoValorW + ") ; ", PreparedStatement.RETURN_GENERATED_KEYS);
		 * 
		 * prep.setString(1, this.gestion.getDescripcion()); // prep.setDate(2, new //
		 * java.sql.Date(this.gestion.getFechaRecepcion().getTime())); prep.setString(3,
		 * this.gestion.getSolicitadoA()); prep.setString(4,
		 * this.gestion.getSolicitud()); prep.setString(5,
		 * this.gestion.getDetallesGenerales()); prep.setInt(6,
		 * Integer.parseInt(sesion.getIdUsuario())); prep.setInt(7,
		 * this.gestion.getStatus().getIdStatusActividad()); prep.setInt(8,
		 * this.gestion.getTipoGestion().getIdTipoGestion()); prep.setInt(9,
		 * this.gestion.getCategoria().getIdCategoriaGestion());
		 * 
		 * if (!this.gestion.getFolio().trim().isEmpty()) {
		 * prep.setInt(indiceComplemento, Integer.parseInt(this.gestion.getFolio()));
		 * indiceComplemento++; }
		 * 
		 * if (!this.gestion.getIdTareaWunderlist().trim().isEmpty()) {
		 * prep.setLong(indiceComplemento,
		 * Long.parseLong(this.gestion.getIdTareaWunderlist())); indiceComplemento++; }
		 * 
		 * prep.executeUpdate();
		 * 
		 * if (!this.gestion.getFolio().trim().isEmpty()) {
		 * this.gestion.setIdGestion(Integer.parseInt(this.gestion.getFolio()));
		 * 
		 * } else { rBD = prep.getGeneratedKeys();
		 * 
		 * if (rBD.next()) { this.gestion.setIdGestion(rBD.getInt(1)); }
		 * 
		 * rBD.close();
		 * 
		 * }
		 * 
		 * prep.close();
		 * 
		 * // Se insertan los costos de la gestión prep = conexion.prepareStatement(
		 * "INSERT INTO sge.costo(idGestion,CostoOriginal,TotalAPagar,idTipoDescuento)"
		 * + "VALUES(?,?,?,?)");
		 * 
		 * prep.setInt(1, this.gestion.getIdGestion()); prep.setBigDecimal(2,
		 * this.gestion.getCosto().getCostoOriginal()); prep.setBigDecimal(3,
		 * this.gestion.getCosto().getTotalAPagar());
		 * 
		 * if (this.gestion.getCosto().getTipoDescuento() != null) { prep.setInt(4,
		 * this.gestion.getCosto().getTipoDescuento().getIdTipoDescuento()); } else {
		 * prep.setNull(4, Types.DOUBLE); }
		 * 
		 * prep.executeUpdate();
		 * 
		 * } else {
		 * 
		 * prep = conexion.prepareStatement(
		 * "UPDATE gestion SET descripcion=?, Solicitud=?, DetallesGenerales=?, idUsuario=?, SolicitadoA=?, idCategoriaGestion=? "
		 * + " WHERE idGestion=?");
		 * 
		 * prep.setString(1, this.gestion.getDescripcion()); prep.setString(2,
		 * this.gestion.getSolicitud()); prep.setString(3,
		 * this.gestion.getDetallesGenerales()); prep.setInt(4,
		 * Integer.parseInt(sesion.getIdUsuario())); prep.setString(5,
		 * this.gestion.getSolicitadoA()); prep.setInt(6,
		 * this.gestion.getCategoria().getIdCategoriaGestion()); prep.setInt(7,
		 * this.gestion.getIdGestion());
		 * 
		 * prep.executeUpdate();
		 * 
		 * prep.close();
		 * 
		 * // Se actualizan los costos de la gestión prep = conexion.prepareStatement(
		 * "UPDATE sge.costo SET CostoOriginal=?,TotalAPagar=?,idTipoDescuento=? WHERE idGestion=? "
		 * );
		 * 
		 * prep.setInt(4, this.gestion.getIdGestion()); prep.setBigDecimal(1,
		 * this.gestion.getCosto().getCostoOriginal()); prep.setBigDecimal(2,
		 * this.gestion.getCosto().getTotalAPagar());
		 * 
		 * if (this.gestion.getCosto().getTipoDescuento() != null) { prep.setInt(3,
		 * this.gestion.getCosto().getTipoDescuento().getIdTipoDescuento()); } else {
		 * prep.setNull(3, Types.DOUBLE); }
		 * 
		 * prep.executeUpdate();
		 * 
		 * }
		 * 
		 * // Se busca el lugar de origen en el catálogo, si existe, se utiliza el id,
		 * en // caso contrario se registra y se utiliza el nuevo Id
		 * 
		 * prep = conexion.
		 * prepareStatement("SELECT * FROM lugarresidencia WHERE descripcion = ? ");
		 * 
		 * prep.setString(1,
		 * this.gestion.getPaciente().getLugarResidencia().getDescripcion());
		 * 
		 * rBD = prep.executeQuery();
		 * 
		 * int idLugarResidenciaGenerado = 1;
		 * 
		 * if (rBD.next()) { idLugarResidenciaGenerado =
		 * rBD.getInt("idLugarResidencia"); } else { // Se inserta y se obtiene el
		 * idGenerado
		 * 
		 * prep.close();
		 * 
		 * prep = conexion.prepareStatement(
		 * "INSERT INTO lugarresidencia (Descripcion,cve_agee,Estado,cve_agem,Municipio,cve_loc,Localidad) VALUES (?, ?, ?, ?, ?, ?, ?)"
		 * , PreparedStatement.RETURN_GENERATED_KEYS);
		 * 
		 * prep.setString(1,
		 * this.gestion.getPaciente().getLugarResidencia().getDescripcion().trim());
		 * 
		 * if (lResidencia.getEstadoINEGI() != null) { prep.setString(2,
		 * lResidencia.getEstadoINEGI().getCve_agee()); prep.setString(3,
		 * lResidencia.getEstadoINEGI().getNom_agee()); } else { prep.setNull(2,
		 * Types.VARCHAR); prep.setNull(3, Types.VARCHAR); }
		 * 
		 * if (lResidencia.getMunicipioINEGI() != null) { prep.setString(4,
		 * lResidencia.getMunicipioINEGI().getCve_agem()); prep.setString(5,
		 * lResidencia.getMunicipioINEGI().getNom_agem()); } else { prep.setNull(4,
		 * Types.VARCHAR); prep.setNull(5, Types.VARCHAR); }
		 * 
		 * if (lResidencia.getLocalidadINEGI() != null) { prep.setString(6,
		 * lResidencia.getLocalidadINEGI().getCve_loc()); prep.setString(7,
		 * lResidencia.getLocalidadINEGI().getNom_loc()); } else { prep.setNull(6,
		 * Types.VARCHAR); prep.setNull(7, Types.VARCHAR); }
		 * 
		 * prep.executeUpdate();
		 * 
		 * rBD = prep.getGeneratedKeys();
		 * 
		 * if (rBD.next()) { idLugarResidenciaGenerado = rBD.getInt(1); }
		 * 
		 * }
		 * 
		 * prep.close();
		 * 
		 * if (nuevaGestion) { // Se inserta al paciente en la bd prep =
		 * conexion.prepareStatement(
		 * " INSERT INTO paciente (idGestion,Nombre,Sexo,Edad,CURP,idLugarResidencia,Diagnostico,HospitalizadoEn,"
		 * + "idSeguridadSocial,Afiliacion,AtendidoEn,ReferenciadoA)\n" +
		 * "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?) ",
		 * PreparedStatement.RETURN_GENERATED_KEYS);
		 * 
		 * prep.setInt(1, this.gestion.getIdGestion()); prep.setString(2,
		 * this.gestion.getPaciente().getNombre()); prep.setString(3,
		 * this.gestion.getPaciente().getSexo()); prep.setInt(4,
		 * this.gestion.getPaciente().getEdad()); prep.setString(5, ""); prep.setInt(6,
		 * idLugarResidenciaGenerado); prep.setString(7,
		 * this.gestion.getPaciente().getDiagnostico()); prep.setString(8, "");
		 * prep.setInt(9,
		 * this.gestion.getPaciente().getSeguridadSocial().getIdSeguridadSocial());
		 * prep.setString(10, this.gestion.getPaciente().getAfiliacion());
		 * 
		 * if (this.gestion.getPaciente().getAtendidoEn() != null) { prep.setInt(11,
		 * this.gestion.getPaciente().getAtendidoEn().getIdUnidadSalud()); } else {
		 * prep.setNull(11, Types.INTEGER); }
		 * 
		 * if (this.gestion.getPaciente().getReferenciadoA() != null) { prep.setInt(12,
		 * this.gestion.getPaciente().getReferenciadoA().getIdUnidadSalud());
		 * 
		 * } else { prep.setNull(12, Types.INTEGER); }
		 * 
		 * prep.executeUpdate();
		 * 
		 * rBD = prep.getGeneratedKeys();
		 * 
		 * if (rBD.next()) { this.gestion.getPaciente().setIdPaciente(rBD.getInt(1)); }
		 * 
		 * prep.close();
		 * 
		 * } else {
		 * 
		 * prep = conexion.prepareStatement(
		 * "UPDATE paciente SET Nombre=?, Sexo=?, Edad=?, idLugarResidencia=?, Diagnostico=?,"
		 * +
		 * "idSeguridadSocial=?, Afiliacion=?, AtendidoEn=?, ReferenciadoA=? WHERE idPaciente=?"
		 * );
		 * 
		 * prep.setString(1, this.gestion.getPaciente().getNombre()); prep.setString(2,
		 * this.gestion.getPaciente().getSexo()); prep.setInt(3,
		 * this.gestion.getPaciente().getEdad()); prep.setInt(4,
		 * idLugarResidenciaGenerado); prep.setString(5,
		 * this.gestion.getPaciente().getDiagnostico()); prep.setInt(6,
		 * this.gestion.getPaciente().getSeguridadSocial().getIdSeguridadSocial());
		 * prep.setString(7, this.gestion.getPaciente().getAfiliacion());
		 * 
		 * if (this.gestion.getPaciente().getAtendidoEn() != null) { prep.setInt(8,
		 * this.gestion.getPaciente().getAtendidoEn().getIdUnidadSalud()); } else {
		 * prep.setNull(8, Types.INTEGER); }
		 * 
		 * if (this.gestion.getPaciente().getReferenciadoA() != null) { prep.setInt(9,
		 * this.gestion.getPaciente().getReferenciadoA().getIdUnidadSalud());
		 * 
		 * } else { prep.setNull(9, Types.INTEGER); }
		 * 
		 * prep.setInt(10, this.gestion.getPaciente().getIdPaciente());
		 * 
		 * prep.executeUpdate(); prep.close();
		 * 
		 * // Se borran a los contactos para antes de volver a insertar los de la
		 * edición
		 * 
		 * prep =
		 * conexion.prepareStatement("DELETE FROM contactogestion WHERE idGestion=?");
		 * prep.setInt(1, this.gestion.getIdGestion());
		 * 
		 * prep.executeUpdate(); prep.close();
		 * 
		 * }
		 * 
		 * // Ahora se insertan los contactos de la gestión for (Contacto contacto :
		 * this.gestion.getContactos()) {
		 * 
		 * prep = conexion.prepareStatement(
		 * "INSERT INTO sge.contactogestion (idGestion,Nombre,Telefonos,Email,Observaciones)\n"
		 * + "VALUES (?, ?, ?, ?, ?) ", PreparedStatement.RETURN_GENERATED_KEYS);
		 * 
		 * prep.setInt(1, this.gestion.getIdGestion()); prep.setString(2,
		 * contacto.getNombres()); prep.setString(3, contacto.getTelefonos());
		 * prep.setString(4, contacto.getEmail()); prep.setString(5,
		 * contacto.getObservaciones());
		 * 
		 * prep.executeUpdate();
		 * 
		 * rBD = prep.getGeneratedKeys();
		 * 
		 * if (rBD.next()) { contacto.setIdContacto(rBD.getInt(1)); }
		 * 
		 * prep.close();
		 * 
		 * }
		 * 
		 * // Se crea la tarea que se va a registrar en el WebService Task nuevaTarea =
		 * new Task();
		 * 
		 * nuevaTarea = new Task();
		 * nuevaTarea.setList_id(UtilidadesGestion.idListaGestiones);
		 * 
		 * LocalDate ldt = LocalDate.now();
		 * 
		 * String folio = "Folio: F-" + ldt.getYear() + "-" +
		 * this.gestion.getIdGestion() + ". - Paciente: " +
		 * this.gestion.getPaciente().getNombre();
		 * 
		 * nuevaTarea.setTitle(folio);
		 * nuevaTarea.setCompleted(this.gestion.getStatus().getIdStatusActividad() == 1
		 * ? true : false); nuevaTarea.setStarred(false);
		 * 
		 * if (nuevaGestion && this.gestion.getFolio().trim().isEmpty()) { // Devuelve
		 * la tarea creada en el atributo Tarea del objeto
		 * wUsuario.postTareaWunderlist(nuevaTarea); } else { // Se obtiene la tarea a
		 * ser editada con el id en el objeto gestión
		 * wUsuario.getTareaWunderlist(this.gestion.getIdTareaWunderlist()); }
		 * 
		 * // Se actualiza en la base de datos el id de la tarea de wunderlist y se
		 * añade a // su registro // Se inserta al paciente en la bd prep =
		 * conexion.prepareStatement(
		 * " UPDATE gestion SET descripcion=?, idTareaWunderlist=?, idListaWunderlist=? WHERE idGestion=? "
		 * );
		 * 
		 * prep.setString(1, folio); prep.setString(2, "" +
		 * wUsuario.getTarea().getId()); prep.setString(3, "" +
		 * nuevaTarea.getList_id()); prep.setInt(4, this.gestion.getIdGestion());
		 * 
		 * prep.executeUpdate();
		 * 
		 * conexion.commit();
		 * 
		 * String contenidoNota = "Fecha de Recepción: " + new
		 * SimpleDateFormat("yyyy-MM-dd - HH:mm:dd").format(this.gestion.
		 * getFechaRecepcion()) + "\n"; contenidoNota += "USUARIO: " +
		 * sesion.getIdUsuario() + " - " + sesion.getNombreUsuario() + "\n\n";
		 * contenidoNota += "SOLICITADO POR: " + this.gestion.getSolicitadoA() + "\n\n";
		 * contenidoNota += "CATEGORÍA DE LA GESTIÓN: " +
		 * this.gestion.getCategoria().getDescripcion() + "\n\n"; contenidoNota +=
		 * "PACIENTE: \n"; contenidoNota += "NOMBRE: " +
		 * this.gestion.getPaciente().getNombre() + "\n"; contenidoNota += "EDAD: " +
		 * this.gestion.getPaciente().getEdad() + "\n"; contenidoNota += "SEXO: " +
		 * (this.gestion.getPaciente().getSexo().equals("m") ? "Masculino" : "Femenino")
		 * + "\n"; contenidoNota += "LUGAR DE ORIGEN: " +
		 * this.gestion.getPaciente().getLugarResidencia().getDescripcion() + "\n\n";
		 * contenidoNota += "SEGURIDAD SOCIAL: " +
		 * this.gestion.getPaciente().getSeguridadSocial().getDescripcion() + "\n";
		 * contenidoNota += "NÚMERO O FOLIO DE AFILIACIÓN: " +
		 * this.gestion.getPaciente().getAfiliacion() + "\n"; contenidoNota +=
		 * "ATENDIDO EN: "; contenidoNota += this.gestion.getPaciente().getAtendidoEn()
		 * != null ? this.gestion.getPaciente().getAtendidoEn().getDescripcion() :
		 * "Sin información"; contenidoNota += "\n\n"; contenidoNota +=
		 * "REFERENCIADO A: "; contenidoNota +=
		 * this.gestion.getPaciente().getReferenciadoA() != null ?
		 * this.gestion.getPaciente().getReferenciadoA().getDescripcion() :
		 * "Sin información"; contenidoNota += "\n\n";
		 * 
		 * contenidoNota += "DIAGNÓSTICO: " +
		 * this.gestion.getPaciente().getDiagnostico() + "\n\n"; contenidoNota +=
		 * "OBSERVACIONES DEL CASO: " + this.gestion.getDetallesGenerales() + "\n\n";
		 * contenidoNota += "SOLICITUD: " + this.gestion.getSolicitud() + "\n\n";
		 * contenidoNota += "CONTACTOS: \n";
		 * 
		 * for (Contacto contacto : this.gestion.getContactos()) { contenidoNota +=
		 * " - " + contacto.getNombres() + " " + (contacto.getTelefonos().isEmpty() ? ""
		 * : ", Teléfonos: " + contacto.getTelefonos()) + " " +
		 * (contacto.getEmail().isEmpty() ? "" : ", Email: " + contacto.getEmail()) +
		 * " " + (contacto.getObservaciones().isEmpty() ? "" : ", Observación: " +
		 * contacto.getObservaciones()) + "\n";
		 * 
		 * }
		 * 
		 * if (nuevaGestion && this.gestion.getFolio().trim().isEmpty()) { Note nota =
		 * new Note(); nota.setTask_id(wUsuario.getTarea().getId());
		 * nota.setContent(contenidoNota);
		 * 
		 * wUsuario.postNotaTareaWunderlist(nota); iniciaNuevaGestion();
		 * 
		 * FacesContext.getCurrentInstance().addMessage(null, new
		 * FacesMessage(FacesMessage.SEVERITY_INFO, "Solicitud Enviada, Folio: " +
		 * folio, "La solicitud ha sido registrada exitosamente en el sistema."));
		 * 
		 * System.out.println("Solicitud enviada");
		 * 
		 * } else {
		 * 
		 * // Se añade un comentario a la tarea indicando que la tarea o gestión fue //
		 * editada wUsuario.getTareaWunderlist(this.gestion.getIdTareaWunderlist());
		 * 
		 * // se pachea la nota wUsuario.getNotaTareaWunderlist(wUsuario.getTarea());
		 * 
		 * // Se pachea la nota wUsuario.getNotas().get(0).setContent(contenidoNota);
		 * 
		 * wUsuario.patchNotaWunderlist(wUsuario.getNotas().get(0));
		 * 
		 * TaskComment taskComment = new TaskComment();
		 * taskComment.setTask_id(wUsuario.getTarea().getId());
		 * taskComment.setList_id(wUsuario.getTarea().getList_id());
		 * taskComment.setText("Datos Editados");
		 * 
		 * // wUsuario.postComentarioTareaWunderlist(taskComment);
		 * 
		 * // Se actualiza el título de la tarea
		 * wUsuario.getTareaWunderlist(this.gestion.getIdTareaWunderlist());
		 * wUsuario.getTarea().setTitle(folio);
		 * wUsuario.patchTareaWunderlist(wUsuario.getTarea());
		 * 
		 * FacesContext.getCurrentInstance().addMessage(null, new
		 * FacesMessage(FacesMessage.SEVERITY_INFO, "Solicitud Editada, Folio: " +
		 * folio, "La solicitud ha sido editada exitosamente en el sistema."));
		 * 
		 * System.out.println("Solicitud editada");
		 * 
		 * }
		 * 
		 * } catch (Exception e) { e.printStackTrace(); conexion.rollback(); }
		 * 
		 * } catch (
		 * 
		 * Exception e) { FacesContext.getCurrentInstance().addMessage(null, new
		 * FacesMessage(FacesMessage.SEVERITY_INFO, "Excepción",
		 * "Ha ocurrido una excepción al registrar el folio de la gestión, favor de contactar con el desarrollador del sistema."
		 * ));
		 * 
		 * e.printStackTrace(); } finally { if (prep != null) { try { prep.close(); }
		 * catch (SQLException e) { // TODO Auto-generated catch block
		 * e.printStackTrace(); } } }
		 */
		/*
		 * wUsuario.getListsWunderlist();
		 * wUsuario.getListWunderlist(UtilidadesGestion.idListaGestiones);
		 * 
		 * this.listasUsuario = new ArrayList<>();
		 * this.listasUsuario.add(wUsuario.getLista()); this.webservice =
		 * wUsuario.getAccess_token().getAccess_token();
		 * 
		 * // Se crea la tarea que se va a registrar en el WebService Task nuevaTarea =
		 * new Task();
		 * 
		 * nuevaTarea = new Task();
		 * nuevaTarea.setList_id(UtilidadesGestion.idListaGestiones);
		 * 
		 * // Crear una tarea Task nuevaTarea = new Task();
		 * nuevaTarea.setList_id(wUsuario.getLista().getId()); nuevaTarea.setTitle(
		 * "Ignorar: Pruebas SGE: Folio-" + this.gestion.getFechaRecepcion() +
		 * "-Nombre/Descripción Asunto"); nuevaTarea.setCompleted(false);
		 * nuevaTarea.setStarred(true);
		 * 
		 * wUsuario.postTareaWunderlist(nuevaTarea); this.webservice =
		 * wUsuario.getTarea().getTitle();
		 * 
		 * wUsuario.getTarea().setTitle("IGNORAR:" + wUsuario.getTarea().getId() +
		 * " MODIFICADO por SGE");
		 * 
		 * wUsuario.patchTareaWunderlist(wUsuario.getTarea()); this.webservice =
		 * wUsuario.getTarea().getTitle();
		 * 
		 * wUsuario.getNotaTareaWunderlist(wUsuario.getTarea());
		 * 
		 * Note nota = new Note(); nota.setTask_id(wUsuario.getTarea().getId()); nota.
		 * setContent("Prueba de la creación de las notas desde la plataforma del SGE modificado desde el código"
		 * );
		 * 
		 * wUsuario.postNotaTareaWunderlist(nota);
		 * 
		 * System.out.println("Contenido de nota: " + wUsuario.getNota().getContent());
		 */
	}

	public void desactivarModoEdicion()
	{
		this.editarGestion = -1;
	}

}
