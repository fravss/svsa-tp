package gaian.svsa.ep.converter;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;

import gaian.svsa.ep.dao.OcorrenciaDAO;
import gaian.svsa.ep.model.Ocorrencia;
import gaian.svsa.ep.service.OcorrenciaService;

@FacesConverter(forClass=Ocorrencia.class)
public class OcorrenciaConverter implements Converter<Object> {
	
	private OcorrenciaDAO ocorrenciaDAO;
	
	public OcorrenciaConverter() {
		this.ocorrenciaDAO = OcorrenciaService.getBean(OcorrenciaDAO.class);
	}
	
	@Override   
	public Object getAsObject(FacesContext context, UIComponent component, String value) {
		Ocorrencia retorno = null;

		if (value != null) {
			retorno = this.ocorrenciaDAO.buscarPeloCodigo(Long.parseLong(value));
		}

		return retorno;
	}

	@Override  
	public String getAsString(FacesContext context, UIComponent component, Object value) {
		if (value != null) {
			Long codigo = ((Ocorrencia) value).getCodigo();
			String retorno = (codigo == null ? null : codigo.toString());
			
			return retorno;
		}
		
		return "";
	}

}