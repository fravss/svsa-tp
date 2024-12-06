package gaian.svsa.ep.dao;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.Order;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.primefaces.model.FilterMeta;
import org.primefaces.model.SortMeta;
import org.primefaces.model.SortOrder;

import gaian.svsa.ep.model.Ocorrencia;
import gaian.svsa.ep.model.UsuarioEP;
import gaian.svsa.ep.model.enums.GrupoEP;
import gaian.svsa.ep.model.enums.StatusOcorrencia;
import gaian.svsa.ep.util.jpa.Transactional;
import lombok.extern.log4j.Log4j;



import java.util.Date;

import javax.persistence.TemporalType;

import gaian.svsa.ep.model.UnidadeEP;

import gaian.svsa.ep.model.enums.TipoOcorrencia;
import gaian.svsa.ep.util.DateUtils;


@Log4j
public class OcorrenciaDAO implements Serializable{
	
	private static final long serialVersionUID = 1L;
	
	@Inject
	private EntityManager manager;
	
	private List<Ocorrencia> resultado;
	
	@Transactional
	public void salvar(Ocorrencia ocorrencia) throws NoResultException {
		try {
			manager.getTransaction().begin();
			Ocorrencia managedOcorrencia = manager.merge(ocorrencia);
			manager.getTransaction().commit();
            manager.flush();
		} catch (NoResultException e) {
			log.warn("Nenhuma resposta encontrada para a ocorrência com código: " + e.getMessage());
		} catch (Exception e) {
			log.warn(e.getMessage());
		}
	}

	

	public Ocorrencia buscarPeloCodigo(Long id) {
		//log.info("Buscando usuario pelo id " + id);
		return manager.find(Ocorrencia.class, id);
	}
	
	@SuppressWarnings("unchecked")
	public List<Ocorrencia> buscarTodos() {
		return manager.createNamedQuery("Ocorrencia.buscarTodos").getResultList();
	}
	
	public List<Ocorrencia> buscarPendenciasPorUsuario(UsuarioEP usuario) throws NoResultException {
	    try {
	        return manager.createNamedQuery("Ocorrencia.buscarPendencias", Ocorrencia.class)
	                .setParameter("usuario", usuario)  
	                .setParameter("unidade", usuario.getUnidade())
	                .setParameter("grupo", usuario.getGrupo().toString())
	                .getResultList();
	    } catch (NoResultException e) {
	        log.warn("Nenhuma pendência encontrada para o usuário: " + usuario.getNome());
	        return null;
	    }
	}
	
	
	
	public List<Ocorrencia> buscarOcorrencias(int first, int pageSize, Map<String, SortMeta> sortBy,
			Map<String, FilterMeta> filterBy) {
        
		CriteriaBuilder cb = manager.getCriteriaBuilder();
        CriteriaQuery<Ocorrencia> cq = cb.createQuery(Ocorrencia.class);
        Root<Ocorrencia> ocorrencia = cq.from(Ocorrencia.class);

        Join<Ocorrencia, UsuarioEP> remetente = ocorrencia.join("remetente");
        
        // Aplicar filtros
        
        List<Predicate> predicates = new ArrayList<>();
        
        //FILTRAR POR TENANT
        if(filterBy.get("tenant").getFilterValue() != null)
        	predicates.add(cb.equal(remetente.get("tenant"), filterBy.get("tenant").getFilterValue()));
        
        //FILTRAR POR REMETENTE, DESTINATARIO OU TESTEMUNHA
        if(filterBy.get("usuario").getFilterValue() != null) {
	        Predicate usuarioPredicate = cb.or(
	                cb.equal(ocorrencia.get("remetente"), filterBy.get("usuario").getFilterValue()),
	                cb.equal(ocorrencia.get("destinatario"), filterBy.get("usuario").getFilterValue()) //,
	                //cb.equal(ocorrencia.get("testemunha"), filterBy.get("usuario").getFilterValue())
	            );
	        //predicates.add(usuarioPredicate);
        
        
        // FILTROS POR GRUPO
        if(filterBy.get("grupo").getFilterValue() != null) {
	        GrupoEP grupo = (GrupoEP) filterBy.get("grupo").getFilterValue();
	        switch (grupo) {
			case GESTORES: {
				predicates.add(cb.or(
						cb.equal(ocorrencia.get("status"), StatusOcorrencia.GESTOR),
						usuarioPredicate));
				//predicates.add(cb.equal(ocorrencia.get("status"), StatusOcorrencia.GESTOR));
	            break;
			}
			case COORDENADORES:
				Predicate unidadePredicate = cb.equal(ocorrencia.get("unidade"), filterBy.get("unidade").getFilterValue());
	            Predicate statusPredicate = cb.or(
	                cb.equal(ocorrencia.get("status"), StatusOcorrencia.COORDENADOR),
	                cb.equal(ocorrencia.get("status"), StatusOcorrencia.GESTOR)
	            );
	            predicates.add(cb.or(
	            		cb.and(unidadePredicate, statusPredicate),
	            		usuarioPredicate));
	            break;
			default:
				predicates.add(usuarioPredicate);
				break;
			}
	        filterBy.remove("grupo");
        }
        }
        
        
        // *************************************************
        // ADICIONAR A LOGICA PARA FILTRAR BASEADO EM FUNCAO
        // FILTRO TECNICO
        // FILTRO COORDENADOR
        // GESTOR NAO PRECISA
        // *************************************************
        
        for (Map.Entry<String, FilterMeta> filtro : filterBy.entrySet()) {
            FilterMeta meta = filtro.getValue();
            if (meta.getFilterValue() != null) {
            	
            	if ("descricao".equals(filtro.getKey())) {
            		String descricaoFiltro = meta.getFilterValue().toString().toLowerCase();
            		predicates.add(cb.like(cb.lower(ocorrencia.get(filtro.getKey())), "%" + descricaoFiltro + "%"));
            	} else if ("usuario".equals(filtro.getKey()) || "grupo".equals(filtro.getKey())) {
            		// PREVECAO DE FILTRO POR ATRIBUTOS NAO EXISTENTES NA CLASSE
            	} else if ("tenant".equals(filtro.getKey()) || "unidade".equals(filtro.getKey())) {
            		
            	}
            	else {
            		predicates.add(cb.equal(ocorrencia.get(filtro.getKey()), meta.getFilterValue()));
            	}
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
        
        resultado = query.getResultList();
        return resultado;
        
    }

	public Ocorrencia buscarRemetente(Ocorrencia buscaOcorrencia, UsuarioEP usuario) {
		CriteriaBuilder cb = manager.getCriteriaBuilder();
        CriteriaQuery<Ocorrencia> cq = cb.createQuery(Ocorrencia.class);
        Root<Ocorrencia> ocorrencia = cq.from(Ocorrencia.class);
   
        List<Predicate> predicates = new ArrayList<>();
		predicates.add(cb.equal(ocorrencia.get("remetente"), usuario));
		predicates.add(cb.equal(ocorrencia.get("codigo"), buscaOcorrencia));
		cq.where(predicates.toArray(new Predicate[0]));

		TypedQuery<Ocorrencia> query = manager.createQuery(cq);
        
        return query.getSingleResult();

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
            }
        });
        cq.where(predicates.toArray(new Predicate[0]));
        cq.select(cb.count(ocorrencia));

        return manager.createQuery(cq).getSingleResult().intValue();
    	//return resultado.size();
        
    }
    public List<Ocorrencia> buscarOcorrenciasGestor(Date ini, Date fim){
		return manager
				.createNamedQuery("Ocorrencia.buscarOcorrenciasGestor",Ocorrencia.class)
				.setParameter("ini",ini,TemporalType.TIMESTAMP)
				.setParameter("fim",DateUtils.plusDay(fim),TemporalType.TIMESTAMP).getResultList();
	}
public List<Ocorrencia> buscarOcorrenciaStatus(UnidadeEP unidade, Date ini, Date fim) {
		
		return manager
				.createNamedQuery("Ocorrencia.buscarOcorrenciaStatusPeriodo", Ocorrencia.class)
				.setParameter("unidade", unidade)
				.setParameter("ini", ini, TemporalType.TIMESTAMP)
				.setParameter("fim", DateUtils.plusDay(fim), TemporalType.TIMESTAMP).getResultList();
	
	}
public List<Ocorrencia> buscarOcorrenciasGestorStatus(Date ini, Date fim, TipoOcorrencia tipo) {
	return manager
			.createNamedQuery("Ocorrencia.buscarOcorrenciasGestorStatus",Ocorrencia.class)
			.setParameter("ini",ini,TemporalType.TIMESTAMP)
			.setParameter("fim",DateUtils.plusDay(fim),TemporalType.TIMESTAMP)
			.setParameter("tipo", tipo).getResultList();
			
}
public List<Ocorrencia> buscarOcorrenciaStatus(UnidadeEP unidade) {
	
	return manager.createNamedQuery("Ocorrencia.buscarOcorrenciaStatus", Ocorrencia.class)
			.setParameter("unidade", unidade).getResultList();

}
    
	
}

