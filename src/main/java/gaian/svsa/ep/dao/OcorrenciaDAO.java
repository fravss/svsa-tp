package gaian.svsa.ep.dao;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.TemporalType;

import gaian.svsa.ep.model.Ocorrencia;
import gaian.svsa.ep.model.UnidadeEP;
import gaian.svsa.ep.model.UsuarioEP;
import gaian.svsa.ep.model.enums.StatusOcorrencia;
import gaian.svsa.ep.model.enums.TipoOcorrencia;
import gaian.svsa.ep.util.DateUtils;
import gaian.svsa.ep.util.jpa.Transactional;
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
	                .setParameter("unidade", usuario.getUnidade().getCodigo())
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
	
	public List<Ocorrencia> buscarOcorrenciasGestor(Date ini, Date fim){
		return manager
				.createNamedQuery("Ocorrencia.buscarOcorrenciasGestor",Ocorrencia.class)
				.setParameter("ini",ini,TemporalType.TIMESTAMP)
				.setParameter("fim",DateUtils.plusDay(fim),TemporalType.TIMESTAMP).getResultList();
	}
	
	@SuppressWarnings("unchecked")
	public List<Ocorrencia> buscarTodos() {
		return manager.createNamedQuery("Ocorrencia.buscarTodos").getResultList();
	}

	public List<Ocorrencia> buscarOcorrenciaStatus(Long unidade, Date ini, Date fim, Long tenant_id) {
		
		return manager
				.createNamedQuery("Ocorrencia.buscarOcorrenciaStatusPeriodo", Ocorrencia.class)
				.setParameter("unidade", unidade).setParameter("tenant_id", tenant_id)
				.setParameter("ini", ini, TemporalType.TIMESTAMP)
				.setParameter("fim", DateUtils.plusDay(fim), TemporalType.TIMESTAMP)
				.setParameter("status", StatusOcorrencia.GESTOR).getResultList();
	
	}
	
	public List<Ocorrencia> buscarOcorrenciaStatus(Long unidade, Long tenant_id) {
		
		return manager.createNamedQuery("Ocorrencia.buscarOcorrenciaStatus", Ocorrencia.class)
				.setParameter("unidade", unidade).setParameter("tenantId", tenant_id)
				.setParameter("status", StatusOcorrencia.GESTOR).getResultList();
	
	}

	public List<Ocorrencia> buscarOcorrenciasGestorStatus(Date ini, Date fim, TipoOcorrencia tipo) {
		return manager
				.createNamedQuery("Ocorrencia.buscarOcorrenciasGestorStatus",Ocorrencia.class)
				.setParameter("ini",ini,TemporalType.TIMESTAMP)
				.setParameter("fim",DateUtils.plusDay(fim),TemporalType.TIMESTAMP)
				.setParameter("tipo", tipo).getResultList();
				
	}
	

}
