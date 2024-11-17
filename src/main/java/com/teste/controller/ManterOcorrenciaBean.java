package com.teste.controller;

import java.io.Serializable;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import javax.annotation.PostConstruct;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

import com.teste.dao.lazy.LazyOcorrencia;
import com.teste.model.Ocorrencia;
import com.teste.model.enums.TipoOcorrencia;
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
	
	private LazyOcorrencia lazyOcorrencias;
    private List<Ocorrencia> ocorrencias;
    
    
    // FILTROS DE BUSCA
    private String colunaSelecionada;
    private String valorFiltro;
    private String valorTipo;
    
    @Inject
    private OcorrenciaService ocorrenciaService;
    
    @PostConstruct
    public void init() {
    	this.lazyOcorrencias = new LazyOcorrencia(this.ocorrenciaService);
    }
    
    public void atualizarFiltro() {
    	
    	log.info("Coluna: " + this.colunaSelecionada);
    	log.info("Tipo: " + this.valorTipo);
    	log.info("BUSCA: " + this.valorFiltro);
    	
    	if (this.colunaSelecionada == null) {
            log.warn("Coluna selecionada está nula.");
            return;
        }

        this.lazyOcorrencias.setColunaSelecionada(this.colunaSelecionada);

        if ("tipo".equals(this.colunaSelecionada)) {
            if (this.valorTipo != null) {
                this.lazyOcorrencias.setValorFiltro(this.valorTipo);
            } else {
                log.warn("Filtro para 'tipo' está nulo.");
            }
        } else {
            if (this.valorFiltro != null && !this.valorFiltro.isEmpty()) {
                this.lazyOcorrencias.setValorFiltro(this.valorFiltro);
            } else {
                log.warn("Filtro padrão está vazio ou nulo.");
            }
        }

        log.info("Filtro atualizado: Coluna=" + this.colunaSelecionada + ", Filtro=" + this.lazyOcorrencias.getValorFiltro());
        
    }
    
    public void limparFiltro() {
    	this.valorTipo = null;
    	this.valorFiltro = null;
    	this.lazyOcorrencias.setColunaSelecionada(null);
        this.lazyOcorrencias.setValorFiltro(null);
    }
    
    public List<String> getTiposOcorrencia() {
        return Arrays.stream(TipoOcorrencia.values()) // Transforma as constantes em um Stream
                     .map(Enum::name)                // Mapeia cada constante para seu nome (String)
                     .toList();                      // Coleta como uma lista
    }
    public String navegarParaOcorrencias() {
        return "/restrito/ocorrencia/ManterOcorrencia.xhtml?faces-redirect=true";
    }
    
	
}
