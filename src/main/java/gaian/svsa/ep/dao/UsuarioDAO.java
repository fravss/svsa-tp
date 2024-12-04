package gaian.svsa.ep.dao;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Order;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.primefaces.model.FilterMeta;
import org.primefaces.model.SortMeta;
import org.primefaces.model.SortOrder;

import gaian.svsa.ep.model.Ocorrencia;
import gaian.svsa.ep.model.UsuarioEP;
import gaian.svsa.ep.util.jpa.Transactional;
import lombok.extern.log4j.Log4j;

@Log4j
public class UsuarioDAO implements Serializable {

	private static final long serialVersionUID = 1L;

	@Inject
	@PersistenceContext
	private EntityManager manager;
	

	
	@Transactional
	public void salvar(UsuarioEP usuario) throws Exception {
		
		try {
            
			manager.getTransaction().begin();
			UsuarioEP managedUsuario = manager.merge(usuario);
            manager.getTransaction().commit();
            manager.flush();
            
        } catch (Exception e) {
                log.warn(e.getMessage());
            
            throw new RuntimeException("Erro ao salvar", e);
        }
		
	}	
	

	public UsuarioEP buscarPeloCodigo(Long id) {
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
        });
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
	}

}
