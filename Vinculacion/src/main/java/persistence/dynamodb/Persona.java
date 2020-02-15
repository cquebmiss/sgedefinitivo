package persistence.dynamodb;

import java.time.LocalDate;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBAttribute;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBAutoGeneratedKey;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBHashKey;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTable;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@DynamoDBTable(tableName = "amigos_Persona")
public class Persona
{

	private String			idPersona;

	private String			nombres;
	private String			apPaterno;
	private String			apMaterno;
	private String			sexo;

	private String			fechaNacimiento;
	private String			calle;
	private Integer			numero;
	private String			entreCalles;
	private String			coloniaBarrio;
	private String			telefonoCelular;
	private Boolean			whatsApp;
	private Boolean			sMS;
	private String			email;
	private String			facebook;
	private String			twitter;
	private String			instagram;
	private Boolean			apoyaCompartirRedesSociales;
	private String			ocupacion;
	private String			seccionElectoral;

	private Boolean			vive;
	private String			observaciones;

	private LocalidadConf	localidad;
	private Decision		decision;

	private String			idUsuarioCreacion;
	private String			nombreUsuarioCreacion;

	private String			idUsuarioCaptura;
	private String			nombreUsuarioCaptura;

	@DynamoDBHashKey(attributeName = "idPersona")
	@DynamoDBAutoGeneratedKey
	public String getIdPersona()
	{
		return idPersona;
	}

	public void setIdPersona(String idPersona)
	{
		this.idPersona = idPersona;
	}

	@DynamoDBAttribute
	public String getNombres()
	{
		return nombres;
	}

	public void setNombres(String nombres)
	{
		this.nombres = nombres;
	}

	@DynamoDBAttribute
	public String getApPaterno()
	{
		return apPaterno;
	}

	public void setApPaterno(String apPaterno)
	{
		this.apPaterno = apPaterno;
	}

	@DynamoDBAttribute
	public String getApMaterno()
	{
		return apMaterno;
	}

	public void setApMaterno(String apMaterno)
	{
		this.apMaterno = apMaterno;
	}

	@DynamoDBAttribute
	public String getSexo()
	{
		return sexo;
	}

	public void setSexo(String sexo)
	{
		this.sexo = sexo;
	}

	@DynamoDBAttribute
	public String getFechaNacimiento()
	{
		return fechaNacimiento;
	}

	public void setFechaNacimiento(String fechaNacimiento)
	{
		this.fechaNacimiento = fechaNacimiento;
	}

	@DynamoDBAttribute
	public String getCalle()
	{
		return calle;
	}

	public void setCalle(String calle)
	{
		this.calle = calle;
	}

	@DynamoDBAttribute
	public Integer getNumero()
	{
		return numero;
	}

	public void setNumero(Integer numero)
	{
		this.numero = numero;
	}

	@DynamoDBAttribute
	public String getEntreCalles()
	{
		return entreCalles;
	}

	public void setEntreCalles(String entreCalles)
	{
		this.entreCalles = entreCalles;
	}

	@DynamoDBAttribute
	public String getColoniaBarrio()
	{
		return coloniaBarrio;
	}

	public void setColoniaBarrio(String coloniaBarrio)
	{
		this.coloniaBarrio = coloniaBarrio;
	}

	@DynamoDBAttribute
	public String getTelefonoCelular()
	{
		return telefonoCelular;
	}

	public void setTelefonoCelular(String telefonoCelular)
	{
		this.telefonoCelular = telefonoCelular;
	}

	@DynamoDBAttribute
	public Boolean getWhatsApp()
	{
		return whatsApp;
	}

	public void setWhatsApp(Boolean whatsApp)
	{
		this.whatsApp = whatsApp;
	}

	@DynamoDBAttribute
	public Boolean getsMS()
	{
		return sMS;
	}

	public void setsMS(Boolean sMS)
	{
		this.sMS = sMS;
	}

	@DynamoDBAttribute
	public String getEmail()
	{
		return email;
	}

	public void setEmail(String email)
	{
		this.email = email;
	}

	@DynamoDBAttribute
	public String getFacebook()
	{
		return facebook;
	}

	public void setFacebook(String facebook)
	{
		this.facebook = facebook;
	}

	@DynamoDBAttribute
	public String getTwitter()
	{
		return twitter;
	}

	public void setTwitter(String twitter)
	{
		this.twitter = twitter;
	}

	@DynamoDBAttribute
	public String getInstagram()
	{
		return instagram;
	}

	public void setInstagram(String instagram)
	{
		this.instagram = instagram;
	}

	@DynamoDBAttribute
	public Boolean getApoyaCompartirRedesSociales()
	{
		return apoyaCompartirRedesSociales;
	}

	public void setApoyaCompartirRedesSociales(Boolean apoyaCompartirRedesSociales)
	{
		this.apoyaCompartirRedesSociales = apoyaCompartirRedesSociales;
	}

	@DynamoDBAttribute
	public String getOcupacion()
	{
		return ocupacion;
	}

	public void setOcupacion(String ocupacion)
	{
		this.ocupacion = ocupacion;
	}

	@DynamoDBAttribute
	public String getSeccionElectoral()
	{
		return seccionElectoral;
	}

	public void setSeccionElectoral(String seccionElectoral)
	{
		this.seccionElectoral = seccionElectoral;
	}

	@DynamoDBAttribute
	public Boolean getVive()
	{
		return vive;
	}

	public void setVive(Boolean vive)
	{
		this.vive = vive;
	}

	@DynamoDBAttribute
	public String getObservaciones()
	{
		return observaciones;
	}

	public void setObservaciones(String observaciones)
	{
		this.observaciones = observaciones;
	}

	@DynamoDBAttribute
	public LocalidadConf getLocalidad()
	{
		return localidad;
	}

	public void setLocalidad(LocalidadConf localidad)
	{
		this.localidad = localidad;
	}

	@DynamoDBAttribute
	public Decision getDecision()
	{
		return decision;
	}

	public void setDecision(Decision decision)
	{
		this.decision = decision;
	}

	@DynamoDBAttribute
	public String getIdUsuarioCreacion()
	{
		return idUsuarioCreacion;
	}

	public void setIdUsuarioCreacion(String idUsuarioCreacion)
	{
		this.idUsuarioCreacion = idUsuarioCreacion;
	}

	@DynamoDBAttribute
	public String getNombreUsuarioCreacion()
	{
		return nombreUsuarioCreacion;
	}

	public void setNombreUsuarioCreacion(String nombreUsuarioCreacion)
	{
		this.nombreUsuarioCreacion = nombreUsuarioCreacion;
	}

	@DynamoDBAttribute
	public String getIdUsuarioCaptura()
	{
		return idUsuarioCaptura;
	}

	public void setIdUsuarioCaptura(String idUsuarioCaptura)
	{
		this.idUsuarioCaptura = idUsuarioCaptura;
	}

	@DynamoDBAttribute
	public String getNombreUsuarioCaptura()
	{
		return nombreUsuarioCaptura;
	}

	public void setNombreUsuarioCaptura(String nombreUsuarioCaptura)
	{
		this.nombreUsuarioCaptura = nombreUsuarioCaptura;
	}

}