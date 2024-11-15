package com.teste.dao;

import java.io.Serializable;

import javax.inject.Inject;
import javax.persistence.EntityManager;

import com.teste.model.UsuarioEP;

import lombok.extern.log4j.Log4j;

@Log4j
public class UsuarioDAO implements Serializable {

	private static final long serialVersionUID = 1L;

	@Inject
	private EntityManager manager;

	public UsuarioEP buscarPeloCodigo(Long id) {
		log.info("Buscando usuario pelo id " + id);
		return manager.find(UsuarioEP.class, id);
	}

	

}
