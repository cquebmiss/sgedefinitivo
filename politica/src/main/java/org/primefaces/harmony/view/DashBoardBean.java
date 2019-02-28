package org.primefaces.harmony.view;

import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;

import org.hibernate.annotations.common.util.impl.LoggerFactory;
import org.primefaces.harmony.dao.SeguridadSocialRepository;
import org.primefaces.harmony.domain.SeguridadSocial;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import lombok.Getter;
import lombok.Setter;

@ManagedBean
@Getter
@Setter
@Component
public class DashBoardBean
{
	private String						leyenda;

	@Autowired
	private SeguridadSocialRepository	seguridadSocialRepository;

	@PostConstruct
	public void postConstruct()
	{
		List<SeguridadSocial> lista = (List<SeguridadSocial>) getSeguridadSocialRepository().findAll();
		
		System.out.println("Seguridad Social: "+ lista.size());

	}

}
