package gaian.svsa.ep.dao;

import java.io.Serializable;
import java.util.List;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;

import gaian.svsa.ep.model.Ocorrencia;
import gaian.svsa.ep.model.Resposta;
import gaian.svsa.ep.util.jpa.Transactional;
import lombok.extern.log4j.Log4j;

@Log4j
public class RespostaDAO implements Serializable {

	private static final long serialVersionUID = 1L;

	@Inject
	private EntityManager manager;

	public List<Resposta> buscarTodasPorOcorrencia(Ocorrencia ocorrencia) throws NoResultException {
		try {
			return manager.createNamedQuery("Resposta.findByOcorrencia", Resposta.class)
					.setParameter("codigoOcorrencia", ocorrencia.getCodigo()).getResultList();
		} catch (NoResultException e) {
			log.warn("Nenhuma resposta encontrada para a ocorrência com código: " + ocorrencia.getCodigo());
			return null;
		}

	}

	@Transactional
	public void salvar(Resposta resposta) throws NoResultException {
		try {
			manager.getTransaction().begin();
			Resposta managedResposta = manager.merge(resposta);
			manager.getTransaction().commit();
            manager.flush();
		} catch (NoResultException e) {
			log.warn("Nenhuma resposta encontrada para a ocorrência com código: " + e.getMessage());
		} catch (Exception e) {
			log.warn(e.getMessage());
		}
	}

}
