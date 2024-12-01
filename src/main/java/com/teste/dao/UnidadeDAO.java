package com.teste.dao;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Order;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.primefaces.model.FilterMeta;
import org.primefaces.model.SortMeta;
import org.primefaces.model.SortOrder;

import com.teste.model.Ocorrencia;
import com.teste.model.UsuarioEP;

import gaian.svsa.ct.modelo.Unidade;
import lombok.extern.log4j.Log4j;

@Log4j
public class UnidadeDAO implements Serializable {

	private static final long serialVersionUID = 1L;

	@Inject
	private EntityManager manager;
	
	public List<Unidade> listarUnidades(UsuarioEP usuario) {
		
		CriteriaBuilder cb = manager.getCriteriaBuilder();
        CriteriaQuery<Unidade> cq = cb.createQuery(Unidade.class);
        Root<Unidade> unidade = cq.from(Unidade.class);
        
        List<Predicate> predicates = new ArrayList<>();
        predicates.add(cb.equal(unidade.get("tenant"), usuario.getTenant()));
        
        cq.where(predicates.toArray(new Predicate[0]));
        TypedQuery<Unidade> query = manager.createQuery(cq);

        return query.getResultList();
		
	}

	/*public UsuarioEP buscarPeloCodigo(Long id) {
		log.info("Buscando usuario pelo id " + id);
		return manager.find(UsuarioEP.class, id);
	}

	public List<UsuarioEP> buscarUsuarios(int first, int pageSize, Map<String, SortMeta> sortBy,
			Map<String, FilterMeta> filterBy) {
        
		CriteriaBuilder cb = manager.getCriteriaBuilder();
        CriteriaQuery<UsuarioEP> cq = cb.createQuery(UsuarioEP.class);
        Root<UsuarioEP> usuario = cq.from(UsuarioEP.class);

        // Aplicar filtros
        List<Predicate> predicates = new ArrayList<>();
        
        for (Map.Entry<String, FilterMeta> filtro : filterBy.entrySet()) {
            FilterMeta meta = filtro.getValue();
            if (meta.getFilterValue() != null) {
            	predicates.add(cb.equal(usuario.get(filtro.getKey()), meta.getFilterValue()));
            	/*if ("tipo".equals(filtro.getKey())) {
            		TipoOcorrencia tipo = TipoOcorrencia.valueOf(meta.getFilterValue().toString().toUpperCase());  // Converte para o enum correspondente
            		predicates.add(cb.equal(ocorrencia.get(filtro.getKey()), tipo));
            	}
            	else {
            		predicates.add(cb.equal(ocorrencia.get(filtro.getKey()), meta.getFilterValue()));
            	}*//*
            }
        }
        cq.where(predicates.toArray(new Predicate[0]));

        // Aplicar ordenação
        List<Order> orders = new ArrayList<>();
        sortBy.forEach((field, sortMeta) -> {
            if (sortMeta.getOrder() == SortOrder.ASCENDING) {
                orders.add(cb.asc(usuario.get(field)));
            } else if (sortMeta.getOrder() == SortOrder.DESCENDING) {
                orders.add(cb.desc(usuario.get(field)));
            }
        });
        cq.orderBy(orders);

        // Executar consulta com paginação
        TypedQuery<UsuarioEP> query = manager.createQuery(cq);
        query.setFirstResult(first);
        query.setMaxResults(pageSize);

        return query.getResultList();
        
    }
	
	public int contarUsuarios(Map<String, FilterMeta> filterBy) {
        CriteriaBuilder cb = manager.getCriteriaBuilder();
        CriteriaQuery<Long> cq = cb.createQuery(Long.class);
        Root<Ocorrencia> ocorrencia = cq.from(Ocorrencia.class);

        // Aplicar filtros
        List<Predicate> predicates = new ArrayList<>();
        filterBy.forEach((field, filterMeta) -> {
            Object filterValue = filterMeta.getFilterValue();
            /*if ("descricao".equals(field) && filterValue != null) {
                predicates.add(cb.like(ocorrencia.get("descricao"), "%" + filterValue + "%"));
            } else if ("codigo".equals(field) && filterValue != null){
            	predicates.add(cb.equal(ocorrencia.get("codigo"), filterValue));
            } else if ("usuario".equals(field) && filterValue != null) {
                predicates.add(cb.equal(ocorrencia.get("usuario").get("codigo"), filterValue));
            }
        });*/ /*
        cq.where(predicates.toArray(new Predicate[0]));
        cq.select(cb.count(ocorrencia));

        return manager.createQuery(cq).getSingleResult().intValue();
    }
	
	public List<UsuarioEP> buscarPorNome(String nome) {
		String jpql = 	"SELECT u FROM UsuarioEP u " +
						"WHERE LOWER(u.nome) LIKE LOWER(:nome)";
		TypedQuery<UsuarioEP> query = manager.createQuery(jpql, UsuarioEP.class);
		query.setParameter("nome", "%" + nome + "%");
		query.setMaxResults(10);
		return query.getResultList();
	}*/

}
