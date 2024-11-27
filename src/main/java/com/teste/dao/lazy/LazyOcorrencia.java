package com.teste.dao.lazy;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.primefaces.model.FilterMeta;
import org.primefaces.model.LazyDataModel;
import org.primefaces.model.SortMeta;
import org.primefaces.model.SortOrder;

import com.teste.dao.OcorrenciaDAO;
import com.teste.model.Ocorrencia;
import com.teste.service.OcorrenciaService;

import lombok.Getter;
import lombok.Setter;

public class LazyOcorrencia extends LazyDataModel<Ocorrencia> implements Serializable{
	
	private static final long serialVersionUID = 1L;
	
	List<Ocorrencia> ocorrencias = new ArrayList<Ocorrencia>();
	private OcorrenciaDAO ocorrenciaDAO;
	
	
	// FILTROS
	@Setter
	private String colunaSelecionada;
	@Getter
	@Setter
    private Object valorFiltro;
	
	
	public LazyOcorrencia(OcorrenciaService ocorrenciaService) {
		this.ocorrenciaDAO = ocorrenciaService.getOcorrenciaDAO();
	}

	
	@Override
	public List<Ocorrencia> load(int first, int pageSize, Map<String, SortMeta> sortBy,
			Map<String, FilterMeta> filterBy) {
		
		if (sortBy.isEmpty()) {
            sortBy.put("dataModificacao", SortMeta.builder()
                .field("dataModificacao")
                .order(SortOrder.DESCENDING)
                .build());
        }
		
		
		// ADICIONAR FUNCAO PARA DEFINIR O FILTRO ENTRE AS OCORRENCIAS ABERTAS E FECHADAS
		if (this.colunaSelecionada != null && !this.colunaSelecionada.isEmpty()) {
			FilterMeta filterMeta = new FilterMeta();
	        filterMeta.setFilterValue(this.colunaSelecionada);
	        filterMeta.setFilterValue(this.valorFiltro);
	        filterBy.put(this.colunaSelecionada, filterMeta);
		}
		
		System.out.println(filterBy);
		
        this.ocorrencias = ocorrenciaDAO.buscarOcorrencias(first, pageSize, sortBy, filterBy);
        this.setRowCount(ocorrenciaDAO.contarOcorrencias(filterBy));

        return this.ocorrencias;
        
	}

	@Override
	public int count(Map<String, FilterMeta> filterBy) {
		// TODO Auto-generated method stub
		return 0;
	}

	
	
	

}
