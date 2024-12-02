package gaian.svsa.ep.converter;

import javax.faces.convert.Converter;
import javax.faces.convert.ConverterException;
import javax.faces.convert.FacesConverter;
import javax.inject.Inject;

import gaian.svsa.ep.model.UsuarioEP;
import gaian.svsa.ep.service.UsuarioService;

import javax.faces.component.FacesComponent;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;

import lombok.extern.log4j.Log4j;

@Log4j
@FacesComponent
@FacesConverter(forClass = UsuarioEP.class, managed = true)
public class UsuarioConverter implements Converter<Object> {

    @Inject
    private UsuarioService usuarioService;

    @Override
    public Object getAsObject(FacesContext context, UIComponent component, String value) {
        if (value == null || value.isEmpty()) {
            return null;
        }
        try {
            // Buscar o objeto pelo ID
            return usuarioService.buscarPorId(Long.valueOf(value));
        } catch (NumberFormatException e) {
            throw new ConverterException("ID inválido: " + value, e);
        }
    }

    @Override
    public String getAsString(FacesContext context, UIComponent component, Object value) {
        if (value == null) {
            return "";
        }
        if (value instanceof UsuarioEP) {
            UsuarioEP usuario = (UsuarioEP) value;
            return usuario.getCodigo() != null ? usuario.getCodigo().toString() : "";
        } else if (value instanceof Long) {
            return value.toString(); // Se já for um ID (Long), apenas retorna como String
        } else {
            throw new ConverterException("Tipo não suportado: " + value.getClass().getName());
        }
    }
}