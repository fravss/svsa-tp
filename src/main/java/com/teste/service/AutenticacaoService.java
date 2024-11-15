package com.teste.service;

import javax.inject.Inject;

import org.primefaces.model.menu.MenuModel;

import com.teste.model.UsuarioEP;
import com.teste.util.CriptografiaUtil;

import java.io.Serializable;

import lombok.*;
import lombok.extern.log4j.Log4j;

@Log4j
@Getter
@Setter
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
