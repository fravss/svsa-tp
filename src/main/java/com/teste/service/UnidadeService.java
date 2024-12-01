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
import com.teste.model.UsuarioEP;

import gaian.svsa.ct.modelo.Unidade;


@ApplicationScoped
public class UnidadeService implements Serializable {

	private static final long serialVersionUID = 1L;
	
	@Inject
	private UnidadeDAO unidadeDAO;
	
	@Inject
	private AutenticacaoService autenticacaoService;
	
	public List<Unidade> listarUnidades() {
		return this.unidadeDAO.listarUnidades(this.autenticacaoService.getUsuarioAutenticado());
	}
	
}
