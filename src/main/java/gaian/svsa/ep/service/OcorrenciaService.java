package gaian.svsa.ep.service;

import java.io.Serializable;


import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;


import java.util.Set;

import javax.enterprise.context.spi.CreationalContext;
import javax.enterprise.inject.spi.Bean;
import javax.enterprise.inject.spi.BeanManager;
import javax.naming.InitialContext;
import javax.naming.NamingException;


import java.util.Date;
import java.util.List;

import gaian.svsa.ep.dao.OcorrenciaDAO;
import gaian.svsa.ep.dao.RespostaDAO;
import gaian.svsa.ep.model.Ocorrencia;
import gaian.svsa.ep.model.Resposta;
import gaian.svsa.ep.model.UnidadeEP;
import gaian.svsa.ep.model.UsuarioEP;
import gaian.svsa.ep.model.enums.GrupoEP;
import gaian.svsa.ep.model.enums.StatusOcorrencia;
import gaian.svsa.ep.model.enums.TipoOcorrencia;
import gaian.svsa.ep.util.jpa.Transactional;
import lombok.extern.log4j.Log4j;


@Log4j
@ApplicationScoped
public class OcorrenciaService implements Serializable {

	private static final long serialVersionUID = 1L;
	
	@Inject
	private OcorrenciaDAO ocorrenciaDAO;
	
	@Inject
	private RespostaDAO respostaDAO;
	
	
	@Transactional
	public void salvar(Ocorrencia ocorrencia) {
		this.ocorrenciaDAO.salvar(ocorrencia);
	}
		
	public Ocorrencia buscarPorId(Long id) {

		log.info("Buscando usuario por Id");

		return this.ocorrenciaDAO.buscarPeloCodigo(id);
	}
	
	public OcorrenciaDAO getOcorrenciaDAO() {
		return this.ocorrenciaDAO;
	}
	
	public List<Resposta> buscarTodasRespostas(Ocorrencia ocorrencia) {
		
		return this.respostaDAO.buscarTodasPorOcorrencia(ocorrencia);
		
	}

	public Ocorrencia buscarRemetente(Ocorrencia ocorrencia,UsuarioEP usuario) {
		return this.ocorrenciaDAO.buscarRemetente(ocorrencia, usuario);
	}
	
	public List<Ocorrencia> buscarTodasPendencias (UsuarioEP usuario) {
		return this.ocorrenciaDAO.buscarPendenciasPorUsuario(usuario);
	}
	
	@Transactional
	public void novaResposta(Resposta resposta) {
	    try {
	        this.respostaDAO.salvar(resposta);

	        Ocorrencia ocorrencia = resposta.getOcorrencia();
	        if (resposta.getUsuario().getGrupo() == GrupoEP.COORDENADORES && ocorrencia.getStatus() == StatusOcorrencia.COORDENADOR) {
	            ocorrencia.setStatus(StatusOcorrencia.GESTOR);
	            log.info("Coordenador criando nova ocorrência: " + ocorrencia);
	            this.ocorrenciaDAO.salvar(ocorrencia);
	        }
	    } catch (Exception e) {
	        log.error("Erro", e);
        }
    }

	private static BeanManager getBeanManager() {
		try {
			InitialContext initialContext = new InitialContext();
			return (BeanManager) initialContext.lookup("java:comp/env/BeanManager");
		} catch (NamingException e) {
			throw new RuntimeException("Não pôde encontrar BeanManager no JNDI.");
		}
	}
	
	@SuppressWarnings("unchecked")
	public static <T> T getBean(Class<T> clazz) {
		BeanManager bm = getBeanManager();
		Set<Bean<?>> beans = (Set<Bean<?>>) bm.getBeans(clazz);

		if (beans == null || beans.isEmpty()) {
			return null;
		}

		Bean<T> bean = (Bean<T>) beans.iterator().next();

		CreationalContext<T> ctx = bm.createCreationalContext(bean);
		T o = (T) bm.getReference(bean, clazz, ctx);

		return o;
	}
  	public List<Ocorrencia> buscarOcorrenciasGestor(Date ini,Date fim){
		if(ini!=null)
			if(fim != null)
				return ocorrenciaDAO.buscarOcorrenciasGestor(ini, fim);
			else 
				return ocorrenciaDAO.buscarOcorrenciasGestor(ini, new Date());
		return ocorrenciaDAO.buscarTodos();
	}

	public List<Ocorrencia> buscarOcorrenciaStatus(UnidadeEP unidade, Date ini, Date fim) {
	
		if(ini != null)
			if(fim != null)
				return ocorrenciaDAO.buscarOcorrenciaStatus(unidade, ini, fim);
			else
				return ocorrenciaDAO.buscarOcorrenciaStatus(unidade, ini, new Date());
		return ocorrenciaDAO.buscarOcorrenciaStatus(unidade);
	}

	public List<Ocorrencia> buscarOcorrenciasGestorStatus(Date ini, Date fim, TipoOcorrencia tipo) {
		if(ini!=null)
			if(fim != null)
				return ocorrenciaDAO.buscarOcorrenciasGestorStatus(ini, fim,tipo);
			else 
				return ocorrenciaDAO.buscarOcorrenciasGestor(ini, fim);
		return ocorrenciaDAO.buscarTodos();

	}
	
	
}