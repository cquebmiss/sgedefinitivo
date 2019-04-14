package mx.gob.salud.reestructurador.util;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

public class UtilidadesCalendario
{
	public static DateFormat		formatter			= new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	public static DateFormat		formatterfullDT		= new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSS'Z'");
	public static DateTimeFormatter	dateformatter		= DateTimeFormatter.ofPattern("yyyy-MM-dd");
	public static DateTimeFormatter	dateTimeformatter	= DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

	public static ZoneId			singaporeZoneId		= ZoneId.of("Greenwich");
	public static ZoneId			newYokZoneId		= ZoneId.of("Mexico/BajaNorte");

	public static java.util.Date getUtilDate(String fecha, String hora)
	{
		try
		{
			return UtilidadesCalendario.formatter.parse(fecha + " " + hora);
		}
		catch (ParseException e)
		{
			e.printStackTrace();
		}

		return null;
	}

	public static LocalDate convertUtilDateToLocalDate(java.util.Date date)
	{
		String fString = new SimpleDateFormat("yyyy-MM-dd").format(date);
		return LocalDate.parse(fString, dateformatter);

	}

	//Hora zulu u hora universal o del meridiano de Greenwich GMT
	public static String parseHoraZuluHoraMexico(String fecha)
	{
		LocalDateTime ldt = LocalDateTime.parse(fecha, DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSSS'Z'"));
		ZonedDateTime asiaZonedDateTime = ldt.atZone(singaporeZoneId);
		//System.out.println("Date (Greenwich) : " + asiaZonedDateTime);

		ZoneId newYokZoneId = ZoneId.of("Mexico/BajaNorte");
		//System.out.println("TimeZone : " + newYokZoneId);

		ZonedDateTime nyDateTime = asiaZonedDateTime.withZoneSameInstant(newYokZoneId);
		// System.out.println("Date (Mexico/BajaNorte) : " + nyDateTime);

		return nyDateTime.format(UtilidadesCalendario.dateTimeformatter);
	}

}
