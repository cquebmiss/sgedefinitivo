package gui.portal.gestion;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;

import persistence.dynamodb.Contacto;
import util.FacesUtils;

@ManagedBean
@ViewScoped
public class DialogoContactoBean
{
	private Contacto contacto;

	public DialogoContactoBean()
	{
		super();
		// TODO Auto-generated constructor stub
	}

	@PostConstruct
	public void postConstruct()
	{
	}

	public void addNuevoContacto()
	{
	
		NuevaGestionBean nuevaGestionBean = (NuevaGestionBean) FacesUtils.getManagedBean("nuevaGestionBean");

		//Se añade un nuevo contacto a la lista de contactos de la gestión y se establece en el bean para poder controlarlo en el diálogo
		this.contacto = new Contacto(-1, "", "", "", "");
		nuevaGestionBean.getGestion().getContactos().add(this.contacto);
	}

	public Contacto getContacto()
	{
		return contacto;
	}

	public void setContacto(Contacto contacto)
	{
		this.contacto = contacto;
	}

}
