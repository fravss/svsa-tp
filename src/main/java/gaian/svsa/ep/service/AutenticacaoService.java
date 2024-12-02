package gaian.svsa.ep.service;

import javax.enterprise.context.ApplicationScoped;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.primefaces.model.menu.MenuModel;

import gaian.svsa.ep.model.UsuarioEP;
import gaian.svsa.ep.util.CriptografiaUtil;

import java.io.Serializable;

import lombok.*;
import lombok.extern.log4j.Log4j;

@Log4j
@Getter
@Setter
@Named
@ApplicationScoped
public class AutenticacaoService implements Serializable {
private static final long serialVersionUID = 1L;

	
	@Inject
	private UsuarioService usuarioService;
	
	@Inject
	private MenuService menuService;
	
	public UsuarioEP autenticar(String idCriptografado) {
		
		try {
			Long usuarioId = Long.parseLong(CriptografiaUtil.descriptografar(idCriptografado));
			
			return this.buscarUsuario(usuarioId);
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		
		
		UsuarioEP usuario = null;
		return usuario;
	}
	
	private HttpSession getSession() {
		
		FacesContext context = FacesContext.getCurrentInstance();
		HttpServletRequest request = (HttpServletRequest) context.getExternalContext().getRequest();
		HttpSession session = request.getSession();
		
		return session;
	}
	
	public UsuarioEP getUsuarioAutenticado() {

		   try {
				HttpSession session = getSession();
				UsuarioEP usuarioLogado = (UsuarioEP)session.getAttribute("usuario");
				return usuarioLogado;
			} catch (Exception e) {
				e.printStackTrace();
				return null;
			} 
		   
	   }

	private UsuarioEP buscarUsuario (Long id) {
		if(id != null) {
			UsuarioEP usuario = usuarioService.buscarPorId(id);		
			
			if(usuario != null ) {			
				return usuario;	
			}
		}		
		return null;
			
	}
	   public MenuModel criarMenu(UsuarioEP usuario) {
	       return menuService.montarMenu(usuario);
		}
	   
	   
	   
}
