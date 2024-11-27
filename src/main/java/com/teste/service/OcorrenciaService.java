package com.teste.service;

import java.io.Serializable;
import java.sql.SQLIntegrityConstraintViolationException;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;


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
	
	
}
