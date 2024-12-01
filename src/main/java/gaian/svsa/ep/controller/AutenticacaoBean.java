package gaian.svsa.ep.controller;

import java.io.IOException;
import java.io.Serializable;

import org.primefaces.model.menu.MenuModel;

import gaian.svsa.ep.model.UsuarioEP;
import gaian.svsa.ep.service.AutenticacaoService;

import javax.enterprise.context.SessionScoped;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;

import javax.inject.Inject;
import javax.inject.Named;
import javax.persistence.NoResultException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

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
	
	private UsuarioEP usuario = null;	
	private MenuModel menu = null;
	
	
	public void autenticar() throws IOException {
		log.info("entrar com o usuário: ");
		
		FacesMessage message = null;       
		
		try {
			HttpSession session = getSession();
			UsuarioEP usuarioLogado = (UsuarioEP)session.getAttribute("usuario");
			if(usuarioLogado == null) {
				
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
						FacesContext context = FacesContext.getCurrentInstance();
						String baseUrl = context.getExternalContext().getRequestScheme() + "://" +
						                 context.getExternalContext().getRequestServerName() + ":" +
						                 context.getExternalContext().getRequestServerPort();
						String redirectUrl = baseUrl + "/svsa-ct/restricted/home/SvsaHome.xhtml";
						context.getExternalContext().redirect(redirectUrl);
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
					removerCookie();
					return cookie.getValue();
				}
			}
		}
		return null;
	}
	
	 public void removerCookie() {
		 HttpServletResponse response = (HttpServletResponse) FacesContext.getCurrentInstance().getExternalContext().getResponse();
         Cookie sessionCookie = new Cookie("SESSIONID", null);
         sessionCookie.setMaxAge(0);
         sessionCookie.setPath("/");
         sessionCookie.setHttpOnly(true);
         sessionCookie.setSecure(true);
       

       response.addCookie(sessionCookie);
   }
	public String sair() {
		log.info("Invalidando sessão");		
		FacesContext context = FacesContext.getCurrentInstance();
		context.getExternalContext().invalidateSession();	    
		
		try {
			 FacesContext.getCurrentInstance().getExternalContext().redirect("/svsa-ep/index.xhtml");
		} catch (IOException e) {
			e.printStackTrace();
		}
		return "/Home.xhtml";
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