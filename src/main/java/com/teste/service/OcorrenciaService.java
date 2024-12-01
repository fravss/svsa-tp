package com.teste.service;

import java.io.Serializable;
import java.sql.SQLIntegrityConstraintViolationException;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;


import java.util.Set;

import javax.enterprise.context.spi.CreationalContext;
import javax.enterprise.inject.spi.Bean;
import javax.enterprise.inject.spi.BeanManager;
import javax.naming.InitialContext;
import javax.naming.NamingException;



import org.apache.log4j.Logger;

import com.teste.dao.OcorrenciaDAO;
import com.teste.model.Ocorrencia;


@ApplicationScoped
public class OcorrenciaService implements Serializable {

	private static final long serialVersionUID = 1L;
	
	private Logger log = Logger.getLogger(OcorrenciaService.class);
	
	@Inject
	private OcorrenciaDAO ocorrenciaDAO;
	
	public void salvar(Ocorrencia ocorrencia) throws SQLIntegrityConstraintViolationException {
			
		/*
		 * Verifica se existe agendamento em aberto	
		 */		
		
		
		//if (ocorrencia.getDestinatario() == null)
		//	throw new NegocioException("O destinatario é obrigatorio");
		/*if (ocorrencia.getTestemunha() == null)
			throw new NegocioException("A testemunha é obrigatorio");
		if (ocorrencia.getDescricao() == null)
			throw new NegocioException("A descrição é obrigatorio");*/
		
		this.ocorrenciaDAO.salvar(ocorrencia);

	}
		
	public Ocorrencia buscarPorId(Long id) {

		log.info("Buscando usuario por Id");

		return this.ocorrenciaDAO.buscarPeloCodigo(id);
	}
	
	public OcorrenciaDAO getOcorrenciaDAO() {
		return this.ocorrenciaDAO;
	}

	private static BeanManager getBeanManager() {
		try {
			InitialContext initialContext = new InitialContext();
			return (BeanManager) initialContext.lookup("java:comp/env/BeanManager");
		} catch (NamingException e) {
			throw new RuntimeException("Não pôde encontrar BeanManager no JNDI.");
		}
	}
	
	@SuppressWarnings("unchecked")
	public static <T> T getBean(Class<T> clazz) {
		BeanManager bm = getBeanManager();
		Set<Bean<?>> beans = (Set<Bean<?>>) bm.getBeans(clazz);

		if (beans == null || beans.isEmpty()) {
			return null;
		}

		Bean<T> bean = (Bean<T>) beans.iterator().next();

		CreationalContext<T> ctx = bm.createCreationalContext(bean);
		T o = (T) bm.getReference(bean, clazz, ctx);

		return o;
	}
	
	
}
