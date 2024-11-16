package com.teste.dao;

import java.io.Serializable;

import javax.inject.Inject;
import javax.persistence.EntityManager;

import com.teste.model.Ocorrencia;

import lombok.extern.log4j.Log4j;

@Log4j
public class OcorrenciaDAO implements Serializable {

	private static final long serialVersionUID = 1L;

	@Inject
	private EntityManager manager;

	public Ocorrencia buscarPeloCodigo(Long id) {
		log.info("Buscando usuario pelo id " + id);
		return manager.find(Ocorrencia.class, id);
	}

	

}
