package gui.portal;

import static java.util.concurrent.TimeUnit.HOURS;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

import javax.annotation.ManagedBean;
import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.faces.bean.ApplicationScoped;

import modelo.RespaldoBD;

@ManagedBean
@ApplicationScoped
public class AplicationControllerBean
{
	private final ScheduledExecutorService	scheduler	= Executors.newScheduledThreadPool(1);
	private RespaldoBD						respaldoBD;

	@PostConstruct
	public void postConstruct()
	{
		this.respaldoBD = new RespaldoBD();

		final Runnable	tareaRespaldo	= new Runnable()
										{
											public void run()
											{
												System.out.println(LocalDateTime.now()
														+ ": Inicio de respaldo de la base de datos del sistema...");

												try
												{
													respaldoBD.exportar();

												} catch (Exception e)
												{
													// TODO Auto-generated catch block
													e.printStackTrace();
												}

												System.out.println(LocalDateTime.now()
														+ ": Fin de respaldo de la base de datos del sistema...");

											}
										};

		LocalDateTime	localNow		= LocalDateTime.now();
		ZoneId			currentZone		= ZoneId.of("America/Los_Angeles");
		ZonedDateTime	zonedNow		= ZonedDateTime.of(localNow, currentZone);
		ZonedDateTime	zonedNext5;
		zonedNext5 = zonedNow.withHour(14).withMinute(30).withSecond(0);

		if (zonedNow.compareTo(zonedNext5) > 0)
			zonedNext5 = zonedNext5.plusDays(1);

		Duration	duration	= Duration.between(zonedNow, zonedNext5);
		long		initalDelay	= duration.getSeconds();

		scheduler.scheduleAtFixedRate(tareaRespaldo, initalDelay, 10, HOURS);

	}

	@PreDestroy
	public void preDestroy()
	{

	}

}
