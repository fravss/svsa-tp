package com.teste.service;

import java.io.Serializable;
import java.util.List;

import javax.inject.Inject;

import com.teste.dao.OcorrenciaDAO;
import com.teste.dao.RespostaDAO;
import com.teste.model.Ocorrencia;
import com.teste.model.Resposta;
import com.teste.model.enums.GrupoEP;

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
	
	public List<Resposta> buscarTodasRespostas(Long id) {
		
		return this.respostaDAO.buscarTodasPorOcorrenciaId(id);
		
	}
	
	public void novaResposta (Resposta resposta) {
		this.respostaDAO.salvar(resposta);
		
		if(resposta.getUsuario().getGrupo() == GrupoEP.COORDENADORES) {
			// mudar a ocorrencia para GESTORES
		} else if (resposta.getUsuario().getGrupo() == GrupoEP.GESTORES) {
			//mudar para finalizada
		}
		
	}
	

}
