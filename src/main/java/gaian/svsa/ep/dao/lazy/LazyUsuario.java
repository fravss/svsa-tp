package gaian.svsa.ep.dao.lazy;

import java.beans.IntrospectionException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.faces.context.FacesContext;

import org.apache.commons.collections4.ComparatorUtils;
import org.primefaces.component.log.Log;
import org.primefaces.model.FilterMeta;
import org.primefaces.model.LazyDataModel;
import org.primefaces.model.SortMeta;
import org.primefaces.model.SortOrder;
import org.primefaces.model.filter.FilterConstraint;
import org.primefaces.util.LocaleUtils;

import gaian.svsa.ep.dao.UsuarioDAO;
import gaian.svsa.ep.model.UsuarioEP;
import gaian.svsa.ep.service.UsuarioService;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.log4j.Log4j;

@Log4j
@Getter
@Setter
public class LazyUsuario extends LazyDataModel<UsuarioEP> implements Serializable{
	
	private static final long serialVersionUID = 1L;
	
	List<UsuarioEP> usuarios = new ArrayList<UsuarioEP>();
	private UsuarioDAO usuarioDAO;
	
	
	// FILTROS
	private String colunaSelecionada;
    private Object valorFiltro;
	
	
	public LazyUsuario(UsuarioService usuarioService) {
		this.usuarioDAO = usuarioService.getUsuarioDAO();
		this.usuarios = new ArrayList<UsuarioEP>();
	}


	
	@Override
	public List<UsuarioEP> load(int first, int pageSize, Map<String, SortMeta> sortBy,
			Map<String, FilterMeta> filterBy) {
		
		/*if (sortBy.isEmpty()) {
            sortBy.put("dataModificacao", SortMeta.builder()
                .field("dataModificacao")
                .order(SortOrder.DESCENDING)
                .build());
        }*/
		
		/*
		// ADICIONAR FUNCAO PARA DEFINIR O FILTRO ENTRE AS OCORRENCIAS ABERTAS E FECHADAS
		if (this.colunaSelecionada != null && !this.colunaSelecionada.isEmpty()) {
			FilterMeta filterMeta = new FilterMeta();
	        filterMeta.setFilterValue(this.colunaSelecionada);
	        filterMeta.setFilterValue(this.valorFiltro);
	        filterBy.put(this.colunaSelecionada, filterMeta);
		}*/
		
		System.out.println(filterBy);
		
        this.usuarios = usuarioDAO.buscarUsuarios(first, pageSize, sortBy, filterBy);
        this.setRowCount(usuarioDAO.contarUsuarios(filterBy));

        return this.usuarios;
        
	}
	
	public List<UsuarioEP> buscarPorNome(String nome) {
		return usuarioDAO.buscarPorNome(nome);
	}

	@Override
	public int count(Map<String, FilterMeta> filterBy) {
		// TODO Auto-generated method stub
		return 0;
	}

	
	
	

}
