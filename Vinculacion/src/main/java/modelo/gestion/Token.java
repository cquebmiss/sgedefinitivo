package modelo.gestion;

public class Token
{
	private String	client_id;
	private String	client_secret;
	private String	code;
	private String	access_token;

	public Token()
	{
		super();
		// TODO Auto-generated constructor stub
	}

	public Token(String client_id, String client_secret, String code, String access_token)
	{
		super();
		this.client_id = client_id;
		this.client_secret = client_secret;
		this.code = code;
		this.access_token = access_token;
	}

	public String getClient_id()
	{
		return client_id;
	}

	public void setClient_id(String client_id)
	{
		this.client_id = client_id;
	}

	public String getClient_secret()
	{
		return client_secret;
	}

	public void setClient_secret(String client_secret)
	{
		this.client_secret = client_secret;
	}

	public String getCode()
	{
		return code;
	}

	public void setCode(String code)
	{
		this.code = code;
	}

	public String getAccess_token()
	{
		return access_token;
	}

	public void setAccess_token(String access_token)
	{
		this.access_token = access_token;
	}

}
