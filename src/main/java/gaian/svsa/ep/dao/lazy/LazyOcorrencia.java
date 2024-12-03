package gaian.svsa.ep.dao.lazy;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.primefaces.model.FilterMeta;
import org.primefaces.model.LazyDataModel;
import org.primefaces.model.SortMeta;
import org.primefaces.model.SortOrder;

import gaian.svsa.ep.service.OcorrenciaService;
import gaian.svsa.ep.dao.OcorrenciaDAO;
import gaian.svsa.ep.model.Ocorrencia;
import gaian.svsa.ep.model.UsuarioEP;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.log4j.Log4j;

@Log4j
public class LazyOcorrencia extends LazyDataModel<Ocorrencia> {
	
	private static final long serialVersionUID = 1L;
	
	List<Ocorrencia> ocorrencias = new ArrayList<Ocorrencia>();
	private OcorrenciaDAO ocorrenciaDAO;
	
	
	private UsuarioEP usuario;
	
	// FILTROS
	@Setter
	private String colunaSelecionada;
	@Getter
	@Setter
    private Object valorFiltro;
	
	
	public LazyOcorrencia(OcorrenciaService ocorrenciaService, UsuarioEP usuario) {
		this.usuario = usuario;
		this.ocorrenciaDAO = ocorrenciaService.getOcorrenciaDAO();
	}

	
	@Override
	public List<Ocorrencia> load(int first, int pageSize, Map<String, SortMeta> sortBy,
			Map<String, FilterMeta> filterBy) {
		
		
		// Filtro para o grupo do usuário
		FilterMeta grupoMeta = new FilterMeta();
		grupoMeta.setFilterValue(usuario.getGrupo());
		filterBy.put("grupo", grupoMeta);

		// Filtro para o tenant do usuário
		FilterMeta tenantMeta = new FilterMeta();
		tenantMeta.setFilterValue(usuario.getTenant());
		filterBy.put("tenant", tenantMeta);

		// Filtro para o usuário
		FilterMeta usuarioMeta = new FilterMeta();
		usuarioMeta.setFilterValue(usuario);
		filterBy.put("usuario", usuarioMeta);

		// Filtro para a unidade do usuário
		FilterMeta unidadeMeta = new FilterMeta();
		unidadeMeta.setFilterValue(usuario.getUnidade());
		filterBy.put("unidade", unidadeMeta);
	    
		
		if (sortBy.isEmpty()) {
            sortBy.put("dataModificacao", SortMeta.builder()
                .field("dataModificacao")
                .order(SortOrder.DESCENDING)
                .build());
        }
		
		System.out.println(filterBy);
		
        this.ocorrencias = ocorrenciaDAO.buscarOcorrencias(first, pageSize, sortBy, filterBy);
        this.setRowCount(this.ocorrencias.size());

        return this.ocorrencias;
        
	}
	

	@Override
	public int count(Map<String, FilterMeta> filterBy) {
		// TODO Auto-generated method stub
		return 0;
	}

	
	
	

}
