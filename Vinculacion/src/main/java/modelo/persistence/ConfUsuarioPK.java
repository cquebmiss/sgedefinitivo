package modelo.persistence;

import java.io.Serializable;
import java.util.Objects;

import javax.persistence.Embeddable;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Embeddable
@Getter
@Setter
@NoArgsConstructor
public class ConfUsuarioPK extends LocalidadPK implements Serializable
{
	private Integer idUsuario;
		
	
	 @Override
	    public boolean equals(Object o) {
	        if (this == o) return true;
	        if (!(o instanceof ConfUsuarioPK)) return false;
	        ConfUsuarioPK that = (ConfUsuarioPK) o;
	        return Objects.equals(getIdLocalidad(), that.getIdLocalidad()) &&
	                Objects.equals(getIdUsuario(), that.getIdUsuario());
	    }
	 
	    @Override
	    public int hashCode() {
	        return Objects.hash(getIdUsuario(), getIdLocalidad());
	    }
}
