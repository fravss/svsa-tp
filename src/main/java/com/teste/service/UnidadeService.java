package com.teste.service;

import java.io.Serializable;
import java.sql.SQLIntegrityConstraintViolationException;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import java.util.List;
import java.util.Set;

import javax.enterprise.context.spi.CreationalContext;
import javax.enterprise.inject.spi.Bean;
import javax.enterprise.inject.spi.BeanManager;
import javax.naming.InitialContext;
import javax.naming.NamingException;



import org.apache.log4j.Logger;

import com.teste.dao.UnidadeDAO;
import com.teste.model.UnidadeEP;
import com.teste.model.UsuarioEP;

@ApplicationScoped
public class UnidadeService implements Serializable {

	private static final long serialVersionUID = 1L;
	
	@Inject
	private UnidadeDAO unidadeDAO;
	
	private UsuarioService usuarioService;
	
	public List<UnidadeEP> listarUnidades() {
		return this.unidadeDAO.listarUnidades(this.usuarioService.getUsuarioAutenticado());
	}
	
}
