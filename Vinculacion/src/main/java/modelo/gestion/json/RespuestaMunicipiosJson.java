package modelo.gestion.json;

import java.util.HashMap;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class RespuestaMunicipiosJson
{
	private List<MunicipioINEGI>	datos;
	private HashMap<String, String>	metadatos;
	private Integer					numReg;
	
	private String result;
	private String mensaje;
}
