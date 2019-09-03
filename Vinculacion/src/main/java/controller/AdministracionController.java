package controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBScanExpression;
import com.amazonaws.services.dynamodbv2.model.AttributeValue;

import gui.Login;
import lombok.Getter;
import lombok.Setter;
import persistence.dynamodb.Usuario;
import resources.DataBase;
import util.FacesUtils;
import util.utilidades;

@Getter
@Setter
public class AdministracionController
{
	private Usuario usuarioEnSesion;

	public AdministracionController()
	{
		DataBase dataBaseBean = (DataBase) FacesUtils.getManagedBean("database");

		Login login = (Login) FacesUtils.getManagedBean("login");
		this.usuarioEnSesion = login.getUsuarioAWS();

	}

	public List<Usuario> getUsuariosSistemaAWS()
	{
		List<Usuario> usuarios = null;

		DynamoDBMapper mapper = new DynamoDBMapper(utilidades.getAWSDynamoDBClient());

		usuarios = mapper.scan(Usuario.class, new DynamoDBScanExpression());

		return usuarios;
	}

	public List<persistence.dynamodb.Estado> getCatEstadosAWS()
	{
		List<persistence.dynamodb.Estado> catEstadosAWS = null;

		DynamoDBMapper mapper = new DynamoDBMapper(utilidades.getAWSDynamoDBClient());

		catEstadosAWS = mapper.scan(persistence.dynamodb.Estado.class, new DynamoDBScanExpression());

		return catEstadosAWS;
	}

	public List<persistence.dynamodb.Municipio> getMunicipoAWS(String cve_agee, String cve_agem)
	{
		List<persistence.dynamodb.Municipio> catMunicipiosAWS = null;

		DynamoDBMapper mapper = new DynamoDBMapper(utilidades.getAWSDynamoDBClient());

		Map<String, AttributeValue> eav = new HashMap<String, AttributeValue>();
		eav.put(":val1", new AttributeValue().withS(cve_agee));
		eav.put(":val2", new AttributeValue().withS(cve_agem));

		DynamoDBScanExpression queryExpression = new DynamoDBScanExpression()
				.withFilterExpression("cve_agee = :val1 and cve_agem = :val2")
				.withProjectionExpression("cve_agee,cve_agem,nom_agem").withExpressionAttributeValues(eav);

		catMunicipiosAWS = mapper.scan(persistence.dynamodb.Municipio.class, queryExpression);

		return catMunicipiosAWS;
	}

	public List<String> getLocalidadesSistemaAWS(String cve_agee, String cve_agem, String cve_loc)
	{
		List<persistence.dynamodb.Localidad> catLocalidades = null;

		DynamoDBMapper mapper = new DynamoDBMapper(utilidades.getAWSDynamoDBClient());

		Map<String, AttributeValue> eav = new HashMap<>();
		eav.put(":val1", new AttributeValue().withS(cve_agee.trim()));
		eav.put(":val2", new AttributeValue().withS(cve_agem.trim()));

		if (!cve_loc.trim().isEmpty())
		{
			eav.put(":val3", new AttributeValue().withS(cve_loc.trim()));
		}

		DynamoDBScanExpression queryExpression = new DynamoDBScanExpression()
				.withFilterExpression("cve_agee = :val1 and cve_agem = :val2 "
						+ (cve_loc.trim().isEmpty() ? "" : "and cve_loc = :val3 "))
				.withProjectionExpression("cve_agee,cve_agem,cve_loc,nom_loc").withExpressionAttributeValues(eav);

		catLocalidades = mapper.scan(persistence.dynamodb.Localidad.class, queryExpression);

		List<String> localidadesString = new ArrayList<>();
		catLocalidades.forEach(localidad -> localidadesString.add(localidad.getClaveCombinada()));

		return localidadesString;
	}

	public List<persistence.dynamodb.PermisoUsuario> getCatPermisoUsuarioAWS()
	{
		List<persistence.dynamodb.PermisoUsuario> catPermisoUsuario = null;

		DynamoDBMapper mapper = new DynamoDBMapper(utilidades.getAWSDynamoDBClient());

		catPermisoUsuario = mapper.scan(persistence.dynamodb.PermisoUsuario.class, new DynamoDBScanExpression());

		return catPermisoUsuario;
	}

	public List<persistence.dynamodb.StatusUsuario> getCatStatusUsuarioAWS()
	{
		List<persistence.dynamodb.StatusUsuario> catStatusUsuario = null;

		DynamoDBMapper mapper = new DynamoDBMapper(utilidades.getAWSDynamoDBClient());

		catStatusUsuario = mapper.scan(persistence.dynamodb.StatusUsuario.class, new DynamoDBScanExpression());

		return catStatusUsuario;
	}

	public void saveUsuarioAWS(Usuario usuario)
	{
		DynamoDBMapper mapper = new DynamoDBMapper(utilidades.getAWSDynamoDBClient());
		mapper.save(usuario);
	}

}
