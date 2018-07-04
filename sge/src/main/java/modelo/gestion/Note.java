package modelo.gestion;

public class Note
{

	private long	id;
	private long	task_id;
	private String	content;
	private String	created_at;
	private String	updated_at;
	private int		revision;
	private String	type;

	//Respuesta
	private String	error;
	private String	created_by_request_id;

	public Note()
	{
		super();
		// TODO Auto-generated constructor stub
	}

	public Note(long id, long task_id, String content, String created_at, String updated_at, int revision, String type,
			String error, String created_by_request_id)
	{
		super();
		this.id = id;
		this.task_id = task_id;
		this.content = content;
		this.created_at = created_at;
		this.updated_at = updated_at;
		this.revision = revision;
		this.type = type;
		this.error = error;
		this.created_by_request_id = created_by_request_id;
	}

	public long getId()
	{
		return id;
	}

	public void setId(long id)
	{
		this.id = id;
	}

	public long getTask_id()
	{
		return task_id;
	}

	public void setTask_id(long task_id)
	{
		this.task_id = task_id;
	}

	public String getContent()
	{
		return content;
	}

	public void setContent(String content)
	{
		this.content = content;
	}

	public String getCreated_at()
	{
		return created_at;
	}

	public void setCreated_at(String created_at)
	{
		this.created_at = created_at;
	}

	public String getUpdated_at()
	{
		return updated_at;
	}

	public void setUpdated_at(String updated_at)
	{
		this.updated_at = updated_at;
	}

	public int getRevision()
	{
		return revision;
	}

	public void setRevision(int revision)
	{
		this.revision = revision;
	}

	public String getType()
	{
		return type;
	}

	public void setType(String type)
	{
		this.type = type;
	}

	public String getError()
	{
		return error;
	}

	public void setError(String error)
	{
		this.error = error;
	}

	public String getCreated_by_request_id()
	{
		return created_by_request_id;
	}

	public void setCreated_by_request_id(String created_by_request_id)
	{
		this.created_by_request_id = created_by_request_id;
	}

}
