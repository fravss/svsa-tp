package com.teste.controller;

import java.io.IOException;
import java.io.Serializable;

import org.primefaces.model.menu.MenuModel;

import javax.enterprise.context.SessionScoped;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;

import javax.inject.Inject;
import javax.inject.Named;
import javax.persistence.NoResultException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import com.teste.model.Usuario;
import com.teste.service.AutenticacaoService;

import lombok.*;
import lombok.extern.log4j.Log4j;


@Getter
@Setter
@Log4j
@Named
@SessionScoped
public class AutenticacaoBean implements Serializable {
	
	private static final long serialVersionUID = 1L;
	

	@Inject
    private AutenticacaoService autenticacaoService;
	
	private Usuario usuario = null;	
	private MenuModel menu = null;
	
	
	public void autenticar() throws IOException {
		log.info("entrar com o usuário: ");
		
		FacesMessage message = null;       
		
		try {
			HttpSession session = getSession();
			Usuario usuarioLogado = (Usuario)session.getAttribute("usuario");
			if(usuarioLogado == null) {
				log.info("o usuario é null");
				
				HttpServletRequest request = this.getRequest();
				String idCriptografado = this.getCookie(request);
										
				this.usuario = this.autenticacaoService.autenticar(idCriptografado);				
					
					if(usuario != null) {			
						log.info("Bem vindo " + usuario.getNome() + "!");
						session.setAttribute("usuario", usuario);
						this.criarMenu();
					
					}
				
				 else {
					log.info("usuario não está logado, será redirecionado ");
					try {
						FacesContext.getCurrentInstance().getExternalContext()
								.redirect("http://localhost:8080/svsa-ct/restricted/home/SvsaHome.xhtml");
					} catch (IOException ioException) {
						log.error("Erro ao redirecionar: " + ioException.getMessage(), ioException);
					}
				}
			}
		} catch (NoResultException e) {
			e.printStackTrace();
			message = new FacesMessage(FacesMessage.SEVERITY_INFO, "Erro de autenticação ", "Verifique se está logado.");
			FacesContext.getCurrentInstance().addMessage(null, message);
		} 
		
	}
	
	public String getCookie(HttpServletRequest request) {
		Cookie[] cookies = request.getCookies();

		if (cookies != null) {
			for (Cookie cookie : cookies) {
				if ("SESSIONID".equals(cookie.getName())) {
					return cookie.getValue();
				}
			}
		}
		return null;
	}
	

 
   public HttpServletRequest getRequest() {	
	   HttpServletRequest request = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext()
				.getRequest();
		
		return request;
	}
   private HttpSession getSession() {
		
		FacesContext context = FacesContext.getCurrentInstance();
		HttpServletRequest request = (HttpServletRequest) context.getExternalContext().getRequest();
		HttpSession session = request.getSession();
		
		return session;
	}
  

   public void criarMenu() {
       this.menu = autenticacaoService.criarMenu(this.usuario);
	}
  

}
