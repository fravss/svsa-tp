package gaian.svsa.ep.controller;

import java.io.Serializable;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

import org.primefaces.PrimeFaces;

import gaian.svsa.ep.model.Ocorrencia;
import gaian.svsa.ep.model.Resposta;
import gaian.svsa.ep.model.UsuarioEP;
import gaian.svsa.ep.model.enums.GrupoEP;
import gaian.svsa.ep.model.enums.StatusOcorrencia;
import gaian.svsa.ep.service.OcorrenciaService;
import gaian.svsa.ep.util.MessageUtil;
import lombok.*;
import lombok.extern.log4j.Log4j;

@Getter
@Setter
@Log4j
@Named
@ViewScoped
public class RespostaOcorrenciaBean implements Serializable {

    private static final long serialVersionUID = 1L;

    private Ocorrencia ocorrencia;
    private List<Ocorrencia> ocorrencias;
    private UsuarioEP usuarioLogado;
    private List<Resposta> respostas;
    private Resposta novaResposta = new Resposta();
    
    private int acao;

    @Inject
    private OcorrenciaService ocorrenciaService;
    @Inject
    private AutenticacaoBean autenticacaoBean;

    @PostConstruct
    public void inicializar() {
        this.usuarioLogado = this.autenticacaoBean.getUsuario();
        this.buscarPendencias();
    }

    public void carregarOcorrencia() {
        try {
            this.respostas = this.ocorrenciaService.buscarTodasRespostas(this.ocorrencia);
        } catch (Exception e) {
            log.error("Erro ao carregar as respostas na ocorrência com ID " + ocorrencia.getCodigo(), e);
            MessageUtil.erro("Não foi possível carregar as respostas. " + e.getMessage());
        }
    }

    public void salvarResposta() {
        try {
        	
        	log.warn(ocorrencia);
            this.novaResposta.setOcorrencia(ocorrencia);
            this.novaResposta.setUsuario(this.usuarioLogado);
            this.ocorrenciaService.novaResposta(novaResposta);
            
            this.carregarOcorrencia();
            this.limpar();
            
            if (usuarioLogado.getGrupo().toString().equals("GESTORES")) {
    
                PrimeFaces.current().executeScript("PF('confirmacaoDialog').show();");
            } else {
    
                PrimeFaces.current().ajax().update("ocorrenciaDetalhes");
                PrimeFaces.current().ajax().update("frmPesquisa:pendenciasTable");
            }

            MessageUtil.sucesso("Resposta salva com sucesso!");
        } catch (Exception e) {
            log.error("Erro ao salvar a resposta na ocorrência com ID " + ocorrencia.getCodigo(), e);
            MessageUtil.erro("Não foi possível salvar a resposta. " + e.getMessage());
        }
    }

    public void buscarPendencias() {
        try {
            this.ocorrencias = this.ocorrenciaService.buscarTodasPendencias(usuarioLogado);
            System.out.println(this.ocorrencias.isEmpty());
            System.out.println("A unidade: " + usuarioLogado.getUnidade());
            System.out.println("O Grupo: " + usuarioLogado.getGrupo());
        } catch (Exception e) {
            log.error("Erro ao buscar pendências para o usuário " + usuarioLogado.getNome(), e);
            MessageUtil.erro("Não foi possível carregar as pendências. " + e.getMessage());
        }
    }

    public void gerirOcorrencia() {
        try {
            if (acao == 1) {
                ocorrencia.setStatus(StatusOcorrencia.FECHADO);
                log.info("Fechando ocorrência com ID " + ocorrencia.getDescricao());
                this.ocorrenciaService.salvar(ocorrencia);
            } else if (acao == 0) {
                ocorrencia.setStatus(StatusOcorrencia.COORDENADOR);
                log.info("Enviando ocorrência com ID " + ocorrencia.getCodigo() + " de volta ao coordenador.");
                this.ocorrenciaService.salvar(ocorrencia);
            }

            PrimeFaces.current().ajax().update("frmPesquisa:pendenciasTable");

            MessageUtil.sucesso("Ocorrência gerida com sucesso!");
        } catch (Exception e) {
            log.error("Erro ao gerir a ocorrência com ID " + ocorrencia.getCodigo(), e);
            MessageUtil.erro("Não foi possível realizar a ação na ocorrência. " + e.getMessage());
        }
    }
    
    public boolean renderizarPainel() {
    	if (this.ocorrencia == null) /// ESSA CONDICAO PRECISA SER A PRIMEIRA, POIS QUANDO A PAGINA CARREGAR O VALOR SEMPRE VAI SER
    								/// NULO, E VAI DAR ERRO SEMPRE
    		return false;
    	
    	
    	if (this.usuarioLogado.getGrupo() == GrupoEP.COORDENADORES) {
    		if (this.ocorrencia.getStatus() == StatusOcorrencia.GESTOR)
    			return false;
    		return true;
    	}
    	if (this.usuarioLogado.getGrupo() == GrupoEP.GESTORES) {
    		if (this.ocorrencia.getStatus() == StatusOcorrencia.COORDENADOR)
    			return false;
    		return true;
    	}
    	if (this.ocorrencia.getStatus() == StatusOcorrencia.FECHADO)
    		return false;
    	if (this.ocorrenciaService.buscarRemetente(this.ocorrencia,this.usuarioLogado) != null)
    		return false;
    	return true;
    }
    
    public boolean isRemetente() {
    	return this.ocorrencia != null && !(this.ocorrencia.getRemetente() == this.usuarioLogado);
    }

    public void limpar() {
        this.novaResposta = new Resposta();
    }
}