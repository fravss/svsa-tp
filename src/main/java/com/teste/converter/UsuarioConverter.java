package com.teste.converter;

import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;
import javax.inject.Inject;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import com.teste.model.Usuario;
import com.teste.service.UsuarioService;

@FacesConverter(forClass = Usuario.class)
public class UsuarioConverter implements Converter {

    @Inject
    private UsuarioService usuarioService;  // Injeção de dependência do serviço para buscar o Usuario
    
    public Object getAsObject(FacesContext context, UIComponent component, Long value) {
        if (value != null && value != 0L) {
            // Lógica para converter o valor string para um objeto Usuario
            try {
                // Aqui você pode buscar o usuário por nome, id ou qualquer critério que se ajuste ao seu caso
                return usuarioService.buscarPorId(value);  // Método do seu service para buscar o usuario pelo nome
            } catch (Exception e) {
                // Trate a exceção caso o valor não consiga ser convertido
                e.printStackTrace();
            }
        }
        return null;  // Retorna null se não encontrar o usuário
    }


    public Object getAsObject(FacesContext context, UIComponent component, String value) {
        if (value != null && !value.isEmpty()) {
            // Lógica para converter o valor string para um objeto Usuario
            try {
                // Aqui você pode buscar o usuário por nome, id ou qualquer critério que se ajuste ao seu caso
                return usuarioService.buscarPorNome(value);  // Método do seu service para buscar o usuario pelo nome
            } catch (Exception e) {
                // Trate a exceção caso o valor não consiga ser convertido
                e.printStackTrace();
            }
        }
        return null;  // Retorna null se não encontrar o usuário
    }

    @Override
    public String getAsString(FacesContext context, UIComponent component, Object value) {
        if (value != null) {
            // Aqui você retorna o nome ou qualquer outra propriedade que você deseja exibir
            return ((Usuario) value).getNome();  // Retorna o nome do usuário
        }
        return "";  // Se o valor for null, retorna uma string vazia
    }
}
