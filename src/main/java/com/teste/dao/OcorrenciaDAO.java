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
import com.teste.model.enums.TipoOcorrencia;

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
	
	
	public List<Ocorrencia> buscarOcorrencias(int first, int pageSize, Map<String, SortMeta> sortBy,
			Map<String, FilterMeta> filterBy) {
        
		CriteriaBuilder cb = manager.getCriteriaBuilder();
        CriteriaQuery<Ocorrencia> cq = cb.createQuery(Ocorrencia.class);
        Root<Ocorrencia> ocorrencia = cq.from(Ocorrencia.class);

        // Aplicar filtros
        List<Predicate> predicates = new ArrayList<>();
        
        // *************************************************
        // ADICIONAR A LOGICA PARA FILTRAR BASEADO EM FUNCAO
        // FILTRO TECNICO
        // FILTRO COORDENADOR
        // GESTOR NAO PRECISA
        // *************************************************
        
        for (Map.Entry<String, FilterMeta> filtro : filterBy.entrySet()) {
            FilterMeta meta = filtro.getValue();
            if (meta.getFilterValue() != null) {
            	predicates.add(cb.equal(ocorrencia.get(filtro.getKey()), meta.getFilterValue()));
            	/*if ("tipo".equals(filtro.getKey())) {
            		TipoOcorrencia tipo = TipoOcorrencia.valueOf(meta.getFilterValue().toString().toUpperCase());  // Converte para o enum correspondente
            		predicates.add(cb.equal(ocorrencia.get(filtro.getKey()), tipo));
            	}
            	else {
            		predicates.add(cb.equal(ocorrencia.get(filtro.getKey()), meta.getFilterValue()));
            	}*/
            }
        }
        cq.where(predicates.toArray(new Predicate[0]));

        // Aplicar ordenação
        List<Order> orders = new ArrayList<>();
        sortBy.forEach((field, sortMeta) -> {
            if (sortMeta.getOrder() == SortOrder.ASCENDING) {
                orders.add(cb.asc(ocorrencia.get(field)));
            } else if (sortMeta.getOrder() == SortOrder.DESCENDING) {
                orders.add(cb.desc(ocorrencia.get(field)));
            }
        });
        cq.orderBy(orders);

        // Executar consulta com paginação
        TypedQuery<Ocorrencia> query = manager.createQuery(cq);
        query.setFirstResult(first);
        query.setMaxResults(pageSize);

        return query.getResultList();
        
    }

    public int contarOcorrencias(Map<String, FilterMeta> filterBy) {
        CriteriaBuilder cb = manager.getCriteriaBuilder();
        CriteriaQuery<Long> cq = cb.createQuery(Long.class);
        Root<Ocorrencia> ocorrencia = cq.from(Ocorrencia.class);

        // Aplicar filtros
        List<Predicate> predicates = new ArrayList<>();
        filterBy.forEach((field, filterMeta) -> {
            Object filterValue = filterMeta.getFilterValue();
            if ("descricao".equals(field) && filterValue != null) {
                predicates.add(cb.like(ocorrencia.get("descricao"), "%" + filterValue + "%"));
            } else if ("codigo".equals(field) && filterValue != null){
            	predicates.add(cb.equal(ocorrencia.get("codigo"), filterValue));
            } else if ("usuario".equals(field) && filterValue != null) {
                predicates.add(cb.equal(ocorrencia.get("usuario").get("codigo"), filterValue));
            }
        });
        cq.where(predicates.toArray(new Predicate[0]));
        cq.select(cb.count(ocorrencia));

        return manager.createQuery(cq).getSingleResult().intValue();
    }

	public void setEntityManager(EntityManager manager) {
		this.manager = manager;
	}

	public void teste() {
		System.out.println("teste");
	}
	
}
