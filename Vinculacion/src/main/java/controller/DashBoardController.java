package controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.StringJoiner;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBScanExpression;
import com.amazonaws.services.dynamodbv2.model.AttributeValue;

import gui.Login;
import lombok.Getter;
import lombok.Setter;
import persistence.dynamodb.LocalidadConf;
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

	public List<Persona> getPersonasNoEntrevistadasAWS(List<LocalidadConf> localidad)
	{
		List<persistence.dynamodb.Persona> catMunicipiosAWS = null;

		DynamoDBMapper mapper = new DynamoDBMapper(utilidades.getAWSDynamoDBClient());

		Map<String, AttributeValue> eav = new HashMap<String, AttributeValue>();
		eav.put(":val1", new AttributeValue().withN("-1"));

		String queryComplete = "";
		StringJoiner query = new StringJoiner(" or ", "(", ")");
		
		if (localidad != null)
		{
			int indice = 2;

			for (LocalidadConf lConf : localidad)
			{
				eav.put(":val" + indice, new AttributeValue().withS(lConf.getIdEstado()));
				queryComplete = " ( localidad.idEstado = :val" + indice + " and ";
				indice++;

				eav.put(":val" + indice, new AttributeValue().withS(lConf.getIdMunicipio()));
				queryComplete += " localidad.idMunicipio = :val" + indice + " and ";
				indice++;

				eav.put(":val" + indice, new AttributeValue().withS(lConf.getIdLocalidad()));
				queryComplete += " localidad.idLocalidad = :val" + indice + " ) ";
				indice++;

				query.add(queryComplete);

			}

		}

		DynamoDBScanExpression queryExpression = new DynamoDBScanExpression()
				.withFilterExpression("decision.idDecision = :val1 " + (localidad != null ? " and " + query : ""))
				// .withProjectionExpression("cve_agee,cve_agem,nom_agem")
				.withExpressionAttributeValues(eav);

		catMunicipiosAWS = mapper.scan(Persona.class, queryExpression);

		return catMunicipiosAWS;
	}

	public List<Persona> getPersonasEntrevistadasAWS(List<LocalidadConf> localidad)
	{
		List<persistence.dynamodb.Persona> catMunicipiosAWS = null;

		DynamoDBMapper mapper = new DynamoDBMapper(utilidades.getAWSDynamoDBClient());

		Map<String, AttributeValue> eav = new HashMap<String, AttributeValue>();
		eav.put(":val1", new AttributeValue().withN("-1"));

		String queryComplete = "";
		StringJoiner query = new StringJoiner(" or ", "(", ")");

		if (localidad != null)
		{
			int indice = 2;

			for (LocalidadConf lConf : localidad)
			{
				eav.put(":val" + indice, new AttributeValue().withS(lConf.getIdEstado()));
				queryComplete = " ( localidad.idEstado = :val" + indice + " and ";
				indice++;

				eav.put(":val" + indice, new AttributeValue().withS(lConf.getIdMunicipio()));
				queryComplete += " localidad.idMunicipio = :val" + indice + " and ";
				indice++;

				eav.put(":val" + indice, new AttributeValue().withS(lConf.getIdLocalidad()));
				queryComplete += " localidad.idLocalidad = :val" + indice + " ) ";
				indice++;

				query.add(queryComplete);

			}

		}

		DynamoDBScanExpression queryExpression = new DynamoDBScanExpression()
				.withFilterExpression("decision.idDecision > :val1 " + (localidad != null ? " and " + query : ""))
				// .withProjectionExpression("cve_agee,cve_agem,nom_agem")
				.withExpressionAttributeValues(eav);

		catMunicipiosAWS = mapper.scan(Persona.class, queryExpression);

		return catMunicipiosAWS;
	}

}
