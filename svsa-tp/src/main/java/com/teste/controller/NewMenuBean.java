package com.teste.controller;

import javax.inject.Inject;
import javax.inject.Named;

import org.primefaces.model.menu.MenuModel;


import lombok.Getter;
import lombok.Setter;


import javax.faces.view.ViewScoped;
import java.io.Serializable;



@Getter
@Setter
@Named
@ViewScoped
public class NewMenuBean implements Serializable {

	private static final long serialVersionUID = 1L;
	
	@Inject
	private AutenticacaoBean loginBean;	
		
	public MenuModel getMenu() {
		return loginBean.getModeloMenu();
	}

}

