package gaian.svsa.ep.service;

import java.io.Serializable;


import javax.inject.Inject;

import gaian.svsa.ep.dao.UsuarioDAO;
import gaian.svsa.ep.model.UsuarioEP;
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
