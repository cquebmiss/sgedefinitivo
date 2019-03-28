package modelo;

public class Pojo
{
	String tipo;
	
	public Pojo(String tipo)
	{
		this.tipo = tipo;
	}
	
	
	public void cambiarPelo(Pojo pojo)
	{
		pojo  = null;
	}
	
	
	
	public static void main(String []args)
	{
		Pojo pojo = new Pojo("Normal");
		Pojo pojo2 = new Pojo("Normal 2");
		
		System.out.println("Cabello de pojo2: "+pojo2.tipo);
		pojo.cambiarPelo(pojo2);
		System.out.println("Cabello de pojo 2 despues de método: "+pojo2.tipo);
		
		System.out.println("Cabello de pojo 1: "+pojo.tipo);
		
		
	}
	

}
