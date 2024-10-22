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
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.primefaces.PrimeFaces;


import com.teste.model.Usuario;
import com.teste.service.MenuService;
import com.teste.service.SessionService;
import com.teste.service.UsuarioService;

import lombok.*;
import lombok.extern.log4j.Log4j;


@Getter
@Setter
@Log4j
@Named
@SessionScoped
public class LoginSSOBean implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	@Inject
	private UsuarioService usuarioService;

	@Inject
    private SessionService sessionService;
	
	@Inject
	private MenuService montarMenu;
	
	private Usuario usuario = null;	
	private Long id = null;
	private boolean autenticado = false;
	private MenuModel modeloMenu = null;
	
	public void login() throws IOException {
		log.info("entrar: ");
		
		FacesMessage message = null; 
        boolean loggedIn = false;        
		
		try {
			
			HttpSession session = getSession(); 
			
			usuario = (Usuario)session.getAttribute("usuario");


			if(usuario == null) {
				
				HttpServletRequest request = this.sessionService.getRequest();
				
					this.id = sessionService.getCookie(request);
					
					usuario = isValidUser();				
					
					if(usuario != null) {
						
						log.info("Bem vindo " + usuario.getNome() + "!");	
						this.criarMenu();
						this.autenticado = true;
					
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
			message = new FacesMessage(FacesMessage.SEVERITY_INFO, "Erro de Login. Verifique seu e-mail. ", "Verifique seu e-mail.");
			FacesContext.getCurrentInstance().addMessage(null, message);
            PrimeFaces.current().ajax().addCallbackParam("loggedIn", loggedIn);
		} catch (Exception e) {
			loggedIn = false;
			message = new FacesMessage(FacesMessage.SEVERITY_INFO, "Erro de Login, tenant não encontrado! ", e.getMessage());
			FacesContext.getCurrentInstance().addMessage(null, message);
            PrimeFaces.current().ajax().addCallbackParam("loggedIn", loggedIn);
			e.printStackTrace();
		}
		
	}
	
	private Usuario isValidUser() {
		if(this.id != null) {
			usuario = usuarioService.buscarPorId(id);		
			
			if(usuario != null ) {			
				return usuario;	
			}
		}		
		return null;
			
	}


   private HttpSession getSession() {
		
		FacesContext context = FacesContext.getCurrentInstance();
		HttpServletRequest request = (HttpServletRequest) context.getExternalContext().getRequest();
		HttpSession session = request.getSession();
		
		return session;
	}
 

   public void criarMenu() {
	   MenuService montarMenu = new MenuService();
       this.modeloMenu = montarMenu.montarMenu(this.usuario);
	}


	

}
