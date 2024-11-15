package com.teste.service;

import java.io.Serializable;


import javax.inject.Inject;

import com.teste.dao.UsuarioDAO;
import com.teste.model.UsuarioEP;

import lombok.extern.log4j.Log4j;

@Log4j
public class UsuarioService implements Serializable {

	private static final long serialVersionUID = 1L;
	
	@Inject
	private UsuarioDAO usuarioDAO;
	
	public UsuarioEP buscarPorId(Long id) {

		log.info("Buscando usuario por Id");

		return this.usuarioDAO.buscarPeloCodigo(id);
	}
	
}
