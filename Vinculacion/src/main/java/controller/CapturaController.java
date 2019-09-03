package controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBScanExpression;
import com.amazonaws.services.dynamodbv2.model.AttributeValue;

import gui.Login;
import lombok.Getter;
import lombok.Setter;
import persistence.dynamodb.LocalidadConf;
import persistence.dynamodb.Usuario;
import resources.DataBase;
import util.FacesUtils;
import util.utilidades;

@Getter
@Setter
public class CapturaController
{
	private Usuario usuarioEnSesion;

	public CapturaController()
	{
		DataBase dataBaseBean = (DataBase) FacesUtils.getManagedBean("database");

		Login login = (Login) FacesUtils.getManagedBean("login");
		this.usuarioEnSesion = login.getUsuarioAWS();

	}

	public boolean existePersona(String nombre, String apPaterno, String apMaterno, String sexo, String cve_agee,
			String nom_agee, String cve_agem, String nom_agem, String cve_loc, String nom_loc)
	{
		List<persistence.dynamodb.Persona> persona = null;

		DynamoDBMapper mapper = new DynamoDBMapper(utilidades.getAWSDynamoDBClient());

		Map<String, AttributeValue> eav = new HashMap<String, AttributeValue>();
		eav.put(":val1", new AttributeValue().withS(nombre));
		eav.put(":val2", new AttributeValue().withS(apPaterno));
		eav.put(":val3", new AttributeValue().withS(apMaterno));
		eav.put(":val4", new AttributeValue().withS(sexo));
		eav.put(":val5", new AttributeValue().withS(cve_agee));
		eav.put(":val6", new AttributeValue().withS(cve_agem));
		eav.put(":val7", new AttributeValue().withS(cve_loc));

		DynamoDBScanExpression queryExpression = new DynamoDBScanExpression()
				.withFilterExpression("nombres = :val1 and apPaterno = :val2 and apMaterno = :val3 and sexo = :val4 "
						+ "and localidad.idEstado = :val5 and localidad.idMunicipio = :val6 and localidad.idLocalidad = :val7")
				.withProjectionExpression("idPersona").withExpressionAttributeValues(eav);

		persona = mapper.scan(persistence.dynamodb.Persona.class, queryExpression);

		return persona != null && !persona.isEmpty();
	}

	public persistence.dynamodb.Persona createNuevaPersona(String nombre, String apPaterno, String apMaterno,
			String sexo, String cve_agee, String nom_agee, String cve_agem, String nom_agem, String cve_loc,
			String nom_loc)
	{

		DynamoDBMapper mapper = new DynamoDBMapper(utilidades.getAWSDynamoDBClient());
		persistence.dynamodb.Persona persona = new persistence.dynamodb.Persona();

		persona.setNombres(nombre);
		persona.setApPaterno(apPaterno);
		persona.setApMaterno(apMaterno);
		persona.setSexo(sexo);
		persona.setVive(true);

		LocalidadConf localidad = new LocalidadConf(cve_agee, nom_agee, cve_agem, nom_agem, cve_loc, nom_loc);
		persona.setDecision(new persistence.dynamodb.Decision(-1, "No encuestado"));

		persona.setLocalidad(localidad);

		mapper.save(persona);

		return persona;

	}

	public persistence.dynamodb.Persona savePersona(persistence.dynamodb.Persona persona, Integer idDecision)
	{
		if (idDecision != null)
		{
			switch (idDecision)
			{
				case 0:
					persona.setDecision(new persistence.dynamodb.Decision(idDecision, "No acepta"));
					break;

				case 1:
					persona.setDecision(new persistence.dynamodb.Decision(idDecision, "Acepta"));
					break;

				case 2:
					persona.setDecision(new persistence.dynamodb.Decision(idDecision, "Quizá más adelante"));
					break;
			}

		}

		DynamoDBMapper mapper = new DynamoDBMapper(utilidades.getAWSDynamoDBClient());
		mapper.save(persona);

		return persona;

	}

}
