package modelo.gestion;

import java.util.List;

public class Task
{
	private int					list_id;
	private String				title;
	private int					assignee_id;
	private boolean				completed;
	private String				recurrence_type;
	private int					recurrence_count;
	private String				due_date;
	private boolean				starred;

	//Respuesta de WebService
	private long				id;
	private String				created_at;
	private int					created_by_id;
	private int					revision;
	private String				error;
	private String				created_by_request_id;
	private String				type;

	private String				completed_at;
	private int					completed_by_id;

	private List<Note>			notas;
	private List<TaskComment>	comentarios;

	public Task()
	{
		super();
		// TODO Auto-generated constructor stub
	}

	public Task(int list_id, String title, int assignee_id, boolean completed, String recurrence_type,
			int recurrence_count, String due_date, boolean starred, long id, String created_at, int created_by_id,
			int revision, String error, String created_by_request_id, String type, String completed_at,
			int completed_by_id)
	{
		super();
		this.list_id = list_id;
		this.title = title;
		this.assignee_id = assignee_id;
		this.completed = completed;
		this.recurrence_type = recurrence_type;
		this.recurrence_count = recurrence_count;
		this.due_date = due_date;
		this.starred = starred;
		this.id = id;
		this.created_at = created_at;
		this.created_by_id = created_by_id;
		this.revision = revision;
		this.error = error;
		this.created_by_request_id = created_by_request_id;
		this.type = type;
		this.completed_at = completed_at;
		this.completed_by_id = completed_by_id;
	}

	public int getList_id()
	{
		return list_id;
	}

	public void setList_id(int list_id)
	{
		this.list_id = list_id;
	}

	public String getTitle()
	{
		return title;
	}

	public void setTitle(String title)
	{
		this.title = title;
	}

	public int getAssignee_id()
	{
		return assignee_id;
	}

	public void setAssignee_id(int assignee_id)
	{
		this.assignee_id = assignee_id;
	}

	public boolean isCompleted()
	{
		return completed;
	}

	public void setCompleted(boolean completed)
	{
		this.completed = completed;
	}

	public String getRecurrence_type()
	{
		return recurrence_type;
	}

	public void setRecurrence_type(String recurrence_type)
	{
		this.recurrence_type = recurrence_type;
	}

	public int getRecurrence_count()
	{
		return recurrence_count;
	}

	public void setRecurrence_count(int recurrence_count)
	{
		this.recurrence_count = recurrence_count;
	}

	public String getDue_date()
	{
		return due_date;
	}

	public void setDue_date(String due_date)
	{
		this.due_date = due_date;
	}

	public boolean isStarred()
	{
		return starred;
	}

	public void setStarred(boolean starred)
	{
		this.starred = starred;
	}

	public long getId()
	{
		return id;
	}

	public void setId(long id)
	{
		this.id = id;
	}

	public String getCreated_at()
	{
		return created_at;
	}

	public void setCreated_at(String created_at)
	{
		this.created_at = created_at;
	}

	public int getCreated_by_id()
	{
		return created_by_id;
	}

	public void setCreated_by_id(int created_by_id)
	{
		this.created_by_id = created_by_id;
	}

	public int getRevision()
	{
		return revision;
	}

	public void setRevision(int revision)
	{
		this.revision = revision;
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

	public String getType()
	{
		return type;
	}

	public void setType(String type)
	{
		this.type = type;
	}

	public String getCompleted_at()
	{
		return completed_at;
	}

	public void setCompleted_at(String completed_at)
	{
		this.completed_at = completed_at;
	}

	public int getCompleted_by_id()
	{
		return completed_by_id;
	}

	public void setCompleted_by_id(int completed_by_id)
	{
		this.completed_by_id = completed_by_id;
	}

	public List<Note> getNotas()
	{
		return notas;
	}

	public void setNotas(List<Note> notas)
	{
		this.notas = notas;
	}

	public List<TaskComment> getComentarios()
	{
		return comentarios;
	}

	public void setComentarios(List<TaskComment> comentarios)
	{
		this.comentarios = comentarios;
	}

}
