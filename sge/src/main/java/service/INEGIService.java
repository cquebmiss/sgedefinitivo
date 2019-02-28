package service;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Invocation;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import modelo.gestion.json.RespuestaEstadosJson;
import modelo.gestion.json.RespuestaLocalidadesJson;
import modelo.gestion.json.RespuestaMunicipiosJson;

public class INEGIService
{
	private final String	URL_ESTADOS		= "http://geoweb2.inegi.org.mx/wscatgeo/mgee/buscar/";
	private final String	URL_MUNICIPIOS	= "http://geoweb2.inegi.org.mx/wscatgeo/mgem/";
	private final String	URL_LOCALIDADES	= "http://geoweb2.inegi.org.mx/wscatgeo/localidades/";
	private Client			client;

	public INEGIService()
	{
		this.client = ClientBuilder.newClient();
	}

	public RespuestaEstadosJson getEstados(String busqueda)
	{
		return consumeServiceEstados(this.URL_ESTADOS + busqueda);
	}

	public RespuestaMunicipiosJson getMunicipios(String idEstado)
	{
		return consumeServiceMunicipios(this.URL_MUNICIPIOS + idEstado);
	}

	public RespuestaLocalidadesJson getLocalidades(String idEstado, String idMunicipio)
	{
		return consumeServiceLocalidades(this.URL_LOCALIDADES + idEstado + "/" + idMunicipio);
	}

	private RespuestaEstadosJson consumeServiceEstados(String url)
	{
		Invocation	invocationTask	= this.client.target(url).request(MediaType.APPLICATION_JSON)
				.header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_TYPE.withCharset("utf-8")).buildGet();

		Response	response		= invocationTask.invoke();

		return (RespuestaEstadosJson) response.readEntity(RespuestaEstadosJson.class);
	}

	private RespuestaMunicipiosJson consumeServiceMunicipios(String url)
	{
		Invocation	invocationTask	= this.client.target(url).request(MediaType.APPLICATION_JSON)
				.header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_TYPE.withCharset("utf-8")).buildGet();

		Response	response		= invocationTask.invoke();

		return (RespuestaMunicipiosJson) response.readEntity(RespuestaMunicipiosJson.class);
	}

	private RespuestaLocalidadesJson consumeServiceLocalidades(String url)
	{
		Invocation	invocationTask	= this.client.target(url).request(MediaType.APPLICATION_JSON)
				.header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_TYPE.withCharset("utf-8")).buildGet();

		Response	response		= invocationTask.invoke();

		return (RespuestaLocalidadesJson) response.readEntity(RespuestaLocalidadesJson.class);
	}

}
