package com.teste.dao;

import java.io.Serializable;
import java.util.List;

import javax.inject.Inject;
import javax.persistence.EntityManager;

import com.teste.model.Ocorrencia;

public class OcorrenciaDAO implements Serializable{
	
	private static final long serialVersionUID = 1L;
	
	@Inject
	private EntityManager manager;

	public Ocorrencia buscarPeloCodigo(Long id) {
		//log.info("Buscando usuario pelo id " + id);
		return manager.find(Ocorrencia.class, id);
	}
	
	@SuppressWarnings("unchecked")
	public List<Ocorrencia> buscarTodos() {
		return manager.createNamedQuery("Ocorrencia.buscarTodos").getResultList();
	}
	
	@SuppressWarnings("unchecked")
	public List<Ocorrencia> buscarComPaginacao(int first, int pageSize, String termo, int codigo) {

		if (codigo == 1) { // CODIGO
			return manager
					.createQuery("Select o From Ocorrencia o where o.codigo LIKE :termo ")
					.setParameter("termo", "%" + termo.toUpperCase() + "%")
					.setFirstResult(first).setMaxResults(pageSize).getResultList();
		} else {
			return manager.createNamedQuery("Ocorrencia.buscarTodos")
					.setFirstResult(first).setMaxResults(pageSize).getResultList();
		}	
	}
	
	public Long encontrarQdeOcorrencias(String termo, int codigo) {

		if (codigo == 1) { // CODIGO
			return (Long) manager
					.createQuery("Select count(o) From Ocorrencia o where o.codigo LIKE :termo")
					.setParameter("termo", "%" + termo.toUpperCase() + "%").getSingleResult();
		} else {
			return (Long) manager
					.createQuery(
							"Select count(o) From Ocorrencia o")
					.getSingleResult();
		}
	}

	public void setEntityManager(EntityManager manager) {
		this.manager = manager;
	}

	public void teste() {
		System.out.println("teste");
	}
	
}
