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
	
	private Ocorrencia novaOcorrencia;
	
	@Inject
	private OcorrenciaService ocorrenciaService;
	@Inject
	private UsuarioService usuarioService;
	
	@PostConstruct
    public void init() {
		log.info("Bean CadastroOcorrenciaBean inicializado.");
    	//this.lazyUsuario = new LazyUsuario(this.usuarioService);
		this.lazyDestinatario = new LazyUsuario(this.usuarioService);
		this.lazyTestemunha = new LazyUsuario(this.usuarioService);
    	this.novaOcorrencia = new Ocorrencia();
    	this.destinatario = new UsuarioEP();
    	this.testemunha = new UsuarioEP();
    	this.tipo = TipoOcorrencia.REUNIAO;
    	this.descricao = "";
    }
	
	
	public void criarOcorrencia() {
		
		if (this.destinatario == null
				|| this.testemunha == null
				|| this.tipo == null
				|| this.descricao == null)
			return;
			
		this.novaOcorrencia = new Ocorrencia();
		this.novaOcorrencia.setCodigo(null);
		this.novaOcorrencia.setDestinatario(this.destinatario);
		this.novaOcorrencia.setTestemunha(this.testemunha);
		this.novaOcorrencia.setTipo(this.tipo);
		this.novaOcorrencia.setDescricao(this.descricao);
		this.novaOcorrencia.setStatus(StatusOcorrencia.COORDENADOR);
		this.novaOcorrencia.setDataCriacao(new Date());
		

		this.novaOcorrencia.setRemetente(this.usuarioService.getUsuarioAutenticado());
		this.novaOcorrencia.setUnidade(this.novaOcorrencia.getRemetente().getUnidade());
		this.novaOcorrencia.setTenant(this.novaOcorrencia.getRemetente().getTenant());
		
		try {
			log.info(this.novaOcorrencia);
			this.ocorrenciaService.salvar(this.novaOcorrencia);
			log.info(this.novaOcorrencia);
		}
		catch (Exception e) {
			log.warn(e.getMessage());
		}
		
		this.destinatario = null;
		this.testemunha = null;
		this.tipo = null;
		this.descricao = null;
		
		
	}
	
	public void editarOcorrencia(Long id) {
        log.info("Editando ocorrência com ID: " + id);
        this.novaOcorrencia = ocorrenciaService.buscarPorId(id); // Busca ocorrência pelo ID
        this.destinatario = this.novaOcorrencia.getDestinatario();
        this.testemunha = this.novaOcorrencia.getTestemunha();
        this.tipo = this.novaOcorrencia.getTipo();
        this.descricao = this.novaOcorrencia.getDescricao();
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
