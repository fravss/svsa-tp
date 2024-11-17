package com.teste.service;

import java.io.Serializable;

import javax.inject.Inject;

import org.apache.log4j.Logger;

import com.teste.dao.OcorrenciaDAO;
import com.teste.model.Ocorrencia;

import lombok.extern.log4j.Log4j;

@Log4j
public class OcorrenciaService implements Serializable {

	private static final long serialVersionUID = 1L;
	
	private Logger log = Logger.getLogger(OcorrenciaService.class);
	
	@Inject
	private OcorrenciaDAO ocorrenciaDAO;
	
	public Ocorrencia buscarPorId(Long id) {

		log.info("Buscando usuario por Id");

		return this.ocorrenciaDAO.buscarPeloCodigo(id);
	}
	
	public OcorrenciaDAO getOcorrenciaDAO() {
		return this.ocorrenciaDAO;
	}
	
	
}
