package com.teste.service;

import java.io.Serializable;

import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import com.teste.dao.UsuarioDAO;
import com.teste.model.UsuarioEP;

import lombok.Getter;
import lombok.extern.log4j.Log4j;

@Log4j
public class UsuarioService implements Serializable {

	private static final long serialVersionUID = 1L;
	
	@Getter
	@Inject
	private UsuarioDAO usuarioDAO;
	
	public UsuarioEP buscarPorId(Long id) {

		log.info("Buscando usuario por Id");

		return this.usuarioDAO.buscarPeloCodigo(id);
	}

	public Object buscarPorNome(String value) {
		// TODO Auto-generated method stub
		return null;
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
	
	
}
