package gaian.svsa.ep.model;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import gaian.svsa.ep.model.enums.StatusOcorrencia;
import gaian.svsa.ep.model.enums.TipoOcorrencia;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@ToString
@Getter
@Setter
@Entity
@Table(name="painel")
public class PainelFuncionarios implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Long codigo;
	
	@NotNull(message="O TIPO DE OCORRÊNCIA É OBRIGATÓRIO")
	@Enumerated(EnumType.STRING)
	private TipoOcorrencia tipo;
	
	@NotNull(message="O STATUS DA OCORRENCIA É OBRIGATÓRIO")
	@Enumerated(EnumType.STRING)
	private StatusOcorrencia status;
	
	@Column(length = 512000, columnDefinition="Text")
	@Basic(fetch=FetchType.LAZY)
	@NotNull(message="POR FAVOR, PREENCHA A DESCRIÇÃO")
	private String descricao;
	//mudar para tipo text
	
	@ManyToOne
	private UsuarioEP remetente;
	
	//@NotBlank(message="Por favor adione um destinatario")
	@ManyToOne
	private UsuarioEP destinatario;
	
	//@NotBlank(message="Por favor adione uma testemunha")
	@ManyToOne
	private UsuarioEP testemunha;
	
	//@ManyToOne
	private Long unidade;
	
	//@ManyToOne
	private Long tenant;
	
	/*
	 * Datas de Criação e Modificação
	 */
	@Temporal(TemporalType.TIMESTAMP)
	private Date dataCriacao;	
	@Temporal(TemporalType.TIMESTAMP)
	private Date dataModificacao;

	@PrePersist
	@PreUpdate
	public void configuraDatasCriacaoAlteracao() {
		this.setDataModificacao( new Date() );
				
		if (this.getDataCriacao() == null) {
			this.setDataCriacao( new Date() );
		}		
	}
	
}
