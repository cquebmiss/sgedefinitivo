package dao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBScanExpression;
import com.amazonaws.services.dynamodbv2.model.AttributeValue;

import persistence.dynamodb.Usuario;
import util.utilidades;

public class LoginDAO {

	public static List<Usuario> getUsuario(String nombre, String contrasena) 
	{
		
		List<Usuario> usuarios = null;

		DynamoDBMapper mapper = new DynamoDBMapper(utilidades.getAWSDynamoDBClient());

		Map<String, AttributeValue> eav = new HashMap<>();
		eav.put(":val1", new AttributeValue().withS(nombre));
		eav.put(":val2", new AttributeValue().withS(contrasena));

		DynamoDBScanExpression queryExpression = new DynamoDBScanExpression()
				.withFilterExpression("nombre = :val1 and contrasena = :val2 ").withExpressionAttributeValues(eav);

		usuarios = mapper.scan(Usuario.class, queryExpression);

		return usuarios;
	}

}
