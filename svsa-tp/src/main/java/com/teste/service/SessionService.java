package com.teste.service;

import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;


import com.teste.dao.UsuarioDAO;
import com.teste.util.jpa.CriptografiaUtil;

import java.io.IOException;
import java.io.Serializable;

import lombok.*;
import lombok.extern.log4j.Log4j;

@Log4j
@Getter
@Setter
public class SessionService implements Serializable {
	private static final long serialVersionUID = 1L;

	private String sessionData;

	@Inject
	private UsuarioDAO usuarioDAO;

	public void init() throws IOException {
		log.info("Iniciando a aplicação e criando a sessão");
	}

	
	public Long getCookie(HttpServletRequest request) {
		Cookie[] cookies = request.getCookies();
		String usuarioId = null;

		if (cookies != null) {
			for (Cookie cookie : cookies) {
				if ("SESSIONID".equals(cookie.getName())) {

					try {
						usuarioId = CriptografiaUtil.descriptografar(cookie.getValue());

					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}

					break;
				}
			}

		}
		if(usuarioId != null) {
			return Long.parseLong(usuarioId);
		}
		
		return null;
	}



	   public HttpServletRequest getRequest() {
			
		   HttpServletRequest request = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext()
					.getRequest();
			
			return request;
		}

}
