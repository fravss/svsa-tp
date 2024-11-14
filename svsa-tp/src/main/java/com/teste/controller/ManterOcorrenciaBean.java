package com.teste.controller;

import java.io.Serializable;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

import com.teste.dao.lazy.LazyOcorrencia;
import com.teste.model.Ocorrencia;
import com.teste.service.OcorrenciaService;

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
	
	private Long codigoBusca;
	
	private LazyOcorrencia lazyOcorrencias;
    private List<Ocorrencia> ocorrencias;
    
    @Inject
    private OcorrenciaService ocorrenciaService;
    
    @PostConstruct
    public void init() {
    	
    	this.lazyOcorrencias = new LazyOcorrencia(this.ocorrenciaService);
        log.info("ManterOcorrenciaBean Inicializado com LazyOcorrencias: " + (this.lazyOcorrencias != null));
    	
    }
    
    
    public String navegarParaOcorrencias() {
        return "/restrito/ocorrencia/ManterOcorrencia.xhtml?faces-redirect=true";
    }
    
	
}
