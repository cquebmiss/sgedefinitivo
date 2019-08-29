/*
s * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package util;

import java.io.File;
import java.io.Serializable;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import javax.faces.model.SelectItem;

import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClientBuilder;
import com.amazonaws.services.dynamodbv2.document.DynamoDB;

import modelo.PermisoSistema;
import modelo.Usuario;
import resources.DataBase;

/**
 *
 * @author desarolloyc
 */
public class utilidades implements Serializable
{

	public static String rutaServerArchivos = "/Users/desarolloyc/Downloads/ArchivosDigitales/";

	public static String[] comandos = new String[] { "pp" };

	public static DecimalFormat formato = new DecimalFormat("$#,###.00");
	
	
	public static AmazonDynamoDB client = null;
	public static DynamoDB dynamoDB = null;
	
	
	public static AmazonDynamoDB getAWSDynamoDBClient()
	{
		if( utilidades.client == null )
		{
			BasicAWSCredentials awsCreds = new BasicAWSCredentials("AKIAVWPSHQFDG3A65247",
					"jzZx6WPlUHIeVH0CxgEh9lael0ZtZQVp9gSVnN32");

			utilidades.client = AmazonDynamoDBClientBuilder.standard()
					.withCredentials(new AWSStaticCredentialsProvider(awsCreds)).withRegion(Regions.US_WEST_2).build();
			
			utilidades.dynamoDB = new DynamoDB(client);
			
		}
		
		
		return utilidades.client;
		
	}
	

	public static String ajustaMoneda(String valor1)
	{
		int lastPosComas = -1;
		int lastPosPuntos = -1;

		lastPosComas = valor1.lastIndexOf(",");
		lastPosPuntos = valor1.lastIndexOf(".");

		if (lastPosComas > lastPosPuntos)
		{
			valor1 = valor1.replace(".", "");
			valor1 = valor1.replace(",", ".");

		}
		else
		{
			valor1 = valor1.replace(",", "");
		}

		return valor1;
	}


	public static String formatoMoneda(String cantidad)
	{
		String cantidadFormato = null;

		try
		{
			cantidadFormato = formato.format(Double.parseDouble(cantidad));
		}
		catch (Exception e)
		{
			cantidadFormato = "NaN";
		}

		return cantidadFormato;
	}

	public static void eliminarRuta(File ruta)
	{
		File[] archivos = null;

		if (ruta.isDirectory())
		{
			archivos = ruta.listFiles();

			if (archivos.length > 0)
			{
				for (File archivo : archivos)
				{
					eliminarRuta(archivo);
				}

			}
		}

		ruta.delete();

	}

	public static String MD5(String md5)
	{
		try
		{
			java.security.MessageDigest md = java.security.MessageDigest.getInstance("MD5");

			byte[] array = md.digest(md5.getBytes());

			StringBuffer sb = new StringBuffer();

			for (int i = 0; i < array.length; ++i)
			{
				sb.append(Integer.toHexString((array[i] & 0xFF) | 0x100).substring(1, 3));
			}

			return sb.toString();

		}
		catch (java.security.NoSuchAlgorithmException e)
		{
			e.printStackTrace();
		}

		return null;

	}


	public static List<Usuario> getUsuariosRegistrados()
	{
		List<Usuario> usuarios = null;

		try (Connection conexion = ((DataBase) FacesUtils.getManagedBean("database")).getConnection();)
		{
			PreparedStatement prep = conexion.prepareStatement(
					" SELECT u.*, pp.Descripcion AS DescripcionNivelPermiso, s.Descripcion AS DescripcionStatus FROM usuario u, permisoportal pp, status s WHERE u.NivelPermiso = pp.idPermiso AND u.idStatus = s.idStatus ORDER BY u.idUsuario ASC ");

			ResultSet rBD = prep.executeQuery();

			if (rBD.next())
			{
				usuarios = new ArrayList<>();

				do
				{
					usuarios.add(new Usuario(rBD.getInt("idUsuario"), rBD.getString("Nombre"), null,
							rBD.getInt("NivelPermiso"), rBD.getString("DescripcionNivelPermiso"),
							rBD.getInt("idStatus"), rBD.getString("DescripcionStatus"), rBD.getString("NombreReal"),
							rBD.getString("CuentaCorreo"), rBD.getDate("VigenciaInicial"),
							rBD.getDate("VigenciaFinal")));

				} while (rBD.next());

			}

		}
		catch (Exception e)
		{
			e.printStackTrace();
		}

		return usuarios;
	}

	public static List<PermisoSistema> getPermisosSistemasUsuario(int idUsuario)
	{

		List<PermisoSistema> permisos = null;

		try (Connection conexion = ((DataBase) FacesUtils.getManagedBean("database")).getConnection();)
		{

			PreparedStatement prep = conexion.prepareStatement(
					"SELECT pus.idSistema, s.Descripcion AS DescripcionSistema, pus.idPermiso, ps.Descripcion AS DescripcionPermisoSistema FROM permisousuariosistema pus, sistemaplataforma s, permisosistema ps WHERE pus.idSistema = s.idSistema AND pus.idPermiso = ps.idPermiso AND pus.idUsuario=? ORDER BY pus.idSistema ASC");

			prep.setInt(1, idUsuario);

			ResultSet rBD = prep.executeQuery();

			if (rBD.next())
			{

				permisos = new ArrayList<>();

				do
				{
					permisos.add(new PermisoSistema(rBD.getInt("idSistema"), rBD.getString("DescripcionSistema"),
							rBD.getInt("idPermiso"), rBD.getString("DescripcionPermisoSistema")));

				} while (rBD.next());

			}

		}
		catch (Exception e)
		{
			e.printStackTrace();
		}

		return permisos;
	}

	// SE UTILIZA PARA PROVEER LA ESTRUCTURA DE ESQUEMA DE PERMISOS DE LOS
	// SISTEMAS DENTRO DE LA PLATAFORMA PARA NUEVO USUARIO
	public static List<PermisoSistema> getEstructuraPermisosSistemas()
	{

		List<PermisoSistema> permisos = null;

		try (Connection conexion = ((DataBase) FacesUtils.getManagedBean("database")).getConnection();)
		{

			PreparedStatement prep = conexion.prepareStatement("SELECT * FROM sistemaplataforma ORDER BY idSistema");

			ResultSet rBD = prep.executeQuery();

			if (rBD.next())
			{

				permisos = new ArrayList<>();

				do
				{
					permisos.add(
							new PermisoSistema(rBD.getInt("idSistema"), rBD.getString("Descripcion"), -1, "Ninguno"));

				} while (rBD.next());

			}

		}
		catch (Exception e)
		{
			e.printStackTrace();
		}

		return permisos;
	}

	public static List<SelectItem> getStatus()
	{
		List<SelectItem> status = null;

		try (Connection conexion = ((DataBase) FacesUtils.getManagedBean("database")).getConnection();)
		{
			PreparedStatement prep = conexion.prepareStatement(" SELECT * FROM status ORDER BY Descripcion ASC");

			ResultSet rBD = prep.executeQuery();

			if (rBD.next())
			{
				status = new ArrayList<>();

				do
				{
					status.add(new SelectItem(rBD.getInt("idStatus"), rBD.getString("Descripcion")));

				} while (rBD.next());

			}

		}
		catch (Exception e)
		{
			e.printStackTrace();
		}

		return status;
	}

	public static List<SelectItem> getPermisosPortal()
	{
		List<SelectItem> permisosPortal = null;

		try (Connection conexion = ((DataBase) FacesUtils.getManagedBean("database")).getConnection();)
		{
			PreparedStatement prep = conexion.prepareStatement(" SELECT * FROM permisoportal ORDER BY idPermiso DESC ");

			ResultSet rBD = prep.executeQuery();

			if (rBD.next())
			{
				permisosPortal = new ArrayList<>();

				do
				{
					permisosPortal.add(new SelectItem(rBD.getInt("idPermiso"), rBD.getString("Descripcion")));

				} while (rBD.next());
			}

		}
		catch (Exception e)
		{
			e.printStackTrace();
		}

		return permisosPortal;
	}

	public static List<SelectItem> getPermisosSistema()
	{
		List<SelectItem> permisosSistema = null;

		try (Connection conexion = ((DataBase) FacesUtils.getManagedBean("database")).getConnection();)
		{
			PreparedStatement prep = conexion.prepareStatement(" SELECT * FROM permisosistema ORDER BY idPermiso ASC ");

			ResultSet rBD = prep.executeQuery();

			if (rBD.next())
			{
				permisosSistema = new ArrayList<>();

				do
				{
					permisosSistema.add(new SelectItem(rBD.getInt("idPermiso"), rBD.getString("Descripcion")));

				} while (rBD.next());
			}

		}
		catch (Exception e)
		{
			e.printStackTrace();
		}

		return permisosSistema;
	}







}
