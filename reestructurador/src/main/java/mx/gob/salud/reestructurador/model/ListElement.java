package mx.gob.salud.reestructurador.model;

import java.util.List;

public class ListElement
{
	private int			id;
	private String		title;
	private String		owner_type;
	private int			owner_id;
	private String		list_type;
	private boolean		Public;
	private int			revision;
	private String		created_at;
	private String		created_by_request_id;
	private String		type;

	private List<Task>	tareas;

	public ListElement()
	{
		super();
		// TODO Auto-generated constructor stub
	}

	public ListElement(int id, String title, String owner_type, int owner_id, String list_type, boolean public1,
			int revision, String created_at, String created_by_request_id, String type)
	{
		super();
		this.id = id;
		this.title = title;
		this.owner_type = owner_type;
		this.owner_id = owner_id;
		this.list_type = list_type;
		Public = public1;
		this.revision = revision;
		this.created_at = created_at;
		this.created_by_request_id = created_by_request_id;
		this.type = type;
	}

	public int getId()
	{
		return id;
	}

	public void setId(int id)
	{
		this.id = id;
	}

	public String getTitle()
	{
		return title;
	}

	public void setTitle(String title)
	{
		this.title = title;
	}

	public String getOwner_type()
	{
		return owner_type;
	}

	public void setOwner_type(String owner_type)
	{
		this.owner_type = owner_type;
	}

	public int getOwner_id()
	{
		return owner_id;
	}

	public void setOwner_id(int owner_id)
	{
		this.owner_id = owner_id;
	}

	public String getList_type()
	{
		return list_type;
	}

	public void setList_type(String list_type)
	{
		this.list_type = list_type;
	}

	public boolean isPublic()
	{
		return Public;
	}

	public void setPublic(boolean public1)
	{
		Public = public1;
	}

	public int getRevision()
	{
		return revision;
	}

	public void setRevision(int revision)
	{
		this.revision = revision;
	}

	public String getCreated_at()
	{
		return created_at;
	}

	public void setCreated_at(String created_at)
	{
		this.created_at = created_at;
	}

	public String getType()
	{
		return type;
	}

	public void setType(String type)
	{
		this.type = type;
	}

	public String getCreated_by_request_id()
	{
		return created_by_request_id;
	}

	public void setCreated_by_request_id(String created_by_request_id)
	{
		this.created_by_request_id = created_by_request_id;
	}

	public List<Task> getTareas()
	{
		return tareas;
	}

	public void setTareas(List<Task> tareas)
	{
		this.tareas = tareas;
	}

}
