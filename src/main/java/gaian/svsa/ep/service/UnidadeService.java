package gaian.svsa.ep.service;

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

import gaian.svsa.ep.dao.UnidadeDAO;
import gaian.svsa.ep.model.UnidadeEP;


@ApplicationScoped
public class UnidadeService implements Serializable {

	private static final long serialVersionUID = 1L;
	
	@Inject
	private UnidadeDAO unidadeDAO;
	
	@Inject
	private UsuarioService usuarioService;
	
	public List<UnidadeEP> listarUnidades() {
		return this.unidadeDAO.listarUnidades(this.usuarioService.getUsuarioAutenticado());
	}
	
}
