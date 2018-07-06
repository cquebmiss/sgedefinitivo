package util;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

public class UtilidadesCalendario
{
	public static DateFormat		formatter		= new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	public static DateTimeFormatter	dateformatter	= DateTimeFormatter.ofPattern("yyyy-MM-dd");

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

}
