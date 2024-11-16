package com.teste.dao;

import java.io.Serializable;
import java.util.List;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;


import com.teste.model.Resposta;
import com.teste.util.jpa.Transactional;

import lombok.extern.log4j.Log4j;

@Log4j
public class RespostaDAO implements Serializable {

	private static final long serialVersionUID = 1L;

	@Inject
	private EntityManager manager;

	public List<Resposta> buscarTodasPorOcorrenciaId(Long id) throws NoResultException {
		try {
			return manager.createNamedQuery("Resposta.findByOcorrencia", Resposta.class)
					.setParameter("codigoOcorrencia", id).getResultList();
		} catch (NoResultException e) {
			log.warn("Nenhuma resposta encontrada para a ocorrência com código: " + id);
			return null;
		}

	}
	
	@Transactional
	public void salvar(Resposta resposta) throws NoResultException {
	    try {
	        manager.merge(resposta);
	    } catch (NoResultException e) {
	        log.warn("Nenhuma resposta encontrada para a ocorrência com código: " + e.getMessage());
	    }
	}

	}

