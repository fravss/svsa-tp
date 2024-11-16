package com.teste.controller;


import java.io.Serializable;

import java.util.List;

import org.primefaces.PrimeFaces;

import javax.annotation.PostConstruct;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

import com.teste.model.Ocorrencia;
import com.teste.model.Resposta;
import com.teste.model.UsuarioEP;
import com.teste.service.OcorrenciaService;

import lombok.*;
import lombok.extern.log4j.Log4j;

@Getter
@Setter
@Log4j
@Named
@ViewScoped
public class PesquisaOcorrenciaBean implements Serializable {

	private static final long serialVersionUID = 1L;

	private Ocorrencia ocorrencia;
	private UsuarioEP usuarioLogado;
	private List<Resposta> respostas;
	private Resposta novaResposta = new Resposta();

	@Inject
	private OcorrenciaService ocorrenciaService;
	@Inject
	private AutenticacaoBean autenticacaoBean;
	
	@PostConstruct
	public void inicializar() {
		this.usuarioLogado = this.autenticacaoBean.getUsuario();
	}

	public void carregarOcorrencia() {
		this.ocorrencia = ocorrenciaService.buscarPorId(Long.valueOf(1));
		this.respostas = this.ocorrenciaService.buscarTodasRespostas(Long.valueOf(1));
	}

	public void salvarResposta() {
		this.novaResposta.setOcorrencia(ocorrencia);
		this.novaResposta.setUsuario(this.usuarioLogado);
		this.ocorrenciaService.novaResposta(novaResposta);

		PrimeFaces.current().ajax().update("ocorrenciaDetalhes");
		PrimeFaces.current().executeScript("PF('respostaDialog').hide(); PF('ocorrenciaDialog').show();");

		this.carregarOcorrencia();
		this.limpar();
	}

	public void limpar() {
		this.novaResposta = new Resposta();
	}

}