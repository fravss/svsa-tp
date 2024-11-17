package com.teste.dao;

import java.io.Serializable;
import java.util.List;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;

import com.teste.model.Ocorrencia;

import com.teste.model.UsuarioEP;

import com.teste.util.jpa.Transactional;

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

	public List<Ocorrencia> buscarPendenciasPorUsuario(UsuarioEP usuario) throws NoResultException {
	    try {
	        return manager.createNamedQuery("Ocorrencia.buscarPendencias", Ocorrencia.class)
	                .setParameter("usuario", usuario)  
	                .setParameter("tenant", usuario.getUnidade().getTenant_id())
	                .setParameter("grupo", usuario.getGrupo().toString())
	                .getResultList();
	    } catch (NoResultException e) {
	        log.warn("Nenhuma pendência encontrada para o usuário: " + usuario.getNome());
	        return null;
	    }
	}
	
	@Transactional
	public void salvar(Ocorrencia ocorrencia) throws NoResultException {
		try {
			manager.merge(ocorrencia);
		} catch (NoResultException e) {
			log.warn("Nenhuma resposta encontrada para a ocorrência com código: " + e.getMessage());
		}
	}

}
