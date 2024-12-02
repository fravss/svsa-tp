package com.teste.controller;

import java.io.Serializable;
import java.sql.SQLIntegrityConstraintViolationException;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.enterprise.context.RequestScoped;
import javax.enterprise.context.SessionScoped;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

import com.teste.dao.lazy.LazyUsuario;
import com.teste.model.Ocorrencia;
import com.teste.model.UnidadeEP;
import com.teste.model.UsuarioEP;
import com.teste.model.enums.StatusOcorrencia;
import com.teste.model.enums.TipoOcorrencia;
import com.teste.service.OcorrenciaService;
import com.teste.service.UnidadeService;
import com.teste.service.UsuarioService;


import com.teste.model.enums.UnidadePainel;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.log4j.Log4j;
import net.bytebuddy.asm.Advice.This;

@Log4j
@Getter
@Setter
@Named
@ViewScoped
public class PainelBean  implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private UsuarioEP usuario;
	private Boolean estagioProbatorio;
	
	
	private LazyUsuario lazyUsuario;
	
	private List<UnidadeEP> unidades;
	
	
	@Inject
	private UsuarioService usuarioService;

	@Inject
	private UnidadeService unidadeService;
	
	
	@PostConstruct
    public void init() {
		this.lazyUsuario = new LazyUsuario(this.usuarioService);
		this.usuario = new UsuarioEP();
    }
	
	 public String navegarParaPainel() {
	        return "/restrito/painel/PainelFuncionarios.xhtml?faces-redirect=true";
	    }
	 
	 public List<UnidadeEP> getListarUnidades() {
		try {
			 return this.unidadeService.listarUnidades();
		} catch (Exception e) {
			log.info(e.getMessage());
		}
		return unidades;
	}
	 
	 public void prepararEdicao(UsuarioEP usuario) {
		 
		 try {
			 this.usuario = usuario;
			 this.estagioProbatorio = this.usuario.getEstagioProbatorio();
		 } catch (Exception e) {
			log.warn("ERRO PREPARAR EDICAO");
			log.warn(this.usuario);
		}
		 
	 }

	 public void alterarEP() {
		 try {
			 log.info("alterarEP executado");
			 this.usuario.setEstagioProbatorio(this.estagioProbatorio);
			 this.usuarioService.salvar(this.usuario);
		} catch (SQLIntegrityConstraintViolationException e) {
			e.printStackTrace();
			log.warn(e.getMessage());
		}
	 }
	    
	
}
