package com.teste.controller;

import java.io.Serializable;
import java.sql.SQLIntegrityConstraintViolationException;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.enterprise.context.RequestScoped;
import javax.enterprise.context.SessionScoped;
import javax.inject.Inject;
import javax.inject.Named;

import com.teste.dao.lazy.LazyUsuario;
import com.teste.model.Ocorrencia;
import com.teste.model.UsuarioEP;
import com.teste.model.enums.StatusOcorrencia;
import com.teste.model.enums.TipoOcorrencia;
import com.teste.service.OcorrenciaService;
import com.teste.service.UnidadeService;
import com.teste.service.UsuarioService;

import gaian.svsa.ct.modelo.Unidade;

import com.teste.model.enums.UnidadePainel;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.log4j.Log4j;

@Log4j
@Getter
@Setter
@Named
@SessionScoped
public class PainelBean  implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	//private LazyUsuario lazyUsuario;
	private LazyUsuario lazyUsuario;
	
	@RequestScoped
	private List<Unidade> unidades;
	
	
	@Inject
	private UsuarioService usuarioService;
	@Inject
	private AutenticacaoBean autenticacao;
	@Inject
	private UnidadeService unidadeService;
	
	@PostConstruct
    public void init() {
		log.info("Bean CadastroOcorrenciaBean inicializado.");
    	//this.lazyUsuario = new LazyUsuario(this.usuarioService);
		this.lazyUsuario = new LazyUsuario(this.usuarioService);
		this.unidades = this.unidadeService.listarUnidades();
    }
	
	public List<UnidadePainel> getListaTipos() {
		    return Arrays.asList(UnidadePainel.values());
		}
	
	 public String navegarParaPainel() {
	        return "/restrito/Painel/PainelFuncionarios.xhtml?faces-redirect=true";
	    }
	    
	
}
