package gaian.svsa.ep.model;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
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
@Table(name="ocorrencia")
@NamedQueries({
	@NamedQuery(name="Ocorrencia.buscarTodos", query="select o from Ocorrencia o"),
    @NamedQuery(
        name = "Ocorrencia.buscarPendencias",
        query = "select o from Ocorrencia o "
                + "where o.destinatario = :usuario " // se a ocorrencia se destinar a ele, ** se ele escreveu a ocorrencia ele vai acompanhar ela na tabela do crud
                + "or (o.unidade = :unidade and :grupo = ('COORDENADORES'))"  // se ele for o cordenador da ocorrenica, ele deve ver todas as fases
                + "or (o.status in ('GESTOR', 'FECHADO') and  :grupo = ('GESTORES'))"), // o gestor só seria depois de passar pelo coordenador
    @NamedQuery(name="Ocorrencia.buscarOcorrenciasGestor", query="select o from Ocorrencia o "
	         + "where o.dataCriacao between :ini and :fim "
	         + "order by o.dataCriacao"),
    @NamedQuery(name="Ocorrencia.buscarOcorrenciasGestorStatus", query="select o from Ocorrencia o "
    		 + "where o.tipo = :tipo "	
	         + "and o.dataCriacao between :ini and :fim "
	         + "order by o.dataCriacao"),
    @NamedQuery(name="Ocorrencia.buscarOcorrenciaStatusPeriodo", query="select o from Ocorrencia o "
			+ "where o.unidade = :unidade "
			+ "and o.dataCriacao between :ini and :fim "
			+ "order by o.status"),
	@NamedQuery(name="Ocorrencia.buscarOcorrenciaStatus", query="select o from Ocorrencia o "
			+ "where o.unidade = :unidade "
			+ "order by o.status")
                
    
})


public class Ocorrencia implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Long codigo;
	
	@NotNull(message = "O TIPO DE OCORRÊNCIA É OBRIGATÓRIO")
	@Enumerated(EnumType.STRING)
	private TipoOcorrencia tipo;
	
	@NotNull(message = "O STATUS É OBRIGATÓRIO")
	@Enumerated(EnumType.STRING)
	private StatusOcorrencia status;
	
	@NotNull(message = "POR FAVOR, PREENCHA A DESCRIÇÃO")
	private String descricao;
	
	
	@NotNull(message = "O REMETENTE É OBRIGATÓRIO")
	@JoinColumn(name="codigo_remetente")
	@ManyToOne
	private UsuarioEP remetente;
	
	@JoinColumn(name="codigo_destinatario")
	@ManyToOne
	private UsuarioEP destinatario;
	
	@JoinColumn(name="codigo_unidade")
	@ManyToOne
	private UnidadeEP unidade;
	
	
	
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