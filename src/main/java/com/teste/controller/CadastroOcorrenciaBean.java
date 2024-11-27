package com.teste.controller;

import java.io.Serializable;
import java.sql.SQLIntegrityConstraintViolationException;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.enterprise.context.SessionScoped;
import javax.inject.Inject;
import javax.inject.Named;

import com.teste.dao.lazy.LazyUsuario;
import com.teste.model.Ocorrencia;
import com.teste.model.Usuario;
import com.teste.model.enums.StatusOcorrencia;
import com.teste.model.enums.TipoOcorrencia;
import com.teste.service.OcorrenciaService;
import com.teste.service.UsuarioService;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.log4j.Log4j;

@Log4j
@Getter
@Setter
@Named
@SessionScoped
public class CadastroOcorrenciaBean implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private LazyUsuario lazyUsuario;
	

	private Usuario destinatario;
	private Usuario testemunha;
	private TipoOcorrencia tipo;
	private String descricao;
	
	private Ocorrencia ocorrencia;
	
	@Inject
	private OcorrenciaService service;
	@Inject
	private UsuarioService usuarioService;
	@Inject
	private AutenticacaoBean autenticacao;
	
	@PostConstruct
    public void init() {
		log.info("Bean CadastroOcorrenciaBean inicializado.");
    	this.lazyUsuario = new LazyUsuario(this.usuarioService);
    	this.ocorrencia = new Ocorrencia();
    	this.destinatario = new Usuario();
    	this.testemunha = new Usuario();
    	this.tipo = TipoOcorrencia.REUNIAO;
    	this.descricao = "";
    }
	
	
	public void criarOcorrencia() throws SQLIntegrityConstraintViolationException {
		
		log.info("Destinatário: " + this.destinatario);
	    log.info("Testemunha: " + this.testemunha);
	    log.info("Tipo: " + this.tipo);
	    log.info("Descrição: " + this.descricao);
	
		this.ocorrencia.setDestinatario(this.destinatario);
		this.ocorrencia.setTestemunha(this.testemunha);
		this.ocorrencia.setTipo(this.tipo);
		this.ocorrencia.setDescricao(this.descricao);
		this.ocorrencia.setStatus(StatusOcorrencia.COORDENADOR);
		this.ocorrencia.setDataCriacao(new Date());
		
		log.info(this.ocorrencia);
		
		log.info("Destinatário: " + this.destinatario);
	    log.info("Testemunha: " + this.testemunha);
	    log.info("Tipo: " + this.tipo);
	    log.info("Descrição: " + this.descricao);
		
		this.ocorrencia.setRemetente(this.autenticacao.getUsuarioAutenticado());
		
		try {
			this.service.salvar(this.ocorrencia);
		}
		catch (Exception e) {
			log.warn(e.getMessage());
		}
		
		
	}
	
	public void onTipoChange() {
	    // Aqui você pode fazer algo, como validar ou ajustar outros campos
	    System.out.println("Tipo selecionado: " + this.tipo);
	    // Ou adicionar algum outro comportamento desejado
	}

	
	
	public List<TipoOcorrencia> getListaTipos() {
	    return Arrays.asList(TipoOcorrencia.values());
	}

	
	
	
	
}
