package modelo.gestion;

import java.util.List;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.Invocation;
import javax.ws.rs.core.GenericType;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.glassfish.jersey.client.HttpUrlConnectorProvider;

import util.gestion.UtilidadesGestion;

public class WUsuario
{
	private Client				client;
	private Token				access_token;
	private Invocation			invocation;
	private String				urlAvatar;

	//Atributos que nos servirán para las respuestas de los métodos
	private List<ListElement>	listas;
	private ListElement			lista;
	private List<Task>			tareas;
	private Task				tarea;
	private List<Note>			notas;
	private Note				nota;

	public WUsuario()
	{
		super();
		// TODO Auto-generated constructor stub
	}

	public void initClient()
	{
		this.client = ClientBuilder.newClient();
	}

	public void initToken()
	{
		//Se obtiene el access_token para las peticiones en Wunderlist
		this.access_token = new Token();
		this.access_token.setClient_id("2f9718b85bd8ee41d5f7");
		this.access_token.setClient_secret("054e16a4b73698c0333f3fd8ac2f20a7406919a10a2d82b6a3426556197a");
		this.access_token.setCode("c9ee49a30ce376f21f20");

		this.invocation = this.client.target(UtilidadesGestion.urlAccessToken).request()
				.buildPost(Entity.entity(this.access_token, MediaType.APPLICATION_JSON));

		Response response = invocation.invoke();

		this.access_token = response.readEntity(Token.class);

		//Se vuelven a establecer los parámetros del token para tener a la mano los datos del cliente
		this.access_token.setClient_id("2f9718b85bd8ee41d5f7");
		this.access_token.setClient_secret("054e16a4b73698c0333f3fd8ac2f20a7406919a10a2d82b6a3426556197a");
		this.access_token.setCode("c9ee49a30ce376f21f20");

	}

	//INICIO MÉTODOS PARA LISTAS

	public void getListsWunderlist()
	{
		Invocation invocationLists = this.client.target(UtilidadesGestion.urlLists)
				.queryParam("client_id", this.access_token.getClient_id())
				.queryParam("access_token", this.access_token.getAccess_token()).request(MediaType.APPLICATION_JSON)
				.header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_TYPE.withCharset("utf-8")).buildGet();

		Response response = invocationLists.invoke();

		this.listas = response.readEntity(new GenericType<List<ListElement>>()
		{
		});
	}

	public void getListWunderlist(int idLista)
	{
		Invocation invocationLists = this.client.target(UtilidadesGestion.urlLists + "/" + idLista)
				.queryParam("client_id", this.access_token.getClient_id())
				.queryParam("access_token", this.access_token.getAccess_token()).request(MediaType.APPLICATION_JSON)
				.header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_TYPE.withCharset("utf-8")).buildGet();

		Response response = invocationLists.invoke();

		this.lista = response.readEntity(new GenericType<ListElement>()
		{
		});
	}

	//FIN MÉTODOS PARA LISTAS

	//INICIO MÉTODOS PARA TAREAS

	public void getTareasWunderlist()
	{

		Invocation invocationTasks = this.client.target("https://a.wunderlist.com/api/v1/tasks")
				.queryParam("list_id", 338456892).queryParam("client_id", this.access_token.getClient_id())
				.queryParam("access_token", this.access_token.getAccess_token()).request(MediaType.APPLICATION_JSON)
				.header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_TYPE.withCharset("utf-8")).buildGet();

		Response response = invocationTasks.invoke();

		this.tareas = response.readEntity(new GenericType<List<Task>>()
		{
		});

	}

	public void getTareaWunderlist(String idTarea, String revision)
	{

		Invocation invocationTask = client.target("https://a.wunderlist.com/api/v1/tasks/" + idTarea)
				.queryParam("revision", revision).queryParam("client_id", this.access_token.getClient_id())
				.queryParam("access_token", this.access_token.getAccess_token()).request(MediaType.APPLICATION_JSON)
				.header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_TYPE.withCharset("utf-8")).buildGet();

		Response response = invocationTask.invoke();

		this.tarea = response.readEntity(new GenericType<Task>()
		{
		});

	}

	public void postTareaWunderlist(Task task)
	{
		Invocation invocationCrearTarea = client.target("https://a.wunderlist.com/api/v1/tasks")
				.queryParam("client_id", this.access_token.getClient_id())
				.queryParam("access_token", this.access_token.getAccess_token()).request()
				.buildPost(Entity.entity(task, MediaType.APPLICATION_JSON));

		Response responseTask = invocationCrearTarea.invoke();

		this.tarea = responseTask.readEntity(Task.class);

	}

	public void patchTareaWunderlist(Task task)
	{
		Invocation invocation = this.client.target("https://a.wunderlist.com/api/v1/tasks/" + task.getId())
				.queryParam("revision", task.getRevision()).queryParam("client_id", this.access_token.getClient_id())
				.queryParam("access_token", this.access_token.getAccess_token()).request()
				.header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_TYPE.withCharset("utf-8"))
				.build("PATCH", Entity.entity(task, MediaType.APPLICATION_JSON))
				.property(HttpUrlConnectorProvider.SET_METHOD_WORKAROUND, true);

		Response response = invocation.invoke();

		this.tarea = response.readEntity(new GenericType<Task>()
		{
		});

	}

	//FIN DE MÉTODOS PARA TAREAS

	//MÉTODOS PARA NOTAS

	public void getNotaTareaWunderlist(Task task)
	{

		Invocation invocationTask = this.client.target(UtilidadesGestion.urlNotes).queryParam("task_id", task.getId())
				.queryParam("client_id", this.access_token.getClient_id())
				.queryParam("access_token", this.access_token.getAccess_token()).request(MediaType.APPLICATION_JSON)
				.header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_TYPE.withCharset("utf-8")).buildGet();

		Response response = invocationTask.invoke();

		this.notas = response.readEntity(new GenericType<List<Note>>()
		{
		});

	}

	public void postNotaTareaWunderlist(Note nota)
	{
		Invocation invocation = this.client.target(UtilidadesGestion.urlNotes)
				.queryParam("client_id", this.access_token.getClient_id())
				.queryParam("access_token", this.access_token.getAccess_token()).request()
				.buildPost(Entity.entity(nota, MediaType.APPLICATION_JSON));

		Response response = invocation.invoke();

		this.nota = response.readEntity(Note.class);

	}

	public void patchNotaWunderlist(Task task)
	{
		Invocation invocation = this.client.target("https://a.wunderlist.com/api/v1/tasks/" + task.getId())
				.queryParam("revision", task.getRevision()).queryParam("client_id", this.access_token.getClient_id())
				.queryParam("access_token", this.access_token.getAccess_token()).request()
				.header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_TYPE.withCharset("utf-8"))
				.build("PATCH", Entity.entity(task, MediaType.APPLICATION_JSON))
				.property(HttpUrlConnectorProvider.SET_METHOD_WORKAROUND, true);

		Response response = invocation.invoke();

		this.tarea = response.readEntity(new GenericType<Task>()
		{
		});

	}

	//FIN DE MÉTODOS PARA NOTAS

	public Token getAccess_token()
	{
		return access_token;
	}

	public void setAccess_token(Token access_token)
	{
		this.access_token = access_token;
	}

	public Invocation getInvocation()
	{
		return invocation;
	}

	public void setInvocation(Invocation invocation)
	{
		this.invocation = invocation;
	}

	public String getUrlAvatar()
	{
		return urlAvatar;
	}

	public void setUrlAvatar(String urlAvatar)
	{
		this.urlAvatar = urlAvatar;
	}

	public List<ListElement> getListas()
	{
		return listas;
	}

	public void setListas(List<ListElement> listas)
	{
		this.listas = listas;
	}

	public Client getClient()
	{
		return client;
	}

	public void setClient(Client client)
	{
		this.client = client;
	}

	public ListElement getLista()
	{
		return lista;
	}

	public void setLista(ListElement lista)
	{
		this.lista = lista;
	}

	public List<Task> getTareas()
	{
		return tareas;
	}

	public void setTareas(List<Task> tareas)
	{
		this.tareas = tareas;
	}

	public Task getTarea()
	{
		return tarea;
	}

	public void setTarea(Task tarea)
	{
		this.tarea = tarea;
	}

	public List<Note> getNotas()
	{
		return notas;
	}

	public void setNotas(List<Note> notas)
	{
		this.notas = notas;
	}

	public Note getNota()
	{
		return nota;
	}

	public void setNota(Note nota)
	{
		this.nota = nota;
	}

}
