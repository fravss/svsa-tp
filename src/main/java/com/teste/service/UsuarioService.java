package com.teste.service;

import java.io.Serializable;


import javax.inject.Inject;

import com.teste.dao.UsuarioDAO;
import com.teste.model.Usuario;

import lombok.extern.log4j.Log4j;

@Log4j
public class UsuarioService implements Serializable {

	private static final long serialVersionUID = 1L;
	
	@Inject
	private UsuarioDAO usuarioDAO;
	
	public Usuario buscarPorId(Long id) {

		log.info("Buscando usuario por Id");

		return this.usuarioDAO.buscarPeloCodigo(id);
	}
	
}
