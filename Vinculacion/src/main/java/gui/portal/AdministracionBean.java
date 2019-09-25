package gui.portal;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;

import org.primefaces.model.DualListModel;

import controller.AdministracionController;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import persistence.dynamodb.Estado;
import persistence.dynamodb.LocalidadConf;
import persistence.dynamodb.Municipio;
import persistence.dynamodb.PermisoUsuario;
import persistence.dynamodb.StatusUsuario;
import persistence.dynamodb.Usuario;

@ManagedBean
@SessionScoped
@Getter
@Setter
@NoArgsConstructor
public class AdministracionBean
{
	private int							tipoPanel;

	private AdministracionController	administracionController;

	// Tabla de usuarios
	private List<Usuario>				usuariosSistema;
	private List<Usuario>				usuariosSistemaFilter;
	private Usuario						usuarioSeleccionado;

	private DualListModel<String>		localidadesDualListModel;
	private List<String>				catEstados;
	private List<String>				catLocalidades;
	private List<String>				catMunicipios;

	private List<StatusUsuario>			catStatusUsuario;
	private List<PermisoUsuario>		catPermisoUsuario;

	private String						cve_agee;
	private String						cve_agem;
	private String						cve_loc;

	@PostConstruct
	public void postConstruct()
	{
		initAdministracionController();
		setTipoPanel(0);
	}

	private void inicializaFormulario()
	{
		this.cve_agee = "";
		this.cve_agem = "";
		this.cve_loc = "";
		this.localidadesDualListModel = new DualListModel<String>(new ArrayList<>(), new ArrayList<String>());
		this.catStatusUsuario = this.administracionController.getCatStatusUsuarioAWS();
		this.catPermisoUsuario = this.administracionController.getCatPermisoUsuarioAWS();
	}

	public void initAdministracionController()
	{
		if (this.administracionController == null)
		{
			this.administracionController = new AdministracionController();
			inicializaFormulario();
		}
	}

	public void actionBuscarLocalidad()
	{
		// Auxiliar para el módulo de personas
		initAdministracionController();

		if (this.cve_agem.trim().isEmpty())
		{
			return;
		}

		if (this.cve_loc.trim().isEmpty())
		{
			this.localidadesDualListModel.setSource(
					this.administracionController.getLocalidadesSistemaAWS(this.cve_agee, this.cve_agem, this.cve_loc));
		}
		else
		{
			this.localidadesDualListModel.setSource(
					this.administracionController.getLocalidadesSistemaAWS(this.cve_agee, this.cve_agem, this.cve_loc));
		}

	}

	public void actionSeleccionUsuario(Usuario usuario)
	{
		setUsuarioSeleccionado(usuario);
		inicializaFormulario();
		setTipoPanel(2);

		this.localidadesDualListModel = new DualListModel<String>(new ArrayList<>(), usuario.getLocalidadesString());

	}

	public void actionNuevoUsuario()
	{
		this.usuarioSeleccionado = new Usuario();
		inicializaFormulario();
		setTipoPanel(2);
	}

	public void actionGetUsuariosSistema()
	{
		setUsuariosSistema(this.administracionController.getUsuariosSistemaAWS());
		setTipoPanel(1);
	}

	public void actionGuardarUsuario()
	{
		List<Estado> catEstados = this.administracionController.getCatEstadosAWS();
		List<Municipio> catMunicipios = new ArrayList<>();

		// Ordenar la lista de localidades
		List<String> target = localidadesDualListModel.getTarget();
		Collections.sort(target, String.CASE_INSENSITIVE_ORDER);

		usuarioSeleccionado.setLocalidades(new ArrayList<>());

		String nombreEstado;
		String cve_loc;
		String nombreMunicipio;
		String nom_loc;
		LocalidadConf locConf;

		List<Estado> estadoAGuardar;
		List<Municipio> municipioAWS;

		for (String targetLoc : target)
		{
			String cve_agee = targetLoc.substring(0, 2);

			// Ubica al estado
			estadoAGuardar = catEstados.stream().filter(estado -> estado.getCve_agee().equalsIgnoreCase(cve_agee))
					.collect(Collectors.toList());
			nombreEstado = estadoAGuardar.get(0).getNom_agee();

			String cve_agem = targetLoc.substring(2, 5);

			// Verifica si el municipio ya lo hemos consultado en AWS
			Optional<Municipio> munEncontrado = catMunicipios.stream()
					.filter(mun -> mun.getCve_agee() == cve_agee && mun.getCve_agem() == cve_agem).findFirst();

			if (munEncontrado.isPresent())
			{
				nombreMunicipio = munEncontrado.get().getNom_agem();
			}
			else
			{
				// Se consulta mediante AWS
				municipioAWS = this.administracionController.getMunicipoAWS(cve_agee, cve_agem);

				if (municipioAWS == null || municipioAWS.isEmpty())
				{
					nombreMunicipio = "";
				}
				else
				{
					nombreMunicipio = municipioAWS.get(0).getNom_agem();
					catMunicipios.add(municipioAWS.get(0));
				}

			}

			cve_loc = targetLoc.substring(5, 9);
			nom_loc = targetLoc.substring(targetLoc.indexOf("-") + 2, targetLoc.length());

			locConf = new LocalidadConf(cve_agee.trim(), nombreEstado.trim(), cve_agem.trim(), nombreMunicipio.trim(), cve_loc.trim(), nom_loc.trim());

			usuarioSeleccionado.getLocalidades().add(locConf);
		}

		this.administracionController.saveUsuarioAWS(this.usuarioSeleccionado);

		setTipoPanel(1);

	}

}
