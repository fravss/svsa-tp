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
public class RespostaOcorrenciaBean implements Serializable {

	private static final long serialVersionUID = 1L;

	private Ocorrencia ocorrencia;
	private List<Ocorrencia> ocorrencias;
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
		this.buscarPendencias();
	}

	public void carregarOcorrencia() {

		this.respostas = this.ocorrenciaService.buscarTodasRespostas(this.ocorrencia);
	}

	public void salvarResposta() {
		this.novaResposta.setOcorrencia(ocorrencia);
		this.novaResposta.setUsuario(this.usuarioLogado);
		this.ocorrenciaService.novaResposta(novaResposta);


		PrimeFaces.current().ajax().update("ocorrenciaDetalhes");
		PrimeFaces.current().executeScript("PF('respostaDialog').hide(); PF('ocorrenciaDialog').show();");

		this.carregarOcorrencia();
	    this.limpar();

	    PrimeFaces.current().ajax().update("frmPesquisa:pendenciasTable");
	}
	
	public void buscarPendencias() {
		this.ocorrencias = this.ocorrenciaService.buscarTodasPendencias(usuarioLogado);
		
		System.out.println("O user: "+ usuarioLogado.getNome());
		System.out.println("A unidade: "+ usuarioLogado.getUnidade());
		System.out.println("O Grupo: "+ usuarioLogado.getGrupo());

	}

	public void limpar() {
		this.novaResposta = new Resposta();
	}

}