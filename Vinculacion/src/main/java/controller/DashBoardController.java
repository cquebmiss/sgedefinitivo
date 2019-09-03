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
import persistence.dynamodb.Persona;
import persistence.dynamodb.Usuario;
import util.FacesUtils;
import util.utilidades;

@Getter
@Setter
public class DashBoardController
{
	private Usuario usuarioEnSesion;

	public DashBoardController()
	{
		Login login = (Login) FacesUtils.getManagedBean("login");
		this.usuarioEnSesion = login.getUsuarioAWS();

	}

	public List<Persona> getPersonasNoEntrevistadasAWS()
	{
		List<persistence.dynamodb.Persona> catMunicipiosAWS = null;

		DynamoDBMapper mapper = new DynamoDBMapper(utilidades.getAWSDynamoDBClient());

		Map<String, AttributeValue> eav = new HashMap<String, AttributeValue>();
		eav.put(":val1", new AttributeValue().withN("-1"));

		DynamoDBScanExpression queryExpression = new DynamoDBScanExpression()
				.withFilterExpression("decision.idDecision = :val1")
				// .withProjectionExpression("cve_agee,cve_agem,nom_agem")
				.withExpressionAttributeValues(eav);

		catMunicipiosAWS = mapper.scan(Persona.class, queryExpression);

		return catMunicipiosAWS;
	}

	public List<Persona> getPersonasEntrevistadasAWS()
	{
		List<persistence.dynamodb.Persona> catMunicipiosAWS = null;

		DynamoDBMapper mapper = new DynamoDBMapper(utilidades.getAWSDynamoDBClient());

		Map<String, AttributeValue> eav = new HashMap<String, AttributeValue>();
		eav.put(":val1", new AttributeValue().withN("-1"));

		DynamoDBScanExpression queryExpression = new DynamoDBScanExpression()
				.withFilterExpression("decision.idDecision > :val1")
				// .withProjectionExpression("cve_agee,cve_agem,nom_agem")
				.withExpressionAttributeValues(eav);

		catMunicipiosAWS = mapper.scan(Persona.class, queryExpression);

		return catMunicipiosAWS;
	}

}
