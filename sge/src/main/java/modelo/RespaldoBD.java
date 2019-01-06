package modelo;

import java.io.File;
import java.util.Properties;

import com.smattme.MysqlExportService;

public class RespaldoBD
{
	MysqlExportService mysqlExportService;

	public RespaldoBD()
	{

		//required properties for exporting of db

    	Properties properties = new Properties();
    
    	properties.setProperty(MysqlExportService.JDBC_DRIVER_NAME, "com.mysql.jdbc.Driver");

    	properties.setProperty(MysqlExportService.JDBC_CONNECTION_STRING, "jdbc:mysql://localhost:3306/sge");

    	//properties.setProperty(MysqlExportService.DB_NAME, "sge");

    	properties.setProperty(MysqlExportService.DB_USERNAME, "root");

    	properties.setProperty(MysqlExportService.DB_PASSWORD, "@FPD1730yendoqueb");

    	//properties relating to email config

    	properties.setProperty(MysqlExportService.EMAIL_HOST, "smtp.gmail.com");

    	properties.setProperty(MysqlExportService.EMAIL_PORT, "587");

    	properties.setProperty(MysqlExportService.EMAIL_USERNAME, "cquebmiss");

    	properties.setProperty(MysqlExportService.EMAIL_PASSWORD, "FPD2600lenovo");

    	properties.setProperty(MysqlExportService.EMAIL_FROM, "cquebmiss@gmail.com	");

    	properties.setProperty(MysqlExportService.EMAIL_TO, "cquebmiss@gmail.com");

    	//set the outputs temp dir

    	properties.setProperty(MysqlExportService.TEMP_DIR, new File("external").getPath());


		this.mysqlExportService = new MysqlExportService(properties);

	}

	public boolean exportar() throws Exception
	{
		try
		{
			mysqlExportService.export();

		} catch (Exception e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return true;
	}

}
