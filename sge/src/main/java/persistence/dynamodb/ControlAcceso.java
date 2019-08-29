package persistence.dynamodb;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBAttribute;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBHashKey;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTable;

@DynamoDBTable(tableName = "ControlAcceso")
public class ControlAcceso {

	private Integer id;
	private String title;
	private String ip;
	private String fechahora;

	@DynamoDBHashKey(attributeName = "id")
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}

	@DynamoDBAttribute(attributeName = "title")
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	
	@DynamoDBAttribute(attributeName = "ip")
	public String getIp() {
		return ip;
	}
	public void setIp(String ip) {
		this.ip = ip;
	}

	@DynamoDBAttribute(attributeName = "fechahora")
	public String getFechahora() {
		return fechahora;
	}
	public void setFechahora(String fechahora) {
		this.fechahora = fechahora;
	}

}
