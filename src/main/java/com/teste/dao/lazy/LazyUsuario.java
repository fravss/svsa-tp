package com.teste.dao.lazy;

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
import org.primefaces.model.FilterMeta;
import org.primefaces.model.LazyDataModel;
import org.primefaces.model.SortMeta;
import org.primefaces.model.SortOrder;
import org.primefaces.model.filter.FilterConstraint;
import org.primefaces.util.LocaleUtils;

import com.teste.dao.OcorrenciaDAO;
import com.teste.dao.UsuarioDAO;
import com.teste.model.Ocorrencia;
import com.teste.model.UsuarioEP;
import com.teste.service.OcorrenciaService;
import com.teste.service.UsuarioService;
import com.teste.util.ShowcaseUtil;
import com.teste.dao.lazy.LazySorter;

import lombok.Getter;
import lombok.Setter;

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


	/*
	@Override
    public Usuario getRowData(String rowKey) {
        for (Usuario usuario : usuarios) {
            if (usuario.getCodigo() == Integer.parseInt(rowKey)) {
                return usuario;
            }
        }

        return null;
    }

    @Override
    public String getRowKey(Usuario usuario) {
        return String.valueOf(usuario.getCodigo());
    }

    @Override
    public int count(Map<String, FilterMeta> filterBy) {
        return (int) usuarios.stream()
                .filter(o -> filter(FacesContext.getCurrentInstance(), filterBy.values(), o))
                .count();
    }

    @Override
    public List<Usuario> load(int offset, int pageSize, Map<String, SortMeta> sortBy, Map<String, FilterMeta> filterBy) {
        // apply offset & filters
    	// Certifique-se de que 'usuarios' seja uma lista inicializada corretamente antes de usar
    	usuarios = usuarios.stream()
    	                    .filter(o -> filter(FacesContext.getCurrentInstance(), filterBy.values(), o))
    	                    .collect(Collectors.toList());


        // sort
        if (!sortBy.isEmpty()) {
            List<Comparator<Usuario>> comparators = sortBy.values().stream()
                    .map(o -> new LazySorter(o.getField(), o.getOrder()))
                    .collect(Collectors.toList());
            Comparator<Usuario> cp = ComparatorUtils.chainedComparator(comparators); // from apache
            usuarios.sort(cp);
        }

        return usuarios.subList(offset, Math.min(offset + pageSize, usuarios.size()));
    }

    private boolean filter(FacesContext context, Collection<FilterMeta> filterBy, Object o) {
        boolean matching = true;

        for (FilterMeta filter : filterBy) {
            FilterConstraint constraint = filter.getConstraint();
            Object filterValue = filter.getFilterValue();

            try {
                Object columnValue = String.valueOf(ShowcaseUtil.getPropertyValueViaReflection(o, filter.getField()));
                matching = constraint.isMatching(context, columnValue, filterValue, LocaleUtils.getCurrentLocale());
            }
            catch (ReflectiveOperationException e) {
                matching = false;
            }

            if (!matching) {
                break;
            }
        }

        return matching;
    }*/

	
	@Override
	public List<UsuarioEP> load(int first, int pageSize, Map<String, SortMeta> sortBy,
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
