package gaian.svsa.ep.service;

import java.io.IOException;
import java.io.InputStream;

import javax.enterprise.context.RequestScoped;
import javax.faces.context.FacesContext;
import javax.inject.Named;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.primefaces.model.menu.DefaultMenuItem;
import org.primefaces.model.menu.DefaultSubMenu;
import org.primefaces.model.menu.MenuModel;
import org.primefaces.model.menu.DefaultMenuModel;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import gaian.svsa.ep.model.UsuarioEP;
import gaian.svsa.ep.model.enums.GrupoEP;

@Named
@RequestScoped
public class MenuService {

    NodeList subMenus = obterSubMenusDoXML();
    
    
	private NodeList obterSubMenusDoXML() {
    	InputStream inputStream = FacesContext.getCurrentInstance().getExternalContext().getResourceAsStream("/WEB-INF/template/menu.xml");
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder;
        NodeList subMenus = null;
		try {
			builder = factory.newDocumentBuilder();
			Document document;
			try {
				document = builder.parse(inputStream);
			    subMenus = document.getElementsByTagName("submenu");
			    return subMenus;
			} catch (SAXException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
				}
			 
		} catch (ParserConfigurationException e) {
			e.printStackTrace();
		}
		return subMenus;

    }


    public MenuModel montarMenu(UsuarioEP usuario) {
        MenuModel modelo = new DefaultMenuModel();


        for (int i = 0; i < subMenus.getLength(); i++) {
            Element subMenu = (Element) subMenus.item(i);

            String grupo = usuario.getGrupo().toString();
            if (temPermissao(grupo, subMenu)) {
            DefaultSubMenu novoSubMenu = DefaultSubMenu.builder()
                    .label(subMenu.getAttribute("label"))
                    .icon(subMenu.getAttribute("icon"))
                    .build();


            NodeList items = subMenu.getElementsByTagName("menuitem");
            for (int j = 0; j < items.getLength(); j++) {
                Element item = (Element) items.item(j);


                DefaultMenuItem menuItem = DefaultMenuItem.builder()
                        .value(item.getAttribute("value"))
                        .command(item.getAttribute("outcome")) 
                        .ajax(false)
                        .build();


                novoSubMenu.getElements().add(menuItem);
            }
    

            modelo.getElements().add(novoSubMenu);
            }   

        }
        
        DefaultMenuItem item = new DefaultMenuItem();
        item.setOutcome("/index.xhtml"); 
        item.setValue("Home");
        item.setIcon("pi pi-fw pi-home");
		modelo.getElements().add(item);
		
		item = new DefaultMenuItem();
		item.setOutcome("/restrito/ocorrencia/ManterOcorrencia.xhtml");
		item.setValue("Ocorrências");
		item.setIcon("pi pi-fw pi-book");
		modelo.getElements().add(item);
		
		if (usuario.getGrupo().equals(GrupoEP.GESTORES) || usuario.getGrupo().equals(GrupoEP.COORDENADORES)) {
			item = new DefaultMenuItem();
			item.setOutcome("/restrito/painel/PainelFuncionarios");
			item.setValue("Painel de funcionários ");
			item.setIcon("pi pi-fw pi-sitemap");
			modelo.getElements().add(item);
		}
		
		item = new DefaultMenuItem();
		item.setCommand("#{autenticacaoBean.sair}"); 
        item.setValue("Sair");
        item.setIcon("pi pi-fw pi-power-off");
		modelo.getElements().add(item);

        return modelo;
    }
    
    private static boolean temPermissao(String grupo, Element elemento) {
		NodeList filhos = elemento.getElementsByTagName("grupo");
		boolean temPermissao = false;

		for (int j = 0; j < filhos.getLength(); j++) {
			Element grupoSubmenu = (Element) filhos.item(j);
	

			if (grupoSubmenu.getAttribute("value").equals(grupo)){
				temPermissao = true;
				break;
			}
		}
		return temPermissao;
	}

}
