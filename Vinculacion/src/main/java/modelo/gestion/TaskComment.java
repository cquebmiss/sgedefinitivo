package modelo.gestion;

import java.text.ParseException;

import util.UtilidadesCalendario;

public class TaskComment
{
	private long	id;
	private long	revision;
	private long	task_id;
	private long	list_id;
	private String	text;
	private String	type;
	private String	created_at;
	private String	createdAt;
	private Author	author;

	//respuesta
	private String	local_created_at;
	private String	localCreatedAt;
	private String	created_by_request_id;

	public TaskComment()
	{
		super();
		this.text = "";
		// TODO Auto-generated constructor stub
	}

	public TaskComment(long id, long revision, long task_id, long list_id, String text, String type, String created_at,
			String createdAt, Author author, String local_created_at, String created_by_request_id,
			String localCreatedAt)
	{
		super();
		this.id = id;
		this.revision = revision;
		this.task_id = task_id;
		this.list_id = list_id;
		this.text = text;
		this.type = type;
		this.created_at = created_at;
		this.createdAt = createdAt;
		this.author = author;
		this.local_created_at = local_created_at;
		this.created_by_request_id = created_by_request_id;
		this.localCreatedAt = localCreatedAt;
	}

	public long getId()
	{
		return id;
	}

	public void setId(long id)
	{
		this.id = id;
	}

	public long getRevision()
	{
		return revision;
	}

	public void setRevision(long revision)
	{
		this.revision = revision;
	}

	public long getTask_id()
	{
		return task_id;
	}

	public void setTask_id(long task_id)
	{
		this.task_id = task_id;
	}

	public long getList_id()
	{
		return list_id;
	}

	public void setList_id(long list_id)
	{
		this.list_id = list_id;
	}

	public String getText()
	{
		return text;
	}

	public void setText(String text)
	{
		this.text = text;
	}

	public String getType()
	{
		return type;
	}

	public void setType(String type)
	{
		this.type = type;
	}

	public String getCreated_at()
	{
		return created_at;
	}

	public void setCreated_at(String created_at)
	{
		this.created_at = created_at;

		try
		{
			setCreatedAt(UtilidadesCalendario.parseHoraZuluHoraMexico(created_at));
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}

	public Author getAuthor()
	{
		return author;
	}

	public void setAuthor(Author author)
	{
		this.author = author;
	}

	public String getLocal_created_at()
	{
		return local_created_at;
	}

	public void setLocal_created_at(String local_created_at)
	{
		this.local_created_at = local_created_at;

		try
		{
			setLocalCreatedAt(UtilidadesCalendario.parseHoraZuluHoraMexico(local_created_at));
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}

	public String getCreated_by_request_id()
	{
		return created_by_request_id;
	}

	public void setCreated_by_request_id(String created_by_request_id)
	{
		this.created_by_request_id = created_by_request_id;
	}

	public String getLocalCreatedAt()
	{
		return localCreatedAt;
	}

	public void setLocalCreatedAt(String localCreatedAt)
	{
		this.localCreatedAt = localCreatedAt;
	}

	public String getCreatedAt()
	{
		return createdAt;
	}

	public void setCreatedAt(String createdAt)
	{
		this.createdAt = createdAt;
	}

}
