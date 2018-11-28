package modelo.gestion;

public class ClientResponse
{
	private String	method;
	private String	uri;
	private String	status;
	private String	reason;

	public ClientResponse()
	{
		super();
		// TODO Auto-generated constructor stub
	}

	public ClientResponse(String method, String uri, String status, String reason)
	{
		super();
		this.method = method;
		this.uri = uri;
		this.status = status;
		this.reason = reason;
	}

	public String getMethod()
	{
		return method;
	}

	public void setMethod(String method)
	{
		this.method = method;
	}

	public String getUri()
	{
		return uri;
	}

	public void setUri(String uri)
	{
		this.uri = uri;
	}

	public String getStatus()
	{
		return status;
	}

	public void setStatus(String status)
	{
		this.status = status;
	}

	public String getReason()
	{
		return reason;
	}

	public void setReason(String reason)
	{
		this.reason = reason;
	}

}
