package com.teste.dao.lazy;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.primefaces.model.FilterMeta;
import org.primefaces.model.LazyDataModel;
import org.primefaces.model.SortMeta;

import com.teste.dao.OcorrenciaDAO;
import com.teste.model.Ocorrencia;
import com.teste.service.OcorrenciaService;

public class LazyOcorrencia extends LazyDataModel<Ocorrencia> implements Serializable{
	
	private static final long serialVersionUID = 1L;
	private Logger log = Logger.getLogger(LazyOcorrencia.class);
	
	List<Ocorrencia> ocorrencias = new ArrayList<Ocorrencia>();
	private OcorrenciaDAO ocorrenciaDAO;
	
	public LazyOcorrencia(OcorrenciaService ocorrenciaService) {
		this.ocorrenciaDAO = ocorrenciaService.getOcorrenciaDAO();
	}
	
	@Override
	public List<Ocorrencia> load(int first, int pageSize, Map<String, SortMeta> sortBy,
			Map<String, FilterMeta> filterBy) {
		
		log.info("Load Executado");
		
		String filtroCodigo = "";
		
		log.info(filtroCodigo);
		
		
		for (Map.Entry<String, FilterMeta> entrada : filterBy.entrySet()) { 
            try {
            	FilterMeta meta = entrada.getValue();                 
               
                if(meta.getField().equals("codigo")) { //1
                	filtroCodigo = (String)meta.getFilterValue();
                	log.debug("property : " + meta.getField() + "-" + (String)meta.getFilterValue());
                }
                else {
                	log.debug("NO FILTER");
                }
            } catch(Exception e) {
                e.printStackTrace();
            }
		}
		
		ocorrencias = new ArrayList<Ocorrencia>();
		int dataSize = 0;
		
		if (filtroCodigo == null || filtroCodigo.equals("")) {
		    ocorrencias = this.ocorrenciaDAO.buscarTodos();
		    log.debug(ocorrencias, null);
		    dataSize = ocorrencias.size();
		    if (dataSize == 0)
		    	dataSize = 1;
		    this.setRowCount(dataSize);
		}
		else {
			ocorrencias = this.ocorrenciaDAO.buscarTodos();
			dataSize = ocorrencias.size();
		}
		
		return ocorrencias;
	}

	@Override
	public int count(Map<String, FilterMeta> filterBy) {
		// TODO Auto-generated method stub
		return 0;
	}

	
	
	

}
