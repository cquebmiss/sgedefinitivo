package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import resources.DataBase;
import util.FacesUtils;

@NoArgsConstructor
@Getter
@Setter
public class GestionesDAO
{

	public LinkedHashMap<String, Integer> getTotalGestionesMensuales(int año)
	{
		LinkedHashMap<String, Integer> resultados = new LinkedHashMap<>();

		PreparedStatement prep = null;
		ResultSet rBD = null;

		try (Connection conexion = ((DataBase) FacesUtils.getManagedBean("database")).getConnectionGestiones();)
		{
			prep = conexion.prepareStatement(
					" SELECT SUBSTRING(FechaRecepcion, 6, 2) as mes, COUNT(*) as totalMes FROM gestion where FechaRecepcion like ? group by SUBSTRING(FechaRecepcion, 6, 2) ");
			prep.setString(1, "%" + año + "%");

			rBD = prep.executeQuery();

			if (rBD.next())
			{
				do
				{
					String mes = null;

					switch (rBD.getString("mes"))
					{
						case "01":
							mes = "Enero";
							break;
						case "02":
							mes = "Febrero";
							break;
						case "03":
							mes = "Marzo";
							break;
						case "04":
							mes = "Abril";
							break;
						case "05":
							mes = "Mayo";
							break;
						case "06":
							mes = "Junio";
							break;
						case "07":
							mes = "Julio";
							break;
						case "08":
							mes = "Agosto";
							break;
						case "09":
							mes = "Septiembre";
							break;
						case "10":
							mes = "Octubre";
							break;
						case "11":
							mes = "Noviembre";
							break;
						case "12":
							mes = "Diciembre";
							break;

					}

					resultados.put(mes, rBD.getInt("totalMes"));

				} while (rBD.next());
			}
		} catch (Exception e)
		{
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,
					"Excepción",
					"Ha ocurrido una excepción al obtener el total de gestiones anuales por mes, favor de contactar con el desarrollador del sistema."));

			e.printStackTrace();

		} finally
		{
			if (prep != null)
			{
				try
				{
					prep.close();
				} catch (SQLException e)
				{
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}

		return resultados;
	}

}
