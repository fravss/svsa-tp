package gaian.svsa.ep.controller;

import java.io.IOException;

import java.io.Serializable;

import java.time.LocalDate;

import java.util.ArrayList;

import java.util.Arrays;

import java.util.Date;

import java.util.List;

import javax.annotation.PostConstruct;

import javax.faces.context.FacesContext;

import javax.faces.view.ViewScoped;

import javax.inject.Inject;
import javax.inject.Named;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import org.apache.log4j.Logger;

import lombok.Getter;
import lombok.Setter;

import com.itextpdf.io.source.ByteArrayOutputStream;

import gaian.svsa.ep.model.Ocorrencia;
import gaian.svsa.ep.model.UnidadeEP;
import gaian.svsa.ep.model.UsuarioEP;
import gaian.svsa.ep.model.enums.Ano;
import gaian.svsa.ep.model.enums.GrupoEP;
import gaian.svsa.ep.model.enums.Mes;
import gaian.svsa.ep.model.enums.StatusOcorrencia;
import gaian.svsa.ep.model.enums.TipoOcorrencia;
import gaian.svsa.ep.service.AutenticacaoService;
import gaian.svsa.ep.service.OcorrenciaPDFService;
import gaian.svsa.ep.service.OcorrenciaService;
import gaian.svsa.ep.util.DatasIniFim;
import gaian.svsa.ep.util.DateUtils;
import gaian.svsa.ep.util.MessageUtil;
import gaian.svsa.ep.util.NegocioException;

@Named
@Getter
@Setter
@ViewScoped
public class PesquisaOcorrenciaBean implements Serializable {

	private static final long serialVersionUID = 1L;

	private Logger log = Logger.getLogger(PesquisaOcorrenciaBean.class);

	private Long qdeTotal = 0L;

	private List<Ocorrencia> ocorrencias = new ArrayList<Ocorrencia>();

	private Ocorrencia ocorrenciaSelecionada;

	private List<String> anos = new ArrayList<>(Arrays.asList(Ano.getAnos()));

	private Integer ano;

	private List<TipoOcorrencia> tipos;

	private TipoOcorrencia tipo;

	private List<Mes> meses;

	private Mes mes;

	private UnidadeEP unidade;

	private DatasIniFim datasTO;

	private Date dataInicio;

	private Date dataFim;

	private boolean consultado = false;

	private boolean temPermissao = false;

	private boolean isGestor = false;

	@Inject
	private OcorrenciaService ocorrenciaService;

	@Inject
	private OcorrenciaPDFService ocorrenciaPDFService;

	@Inject
	private AutenticacaoBean	autenticacaoBean;

	@PostConstruct
	public void inicializar() {

		LocalDate data = LocalDate.now();

		setAno(data.getYear());

		setMes(Mes.porCodigo(data.getMonthValue()));

		anos = new ArrayList<>(Arrays.asList(Ano.getAnos()));

		meses = Arrays.asList(Mes.values());

		tipos = Arrays.asList(TipoOcorrencia.values());

		unidade = autenticacaoBean.getUsuario().getUnidade();

		temPermissao = isTemPermissao(); 
		System.out.println("Tem permissao selecionado para a pesquisa: " + temPermissao);


	}

	public void consultarOcorrencias() {

		datasTO = DateUtils.getDatasIniFim(getAno(), getMes());
		if(isGestor==true) {
			if(tipo==null)
				ocorrencias = ocorrenciaService.buscarOcorrenciasGestor(datasTO.getIni(), datasTO.getFim());
			else
				ocorrencias = ocorrenciaService.buscarOcorrenciasGestorStatus(datasTO.getIni(),datasTO.getFim(), tipo);
		}else {
			ocorrencias = ocorrenciaService.buscarOcorrenciaStatus(unidade, datasTO.getIni(), datasTO.getFim());
		}
		
		
		qdeTotal = (long) ocorrencias.size();
		consultado = true;
	}

	public boolean isTemPermissao() {
		if(autenticacaoBean.getUsuario().getGrupo() == GrupoEP.GESTORES) {
			this.isGestor=true;
			return true;		
		}else if (autenticacaoBean.getUsuario().getGrupo() == GrupoEP.COORDENADORES) {
			return true;
		}else {
			return false;
		}	
	}

	public void showPDFOcorrencia() {

		try {

			FacesContext context = FacesContext.getCurrentInstance();

			HttpServletResponse response = (HttpServletResponse) context.getExternalContext().getResponse();

			response.setContentType("application/pdf");

			response.setHeader("Content-disposition", "inline=filename=file.pdf");

			ByteArrayOutputStream baos = ocorrenciaPDFService.generateStream(ocorrencias);
			response.setHeader("Expires", "0");

			response.setHeader("Cache-Control", "must-revalidate, post-check=0, pre-check=0");

			response.setHeader("Pragma", "public");

			response.setContentType("application/pdf");

			response.setContentLength(baos.size());

			ServletOutputStream os = response.getOutputStream();

			baos.writeTo(os);

			os.flush();

			os.close();

			context.responseComplete();

		} catch (NegocioException ne) {

			ne.printStackTrace();

			MessageUtil.erro(ne.getMessage());

		} catch (IOException e) {

			e.printStackTrace();

			MessageUtil.erro("Problema na escrita do PDF.");

		} catch (Exception ex) {

			ex.printStackTrace();

			MessageUtil.erro("Problema na geração do PDF.");

		}

		log.info("PDF gerado!");

	}

}