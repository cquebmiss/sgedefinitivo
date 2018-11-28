package gui.persistence.wunderlist;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;

import modelo.gestion.WUsuario;

@ManagedBean
@SessionScoped
public class WunderlistWSBean
{
	private WUsuario wUsuario;

	public WunderlistWSBean()
	{
		this.wUsuario = new WUsuario();
	}

	@PostConstruct
	public void postConstruct()
	{
		//Crea el código de acceso para utilizar la API de Wunderlist
		this.wUsuario.initClient();
		this.wUsuario.initToken();
	}

	public WUsuario getwUsuario()
	{
		return wUsuario;
	}

	public void setwUsuario(WUsuario wUsuario)
	{
		this.wUsuario = wUsuario;
	}

}
