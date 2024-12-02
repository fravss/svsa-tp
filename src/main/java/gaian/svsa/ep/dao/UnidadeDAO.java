package gaian.svsa.ep.dao;

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

import gaian.svsa.ep.model.UnidadeEP;
import gaian.svsa.ep.model.UsuarioEP;
import lombok.extern.log4j.Log4j;

@Log4j
public class UnidadeDAO implements Serializable {

	private static final long serialVersionUID = 1L;

	@Inject
	private EntityManager manager;
	
	public List<UnidadeEP> listarUnidades(UsuarioEP usuario) {
		
		CriteriaBuilder cb = manager.getCriteriaBuilder();
        CriteriaQuery<UnidadeEP> cq = cb.createQuery(UnidadeEP.class);
        Root<UnidadeEP> unidade = cq.from(UnidadeEP.class);
        
        List<Predicate> predicates = new ArrayList<>();
        predicates.add(cb.equal(unidade.get("tenant"), usuario.getTenant()));
        
        cq.where(predicates.toArray(new Predicate[0]));
        TypedQuery<UnidadeEP> query = manager.createQuery(cq);

        return query.getResultList();
		
	}

}
