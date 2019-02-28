package org.primefaces.harmony.domain;

import javax.persistence.Entity;
import javax.persistence.Id;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class SeguridadSocial
{
	@Id
	private Integer	idSeguridadSocial;

	private String	descripcion;

}
