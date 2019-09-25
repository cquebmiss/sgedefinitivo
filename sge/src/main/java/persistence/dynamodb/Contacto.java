package persistence.dynamodb;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBDocument;

import lombok.AllArgsConstructor;

import lombok.Setter;

import lombok.Getter;
import lombok.NoArgsConstructor;

@DynamoDBDocument
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Contacto
{
	private int		idContacto;
	private String	nombres;
	private String	telefonos;
	private String	email;
	private String	observaciones;

}
