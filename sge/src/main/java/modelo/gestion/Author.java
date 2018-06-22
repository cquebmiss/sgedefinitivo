package modelo.gestion;

public class Author
{
	private long	id;
	private String	name;
	private String	avatar;

	public Author()
	{
		super();
		// TODO Auto-generated constructor stub
	}

	public Author(long id, String name, String avatar)
	{
		super();
		this.id = id;
		this.name = name;
		this.avatar = avatar;
	}

	public long getId()
	{
		return id;
	}

	public void setId(long id)
	{
		this.id = id;
	}

	public String getName()
	{
		return name;
	}

	public void setName(String name)
	{
		this.name = name;
	}

	public String getAvatar()
	{
		return avatar;
	}

	public void setAvatar(String avatar)
	{
		this.avatar = avatar;
	}

}
