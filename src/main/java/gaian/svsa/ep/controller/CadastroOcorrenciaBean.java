package gaian.svsa.ep.controller;

import java.io.Serializable;
import java.sql.SQLIntegrityConstraintViolationException;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.enterprise.context.SessionScoped;
import javax.inject.Inject;
import javax.inject.Named;

import gaian.svsa.ep.dao.lazy.LazyUsuario;
import gaian.svsa.ep.model.Ocorrencia;
import gaian.svsa.ep.model.UsuarioEP;
import gaian.svsa.ep.model.enums.StatusOcorrencia;
import gaian.svsa.ep.model.enums.TipoOcorrencia;
import gaian.svsa.ep.service.OcorrenciaService;
import gaian.svsa.ep.service.UsuarioService;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.log4j.Log4j;

@Log4j
@Getter
@Setter
@Named
@SessionScoped
public class CadastroOcorrenciaBean implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	//private LazyUsuario lazyUsuario;
	private LazyUsuario lazyDestinatario;
	private LazyUsuario lazyTestemunha;
	

	private UsuarioEP destinatario;
	private UsuarioEP testemunha;
	private TipoOcorrencia tipo;
	private String descricao;
	
	private Ocorrencia ocorrencia;
	
	@Inject
	private OcorrenciaService service;
	@Inject
	private UsuarioService usuarioService;
	
	@PostConstruct
    public void init() {
		log.info("Bean CadastroOcorrenciaBean inicializado.");
    	//this.lazyUsuario = new LazyUsuario(this.usuarioService);
		this.lazyDestinatario = new LazyUsuario(this.usuarioService);
		this.lazyTestemunha = new LazyUsuario(this.usuarioService);
    	this.ocorrencia = new Ocorrencia();
    	this.destinatario = new UsuarioEP();
    	this.testemunha = new UsuarioEP();
    	this.tipo = TipoOcorrencia.REUNIAO;
    	this.descricao = "";
    }
	
	
	public void criarOcorrencia() throws SQLIntegrityConstraintViolationException {
	
		this.ocorrencia.setDestinatario(this.destinatario);
		this.ocorrencia.setTestemunha(this.testemunha);
		log.info(this.testemunha);
		this.ocorrencia.setTipo(this.tipo);
		this.ocorrencia.setDescricao(this.descricao);
		this.ocorrencia.setStatus(StatusOcorrencia.COORDENADOR);
		this.ocorrencia.setDataCriacao(new Date());
		

		this.ocorrencia.setRemetente(this.usuarioService.getUsuarioAutenticado());
		this.ocorrencia.setUnidade(this.ocorrencia.getRemetente().getUnidade());
		this.ocorrencia.setTenant(this.ocorrencia.getRemetente().getTenant());
		
		try {
			this.service.salvar(this.ocorrencia);
		}
		catch (Exception e) {
			log.warn(e.getMessage());
		}
		
		
	}
	
	public void editarOcorrencia(Long id) {
        log.info("Editando ocorrência com ID: " + id);
        this.ocorrencia = service.buscarPorId(id); // Busca ocorrência pelo ID
        this.destinatario = this.ocorrencia.getDestinatario();
        this.testemunha = this.ocorrencia.getTestemunha();
        this.tipo = this.ocorrencia.getTipo();
        this.descricao = this.ocorrencia.getDescricao();
    }
	
	public List<UsuarioEP> filtrarDestinatario(String query) {
		log.info(query);
		return lazyDestinatario.buscarPorNome(query);
	}
	
	public List<UsuarioEP> filtrarTestemunha(String query) {
		log.info(query);
		return lazyTestemunha.buscarPorNome(query);
	}
	
	public void onTipoChange() {
	    // Aqui você pode fazer algo, como validar ou ajustar outros campos
	    System.out.println("Tipo selecionado: " + this.tipo);
	    // Ou adicionar algum outro comportamento desejado
	}

	
	
	public List<TipoOcorrencia> getListaTipos() {
	    return Arrays.asList(TipoOcorrencia.values());
	}

	
	
	
	
}
