package com.teste.dao.lazy;

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
import lombok.extern.log4j.Log4j;

@Log4j
public class LazyOcorrencia extends LazyDataModel<Ocorrencia> {
	
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
		
		log.info(filterBy);
		log.info("LAZY MODEL LOAD EXECUTDADO");
		
		log.info("Filtros recebidos: {}" +  filterBy);
		
		String descricaoFiltro = null;
	    
	    // Verificando se o filtro da descrição foi passado
	    if (filterBy.containsKey("descricao")) {
	        descricaoFiltro = (String) filterBy.get("descricao").getFilterValue();
	        this.ocorrencias = this.ocorrenciaDAO.buscarPorDescricao(first, pageSize, descricaoFiltro);
	        this.setRowCount(this.ocorrencias.size());
	        return ocorrencias;
	    }
	    
	    
		
		if (sortBy.isEmpty()) {
            sortBy.put("dataModificacao", SortMeta.builder()
                .field("dataModificacao")
                .order(SortOrder.DESCENDING)
                .build());
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
