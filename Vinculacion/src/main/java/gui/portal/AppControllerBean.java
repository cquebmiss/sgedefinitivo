package gui.portal;

import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.temporal.TemporalUnit;
import java.util.Properties;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.faces.bean.ApplicationScoped;
import javax.faces.bean.ManagedBean;

import com.smattme.MysqlExportService;

import lombok.Getter;
import lombok.Setter;
import modelo.RespaldoBD;

@ManagedBean
@ApplicationScoped
@Getter
@Setter
public class AppControllerBean
{
	public final ScheduledExecutorService	scheduler	= Executors.newScheduledThreadPool(1);
	public RespaldoBD						respaldoBD;
	public Runnable							tareaRespaldo;
	long									initialDelay;
	boolean									iniciado	= false;

	public AppControllerBean()
	{
		System.out.println("Creado el constructor del AppControllerBean");
	}

	@PostConstruct
	public void postConstruct()
	{
		System.out.println("Creado el postconstruct del AppControllerBean");
		this.respaldoBD = new RespaldoBD();

		this.tareaRespaldo = new Runnable()
		{
			public void run()
			{
				System.out.println(LocalDateTime.now() + ": Inicio de respaldo de la base de datos del sistema...");

				try
				{
					respaldoBD.exportar();

				} catch (Exception e)
				{
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

				System.out.println(LocalDateTime.now() + ": Fin de respaldo de la base de datos del sistema...");

			}
		};

		LocalDateTime	localNow	= LocalDateTime.now();
		ZoneId			currentZone	= ZoneId.of("America/Los_Angeles");
		ZonedDateTime	zonedNow	= ZonedDateTime.of(localNow, currentZone);
		ZonedDateTime	zonedNow2	= ZonedDateTime.of(localNow, currentZone);

		ZonedDateTime zonedNext5;
		zonedNext5 = zonedNow.withHour(14).withMinute(0).withSecond(0);

		if (zonedNow.compareTo(zonedNext5) > 0)
			zonedNext5 = zonedNext5.plusDays(1);

		Duration duration = Duration.between(zonedNow, zonedNext5);
		initialDelay = duration.getSeconds();
		System.out.println("Initial delay de respaldos: " + initialDelay);

	}

	public void startSchedulerRespaldos()
	{
		System.out.println("Iniciando el Scheduler de respaldos...");
		
		if (!iniciado)
		{
			//required properties for exporting of db
			System.out.println("Configurando las propiedades...");
	    	Properties properties = new Properties();
	    
	    	properties.setProperty(MysqlExportService.JDBC_DRIVER_NAME, "com.mysql.jdbc.Driver");

	    	properties.setProperty(MysqlExportService.JDBC_CONNECTION_STRING, "jdbc:mysql://sge-mysql:3306/sge");

	    	//properties.setProperty(MysqlExportService.DB_NAME, "sge");

	    	properties.setProperty(MysqlExportService.DB_USERNAME, "indesalud");

	    	properties.setProperty(MysqlExportService.DB_PASSWORD, "@FPD1730yendoqueb");

	    	//properties relating to email config

	    	properties.setProperty(MysqlExportService.EMAIL_HOST, "gmail-smtp-in.l.google.com");

	    	properties.setProperty(MysqlExportService.EMAIL_PORT, "587");

	    	properties.setProperty(MysqlExportService.EMAIL_USERNAME, "cquebmiss");

	    	properties.setProperty(MysqlExportService.EMAIL_PASSWORD, "FPD2600lenovo");

	    	properties.setProperty(MysqlExportService.EMAIL_FROM, "cquebmiss@gmail.com	");

	    	properties.setProperty(MysqlExportService.EMAIL_TO, "cquebmiss@gmail.com");

	    	//set the outputs temp dir

	    	System.out.println("Propiedades definidas...");
	    	properties.setProperty(MysqlExportService.TEMP_DIR, new File("external").getPath());
	    	System.out.println("Archivo de respaldo creado...");

	    	MysqlExportService mysqlExportService = new MysqlExportService(properties);
	    	System.out.println("MySqlExportService definido...");

	    	try
			{
				mysqlExportService.export();
				System.out.println("MySqlExportService exitoso...");
				
			} catch (Exception e)
			{
				// TODO Auto-generated catch block
				System.out.println("MySqlExportService Exception...");
				e.printStackTrace();
			} 
	    	
			this.iniciado = true;
			System.out.println("Scheduler de respaldos iniciado");
		}
	}

	@PreDestroy
	public void preDestroy()
	{

	}
}
