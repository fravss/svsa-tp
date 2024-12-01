package com.teste.controller;

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

import com.teste.model.Ocorrencia;
import com.teste.model.UnidadeEP;
import com.teste.model.UsuarioEP;
import com.teste.model.enums.Ano;
import com.teste.model.enums.GrupoEP;
import com.teste.model.enums.Mes;
import com.teste.model.enums.StatusOcorrencia;
import com.teste.model.enums.TipoOcorrencia;
import com.teste.service.AutenticacaoService;
import com.teste.service.OcorrenciaPDFService;

import com.teste.service.OcorrenciaService;
import com.teste.util.DatasIniFim;
import com.teste.util.DateUtils;

import com.teste.util.MessageUtil;

import com.teste.util.NegocioException;

import lombok.Getter;
import lombok.Setter;

import com.itextpdf.io.source.ByteArrayOutputStream;

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
	
	private Long unidade;

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
		
		unidade = autenticacaoBean.getUsuario().getUnidade().getCodigo();
		
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
			ocorrencias = ocorrenciaService.buscarOcorrenciaStatus(unidade, datasTO.getIni(), datasTO.getFim(), autenticacaoBean.getUsuario().getUnidade().getTenant_id());
		}
		System.out.println("Tipo selecionado para a pesquisa: " + tipo);
		System.out.println("Gestor selecionado para a pesquisa: " + isGestor);
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

	public List<Ocorrencia> getocorrencias() {

		return ocorrencias;

	}

	public Ocorrencia getOcorrenciaSelecionada() {

		return ocorrenciaSelecionada;

	}

	public void setOcorrenciaSelecionada(Ocorrencia OcorrenciaSelecionada) {

		this.ocorrenciaSelecionada = OcorrenciaSelecionada;

	}

	public Integer getAno() {

		return ano;

	}

	public void setAno(Integer ano) {

		this.ano = ano;

	}

	public Mes getMes() {

		return mes;

	}

	public void setMes(Mes mes) {

		this.mes = mes;

	}

	public List<String> getAnos() {

		return anos;

	}

	public void setAnos(List<String> anos) {

		this.anos = anos;

	}

	public List<Mes> getMeses() {

		return meses;

	}

	public void setMeses(List<Mes> meses) {

		this.meses = meses;

	}

	public boolean isConsultado() {

		return consultado;

	}

	public void setConsultado(boolean consultado) {

		this.consultado = consultado;

	}
	
	public TipoOcorrencia getTipo() {
		return tipo;
	}
	public void setTipo(TipoOcorrencia tipo) {
		this.tipo = tipo;
	}
	
}