package com.teste.controller;

import java.io.Serializable;
import java.util.Arrays;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

import com.teste.dao.lazy.LazyOcorrencia;
import com.teste.model.Ocorrencia;
import com.teste.model.UsuarioEP;
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
@ViewScoped
public class ManterOcorrenciaBean implements Serializable{
	    
	private static final long serialVersionUID = 1L;
	
	private LazyOcorrencia lazyOcorrencias;
    private List<Ocorrencia> ocorrencias;
    
    private UsuarioEP usuario;
    @Inject
    private UsuarioService usuarioService;
    
    @Inject
    private OcorrenciaService ocorrenciaService;
    
    @PostConstruct
    public void init() {
    	this.usuario = this.usuarioService.getUsuarioAutenticado();
    	this.lazyOcorrencias = new LazyOcorrencia(this.ocorrenciaService, this.usuario);
    }
    
    
    public List<TipoOcorrencia> getListaTipos() {
	    return Arrays.asList(TipoOcorrencia.values());
	}
    
    public String navegarParaOcorrencias() {
        return "/restrito/ocorrencia/ManterOcorrencia.xhtml?faces-redirect=true";
    }
    
    public Boolean isRascunho(Ocorrencia ocorrencia) {
    	return ocorrencia.getStatus() == StatusOcorrencia.ABERTO;
    }
    
	
}
