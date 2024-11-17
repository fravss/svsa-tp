package com.teste.service;

import java.io.Serializable;
import java.util.List;

import javax.inject.Inject;

import com.teste.dao.OcorrenciaDAO;
import com.teste.dao.RespostaDAO;
import com.teste.model.Ocorrencia;
import com.teste.model.Resposta;
import com.teste.model.UsuarioEP;
import com.teste.model.enums.GrupoEP;
import com.teste.model.enums.StatusOcorrencia;
import com.teste.util.jpa.Transactional;

import lombok.extern.log4j.Log4j;

@Log4j
public class OcorrenciaService implements Serializable {

	private static final long serialVersionUID = 1L;
	
	@Inject
	private OcorrenciaDAO ocorrenciaDAO;
	
	@Inject
	private RespostaDAO respostaDAO;
	
	public Ocorrencia buscarPorId(Long id) {

		log.info("Buscando ocorrencia por Id");

		return this.ocorrenciaDAO.buscarPeloCodigo(id);
	}
	
	public List<Resposta> buscarTodasRespostas(Ocorrencia ocorrencia) {
		
		return this.respostaDAO.buscarTodasPorOcorrencia(ocorrencia);
		
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
	        } else if (resposta.getUsuario().getGrupo() == GrupoEP.GESTORES && ocorrencia.getStatus() == StatusOcorrencia.GESTOR) {
	            ocorrencia.setStatus(StatusOcorrencia.FECHADO);
	            log.info("Gestor fechando ocorrência: " + ocorrencia);
	            this.ocorrenciaDAO.salvar(ocorrencia);
	        }
	    } catch (Exception e) {
	        log.error("Erro", e);

	    }
	}
		
	}
	